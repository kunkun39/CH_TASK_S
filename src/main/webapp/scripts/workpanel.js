$(function() {
	$("#containerTab").width(document.body.clientWidth - 45);
	$("td.col").css({"max-width": document.body.clientWidth / col - wspan, "min-width": document.body.clientWidth / col - wspan});
	
	// init my projects
	for(var i in pinfo) {
		html += "<tr id='" + pinfo[i].id + "'>";
		html += "<td class='sn'>" + (parseInt(i) + 1) + "</td>";
		html += "<td><div class='auto-link'><a href='#' onclick='openProject($(this));return false;'>" + pinfo[i].name + "</a></div></td>";
		html += "<td><div>" + getDateStr(pinfo[i].startDate) + "</div></td>";
		html += "<td><div>" + getDateStr(pinfo[i].endDate) + "</div></td>";
		html += "<td><div>" + getTimeStr(pinfo[i].createTime) + "</div></td>";
		html += "</tr>";
	}

	$("#projectTab").append(html);

	// init my bugs view
	_remoteCall("bug/getViews.do", null, function(data) {
		var info = eval(data), html = "";
		
		for(var i in info) {
			html += "<li><a href='#' onclick='selectView(\"" + info[i].id + "\");return false;'><span onmouseover='this.title=this.innerText' style='width: 115px;display: inline-block;' class='text-omit'>" + info[i].name + "</span></a></li>";
			if(info[i].isDefault == "Y")  $("#viewName").text(info[i].name);
		}
		
		$("#defaultView").after(html);
	});

	// init my bugs
	search();
	initSort();
	
	// init bug status chart
	initPieChart("statusChart", "任务状态统计图", statusStat);

	// init project bug chart
	initPieChart("bugProjectChart", "项目任务统计图", bugProjectChart);

	// init system activities
	html = combineRecordHtml(rinfo);

	if(hasMore == "Y") {
		html += "<div id='hasMore' class='more' onclick='showMoreRecord();'>显示更多 <i class='icon-chevron-down' style='vertical-align: middle;margin-left: 3px;margin-bottom: 6px;'></i></div>";
	}

	$("#recordContent").html(html);
});

/**
 * ==========================  functions  ==========================
*/
function search(toPage) {
	_remoteCall("bug/getBugs.do", {workspace: "Y", page: toPage ? toPage : 1, sort: sortInfo.join(","), code: $("#code").val(), name: $("#name").val(), projectIds: $("#project").attr("val") ? $("#project").attr("val") : "", status: $("#status").attr("val") ? $("#status").attr("val") : ""}, function(data) {
		var dataInfo = eval("(" + data + ")"), page = dataInfo.page, info = dataInfo.bugs, html = "";
		
		// init bugs table
		for(var i in info) {
			html += "<tr id='" + info[i].id + "'>";
			html += "<td class='sn'>" + getPageSn(page, i) + "</td>";
			html += "<td><div>" + info[i].code + "</div></td>";
			html += "<td><div class='auto-link'><a href='#' onclick='openBug($(this));return false;'>" + info[i].name + "</a></div></td>";
			html += "<td><div>" + info[i].projectName + "</div></td>";
			html += "<td><div>" + info[i].statusName + "</div></td>";
			html += "<td><div>" + getTimeStr(info[i].modifyTime) + "</div></td>";
			html += "</tr>";
		}
		
		// remove old trs first
		$("#bugTab tr:gt(1)").remove();
		$("#bugTab").append(html);
		
		// init page bar
		initPage(page);
		
		parent.autoHeight();
	});
}

function textSearch() {
	if(event.keyCode != 13)  return;
	search();
}

function initSort() {
	$("span.head-sort").click(function() {
		sortInfo = [];
		var $i = $("#head-tr i"), $spanI = $("i", $(this));
		sortInfo.push($(this).attr("field"));
		
		if($spanI.size() == 0) {
			$(this).append($i.removeClass("icon-arrow-down").addClass("icon-arrow-up"));
			sortInfo.push("asc");
		} else {
			if($spanI.hasClass("icon-arrow-up")) {
				$spanI.removeClass("icon-arrow-up").addClass("icon-arrow-down");
				sortInfo.push("desc");
			} else {
				$spanI.removeClass("icon-arrow-down").addClass("icon-arrow-up");
				sortInfo.push("asc");
			}
		}
		
		search();
	});
}

function selectView(viewId) {
	location.href = parent.basePath + "work/selectView.do?viewId=" + (viewId ? viewId : "") + "&col=" + col;
}

function openProject($a) {
	var id = $a.closest("tr").attr("id");
	location.href = parent.basePath + "project/openProject.do?projectId=" + id + "&workspace=Y";
}

function openBug($a, noTr) {
	var id = !noTr ? $a.closest("tr").attr("id") : $a.attr("id");
	location.href = parent.basePath + "bug/openBug.do?bugId=" + id + "&workspace=Y";
}

var page = 1;

function combineRecordHtml(info) {
	var html = "";
	var fixW = document.body.clientWidth / col - wspan - 60 - 155;
	
	for(var i in info) {
    	html += "<div class='record_box'>";
    	html += "<div>";
    	html += "<span class='record-1'>" + info[i].creator + "</span>";
    	html += "<span class='record-2' style='min-width: " + fixW + "px;max-width: " + fixW + "px;margin-right: 5px;'>" + info[i].opName + " <a id='" + info[i].bugId + "' onclick='openBug($(this), true);return false;' href='#' onmouseover='this.title=this.innerText'>" + info[i].bugName + "</a></span>";
		html += "<span class='record-3'>" + getTimeStr(info[i].createTime, true) + "</span>";
		html += "</div>";
		html += "<div class='record_note'>" + info[i].note + "</div>";
		html += "</div>";
    }
	
	return html;
}

function showMoreRecord() {
	_remoteCall("/work/moreRecord.do", {page: ++page}, function(data) {
		
		var d = eval("(" + data + ")"), hasMore = d.hasMore, info = d.records;
		
		$("#hasMore").before(combineRecordHtml(info));
		if(hasMore == "N")  $("#hasMore").remove();
		
		parent.autoHeight();
	});	
}