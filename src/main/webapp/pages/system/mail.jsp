<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>.chx-container {margin-left: 8px;}</style>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv">
			<button class="button button-rounded button-flat-primary" onclick="saveMailConfig();">保存配置</button>
		</div>
		
		<div class="alert alert-info">
			当任务流程进入以下状态时向缺陷责任人发送邮件
		</div>
		
		<div class="chx-container">
			<span class="chx-item"><input id="s2" type='checkbox' />待审核</span>
			<span class="chx-item"><input id="s3" type='checkbox' />修改中</span>
			<span class="chx-item"><input id="s4" type='checkbox' />回归测试中</span>
			<span class="chx-item"><input id="s5" type='checkbox' />挂起</span>
			<span class="chx-item"><input id="s6" type='checkbox' />关闭</span>
		</div>
		
		<div class="alert alert-info">
			每天凌晨12:00点系统自动发送用户任务情况邮件
		</div>
		
		<div class="chx-container">
			<span class="chx-item"><input id="d1" type='checkbox' />是/否</span>
		</div>
		
		<div class="alert alert-info">
			向拥有以下角色的人员发送邮件
		</div>
		
		<div style="padding-left: 10px;">
			暂未实现
		</div>
	</div>
	
	<script>
		var config = "${config}";
		
		$("input:checkbox").iCheck();
		
		if(config) {
			var cs = config.split(",");
			for(var i in cs) { $("#" + cs[i]).iCheck("check"); }
		}
		
		function saveMailConfig() {
			_remoteCall("system/saveMailConfig.do", {mailConfig: combineIds($("input:checkbox:checked"))}, function() {
				ppmAlert("邮件配置", "保存邮件配置成功。");
			});
		}
	</script>
</body>
</html>
