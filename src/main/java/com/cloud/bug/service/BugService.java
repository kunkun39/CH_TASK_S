package com.cloud.bug.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.bug.model.Bug;
import com.cloud.bug.model.BugField;
import com.cloud.bug.model.BugPageField;
import com.cloud.bug.model.BugRecord;
import com.cloud.bug.model.BugView;
import com.cloud.bug.util.BaseInfoUtil;
import com.cloud.bug.util.BugPageUtil;
import com.cloud.bug.util.BugUtil;
import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.HqlUtil;
import com.cloud.platform.IDao;
import com.cloud.platform.SpringUtil;
import com.cloud.platform.StringUtil;
import com.cloud.system.model.SystemConfig;

@Service
public class BugService {

	@Autowired
	private IDao dao;
	
	/**
	 * get bug if has share
	 * 
	 * @param bugId
	 * @return
	 */
	public boolean getBugShareStatus(String bugId) {
		
		if(StringUtil.isNullOrEmpty(bugId)) {
			return false;
		}
		
		String hql = "from BugShare where bugId = ?";
		List list = dao.getAllByHql(hql, bugId);
		
		return list.size() > 0;
	}
	
	/**
	 * remove view
	 * 
	 * @param viewId
	 */
	public void removeView(String viewId) {
		
		if(StringUtil.isNullOrEmpty(viewId)) {
			return;
		}
		
		dao.removeById(BugView.class, viewId);
	}
	
	/**
	 * get bug view by id
	 * 
	 * @param viewId
	 * @return
	 */
	public BugView getBugViewById(String viewId) {
		
		if(StringUtil.isNullOrEmpty(viewId)) {
			return new BugView();
		}
		
		return (BugView) dao.getObject(BugView.class, viewId);
	}
	
	/**
	 * search bug views
	 * 
	 * @return
	 */
	public List<BugView> searchViews() {
		
		String hql = "from BugView where creatorId = ? order by createTime";
		
		return dao.getAllByHql(hql, Constants.getLoginUserId());
	}
	
	/**
	 * save bug view
	 * 
	 * @param view
	 */
	public void saveView(BugView view) {
		
		if(view == null) {
			return;
		}
		
		// reset default view
		resetDefaultView(null);
	
		// save view
		view.setIsDefault(Constants.VALID_YES);
		
		if(StringUtil.isNullOrEmpty(view.getId())) {
			view.setId(Constants.getID());
			view.setCreatorId(Constants.getLoginUserId());
			view.setCreateTime(new Date());
			
		} else {
			BugView v = getBugViewById(view.getId());
			view.setCreatorId(v.getCreatorId());
			view.setCreateTime(v.getCreateTime());
		}
		
		dao.saveObject(view);
	}
	
	/**
	 * reset default view
	 * 
	 * @param viewId
	 */
	public void resetDefaultView(String viewId) {
		
		String hql = "update BugView set isDefault = ? where creatorId = ?";
		dao.updateByHql(hql, new Object[] {Constants.VALID_NO, Constants.getLoginUserId()});
		
		if(!StringUtil.isNullOrEmpty(viewId)) {
			hql = "update BugView set isDefault = ? where id = ?";
			dao.updateByHql(hql, new Object[] {Constants.VALID_YES, viewId});
		}
	}
	
	/**
	 * remove bug
	 * 
	 * @param bugId
	 */
	public void removeBug(String bugId) {
		
		if(StringUtil.isNullOrEmpty(bugId)) {
			return;
		}
		
		dao.removeById(Bug.class, bugId);
	}
	
	/**
	 * get bug by id
	 * 
	 * @param bugId
	 * @return
	 */
	public Bug getBug(String bugId) {
		
		if(StringUtil.isNullOrEmpty(bugId)) {
			return new Bug();
		}
		
		Bug bug = (Bug) dao.getObject(Bug.class, bugId);
		
		// set transient attributes
		bug.setOwner(Constants.getUsernameById(bug.getOwnerId()));
		bug.setCreator(Constants.getUsernameById(bug.getCreator()));
		bug.setStatusName(BugUtil.getStatusName(bug.getStatus()));
		bug.setProjectName(BugConstants.getProjectNameById(bug.getProjectId()));
		
		return bug;
	}
	
	/**
	 * search bugs
	 * 
	 * @return
	 */
	public List<Bug> searchBugs(BugSearchVo searchVo) {
		
		// search bugs
		StringBuffer hql = new StringBuffer();
		hql.append("select b,(select u.username from User u where b.ownerId = u.id),");
		hql.append("(select p.name from Project p where b.projectId = p.id), (select u.username from User u where b.creatorId = u.id) from Bug b");
		hql.append(" where 1 = 1");
		
		if(searchVo != null) {
			// combine code filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getCode())) {
				hql.append(" and b.code like '%" + searchVo.getCode() + "%'");
			}
			
			// combine name filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getName())) {
				hql.append(" and b.name like '%" + searchVo.getName() + "%'");
			}
			
