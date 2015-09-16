<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="status" %>
<%@ attribute name="records" type="java.util.List" %>

<div class="workflow">
	<div class="title">流程图</div>

	<div class="graph">
	<svg width="640" height="380" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
		<defs>
			<marker orient="auto" markerHeight="20" markerWidth="8" markerUnits="strokeWidth" refY="10" refX="0" viewBox="0 0 20 20" id="arrow">
			   <path stroke="black" d="m0,0l20,10l-20,10l0,-20z" />
			</marker>
		</defs>
	 
	    <!-- 步骤图标 -->
	    <image id="status_0" x="17" y="156" width="32" height="32" xlink:href="..//..//img///start.png" />
	    <image id="status_1" xlink:href="..//..//img///node.png" height="32" width="32" y="39" x="82" />
	    <image id="status_2" x="157" y="155" width="32" height="32" xlink:href="..//..//img///node.png" />
	    <image id="status_3" xlink:href="..//..//img///node.png" height="32" width="32" y="155" x="310" />
	    <image id="status_4" xlink:href="..//..//img///node.png" height="32" width="32" y="155" x="449" />
	  	<image id="status_5" xlink:href="..//..//img///node.png" height="32" width="32" y="41" x="232" />
	  	<image id="status_6" x="591" y="153" width="32" height="32" xlink:href="..//..//img///end.png" />
	  	
	  	<!-- 步骤文字 -->
	  	<text id="svg_30" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="203" x="33"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">Start</text>
				
		<text stroke="#000000" id="svg_35" xml:space="preserve"
				text-anchor="middle" font-family="serif" font-size="12" y="84" x="99"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" fill="#000000">初始化</text>
				
		<text style="cursor: move;" id="svg_31" xml:space="preserve"
				text-anchor="middle" font-family="serif" font-size="12" y="202"
				x="174" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="0" stroke="#000000"
				fill="#000000">待审核</text>
				
		<text id="svg_36" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="87" x="248"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">挂起</text>
				
		<text id="svg_32" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="202" x="328"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">修改中</text>
				
	  	<text id="svg_33" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="201" x="469"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">回归测试中</text>
				
	  	<text id="svg_34" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="200" x="608"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">关闭</text>
		
		<!-- 操作线及文字 -->	
		<line id="create_0" marker-end="url(#arrow)" stroke="#000000" y2="86.999999" x2="81" y1="153"
				x1="38" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="1" fill="none" />
		<text id="svg_28" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="112" x="49"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">保存</text>
				
				
	  	<line id="commit_0" marker-end="url(#arrow)" fill="none"
				stroke="#000000" stroke-width="1" stroke-dasharray="null"
				stroke-linejoin="null" stroke-linecap="null" x1="51" y1="169"
				x2="152" y2="169" />
	  	<text xml:space="preserve" text-anchor="middle" font-family="serif"
				font-size="12" id="svg_5" y="183" x="96" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="0"
				stroke="#000000" fill="#000000">提交</text>
				
				
		<line id="commit_1" marker-end="url(#arrow)" stroke="#000000" y2="149" x2="163" y1="87.000002"
				x1="123.000001" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="1" fill="none" />
		<text id="svg_29" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="109" x="155"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">提交</text>
				
				
		<line id="unpass_2" marker-end="url(#arrow)" stroke="#000000" y2="93" x2="115.000001" y1="153"
				x1="155" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="1" fill="none" />
		<text transform="rotate(56.8887 126 129.5)" id="svg_37"
				xml:space="preserve" text-anchor="middle" font-family="serif"
				font-size="11" y="133" x="126" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="0"
				stroke="#000000" fill="#000000">审核不通过</text>
				
			
		<line id="hangup_2" marker-end="url(#arrow)" stroke="#000000" y2="83.499999" x2="227.5"
				y1="149.499999" x1="186.5" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="1"
				fill="none" />	
		<text id="svg_38" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="108" x="198"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">挂起</text>
				
				
		<line id="assign_2" marker-end="url(#arrow)" fill="none"
				stroke="#000000" stroke-width="1" stroke-dasharray="null"
				stroke-linejoin="null" stroke-linecap="null" x1="200.5" y1="170"
				x2="301.5" y2="170" />
		<text id="svg_39" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="165" x="257"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">指定修改</text>
				
				
		<line id="assign_5" stroke="#000000" marker-end="url(#arrow)" y2="151" x2="311" y1="92"
				x1="265.999998" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="1" fill="none" />
		<text transform="rotate(50.7106 278 126)" id="svg_40"
				xml:space="preserve" text-anchor="middle" font-family="serif"
				font-size="12" y="130" x="278" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="0"
				stroke="#000000" fill="#000000">指定修改</text>
				
				
		<line id="hangup_3" marker-end="url(#arrow)" stroke="#000000" y2="84.999999" x2="273" y1="147"
				x1="317" stroke-linecap="null" stroke-linejoin="null"
				stroke-dasharray="null" stroke-width="1" fill="none" />
		<text id="svg_41" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="110" x="308"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">挂起</text>
				
				
		<line id="gotest_3" marker-end="url(#arrow)" fill="none"
				stroke="#000000" stroke-width="1" stroke-dasharray="null"
				stroke-linejoin="null" stroke-linecap="null" x1="347.5" y1="170"
				x2="448.5" y2="170" />
		<text id="svg_42" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="163" x="397"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">提交测试</text>
				
			
		<line id="fail_4" stroke="#000000" marker-end="url(#arrow)"
				fill="none" stroke-width="1" stroke-dasharray="null"
				stroke-linejoin="null" stroke-linecap="null" x1="448.5" y1="178"
				x2="347.5" y2="178" />	
		<text id="svg_43" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="192" x="399"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">测试不通过</text>
				
				
	  	<line id="success_4" marker-end="url(#arrow)" fill="none"
				stroke="#000000" stroke-width="1" stroke-dasharray="null"
				stroke-linejoin="null" stroke-linecap="null" x1="488.5" y1="170"
				x2="589.5" y2="170" />
	  	<text id="svg_44" xml:space="preserve" text-anchor="middle"
				font-family="serif" font-size="12" y="164" x="541"
				stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
				stroke-width="0" stroke="#000000" fill="#000000">测试通过</text>
				
		 <path id="close_2" marker-end="url(#arrow)" stroke="#000000" 
				d="m165,206.7907l0,75.2093l447,0l-2,-77" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="1"
				fill="none" />
  		<text stroke="#000000" id="svg_19" fill="#000000" stroke-width="0"
				stroke-dasharray="null" stroke-linejoin="null" stroke-linecap="null"
				x="274" y="295" font-size="12" font-family="serif"
				text-anchor="middle" xml:space="preserve">直接关闭</text>
				
  		<path marker-end="reopen_6" stroke="#000000" id="svg_24"
				d="m602,206l2,64.6875l-429,4.3125l-1,-64.6875" stroke-linecap="null"
				stroke-linejoin="null" stroke-dasharray="null" stroke-width="1"
				fill="none" />
  		<text id="svg_25" fill="#000000" stroke="#000000" stroke-width="0"
				stroke-dasharray="null" stroke-linejoin="null" stroke-linecap="null"
				x="464" y="265" font-size="12" font-family="serif"
				text-anchor="middle" xml:space="preserve">重新开启</text>
				
		<!-- two change owner line -->
		<path id="assign_3"  marker-end="url(#arrow)" stroke="#000000"
			transform="rotate(119.255 328.81 210.969)"
			d="m319.01501,196.439c28.995,0.50101 17.22897,29.06 17.22897,29.06c0,0 -17.22897,-29.06"
			stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
			stroke-width="1" fill="none" />
  		<text id="svg_47" stroke="#000000" fill="#000000" stroke-width="0"
			stroke-dasharray="null" stroke-linejoin="null" stroke-linecap="null"
			x="335" y="229.96899" font-size="12" font-family="serif"
			text-anchor="middle" xml:space="preserve">更换责任人</text>
			
			
		<path id="chgtestor_4" marker-end="url(#arrow)" stroke="#000000" transform="rotate(-61.3733 464.81 146)"
			d="m455.01501,131.47c28.995,0.50101 17.22897,29.06 17.22897,29.06c0,0 -17.22897,-29.06"
			stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null"
			stroke-width="1" fill="none" />
		<text style="cursor: move;" stroke="#000000" id="svg_45"
			fill="#000000" stroke-width="0" stroke-dasharray="null"
			stroke-linejoin="null" stroke-linecap="null" x="464" y="130"
			font-size="12" font-family="serif" text-anchor="middle"
			xml:space="preserve">更换责任人</text>
		
			
		<!-- select node rect -->
		<rect id="rect" height="38" width="38" y="153" x="14" stroke-width="1" stroke="red" fill="none" class="hide" />
	</svg>
	</div>

</div>

<script>
	var status = "${status}";
	
	// init select node
	var $node = $("#status_" + status), $rect = $("#rect");
	$rect.attr({"x": parseInt($node.attr("x")) - 3, "y": parseInt($node.attr("y")) - 3}).show();
	
	// get operates
	var ops = [], op, fs, ts;
	
	<c:forEach var="record" items="${records}">
		op = "${record.operate}", fs = "${record.fromStatus}", to = "${record.toStatus}";
		
		if(op == "create" && to == "2") {
			op = "commit";
		}
		
		ops.push(op + "_" + fs);
	</c:forEach>
	
	for(var i in ops) {
		$("#" + ops[i]).attr("stroke", "red");
	}
</script>