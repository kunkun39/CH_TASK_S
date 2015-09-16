<%@ tag pageEncoding="UTF-8" %>

<%@ tag import="com.cloud.platform.*" %>
<%@ tag import="com.cloud.bug.util.BaseInfoUtil" %>

<%@ attribute name="itemField" %>
<%@ attribute name="itemIds" %>

<% 
	if(StringUtil.isNullOrEmpty(itemIds)) {
		out.print("");
	} else {
		StringBuffer names = new StringBuffer();
		
		String[] idArr = itemIds.split(",");
		
		for(String id : idArr) {
			if(names.length() != 0) {
				names.append(", ");
			}
			names.append(BaseInfoUtil.getItemName(itemField, id));	
		}
		
		out.print(names.toString());
	}
%>