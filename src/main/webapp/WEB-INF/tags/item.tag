<%@ tag pageEncoding="UTF-8" import="com.cloud.bug.util.BaseInfoUtil" %>

<%@ attribute name="itemField" %>
<%@ attribute name="itemId" %>

<%=BaseInfoUtil.getItemName(itemField, itemId) %>