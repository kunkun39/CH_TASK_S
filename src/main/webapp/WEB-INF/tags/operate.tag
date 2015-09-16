<%@ tag pageEncoding="UTF-8" import="com.cloud.platform.BugConstants" %>

<%@ attribute name="op" %>

<%
	out.print(BugConstants.getOperateName(op));
%>