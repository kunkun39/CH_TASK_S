<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style>
		.input-text {margin-bottom: 0!important;}
	</style>
</head>

<body>
	<div class="wrapper">
		<div id="btnDiv">
            <%--请选择统计的项目 :--%>
            <%--<select id="reportProject" class="input-text" >--%>
            <%--</select>--%>
            请选择统计的时间 :
            <select id="reportYear" class="input-text" >
                <option value="2015">2015</option>
                <option value="2016">2016</option>
                <option value="2017">2017</option>
            </select>
            <select id="reportMonth" class="input-text" >
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
            </select>
            总共工作天数:
            <input id="reportDays" type="text" class="input-text input-filter"/>
            <button class="button button-rounded button-flat-primary" onclick="generateReport('<c:url value="/bugfile/exportExcel.do"/>')">统计报表</button>
            <a href="<c:url value="/bug/openOperate.do?op=create&fromStatus=0" />" class="button button-rounded button-flat-primary">新建任务</a>

			<span class="view-bar">
				<div class="dropdown">
					<a data-toggle="dropdown" class="dropdown-toggle" href="#"><span id="viewName">默认视图</span><i class="icon-th-list"></i></a>
					
					<ul class="dropdown-menu">
						<li id="defaultView"><a href="#" onclick="selectView();return false;">默认视图</a></li>
						<li class="divider"></li>
						<li style="text-align: center;"><a href="<c:url value="/bug/createOrEditView.do" />"><i class="icon-plus"></i> 新增视图</a></li>
					</ul>
				</div>
			</span>
		</div>
		
		<div id="listDiv">
			<table id="bugTab" class="list-table">
				<tr>
					<th width="50px"></th>
					<th width="80px"><input id="code" type="text" class="input-text input-filter" onkeydown="textSearch();" /></th>
					<th width="300px"><input id="name" type="text" class="input-text input-filter" onkeydown="textSearch();" /></th>
					<th width="200px"><input id="project" type="text" class="input-text input-filter" ondblclick="showProjects($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></th>
					<th width="100px"><input id="status" type="text" class="input-text input-filter" ondblclick="showStatuses($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></th>
					<th width="90px"><input id="level" type="text" class="input-text input-filter" ondblclick="showLevels($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></th>
					<th width="90px"><input id="priority" type="text" class="input-text input-filter" ondblclick="showPriorities($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></th>
					<th width="90px"><input id="creator" type="text" class="input-text input-filter" ondblclick="showUsers($(this));" onkeydown="return false;" /><i onclick="$(this).prev().dblclick();" class="icon-search input-icon"></i></th>
					<th width="100px"></th>
					<th width="210px"></th>
				</tr>
				<tr id="head-tr">
					<th style="text-align: left">序号</th>
					<th style="text-align: left"><span class="head-sort" field="code">编号</span></th>
					<th style="text-align: left"><span class="head-sort" field="name">名称</span></th>
					<th style="text-align: left"><span class="head-sort" field="projectId">所属项目</span></th>
					<th style="text-align: left"><span class="head-sort" field="status">状态</span></th>
					<th style="text-align: left"><span class="head-sort" field="level">严重性</span></th>
					<th style="text-align: left"><span class="head-sort" field="priority">优先级</span></th>
                    <th style="text-align: left"><span class="head-sort" field="creatorId">拥有者</span></th>
                    <th style="text-align: left"><span class="head-sort" field="ownerId">责任人</span></th>
                    <th style="text-align: left"><span class="head-sort" field="requireDays">需要完成时间(H)</span></th>
				</tr>
			</table>

			<div class="pagination"><ul></ul></div>
		</div>
	</div>
	
	<jsp:include page="/pages/common/projectSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/userSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/statusSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/levelSelectMul.jsp"></jsp:include>
	<jsp:include page="/pages/common/prioritySelectMul.jsp"></jsp:include>
	
	<script>
		var sortInfo = [];

		search();
		initView();
		initSort();

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

		function initView() {
			_remoteCall("bug/getViews.do", null, function(data) {
				var info = eval(data), html = "", noSort = true;

				for(var i in info) {
					html += "<li><a href='#' onclick='selectView(\"" + info[i].id + "\");return false;'><span onmouseover='this.title=this.innerText' style='width: 95px;display: inline-block;' class='text-omit'>" + info[i].name + "</span> <i class='icon-pencil' style='float: right;' onclick='editView(\"" + info[i].id + "\");return false;'></i></a></li>";

					if(info[i].isDefault == "Y") {
						$("#viewName").text(info[i].name);

						if(info[i].sortField) {
							noSort = false;
							var $sf = $("span.head-sort[field='" + info[i].sortField + "']");
							$sf.append("<i class='icon-arrow-" + (info[i].sortTo == "asc" ? "up" : "down") + "'></i>");
						}
					}
				}

				if(noSort) {
					$("span.head-sort[field='modifyTime']").append("<i class='icon-arrow-down'></i>");
				}

				$("#defaultView").after(html);
			});

            _remoteCall("project/getProjects.do", null, function(data) {
                var d = eval("(" + data + ")");
                var info = d.projects;

                // remove origin rows
                $("#reportProject").html("");
                var html;
                var number = 1;
                for(var i in info) {
                    html += "<option value='" + info[i].id + "'>" + number + " - " +  info[i].name + "</option>";
                    number ++;
                }
                $("#reportProject").html(html);
            });

            var d = new Date();
            var vYear = d.getFullYear();
            var vMon = d.getMonth() + 1;
            $("#reportYear").val(vYear);
            $("#reportMonth").val(vMon);
		}

		function selectView(viewId) {
			location.href = parent.basePath + "bug/selectView.do?viewId=" + (viewId ? viewId : "");
		}

		function editView(viewId) {
			location.href = parent.basePath + "bug/createOrEditView.do?viewId=" + viewId;
			event.cancelBubble = true;
		}

		function openBug($a) {
			var id = $a.closest("tr").attr("id");
			location.href = parent.basePath + "bug/openBug.do?bugId=" + id;
		}

		function textSearch() {
			if(event.keyCode != 13)  return;
			search();
		}

		function search(toPage) {
			_remoteCall("bug/getBugs.do", {page: toPage ? toPage : 1, sort: sortInfo.join(","), code: $("#code").val(), name: $("#name").val(),
					projectIds: $("#project").attr("val") ? $("#project").attr("val") : "", status: $("#status").attr("val") ? $("#status").attr("val") : "",
					levels: $("#level").attr("val") ? $("#level").attr("val") : "", priorities: $("#priority").attr("val") ? $("#priority").attr("val") : "",
					ownerIds: $("#creator").attr("val") ? $("#creator").attr("val") : ""}, function(data) {

				var dataInfo = eval("(" + data + ")"), page = dataInfo.page, info = dataInfo.bugs, html = "";

				// init bugs table
				for(var i in info) {
					html += "<tr id='" + info[i].id + "'>";
					html += "<td class='sn'>" + getPageSn(page, i) + "</td>";
					html += "<td><div>" + info[i].code + "</div></td>";
					html += "<td><div class='auto-link'><a href='#' onclick='openBug($(this));return false;'>" + info[i].name + "</a></div></td>";
					html += "<td><div>" + info[i].projectName + "</div></td>";
					html += "<td><div>" + info[i].statusName + "</div></td>";
					html += "<td><div>" + info[i].levelName + "</div></td>";
					html += "<td><div>" + info[i].priorityName + "</div></td>";
					html += "<td><div>" + info[i].creator + "</div></td>";
					html += "<td><div>" + info[i].owner + "</div></td>";
					html += "<td><div>" + info[i].requireDays + "</div></td>";
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

        function generateReport(reportUrl) {
            var reportProject = $("#reportProject").val();
            var reportYear = $("#reportYear").val();
            var reportMonth = $("#reportMonth").val();
            var reportDays = $("#reportDays").val();

            if(reportDays == null || reportDays == '') {
                alert("请输入统计时间内工作的天数");
                return;
            } else {
                if(!IsNum(reportDays)) {
                    alert("输入统计时间内工作的天数为数字类型");
                    return;
                }
            }

            var requestUrl = reportUrl + "?reportProject=" + reportProject + "&reportYear=" + reportYear + "&reportMonth=" + reportMonth+ "&reportDays=" + reportDays;
            window.location.href = requestUrl;
        }

        function IsNum(s) {
            if (s != null && s != "") {
                return !isNaN(s);
            }
            return false;
        }
	</script>
</body>
</html>
