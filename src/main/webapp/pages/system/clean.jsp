<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
	<div class="wrapper">
		<div id="btnDiv">
			<button onclick="clean();" class="button button-rounded button-flat-primary">开始清理</button>
		</div>
		
		<div class="alert alert-info">
			永久删除已关闭项目的所有数据，包括任务，操作记录，经验，附件等。
		</div>
		
		<div id="listDiv">
			<table class="list-table">
				<tr>
					<th width="60px">选择</th>
					<th width="350px">名称</th>
					<th width="150px">项目经理</th>
					<th width="120px">起始日期</th>
					<th width="120px">结束日期</th>
					<th width="150px">创建时间</th>
				</tr>
				
				<c:forEach items="${projects}" var="p">
				<tr id="${p.id}">
					<td align="center"><input type="checkbox" /></td>
					<td><div>${p.name}</div></td>
					<td><div><cloud:user userId="${p.managerId}" /></div></td>
					<td><div><cloud:date date="${p.startDate}" /></div></td>
					<td><div><cloud:date date="${p.endDate}" /></div></td>
					<td><div><cloud:time date="${p.createTime}" /></div></td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	
	<script>
		$("input:checkbox").iCheck({checkboxClass: "icheckbox_square-blue"});
		
		function clean() {
			var $chxs = $("table.list-table input:checkbox:checked"), ids = [];
			
			if($chxs.size() == 0) {
				ppmAlert("清理数据", "请选择要清理的主项目。");  return;
			}
			
			ppmConfirm("清理数据", "确定要彻底删除选中的项目及该项目下的所有任务，操作记录，经验，附件么？此操作为不可回滚式彻底删除，请认真考虑后再执行此操作！", function() {
				$chxs.each(function() {
					ids.push($(this).closest("tr").attr("id"));
				});
				
				_remoteCall("system/cleanData.do", {projectIds: ids.join(",")}, function() {
					ppmAlert("清理数据", "清理系统数据完成。", function() { reloadPage(); });
				});
			});
		}
	</script>
</body>
</html>
