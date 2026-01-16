package com.storyarc.service;

import com.storyarc.entity.Topic;
import java.util.List;

public interface TopicService {
    Topic addTopic(Topic topic);
    List<Topic> getAllTopics();
    Topic getTopicById(Long id);
}
