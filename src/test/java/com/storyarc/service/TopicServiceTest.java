package com.storyarc.service;

import com.storyarc.dto.AnalysisResult;
import com.storyarc.dto.NewsResult;
import com.storyarc.entity.Topic;
import com.storyarc.entity.TopicStatus;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.mapper.TopicUpdateMapper;
import com.storyarc.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private NewsAnalysisService newsAnalysisService;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    void testCheckUpdates() {
        Long topicId = 1L;
        Topic topic = Topic.builder().id(topicId).keyword("SpaceX").currentSummary("Old Summary").build();
        NewsResult newsResult = new NewsResult("Title", "URL", "Snippet", LocalDateTime.now());
        List<NewsResult> newsResults = List.of(newsResult);
        AnalysisResult analysisResult = new AnalysisResult(false, "No update", "Old Summary");

        when(topicMapper.selectById(topicId)).thenReturn(topic);
        when(newsSearchService.search("SpaceX")).thenReturn(newsResults);
        when(newsAnalysisService.analyze("Old Summary", newsResults)).thenReturn(analysisResult);

        topicService.checkUpdates(topicId);

        verify(topicMapper).selectById(topicId);
        verify(newsSearchService).search("SpaceX");
        verify(newsAnalysisService).analyze("Old Summary", newsResults);
    }

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
}
