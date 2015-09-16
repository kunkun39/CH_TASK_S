<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		body {background-image: url(img/cloud_bg.jpg);background-repeat: no-repeat;background-size: 100% 100%;font-size: 13px;}
		table {position: absolute;top: 37%;left: 50%;}
		table tr {height: 50px;}
		.input-text {width: 300px;margin-top: 5px;}
	</style>
</head>

<body>
	<form action="j_spring_security_check" method="POST">
	<table style="font-size: 13px;">
		<tr>
			<td colspan="2" align="center" style="font-weight: bold;color: white;font-size: 16px;">网络产品部任务管理系统</td>
		</tr>
		<tr>
			<td align="right" style="color: white;">用户名&nbsp;&nbsp;</td>
			<td><input type="text" name="j_username" class="input-text" placeholder="用户名" /></td>
		</tr>
		<tr>
			<td align="right" style="color: white;">密码&nbsp;&nbsp;</td>
			<td><input type="password" name="j_password" class="input-text" placeholder="密码" /></td>
		</tr>
		<tr style="height: 20px;">
			<td colspan="2" align="center" style="color: red;font-size: 12px;">
				<c:if test="${param.error == null}">推荐使用Chrome浏览器，暂不支持IE系列</c:if>
				<c:if test="${param.error != null}">用户名或密码输入错误！</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input class="button button-pill button-flat-primary" type="submit" style="margin-left: 50px;width: 150px;" />
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
