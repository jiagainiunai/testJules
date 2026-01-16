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
@TableName("topic")
public class Topic {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String keyword;
    private String currentSummary;
    private LocalDateTime lastCheckTime;
    private TopicStatus status;
}
