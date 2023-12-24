package com.community.amkorea.member.service;

import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberDto;

public interface MemberService {
  MemberDto findMember(String username);

  UpdateMemberDto updateMember(UpdateMemberDto updateMemberDto, String username);

}
