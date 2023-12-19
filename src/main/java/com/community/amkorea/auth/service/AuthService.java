package com.community.amkorea.auth.service;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.Util.Jwt.TokenDto;

public interface AuthService {

  SignUpDto signUp(SignUpDto request);

  TokenDto signIn(SignInDto request);
}
