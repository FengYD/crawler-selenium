package com.feng.crawlerselenium.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.crawlerselenium.base.domain.SysTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fengyadong
 * @date 2023/6/19 10:12
 * @Description
 */
@Mapper
public interface SysTaskMapper extends BaseMapper<SysTask> {

    /**
     * 根据任务名称查询任务
     *
     * @param taskName 任务名称
     */
    default SysTask selectOneByTaskName(String taskName) {
        LambdaQueryWrapper<SysTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTask::getTaskName, taskName);
        return selectOne(wrapper);
    }

}
