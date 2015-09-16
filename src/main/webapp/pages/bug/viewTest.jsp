<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBtdC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.fancybox.css" />" />
	<script type="text/javascript" src="<c:url value="/scripts/jquery.fancybox.pack.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/attach.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/bugView.js" />"></script>
</head>

<body>
	<div class="wrapper">
	
		<div id="btnDiv">
			<c:if test="${param.workspace == 'Y'}">
				<a href="<c:url value="/work/openWork.do" />" class="button button-rounded button-flat-primary">返回</a>
			</c:if>
			<c:if test="${param.workspace != 'Y'}">
				<a href="<c:url value="/pages/bug/bug.jsp" />" class="button button-rounded button-flat-primary">返回</a>
			</c:if>
			
			<!-- 这里不要使用 hide 样式 -->
			<div class="operate-div" style="display: none;">
				<a href="<c:url value="/bug/openOperate.do?op=success&fromStatus=4&bugId=${param.bugId}" />" class="button button-rounded button-flat-primary">测试通过</a>
				<a href="<c:url value="/bug/openOperate.do?op=fail&fromStatus=4&bugId=${param.bugId}" />" class="button button-rounded button-flat-primary">测试不通过</a>
				<a href="<c:url value="/bug/openOperate.do?op=chgtestor&fromStatus=4&bugId=${param.bugId}" />" class="button button-rounded button-flat-primary">更换责任人</a>
			</div>
		</div>
		
		<div class="name">${bug.name}</div>
		
		${fieldHtml}
		
		<cloud:record records="${records}" />
		
		<cloud:workflow status="${bug.status}" records="${records}" />
		
		<input id="bugId" type="hidden" value="${param.bugId}" />
	</div>
</body>
</html>
