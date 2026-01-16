package com.storyarc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storyarc.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
