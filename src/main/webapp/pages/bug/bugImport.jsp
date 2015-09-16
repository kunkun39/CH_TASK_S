<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/uploadify.css" />">
	<script type="text/javascript" src="<c:url value="/scripts/jquery.uploadify.min.js" />"></script>
	
	<style>
		.title {margin-top: 20px;}
		.wrapDiv {margin-left: 20px;}
	</style>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv">
			<a href="<c:url value="/pages/bug/bug.jsp" />" class="button button-rounded button-flat-primary">返回</a>
			<a href="<c:url value="/bugfile/exportTemplate.do" />" class="button button-rounded button-flat-primary">导入模板下载</a>
		</div>
		
		<div class="alert alert-info">
			请先下载模板文件，然后根据模板文件的格式填写要上传的任务的数据！
		</div>
	
		<div class="title" style="margin-top: 0;">上传数据文件</div>
		<div style="margin-left: 15px;"><input id="file_upload" type="file" /></div>
		
		<div id="dataDiv"></div>
	</div>
	
	<script>
		$("#file_upload").uploadify({
			buttonText: "上传",
			removeCompleted: false,
			fileTypeDesc : "Excel 2003 Files",
	        fileTypeExts : "*.xls",
	        multi: false,
			swf: parent.basePath + "/css/uploadify.swf",
			uploader : parent.basePath + "/bugfile/importBugs.do?" + getJSessionCookie(),
			onUploadProgress: function() {
				loadStatus();
			}
		});
		
		function loadStatus() {
			_remoteCall("bugfile/loadStatus.do", null, function(data) {
				$("#dataDiv").html(data);
				
				if(data.substring(data.length - 4) != "over") {
					setTimeout(function() {  loadStatus(); }, 500);					
				}
			});
		}
	</script>
</body>
</html>
