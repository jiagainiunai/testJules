package com.storyarc.service.impl;

import com.storyarc.dto.AnalysisResult;
import com.storyarc.dto.NewsResult;
import com.storyarc.entity.Topic;
import com.storyarc.entity.TopicUpdate;
import com.storyarc.mapper.TopicMapper;
import com.storyarc.mapper.TopicUpdateMapper;
import com.storyarc.service.NewsAnalysisService;
import com.storyarc.service.NewsSearchService;
import com.storyarc.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;
    private final TopicUpdateMapper topicUpdateMapper;
    private final NewsSearchService newsSearchService;
    private final NewsAnalysisService newsAnalysisService;

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

            AnalysisResult analysis = newsAnalysisService.analyze(topic.getCurrentSummary(), results);

            if (analysis.isSignificant()) {
                TopicUpdate update = TopicUpdate.builder()
                        .topicId(topic.getId())
                        .title(analysis.reason())
                        .summary(analysis.newSummary())
                        .isSignificant(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                topicUpdateMapper.insert(update);

                topic.setCurrentSummary(analysis.newSummary());
            }

            topic.setLastCheckTime(LocalDateTime.now());
            topicMapper.updateById(topic);
        }
    }
}
