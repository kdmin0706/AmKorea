package com.community.amkorea.post.entity;

import com.community.amkorea.comment.entity.Comment;
import com.community.amkorea.global.Util.aws.entity.Image;
import com.community.amkorea.global.entity.BaseEntity;
import com.community.amkorea.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private int views;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false)
  private int likeCount;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> postLikeList = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private PostCategory postCategory;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images = new ArrayList<>();

  public void updateLikeCount() {
    this.likeCount = this.postLikeList.size();
  }

  public void addPostLike(PostLike postLike) {
    this.postLikeList.add(postLike);
  }

  public void removePostLike(PostLike postLike) {
    this.postLikeList.remove(postLike);
  }

  public void addCategory(PostCategory postCategory) {
    this.postCategory = postCategory;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void addImage(Image image) {
    this.images.add(image);
    image.mappingPost(this);
  }
}
