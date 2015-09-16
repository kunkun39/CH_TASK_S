<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cloud" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/workpanel.css" />">
	
	<script type="text/javascript" src="<c:url value="/scripts/highcharts.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/stat.chart.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/workpanel.js" />"></script>
</head>

<body>
	<div class="wrapper">
	
	<table id="containerTab" style="table-layout: fixed;">
	<tr>
	<td>
		<div class="button-div">
			<a href="<c:url value="/bug/openOperate.do?op=create&fromStatus=0&workspace=Y" />" class="button button-rounded button-flat-primary">新建任务</a>
		</div>
	</td>
	<td align="center">
		<div class="btn-group button-div">
			<a href="#" onclick="return false;" class="btn disabled">三栏</a>
			<a href="<c:url value="/work/openWork.do?col=2" />" class="btn">双栏</a>
			<a href="<c:url value="/work/openWork.do?col=1" />" class="btn">单栏</a>
		</div>
	</td>
	<td align="right">
		<div class="button-div">
			<button onclick="alert('正在努力地研发中...');" class="btn">定制统计图</button>
		</div>
	</td>
	</tr>
	
	<tr>
	<td class="col">
		<div class="box">
			<div class="title">我的项目</div>
			<div class="content" style="padding-bottom: 25px;">
				<table id="projectTab" class="list-table fix-table" style="width: 540px;">
					<tr>
						<th width="40px">序号</th>
						<th width="350px">名称</th>
						<th width="120px">起始日期</th>
						<th width="120px">结束日期</th>
						<th width="150px">创建时间</th>
					</tr>
				</table>
			</div>
		</div>
		
		<div class="box">
			<div class="title">
				我的任务
				<span class="view-bar">
					<div class="dropdown">
						<a data-toggle="dropdown" class="dropdown-toggle" href="#"><span id="viewName">默认视图</span><i class="icon-th-list"></i></a>
						
						<ul class="dropdown-menu">
							<li id="defaultView"><a href="#" onclick="selectView();return false;">默认视图</a></li>
						</ul>
					</div>
				</span>
			</div>
			
			<div class="content">
				<table id="bugTab" class="list-table fix-table" style="width: 740px;">
					<tr>
						<th width="40px"></th>
						<th width="100px"><input id="code" type="text" class="input-text" style="width: 95px;" onkeydown="textSearch();" /></th>
						<th width="300px"><input id="name" type="text" class="input-text" style="width: 295px;" onkeydown="textSearch();" /></th>
						<th width="150px"><input id="project" type="text" class="input-text" style="width: 145px;" ondblclick="showProjects($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon" style="margin-left: -19px;margin-top: 5px;"></i></th>
						<th width="100px"><input id="status" type="text" class="input-text" style="width: 95px;" ondblclick="showStatuses($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon" style="margin-left: -19px;margin-top: 5px;"></i></th>
						<th width="150px"></th>
					</tr>
					<tr id="head-tr">
						<th style="text-align: left">序号</th>
						<th style="text-align: left"><span class="head-sort" field="code">编号</span></th>
						<th style="text-align: left"><span class="head-sort" field="name">名称</span></th>
						<th style="text-align: left"><span class="head-sort" field="projectId">所属项目</span></th>
						<th style="text-align: left"><span class="head-sort" field="status">状态</span></th>
						<th style="text-align: left"><span class="head-sort" field="modifyTime">最近更新时间<i class="icon-arrow-down"></i></span></th>
					</tr>
				</table>
				
				<div class="pagination"><ul></ul></div>
			</div>
		</div>
	</td>
	
	<td class="col">
		<div class="box">
			<div class="title">任务状态统计图</div>
			<div id="statusChart" class="content"></div>
		</div>
		
		<div class="box">
			<div class="title">项目任务统计图</div>
			<div id="bugProjectChart" class="content"></div>
		</div>
	</td>
	
	<td class="col">
		<div class="box">
			<div class="title">系统动态</div>
			<div id="recordContent" class="content">
				<div style="margin-left: 20px;">无</div>
			</div>
		</div>
	</td>
	</tr>
	</table>
	
	</div>
	
	<jsp:include page="/pages/common/projectSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/statusSelectMul.jsp"></jsp:include>
	
	<script>
		var readStatus = "${param.readStatus}" == "Y";
		var col = 3, wspan = readStatus ? 15 : 9, pinfo = ${projects}, rinfo = ${records}, sortInfo = [], html = "", hasMore = "${hasMore}", statusStat = ${statusStat}, bugProjectChart = ${bugPjtStat};
	</script>
</body>
</html>
