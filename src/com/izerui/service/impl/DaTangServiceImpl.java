package com.izerui.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;
import com.izerui.mapper.OAMapper;
import com.izerui.mapper.ThamsMapper;
import com.izerui.service.DaTangService;
import com.izerui.utils.ConvertDocUtil;
import com.izerui.utils.DTFenmun;
import com.izerui.utils.DTenmun;
@Service("daTangService")
public class DaTangServiceImpl implements DaTangService{

	@Autowired
	private OAMapper oaMapper;
	
	@Autowired
	private ThamsMapper thamsMapper;
	
	@Autowired
	@Value("${backFilePath}")
	private String backFilePath;
	
	@Autowired
	@Value("${ftphost}")
	private String ftphost;
	
	@Autowired
	@Value("${ftpuser}")
	private String ftpuser;
	
	@Autowired
	@Value("${ftppwd}")
	private String ftppwd;
	
	@Autowired
	@Value("${ftpport}")
	private Integer ftpport;
	
	@Autowired
	@Value("${pzm}")
	private String pzm;
	
	private FileTransferClient ftpClient = null;
	
	/**
	 * 转换数据
	 */
	public List<Map> convertData(DTenmun em){
		List<Map> list = oaMapper.listOA(em.getOATable());
		if(list==null||list.size()<=0) return list;
		for (int i=list.size()-1; i>=0;i--) {
			Map map = list.get(i);
			//如果该文件已经导入过
			if(thamsMapper.getCountOAFile(em.getTable(), em.getOAFieldName(),MapUtils.getString(map, "ID"))>0){
				list.remove(map);
				continue;
			}
			//写入W_QT表
			String kind = MapUtils.getString(map, "KIND");
			String bmid = kind!=null&&kind.contains("控股")?"KG_":"01_";
			map.put("BMID", bmid);
			
			Integer maxDID = thamsMapper.getMaxDID(em.getTable());
			maxDID = maxDID!=null?maxDID+1:1;
			map.put("DID", maxDID);
			
			switch (em.getValue()) {
			case 0:
				String bianhaozi = MapUtils.getString(map, "BIANHAOZI");
				map.put("BIANHAOZI", bianhaozi+"字["+MapUtils.getString(map, "BIANHAONIAN")+"]"+MapUtils.getString(map, "BIANHAO")+"号");
				thamsMapper.insertFaWen(map);
				break;
			case 1:
				thamsMapper.insertShouWen(map);
				break;
			case 2:
				thamsMapper.insertQingShi(map);
				break;
			case 3:
				map.put("LAIWENHAO", MapUtils.getString(map, "LAIWENWENHAO"));
				thamsMapper.insertShouWen(map);
				break;
			}
			
			
			convertEfile(map,em,DTFenmun.DT_ZHENGWEN);//转换正文
			
			convertEfile(map,em,DTFenmun.DT_EDI);//转换EDI
			
			convertEfile(map,em,DTFenmun.DT_FUJIAN);//转换附件
			
			convertEfile(map,em,DTFenmun.DT_LIUCHENGDAN);//转换流程单
		}
		closeFtpConnection();
		return list;
	}
	
	
	
	private void convertEfile(Map map, DTenmun em, DTFenmun dtf) {
		
		List<Map> attachmentsList = null;
		if(dtf.getValue()==2){//流程单
			attachmentsList = oaMapper.getWorkFlowAttachments(MapUtils.getString(map, dtf.getField()));
		}else if(dtf.getValue()==3){//获取edi文件
			attachmentsList = oaMapper.getEdiAttachment(MapUtils.getString(map, dtf.getField()));
		}else{
			attachmentsList = oaMapper.getAttachments(MapUtils.getString(map, dtf.getField()));
		}
		if(attachmentsList!=null&&attachmentsList.size()>0){
			thamsMapper.updateAttached(em.getTable(), em.getOAFieldName(), MapUtils.getString(map, "ID"));
			Integer pid = MapUtils.getInteger(map, "DID");
			for (Map map2 : attachmentsList) {
				uploadEfile(pid,map2,em,dtf.getChname());
				if(dtf.getValue()==0){//正文 只上传第一份最新的
					break;
				}
			}
		}
		
	}
	


