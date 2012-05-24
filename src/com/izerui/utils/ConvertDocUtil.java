package com.izerui.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;


public class ConvertDocUtil {

	public static void converDoc(String filePath, Object obj) throws IOException, SQLException {
		InputStream is = null;
		try {
			if(obj instanceof Clob){
				is = ((Clob)obj).getAsciiStream();
			}else{
				is = ((Blob)obj).getBinaryStream(); // 定义流
			}
			final FileOutputStream fos = new FileOutputStream(filePath);// 定义一个file输出流
			final BufferedOutputStream bos = new BufferedOutputStream(fos);

			int bytesRead = 0;
			int byteSum = 0;
			byte[] buffer = new byte[50 * 1024 * 1024];
			while ((bytesRead = is.read(buffer)) != -1) {
				byteSum += bytesRead;
				bos.write(buffer, 0, bytesRead);// 写入文件
				bos.flush();
				bos.close();
			}
		} catch (IOException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			 try {
			 is.close();
			 } catch (IOException e) {
			 e.printStackTrace();
			 }
		}
	}
}