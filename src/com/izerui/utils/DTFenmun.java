package com.izerui.utils;
public enum DTFenmun{
	/**
	 * 正文(0)
	 */
	DT_ZHENGWEN(0),
	/**
	 * 附件(1)
	 */
	DT_FUJIAN(1),
	/**
	 * 流程单(2)
	 */
	DT_LIUCHENGDAN(2),
	/**
	 * EDI(3)
	 */
	DT_EDI(3);
	
	DTFenmun(Integer value) {
		this.value = value;
	}

	private Integer value;

	public Integer getValue() {
		return value;
	}
	
	public String getChname(){
		switch (value) {
		case 0:
			return "正文";
		case 1:
			return "附件";
		case 2:
			return "流程单";
		case 3:
			return "EDI文件";
		}
		return null;
	}
	
	public String getField(){
		switch (value) {
		case 0:
			return "XMLID";
		case 1:
			return "ID";
		case 2:
			return "XMLID";
		case 3:
			return "XMLID";
		}
		return null;
	}

}