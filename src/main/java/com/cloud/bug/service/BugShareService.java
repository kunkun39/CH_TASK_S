package com.cloud.bug.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.bug.model.BugShare;
import com.cloud.platform.Constants;
import com.cloud.platform.IDao;
import com.cloud.platform.StringUtil;

@Service
public class BugShareService {

	@Autowired
	private IDao dao;
	
	/**
	 * remove bug share
	 * 
	 * @param bugId
	 */
	public void removeShare(String bugId) {
		
		if(StringUtil.isNullOrEmpty(bugId)) {
			return;
		}
		
		dao.removeByHql("delete from BugShare where bugId = ?", bugId);
	}
	
	/**
	 * search bug shares
	 * 
	 * @return
	 */
	public List<Object[]> searchBugShares() {
		
		String hql = "select b.id,b.name,b.intro,b.solveInfo,b.modifierId,s.createTime "
				+ "from Bug b,BugShare s where b.id = s.bugId order by s.createTime desc";
		
		return dao.getAllByHql(hql);
	}
	
	/**
	 * share bug
	 * 
	 * @param bugId
	 */
	public void shareBug(String bugId) {
		
		if(StringUtil.isNullOrEmpty(bugId)) {
			return;
		}
		
		BugShare share = new BugShare();
		
		share.setId(Constants.getID());
		share.setBugId(bugId);
		share.setCreateTime(new Date());
		
		dao.saveObject(share);
	}
}
