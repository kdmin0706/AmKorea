package com.community.amkorea.member.service;

import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberResponse;

public interface MemberService {
  MemberDto findMember(String username);

  UpdateMemberResponse updateMember(UpdateMemberResponse updateMemberDto, String username);

}
