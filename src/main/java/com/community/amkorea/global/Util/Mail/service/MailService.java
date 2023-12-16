package com.community.amkorea.global.Util.Mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender javaMailSender;

  private static final int CODE_LENGTH = 6;

  public void sendAuthMail(String email) {
    String authKey = createRandomCode();

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

    try {
      mimeMessageHelper.setTo(email); // 메일 수신자
      mimeMessageHelper.setSubject("회원가입 이메일 인증코드입니다."); // 메일 제목

      String msgCode = "<div style='margin:20px;'>"
          + "<h1> 안녕하세요 'AmKorea' 입니다. </h1>"
          + "<br>"
          + "<p>아래 코드를 입력해주세요<p>"
          + "<br>"
          + "<p>감사합니다.<p>"
          + "<br>"
          + "<div align='center' style='border:1px solid black; font-family:verdana';>"
          + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
          + "<div style='font-size:130%'>"
          + "CODE : <strong>" + authKey + "</strong><div><br/> "
          + "</div>";
      
      mimeMessageHelper.setText(msgCode, true); // 메일 본문 내용, HTML 여부

    } catch (MessagingException e) {
      log.info("fail");
      throw new RuntimeException(e);
    }

    javaMailSender.send(mimeMessage);
  }

  private String createRandomCode() {
    Random random = new Random();
    StringBuilder buffer = new StringBuilder();

    while (buffer.length() < CODE_LENGTH) {
      buffer.append(random.nextInt(10));
    }

    return buffer.toString();
  }

}
