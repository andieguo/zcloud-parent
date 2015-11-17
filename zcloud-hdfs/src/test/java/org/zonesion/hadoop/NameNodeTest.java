package org.zonesion.hadoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.StringUtils;

import junit.framework.TestCase;

public class NameNodeTest extends TestCase {
	public void testGetDataNode(){
		long diskBytes = 1024 * 1024 * 1024;
		Configuration conf = new Configuration();
		InetSocketAddress nameNodeAddr = new InetSocketAddress("192.168.100.141", 9000);
		ClientProtocol clientProtocl;
		try {
			clientProtocl = DFSClient.createNamenode(nameNodeAddr, conf);
			DatanodeInfo[] datanodeInfoList = clientProtocl.getDatanodeReport(DatanodeReportType.ALL);
			for(DatanodeInfo info:datanodeInfoList){
				System.out.println("name:"+info.getName());//192.168.100.134:50010
				System.out.println(info.getDatanodeReport());
				System.out.println("getCapacity"+StringUtils.limitDecimalTo2(info.getCapacity()*1.0/diskBytes));//52844687360
				System.out.println("getDfsUsed"+StringUtils.limitDecimalTo2(info.getDfsUsed()*1.0/diskBytes));//52844687360
				System.out.println("getNonDfsUsed"+StringUtils.limitDecimalTo2(info.getNonDfsUsed()*1.0/diskBytes));//52844687360
				System.out.println("getRemaining"+StringUtils.limitDecimalTo2(info.getRemaining()*1.0/diskBytes));//52844687360
				System.out.println("getDfsUsedPercent:"+StringUtils.limitDecimalTo2(info.getDfsUsedPercent()));//0.37581575
				System.out.println("getRemainingPercent"+StringUtils.limitDecimalTo2(info.getRemainingPercent()));//91.67534
				System.out.println(info.getHostName());//KVMSlave0
				System.out.println(info.getHost());//192.168.100.134
				String adminState = (info.isDecommissioned() ? "Decommissioned" :
					(info.isDecommissionInProgress() ? "Decommission In Progress":
					"In Service"));
				System.out.println(adminState);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetStatus(){
		Configuration conf = new Configuration();
		JobStatus[] jobStatusAll;
		JobClient jobClient;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		try {
			jobClient = new JobClient(new InetSocketAddress("192.168.100.141",9001), conf);
			jobStatusAll =	jobClient.getAllJobs();
			ClusterStatus clustersStatus = jobClient.getClusterStatus();
			System.out.println(clustersStatus.getMapTasks());//4
			System.out.println(clustersStatus.getReduceTasks());//4
			System.out.println(clustersStatus.getTaskTrackers());//2
			System.out.println(clustersStatus.getJobTrackerState().name());//RUNNING
			System.out.println(clustersStatus.getActiveTrackerNames());
//			System.out.println(clustersStatus.getUsedMemory());
//			System.out.println(clustersStatus.getMaxMemory());
			for(JobStatus status:jobStatusAll){
				System.out.println(""+status.getJobID());//jobidjob_201511091736_0001
				RunningJob job = jobClient.getJob(status.getJobID());
				System.out.println(""+job.getJobName());
				System.out.println(""+JobStatus.getJobRunState(status.getRunState()));//RUNNING = 1,SUCCEEDED = 2,FAILED = 3,PREP = 4,KILLED = 5
				System.out.println(""+dateFormat.format(new Date(status.getStartTime())));//1447147188397
				System.out.println(""+status.getUsername());//hadoop
				System.out.println(""+status.mapProgress());//1.0
				System.out.println(""+status.reduceProgress());//1.0
				System.out.println(""+status.setupProgress());//1.0
				System.out.println(""+status.getFailureInfo());
				System.out.println(jobClient.getDefaultMaps());
				System.out.println(jobClient.getDefaultReduces());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
