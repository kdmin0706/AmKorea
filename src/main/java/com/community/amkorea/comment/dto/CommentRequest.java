package com.community.amkorea.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
    @NotNull(message = "게시글 ID를 입력해주세요")
    Long postId,

    @NotBlank(message = "내용을 입력해주세요")
    String content
) {

}
