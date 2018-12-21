package com.echoyy.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.echoyy.pojo.Information;
import com.sun.deploy.net.URLEncoder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.net.www.http.HttpClient;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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



    private void doWork(List<String> list){
        String remark = "家长您好，我是DaDa英语的课程顾问老师xx，现在刚好赶上年终感恩回馈，今天特别筛选100个历史注册客户，来参加爱心妈妈推广大使的活动。只针对于六单元的课程，之前是74节课时，现在可以获得80节1对1课程，专享狂欢节特惠9890元，而且现在课程没有时间限制。名额有限，您如果决定参加，我这边给您登记上，安排后续入学事宜哈。";
        JSONObject json = new JSONObject();
        json.put("remark", remark);
        json.put("activeStudent",list);
        BufferedReader in = null;
        PrintWriter out = null;
        String result = "";
        try {
            URL url = new URL("http://crm.dadaorg.com/admin/api/operator/active/student");
//            URL url = new URL("http://localhost:8610/admin/api/operator/active/student");

            HttpURLConnection  conn =  (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Authorization","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1X2lkIjoiODg5MDYiLCJ1X21peCI6IiIsInVfbmFtZSI6ImRvdWRvdSIsInVfdGltZSI6MTUzOTY3NzU3Mn0.LPMf8StofgcU47erPfigKFuVo491FlmTWPiZ0Iy_ZyU");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);


            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
            out.print(json);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine())!=null){
                result+=line;
            }
        }catch (Exception e){
            System.out.println("post error");
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println("post推送结果："+result);
    }
}
