package org.zonesion.hadoop.hdfs.util;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HadoopUtil {

	//添加第三方Jar文件到CLASSPATH
	@SuppressWarnings("deprecation")
	public static void addJarToDistributedCache(String path, Configuration conf) throws IOException {

		File jarFile = new File(path);

		// Mount HDFS
		FileSystem fs = FileSystem.get(conf);

		// Copy (override) jar file to HDFS
		Path srcPath = new Path(path); // 原路径
		Path mkdir = new Path("/user/hadoop/lib/");
		if(!fs.exists(mkdir)){
			fs.mkdirs(mkdir);
		}
		Path dstPath = new Path("/user/hadoop/lib/" + jarFile.getName());// 目标路径
		// 调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
		if(!fs.exists(dstPath)){
			fs.copyFromLocalFile(false, srcPath, dstPath);
		}

		// Add jar to distributed classPath
		DistributedCache.addArchiveToClassPath(dstPath, conf);
	}
	
	//添加第三方class文件到CLASSPATH
	public static void addJarToDistributedCache(Class classToAdd, Configuration conf)throws IOException {

	        // Retrieve jar file for class2Add
	        String path = classToAdd.getProtectionDomain().getCodeSource().getLocation().getPath();
	        System.out.println(path);
	        File jarFile = new File(path);

	        // Mount HDFS
	        FileSystem fs = FileSystem.get(conf);
	        
	        // Declare new HDFS location
	        Path srcPath = new Path(path); // 原路径
			Path mkdir = new Path("/user/hadoop/lib/");
			if(!fs.exists(mkdir)){
				fs.mkdirs(mkdir);
			}

			Path dstPath = new Path("/user/hadoop/lib/" + jarFile.getName());// 目标路径
			// 调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
			if(!fs.exists(dstPath)){
				fs.copyFromLocalFile(false, srcPath, dstPath);
			}

	        // Add jar to distributed classPath
	        DistributedCache.addFileToClassPath(dstPath, conf);
	    }
	
	public static void main(String[] args) throws IOException {
		//Configuration conf = HadoopConfiguration.getConfiguration();
		//HadoopUtil.addJarToDistributedCache("E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp5/wtpwebapps/education-web/WEB-INF/lib/json-20140107.jar", conf);
		//HadoopUtil.addJarToDistributedCache(WordCount.class, conf);
//		String path = HadoopUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println(path);
//        String path = com.education.experiment.commons.HadoopUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");
//        System.out.println(path);
	}
	        
}
