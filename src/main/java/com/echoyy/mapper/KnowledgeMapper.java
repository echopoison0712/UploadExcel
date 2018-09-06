package com.echoyy.mapper;

import com.echoyy.pojo.Knowledge;

import java.util.List;

/**
 * @ClassName KnowledgeMapper
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午3:12
 * @Version 1.0
 */
public interface KnowledgeMapper {
    int add(Knowledge knowledge);

    void delete(int id);

    Knowledge get(int id);

    int update(Knowledge knowledge);

    List<Knowledge> list();
}
