package com.community.amkorea.auth.service;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;

public interface AuthService {

  SignUpDto signUp(SignUpDto request);

  void signIn(SignInDto request);
}
