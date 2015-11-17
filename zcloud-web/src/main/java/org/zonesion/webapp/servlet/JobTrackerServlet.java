package org.zonesion.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.json.JSONObject;

public class JobTrackerServlet extends HttpServlet {

	private static final long serialVersionUID = -5692181738963360557L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		JobStatus[] jobStatusAll;
		JobClient jobClient;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		List<JobSummaryInfo> runningJobs = new ArrayList<JobSummaryInfo>();
		List<JobSummaryInfo> completedJobs = new ArrayList<JobSummaryInfo>();
		List<JobSummaryInfo> failedJobs = new ArrayList<JobSummaryInfo>();
		List<JobSummaryInfo> killedJobs = new ArrayList<JobSummaryInfo>();
		try {
			jobClient = new JobClient(new InetSocketAddress("192.168.100.141",9001), conf);
			jobStatusAll =	jobClient.getAllJobs();
			for(JobStatus status:jobStatusAll){
				JobID jobid = status.getJobID();//jobidjob_201511091736_0001
				RunningJob job = jobClient.getJob(jobid);
				String jobname = job.getJobName();
				String jobstatus = JobStatus.getJobRunState(status.getRunState());//RUNNING = 1,SUCCEEDED = 2,FAILED = 3,PREP = 4,KILLED = 5
				String starttime = dateFormat.format(new Date(status.getStartTime()));//1447147188397
				String username = status.getUsername();//hadoop
				float mapprogress = status.mapProgress();
				float reduceprogress = status.reduceProgress();
				String failureinfo = status.getFailureInfo();
				JobSummaryInfo jobInfo = new JobSummaryInfo(jobid.toString(),jobname,username,jobstatus,failureinfo,starttime,reduceprogress,mapprogress);
				switch (status.getRunState()) {
					case 1://RUNNING
						runningJobs.add(jobInfo);
						break;
					case 2://SUCCEEDED
						completedJobs.add(jobInfo);
						break;
					case 3://FAILED
						failedJobs.add(jobInfo);
						break;
					case 4://PREP
						runningJobs.add(jobInfo);
						break;
					case 5://KILLED
						killedJobs.add(jobInfo);
						break;
					default:
						break;
				}
			}
			resp.setContentType("application/x-json");// 需要设置ContentType 为"application/x-json"
			JSONObject resultJSON = new JSONObject();// 构建一个JSONObject
			resultJSON.put("running", runningJobs);
			resultJSON.put("completed", completedJobs);
			resultJSON.put("failed", failedJobs);
			resultJSON.put("killed", killedJobs);
			PrintWriter out = resp.getWriter();
			out.println(resultJSON.toString());// 向客户端输出JSONObject字符串
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class JobSummaryInfo {
		private String jobId;
		private String jobName;
		private String userName;
		private String jobPriority;
		private String jobRunState;
		private String jobFailureInfo;
		private String starttime;
		private String endtime;
		private float reduceProgress;
		private float mapProgress;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getJobPriority() {
			return jobPriority;
		}

		public void setJobPriority(String jobPriority) {
			this.jobPriority = jobPriority;
		}

		public String getJobRunState() {
			return jobRunState;
		}

		public void setJobRunState(String jobRunState) {
			this.jobRunState = jobRunState;
		}

		public String getJobFailureInfo() {
			return jobFailureInfo;
		}

		public void setJobFailureInfo(String jobFailureInfo) {
			this.jobFailureInfo = jobFailureInfo;
		}

		public float getReduceProgress() {
			return reduceProgress;
		}

		public void setReduceProgress(float reduceProgress) {
			this.reduceProgress = reduceProgress;
		}

		public float getMapProgress() {
			return mapProgress;
		}

		public void setMapProgress(float mapProgress) {
			this.mapProgress = mapProgress;
		}

		public String getStarttime() {
			return starttime;
		}

		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}

		public String getEndtime() {
			return endtime;
		}

		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}

		public JobSummaryInfo(String jobId, String jobName, String userName, String jobRunState, String jobFailureInfo,
				String starttime,float reduceProgress, float mapProgress) {
			super();
			this.jobId = jobId;
			this.jobName = jobName;
			this.userName = userName;
			this.jobRunState = jobRunState;
			this.jobFailureInfo = jobFailureInfo;
			this.starttime = starttime;
			this.reduceProgress = reduceProgress;
			this.mapProgress = mapProgress;
		}
		
	}

}
