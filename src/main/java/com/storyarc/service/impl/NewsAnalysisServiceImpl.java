package com.storyarc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storyarc.dto.AnalysisResult;
import com.storyarc.dto.NewsResult;
import com.storyarc.service.NewsAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsAnalysisServiceImpl implements NewsAnalysisService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public AnalysisResult analyze(String currentSummary, List<NewsResult> news) {
        String newsString = news.stream()
                .map(n -> String.format("- %s (%s): %s", n.title(), n.publishDate(), n.snippet()))
                .collect(Collectors.joining("\n"));

        String promptText = String.format("""
            You are an editor. Compare the 'Current Summary' with the 'New News List'. Determine if there is a SUBSTANTIAL update. Return a JSON object: { "is_significant": boolean, "reason": string, "new_summary": string }.

            Current Summary:
            %s

            New News List:
            %s
            """, currentSummary != null ? currentSummary : "None", newsString);

        ChatClient chatClient = chatClientBuilder.build();
        String response = chatClient.prompt()
                .user(promptText)
                .call()
                .content();

        return parseResponse(response);
    }

    private AnalysisResult parseResponse(String response) {
        try {
            String json = response.trim();
            if (json.startsWith("```json")) {
                json = json.substring(7);
            } else if (json.startsWith("```")) {
                 json = json.substring(3);
            }
            if (json.endsWith("```")) {
                json = json.substring(0, json.length() - 3);
            }
            json = json.trim();
            return objectMapper.readValue(json, AnalysisResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response: " + response, e);
        }
    }
}
