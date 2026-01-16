package com.storyarc.service.impl;

import com.storyarc.dto.NewsResult;
import com.storyarc.entity.Topic;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.service.NewsSearchService;
import com.storyarc.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;
    private final NewsSearchService newsSearchService;

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
        Topic topic = topicMapper.selectById(topicId);
        if (topic != null && topic.getKeyword() != null) {
            List<NewsResult> results = newsSearchService.search(topic.getKeyword());
            // TODO: Process results (save to DB, notify user, etc.)
            System.out.println("Found " + results.size() + " updates for topic: " + topic.getKeyword());
        }
    }
}
