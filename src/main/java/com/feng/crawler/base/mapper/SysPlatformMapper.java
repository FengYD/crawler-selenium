package com.feng.crawler.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.crawler.base.domain.SysPlatform;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fengyadong
 * @date 2023/6/19 10:11
 * @Description
 */
@Mapper
public interface SysPlatformMapper extends BaseMapper<SysPlatform> {

    /**
     * 根据平台名称查询平台
     *
     * @param platform 平台名称
     */
    default SysPlatform selectOneByPlatform(String platform) {
        LambdaQueryWrapper<SysPlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPlatform::getPlatform, platform);
        return selectOne(wrapper);
    }

}
