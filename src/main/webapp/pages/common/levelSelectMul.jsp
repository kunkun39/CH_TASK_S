<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="levelModal" class="hide"><p style="padding: 5px;">
	<table id="levelTab" class="list-table full-table">
		<tr>
			<th width="80px" style="padding-left: 20px;"><input id="l_all" name="_user" type="checkbox" /></th>
			<th width="460px">严重性</th>
		</tr>
		<tr id="1" onclick="clickLevel($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>致命</div></td>
		</tr>
		<tr id="2" onclick="clickLevel($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>严重</div></td>
		</tr>
		<tr id="3" onclick="clickLevel($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>一般</div></td>
		</tr>
		<tr id="4" onclick="clickLevel($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>优化</div></td>
		</tr>
	</table>
</p></div>

<script>
	var $_l_src;
	
	ppmDialog("#levelModal", "选择严重性", {
        "确认": function() { selectLevel(); },
        "清空": function() { selectLevel(true); },
        "取消": function() { $(this).dialog("close"); }
	});
	
	function initLevel($inp) {
		$_l_src = $inp;
		
		// init select statuses
		var id = $inp.attr("val");  id = id ? id : "";
		
		$("#levelTab input:checkbox").iCheck("uncheck");
		
		$("#levelTab tr:gt(0)").each(function() {
			if(id.indexOf($(this).attr("id")) >= 0)  $("input:checkbox", $(this)).iCheck("check");
		});
		
		// init check box
		$("#levelTab input").iCheck({checkboxClass: "icheckbox_square-blue"});
		
		// init select all
		$("#l_all").on("ifChecked", function() { $("#levelModal input").iCheck("check"); }).on("ifUnchecked", function() { $("#levelModal input").iCheck("uncheck"); });
	}
	
	function clickLevel($tr) {
		$("input", $tr).iCheck("toggle");
	}
	
	function selectLevel(isClear) {
		if(isClear) {
			$_l_src.attr("val", "").val("");
		} else {
			var $sel = $("#levelModal input:checkbox:checked"), $tr, ids = [], names = [];
			
			$sel.each(function() {
				$tr = $(this).closest("tr");
				if(!$tr.attr("id"))  return true;
				
				ids.push($tr.attr("id"));
				names.push($("td:eq(1)", $tr).text());
			});
			
			$_l_src.attr("val", ids.join(",")).val(names.join(", "));
		}
		
		// if has filter, call search to refresh table
		if(typeof(search) == "function")  search();
		
		$("#levelModal").dialog("close");
	}
</script>
