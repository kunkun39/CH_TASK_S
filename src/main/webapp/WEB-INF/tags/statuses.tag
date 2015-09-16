<%@ tag pageEncoding="UTF-8" %>
<%@ tag import="com.cloud.platform.*" %>
<%@ tag import="com.cloud.bug.util.BugUtil" %>

<%@ attribute name="ids" %>

<% 
	if(StringUtil.isNullOrEmpty(ids)) {
		out.print("");
	} else {
		StringBuffer names = new StringBuffer();
		
		String[] idArr = ids.split(",");
		
		for(String id : idArr) {
			if(names.length() != 0) {
				names.append(", ");
			}
			names.append(BugUtil.getStatusName(Integer.parseInt(id)));	
		}
		
		out.print(names.toString());
	}
%>