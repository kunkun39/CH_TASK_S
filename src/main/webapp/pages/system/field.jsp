<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		.input-text {width: 400px;}
	</style>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv">
			<button class="button button-rounded button-flat-primary" onclick="alert('正在努力地研发中...');">添加字段</button>
		</div>
		
		<div id="listDiv">
			<table class="list-table">
				<tr>
					<th width="60px">序号</th>
					<th width="100px">类型</th>
					<th width="250px">字段名</th>
					<th width="250px">显示名</th>
					<th width="150px">字段页面类型</th>
				</tr>
				
				<c:forEach var="field" items="${fields}" varStatus="status">
				<tr>
					<td class="sn">${status.index + 1}</td>
					<td><div>${field.typeName}</div></td>
					<td><div>${field.name}</div></td>
					<td><div>${field.label}</div></td>
					<td><div>${field.htmlTypeName}</div></td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>
