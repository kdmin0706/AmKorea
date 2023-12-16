package com.community.amkorea.auth.controller;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.auth.service.AuthService;
import com.community.amkorea.global.Util.Mail.dto.SendMailRequest;
import com.community.amkorea.global.Util.Mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService memberService;
  private final MailService mailService;

  @PostMapping("/signUp")
  public ResponseEntity<?> signUpMember(@RequestBody SignUpDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(request));
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signInMember(@RequestBody SignInDto request) {
    memberService.signIn(request);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/signUp/certification")
  public ResponseEntity<?> sendSignupCertificationMail(
      @RequestBody SendMailRequest request
  ) {
    mailService.sendAuthMail(request.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body("인증 코드를 발송했습니다.");
  }
}
