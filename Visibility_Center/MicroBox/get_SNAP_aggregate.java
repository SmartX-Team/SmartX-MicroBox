package smartx.multiview.collectors.microbox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.bson.Document;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_SNAP_aggregate extends TimerTask {
	private Document document;
	private String IOVISORMongoCollection_latency = "daily-report-IOVISOR-data"; 
	private String SNAPMongoCollection = "daily-report-SNAP-data";
	
	private MongoDB_Connector mongoConnector;
	
	
	
	private int Count_GIST1=0,Count_GIST2=0,Count_GIST3=0,Count_HUST=0,Count_MYREN=0,Count_CHULA=0,Count_PH=0,Count_NCKU=0;
	private String bootstrapServer;
	
	private Thread thread;
	private String ThreadName = "Intel SNAP Daily Collection report Thread";
	private int snap_value = 0;
	private static final String regex = "^\\d{1,10}$";
	private Date timestamp;
	private Date timestamp_tomorrow,timestamp_yesterday;
	private String[] elements = { "SmartX-Box-GIST1","SmartX-Box-PH", "SmartX-Box-HUST","SmartX-Box-CHULA","SmartX-Box-MYREN","SmartX-Box-GIST3" };
	private String[] sites_snap = new String[6];
	private int i=0;
	

	
	public get_SNAP_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}

	@Override
	public void run() {
		System.out.println("************************************************SNAP Timer Called************************************************************** ");
		try {
			this.get_SNAP();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
private void get_SNAP() throws ParseException
	
	{
	System.out.println("Writing the SNAP Collection in the Table  ");
	document = new Document();
	
	SimpleDateFormat sdf_1 = new SimpleDateFormat ("yyyy/MM/dd");
	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
	timestamp = new Date();
	timestamp_tomorrow = new Date();
	timestamp_yesterday= new Date();
	
	Calendar c = Calendar.getInstance(); 
	c.setTime(timestamp); 
	c.add(Calendar.DATE, 1);
	timestamp_tomorrow = c.getTime();
	c.add(Calendar.DATE, -2);
	timestamp_yesterday = c.getTime();
	
	
	SimpleDateFormat sdf_short = new SimpleDateFormat ("yyyyMMdd");
	String date_today =sdf.format(timestamp);
	String date_tomorrow =sdf.format(timestamp_tomorrow);
	String date_yesterday =sdf.format(timestamp_yesterday);
	String date_time = "Daily-report-SNAP-data"+"_"+sdf_short.format(timestamp_yesterday);
	String csvFile = date_time+".csv";
	i=0;
	for (String s: elements) {           
	
	// Snap_influxdb obj = new Snap_influxdb();
	String[] command = new String[] { "curl", "-G", "http://103.22.221.56:8086/query?pretty=true",
			"--data-urlencode", " snap-pbox-visibility", "--data-urlencode", "chunk_size=20000",
			" ","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"="+"'"+s+"'"+"AND \"time\">="+"'"+date_yesterday+"'"+"AND \"time\"<=" +"'"+date_today+"'"+"",};
	
		
		/*String[] command = new String[] { "curl", "-G", "http://103.22.221.56:8086/query?pretty=true",
				"--data-urlencode", "db=snap-pbox-visibility", " ", "chunk_size=20000",
				"--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"="+"'"+s+"'"+"AND \"time\">="+"'"+date_yesterday+"'"+"",};
		*/
	
	System.out.print("Site: ");
	System.out.println(s);
	System.out.print("Date Today and tomorrow: ");
	System.out.print(date_today+" ");
	System.out.println(date_tomorrow);
	System.out.println("date_yesterday"+" ");
	System.out.println(date_yesterday);
	
	
	String output = executeCommand(command);
	System.out.println("Output"+"=");
	System.out.println(output);
	i+=1;
} 
	i=0;
	document.put("timestamp", date_today);
	System.out.println("Printing the SNAP result Site: ");
	for (String s: elements) {  
		System.out.print(s+":");
		System.out.println(sites_snap[i]);
		document.put(s, sites_snap[i]);
		i+=1;
	}
	System.out.println(document.toString());
	mongoConnector.getDbConnection().getCollection(SNAPMongoCollection).insertOne(document);
	document.clear();
    try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/Daily-report-SNAP-data/"+csvFile, true);

			BufferedWriter bw = new BufferedWriter(fw);

			PrintWriter out = new PrintWriter(bw,false))
	{
		File file = new File("/home/netcs/active_monitoring/Daily-report-SNAP-data/"+csvFile);
		if(file.length()!=0)
		{
			out.print("");
		}
		if (file.length() == 0)
        {
        	out.println("Date"+" "+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
        }
	
	out.println(date_yesterday/*sdf_1.format(timestamp)*/+" "+sites_snap[0]+" "+sites_snap[1]+" "+sites_snap[2]+" "+sites_snap[3]+" "+sites_snap[4]+" "+sites_snap[5]);	
	out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}
	
private String executeCommand(String... command) {

		StringBuffer output = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);

			// p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println(reader.readLine()); // value is NULL
			String line = "";
			while ((line = reader.readLine()) != null) {
				line=line.replaceAll("\\s+","");
				
					
				//System.out.println(line);
				output.append(line + "\n");
				// method 1
				boolean isInteger = isInteger(line);
				if (isInteger) {
					System.out.println(line + " is an integer method 1");
					sites_snap[i]=line;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
}

	public static boolean isInteger(String s) {
		boolean isValidInteger = false;
		try {
			Integer.parseInt(s);

			// s is a valid integer

			isValidInteger = true;
		} catch (NumberFormatException ex) {
			// s is not an integer
		}

		return isValidInteger;
	}

	

}
