package com.izerui.service;

import java.util.List;
import java.util.Map;

import com.izerui.utils.DTenmun;

public interface DaTangService {

	/**
	 * 转换oa条目
	 * @param em 0:发文 1:收文 2:请示报告
	 * @return 转换的条目列表
	 */
	public List<Map> convertData(DTenmun em);
}
