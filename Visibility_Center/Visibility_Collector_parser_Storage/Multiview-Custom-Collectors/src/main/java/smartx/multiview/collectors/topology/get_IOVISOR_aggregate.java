package smartx.multiview.collectors.topology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.bson.Document;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_IOVISOR_aggregate extends TimerTask {
	private Document document;
	private String IOVISORMongoCollection = "daily-report-IOVISOR-data"; 
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private Thread thread;
	private String ThreadName = "IOVISOR Daily Collection report Thread";
	private Date timestamp;
	private int Count_GIST1=0,Count_GIST2=0,Count_GIST3=0,Count_HUST=0,Count_MYREN=0,Count_CHULA=0,Count_PH=0,Count_NCKU=0;
	private Date timestamp_yesterday;
	

	
	public get_IOVISOR_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}

	@Override
	public void run() {
		System.out.println("************************************************IOVISOR Timer Called************************************************************** ");
		try {
			this.get_IOVISOR();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void get_IOVISOR() throws ParseException
	
	{
		
		System.out.println("Writing the IOVisor Collection in the Table  ");
		document = new Document();
		
		
		File folder = new File("/home/netcs/IOVisor-Data/Control-Plane/");
		File[] listOfFiles = folder.listFiles();
		List<String> filteredList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat sdf_short = new SimpleDateFormat ("yyyyMMdd");
		SimpleDateFormat sdf_1 = new SimpleDateFormat ("yyyy/MM/dd");
		timestamp = new Date();
		String date_today =sdf.format(timestamp);
		//String date_today_short =sdf_short.format(timestamp);
		//date_today =date_today.replace("-", "");
		System.out.println("Today's date:"+date_today);
		
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(timestamp); 
		c.add(Calendar.DATE, -1);
		timestamp_yesterday = c.getTime();
		date_today =sdf.format(timestamp_yesterday);
		
		String date_time = "Daily-report-IOVISOR-mc"+"_"+sdf_short.format(timestamp_yesterday);
		
		
		
		
		
		
		
		
		
		
		String csvFile = date_time.replace("/", "")+".csv";

		for (File file : listOfFiles) {
		    // Be aware that folder.listFiles() give list with directories and files
		    if (file.isFile()) {
		        //System.out.println(file.getName());

		        // apply your filter. for simplicity I am equating the file names
		        // name refers to input variable to method
		        if(file.getName().contains("SmartX-Box-GIST1-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_GIST1 =Count_GIST1+ 1;
		        }                                
		        else if(file.getName().contains("SmartX-Box-GIST2-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_GIST2 =Count_GIST2+ 1;
		        }
		        else if(file.getName().contains("SmartX-Box-GIST3-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_GIST3 =Count_GIST3+ 1;
		        }
		        else if(file.getName().contains("SmartX-Box-HUST-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_HUST =Count_HUST+ 1;
		        }
		        else if(file.getName().contains("SmartX-Box-CHULA-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_CHULA =Count_CHULA+ 1;
		        }
		        
		        								 
		        else if(file.getName().contains("SmartX-Box-MYREN-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_MYREN =Count_MYREN+ 1;
		        }
		        else if(file.getName().contains("SmartX-Box-PH-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_PH =Count_PH+ 1;
		        }
		        else if(file.getName().contains("SmartX-Box-NCKU-mc-"+date_today)) { 
		            // create user object and add it to list. 
		            // Change below line with appropriate constructor params
		            filteredList.add(file.getName());
		            Count_NCKU =Count_NCKU+ 1;
		        }
		        
		    }
		}
		System.out.println("Printing files found for SmartX-Box-GIST1-mc in today");
		for (String temp : filteredList) {
			//System.out.println(temp);
		}
		System.out.println("Total number of files found for SmartX-Box-GIST1-mc in today:");
		System.out.println(Count_GIST1);
		System.out.println("Total number of files found for SmartX-Box-GIST2-mc in today:");
		System.out.println(Count_GIST2);
		System.out.println("Total number of files found for SmartX-Box-GIST3-mc in today:");
		System.out.println(Count_GIST3);
		System.out.println("Total number of files found for SmartX-Box-HUST-mc in today:");
		System.out.println(Count_HUST);
		System.out.println("Total number of files found for SmartX-Box-CHULA-mc in today:");
		System.out.println(Count_CHULA);
		System.out.println("Total number of files found for SmartX-Box-MYREN-mc in today:");
		System.out.println(Count_MYREN);
		System.out.println("Total number of files found for SmartX-Box-PH-mc in today:");
		System.out.println(Count_PH);
		System.out.println("Total number of files found for SmartX-Box-NCKU-mc in today:");
		System.out.println(Count_NCKU);
		
		double d = ((float)Count_GIST1/285)*100;
		DecimalFormat f = new DecimalFormat("##.00");
		DecimalFormat e = new DecimalFormat("##");
		DecimalFormat df = new DecimalFormat("#.00");
		
		document.put("timestamp", date_today);
		document.put("Total Files expected", "285");
		if (((double)((float)Count_GIST1/285)*100) > 100)
			document.put("SmartX-Box-GIST1", "100");
		else
		document.put("SmartX-Box-GIST1", f.format((double)((float)Count_GIST1/285)*100));
		if (((double)((float)Count_PH/285)*100) > 100)
			document.put("SmartX-Box-PH","100");
		else
		document.put("SmartX-Box-PH",f.format((double)((float)Count_PH/285)*100));
		
		if (((double)((float)Count_HUST/285)*100) > 100)
			document.put("SmartX-Box-HUST","100");
		else
		document.put("SmartX-Box-HUST",f.format((double)((float)Count_HUST/285)*100));
		
		if (((double)((float)Count_CHULA/285)*100) > 100)
			document.put("SmartX-Box-CHULA","100");
		else
		document.put("SmartX-Box-CHULA",f.format((double)((float)Count_CHULA/285)*100));
		
		if (((double)((float)Count_MYREN/285)*100) > 100)
			document.put("SmartX-Box-MYREN","100");
		else
		document.put("SmartX-Box-MYREN",f.format((double)((float)Count_MYREN/285)*100));
		
		if (((double)((float)Count_GIST3/285)*100) > 100)
			document.put("SmartX-Box-GIST3", "100");
		else
		document.put("SmartX-Box-GIST3", f.format((double)((float)Count_GIST3/285)*100));
		
		//document.put("SmartX-Box-NCKU",Count_NCKU);
		System.out.println(document.toString());
		mongoConnector.getDbConnection().getCollection(IOVISORMongoCollection).insertOne(document);
		
		try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/Daily-report-IOVISOR-data/"+csvFile, true);

				BufferedWriter bw = new BufferedWriter(fw);

				PrintWriter out = new PrintWriter(bw))
		{
			File file = new File("/home/netcs/active_monitoring/Daily-report-IOVISOR-data/"+csvFile);
			if (file.length() == 0)
	        {
	        	out.println("Date"+" "+"Total_Expected_Files"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
	        }
			
			
			System.out.println(f.format(d));
			System.out.println(df.format(d));
			System.out.println(f.format((double)((float)Count_GIST1/285)*100));
		//out.println(sdf_1.format(timestamp)+" "+"285"+" "+f.format((double)((float)Count_GIST1/285)*100)+"%"+" "+f.format((double)((float)Count_PH/285)*100)+"%"+" "+f.format((double)((float)Count_HUST/285)*100)+"%"+" "+f.format((double)((float)Count_CHULA/285)*100)+"%"+" "+f.format((double)((float)Count_MYREN/285)*100)+"%"+" "+f.format((double)((float)Count_GIST3/285)*100)+"%");
		out.println(sdf_short.format(timestamp_yesterday)+" "+"285"+" "+document.getString("SmartX-Box-GIST1")+"%"+" "+document.getString("SmartX-Box-PH")+"%"+" "+document.getString("SmartX-Box-HUST")+"%"+" "+document.getString("SmartX-Box-CHULA")+"%"+" "+document.getString("SmartX-Box-MYREN")+"%"+" "+document.getString("SmartX-Box-GIST3")+"%");
		
		out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Count_GIST1=0;Count_GIST2=0;Count_GIST3=0;Count_HUST=0;Count_MYREN=0;Count_CHULA=0;Count_PH=0;Count_NCKU=0;
		document.clear();
		
		
	}

}
