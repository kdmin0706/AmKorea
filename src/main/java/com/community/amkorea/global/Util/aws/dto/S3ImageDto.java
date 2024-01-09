package com.community.amkorea.global.Util.aws.dto;

import com.community.amkorea.global.Util.aws.entity.Image;

public record S3ImageDto(
    String url,
    String fileName,
    Long size
) {
  public Image toEntity() {
    return Image.builder()
        .url(url)
        .fileName(fileName)
        .size(size)
        .build();
  }
}
