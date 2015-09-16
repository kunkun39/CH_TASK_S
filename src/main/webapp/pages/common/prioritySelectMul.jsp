<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="priorityModal" class="hide"><p style="padding: 5px;">
	<table id="priorityTab" class="list-table full-table">
		<tr>
			<th width="80px" style="padding-left: 20px;"><input id="pr_all" name="_user" type="checkbox" /></th>
			<th width="460px">优先级</th>
		</tr>
		<tr id="1" onclick="clickPriority($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>高</div></td>
		</tr>
		<tr id="2" onclick="clickPriority($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>中</div></td>
		</tr>
		<tr id="3" onclick="clickPriority($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>低</div></td>
		</tr>
	</table>
</p></div>

<script>
	var $_pr_src;
	
	ppmDialog("#priorityModal", "选择优先级", {
        "确认": function() { selectPriority(); },
        "清空": function() { selectPriority(true); },
        "取消": function() { $(this).dialog("close"); }
	});
	
	function initPriority($inp) {
		$_pr_src = $inp;
		
		// init select statuses
		var id = $inp.attr("val");  id = id ? id : "";
		
		$("#priorityTab input:checkbox").iCheck("uncheck");
		
		$("#priorityTab tr:gt(0)").each(function() {
			if(id.indexOf($(this).attr("id")) >= 0)  $("input:checkbox", $(this)).iCheck("check");
		});
		
		// init check box
		$("#priorityTab input").iCheck({checkboxClass: "icheckbox_square-blue"});
		
		// init select all
		$("#pr_all").on("ifChecked", function() { $("#priorityModal input").iCheck("check"); }).on("ifUnchecked", function() { $("#priorityModal input").iCheck("uncheck"); });
	}
	
	function clickPriority($tr) {
		$("input", $tr).iCheck("toggle");
	}
	
	function selectPriority(isClear) {
		if(isClear) {
			$_pr_src.attr("val", "").val("");
		} else {
			var $sel = $("#priorityModal input:checkbox:checked"), $tr, ids = [], names = [];
			
			$sel.each(function() {
				$tr = $(this).closest("tr");
				if(!$tr.attr("id"))  return true;
				
				ids.push($tr.attr("id"));
				names.push($("td:eq(1)", $tr).text());
			});
			
			$_pr_src.attr("val", ids.join(",")).val(names.join(", "));
		}
		
		// if has filter, call search to refresh table
		if(typeof(search) == "function")  search();
		
		$("#priorityModal").dialog("close");
	}
</script>
