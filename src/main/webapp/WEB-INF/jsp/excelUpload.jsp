<%--
  Created by IntelliJ IDEA.
  User: echoyy
  Date: 2018/8/29
  Time: 下午6:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

    <%
    String importMsg="";
    if(request.getSession().getAttribute("msg")!=null){
        importMsg=request.getSession().getAttribute("msg").toString();
    }
    request.getSession().setAttribute("msg", "");
    %>
<head>
    <title>导入excel</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<div><font color="bule">导入excel</font></div>
<form action="/import" method="post" enctype="multipart/form-data" onsubmit="return check();">
    <div style="margin: 30px;"><input id="excel_file" type="file" name="filename" accept="xlsx" size="80"/>
        <input id="excel_button" type="submit" value="导入Excel"/></div>
    <font id="importMsg" color="red"><%=importMsg%></font><input type="hidden"/>
</form>
</body>
<script type="text/javascript">
    function check() {
        var excel_file = $("#excel_file").val();
        if (excel_file == "" || excel_file.length == 0) {
            alert("请选择文件路径！");
            return false;
        } else {
            return true;
        }
    }

    $(document).ready(function () {
        var msg="";
        if($("#importMsg").text()!=null){
            msg=$("#importMsg").text();
        }
        if(msg!=""){
            alert(msg);
        }
    });
</script>

