package com.storyarc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storyarc.dto.AnalysisResult;
import com.storyarc.dto.NewsResult;
import com.storyarc.service.impl.NewsAnalysisServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsAnalysisServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private NewsAnalysisServiceImpl newsAnalysisService;

    @Test
    void testAnalyze_SignificantUpdate() {
        // Setup mocks
        when(chatClientBuilder.build()).thenReturn(chatClient);

        String jsonResponse = """
            ```json
            {
                "is_significant": true,
                "reason": "Major breakthrough found.",
                "new_summary": "Scientists discovered a new particle."
            }
            ```
            """;
        when(chatClient.prompt().user(anyString()).call().content()).thenReturn(jsonResponse);

        newsAnalysisService = new NewsAnalysisServiceImpl(chatClientBuilder, new ObjectMapper());

        // Test data
        List<NewsResult> news = List.of(
            new NewsResult("New Particle", "http://example.com", "Discovery...", LocalDateTime.now())
        );

        // Execute
        AnalysisResult result = newsAnalysisService.analyze("Old summary", news);

        // Verify
        assertTrue(result.isSignificant());
        assertEquals("Major breakthrough found.", result.reason());
        assertEquals("Scientists discovered a new particle.", result.newSummary());
    }

    @Test
    void testAnalyze_NoUpdate_PlainJson() {
        // Setup mocks
        when(chatClientBuilder.build()).thenReturn(chatClient);

        String jsonResponse = """
            {
                "is_significant": false,
                "reason": "No changes.",
                "new_summary": "Old summary"
            }
            """;
        when(chatClient.prompt().user(anyString()).call().content()).thenReturn(jsonResponse);

        newsAnalysisService = new NewsAnalysisServiceImpl(chatClientBuilder, new ObjectMapper());

        // Test data
        List<NewsResult> news = List.of();

        // Execute
        AnalysisResult result = newsAnalysisService.analyze("Old summary", news);

        // Verify
        assertFalse(result.isSignificant());
        assertEquals("No changes.", result.reason());
        assertEquals("Old summary", result.newSummary());
    }
}
