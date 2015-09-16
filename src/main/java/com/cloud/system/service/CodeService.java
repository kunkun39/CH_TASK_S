package com.cloud.system.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.platform.IDao;
import com.cloud.system.model.Code;

/**
 * 重置间隔：
 *年：每年重置（流水号从新从1开始），重置时间描述格式为01-01（每年1月1日重置）
 *月：每月重置（流水号从新从1开始），重置时间描述格式为1（每月1日重置）
 *周：每周重置（流水号从新从1开始），重置时间描述格式为1（每周周一重置）
 *无：不自动重置
 *描述符号%：
 *%2Y%：当前年，值为12
 *%4Y%：当前年，值为2012
 *%MM%：当前月，值为01
 *%dd%：当前日，值为01
 *%HH%：当前小时，值为12
 *%mm%：当前分钟，值为59
 *%ss%：当前分钟，值为59
 *%4N%：流水号，4N表示4位流水号，最大值9999，最大支持99N
 *示例：
 *PPM-%4Y%-%4N%：第一个流水号为：PPM-2012-0001
 *PPM-%4Y%%MM%-%4N%：第一个流水号为：PPM-201201-0001
 * @author chpinck@gmail.com
 *
 */
public class CodeService {

	@Autowired
	private IDao dao;

	@Transactional
	public String getCode(String scode, String... s) {
		// 根据Code.code 查询
		//TODO
		Code code = (Code) dao.getObject(Code.class, scode);
		if (!"N".equals(code.getRestmode())) {
			restSn(code);
		}
		StringBuilder codeSb = new StringBuilder(100);
		char[] rule_char = code.getCoderule().toCharArray();
		int sep_count = 0;
		List<HashMap<Integer, Integer>> list = new ArrayList<HashMap<Integer, Integer>>();
		HashMap<Integer, Integer> sepMap = null;
		HashMap<Integer, Integer> strMap = null;
		for (int i = 0; i < rule_char.length; i++) {
			char c = rule_char[i];
			if (sep_count == 0) {
				if (sepMap == null) {
					sepMap = new HashMap<Integer, Integer>();
				}
				if (strMap == null) {
					strMap = new HashMap<Integer, Integer>();
					if (strMap.get(1) == null) {
						strMap.put(1, i);
					}
				}
			}
			if (c == '%') {
				sep_count++;
			}
			if (sep_count == 1 && sepMap != null && sepMap.get(1) == null) {
				sepMap.put(1, i);
			}
			if (sep_count == 1 && strMap != null && strMap.get(2) == null) {
				strMap.put(2, i);
				list.add(strMap);
				strMap = null;
			}
			if (sep_count == 2 && sepMap != null && sepMap.get(2) == null) {
				sepMap.put(2, i);
				sep_count = 0;
				list.add(sepMap);
				sepMap = null;
			}
		}
		String subRule;
		int s_num = 0;
		for (HashMap<Integer, Integer> hashMap : list) {
			subRule = code.getCoderule().substring(hashMap.get(1), hashMap.get(2));
			if (subRule.startsWith("%")) {
				codeSb.append(getCodeByRule(code, subRule.substring(1), s_num, s));
				if ("%*".equals(subRule)) {
					s_num++;
				}
			} else {
				codeSb.append(subRule);
			}
		}
		return codeSb.toString();
	}

	public String getCodeByRule(Code code, String rule, int s_num, String... s) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String today = sdf.format(new Date());
		if ("2Y".equals(rule)) {
			result = today.substring(2, 4);
		} else if ("4Y".equals(rule)) {
			result = today.substring(0, 4);
		} else if ("MM".equals(rule)) {
			result = today.substring(5, 7);
		} else if ("dd".equals(rule)) {
			result = today.substring(8, 10);
		} else if ("HH".equals(rule)) {
			result = today.substring(11, 13);
		} else if ("mm".equals(rule)) {
			result = today.substring(14, 16);
		} else if ("ss".equals(rule)) {
			result = today.substring(17, 18);
		} else if ("*".equals(rule)) {
			if (s != null && s.length > s_num) {
				result = s[s_num];
			}
		}
		if (rule.length() >= 2 && rule.substring(1, 2).equals("N")) {
			result = int2String(code.getCurrentsn() + 1, Integer.parseInt(rule.substring(0, 1)));
			code.setCurrentsn(code.getCurrentsn() + 1);
		}
		return result;
	}

	public void restSn(Code code) {
		Date today = new Date();
		Date resetDate = code.getResttime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(resetDate);
		String str = code.getReststr();
		if ("Y".equals(code.getRestmode())) {
			calendar.add(Calendar.YEAR, 1);
			// 01-01,每年1月1日重置
			try {
				String[] strs = str.split("-");
				calendar.set(Calendar.MONTH, Integer.parseInt(strs[0]) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strs[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("M".equals(code.getRestmode())) {
			calendar.add(Calendar.MONTH, 1);
			try {
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("W".equals(code.getRestmode())) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			try {
				calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (today.after(calendar.getTime())) {
			code.setResttime(today);
			code.setCurrentsn(0);
		}
	}

	public String int2String(Integer val, int len) {
		String resultString = val.toString();
		int reallength = resultString.length();
		if (len > reallength) {
			for (int i = 0; i < len - reallength; i++) {
				resultString = "0" + resultString;
			}
		}
		return resultString;
	}
}