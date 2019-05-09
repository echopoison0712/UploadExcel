package com.echoyy.controller;

import com.echoyy.pojo.Knowledge;
import com.echoyy.service.UploadService;
import com.echoyy.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UploadController
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午3:22
 * @Version 1.0
 */
@Controller
@RequestMapping("")
public class UploadController {

    @Autowired
    UploadService service;

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public ModelAndView batchImport(@RequestParam(value="filename") MultipartFile file,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("AddController ..batchimport() start");
        ModelAndView mav = new ModelAndView();
        //判断文件是否为空
        if(file==null) return null;
        //获取文件名
        String name=file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(name==null || ("").equals(name) && size==0) return null;

        //批量导入。参数：文件名，文件。
        boolean b = service.getInformation(name,file);
        if(b){
            String Msg ="批量导入EXCEL成功！";
            request.getSession().setAttribute("msg",Msg);
        }else{
            String Msg ="批量导入EXCEL失败！";
            request.getSession().setAttribute("msg",Msg);
        }
        mav.setViewName("excelUpload");
        return mav;
    }

    @RequestMapping("upload")
    public ModelAndView uploadKnowledge(){

        ModelAndView mav = new ModelAndView("excelUpload");
        return mav;
    }
}
