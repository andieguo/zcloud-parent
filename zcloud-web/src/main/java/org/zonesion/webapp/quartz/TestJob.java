package org.zonesion.webapp.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {


    public void execute(JobExecutionContext context) throws JobExecutionException {
        // TODO Auto-generated method stub
    	printTest();
    }
    
    public void printTest(){
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String returnstr = DateFormat.format(d);  
        System.out.println(returnstr+"★★★★★★★★★★★");
    }
}