	/**
	 * 上传正文并且写入efile表
	 * @param map
	 */
	private void uploadEfile(Integer pid,Map map,DTenmun em,String fileType){
		String filePath = backFilePath+fileType;
		try {
			if(map.containsKey("BLOB_CONTENT")){//正文或者附件
				filePath += "-"+MapUtils.getString(map, "ID")+"."+MapUtils.getString(map, "EXTENTION");
				ConvertDocUtil.converDoc(filePath,(Blob) map.get("BLOB_CONTENT"));
			}else{//流程单
				filePath += "-"+em.getOATable()+"-"+pid+".HTML";
				Clob clob = (Clob) map.get("HTML");
				String text = clob.getSubString((long)1,(int)clob.length());
				FileUtils.writeStringToFile(new File(filePath),text,"GBK");
			}
			if(ftpClient==null){
				initFtpClient(em);
			}
			uploadFile(filePath, FilenameUtils.getName(filePath));
			
			Integer maxDID = thamsMapper.getMaxDID(em.getETable());
			maxDID = maxDID!=null?maxDID+1:1;
			
			Map efileMap = new HashMap();
			efileMap.put("DID", maxDID);
			efileMap.put("PID", pid);
			efileMap.put("EFILENAME", FilenameUtils.getName(filePath));
			efileMap.put("TITLE", FilenameUtils.getBaseName(filePath)+(map.containsKey("SAVE_TIME")?(" 于:"+MapUtils.getString(map, "SAVE_TIME")):""));
			efileMap.put("EXT", FilenameUtils.getExtension(filePath));
			efileMap.put("PZM", pzm);
			efileMap.put("PATHNAME", ftpClient.getRemoteDirectory().replaceFirst("/", "").replace("/", "\\"));
			thamsMapper.insertEFileQT(em.getETable(), efileMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String filename = "d:/sdfw/引荐/djdjffwej束带结发.doc";
		System.out.println(FilenameUtils.getBaseName(filename));
		System.out.println(FilenameUtils.getName(filename));
		System.out.println(FilenameUtils.getExtension(filename));
		String filepath =  "/sdfw/引荐";
		System.out.println(filepath.replaceFirst("/", "").replace("/", "\\"));
	}
	
	/**
	 * 初始化ftp实例
	 * @param em
	 */
	private void initFtpClient(DTenmun em){
		try {
			ftpClient = new FileTransferClient();
			ftpClient.setRemoteHost(ftphost);
			ftpClient.setUserName(ftpuser);
			ftpClient.setPassword(ftppwd);
			ftpClient.setRemotePort(ftpport);
			// the transfer notify interval must be greater than buffer size
			ftpClient.getAdvancedSettings().setTransferBufferSize(1000);
			ftpClient.getAdvancedSettings().setTransferNotifyInterval(5000);
			// set enconding ,to support chinese file name
			ftpClient.getAdvancedSettings().setControlEncoding("GBK");
			ftpClient.connect();
			
			if(!isExist(ftpClient.directoryList(),em.getFtpRootPath())){//如果不存默认ftp目录
				ftpClient.createDirectory(em.getFtpRootPath());
			}
			ftpClient.changeDirectory(em.getFtpRootPath());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String _sonDirectory = sdf.format(new java.util.Date());
			if(!isExist(ftpClient.directoryList(), _sonDirectory)){
				ftpClient.createDirectory(_sonDirectory);
			}
			ftpClient.changeDirectory(_sonDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//判断目录是否存在
	private boolean isExist(FTPFile[] ftpfileList,String directoryName){
		boolean isExist = false;
		for (FTPFile ftpFile : ftpfileList) {
			if (ftpFile.getName().equals(directoryName)) {
				isExist = true;
				break;

			}
		}
		return isExist;
	}
	
	//开始上传
	private void uploadFile(String filePath,String fileName){
		try {
			ftpClient.uploadFile(filePath, fileName, WriteMode.RESUME);
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//关闭连接
	private void closeFtpConnection(){
		if(ftpClient!=null){
			try {
				ftpClient.disconnect();
			} catch (FTPException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ftpClient = null;
		}
	}

}
