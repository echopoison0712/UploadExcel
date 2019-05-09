package com.echoyy.service.impl;

import com.echoyy.mapper.KnowledgeMapper;
import com.echoyy.service.UploadService;
import com.echoyy.util.ReadExcelInformation;
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
    public boolean getInformation(String name, MultipartFile file){
        boolean b = false;
        //创建处理EXCEL
        ReadExcelInformation readExcel=new ReadExcelInformation();
        List<Information> informationList = readExcel.getExcelInfo(name ,file);

        if(informationList != null){
            b = true;
        }
/*        List<String> updatelist = new ArrayList<>();
        for(Information info : informationList){
            StringBuilder content = new StringBuilder();
            content.append("{\"studentId\":\""+info.getStudentId()+"\",");
            content.append("\"oldValue\":\""+info.getOldValue()+"\",");
            content.append("\"newValue\":\""+info.getNewValue()+"\",");
            content.append("\"person\":\"SYSTEM\"}");
            updatelist.add(content.toString());
        }
        System.out.println(updatelist);*/
        return b;
    }


}
