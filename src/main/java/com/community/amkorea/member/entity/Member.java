package com.community.amkorea.member.entity;

import com.community.amkorea.bookmark.entity.Bookmark;
import com.community.amkorea.global.entity.BaseEntity;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.entity.PostLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String phoneNumber;

  @Builder.Default
  private boolean emailAuth = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleType roleType;

  @Builder.Default
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> postLikes = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  public void changeEmailAuth() {
    this.emailAuth = true;
  }

  public void addPost(Post post) {
    this.posts.add(post);
    post.setMember(this);
  }

  public void removePost(Post post) {
    this.posts.remove(post);
    post.setMember(null);
  }

  public void addPostLike(PostLike postLike) {
    this.postLikes.add(postLike);
  }

  public void addBookmark(Bookmark bookmark) {
    this.bookmarks.add(bookmark);
  }
}
