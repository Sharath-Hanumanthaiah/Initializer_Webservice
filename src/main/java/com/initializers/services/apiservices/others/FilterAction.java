package com.initializers.services.apiservices.others;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterAction {
	
	public List<FilterValue> getFilterValue(String[] filters) {
		List<FilterValue> filterValues = new ArrayList<>();
		for(String filter : filters) {
			List<String> value = new ArrayList<String>();
			FilterValue filterValue = new FilterValue();
			String[] filterArr = filter.split(" ");
			System.out.println(filterArr.length);
			if(filterArr.length == 3) {
				filterValue.setName(filterArr[0]);
				filterValue.setOperator(filterArr[1]);
				//array value are split by ~
				String[] values = filterArr[2].substring(1, filterArr[2].length() - 1).split("~");
				for(String val : values) {
					//space in string is split by _
					value.add(val.replace("_", " "));
				}
				filterValue.setValue(value);
			}
			filterValues.add(filterValue);
		}
		return filterValues;
	}
}
