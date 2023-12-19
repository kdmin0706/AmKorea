package com.community.amkorea.global.Util.Jwt;

import com.community.amkorea.global.Util.Jwt.dto.CustomUserDto;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(CustomUserDto memberDto) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(memberDto::getRoleType);
    return authorities;
  }

  @Override
  public String getPassword() {
    return memberDto.getPassword();
  }

  @Override
  public String getUsername() {
    return memberDto.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}