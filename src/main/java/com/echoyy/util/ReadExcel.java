package com.echoyy.util;

import com.echoyy.pojo.Knowledge;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ReadExcel
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午6:28
 * @Version 1.0
 */
public class ReadExcel {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;
    //构造方法
    public ReadExcel(){}
    //获取总行数
    public int getTotalRows()  { return totalRows;}
    //获取总列数
    public int getTotalCells() {  return totalCells;}
    //获取错误信息
    public String getErrorInfo() { return errorMsg; }

    /**
     * 验证EXCEL文件
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath){
        if (filePath == null || !(VersionUtil.isExcel2003(filePath) || VersionUtil.isExcel2007(filePath))){
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    /**
     * 读EXCEL文件，获取客户信息集合
     * @param fileName
     * @param Mfile
     * @return
     */
    public List<Knowledge> getExcelInfo(String fileName, MultipartFile Mfile){

        //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
        CommonsMultipartFile cf= (CommonsMultipartFile)Mfile; //获取本地存储路径
        File file = new  File("/Users/echoyy/tempExcel");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!file.exists()) file.mkdirs();
        //新建一个文件
        File file1 = new File("/Users/echoyy/tempExcel/" + new Date().getTime() + ".xls");
        //将上传的文件写入新建的文件中
        try {
            cf.getFileItem().write(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化客户信息的集合
        List<Knowledge> knowledgeList=new ArrayList<>();
        //初始化输入流
        InputStream is = null;
        try{
            //验证文件名是否合格
            if(!validateExcel(fileName)){
                return null;
            }
            //根据文件名判断文件是2003版本还是2007版本
            boolean isExcel2003 = true;
            if(VersionUtil.isExcel2007(fileName)){
                isExcel2003 = false;
            }
            //根据新建的文件实例化输入流
            is = new FileInputStream(file1);
            //根据excel里面的内容读取客户信息
            knowledgeList = getExcelInfo(is, isExcel2003);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            if(is !=null)
            {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return knowledgeList;
    }
    /**
     * 根据excel里面的内容读取客户信息
     * @param is 输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public  List<Knowledge> getExcelInfo(InputStream is,boolean isExcel2003){
        List<Knowledge> knowledgeList=null;
        try{
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            //当excel是2003时
            if(isExcel2003){
                wb = new HSSFWorkbook(is);
            }
            else{//当excel是2007时
                wb = new XSSFWorkbook(is);
            }
            //读取Excel里面客户的信息
            knowledgeList=readExcelValue(wb);
        }
        catch (IOException e)  {
            e.printStackTrace();
        }
        return knowledgeList;
    }
    /**
     * 读取Excel里面客户的信息
     * @param wb
     * @return
     */
    private List<Knowledge> readExcelValue(Workbook wb){
        // 得到第一个shell
        Sheet sheet=wb.getSheetAt(0);

        // 得到Excel的行数
        this.totalRows=sheet.getPhysicalNumberOfRows();

        // 得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){
            this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<Knowledge> knowledgeList=new ArrayList<>();
        Knowledge knowledge;
        // 循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            knowledge = new Knowledge();

            // 循环Excel的列
            for(int c = 0; c <this.totalCells; c++){
                String value =  row.getCell(c).getStringCellValue();
                if (null != value){
                    if(c==0){
                        knowledge.setClassify(value); // 分类
                    }else if(c==1){
                        knowledge.setField(value);  // 领域
                    }else if(c==2){
                        knowledge.setModule(value); // 模块
                    }else if(c==3){
                        knowledge.setProblem(value); // 问题
                    }else if(c==4){
                        knowledge.setAnswer(value); // 答案
                    }else if(c==5){
                        knowledge.setAskpsn(value); // 问题提出人
                    }else if (c==6){
                        knowledge.setAnswerpsn(value); // 问题解答人
                    }
                }
            }
            setDefaultValue(knowledge);
            //添加客户
            knowledgeList.add(knowledge);
        }
        return knowledgeList;
    }

    private void setDefaultValue(Knowledge knowledge){
        if(knowledge.getAskpsn() == null)
            knowledge.setAskpsn("系统管理员");
        if(knowledge.getAnswerpsn()==null)
            knowledge.setAnswerpsn("系统管理员");
        knowledge.setDr(0);
        knowledge.setModifypsn("系统管理员");
        knowledge.setScore(0);
        knowledge.setVersion(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(new Date());
        knowledge.setTmaketime(time);
        knowledge.setTmodifytime(time);
    }
}
