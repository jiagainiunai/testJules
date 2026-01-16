package com.storyarc.service;

import com.storyarc.dto.NewsResult;
import com.storyarc.entity.Topic;
import com.storyarc.entity.TopicStatus;
import com.storyarc.mapper.TopicMapper;
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
    private NewsSearchService newsSearchService;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    void testCheckUpdates() {
        Long topicId = 1L;
        Topic topic = Topic.builder().id(topicId).keyword("SpaceX").build();
        NewsResult newsResult = new NewsResult("Title", "URL", "Snippet", LocalDateTime.now());

        when(topicMapper.selectById(topicId)).thenReturn(topic);
        when(newsSearchService.search("SpaceX")).thenReturn(List.of(newsResult));

        topicService.checkUpdates(topicId);

        verify(topicMapper).selectById(topicId);
        verify(newsSearchService).search("SpaceX");
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
