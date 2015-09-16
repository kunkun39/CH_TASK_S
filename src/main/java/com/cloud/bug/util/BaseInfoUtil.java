package com.cloud.bug.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseInfoUtil {
	
	public static String ITEM_LEVEL_1 = "致命";
	public static String ITEM_LEVEL_2 = "严重";
	public static String ITEM_LEVEL_3 = "一般";
	public static String ITEM_LEVEL_4 = "优化";
	
	public static String ITEM_PRIORITY_1 = "高";
	public static String ITEM_PRIORITY_2 = "中";
	public static String ITEM_PRIORITY_3 = "低";

	/**
	 * get field base items
	 * 
	 * @param field
	 * @return
	 */
	public static List<String[]> getBaseItems(String field) {
		
		List<String[]> items = new ArrayList();
		
		// level field
		if("level".equals(field)) {
			items.add(new String[] {"1", ITEM_LEVEL_1});
			items.add(new String[] {"2", ITEM_LEVEL_2});
			items.add(new String[] {"3", ITEM_LEVEL_3});
			items.add(new String[] {"4", ITEM_LEVEL_4});
		}
		// priority field
		else if("priority".equals(field)) {
			items.add(new String[] {"1", ITEM_PRIORITY_1});
			items.add(new String[] {"2", ITEM_PRIORITY_2});
			items.add(new String[] {"3", ITEM_PRIORITY_3});
		}
		
		return items;
	}
	
	/**
	 * get base item name by item field and item id
	 * 
	 * @param field
	 * @param item
	 * @return
	 */
	public static String getItemName(String field, String item) {
		
		List<String[]> items = getBaseItems(field);
		
		// convert list to map
		Map<String, String> itemMap = new HashMap();
		
		for(String[] i : items) {
			itemMap.put(i[0], i[1]);
		}
		
		// get item name
		if(itemMap.containsKey(item)) {
			return itemMap.get(item);
		} else {
			return "";
		}
	}
}
