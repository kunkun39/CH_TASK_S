<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBtdC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<script type="text/javascript" src="<c:url value="/scripts/highcharts.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/stat.chart.js" />"></script>
</head>

<body>
	<div class="wrapper">
	
		<div id="btnDiv">
			<c:if test="${param.workspace == 'Y'}">
				<a href="<c:url value="/work/openWork.do" />" class="button button-rounded button-flat-primary">返回</a>
			</c:if>
			<c:if test="${param.workspace != 'Y'}">
				<a href="<c:url value="/pages/project/project.jsp" />" class="button button-rounded button-flat-primary">返回</a>
			</c:if>
			<c:if test="${canEdit == 'Y'}">
				<a href="<c:url value="/project/createOrEdit.do?projectId=${project.id}" />" class="button button-rounded button-flat-primary">编辑</a>
				
				<c:if test="${project.isClose != 'Y'}">
					<a href="#" onclick="toggleProject($(this));return false;" class="button button-rounded button-flat-primary">关闭</a>
				</c:if>
				
				<c:if test="${project.isClose == 'Y'}">
					<a href="#" onclick="toggleProject($(this));return false;" class="button button-rounded button-flat-primary">重新开启</a>
				</c:if>
			</c:if>
		</div>
		
		<div class="name">${project.name}</div>
		
		<table class="detail-table">
			<tr>
				<td class="label-td" width="100px">项目经理</td><td width="250px"><cloud:user userId="${project.managerId}" /></td>
				<td class="label-td" width="100px">项目助理</td><td width="250px"><cloud:users ids="${project.assistantIds}" /></td>
			</tr>
			<tr class="odd">
				<td class="label-td">起始日期</td><td><cloud:date date="${project.startDate}" /></td>
				<td class="label-td">结束日期</td><td><cloud:date date="${project.endDate}" /></td>
			</tr>
			<tr>
				<td class="label-td">项目描述</td><td colspan="3">${project.intro}</td>
			</tr>
			<tr class="odd">
				<td class="label-td">创建时间</td><td colspan="3"><cloud:time date="${project.createTime}" /></td>
			</tr>
		</table>
		
		<div class="stat">
			<div class="title">任务统计</div>
			
			<div id="chart1" class="chart-box"></div>
			<div id="chart2" class="chart-box"></div>
			<div id="chart3" class="chart-box"></div>
		</div>
	</div>
	
	<script>
		var statusStat = ${statusStat};
        initPieChart("chart1", "任务状态统计图", statusStat);
        
        var personReciveStat = ${personReciveStat};
        initPieChart("chart2", "人员接收任务统计图", personReciveStat);
        
        var personSolveStat = ${personSolveStat};
        initPieChart("chart3", "人员解决任务统计图", personSolveStat);
        
        function toggleProject($btn) {
        	if($btn.text() == "关闭") {
        		ppmConfirm("关闭项目", "确定要关闭项目么？关闭项目后，所有项目下的任务状态都将被置为关闭！", function() {
            		_remoteCall("project/closeProject.do", {projectId: "${param.projectId}"}, function() {
            			$btn.text("重新开启");
            			ppmAlert("关闭项目", "关闭项目成功。");
            		});
            	});
        	} else {
        		ppmConfirm("开启项目", "确定要重新开启项目么？", function() {
            		_remoteCall("project/reopenProject.do", {projectId: "${param.projectId}"}, function() {
            			$btn.text("关闭");
            			ppmAlert("开启项目", "重新开启项目成功。");
            		});
            	});
        	}
        }
	</script>
</body>
</html>
