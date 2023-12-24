package com.community.amkorea.member.controller;

import com.community.amkorea.global.Util.Jwt.CustomUserDetails;
import com.community.amkorea.member.dto.UpdateMemberDto;
import com.community.amkorea.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public ResponseEntity<?> findMemberInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.findMember(userDetails.getUsername()));
  }

  @PutMapping("/member")
  public ResponseEntity<?> updateMember(@RequestBody UpdateMemberDto updateMemberDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.updateMember(updateMemberDto, userDetails.getUsername()));
  }
}
