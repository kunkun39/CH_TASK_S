package com.cloud.system.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.attach.Attach;
import com.cloud.bug.model.BugField;
import com.cloud.bug.model.BugPageField;
import com.cloud.bug.util.FieldUtil;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.HqlUtil;
import com.cloud.platform.IDao;
import com.cloud.platform.StringUtil;
import com.cloud.system.model.SystemConfig;

@Service
public class SystemService {

	@Autowired
	private IDao dao;
	
	/**
	 * clean system data
	 * 
	 * @param projectIds
	 */
	public void cleanData(String projectIds) {
		
		if(StringUtil.isNullOrEmpty(projectIds)) {
			return;
		}
		
		// get bug attach ids
		StringBuilder hql = new StringBuilder();
		hql.append("select a from Attach a,Bug b where a.entityId = b.id and ");
		hql.append(HqlUtil.combineInHql("b.projectId", projectIds, true, true));
		
		List<Attach> attachs = dao.getAllByHql(hql.toString());
		
		// clean projects
		dao.removeByHql("delete from Project where " + HqlUtil.combineInHql("id", projectIds, true, true));
		
		// clean bug records
		dao.removeByHql("delete from BugRecord r where r.bugId in (select b.id from Bug b where " + HqlUtil.combineInHql("b.projectId", projectIds, true, true) + ")");
		
		// clean bug shares
		dao.removeByHql("delete from BugShare s where s.bugId in (select b.id from Bug b where " + HqlUtil.combineInHql("b.projectId", projectIds, true, true) + ")");
		
		// clean db bug attachs
		dao.removeByHql("delete from Attach a where a.entityId in (select b.id from Bug b where " + HqlUtil.combineInHql("b.projectId", projectIds, true, true) + ")");
		
		// clean bugs
		dao.removeByHql("delete from Bug where " + HqlUtil.combineInHql("projectId", projectIds, true, true));
		
		// clean physical attachs
		File file = null;
		
		for(Attach attach : attachs) {
			file = new File(BugConstants.ROOTPATH + "upload/" + attach.getId() + "." + attach.getExtendType());
			
			if(file != null && file.exists()) {
				file.delete();
			}
		}
	}
	
	/**
	 * remove page field
	 * 
	 * @param pageFieldId
	 */
	public void removePageField(String pageFieldId) {
		
		if(StringUtil.isNullOrEmpty(pageFieldId)) {
			return;
		}
		
		dao.removeById(BugPageField.class, pageFieldId);
	}
	
	/**
	 * get specified page's fields with info
	 * 
	 * @param pageFlag
	 * @return
	 */
	public List<Object[]> getPageFieldInfos(int pageFlag) {
		
		StringBuffer hql = new StringBuffer();
		hql.append("select p.id,p.sortSn,f.label,f.type,f.htmlType,p.isRequire");
		hql.append(" from BugPageField p,BugField f where p.pageFlag = ?");
		hql.append(" and p.fieldId = f.id order by p.sortSn");
		
		List<Object[]> list = dao.getAllByHql(hql.toString(), pageFlag);
		
		for(Object[] info : list) {
			info[3] = FieldUtil.getTypeName((Integer) info[3]);
			info[4] = FieldUtil.getHtmlTypeName((String) info[4]);
			info[5] = Constants.VALID_YES.equals(info[5]) ? "是" : "否";
		}
		
		return list;
	}
	
	/**
	 * get specified page's fields
	 * 
	 * @param pageFlag
	 * @return
	 */
	public List<BugPageField> getPageFields(int pageFlag) {
		
		StringBuffer hql = new StringBuffer();
		hql.append("select p,f from BugPageField p,BugField f");
		hql.append(" where p.pageFlag = ? and p.fieldId = f.id order by p.sortSn");
		
		List<Object[]> list = dao.getAllByHql(hql.toString(), pageFlag);
		
		List<BugPageField> pageFields = new ArrayList();
		
		for(Object[] info : list) {
			BugPageField pageField = ((BugPageField) info[0]);
			pageField.setField((BugField) info[1]);
			
			pageFields.add(pageField);
		}
		
		return pageFields;
	}
	
	/**
	 * save specified page's field
	 * 
	 * @param pageFlag
	 * @param fieldId
	 * @param sortSn
	 * @param isRequire
	 */
	public void savePageField(int pageFlag, String fieldId, int sortSn,
			String isRequire) {
		
		BugPageField pageField = new BugPageField();
		
		pageField.setId(Constants.getID());
		pageField.setPageFlag(pageFlag);
		pageField.setFieldId(fieldId);
		pageField.setSortSn(sortSn);
		pageField.setIsRequire(isRequire);
		
		dao.saveObject(pageField);
	}
	
	/**
	 * get fields
	 * 
	 * @return
	 */
	public List<BugField> getFields() {
		
		List<BugField> fields = dao.getAllByHql("from BugField");
		
		for(BugField f : fields) {
			f.setTypeName(FieldUtil.getTypeName(f.getType()));
			f.setHtmlTypeName(FieldUtil.getHtmlTypeName(f.getHtmlType()));
		}
		
		return fields;
	}
	
	/**
	 * reset system config
	 * 
	 * @param systemConfig
	 */
	public void resetSystemConfig(SystemConfig systemConfig) {
		
		dao.saveObject(systemConfig);
		
		BugConstants.systemConfig = null;
	}
}
