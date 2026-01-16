package com.storyarc.service;

import com.storyarc.dto.NewsResult;
import com.storyarc.dto.TopicAnalysisResponse;
import com.storyarc.entity.Topic;
import com.storyarc.entity.TopicStatus;
import com.storyarc.entity.TopicUpdate;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.mapper.TopicUpdateMapper;
import com.storyarc.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private TopicMapper topicMapper;

    @Mock
    private TopicUpdateMapper topicUpdateMapper;

    @Mock
    private NewsSearchService newsSearchService;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    void testAddTopic() {
        Topic topic = Topic.builder().keyword("test").status(TopicStatus.ACTIVE).build();
        when(topicMapper.insert(any(Topic.class))).thenReturn(1);

        Topic result = topicService.addTopic(topic);

        assertEquals(topic, result);
        verify(topicMapper).insert(topic);
    }

    @Test
    void testGetAllTopics() {
        Topic topic = Topic.builder().keyword("test").status(TopicStatus.ACTIVE).build();
        when(topicMapper.selectList(null)).thenReturn(Collections.singletonList(topic));

        List<Topic> topics = topicService.getAllTopics();

        assertEquals(1, topics.size());
        assertEquals(topic, topics.get(0));
    }

    @Test
    void testCheckUpdates_Significant() {
        // Setup
        Topic topic = Topic.builder().id(1L).keyword("Java").currentSummary("Old").build();
        when(topicMapper.selectById(1L)).thenReturn(topic);

        List<NewsResult> news = List.of(new NewsResult("Title", "URL", "Snippet", LocalDateTime.now()));
        when(newsSearchService.searchNews("Java")).thenReturn(news);

        // Mock ChatClient Builder and Fluent API
        ChatClient chatClient = mock(ChatClient.class);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Correct types based on inspection
        ChatClient.ChatClientRequest requestSpec = mock(ChatClient.ChatClientRequest.class);
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(any(Consumer.class))).thenReturn(requestSpec);

        ChatClient.ChatClientRequest.CallResponseSpec callResponseSpec = mock(ChatClient.ChatClientRequest.CallResponseSpec.class);
        when(requestSpec.call()).thenReturn(callResponseSpec);

        TopicAnalysisResponse analysisResponse = new TopicAnalysisResponse(true, "Reason", "New Summary");
        when(callResponseSpec.entity(TopicAnalysisResponse.class)).thenReturn(analysisResponse);

        // Execute
        topicService.checkUpdates(1L);

        // Verify
        verify(topicUpdateMapper).insert(any(TopicUpdate.class));
        verify(topicMapper).updateById(topic);
        assertEquals("New Summary", topic.getCurrentSummary());
    }
}
