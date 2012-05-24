package com.izerui.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.izerui.service.DaTangService;
import com.izerui.utils.DTenmun;

@Controller
public class DaTangController {
	
	@Autowired
	private DaTangService daTangService;
	
	
	@RequestMapping(value = "convert/{filetype}", method = RequestMethod.GET)
	public String convertFaWen(ModelMap modelMap,@PathVariable("filetype") String filetype) {
		DTenmun em = null;
		if(filetype.equals("fawen")){
			em = DTenmun.DT_FAWEN;
		}else if(filetype.equals("shouwen")){
			em = DTenmun.DT_SHOUWEN;
		}else if(filetype.equals("qingshi")){
			em = DTenmun.DT_QINGSHI;
		}else if(filetype.equals("xialaiwen")){
			em = DTenmun.DT_XIALAIWEN;
		}
		List<Map> dataList = daTangService.convertData(em);
		modelMap.addAttribute("dataList", dataList);
		return "/success/list.jsp";
	}
}
