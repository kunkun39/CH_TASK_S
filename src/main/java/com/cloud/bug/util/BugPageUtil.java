package com.cloud.bug.util;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.cloud.attach.Attach;
import com.cloud.attach.AttachService;
import com.cloud.bug.model.Bug;
import com.cloud.bug.model.BugField;
import com.cloud.bug.model.BugPageField;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.DateUtil;
import com.cloud.platform.SpringUtil;
import com.cloud.system.service.SystemService;

public class BugPageUtil {
	
	/**
	 * page types
	 */
	public static final int PAGE_VIEW_INIT = 1;
	public static final int PAGE_VIEW_AUDIT = 2;
	public static final int PAGE_VIEW_SOLVE = 3;
	public static final int PAGE_VIEW_TEST = 4;
	public static final int PAGE_VIEW_HANGUP = 5;
	public static final int PAGE_VIEW_CLOSE = 6;
	public static final int PAGE_OP_CREATE_EDIT = 7;
	public static final int PAGE_OP_ASSIGN = 8;
	public static final int PAGE_OP_UNPASS = 9;
	public static final int PAGE_OP_GOTEST = 10;
	public static final int PAGE_OP_SUCCESS = 11;
	public static final int PAGE_OP_FAILURE = 12;
	public static final int PAGE_OP_HANGUP = 13;
	public static final int PAGE_OP_CHGTESTOR = 14;
	public static final int PAGE_OP_CLOSE = 15;
	public static final int PAGE_OP_REOPEN = 16;
	
	/**
	 * operate types
	 */
	public static final String OP_CREATE = "create";
	public static final String OP_EDIT = "edit";
	public static final String OP_COMMIT = "commit";
	public static final String OP_ASSIGN = "assign";
	public static final String OP_UNPASS = "unpass";
	public static final String OP_HANGUP = "hangup";
	public static final String OP_CLOSE = "close";
	public static final String OP_GOTEST = "gotest";
	public static final String OP_SUCCESS = "success";
	public static final String OP_FAILURE = "fail";
	public static final String OP_CHGTESTOR = "chgtestor";
	public static final String OP_REOPEN = "reopen";
	
	/**
	 * html types
	 */
	public static final String HTML_TEXT = "TEXT";
	public static final String HTML_PROJECT = "PROJECT";
	public static final String HTML_USER = "USER";
	public static final String HTML_TEXTAREA = "TEXTAREA";
	public static final String HTML_ATTACH = "ATTACH";
	public static final String HTML_SELECT = "SELECT";
	public static final String HTML_DATE = "DATE";
	public static final String HTML_DATETIME = "DATETIME";
	public static final String HTML_PASSWORD = "PASSWORD";
	public static final String HTML_CHECKBOX = "CHECKBOX";
	
	/**
	 * combine bug oeprate field html
	 * 
	 * @param operate
	 * @param bug
	 * @return
	 * @throws Exception 
	 */
	public static String combineFieldHtml(String operate, Bug bug) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table class='edit-table'>");
		
		Object[] info = getPageFields(operate);
		List<BugPageField> pageFields = (List) info[0];
		boolean isViewPage = (Boolean) info[1];
		
		for(BugPageField f : pageFields) {
			sb.append(fieldHtml(f, bug));
		}
		
		sb.append("</table>");
		
		// specially combine attach field html
		sb.append(combineAttachHtml(bug.getId(), isViewPage, pageFields));
		
