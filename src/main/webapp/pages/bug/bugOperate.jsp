<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/uploadify.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.fancybox.css" />" />
	
	<script type="text/javascript" src="<c:url value="/scripts/jquery.uploadify.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/jquery.fancybox.pack.js" />"></script>
	
	<style>
		.input-text {width: 460px;}
		textarea {width: 460px;height: 150px;}
	</style>
</head>

<body>
	<div class="wrapper">
		<form action="<c:url value="/bug/saveBug.do" />" onsubmit="return initForm();" method="post">
			<div id="btnDiv">${btnHtml}</div>
			${fieldHtml}
			
			<input type="hidden" name="id" value="${param.bugId}" />
			<input type="hidden" name="operate" value="${param.op}" />
			<input type="hidden" name="fromStatus" value="${param.fromStatus}" />
			<input type="hidden" name="workspace" value="${param.workspace}" />
			<input type="hidden" id="_status" name="status" />
			<input type="hidden" id="_attachIds" name="attachIds" />
		</form>
	</div>
	
	<jsp:include page="/pages/common/projectSelect.jsp"></jsp:include>
	<jsp:include page="/pages/common/userSelect.jsp"></jsp:include>
	
	<script>
		var status, attachIds = [];
		
		function setStatus(s) {
			status = s;
		}
		
		function initForm() {
			$("input[field='relate']").each(function() {
				var val = $(this).attr("val");
				$("#_" + $(this).attr("id")).val(val ? val : "");
			});
			
			$("div.select-div").each(function() {
				$("#_" + $(this).attr("id")).val($(this).selectVal());
			});
			
			$("#_status").val(status);
			$("#_attachIds").val(attachIds.join(","));
			
			return checkForm();
		}
		
		$("div.select-div").select();
		
		// deal with attach
		if($("div.attach").size() > 0) {
			$("#file_upload").uploadify({
				buttonText: "上传",
				removeCompleted: false,
				swf: parent.basePath + "/css/uploadify.swf",
				uploader : parent.basePath + "/upload.action?" + getJSessionCookie(),
				onQueueComplete: function() {
					parent.autoHeight();
				},
				onUploadSuccess: function(file, data, response) {
					attachIds.push(data);
				}
			});
			
			$(".fancybox").fancybox({
				openEffect	: 'elastic',
		    	closeEffect	: 'elastic'
			});
		}
		
		function downloadAttach(attachName, realName) {
			location.href = parent.basePath + "download.action?fileName=" + attachName + "&realName=" + realName;
		}
	</script>
</body>
</html>
