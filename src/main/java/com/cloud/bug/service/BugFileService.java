package com.cloud.bug.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.bug.model.Bug;
import com.cloud.platform.BugConstants;
import com.cloud.platform.Constants;
import com.cloud.platform.DateUtil;
import com.cloud.platform.ExcelUtil;
import com.cloud.platform.IDao;
import com.cloud.platform.StringUtil;

@Service
public class BugFileService {
	
	@Autowired
	private IDao dao;
	
	/**
	 * import bugs by import data file
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public boolean importBugs(Sheet sheet, int rowNum, int colNum) {
		Row row = null;
		Cell cell = null;
		String name = null, value = null;
		Bug bug = null;
		
		ExcelUtil.import_status.append("<div class='title'>批量导入</div>");
		ExcelUtil.import_status.append("<div class='wrapDiv'>");
		
		// iterate row
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            bug = new Bug();
            
            // init bug property
            bug.setId(Constants.getID());
            bug.setStatus(BugConstants.BUG_STATUS_INIT);
            bug.setCreatorId(Constants.getLoginUserId());
            bug.setCreateTime(new Date());
            bug.setModifyTime(new Date());
            
            // iterate cell
            for(int j = 0; j < colNum; j++) {
            	cell = row.getCell((short) j);
            	value = ExcelUtil.getStringCellValue(cell);
            	
            	// name field
            	if(j == 0) {
            		name = value;
            		bug.setName(value);
            	}
            	// project field
            	else if(j == 1) {
            		bug.setProjectId(BugConstants.getProjectIdByName(value));
            	}
            	// owner field
            	else if(j == 2) {
            		bug.setOwner(value);
            	}
            }
            
            // save import bug
            dao.saveObject(bug);
            
            // write status
            ExcelUtil.import_status.append("任务 " + name + " 导入成功。<br>");
        }
		
		// set over status
        ExcelUtil.import_status.append("over");
        
        ExcelUtil.import_status.append("</div>");
        
		return true;
	}
	
	/**
	 * check import data valid
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public boolean checkImportData(Sheet sheet, int rowNum, int colNum) {
		Row row = null;
		Cell cell = null;
		boolean valid = true, totalValid = true;
		String wrongTip = null;
		
		ExcelUtil.import_status.append("<div class='title'>检查数据有效性</div>");
		ExcelUtil.import_status.append("<div class='wrapDiv'>");
		
		// iterate row
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            valid = true;
            
            // iterate cell
            for(int j = 0; j < colNum; j++) {
            	cell = row.getCell((short) j);
            	
            	if(StringUtil.isNullOrEmpty(ExcelUtil.getStringCellValue(cell))) {
            		// name is empty
            		if(j == 0) {
            			wrongTip = "任务名称为空";
            		}
            		// project is empty
            		if(j == 1) {
            			wrongTip = "项目名称为空，没有选择所属项目";
            		}
            		
            		valid = false;
            		totalValid = false;
            		break;
            	}
            }
            
            if(valid) {
            	ExcelUtil.import_status.append("第" + i + "条数据正确。<br>");
            } else {
            	ExcelUtil.import_status.append("<span class='outline'>第" + i + "条数据错误，" + wrongTip + "！</span><br>");
            }
        }
        
        // set over status
        if(!totalValid) {
	        ExcelUtil.import_status.append("over");
        }
        
        ExcelUtil.import_status.append("</div>");
		
		return totalValid;
	}
	
	/**
	 * export excel template file for import
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String exportTemplate() throws Exception {
		
		// combine export excel path
		String path = BugConstants.ROOTPATH + "temp/" + Constants.getID() + ".xls";
		
		// checkout dir exists
		File dir = new File(BugConstants.ROOTPATH + "temp/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		// init workbook and sheet
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("template");
		Row row = null;
		Cell cell = null;
		
		// set column width
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 4000);
		
		// init head row
		row = sheet.createRow(0);
		row.setHeight((short) 400);
		
		CellStyle head = wb.createCellStyle();
		head.setAlignment(CellStyle.ALIGN_CENTER);
		head.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		head.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		head.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		cell = row.createCell(0);
		cell.setCellValue("名称");
		cell.setCellStyle(head);
		
		cell = row.createCell(1);
		cell.setCellValue("所属项目");
		cell.setCellStyle(head);
		
		cell = row.createCell(2);
		cell.setCellValue("责任人");
		cell.setCellStyle(head);
		
		// set project select validation
		BugConstants.getProjectNameById("");
		Collection<String> nameCol = BugConstants.projectNameIdMap.values();
		String[] projectNames = nameCol.toArray(new String[nameCol.size()]);
		
		CellRangeAddressList regions = new CellRangeAddressList(1, 10000, 1, 1);
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(projectNames);
		HSSFDataValidation data_validation = new HSSFDataValidation(regions,
				constraint);
		sheet.addValidationData(data_validation);

		// set owner select validation
		Constants.getUserIdByName("");
		nameCol = Constants.userIdNameMap.values();
		String[] userNames = nameCol.toArray(new String[nameCol.size()]);
		
		regions = new CellRangeAddressList(1, 10000, 2, 2);
		constraint = DVConstraint.createExplicitListConstraint(userNames);
		data_validation = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(data_validation);
		
		// write excel to disk
		FileOutputStream out = new FileOutputStream(path);
		wb.write(out);
		out.close();
		
		return path;
	}

	/**
	 * export bug list excel
	 * 
	 * @param bugs
	 * @return
	 * @throws Exception 
	 */
	public String exportExcel(List<Bug> bugs, int reportDays) throws Exception {
		
		if(bugs == null || bugs.isEmpty()) {
			return "";
		}

        int allTotal = 0;
        Map<String, List<Bug>> persons = new HashMap<String, List<Bug>>();
        Map<String, List<Bug>> projects = new HashMap<String, List<Bug>>();
        for (Bug bug : bugs) {
            String person = bug.getCreator();
            String project = bug.getProjectName();

            List<Bug> personBugList = persons.get(person);
            if (personBugList == null) {
                personBugList = new ArrayList<Bug>();
            }
            personBugList.add(bug);
            persons.put(person, personBugList);

            List<Bug> projectBugList = projects.get(project);
            if (projectBugList == null) {
                projectBugList = new ArrayList<Bug>();
            }
            projectBugList.add(bug);
            projects.put(project, projectBugList);

            allTotal = allTotal + bug.getRequireDays();
        }

		// combine export excel path
		String path = BugConstants.ROOTPATH + "temp/" + Constants.getID() + ".xls";
		
		// checkout dir exists
		File dir = new File(BugConstants.ROOTPATH + "temp/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		// init workbook and sheet
		Workbook wb = new HSSFWorkbook();

        createPersonTaskSheet(reportDays, persons, wb);
        createProjectTaskSheet(allTotal, projects, wb);

		// write excel to disk
		FileOutputStream out = new FileOutputStream(path);
		wb.write(out);
		out.close();
		
		return path;
	}

    private void createPersonTaskSheet(int reportDays, Map<String, List<Bug>> persons, Workbook wb) {
        Sheet sheet = wb.createSheet("项目成员任务完成情况");
        Row row = null;
        Cell cell = null;
        int count = 0;

        // set column width
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);

        CellStyle first = wb.createCellStyle();
        first.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle head = wb.createCellStyle();
        head.setAlignment(CellStyle.ALIGN_CENTER);
        head.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        head.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        head.setFillPattern(CellStyle.SOLID_FOREGROUND);

        row = sheet.createRow(count++);
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(range);
        cell = row.createCell(0);
        cell.setCellValue("总共统计天数:" + reportDays + "天(" + (reportDays * 8) + "H)");
        cell.setCellStyle(first);

        // init head row
        row = sheet.createRow(count++);
        row.setHeight((short) 400);

        cell = row.createCell(0);
        cell.setCellValue("任务完成者");
        cell.setCellStyle(head);

        cell = row.createCell(1);
        cell.setCellValue("所属项目");
        cell.setCellStyle(head);

        cell = row.createCell(2);
        cell.setCellValue("任务名称");
        cell.setCellStyle(head);

        cell = row.createCell(3);
        cell.setCellValue("状态");
        cell.setCellStyle(head);

        cell = row.createCell(4);
        cell.setCellValue("创建时间");
        cell.setCellStyle(head);

        cell = row.createCell(5);
        cell.setCellValue("完成时间(H)");
        cell.setCellStyle(head);

        // export bugs list
        Set<String> personKeys = persons.keySet();
        for (String personKey : personKeys) {
            row = sheet.createRow(count++);
            cell = row.createCell(0);
            cell.setCellValue(personKey);

            List<Bug> list = persons.get(personKey);
            int totalHour = 0;
            for(Bug bug : list) {
                row = sheet.createRow(count++);

                cell = row.createCell(1);
                cell.setCellValue(bug.getProjectName());

                cell = row.createCell(2);
                cell.setCellValue(bug.getName());

                cell = row.createCell(3);
                cell.setCellValue(bug.getStatusName());

                cell = row.createCell(4);
                cell.setCellValue(bug.getCreateTime().toString());

                cell = row.createCell(5);
                cell.setCellValue(bug.getRequireDays() + "");

                totalHour = totalHour + bug.getRequireDays();
		    }

            row = sheet.createRow(count++);
            cell = row.createCell(4);
            cell.setCellValue("工作负荷：" + (totalHour * 100)/(reportDays * 8) + "%");
            cell = row.createCell(5);
            cell.setCellValue("总共工作时间：" + totalHour + "h");
        }
    }

