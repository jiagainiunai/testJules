package com.storyarc.service;

import com.storyarc.dto.NewsResult;
import java.util.List;

public interface NewsSearchService {
    List<NewsResult> search(String keyword);
}
