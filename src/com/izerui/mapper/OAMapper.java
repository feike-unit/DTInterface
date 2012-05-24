package com.izerui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OAMapper {

	/**
	 * 获取办理完毕的公文
	 */
	@Select("Select dt.* ,XMLWORK.id as xmlid From" +
			" ${tablename} dt,T_HZDRP_SYS_XMLWORKAPP XMLWORKAPP,T_HZDRP_SYS_XMLWORK XMLWORK " +
			"where dt.ID = XMLWORKAPP.DATAID AND XMLWORKAPP.WORKID = XMLWORK.ID and XMLWORK.flowstatus > 0 and XMLWORK.nodeid like 'End%'")
	List<Map> listOA(@Param("tablename") String tablename);

	/**
	 * 获取正文/附件
	 * @param id 业务表的ID
	 */
	@Select("select ment.EXTENTION , ment.SAVE_TIME , bl.* from T_HZDRP_SYS_STORAGE_BLOB bl,T_HZDRP_SYS_STORAGE_ATTACHMENT ment where  ment.id=bl.object_id and ment.File_size !=0  and ment.object_id = #{id} and ment.EXTENTION != 'edi'  order by ment.save_time desc")
	List<Map> getAttachments(@Param("id") String id);
	
	/**
	 * 获取EDI
	 * @param id 业务表的ID
	 */
	@Select("select ment.EXTENTION , ment.SAVE_TIME , bl.* from T_HZDRP_SYS_STORAGE_BLOB bl,T_HZDRP_SYS_STORAGE_ATTACHMENT ment where  ment.id=bl.object_id and ment.File_size !=0  and ment.object_id = #{id} and ment.EXTENTION = 'edi'  order by ment.save_time desc")
	List<Map> getEdiAttachment(@Param("id") String id);

	
	/**
	 * 获取流程单
	 * @param xmlid
	 * @return
	 */
	@Select("select html from t_hzoa_app_flowhtml where workid=#{xmlid}")
	List<Map> getWorkFlowAttachments(@Param("xmlid") String xmlid);
}
