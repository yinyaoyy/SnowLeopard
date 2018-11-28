package com.thinkgem.jeesite.modules.cms.web.upload;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
			
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		if (targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}
		try {
			FileUtils.moveFile(tmpFile, targetFile);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}

	/**
     * 上传FTP文件
     * @param is
     * @param path
     * @param maxSize
     * @return
     */
    @SuppressWarnings("static-access")
	public static State saveFtpFileByInputStream(InputStream is, String remoteDir, String path, long maxSize,boolean keepLocalFile, String filename) {
      State state = null;

      File tmpFile = getTmpFile();

      byte[] dataBuf = new byte[2048];
      BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);
      try {
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);
          int count = 0;
          while ((count = bis.read(dataBuf)) != -1) {
              bos.write(dataBuf, 0, count);
          }
          bos.flush();
          bos.close();

          if (tmpFile.length() > maxSize) {
              tmpFile.delete();
              return new BaseState(false, AppInfo.MAX_SIZE);
          }
          try {
              FileInputStream in=new FileInputStream(tmpFile); 
              FtpUtil t = new FtpUtil();
              System.out.println("*******************");
          	  t.uploadFile("192.168.184.1",21,"admin","wife",
          			"/photo", remoteDir,filename,in);
              //t.uploadFile("文件服务器IP", 端口, "用户名", "密码", "", remoteDir, filename, in);
          }catch (Exception e) {
              tmpFile.delete();
              return new BaseState(false, AppInfo.IO_ERROR);
          }
          state = new BaseState(true);
          state.putInfo("size", tmpFile.length());
          
          if (keepLocalFile){
              File targetFile = new File(path);

              if (targetFile.canWrite()){
                  return new BaseState(false, AppInfo.PERMISSION_DENIED);
              }
              try    {
                  FileUtils.moveFile(tmpFile, targetFile);
              } catch (IOException e) {
                  tmpFile.delete();
                  return new BaseState(false, AppInfo.IO_ERROR);
              }
          }
        tmpFile.delete();
        return state;
      } catch (IOException localIOException) {
          tmpFile.delete();
      }
      return new BaseState(false, AppInfo.IO_ERROR);
    }
}