		return sb.toString();
	}
	
	/**
	 * combine attach field html
	 * 
	 * @param bugId
	 * @param isViewPage
	 * @param pageFields
	 * @return
	 */
	private static String combineAttachHtml(String bugId, boolean isViewPage,
			List<BugPageField> pageFields) {
		StringBuffer html = new StringBuffer();
		boolean hasAttach = false;
		
		// check if has attach field
		for(BugPageField f : pageFields) {
			if(HTML_ATTACH.equals(f.getField().getHtmlType())) {
				hasAttach = true;
				break;
			}
		}
		
		if(!hasAttach) {
			return html.toString();
		}
		
		// get bug attachments
		AttachService attachService = (AttachService) SpringUtil.getBean("attachService");
		List<Attach> attachs = attachService.searchEntityAttachs(bugId);
		
		// combine attach html
		html.append("<div class='attach'><div class='title'>附件</div>");
		
		if(!isViewPage) {
			html.append("<div style='margin-left: 15px;'><input id='file_upload' type='file' /></div>");
		} else if(attachs.isEmpty()) {
			html.append("<div style='margin-left: 20px;'>无</div>");
		}
		
		for(Attach attach : attachs) {
			html.append("<div class='attach_box'><div class='attach_img'>");
			
			if(Constants.isImage(attach.getExtendType())) {
				String attachPath = Constants.BASEPATH + "upload/" + attach.getId() + "." + attach.getExtendType();
				html.append("<a class='fancybox' rel='group' title='" + attach.getFileName() + "' href='" + attachPath + "'>");
				html.append("<img src='" + attachPath + "' />");
				html.append("</a>");
			} else {
				html.append("<div onclick='downloadAttach(\"" + (attach.getId() + "." + attach.getExtendType()) + "\",");
				html.append("\"" + attach.getFileName() + "\");' class='file_bk'></div>");
			}
			
			html.append("</div><div class='attach_name'>" + attach.getFileName() + "</div></div>");
		}
		
		html.append("</div>");
		return html.toString();
	}
	
	/**
	 * init field html by field html type
	 * 
	 * @param field
	 * @return
	 * @throws Exception 
	 */
	private static String fieldHtml(BugPageField pageField, Bug bug) throws Exception {
		StringBuffer sb = new StringBuffer();
		BugField field = pageField.getField();
		
		if(HTML_ATTACH.equals(field.getHtmlType())) {
			return "";
		}
		
		// get field value
		String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		Method fieldMethod = Bug.class.getMethod(methodName);
		Object v = fieldMethod.invoke(bug);
		String value = v == null ? "" : String.valueOf(v);
		
		// combine html
		sb.append("<tr>");
		sb.append("<td class='left-td' width='80px'>" + field.getLabel() + "</td>");
		sb.append("<td>");
		
		String requireClass = (Constants.VALID_YES.equals(pageField.getIsRequire()) ? " input-require" : "");
		
		// get field column length for validate
		String lengthClass = "";
		Column an = fieldMethod.getAnnotation(Column.class);
		
		if(an != null) {
			lengthClass = " input-maxlen" + an.length();
		}
		
		// text field
		if(HTML_TEXT.equals(field.getHtmlType())) {
			sb.append("<input name='" + field.getName() + "' value='" + value + "' type='text' class='input-text " + requireClass + lengthClass + "' />");
		}
		// project field
		else if(HTML_PROJECT.equals(field.getHtmlType())) {
			sb.append("<input id='" + field.getName() + "' field='relate' type='text' class='input-text " + requireClass + "'");
			sb.append(" ondblclick='showProject($(this));' onkeydown='return false;'");
			sb.append(" val='" + value + "' value='" + BugConstants.getProjectNameById(value) + "' />");
			sb.append("<i onclick='$(this).prev().dblclick();' class='icon-search input-icon'></i>");
			
			sb.append("<input id='_" + field.getName() + "' type='hidden' name='" + field.getName() + "' />");
		}
		// user field
		else if(HTML_USER.equals(field.getHtmlType())) {
			sb.append("<input id='" + field.getName() + "' field='relate' type='text' class='input-text " + requireClass + "'");
			sb.append(" ondblclick='showUser($(this));' onkeydown='return false;'");
			sb.append(" val='" + value + "' value='" + Constants.getUsernameById(value) + "' />");
			sb.append("<i onclick='$(this).prev().dblclick();' class='icon-search input-icon'></i>");
			
			sb.append("<input id='_" + field.getName() + "' type='hidden' name='" + field.getName() + "' />");
		}
		// textarea field
		else if(HTML_TEXTAREA.equals(field.getHtmlType())) {
			sb.append("<textarea name='" + field.getName() + "' class='" + requireClass + lengthClass + "'>" + value + "</textarea>");
		}
		// select field
		else if(HTML_SELECT.equals(field.getHtmlType())) {
			List<String[]> items = BaseInfoUtil.getBaseItems(field.getName());
			
			sb.append("<div id='" + field.getName() + "' class='select-div" + requireClass + "'><ul class='menu-ul'>");
			sb.append("<li>请选择</li>");
			
			for(Object[] item : items) {
				sb.append("<li val='" + item[0] + "'" + (value.equals(item[0]) ? " checked='checked'" : "") + ">" + item[1] + "</li>");
			}
			
			sb.append("</ul></div>");
			
			sb.append("<input id='_" + field.getName() + "' type='hidden' name='" + field.getName() + "' />");
		}
		
		sb.append("</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	
	/**
	 * get page fields
	 * 
	 * @param operate
	 * @param bug
	 * @return
	 */
	public static Object[] getPageFields(String operate) {
		
		// ensure page by operate
		int pageFlag = PAGE_OP_CREATE_EDIT;
		
		if(OP_CREATE.equals(operate) || OP_EDIT.equals(operate) || OP_COMMIT.equals(operate)) {
			pageFlag = PAGE_OP_CREATE_EDIT;
		}
		else if(OP_ASSIGN.equals(operate)) {
			pageFlag = PAGE_OP_ASSIGN;
		}
		else if(OP_UNPASS.equals(operate)) {
			pageFlag = PAGE_OP_UNPASS;
		}
		else if(OP_GOTEST.equals(operate)) {
			pageFlag = PAGE_OP_GOTEST;
		}
		else if(OP_SUCCESS.equals(operate)) {
			pageFlag = PAGE_OP_SUCCESS;
		}
		else if(OP_FAILURE.equals(operate)) {
			pageFlag = PAGE_OP_FAILURE;
		}
		else if(OP_HANGUP.equals(operate)) {
			pageFlag = PAGE_OP_HANGUP;
		}
		else if(OP_CHGTESTOR.equals(operate)) {
			pageFlag = PAGE_OP_CHGTESTOR;
		}
		else if(OP_CLOSE.equals(operate)) {
			pageFlag = PAGE_OP_CLOSE;
		}
		else if(OP_REOPEN.equals(operate)) {
			pageFlag = PAGE_OP_REOPEN;
		}
		
		// get page fields by page flag
		SystemService systemService = (SystemService) SpringUtil.getBean("systemService");
		List<BugPageField> pageFields = systemService.getPageFields(pageFlag);
		
		return new Object[] {pageFields, pageFlag < 7};
	}
	
	/**
	 * combine bug operate page html
	 * 
	 * @param operate
	 * @param bug
	 * @return
	 */
	public static String combineBtnHtml(String operate, Bug bug, String workspace) {
		
		StringBuffer html = new StringBuffer();
		int status = 0;
		
		String returnPage = Constants.VALID_YES.equals(workspace) ? "work/openWork.do" : "pages/bug/bug.jsp";
		returnPage = Constants.BASEPATH + returnPage;
		
		// create operate
		if(OP_CREATE.equals(operate)) {
			html.append("<input type='submit' class='button button-rounded button-flat-primary' value='保存' onclick='setStatus(" + BugConstants.BUG_STATUS_INIT + ");' />");
			html.append("<input type='submit' class='button button-rounded button-flat-primary' value='提交' onclick='setStatus(" + BugConstants.BUG_STATUS_AUDIT + ");' />");
			html.append("<a href='" + returnPage + "' class='button button-rounded button-flat-primary'>返回</a>");
		}
		// edit operate
		else if(OP_EDIT.equals(operate)) {
			html.append("<input type='submit' class='button button-rounded button-flat-primary' value='保存' onclick='setStatus(" + BugConstants.BUG_STATUS_INIT + ");' />");
			html.append("<a href='" + Constants.BASEPATH + "/bug/openBug.do?bugId=" + bug.getId() + "' class='button button-rounded button-flat-primary'>返回</a>");
		}
		// commit operate
		else if(OP_COMMIT.equals(operate)) {
			html.append("<input type='submit' class='button button-rounded button-flat-primary' value='提交' onclick='setStatus(" + BugConstants.BUG_STATUS_AUDIT + ");' />");
			html.append("<a href='" + Constants.BASEPATH + "/bug/openBug.do?bugId=" + bug.getId() + "' class='button button-rounded button-flat-primary'>返回</a>");
		}
		// other operate
		else {
			if(OP_UNPASS.equals(operate)) {
				status = BugConstants.BUG_STATUS_INIT;
			} else if(OP_HANGUP.equals(operate)) {
				status = BugConstants.BUG_STATUS_HANGUP;
			} else if(OP_CLOSE.equals(operate) || OP_SUCCESS.equals(operate)) {
				status = BugConstants.BUG_STATUS_CLOSE;
			} else if(OP_ASSIGN.equals(operate) || OP_FAILURE.equals(operate)) {
				status = BugConstants.BUG_STATUS_SOLVE;
			} else if(OP_GOTEST.equals(operate) || OP_CHGTESTOR.equals(operate)) {
				status = BugConstants.BUG_STATUS_TEST;
			} else if(OP_REOPEN.equals(operate)) {
				status = BugConstants.BUG_STATUS_AUDIT;
			}
			
			html.append("<input type='submit' class='button button-rounded button-flat-primary' value='确定' onclick='setStatus(" + status + ");' />");
			html.append("<a href='" + Constants.BASEPATH + "/bug/openBug.do?bugId=" + bug.getId() + "' class='button button-rounded button-flat-primary'>取消</a>");
		}
		
		return html.toString();
	}
	
	/**
	 * combine bug view page html
	 * 
	 * @param bug
	 * @return
	 */
	public static String combineViewFieldHtml(int pageFlag, Bug bug) throws Exception {
		
		// get page fields by page flag
		SystemService systemService = (SystemService) SpringUtil.getBean("systemService");
		List<BugPageField> pageFields = systemService.getPageFields(pageFlag);
		
		// combine field html
		StringBuffer html = new StringBuffer();
		html.append("<table class='detail-table'>");
		
		for(int i = 0; i < pageFields.size(); i++) {
			BugPageField pageField = pageFields.get(i);
			BugField field = pageField.getField();
			
			if(HTML_ATTACH.equals(field.getHtmlType())) {
				continue;
			}
			
			// get field html
			html.append("<tr" + (i % 2 == 0 ? "" : " class='odd'") + ">");
			html.append("<td class='label-td' width='100px'>" + field.getLabel() + "</td>");
			html.append("<td width='645px'>" + getFieldTextValue(field, bug) + "</td>");
			html.append("</tr>");
		}
		
		html.append("</table>");
		
		// combine attach field html
		html.append(combineAttachHtml(bug.getId(), true, pageFields));
		
		return html.toString();
	}
	
	/**
	 * get specified html type field's text value
	 * 
	 * @param field
	 * @param bug
	 * @return
	 * @throws Exception
	 */
	private static String getFieldTextValue(BugField field, Bug bug) throws Exception {
		String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		Method fieldMethod = Bug.class.getMethod(methodName);
		Object value = fieldMethod.invoke(bug);
		
		if(value == null) {
			return "";
		}
		
		if("status".equals(field.getName())) {
			value = BugUtil.getStatusName((Integer) value); 
		}
		else if(HTML_PROJECT.equals(field.getHtmlType())) {
			value = BugConstants.getProjectNameById((String) value);
		}
		else if(HTML_USER.equals(field.getHtmlType())) {
			value = Constants.getUsernameById((String) value);
		}
		else if(HTML_SELECT.equals(field.getHtmlType())) {
			value = BaseInfoUtil.getItemName(field.getName(), String.valueOf(value));
		}
		else if(HTML_DATE.equals(field.getHtmlType())) {
			value = DateUtil.getDateStr((Date) value);
		}
		else if(HTML_DATETIME.equals(field.getHtmlType())) {
			value = DateUtil.getTimeStr((Date) value);
		}
		
		return (String) value;
	}
}