			// combine owner filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getOwnerIds())) {
				hql.append(" and " + HqlUtil.combineInHql("b.creatorId", searchVo.getOwnerIds(), true, true));
			}
			
			// combine project filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getProjectIds())) {
				hql.append(" and " + HqlUtil.combineInHql("b.projectId", searchVo.getProjectIds(), true, true));
			}
			
			// combine status filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getStatus())) {
				hql.append(" and " + HqlUtil.combineInHql("b.status", searchVo.getStatus(), false, true));
			}
			
			// combine level filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getLevels())) {
				hql.append(" and " + HqlUtil.combineInHql("b.level", searchVo.getLevels(), true, true));
			}
			
			// combine priority filter condition by search vo
			if(!StringUtil.isNullOrEmpty(searchVo.getPriorities())) {
				hql.append(" and " + HqlUtil.combineInHql("b.priority", searchVo.getPriorities(), true, true));
			}

            // combine the time
            if (searchVo.hasTimeLimit()) {
                hql.append(" and b.createTime >= '" + searchVo.getFromTime() + "' and b.createTime <= '" + searchVo.getEndTime() + "' and b.status=6");
            }
		}
		
		// get current view
		String viewHql = "from BugView where creatorId = ? and isDefault = ?";
		
		List viewList = dao.getAllByHql(viewHql, new Object[] {
						Constants.getLoginUserId(), Constants.VALID_YES });
		
		BugView view = null;
		
		if(!viewList.isEmpty()) {
			view = (BugView) viewList.get(0);
		}
		
		// combine view condition
		combineViewFilter(hql, searchVo, view);
		
		// combine workspace condition
		if(Constants.VALID_YES.equals(searchVo.getWorkspace())) {
			hql.append(" and b.creatorId = '" + Constants.getLoginUserId() + "'");
		}
		
		// combine sort condition
		if(!StringUtil.isNullOrEmpty(searchVo.getSort())) {
			String[] sortInfo = searchVo.getSort().split(",");
			hql.append(" order by b." + sortInfo[0] + " " + sortInfo[1]);
			
		} else if(view != null && !StringUtil.isNullOrEmpty(view.getSortField())) {
			hql.append(" order by b." + view.getSortField() + " " + view.getSortTo());
			
		} else {
			hql.append(" order by b.modifyTime desc");
		}
		
		List<Object[]> list = dao.getPageByHql(hql.toString(), searchVo);
		
		// combine owner name
		Bug b = null;
		List<Bug> bugs = new ArrayList();
		
		for(Object[] o : list) {
			b = (Bug) o[0];
			b.setOwner((String) o[1]);
			b.setProjectName((String) o[2]);
            b.setCreator((String) o[3]);
			b.setStatusName(BugUtil.getStatusName(b.getStatus()));
			b.setLevelName(BaseInfoUtil.getItemName("level", b.getLevel()));
			b.setPriorityName(BaseInfoUtil.getItemName("priority", b.getPriority()));
			
			bugs.add(b);
		}
		
		return bugs;
	}
	
	/**
	 * combine view condition for bugs list
	 * 
	 * @param hql
	 * @param searchVo
	 */
	private void combineViewFilter(StringBuffer hql, BugSearchVo searchVo, BugView view) {

		if(view == null) {
			return;
		}
		
		// set page size
		searchVo.setPageSize(view.getPageSize());
		
		// combine name filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getBugName())) {
			hql.append(" and b.name like '%" + view.getBugName() + "%'");
		}
		
		// combine owner filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getOwnerIds())) {
			hql.append(" and " + HqlUtil.combineInHql("b.ownerId", view.getOwnerIds(), true, true));
		}
		
		// combine project filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getProjectIds())) {
			hql.append(" and " + HqlUtil.combineInHql("b.projectId", view.getProjectIds(), true, true));
		}
		
		// combine status filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getStatus())) {
			hql.append(" and " + HqlUtil.combineInHql("b.status", view.getStatus(), false, true));
		}
		
		// combine level filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getLevels())) {
			hql.append(" and " + HqlUtil.combineInHql("b.level", view.getLevels(), true, true));
		}
		
		// combine priority filter condition by view
		if(!StringUtil.isNullOrEmpty(view.getPriorities())) {
			hql.append(" and " + HqlUtil.combineInHql("b.priority", view.getPriorities(), true, true));
		}
	}
	
	/**
	 * create bug
	 * 
	 * @param bug
	 * @param type
	 * @throws SecurityException 
	 * @throws Exception 
	 */
	public String saveBug(Bug bugVo, int fromStatus, int toStatus, String operate) throws Exception {
		
		// get database bug model
		Bug bug = getBug(bugVo.getId());
		
		// if create bug
		if(StringUtil.isNullOrEmpty(bug.getId())) {
			bug.setId(Constants.getID());
			bug.setCreatorId(Constants.getLoginUserId());
			bug.setCreateTime(new Date());
			
			// set bug code
			bug.setCode(generateBugCode());
		}
		
		bug.setModifyTime(new Date());
		bug.setStatus(toStatus);
		
		// set bug info by page field
		Object[] info = BugPageUtil.getPageFields(operate);
		List<BugPageField> pageFields = (List) info[0];
		
		String fieldName = null;
		Object value = null;
		Method get = null;
		Field set = null;
		
		for(BugPageField pageField : pageFields) {
			BugField f = pageField.getField();
			
			if(BugPageUtil.HTML_ATTACH.equals(f.getHtmlType())) {
				continue;
			}
			
			fieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
			get = Bug.class.getMethod("get" + fieldName);
			set = Bug.class.getField(f.getName());
			
			// get value from web bug
			value = get.invoke(bugVo);
			
			// if field is textarea field, convert line html
			if(BugPageUtil.HTML_TEXTAREA.equals(f.getHtmlType())) {
				value = value.toString().replaceAll("\r\n", "<br>");
			}
			
			// set value to model bug
			set.set(bug, value);
		}
		
		// set owner
		if(toStatus == BugConstants.BUG_STATUS_INIT) {
			bug.setOwnerId(bug.getCreatorId());
		}
		else if(toStatus == BugConstants.BUG_STATUS_AUDIT || toStatus == BugConstants.BUG_STATUS_HANGUP) {
			bug.setOwnerId(bug.getAuditorId());
		}
		else if(toStatus == BugConstants.BUG_STATUS_SOLVE) {
			bug.setOwnerId(bug.getModifierId());
		}
		else if(toStatus == BugConstants.BUG_STATUS_TEST) {
			bug.setOwnerId(bug.getTestorId());
		}
		else if(toStatus == BugConstants.BUG_STATUS_CLOSE) {
			bug.setOwnerId(null);
		}
		
		dao.saveObject(bug);
		
		// trigger send email event
		BugMailService mailService = (BugMailService) SpringUtil.getBean("bugMailService");
		mailService.sendStatusMail(toStatus, bug.getOwnerId(), Constants.getLoginUserId(), operate, bug);
		
		// save bug operate record
		BugRecord record = new BugRecord();
		
		record.setBugId(bug.getId());
		record.setOperate(operate);
		record.setNote(bugVo.getNote());
		record.setFromStatus(fromStatus);
		record.setToStatus(toStatus);
		record.setCreatorId(Constants.getLoginUserId());
		record.setCreateTime(new Date());
		
		dao.saveObject(record);
		
		return bug.getId();
	}
	
	/**
	 * search bug's operate records
	 * 
	 * @param bugId
	 * @return
	 */
	public List<BugRecord> searchBugOperateRecords(String bugId, BugSearchVo searchVo) {
		
		// search bug records
		StringBuffer hql = new StringBuffer();
		hql.append("select r,b.name from BugRecord r,Bug b where r.bugId = b.id");
		
		if(!StringUtil.isNullOrEmpty(bugId)) {
			hql.append(" and r.bugId = '" + bugId + "'");
		}
		
		hql.append(" order by r.createTime desc");
		
		List<Object[]> list = null;
		
		if(!StringUtil.isNullOrEmpty(bugId)) {
			list = dao.getAllByHql(hql.toString());
		} else {
			list = dao.getPageByHql(hql.toString(), searchVo);
		}
		
		// combine bug name
		BugRecord r = null;
		List<BugRecord> records = new ArrayList();
		
		for(Object[] o : list) {
			r = (BugRecord) o[0];
			r.setBugName((String) o[1]);
			r.setCreator(Constants.getUsernameById(r.getCreatorId()));
			r.setOpName(BugConstants.getOperateName(r.getOperate()));
			
			records.add(r);
		}
		
		return records;
	}
	
	/**
	 * generate bug code
	 * 
	 * @return
	 */
	private String generateBugCode() {
		StringBuffer code = new StringBuffer();
		
		code.append("Task");
		
		// append code sn
		SystemConfig systemConfig = BugConstants.getSystemConfig();
		code.append(systemConfig.getCodeSn());
		
		// increase code sn
		systemConfig.setCodeSn(systemConfig.getCodeSn() + 1);
		dao.saveObject(systemConfig);
		BugConstants.systemConfig = null;
		
		return code.toString();
	}
}
