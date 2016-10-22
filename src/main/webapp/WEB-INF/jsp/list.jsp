<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<html>
<head>
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<!--bootstrap-->
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>库存</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>创建时间</th>
                        <th>详情页</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="sk" items="${list}">
                        <tr>
                            <td>${sk.name}</td>
                            <td>${sk.number}</td>
                            <td>
                                <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">
                                    列表页
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div id="pages">
                <a href="/seckill/list/1" >首页</a>
                <c:choose>
                    <c:when test="${page>1}">
                        <a href="/seckill/list/${page-1}">上一页</a>
                    </c:when>
                    <c:otherwise><a href="#">上一页</a></c:otherwise>
                </c:choose>


                <c:forEach begin="1" end="2" var="p">
                    <c:choose>
                        <c:when test="${page==p}">
                            <a href="/seckill/list/${p}" class="current_page">${p}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/seckill/list/${p}">${p}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>


                <c:choose>
                    <c:when test="${page<totalPage}">
                        <a href="/seckill/list/${page+1}">下一页</a>
                    </c:when>
                    <c:otherwise><a href="#">下一页</a></c:otherwise>
                </c:choose>
                <a href="/seckill/list/${totalPage}" >末页</a>
            </div>
        </div>
    </div>
</div>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
