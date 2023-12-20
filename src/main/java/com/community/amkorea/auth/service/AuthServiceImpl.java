package com.community.amkorea.auth.service;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_VERITY;
import static com.community.amkorea.global.exception.ErrorCode.PASSWORD_NOT_MATCH;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.Util.Jwt.dto.TokenDto;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  private final TokenProvider tokenProvider;
  private final RedisService redisService;
  private static final String REFRESH_TOKEN_PREFIX = "RT: ";
  public static final long ACCESS_TOKEN_TTL = (1000 * 60) * 30; // 30분
  public static final long REFRESH_TOKEN_TTL = (60 * 1000) * 60 * 24 * 5; // 5일

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

  private TokenDto checkToken(Member member) {
    TokenDto tokenDto = tokenProvider.generateToken(member);
    saveRefreshToken(REFRESH_TOKEN_PREFIX + member.getEmail(), tokenDto.getRefreshToken());
    return tokenDto;
  }

  private void saveRefreshToken(String key, String refreshToken) {
    redisService.setDataExpire(key, refreshToken, REFRESH_TOKEN_TTL);
  }

}
