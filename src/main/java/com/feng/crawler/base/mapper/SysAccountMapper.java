package com.feng.crawler.base.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.crawler.base.domain.SysAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/19 10:12
 * @Description
 */
@Mapper
public interface SysAccountMapper extends BaseMapper<SysAccount> {

    /**
     * 根据平台名称查询账户
     *
     * @param platform 平台名称
     */
    default List<SysAccount> selectListByPlatform(String platform) {
        LambdaQueryWrapper<SysAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAccount::getPlatform, platform);
        return selectList(wrapper);
    }

}
