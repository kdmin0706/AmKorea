package com.community.amkorea.soccer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Team implements Persistable<String> {

  @Id
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String venue;

  @Column(nullable = false)
  private String coach;

  @Transient
  protected boolean isNew = true;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  public void setIsNewStatus(boolean isNew) {
    this.isNew = isNew;
  }

}
