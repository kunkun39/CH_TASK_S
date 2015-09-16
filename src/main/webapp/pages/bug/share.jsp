<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		.share-section {padding-bottom: 10px;margin-bottom: 10px;border-bottom: 1px dotted #ccc;}
		.share-section table {font-size: 13px;margin-left: 20px;margin-top: 5px;}
		.share-section table td {padding: 5px 5px;}
		.share-section .bottom {margin-top: 10px;margin-left: 25px;color: #888;}
		.share-section .left-label {text-decoration: underline;}
	</style>
</head>

<body>
	<div class="wrapper">
		<c:if test="${shares == null || fn:length(shares) == 0}"><div style="margin: 10px;">暂无任务经验分享。</div></c:if>
	
		<c:forEach var="share" items="${shares}" varStatus="status">
		<div class="share-section">
			<div>${status.index + 1}. <a id="${share[0]}" href="#" onclick="openBug($(this));">${share[1]}</a></div>
			<table>
				<tr>
					<td valign="top" width="80px"><span class="left-label">任务描述</span></td>
					<td>
						<c:if test="${share[2] == null || share[2] == ''}">无</c:if>
						<c:if test="${share[2] != null && share[2] != ''}">${share[2]}</c:if>
					</td>
				</tr>
				<tr>
					<td valign="top" width="80px"><span class="left-label">解决方法</span></td>
					<td>
						<c:if test="${share[3] == null || share[3] == ''}">无</c:if>
						<c:if test="${share[3] != null && share[3] != ''}">${share[3]}</c:if>
					</td>
				</tr>
			</table>
			<div class="bottom"><span>解决人：<cloud:user userId="${share[4]}" /></span><span style="margin-left: 40px;">分享时间：<cloud:time date="${share[5]}" /></span></div>
		</div>
		</c:forEach>
	</div>
	
	<script>
		function openBug($a) {
			var id = $a.attr("id");
			location.href = parent.basePath + "bug/openBug.do?bugId=" + id;
		}
	</script>
</body>
</html>
