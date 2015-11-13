package org.zonesion.hadoop.hdfs.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {
	private FileSystem fs;
	
	public HDFSUtil(String hdfsName){
        Configuration conf = new Configuration();
        conf.set("fs.default.name", hdfsName);
        try {
			 fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 // 创建目录
    public  boolean mkdir(String path) throws IOException {
            Path srcPath = new Path(path);
            return fs.mkdirs(srcPath);
    }
    
    // 上传本地文件
    public  void put(String src, String dst) throws IOException {
            Path srcPath = new Path(src); // 原路径
            Path dstPath = new Path(dst); // 目标路径
            // 调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认>为false
            fs.copyFromLocalFile(true, srcPath, dstPath);
    }
    
    //校验本地文件是否存在
	public boolean check(String filePath) throws IOException {
		Path path = new Path(filePath);
		boolean isExists = fs.exists(path);
		return isExists;
	}
	//错误的递归
	public void list(String filePath) throws IOException{
		Path path = new Path(filePath);
		 FileStatus[] fileStatus = fs.listStatus(path);
         for (FileStatus file : fileStatus) {
        	 	int depath = file.getPath().depth();
        	 	System.out.println(file.getPath());
        	 	if(depath != 5){
        	 		list(file.getPath().toString());
        	 	}else{
        	 		break;
        	 	}
         }
	}
	//循环调用2次，正确的使用
	public List<String> listFile(String filePath) throws IOException{
		List<String> listDir = new ArrayList<String>();
		Path path = new Path(filePath);
		 FileStatus[] fileStatus = fs.listStatus(path);
		if(fileStatus.length!=0){
			 for (FileStatus file : fileStatus) {
				 listDir.add(file.getPath().toString());
			 }
			 return listDir;
		}
		return listDir;
	}
	
	//释放资源
	public void destroy(){
		if(fs!=null)
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
    public FileSystem getFs() {
		return fs;
	}

	public void setFs(FileSystem fs) {
		this.fs = fs;
	}

	public static void main(String[] args) {
    	// 创建文件夹
        try {
        	HDFSUtil hdfsAPI = new HDFSUtil("hdfs://192.168.1.30:9002"); 
        	FileSystem fs = hdfsAPI.getFs(); 
        	if(fs ==null) System.out.println("空");
        	
        	//hdfsAPI.mkdir("/user/hadoop/test");
        	//hdfsAPI.put("config.properties","/user/hadoop/test");
        	//hdfsAPI.check("/user/hadoop/test/config.properties");
//        	hdfsAPI.list("hdfs://192.168.1.30:9000/user/hadoop/zcloud/");
//        	for(String str : hdfsAPI.listFile("/user/hadoop/zcloud")){
//        		for(String str1 : hdfsAPI.listFile(str)){//连续调用2次
//        			System.out.println(str1);
//        		}
//        	}
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
	}
}
