package com.community.amkorea.auth.service;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_VERITY;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    return this.checkToken(member);
  }

  @Override
  @Transactional
  public void Logout(LogoutDto request) {
    //1. 토큰 유효성 검증
    if (!tokenProvider.validateToken(request.getAccessToken())) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    //2. access-token 이용한 user 정보 찾기
    Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

    //3. redis 에서 해당 유저 정보로 저장된 refresh-token 확인
    String rtToken = REFRESH_TOKEN_PREFIX + authentication.getPrincipal().toString();
    if (redisService.existData(rtToken)) {
      log.info("Refresh-Token 삭제");
      redisService.deleteData(rtToken);
    }

    //4. access-token 유효시간 확인 후에 BlackList 저장
    Long expireTime = tokenProvider.getExpireTime(request.getAccessToken());
    redisService.setDataExpire(request.getAccessToken(), "Logout", expireTime);
  }

  private TokenDto checkToken(Member member) {
    TokenDto tokenDto = tokenProvider.generateToken(member);
    redisService.setDataExpire(REFRESH_TOKEN_PREFIX + member.getEmail(),
        tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpireTime());
    return tokenDto;
  }

}
