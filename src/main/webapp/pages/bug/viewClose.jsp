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
				<a href="<c:url value="/bug/openOperate.do?op=reopen&fromStatus=6&bugId=${param.bugId}" />" class="button button-rounded button-flat-primary">重新开启</a>
			</div>
			
			<button id="delBtn" onclick="confirmRemove();" class="button button-rounded button-flat-primary hide">删除</button>
			
			<button style="margin-left: 25px;" onclick="share($(this));" class="button button-rounded button-flat-primary">
				<c:if test="${shareStatus == 'Y'}">取消分享</c:if>
				<c:if test="${shareStatus == 'N'}">分享经验</c:if>
			</button>
		</div>
		
		<div class="name">${bug.name}</div>
		
		${fieldHtml}
		
		<cloud:record records="${records}" />
		
		<cloud:workflow status="${bug.status}" records="${records}" />
		
		<input id="bugId" type="hidden" value="${param.bugId}" />
	</div>
	
	<script>
		initRole();
		initRemoveRole();
		
		function initRemoveRole() {
			_remoteCall("role/hasBugRemoveAuth.do", null, function(data) {
				data == "Y" ? $("#delBtn").show() : $("#delBtn").remove(); 
			});
		}
	
		function confirmRemove() {
			ppmConfirm("任务删除", "确定要从缺陷库中删除该缺陷么？", function() {
				location.href = parent.basePath + "bug/removeBug.do?bugId=${param.bugId}";
			});
		}
		
		function share($btn) {
			var share = $btn.text().trim() == "分享经验";
			
			_remoteCall("share/shareBug.do", {bugId: "${param.bugId}", flag: share ? "Y" : "N"}, function() {
				ppmAlert("分享任务经验", (share ? "" : "取消") + "分享任务经验成功！");
				$btn.text(share ? "取消分享" : "分享经验");
			});
		}
	</script>
</body>
</html>
