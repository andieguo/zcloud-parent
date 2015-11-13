package org.zonesion.hadoop;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UtilTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UtilTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( UtilTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testStr(){
    	String  str = "hdfs://master.zonesion:9000/user/hadoop/zcloud/userid/channalid/file2.txt=1";
    	String str2 = str.substring(str.indexOf(":")+1,str.length());
    	System.out.println(str2);
    	System.out.println(str.indexOf("/user/hadoop/"));
    	System.out.println("/user/hadoop/".length());
    	System.out.println(str.indexOf("zcloud/"));
    	for(String string : str.substring(str.indexOf("zcloud/"), str.length()).split("/")){
    		System.out.println(string);
    	}
    }
    
   
}
