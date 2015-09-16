<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>

<body>
	<div class="wrapper">
	
		<form action="<c:url value="/project/saveProject.do" />" onsubmit="return initForm();" method="post">
		
		<div id="btnDiv">
			<input type="submit" class="button button-rounded button-flat-primary" value="保存" />
			<a href="<c:url value="/pages/project/project.jsp" />" class="button button-rounded button-flat-primary">返回</a>
		</div>
		
		<table class="edit-table">
			<tr>
				<td class="left-td" width="80px">项目名称</td>
				<td><input name="name" type="text" class="input-text input-require input-maxlen255" value="${project.name}" /></td>
			</tr>
			<tr>
				<td class="left-td">项目经理</td>
				<td><input id="manager" type="text" class="input-text input-require" ondblclick="showUser($(this));" onkeydown="return false;" val="${project.managerId}" value="${project.manager}" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">项目助理</td>
				<td><input id="assistant" type="text" class="input-text" ondblclick="showUsers($(this));" onkeydown="return false;" val="${project.assistantIds}" value="<cloud:users ids="${project.assistantIds}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">起始日期</td>
				<td><input id="startDate" name="startDate" type="text" class="input-text" onkeydown="return false;" value="<cloud:date date="${project.startDate}" />" /><i onclick="$(this).prev().focus();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">结束日期</td>
				<td><input id="endDate" name="endDate" type="text" class="input-text" onkeydown="return false;" value="<cloud:date date="${project.endDate}" />" /><i onclick="$(this).prev().focus();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">项目描述</td>
				<td><textarea name="intro" class="input-textarea  input-maxlen2048">${project.intro}</textarea></td>
			</tr>
		</table>
		
		<input type="hidden" name="id" value="${project.id}" />
		<input type="hidden" id="_managerId" name="managerId" />
		<input type="hidden" id="_assistantIds" name="assistantIds" />
		
		</form>
	</div>

	<jsp:include page="/pages/common/userSelect.jsp"></jsp:include>
	<jsp:include page="/pages/common/userSelectMul.jsp"></jsp:include>
	
	<script>
		$("#startDate, #endDate").datepicker({
			dateFormat: "yy-mm-dd"
		});
		
		function initForm() {
			$("#_managerId").val($("#manager").attr("val") ? $("#manager").attr("val") : "");
			$("#_assistantIds").val($("#assistant").attr("val") ? $("#assistant").attr("val") : "");
			
			return checkForm();
		}
	</script>
</body>
</html>
