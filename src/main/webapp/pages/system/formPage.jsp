<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv">
			<button class="button button-rounded button-flat-primary" onclick="$('#fieldModal').dialog('open');">添加页面字段</button>
			<a class="button button-rounded button-flat-primary" href="<c:url value="/pages/system/form.jsp" />">返回</a>
		</div>
		
		<div id="listDiv">
			<table class="list-table">
				<tr>
					<th width="60px">顺序</th>
					<th width="250px">字段</th>
					<th width="150px">字段类型</th>
					<th width="150px">字段页面类型</th>
					<th width="100px">是否必填</th>
					<th width="30px"></th>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="fieldModal" class="hide"><p style="padding: 5px;">
		<table class="edit-table">
			<tr>
				<td class="left-td" width="100px">选择字段</td>
				<td><div id="fieldSelect" class="select-div input-require"></div></td>
			</tr>
			<tr>
				<td class="left-td" width="100px">字段顺序</td>
				<td><input id="sortSn" type="text" class="input-text" /></td>
			</tr>
			<tr>
				<td class="left-td">是否必填</td>
				<td><input id="isRequire" type="checkbox" /></td>
			</tr>
		</table>
	</p></div>
	
	<script>
		var pageFlag = ${param.pageFlag}, isView = pageFlag < 7;
		refresh();
		
		if(isView) {
			$("#isRequire").closest("tr").hide();
		} else {
			$("#isRequire").iCheck({checkboxClass: "icheckbox_square-blue"});			
		}
		
		$("#fieldSelect").select({
			load: function() {
				var items = [];
				
				_remoteCall("system/getSelectFields.do", null, function(data) {
					items = data;
				}, true, true);
				
				return items;
			}
		});
		
		ppmDialog("#fieldModal", "添加页面字段", {
	        "添加": function() { if(checkForm()) savePageField(); },
	        "取消": function() { $(this).dialog("close"); }
		});
		
		function refresh() {
			_remoteCall("system/getPageFields.do", {pageFlag: pageFlag}, function(info) {
				var $tab = $("table.list-table"), html = "";
				
				// remove origin rows
				$("tr:gt(0)", $tab).remove();
				
				for(var i in info) {
					html += "<tr id='" + info[i][0] + "'>";
					html += "<td class='sn'>" + info[i][1] + "</td>";
					html += "<td><div>" + info[i][2] + "</div></td>";
					html += "<td><div>" + info[i][3] + "</div></td>";
					html += "<td><div>" + info[i][4] + "</div></td>";
					html += "<td><div>" + info[i][5] + "</div></td>";
					
					html += "<td><div class='hide'>";
					html += "<i class='icon-trash' style='cursor: pointer;' onclick='removePageField($(this));'></i>";
					html += "</div></td>";
					
					html += "</tr>";
				}
				
				$tab.append(html);
				
				// bind mouse event
				$("tr:gt(0)", $tab).hover(
					function() {
						$("td:last div", this).show();
					}, 
					function() {
						$("td:last div", this).hide();
					}
				);
				
				autoFrameHeight();
			}, true);
		}
		
		function savePageField() {
			var fieldId = $("#fieldSelect").selectVal();
			var sortSn = $("#sortSn").val() ? $("#sortSn").val() : 0;
			var isRequire = $("#isRequire").parent().hasClass("checked") ? "Y" : "N";
			
			_remoteCall("system/savePageField.do", {pageFlag: pageFlag, fieldId: fieldId, sortSn: sortSn, isRequire: isRequire}, function(data) {
				refresh();
				$("#fieldModal").dialog("close");
			});
		}
		
		function removePageField($i) {
			ppmConfirm("页面字段删除", "确定要删除该页面字段么？", function() {
				_remoteCall("system/removePageField.do", {pageFieldId: $i.closest("tr").attr("id")}, function() {
					$i.closest("tr").remove();
					
					$("#dialog-del").dialog({
						resizable: false,
					    height: 200,
					    modal: true,
					    buttons: {
					        "确定": function() {
					        	$(this).dialog("close");
					        	$("#dialog-confirm").dialog("close");
					        }
						}
					});
				});
			});
		}
	</script>
	
</body>
</html>
