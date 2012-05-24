package com.izerui.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ThamsMapper {
	
	@Select("select MAX(DID) from ${tablename}")
	Integer getMaxDID(@Param("tablename") String tablename);
	
	@Select("select count(DID) from ${tablename} where ${fieldname} = #{OAID} and status = 0")
	Integer getCountOAFile(@Param("tablename") String tablename,@Param("fieldname") String fieldname, @Param("OAID") String OAID);
	
	@Update("update ${tablename} set attached = 1 where ${fieldname} = #{OAID} and status = 0")
	void updateAttached(@Param("tablename") String tablename,@Param("fieldname") String fieldname,@Param("OAID") String OAID);
	
	@Insert("insert into w_qt13" +
			"(creator,createtime,status,attr,attrex,attached,bmid,pid,did,sfxygd,sfylj,title,mj,nd,f1,f2,f3,f4,f5,f6,f7)" +
			" values (" +
			"'ROOT',sysdate,0,1,0,0,#{map.BMID,jdbcType=VARCHAR},-1,#{map.DID,jdbcType=NUMERIC},1,0,#{map.TITLE,jdbcType=VARCHAR}," +
			"#{map.MIJI,jdbcType=VARCHAR},#{map.BIANHAONIAN,jdbcType=VARCHAR},#{map.SHOUWENHAO,jdbcType=VARCHAR},#{map.SHOUWENRIQI,jdbcType=VARCHAR},#{map.LAIWENDANWEI,jdbcType=VARCHAR}," +
			"#{map.CHENGWENRIQI,jdbcType=VARCHAR},#{map.JINJICHENGDU,jdbcType=VARCHAR},#{map.LAIWENHAO,jdbcType=VARCHAR},#{map.ID,jdbcType=VARCHAR}" +
			")")
	void insertShouWen(@Param("map") Map map);

	@Insert("insert into w_qt14" +
			"(creator,createtime,status,attr,attrex,attached,bmid,pid,did,sfxygd,sfylj,title,mj,ztc,f1,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f2)" +
			" values (" +
			"'ROOT',sysdate,0,1,0,0,#{map.BMID,jdbcType=VARCHAR},-1,#{map.DID,jdbcType=NUMERIC},1,0,#{map.TITLE,jdbcType=VARCHAR},#{map.MIJI,jdbcType=VARCHAR},#{map.ZHUTICI,jdbcType=VARCHAR}," +
			"#{map.JIHUAN,jdbcType=VARCHAR},#{map.NIGAOCHUSHI,jdbcType=VARCHAR},#{map.ZHUSONG,jdbcType=VARCHAR},#{map.NIGAOREN,jdbcType=VARCHAR}," +
			"#{map.DIANHUA,jdbcType=VARCHAR},#{map.CHAOSONG,jdbcType=VARCHAR},#{map.BIANHAOZI,jdbcType=VARCHAR},#{map.FENSHU,jdbcType=VARCHAR}," +
			"#{map.DAZI,jdbcType=VARCHAR},#{map.JIAODUI,jdbcType=VARCHAR},#{map.FENGFARIQI,jdbcType=VARCHAR},#{map.ID,jdbcType=VARCHAR}"+
			")")
	void insertFaWen(@Param("map") Map map);
	
	
	@Insert("insert into w_qt15" +
			"(creator,createtime,status,attr,attrex,attached,bmid,pid,did,sfxygd,sfylj,title,mj,ztc,f1,f2,f9,f10,f11,f12,f3)" +
			" values (" +
			"'ROOT',sysdate,0,1,0,0,#{map.BMID,jdbcType=VARCHAR},-1,#{map.DID,jdbcType=NUMERIC},1,0,#{map.TITLE,jdbcType=VARCHAR},#{map.MIJI,jdbcType=VARCHAR},#{map.ZHUTICI,jdbcType=VARCHAR}," +
			"#{map.BIANHAO,jdbcType=VARCHAR},#{map.HUANJI,jdbcType=VARCHAR},#{map.CHENGBAOBUSHI,jdbcType=VARCHAR},#{map.NIGAOREN,jdbcType=VARCHAR}," +
			"#{map.DIANHUA,jdbcType=VARCHAR},#{map.ZHUSONGDANWEI,jdbcType=VARCHAR},#{map.ID,jdbcType=VARCHAR}"+
			")")
	void insertQingShi(@Param("map") Map map);
	
	@Insert("insert into ${tablename}(did,pid,efilename,title,ext,pzm,pathname,status,attr,attrex,creator,createtime) values (" +
			"#{map.DID,jdbcType=NUMERIC},#{map.PID,jdbcType=NUMERIC},#{map.EFILENAME,jdbcType=VARCHAR},#{map.TITLE,jdbcType=VARCHAR}," +
			"#{map.EXT,jdbcType=VARCHAR},#{map.PZM,jdbcType=VARCHAR},#{map.PATHNAME,jdbcType=VARCHAR},0,0,0,'ROOT',sysdate" +
			")")
	void insertEFileQT(@Param("tablename") String tablename,@Param("map") Map map);
}
