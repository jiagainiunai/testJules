package com.storyarc.service.impl;

import com.storyarc.dto.NewsResult;
import com.storyarc.service.NewsSearchService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnProperty(name = "storyarc.news.provider", havingValue = "mock", matchIfMissing = true)
public class MockNewsSearchService implements NewsSearchService {

    @Override
    public List<NewsResult> search(String keyword) {
        if ("SpaceX".equalsIgnoreCase(keyword)) {
            return List.of(new NewsResult(
                "Starship launch successful",
                "https://example.com/spacex-starship",
                "SpaceX has successfully launched its Starship rocket.",
                LocalDateTime.now()
            ));
        }
        return List.of(new NewsResult(
            "News about " + keyword,
            "https://example.com/news/" + keyword.replaceAll("\\s+", "-"),
            "This is a mock news snippet for " + keyword,
            LocalDateTime.now()
        ));
    }
}
