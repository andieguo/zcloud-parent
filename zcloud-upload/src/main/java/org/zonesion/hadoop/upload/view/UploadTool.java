package org.zonesion.hadoop.upload.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.zonesion.hadoop.base.util.LogListener;

public class UploadTool {
	
	private  int current = 0;
	
	private LogListener logListener;
	
	/**
	 * 拷贝文件夹
	 * 
	 * @param srcDir
	 * @param dstDir
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public  boolean copyDirectory(String srcDir, String dstDir,
			Configuration conf) throws Exception {

		FileSystem fs = FileSystem.get(conf);
		if (!fs.exists(new Path(dstDir))) {//目的路径是否存在，不存在则创建
			fs.mkdirs(new Path(dstDir));
		}
		FileStatus status = fs.getFileStatus(new Path(dstDir));
		File file = new File(srcDir);
		if (!status.isDir()) {
			System.exit(2);
			System.out.println("You put in the " + dstDir + "is file !");
		} 
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				// 准备复制的源文件夹
				String srcDir1 = srcDir + File.separator + f.getName();
				String dstDir1 = dstDir + File.separator + f.getName();
				System.out.println("src-dir:"+srcDir1);
				System.out.println("dst-dir:"+dstDir1);
				copyDirectory(srcDir1, dstDir1, conf);
			} else {
				String dstfile = new File(dstDir).getAbsolutePath() +File.separator+f.getName() ;
				System.out.println("src-file:"+f.getPath());
				System.out.println("dst-file:"+dstfile);
				copyFile(f.getPath(),dstfile, conf);
				if(logListener != null) logListener.log("成功上传:"+f.getPath());
			}
		}
		return true;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 * @param dst
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public  boolean copyFile(String src, String dst, Configuration conf)
			throws Exception {
		current += 1;
		FileSystem fs = FileSystem.get(conf);
		File file = new File(src);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		/**
		 * FieSystem的create方法可以为文件不存在的父目录进行创建，
		 */
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress() {
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
		if(logListener != null) logListener.progress(current, UploadListener.Length);
		return true;
	}
	
	public void registerLogListener(LogListener logListener) {
		this.logListener = logListener;
	}
	
	public static void main(String[] args) throws Exception {
		UploadTool uploadTool = new UploadTool();
		if (args.length < 2) {
			System.out.println("Please input two number");
			System.exit(2);
		}
		String localSrc = args[0];
		String dst = args[1];
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://master.zonesion:9000");
		File srcFile = new File(localSrc);
		if (srcFile.isDirectory()) {
			uploadTool.copyDirectory(localSrc, dst, conf);
		} else {
			uploadTool.copyFile(localSrc, dst, conf);
		}
	}
}
