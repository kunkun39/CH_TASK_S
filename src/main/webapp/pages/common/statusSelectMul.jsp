<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="statusModal" class="hide"><p style="padding: 5px;">
	<table id="statusTab" class="list-table full-table">
		<tr>
			<th width="80px" style="padding-left: 20px;"><input id="s_all" name="_user" type="checkbox" /></th>
			<th width="460px">状态名称</th>
		</tr>
		<tr id="1" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>初始化</div></td>
		</tr>
		<tr id="2" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>待审核</div></td>
		</tr>
		<tr id="3" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>修改中</div></td>
		</tr>
		<tr id="4" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>回归测试中</div></td>
		</tr>
		<tr id="5" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>挂起</div></td>
		</tr>
		<tr id="6" onclick="clickStatus($(this));">
			<td style="padding-left: 20px;"><input name="_status" type="checkbox" /></td>
			<td><div>关闭</div></td>
		</tr>
	</table>
</p></div>

<script>
	var $_s_src;
	
	ppmDialog("#statusModal", "选择状态", {
        "确认": function() { selectStatus(); },
        "清空": function() { selectStatus(true); },
        "取消": function() { $(this).dialog("close"); }
	});
	
	function initStatus($inp) {
		$_s_src = $inp;
		
		// init select statuses
		var id = $inp.attr("val");  id = id ? id : "";
		
		$("#statusTab input:checkbox").iCheck("uncheck");
		
		$("#statusTab tr:gt(0)").each(function() {
			if(id.indexOf($(this).attr("id")) >= 0)  $("input:checkbox", $(this)).iCheck("check");
		});
		
		// init check box
		$("#statusTab input").iCheck({checkboxClass: "icheckbox_square-blue"});
		
		// init select all
		$("#s_all").on("ifChecked", function() { $("#statusModal input").iCheck("check"); }).on("ifUnchecked", function() { $("#statusModal input").iCheck("uncheck"); });
	}
	
	function clickStatus($tr) {
		$("input", $tr).iCheck("toggle");
	}
	
	function selectStatus(isClear) {
		if(isClear) {
			$_s_src.attr("val", "").val("");
		} else {
			var $sel = $("#statusTab input:checkbox:checked"), $tr, ids = [], names = [];
			
			$sel.each(function() {
				$tr = $(this).closest("tr");
				if(!$tr.attr("id"))  return true;
				
				ids.push($tr.attr("id"));
				names.push($("td:eq(1)", $tr).text());
			});
			
			$_s_src.attr("val", ids.join(",")).val(names.join(", "));
		}
		
		// if has filter, call search to refresh table
		if(typeof(search) == "function")  search();
		
		$("#statusModal").dialog("close");
	}
</script>
