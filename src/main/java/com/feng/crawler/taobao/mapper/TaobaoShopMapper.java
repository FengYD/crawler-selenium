package com.feng.crawler.taobao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.crawler.taobao.domain.TaobaoShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/21 14:00
 * @Description
 */
@Mapper
public interface TaobaoShopMapper extends BaseMapper<TaobaoShop> {

    int insertBatch(List<TaobaoShop> shopList);

    @Select("select * from taobao_shop where id > #{geId} order by id limit #{size}")
    List<TaobaoShop> selectScopeOverId(long geId, int size);

    @Select("select * from taobao_shop where id > #{geId} and mod(id, #{modNum}) = #{num} order by id limit #{size}")
    List<TaobaoShop> selectScopeOverIdAndModId(long geId, int size, int modNum, int num);

}
