package com.community.amkorea.member.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.global.Util.Jwt.CustomUserDetails;
import com.community.amkorea.member.dto.UpdateMemberResponse;
import com.community.amkorea.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @GetMapping("/member")
  public ResponseEntity<?> findMemberInfo(@LoginUser CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.findMember(userDetails.getUsername()));
  }

  @PutMapping("/member")
  public ResponseEntity<?> updateMember(@Valid @RequestBody UpdateMemberResponse updateMemberDto,
      @LoginUser CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.updateMember(updateMemberDto, userDetails.getUsername()));
  }
}
