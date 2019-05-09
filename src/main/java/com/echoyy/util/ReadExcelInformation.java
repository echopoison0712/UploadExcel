package com.echoyy.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName ReadExcel
 * @Description
 * @Author echoyy
 * @Date 2018/8/29 下午6:28
 * @Version 1.0
 */
public class ReadExcelInformation {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;
    //构造方法
    public ReadExcelInformation(){}
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
    public List<Information> getExcelInfo(String fileName, MultipartFile Mfile){

        //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
        CommonsMultipartFile cf= (CommonsMultipartFile)Mfile; //获取本地存储路径
        File file = new  File("/Users/echoyy/tempExcel");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!file.exists()) file.mkdirs();
        //新建一个文件
        File file1 = new File("/Users/echoyy/tempExcel/" + System.currentTimeMillis() + ".xls");
        //将上传的文件写入新建的文件中
        try {
            cf.getFileItem().write(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化客户信息的集合
        List<Information> informationList=new ArrayList<>();
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
            informationList = getExcelInfo(is, isExcel2003);
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
        return informationList;
    }
    /**
     * 根据excel里面的内容读取客户信息
     * @param is 输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public  List<Information> getExcelInfo(InputStream is,boolean isExcel2003){
        List<Information> informationList=null;
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
            informationList=readExcelValue(wb);
        }
        catch (IOException e)  {
            e.printStackTrace();
        }
        return informationList;
    }
    /**
     * 读取Excel里面客户的信息
     * @param wb
     * @return
     */
    private List<Information> readExcelValue(Workbook wb) {
        DecimalFormat format = new DecimalFormat("#");
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);

        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();

        // 得到Excel的列数(前提是有行数)
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<Information> informationList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Information information;
        // 循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {continue;}
            information = new Information();

            // 循环Excel的列
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                String value = null;
                switch (cell.getCellType())
                {
                    case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                        value = format.format(cell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:// 字符串
                        value = format.format(Double.parseDouble(cell.toString().trim()));
                        break;
                    default:
                        value = cell.toString();
                        break;
                }

                if (!value.equals("0")) {
                    if (c == 0) {
                        if(list.contains(value)) {
                            System.out.println(value);
                        }else {
                            list.add(value);
                        }
                        information.setStudentId(value); // id
                    } else if (c == 1) {
                        information.setOldValue(value);  // 原手机号
                    } else if (c == 2) {
                        information.setNewValue(value); // 新手机号
                    } else if (c == 3) {
                        information.setDelete(value); // 删除

                    }
                }
                //添加客户
            }
            informationList.add(information);
        }
        System.out.println(list.size());
        System.out.println(informationList.size());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(list);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        this.doWork(list);
        return informationList;
    }

}
