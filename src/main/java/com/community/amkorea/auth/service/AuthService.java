package com.community.amkorea.auth.service;

import com.community.amkorea.auth.dto.LogoutDto;
import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.Util.Jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  SignUpDto signUp(SignUpDto request);

  TokenDto signIn(SignInDto request);

  void logout(LogoutDto request);

  TokenDto reissue(HttpServletRequest request);
}
