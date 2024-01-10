package com.community.amkorea.global.Util.Mail.service;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.INVALID_AUTH_CODE;
import static com.community.amkorea.global.exception.ErrorCode.USER_NOT_FOUND;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender javaMailSender;
  private final RedisService redisService;
  private final MemberRepository memberRepository;

  private static final int CODE_LENGTH = 6;

  //이메일 토큰 만료 기간 10분으로 설정
  private static final Long EMAIL_TOKEN_EXPIRATION = 600000L;

  private static final String EMAIL_PREFIX = "Email-Auth: ";

  @Async("mailExecutor")
  public void sendAuthMail(String email) {
    String code = createRandomCode();

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

    try {
      mimeMessageHelper.setTo(email); // 메일 수신자
      mimeMessageHelper.setSubject("AmKorea 회원가입 인증코드입니다."); // 메일 제목

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
          + "CODE : <strong>" + code + "</strong><div><br/> "
          + "</div>";

      mimeMessageHelper.setText(msgCode, true); // 메일 본문 내용, HTML 여부

    } catch (MessagingException e) {
      log.info("fail");
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    try {
      javaMailSender.send(mimeMessage);
      redisService.setDataExpire(EMAIL_PREFIX + email, code, EMAIL_TOKEN_EXPIRATION);
    } catch (MailException e) {
      log.error("Error sending email", e);
      throw new CustomException(ErrorCode.EMAIL_HAS_FAIL);
    }
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    if (!isVerify(email, code)) {
      throw new CustomException(INVALID_AUTH_CODE);
    }

    //멤버 이메일 인증 여부 변경
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    member.changeEmailAuth();
    memberRepository.save(member);

    redisService.deleteData(email);
  }

  private boolean isVerify(String email, String code) {
    String data = redisService.getData(EMAIL_PREFIX + email);
    if (data == null) {
      throw new CustomException(EMAIL_NOT_FOUND);
    }

    return data.equals(code);
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
