<%--
  Created by IntelliJ IDEA.
  User: echoyy
  Date: 2018/8/29
  Time: 下午3:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div style="width:1000px;margin:0px auto;text-align:center">


    <div style="text-align:center;margin-top:40px">

        <form method="post" action="update">
            问题： <input name="problem" value="${knowledge.problem}" type="text"> <br><br>
            答案： <input name="answer" value="${knowledge.answer}" type="text"> <br><br>
            <input type="hidden" value="${knowledge.id}" name="id">

            <input type="submit" value="修改">
        </form>

    </div>
</div>

