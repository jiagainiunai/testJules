package com.storyarc.service.impl;

import com.storyarc.dto.NewsResult;
import com.storyarc.dto.TopicAnalysisResponse;
import com.storyarc.entity.Topic;
import com.storyarc.entity.TopicUpdate;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.mapper.TopicUpdateMapper;
import com.storyarc.service.NewsSearchService;
import com.storyarc.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;
    private final TopicUpdateMapper topicUpdateMapper;
    private final NewsSearchService newsSearchService;
    private final ChatClient.Builder chatClientBuilder;

    @Override
    public Topic addTopic(Topic topic) {
        topicMapper.insert(topic);
        return topic;
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicMapper.selectList(null);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicMapper.selectById(id);
    }

    @Override
    public void checkUpdates(Long topicId) {
        Topic topic = getTopicById(topicId);
        if (topic == null) {
            return;
        }

        List<NewsResult> newsResults = newsSearchService.searchNews(topic.getKeyword());
        if (newsResults == null || newsResults.isEmpty()) {
            return;
        }

        String currentSummary = topic.getCurrentSummary() != null ? topic.getCurrentSummary() : "No summary yet.";
        String newNews = newsResults.stream()
                .map(n -> String.format("Title: %s\nSnippet: %s", n.title(), n.snippet()))
                .collect(Collectors.joining("\n\n"));

        ChatClient chatClient = chatClientBuilder.build();

        TopicAnalysisResponse response = chatClient.prompt()
                .system("You are a news analyst. Compare the 'Current State' with the 'New News'. Return JSON: { \"is_significant\": boolean, \"reason\": string, \"new_summary\": string }.")
                .user(u -> u.text("Current State:\n{currentState}\n\nNew News:\n{newNews}")
                        .param("currentState", currentSummary)
                        .param("newNews", newNews))
                .call()
                .entity(TopicAnalysisResponse.class);

        if (response != null && response.isSignificant()) {
            TopicUpdate update = TopicUpdate.builder()
                    .topicId(topic.getId())
                    .title("Update for " + topic.getKeyword())
                    .summary(response.newSummary())
                    .isSignificant(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            topicUpdateMapper.insert(update);

            topic.setCurrentSummary(response.newSummary());
            topic.setLastCheckTime(LocalDateTime.now());
            topicMapper.updateById(topic);
        } else {
             topic.setLastCheckTime(LocalDateTime.now());
             topicMapper.updateById(topic);
        }
    }
}
