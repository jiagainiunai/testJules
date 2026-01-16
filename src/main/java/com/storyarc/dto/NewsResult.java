package com.storyarc.dto;

import java.time.LocalDateTime;

public record NewsResult(
    String title,
    String url,
    String snippet,
    LocalDateTime publishedAt
) {}