    private void createProjectTaskSheet(int allTotal, Map<String, List<Bug>> projects, Workbook wb) {
        Sheet sheet = wb.createSheet("项目任务情况统计");
        Row row = null;
        Cell cell = null;
        int count = 0;

        // set column width
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);

        // init head row
        row = sheet.createRow(count++);
        row.setHeight((short) 400);

        CellStyle head = wb.createCellStyle();
        head.setAlignment(CellStyle.ALIGN_CENTER);
        head.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        head.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        head.setFillPattern(CellStyle.SOLID_FOREGROUND);

        cell = row.createCell(0);
        cell.setCellValue("项目名称");
        cell.setCellStyle(head);

        cell = row.createCell(1);
        cell.setCellValue("任务名称");
        cell.setCellStyle(head);

        cell = row.createCell(2);
        cell.setCellValue("责任人");
        cell.setCellStyle(head);

        cell = row.createCell(3);
        cell.setCellValue("状态");
        cell.setCellStyle(head);

        cell = row.createCell(4);
        cell.setCellValue("创建时间");
        cell.setCellStyle(head);

        cell = row.createCell(5);
        cell.setCellValue("完成时间(H)");
        cell.setCellStyle(head);

        // export bugs list
        Set<String> projectKeys = projects.keySet();
        for (String projectKey : projectKeys) {
            row = sheet.createRow(count++);
            cell = row.createCell(0);
            cell.setCellValue(projectKey);

            List<Bug> list = projects.get(projectKey);
            int totalHour = 0;
            for(Bug bug : list) {
                row = sheet.createRow(count++);

                cell = row.createCell(1);
                cell.setCellValue(bug.getName());

                cell = row.createCell(2);
                cell.setCellValue(bug.getCreator());

                cell = row.createCell(3);
                cell.setCellValue(bug.getStatusName());

                cell = row.createCell(4);
                cell.setCellValue(bug.getCreateTime().toString());

                cell = row.createCell(5);
                cell.setCellValue(bug.getRequireDays() + "");

                totalHour = totalHour + bug.getRequireDays();
		    }

            row = sheet.createRow(count++);
            cell = row.createCell(4);
            cell.setCellValue("项目占用时间比：" + (totalHour * 100) / allTotal + "%");
            cell = row.createCell(5);
            cell.setCellValue("项目总共时间：" + totalHour + "h");
        }
    }
}
