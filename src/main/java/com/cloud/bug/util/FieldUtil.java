package com.cloud.bug.util;

public class FieldUtil {

	public static final int FIELD_SYSTEM = 0;
	public static final int FIELD_CUSTOM = 1;
	
	/**
	 * get field html type name
	 * 
	 * @param htmlType
	 * @return
	 */
	public static String getHtmlTypeName(String htmlType) {
		String name = "";
		
		if(BugPageUtil.HTML_TEXT.equals(htmlType)) {
			name = "文本";
		}
		else if(BugPageUtil.HTML_PROJECT.equals(htmlType)) {
			name = "项目";
		}
		else if(BugPageUtil.HTML_USER.equals(htmlType)) {
			name = "用户";
		}
		else if(BugPageUtil.HTML_TEXTAREA.equals(htmlType)) {
			name = "文本域";
		}
		else if(BugPageUtil.HTML_ATTACH.equals(htmlType)) {
			name = "附件";
		}
		else if(BugPageUtil.HTML_SELECT.equals(htmlType)) {
			name = "下拉框";
		}
		else if(BugPageUtil.HTML_DATE.equals(htmlType)) {
			name = "日期";
		}
		else if(BugPageUtil.HTML_DATETIME.equals(htmlType)) {
			name = "时间";
		}
		
		return name;
	}
	
	/**
	 * get field type name
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeName(int type) {
		if(FIELD_SYSTEM == type) {
			return "系统字段";
		} else {
			return "自定义字段";
		}
	}
}
