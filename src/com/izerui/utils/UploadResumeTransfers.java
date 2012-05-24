package com.izerui.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.enterprisedt.net.ftp.EventAdapter;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;
import com.enterprisedt.util.debug.Level;
import com.enterprisedt.util.debug.Logger;

public class UploadResumeTransfers {

	// set up logger so that we get some output
	private static Logger log = Logger.getLogger(UploadResumeTransfers.class);

	public static void main(String[] args) {

		// we want remote host, user name and password
		if (args.length < 3) {
			System.out.println("Usage: run remote-host username password");
			System.exit(1);
		}

		// extract command-line arguments
		String host = args[0];
		String username = args[1];
		String password = args[2];
		String path = "I:/电影/";
		// name 可以为文件夹或者具体文件
		String name = "测试文件夹";

		Logger.setLevel(Level.INFO);

		FileTransferClient ftp = null;

		try {
			// create client
			log.info("Creating FTP client");
			ftp = new FileTransferClient();

			// set remote host
			ftp.setRemoteHost(host);
			ftp.setUserName(username);
			ftp.setPassword(password);

			// set up listener
			UploadListener ul = new UploadListener(ftp);
			ftp.setEventListener(ul);

			// the transfer notify interval must be greater than buffer size
			ftp.getAdvancedSettings().setTransferBufferSize(1000);
			ftp.getAdvancedSettings().setTransferNotifyInterval(5000);
			// set enconding ,to support chinese file name
			ftp.getAdvancedSettings().setControlEncoding("GBK");

			// connect to the server
			log.info("Connecting to server " + host);
			ftp.connect();
			log.info("Connected and logged in to server " + host);

			File file = new File(path + name);
			uplaodFile(ftp, file, ul);

			// Shut down client
			log.info("Quitting client");
			ftp.disconnect();

			log.info("Example complete");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * 上传文件夹
	 **************************************************************************/

	public static void uplaodFile(FileTransferClient ftp, File file,
			UploadListener ul) {
		if (file.isFile()) {
			// 执行上传代码
			runUpload(ftp, file, ul);
		}
		try {
			if (file.isDirectory()) {

				FTPFile[] ftpFiles = ftp.directoryList();
				boolean isExist = false;
				for (FTPFile ftpFile : ftpFiles) {
					if (ftpFile.getName().equals(file.getName())) {
						isExist = true;
						break;

					}
				}
				if (!isExist)
					ftp.createDirectory(file.getName());
				ftp.changeDirectory(file.getName());

				File[] childFile = file.listFiles();
				for (int i = 0; i < childFile.length; i++) {
					if (childFile[i].isFile()) {
						// 执行上传代码
						runUpload(ftp, childFile[i], ul);
					}
					if (childFile[i].isDirectory()) {
						uplaodFile(ftp, childFile[i], ul);
					}
				}
				ftp.changeToParentDirectory();
			}
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/***************************************************************************
	 * 执行文件上传
	 **************************************************************************/
	public static void runUpload(FileTransferClient ftp, File file,
			UploadListener ul) {
		String fileName = file.getName();
		String filePath = file.getPath();
		try {
			ftp.uploadFile(filePath, fileName, WriteMode.RESUME);

			int len = (int) ftp.getSize(fileName);

			// only the remaining bytes are transferred as can be seen
			log.info(file.getName() + " Bytes transferred="
					+ ul.getBytesTransferred());
			log.info(file.getName() + " uploaded (localsize=" + file.length()
					+ " remotesize=" + len);
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

/**
 * 上传过程监听类
 */
class UploadListener extends EventAdapter {

	private Logger log = Logger.getLogger(UploadListener.class);

	/**
	 * Keep the last reported byte count
	 */
	private long bytesTransferred = 0;

	/**
	 * FTPClient reference
	 */
	private FileTransferClient ftp;

	/**
	 * Constructor
	 * 
	 * @param ftp
	 */
	public UploadListener(FileTransferClient ftp) {
		this.ftp = ftp;
	}

	public void bytesTransferred(String connId, String remoteFilename,
			long bytes) {
		log.info("transfered size:" + bytes);
		bytesTransferred = bytes;
	}

	/**
	 * Will contain the total bytes transferred once the transfer is complete
	 */
	public long getBytesTransferred() {
		return bytesTransferred;
	}

}