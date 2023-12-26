package com.community.amkorea.auth.service;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_VERITY;
import static com.community.amkorea.global.exception.ErrorCode.INVALID_TOKEN;
import static com.community.amkorea.global.exception.ErrorCode.PASSWORD_NOT_MATCH;

import com.community.amkorea.auth.dto.LogoutDto;
import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.Util.Jwt.dto.TokenDto;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  private final TokenProvider tokenProvider;
  private final RedisService redisService;

  private static final String REFRESH_TOKEN_PREFIX = "RT: ";

  @Override
  public SignUpDto signUp(SignUpDto request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_USER);
    }

    Member savedMember = memberRepository.save(Member.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .phoneNumber(request.getPhoneNumber())
            .nickname(request.getNickname())
            .roleType(RoleType.USER)
            .build());

    return SignUpDto.fromEntity(savedMember);
  }

  @Override
  public TokenDto signIn(SignInDto request) {
    Member member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new CustomException(PASSWORD_NOT_MATCH);
    }
    if (!member.isEmailAuth()) {
      throw new CustomException(EMAIL_NOT_VERITY);
    }

    return generateToken(member.getEmail(), member.getRoleType().getCode());
  }

  @Override
  @Transactional
  public void logout(LogoutDto request) {
    //1. 토큰 유효성 검증
    if (!tokenProvider.validateToken(request.getAccessToken())) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    //2. redis 에서 해당 access-token 정보로 저장된 refresh-token 확인
    if (redisService.getData(REFRESH_TOKEN_PREFIX + request.getAccessToken()) != null) {
      redisService.deleteData(REFRESH_TOKEN_PREFIX + request.getAccessToken());
    }

    //3. access-token 유효시간 확인 후에 BlackList 저장
    Long expireTime = tokenProvider.getExpireTime(request.getAccessToken());
    redisService.setDataExpire(request.getAccessToken(), "Logout", expireTime);
  }

  private TokenDto generateToken(String email, String roleType) {
    TokenDto tokenDto = tokenProvider.generateToken(email, roleType);
    redisService.setDataExpire(REFRESH_TOKEN_PREFIX + tokenDto.getAccessToken(),
        tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpireTime());
    return tokenDto;
  }

  /**
   * token 재발급
   */
  @Override
  public TokenDto reissue(HttpServletRequest request) {
    //1. HttpServletRequest 를 이용해서 redis 에 있는 refresh-token을 가져온다.
    String data = redisService.getData(request.getHeader("accessToken"));

    //2. refresh-token exception check(null or invalid)
    if (!StringUtils.hasText(data) || !data.equals(request.getHeader("refreshToken"))) {
      throw new CustomException(INVALID_TOKEN);
    }

    //3. token 정보를 이용해서 UserDetails 확인
    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("accessToken"));

    //4. 기존 토큰 정보 삭제
    redisService.deleteData(data);

    //5. 토큰 재발급 및 redis 업데이트
    return generateToken(authentication.getName(), getAuthorities(authentication));
  }


  /**
   * 권한 확인
   */
  private String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(Collectors.joining());
  }
}
