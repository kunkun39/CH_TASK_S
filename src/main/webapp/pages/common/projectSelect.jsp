<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="projectModal" class="hide"><p style="padding: 5px;">
	<table id="projectTab" class="list-table full-table">
		<tr>
			<th width="80px"></th>
			<th width="310px">项目名称</th>
			<th width="150px">项目经理</th>
		</tr>
	</table>
</p></div>

<script>
	var $_p_src;
	
	ppmDialog("#projectModal", "选择项目", {
        "确认": function() { selectProject(); },
        "清空": function() { selectProject(true); },
        "取消": function() { $(this).dialog("close"); }
	});
	
	function loadProjectData($inp) {
		$_p_src = $inp;
		var id = $inp.attr("val"), id = id ? id : "";
		
		_remoteCall("project/getProjects.do", null, function(data) {
			var d = eval("(" + data + ")"), info = d.projects, html = "", $tab = $("#projectTab");
			
			// remove origin rows
			$("tr:gt(0)", $tab).remove();
			
			for(var i in info) {
				html += "<tr id='" + info[i].id + "' onclick='clickProject($(this));' ondblclick='selectProject();'>";
				html += "<td class='sn'><input name='_user' type='radio' " + (id == info[i].id ? "checked" : "") + " /></td>";
				html += "<td><div>" + info[i].name + "</div></td>";
				html += "<td><div>" + info[i].manager + "</div></td>";
				html += "</tr>";
			}
			
			$tab.append(html);
			
			// init radio box
			$("#projectTab input").iCheck({radioClass: "iradio_square-blue"});
		});
	}
	
	function clickProject($tr) {
		$("input", $tr).iCheck("check");
	}
	
	function selectProject(isClear) {
		if(isClear) {
			$_p_src.attr("val", "").val("");
		} else {
			var $sel = $("#projectModal input:radio:checked").closest("tr");
			$_p_src.attr("val", $sel.attr("id")).val($("td:eq(1)", $sel).text());
		}
		
		// if has filter, call search to refresh table
		if(typeof(search) == "function")  search();
		
		$("#projectModal").dialog("close");
	}
</script>
