package com.storyarc.service;

import com.storyarc.dto.AnalysisResult;
import com.storyarc.dto.NewsResult;
import java.util.List;

public interface NewsAnalysisService {
    AnalysisResult analyze(String currentSummary, List<NewsResult> news);
}
