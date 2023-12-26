package com.community.amkorea.global.Util.Jwt;

import static com.community.amkorea.global.exception.ErrorCode.EXPIRED_TOKEN;
import static com.community.amkorea.global.exception.ErrorCode.INVALID_TOKEN;
import static com.community.amkorea.global.exception.ErrorCode.UNSUPPORTED_TOKEN;
import static com.community.amkorea.global.exception.ErrorCode.WRONG_TYPE_TOKEN;

import com.community.amkorea.global.Util.Jwt.dto.TokenDto;
import com.community.amkorea.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.prefix}")
  private String tokenPrefix;

  @Value("${spring.jwt.header}")
  private String tokenHeader;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Value("${spring.jwt.access-token-expire-time}")
  private Long accessTokenExpiration;

  @Value("${spring.jwt.refresh-token-expire-time}")
  private Long refreshTokenExpiration;

  private final CustomUserDetailService customUserDetailService;

  public TokenDto generateToken(String email, String roleType) {
    Date now = new Date();

    Date accessTokenExpireTime = new Date(now.getTime() + accessTokenExpiration);
    Date refreshTokenExpireTime = new Date(now.getTime() + refreshTokenExpiration);

    String accessToken = Jwts.builder()
        .setIssuedAt(now)
        .setSubject("access-token")
        .claim("role", roleType)
        .claim("userId", email)
        .setExpiration(accessTokenExpireTime)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    String refreshToken = Jwts.builder()
        .setIssuedAt(now)
        .setSubject("refresh-token")
        .claim("role", roleType)
        .claim("userId", email)
        .setExpiration(refreshTokenExpireTime)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    return TokenDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpireTime(accessTokenExpireTime.getTime())
        .refreshTokenExpireTime(refreshTokenExpireTime.getTime())
        .build();
  }

  /**
   * http 헤더로부터 bearer 토큰을 가져온다.
   */
  public String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(this.tokenHeader);
    if (!ObjectUtils.isEmpty(token) && token.startsWith(this.tokenPrefix)) {
      return token.substring(this.tokenPrefix.length());
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Claims claims = this.parseClaims(token);
      return !claims.getExpiration().before(new Date());
    } catch (SecurityException | MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      throw new CustomException(INVALID_TOKEN);
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
      throw new CustomException(EXPIRED_TOKEN);
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
      throw new CustomException(UNSUPPORTED_TOKEN);
    } catch (IllegalArgumentException e) {
      log.error("JWT token is wrong type: {}", e.getMessage());
      throw new CustomException(WRONG_TYPE_TOKEN);
    }
  }

  /**
   * Token 복호화 및 예외 발생(토큰 만료, 시그니처 오류) 시에 Claim 객체가 만들어지지 않음.
   */
  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    String encoded = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    return Keys.hmacShaKeyFor(encoded.getBytes());
  }

  public Authentication getAuthentication(String token) {
    Claims claims = this.parseClaims(token);
    String userId = claims.get("userId").toString();

    CustomUserDetails customUserDetails =
        (CustomUserDetails) customUserDetailService.loadUserByUsername(userId);

    return new UsernamePasswordAuthenticationToken(customUserDetails,
        "", customUserDetails.getAuthorities());
  }

  public Long getExpireTime(String token) {
    return this.parseClaims(token).getExpiration().getTime();
  }
}

