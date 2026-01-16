package com.storyarc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnalysisResult(
    @JsonProperty("is_significant") boolean isSignificant,
    @JsonProperty("reason") String reason,
    @JsonProperty("new_summary") String newSummary
) {}
