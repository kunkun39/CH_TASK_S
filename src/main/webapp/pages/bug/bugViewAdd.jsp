<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		.input-text {width: 460px;}
		textarea {width: 460px;height: 150px;}
		.mid-td {padding-left: 5px;padding-bottom: 8px;font-weight: bold;}
	</style>
</head>

<body>
	<div class="wrapper">
	
		<form action="<c:url value="/bug/saveView.do" />" onsubmit="return initForm();" method="post">
		
		<div id="btnDiv">
			<input type="submit" class="button button-rounded button-flat-primary" value="保存" />
			<a href="<c:url value="/pages/bug/bug.jsp" />" class="button button-rounded button-flat-primary">返回</a>
			
			<c:if test="${param.viewId != null && param.viewId != ''}">
				<a href="#" class="button button-rounded button-flat-primary" onclick="removeView();return false;">删除</a>
			</c:if>
		</div>
		
		<div class="title">基础配置</div>
		
		<table class="edit-table" style="margin-left: 30px;">
			<tr>
				<td class="left-td" width="80px">视图名称</td>
				<td><input name="name" type="text" class="input-text input-require" value="${view.name}" style="width: 515px;" /></td>
			</tr>
		</table>
		
		<div class="title">分页配置</div>
		
		<table class="edit-table" style="margin-left: 30px;">
			<tr>
				<td class="left-td" width="80px">单页任务数</td>
				<td><input name="pageSize" type="text" class="input-text" value="${view.pageSize}" style="width: 515px;" /></td>
			</tr>
		</table>
		
		<div class="title">排序配置</div>
		
		<table class="edit-table" style="margin-left: 30px;">
			<tr>
				<td class="left-td" width="80px">字段</td>
				<td class="left-td" width="250px">
					<div id="sortField" class="select-div" style="width: 220px;">
						<ul class="menu-ul">
							<li>请选择</li>
							<li val="name">名称</li>
							<li val="projectId">所属项目</li>
							<li val="status">状态</li>
							<li val="level">严重性</li>
							<li val="priority">优先级</li>
							<li val="ownerId">责任人</li>
							<li val="modifyTime" checked="checked">最近更新时间</li>
						</ul>
					</div>
				</td>
				<td class="left-td" width="80px">顺序</td>
				<td><div id="sortTo" val="desc" style="cursor: pointer;padding-bottom: 7px;">降序 <i class="icon-arrow-down"></i></div></td>
			</tr>
		</table>
		
		<div class="title">过滤配置</div>
		
		<table class="edit-table" style="margin-left: 30px;">
			<tr>
				<td class="left-td" width="80px">任务名称</td>
				<td class="mid-td" width="50px">like</td>
				<td><input name="bugName" type="text" class="input-text" value="${view.bugName}" /></td>
			</tr>
			<tr>
				<td class="left-td">所属项目</td>
				<td class="mid-td">in</td>
				<td><input id="projectIds" type="text" class="input-text" ondblclick="showProjects($(this));" onkeydown="return false;" val="${view.projectIds}" value="<cloud:projects ids="${view.projectIds}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">状态</td>
				<td class="mid-td">in</td>
				<td><input id="status" type="text" class="input-text" ondblclick="showStatuses($(this));" onkeydown="return false;" val="${view.status}" value="<cloud:statuses ids="${view.status}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">责任人</td>
				<td class="mid-td">in</td>
				<td><input id="ownerIds" type="text" class="input-text" ondblclick="showUsers($(this));" onkeydown="return false;" val="${view.ownerIds}" value="<cloud:users ids="${view.ownerIds}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">严重性</td>
				<td class="mid-td">in</td>
				<td><input id="levels" type="text" class="input-text" ondblclick="showLevels($(this));" onkeydown="return false;" val="${view.levels}" value="<cloud:items itemField="level" itemIds="${view.levels}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
			<tr>
				<td class="left-td">优先级</td>
				<td class="mid-td">in</td>
				<td><input id="priorities" type="text" class="input-text" ondblclick="showPriorities($(this));" onkeydown="return false;" val="${view.priorities}" value="<cloud:items itemField="level" itemIds="${view.priorities}" />" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></td>
			</tr>
		</table>
		
		<input type="hidden" id="viewId" name="id" value="${view.id}" />
		<input type="hidden" id="_projectIds" name="projectIds" />
		<input type="hidden" id="_status" name="status" />
		<input type="hidden" id="_ownerIds" name="ownerIds" />
		<input type="hidden" id="_levels" name="levels" />
		<input type="hidden" id="_priorities" name="priorities" />
		<input type="hidden" id="_sortField" name="sortField" />
		<input type="hidden" id="_sortTo" name="sortTo" />
		
		</form>
	</div>
	
	<jsp:include page="/pages/common/projectSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/userSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/statusSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/levelSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/prioritySelectMul.jsp"></jsp:include>
	
	<script>
		var sortField = "${view.sortField}", sortTo = "${view.sortTo}";
		
		$("#sortField").select();
		
		$("#sortTo").click(function() {
			var sortTo = $(this).attr("val");
			
			if(sortTo == "desc") {
				$(this).html("升序 <i class='icon-arrow-up'></i>").attr("val", "asc");
			} else {
				$(this).html("降序 <i class='icon-arrow-down'></i>").attr("val", "desc");
			}
		});
		
		if(sortField) {
			$("#sortField").selectVal(sortField);
		}
		
		if(sortTo == "asc") {
			$("#sortTo").click();
		}
		
		function initForm() {
			$("#_projectIds").val($("#projectIds").attr("val") ? $("#projectIds").attr("val") : "");
			$("#_status").val($("#status").attr("val") ? $("#status").attr("val") : "");
			$("#_ownerIds").val($("#ownerIds").attr("val") ? $("#ownerIds").attr("val") : "");
			$("#_levels").val($("#levels").attr("val") ? $("#levels").attr("val") : "");
			$("#_priorities").val($("#priorities").attr("val") ? $("#priorities").attr("val") : "");
			$("#_sortField").val($("#sortField").selectVal());
			$("#_sortTo").val($("#sortTo").attr("val"));
			
			return checkForm();
		}
		
		function removeView() {
			ppmConfirm("删除视图", "确定要删除此视图么？", function() {
				location.href = parent.basePath + "bug/removeView.do?viewId=" + $("#viewId").val();
			});
		}
	</script>
</body>
</html>
