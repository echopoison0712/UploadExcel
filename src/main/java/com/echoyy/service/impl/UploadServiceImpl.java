package com.echoyy.service.impl;

import com.echoyy.mapper.KnowledgeMapper;
import com.echoyy.pojo.Knowledge;
import com.echoyy.service.UploadService;
import com.echoyy.util.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName UploadServiceImpl
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午3:20
 * @Version 1.0
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Override
    public int add(Knowledge knowledge) {
        return knowledgeMapper.add(knowledge);
    }

    @Override
    public void delete(int id) {
        knowledgeMapper.delete(id);
    }

    @Override
    public Knowledge get(int id) {
        return knowledgeMapper.get(id);
    }

    @Override
    public int update(Knowledge knowledge) {
        return knowledgeMapper.update(knowledge);
    }

    @Override
    public List<Knowledge> list() {
        return knowledgeMapper.list();
    }

    @Override
    public boolean upload(String name, MultipartFile file) {
        boolean b = false;
        //创建处理EXCEL
        ReadExcel readExcel=new ReadExcel();
        //解析excel，获取客户信息集合。
        List<Knowledge> knowledgeList = readExcel.getExcelInfo(name ,file);

        if(knowledgeList != null){
            b = true;
        }

        //迭代添加客户信息（注：实际上这里也可以直接将customerList集合作为参数，在Mybatis的相应映射文件中使用foreach标签进行批量添加。）
        for(Knowledge knowledge:knowledgeList){
            knowledgeMapper.add(knowledge);
        }
        return b;
    }
}
