package smartx.multiview.collectors.microbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
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

import com.mongodb.client.MongoCollection;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_collectd_aggregate extends TimerTask {
	private MongoDB_Connector mongoConnector;
	private String topologyMongoCollection_collectd = "microbox-collectd"; //Change collection name
	private String topologyMongoCollection_collectd_aggregate_idle = "microbox-collectd-DailyCollection_CPU_idle"; //Change collection name
	private String topologyMongoCollection_collectd_aggregate_free = "microbox-collectd-DailyCollection_Mem_free"; //Change collection name
	public List<Document> documents_OneDay;
	private Date timeStamp_file;
	private Date timestamp;
	public int count_oneday_collectd_SmartX_Box_idle=0,count_oneday_collectd_SmartX_Box_free=0,Box_GIST1_collectd=0,Box_PH_collectd=0,Box_HUST_collectd=0,Box_CHULA_collectd=0,Box_MYREN_collectd=0,Box_GIST3_collectd=0, Box_GIST0_collectd=0, collectd_record_total_today=0;
	private Document document_collectd_status_free,document_collectd_status_idle;
	private int count_site_yaml=0,count_site=0;
	String sites_name[];
	int sites_count_free[],sites_count_idle[];
	public String path="/microbox_sites.yaml";
	
	private int count_site_working=0;
	String sites_name_working[];
	public String path_sites_working="/microbox_sites_working.yaml";
	
	public get_collectd_aggregate(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************collectd Timer Called************************************************************** ");
		try {
			this.get_collectd();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	private void get_collectd() throws ParseException {
		
		documents_OneDay = new ArrayList<Document>();
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_collectd);
		
		List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0); 
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String yesterday_timeStamp =getYesterdayDateString();
		
		try {
			count_site_yaml=getNumberofSites();
						
			sites_name=  new String[count_site_yaml];
			sites_count_free=  new int[count_site_yaml];
			sites_count_idle=  new int[count_site_yaml];
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for (Document document : documents) 
		{
		
/////Getting data from previous day//////
			if (document.getString("timestamp").contains(yesterday_timeStamp))
		
			{

				//documents_OneDay.add(document);
				if(!document.containsValue("-1"))
        		{
        			documents_OneDay.add(document);
        		}

			}
		}
		
		
		//get working sites list
		
				get_working_sites_list();
		
		
		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss zzz");
		SimpleDateFormat simpleDateFormatNoZone = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd"); 
		timeStamp_file=  new Date();
		String date_time = "Daily-report-collectd-data"+"_"+simpleDateFormat_startDate.format(yesterday());
		String csvFile = date_time.replace("/", "")+".csv";
		document_collectd_status_free = new Document();
		document_collectd_status_idle = new Document();
		
		
		
		
		
		
		
		
		Yaml yaml = new Yaml();
		try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
			Iterable<Object> itr = yaml.loadAll(in);
			//Iterable<Object> itr1 = itr;
			System.out.println("-- iterating loaded Iterable --");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
			//int i=3;
			
			String microbox_site_source="";
			count_site=0;
			
			//We calculate the total number of pings collected from each microbox to other microboxes
			for (Object o : itr) 														
			{ 
				microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
				sites_name[count_site]=microbox_site_source;
				System.out.println("sites_name[count_site]:"+sites_name[count_site]);
				count_site=count_site+1;
			}
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//writing to file
		
		try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-collectd-data/"+csvFile, true);
				

				BufferedWriter bw = new BufferedWriter(fw);

				PrintWriter out = new PrintWriter(bw))
		{
			File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-collectd-data/"+csvFile);
			if (file.length() == 0)
	        {
				out.print("Date"+" "+"Expected Value"+" "+"Type_instance");
				for( int i = 0; i < sites_name_working.length; i++)
				{
					System.out.println("sites_name_working[i]:"+sites_name_working[i]);
					out.print(sites_name_working[i]+" ");
				}
				out.println();

	        }

			for( int i = 0; i < count_site_yaml; i++)
			{
				for (Document document : documents_OneDay) 
				{
					if (document.getString("microbox-SOURCE").contains(sites_name[i]) &&  (document.getString("type_instance").contains("free")))
							{
						//System.out.println(document.toString());
						calculate_collectd_free(document,i);
							}
					if (document.getString("microbox-SOURCE").contains(sites_name[i]) &&  (document.getString("type_instance").contains("idle")))
					{
				//System.out.println(document.toString());
				calculate_collectd_idle(document,i);
					}

				}
				System.out.println("sites_name[i]:count_oneday_collectd_SmartX_Box_free"+sites_name[i]+":"+count_oneday_collectd_SmartX_Box_free);
				count_oneday_collectd_SmartX_Box_free=0;
				count_oneday_collectd_SmartX_Box_idle=0;
			}
			//System.out.println("Liveliness of Connection with VCenter:" + Box_GIST1_collectd +" "+ Box_PH_collectd +" "+ Box_HUST_collectd +" "+ Box_CHULA_collectd +" "+ Box_MYREN_collectd +" "+ Box_GIST3_collectd);
			
			
			document_collectd_status_free.put("timestamp",   simpleDateFormat.format(yesterday()/*timeStamp_file*/));
			document_collectd_status_idle.put("timestamp",   simpleDateFormat.format(yesterday()/*timeStamp_file*/));
			
			for( int i = 0; i < count_site_yaml; i++)
			{
				/*float f1= round((((float)sites_count[i]/144)*100),2);
				if (f1>100) 
				{
					document_collectd_status.put(sites_name[i],"100");
					
				}
				else
				{
					document_collectd_status.put(sites_name[i], Float.toString(f1));
				}*/
				//document_collectd_status_free.put("Expected_Values",   "144");
				document_collectd_status_free.put("type_instance",   "Memory Free Daily Collection");
				document_collectd_status_free.put(sites_name[i], Integer.toString(sites_count_free[i]));
				
				//document_collectd_status_idle.put("Expected_Values",   "144");
				document_collectd_status_idle.put("type_instance",   "CPU Idle Daily Collection");
				document_collectd_status_idle.put(sites_name[i], Integer.toString(sites_count_idle[i]));
				
				
				
			}
			System.out.println("document_collectd_status_free"+document_collectd_status_free.toString());
			mongoConnector.getDbConnection().getCollection(topologyMongoCollection_collectd_aggregate_free).insertOne(document_collectd_status_free);
			mongoConnector.getDbConnection().getCollection(topologyMongoCollection_collectd_aggregate_idle).insertOne(document_collectd_status_idle);
			
			for( int i = 0; i < sites_name_working.length; i++)
			{
				out.print(document_collectd_status_free.getString("timestamp"+ " "));
				//out.print(document_collectd_status_free.getString("Expected_Values"+ " "));
				out.print(document_collectd_status_free.getString(sites_name_working[i])+ " ");
				
			}
			for( int i = 0; i < sites_name_working.length; i++)
			{
				out.print(document_collectd_status_idle.getString("timestamp"+ " "));
				//out.print(document_collectd_status_idle.getString("Expected_Values"+ " "));
				out.print(document_collectd_status_idle.getString(sites_name_working[i])+ " ");
				
			}
			out.println("");
			document_collectd_status_free.clear();
			document_collectd_status_idle.clear();
			reset_values();
		
		
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
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
	
	private void get_working_sites_list() {
		// TODO Auto-generated method stub
		
		try {
			count_site_working= getNumberofSitesWorking();

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
	
	
	private String getYesterdayDateString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return dateFormat.format(yesterday());
	}
	private String getTodayDateString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return dateFormat.format(today());
	}
	private Date yesterday() {
		// TODO Auto-generated method stub
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	private Date today() {
		// TODO Auto-generated method stub
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		return cal.getTime();
	}
	
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	private void reset_values() {
		// TODO Auto-generated method stub
		count_oneday_collectd_SmartX_Box_idle=0;
		count_oneday_collectd_SmartX_Box_free=0;
		collectd_record_total_today=0;
	}
	private void calculate_collectd_free(Document document, int site_counter) {

		count_oneday_collectd_SmartX_Box_free+=1;
		if (document.getString("type_instance").contains("free"))
				{
					sites_count_free[site_counter]=count_oneday_collectd_SmartX_Box_free;
				}
		
	}
	private void calculate_collectd_idle(Document document, int site_counter) {

		count_oneday_collectd_SmartX_Box_idle+=1;
		
		if (document.getString("type_instance").contains("idle"))
		{
			sites_count_idle[site_counter]=count_oneday_collectd_SmartX_Box_idle;
		}
	}
}
	
