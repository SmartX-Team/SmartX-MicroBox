package smartx.multiview.collectors.microbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_IOVISOR_aggregate extends TimerTask {
	private Document document;
	//private Document count;
	private String IOVISORMongoCollection = "microbox-IOVISOR-data"; 
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private Thread thread;
	private String ThreadName = "IOVISOR Daily Collection report Thread";
	private Date timestamp;
	private int Count_GIST1=0,Count_GIST2=0,Count_GIST3=0,Count_HUST=0,Count_MYREN=0,Count_CHULA=0,Count_PH=0,Count_NCKU=0;
	private Date timestamp_yesterday;
	private int count_sites_total=0,Count_microbox=0,count_site_yaml=0;
	private String count[];
	public String path="/microbox_sites.yaml";
	String sites_name[];
	public String path_sites_working="/microbox_sites_working.yaml";
	private String sites_name_working[];
	int count_site_working=0;

	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void get_IOVISOR() throws ParseException, IOException
	
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
		
		get_working_sites_list();
		
		try {
			count_site_yaml=getNumberofSites();
						
			sites_name=  new String[count_site_yaml];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(timestamp); 
		c.add(Calendar.DATE, -1);
		timestamp_yesterday = c.getTime();
		date_today =sdf.format(timestamp_yesterday);
		
		String date_time = "Daily-report-microbox-IOVISOR"+"_"+sdf_short.format(timestamp_yesterday);
		
		
		String path="/microbox_sites.yaml";
		
		try {
			count_site_yaml=getNumberofSites();
			count=  new String[count_site_yaml];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		Yaml yaml = new Yaml();
        try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
            Iterable<Object> itr = yaml.loadAll(in);
            System.out.println("-- iterating loaded Iterable --");
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
            int i=3;
           // document.put("timestamp",   sdf.format(timestamp));
			//document.put("microbox-SOURCE",   record_values[2]);
            document.put("timestamp", date_today);
    		document.put("Total Files expected", "288");
            for (Object o : itr) 
            {
            	 String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
            	 System.out.println("microbox_site:"+microbox_site);
            	 System.out.println("sites_name.lenght:"+sites_name.length);
            	 System.out.println("	:"+count_sites_total);
            	 sites_name[count_sites_total]=microbox_site;
            	 for (File file : listOfFiles) {
         		    // Be aware that folder.listFiles() give list with directories and files
         		    if (file.isFile()) {
         		        
         		    	
         		        // apply your filter. for simplicity I am equating the file names
         		        // name refers to input variable to method
         		        if(file.getName().contains("smartx-"+microbox_site+"-mc-"+date_today)) { 
         		        	// smartx-microbox-itb-1-mc-2019-03-06-01-10
         		       /*System.out.println("file.getName():"+file.getName());*/
         		            // create user object and add it to list. 
         		            // Change below line with appropriate constructor params
         		            
         		       filteredList.add(file.getName());
         		           Count_microbox+=1;
         		          document.put(microbox_site, Integer.toString(Count_microbox));
         		       /*  System.out.println("document.getString(microbox_site):"+document.getString(microbox_site));*/
         		            
         		        }                                
         		      
         		      
         		    //System.out.println("document:"+document.toString());
         		        
         		    }
         		}
            	 
                 
               System.out.println("document.getString(microbox_site):"+document.getString(microbox_site));
               System.out.println("Count_microbox:"+Count_microbox);
               //System.out.println("document.getString(microbox_site):"+document.getString(microbox_site));
               if(Count_microbox>0)
               {
               DecimalFormat f = new DecimalFormat("##.00");
   		       double d = ((float) Integer.parseInt(document.getString(microbox_site))/285)*100;
   		       System.out.println("d:"+d);
   		      //document.put(microbox_site, f.format((double)d);
   		       document.put(microbox_site, (((float) Integer.parseInt(document.getString(microbox_site))/285)*100)>100 ? "100.00":f.format(((float) Integer.parseInt(document.getString(microbox_site))/285)*100));
   		       System.out.println("Percentage value for a day:"+document.getString(microbox_site)+"%");  
               }
               else
            	   document.put(microbox_site,"0");
   		       
               count_sites_total=count_sites_total+1;
               Count_microbox=0;
               System.out.println("");
               System.out.println("");
            }
            mongoConnector.getDbConnection().getCollection(IOVISORMongoCollection).insertOne(document);
           
            
		
		
		
		String csvFile = date_time.replace("/", "")+".csv";

		
		
		
		try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-IOVISOR-data/"+csvFile, true);

				BufferedWriter bw = new BufferedWriter(fw);

				PrintWriter out = new PrintWriter(bw))
		{
			File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-IOVISOR-data/"+csvFile);
			if (file.length() == 0)
	        {
				out.print("Date"+" "+"Total_Expected_Files"+" ");
				
				out.print("Date"+" "+"TIME"+" "+"SOURCE-SITE"+" ");
				for ( i = 0; i < sites_name_working.length; i++) {
					out.print(sites_name_working[i]+" ");	
				}
				out.println("");
	        }
			else
				out.println("");
				
				
				
				
				/*for (int l = 0; l < count_sites_total; l++)
			        {
					if ((sites_name[l].equals("microbox-ptit-1"))|| (sites_name[l].equals("microbox-monash-1")) 
							|| (sites_name[l].equals("microbox-itb-1"))|| (sites_name[l].equals("microbox-iit-1"))
							|| (sites_name[l].equals("microbox-nuol-1")))
					{
						System.out.println("sites_name[l]:"+sites_name[l]);
					}
					else
					{
					if (l+1==count_sites_total)//check for last one
						out.print(sites_name[l]);
					else
						out.print(sites_name[l]+" ");
			        }
			        
				
				//out.println("Date"+" "+"Total_Expected_Files"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
	        }*/
				
	        
			
			
			//out.println(sdf_short.format(timestamp_yesterday)+" "+"280"+" "+document.getString("SmartX-Box-GIST1")+"%"+" "+document.getString("SmartX-Box-PH")+"%"+" "+document.getString("SmartX-Box-HUST")+"%"+" "+document.getString("SmartX-Box-CHULA")+"%"+" "+document.getString("SmartX-Box-MYREN")+"%"+" "+document.getString("SmartX-Box-GIST3")+"%");
		
			out.print(sdf_short.format(timestamp_yesterday)+" "+"288"+" ");
			
			
			
			for (String s: sites_name_working) {  
				out.print(document.get(s)+"%"+" ");
				System.out.println("sites_name_working[i]"+s);
				System.out.println("document.get(sites_name_working[i]"+document.get(s));
			}
			
			
			
			/*for (int l = 0; l < count_sites_total; l++)
	        {//Site to exclude
				if ((sites_name[l].equals("microbox-ptit"))|| (sites_name[l].equals("microbox-monash")) 
						|| (sites_name[l].equals("microbox-itb-1"))|| (sites_name[l].equals("microbox-iit-1"))
						|| (sites_name[l].equals("microbox-drukren-1")))
				{
					System.out.println("sites_name[l]:"+sites_name[l]);
				}
				else
				{
					
				
				if (l+1==count_sites_total)
					out.print(document.getString(sites_name[l])+"%");
				else
					out.print(document.getString(sites_name[l])+"%"+" ");
	        }
				}
			out.println("");*/
			out.println("");
		out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		document.clear();
		count_sites_total=0;
		
	}

}

	private int getNumberofSites() throws IOException {
		// TODO Auto-generated method stub
	Map<String, Float> uptime_oneday_ping_microbox_hmap = new HashMap<String, Float>();
	String write_to_file = "";
	System.out.printf("-- loading from %s --%n", path);
	Yaml yaml = new Yaml();
	int siteCount = 0;
	try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
		Iterable<Object> itr = yaml.loadAll(in);
		
		System.out.println("-- iterating loaded Iterable --");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
		
		for (Object o : itr) 															//We calcualcuate the total number of pings collected from each microbox to other microboxes
		{ 
			siteCount++;
		}
	}
		return siteCount;
	}
	
	private void get_working_sites_list() {
		// TODO Auto-generated method stub
		
		try {
			count_site_working=getNumberofSitesWorking();

			//count_oneday_ping= new int[count_site_working];
			//anStringArray= new String[count_site_working+1][count_site_working+1];

			sites_name_working=  new String[count_site_working];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("count_site_working"+count_site_yaml);
		
		
		
		System.out.printf("-- loading from %s --%n", path);
		 Yaml yaml = new Yaml();
		 try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path_sites_working)) {
			 Iterable<Object> itr = yaml.loadAll(in);
			 //Iterable<Object> itr1 = itr;
			 System.out.println("-- iterating loaded Iterable --");
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			 timestamp = new Date();
			 int i=0;

			 String microbox_site_source="";
			 //					

			 //We calculate the total number of pings collected from each microbox to other microboxes
			 for (Object o : itr) 														
			 { 
				 microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
				 sites_name_working[i]=microbox_site_source;
				 
				 i+=1;
			 }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	private int getNumberofSitesWorking() throws IOException {
		// TODO Auto-generated method stub
		
		
		System.out.printf("-- loading from %s --%n", path);
		Yaml yaml = new Yaml();
		int siteCount = 0;
		try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path_sites_working)) {
			Iterable<Object> itr = yaml.loadAll(in);

			System.out.println("-- iterating loaded Iterable --");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();





			for (Object o : itr) 															//We calcualcuate the total number of pings collected from each microbox to other microboxes
			{ 
				siteCount++;
			}
		}
		return siteCount;
	}
	
}
