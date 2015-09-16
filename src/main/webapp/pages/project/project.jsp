<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		.input-text {margin-bottom: 0!important;}
	</style>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv" class="hide">
			<a href="<c:url value="/project/createOrEdit.do" />" class="button button-rounded button-flat-primary">新建项目</a>
			
			<div class="chx-container btn-right">
				<input id="showClose" type="checkbox" />显示已关闭
			</div>
		</div>
		
		<div id="listDiv">
			<table id="projectTab" class="list-table">
				<tr>
					<th width="60px"></th>
					<th width="450px"><input id="name" type="text" class="input-text input-filter" onkeydown="textSearch();" /></th>
					<th width="150px"><input id="owner" type="text" class="input-text input-filter" ondblclick="showUsers($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon" style="margin-left: -19px;margin-top: 5px;"></i></th>
					<th width="120px"></th>
					<th width="120px"></th>
					<th width="150px"></th>
				</tr>
				<tr>
					<th style="text-align: left">序号</th>
					<th style="text-align: left">名称</th>
					<th style="text-align: left">项目经理</th>
					<th style="text-align: left">起始日期</th>
					<th style="text-align: left">结束日期</th>
					<th style="text-align: left">创建时间</th>
				</tr>
			</table>
			
			<div class="pagination"><ul></ul></div>
		</div>
	</div>
	
	<jsp:include page="/pages/common/userSelectMul.jsp"></jsp:include>
	
	<script>
		search();
		initRole();
		
		$("#showClose").iCheck({checkboxClass: "icheckbox_square-blue"}).on("ifChecked", function() { search(1, true); }).on("ifUnchecked", function() { search(); });
		
		function initRole() {
			_remoteCall("role/hasPjtManageAuth.do", null, function(data) {
				data == "Y" ? $("#btnDiv").show() : $("#btnDiv").remove(); 
			});
		}
		
		function search(toPage, showClose) {
			_remoteCall("project/getProjects.do", {page: toPage ? toPage : 1, showClose : showClose ? "Y" : "N", name: $("#name").val(), ownerIds: $("#owner").attr("val") ? $("#owner").attr("val") : ""}, function(data) {
				var d = eval("(" + data + ")"), page = d.page, info = d.projects, html = "";
				
				for(var i in info) {
					html += "<tr id='" + info[i].id + "'>";
					html += "<td class='sn'>" + (parseInt(i) + 1) + "</td>";
					html += "<td><div class='auto-link'><a href='#' onclick='openProject($(this));return false;'>" + info[i].name + "</a></div></td>";
					html += "<td><div>" + info[i].manager + "</div></td>";
					html += "<td><div>" + getDateStr(info[i].startDate) + "</div></td>";
					html += "<td><div>" + getDateStr(info[i].endDate) + "</div></td>";
					html += "<td><div>" + getTimeStr(info[i].createTime) + "</div></td>";
					html += "</tr>";
				}
				
				$("#projectTab tr:gt(1)").remove();
				$("#projectTab").append(html);
				
				// init page bar
				initPage(page);
				
				parent.autoHeight();
			});
		}
	
		function textSearch() {
			if(event.keyCode != 13)  return;
			search();
		}
		
		function openProject($a) {
			var id = $a.closest("tr").attr("id");
			location.href = parent.basePath + "project/openProject.do?projectId=" + id;
		}
	</script>
</body>
</html>
