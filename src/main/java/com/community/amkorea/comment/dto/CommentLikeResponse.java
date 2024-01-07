package com.community.amkorea.comment.dto;

import lombok.Builder;

@Builder
public record CommentLikeResponse(
    String username,
    Long commentId,
    boolean isLike
) {

}
