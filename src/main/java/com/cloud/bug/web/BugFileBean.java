package com.cloud.bug.web;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.bug.model.Bug;
import com.cloud.bug.service.BugFileService;
import com.cloud.bug.service.BugService;
import com.cloud.bug.vo.BugSearchVo;
import com.cloud.platform.ExcelUtil;

@Controller
@RequestMapping("bugfile")
public class BugFileBean {

	private static Logger logger = Logger.getLogger(BugFileBean.class);
	
	@Autowired
	private BugFileService bugFileService;

	@Autowired
	private BugService bugService;
	
	/**
	 * get import data status
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadStatus.do")
	public String loadStatus() {
		
		return ExcelUtil.import_status.toString();
	}
	
	/**
	 * import bugs
	 * 
	 * @param request
	 */
	@ResponseBody
	@RequestMapping("/importBugs.do")
	public String importBugs(HttpServletRequest request) {
		
		try {
			// reset import status
			ExcelUtil.import_status.setLength(0);
			
			// init upload import data file
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			List items = upload.parseRequest(request);
			
			for(Object o : items) {
				// get upload file
				FileItem file = (FileItem) o;
				
				if(file.getName() == null) {
					continue;
				}
				
				// read import data file
				Workbook wb = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
				Sheet sheet = wb.getSheetAt(0);
				
				// get row count and cell count
				int rowNum = sheet.getLastRowNum();
		        int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
		        
		        // check data valid
		        boolean valid = bugFileService.checkImportData(sheet, rowNum, colNum);
		        
		        if(valid) {
		        	bugFileService.importBugs(sheet, rowNum, colNum);
		        }
			}
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：importBugs", e);
		}
		
		return "";
	}
	
	/**
	 * export bug list excel
	 * 
	 * @param response
	 */
	@RequestMapping("/exportExcel.do")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		
		try {
            //get para
            String projectId = ServletRequestUtils.getStringParameter(request, "reportProject", "");
            int year = ServletRequestUtils.getIntParameter(request, "reportYear", 1);
            int month = ServletRequestUtils.getIntParameter(request, "reportMonth", 1);
            int reportDays = ServletRequestUtils.getIntParameter(request, "reportDays", 1);

            // search bugs
            BugSearchVo searchVo = new BugSearchVo();
            searchVo.setSort("creatorId,asc");
            searchVo.setPageSize(-1);
            searchVo.setYear(year);
            searchVo.setMonth(month);
            List<Bug> bugs = bugService.searchBugs(searchVo);

            // export bugs
            String path = bugFileService.exportExcel(bugs, reportDays);

            // write export file to front page
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=Task_Report.xls");

            OutputStream out = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));

            byte[] buffer = new byte[1024 * 5];
            int len;

            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            out.flush();
            out.close();

        } catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：exportExcel", e);
		}
	}
	
	/**
	 * export excel template file for import
	 * 
	 * @param response
	 */
	@RequestMapping("/exportTemplate.do")
	public void exportTemplate(HttpServletResponse response) {

		try {
			// export template
			String path = bugFileService.exportTemplate();
			
			// write export file to front page
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=template");

			OutputStream out = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(path));

			byte[] buffer = new byte[1024 * 5];
			int len;

			while ((len = bis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.flush();
			out.close();
			
		} catch(Exception e) {
			logger.error("***** 异常信息 ***** 方法：exportTemplate", e);
		}
	}
}
