package com.storyarc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("topic_update")
public class TopicUpdate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long topicId;
    private String title;
    private String url;
    private String summary;
    private Boolean isSignificant;
    private LocalDateTime createdAt;
}
