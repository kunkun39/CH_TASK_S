<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="projectsModal" class="hide"><p style="padding: 5px;">	
	<table id="projectsTab" class="list-table full-table">
		<tr>
			<th width="80px" style="padding-left: 20px;"><input id="p_all" name="_user" type="checkbox" /></th>
			<th width="310px">项目名称</th>
			<th width="150px">项目经理</th>
		</tr>
	</table>
</p></div>

<script>
	var $_p_src;
	
	ppmDialog("#projectsModal", "选择项目", {
        "确认": function() { selectProject(); },
        "清空": function() { selectProject(true); },
        "取消": function() { $(this).dialog("close"); }
	});
	
	function loadProjectData($inp) {
		$_p_src = $inp;
		var id = $inp.attr("val"), id = id ? id : "";
		
		_remoteCall("project/getProjects.do", null, function(data) {
			var d = eval("(" + data + ")"), info = d.projects, html = "", $tab = $("#projectsTab");
			
			// remove origin rows
			$("tr:gt(0)", $tab).remove();
			
			for(var i in info) {
				html += "<tr id='" + info[i].id + "' onclick='clickProject($(this));'>";
				html += "<td style='padding-left: 20px;'><input name='_user' type='checkbox' " + (id.indexOf(info[i].id) >= 0 ? "checked" : "") + " /></td>";
				html += "<td><div>" + info[i].name + "</div></td>";
				html += "<td><div>" + info[i].manager + "</div></td>";
				html += "</tr>";
			}
			
			$tab.append(html);
			
			// init check box
			$("#projectsTab input").iCheck({checkboxClass: "icheckbox_square-blue"});
			
			// init select all
			$("#p_all").on("ifChecked", function() { $("#projectsModal input").iCheck("check"); }).on("ifUnchecked", function() { $("#projectsModal input").iCheck("uncheck"); });
		});
	}
	
	function clickProject($tr) {
		$("input", $tr).iCheck("toggle");
	}
	
	function selectProject(isClear) {
		if(isClear) {
			$_p_src.attr("val", "").val("");
		} else {
			var $sel = $("#projectsTab input:checkbox:checked"), $tr, ids = [], names = [];
			
			$sel.each(function() {
				$tr = $(this).closest("tr");
				if(!$tr.attr("id"))  return true;
				
				ids.push($tr.attr("id"));
				names.push($("td:eq(1)", $tr).text());
			});
			
			$_p_src.attr("val", ids.join(",")).val(names.join(", "));
		}
		
		// if has filter, call search to refresh table
		if(typeof(search) == "function")  search();
		
		$("#projectsModal").dialog("close");
	}
</script>
