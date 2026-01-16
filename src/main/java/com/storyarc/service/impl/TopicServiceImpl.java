package com.storyarc.service.impl;

import com.storyarc.entity.Topic;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;

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
}
