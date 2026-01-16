package com.storyarc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TopicAnalysisResponse(
    @JsonProperty("is_significant") boolean isSignificant,
    String reason,
    @JsonProperty("new_summary") String newSummary
) {}
