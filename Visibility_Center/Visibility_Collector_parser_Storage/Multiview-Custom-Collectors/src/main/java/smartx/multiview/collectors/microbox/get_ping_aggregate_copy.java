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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import smartx.multiview.DataLake.MongoDB_Connector;

public class get_ping_aggregate_copy  extends TimerTask {
	private Thread thread;
	private String topologyMongoCollection_ping = "microbox-ping-data-raw"; //Change collection name
	private String microboxMongoCollection_ping_aggregate = "microbox-ping-data-aggregate"; //Change collection name
	private String topologyMongoCollection_ping_collection = "microbox-ping-collection"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "TCP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_ping"; 
	private Document document;
	private Date timestamp;
	public int Box_GIST1_ping=0,Box_PH_ping=0,Box_HUST_ping=0,Box_CHULA_ping=0,Box_MYREN_ping=0,Box_GIST3_ping=0, Box_GIST0_ping=0, ping_record_total_today=0; 
	private int[] microbox_ping;
	public String path="/microbox_sites.yaml";
	public String path_sites_working="/microbox_sites_working.yaml";
	private String sites_name_working[];

	//public HashMap<String,HashMap<String,Integer>> microbox_ping_allsites_hmap= new HashMap<String,HashMap<String,Integer>>();

	public HashMap<String, Integer> microbox_ping_hmap= new HashMap<String, Integer>();
	public HashMap<String, Integer> microbox_ping_copy_hmap= new HashMap<String, Integer>();

	public int count_oneday_singlesite_ping_SmartX_Box=0,count_oneday_ping_SmartX_Box=0,count_oneday_ping_SmartX_Box_GIST1=0, count_oneday_ping_SmartX_Box_PH=0, count_oneday_ping_SmartX_Box_HUST=0, count_oneday_ping_SmartX_Box_CHULA=0, 
			count_oneday_ping_SmartX_Box_MYREN=0, count_oneday_ping_SmartX_Box_GIST3=0, count_oneday_ping_SmartX_Box_GIST_0=0;
	public List<Document> documents_OneDay;
	private Document document_pingscore,document_pingcollection;
	int count_site=0,Number_of_sites=0, count_site_yaml,count_site_working=0;
	float livliness_site=0;
	String found="";
	String[][] anStringArray;
	int [] count_oneday_ping;
	SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd");
	private Document document_collection;
	private String sites_name[];//= new String[];
	
	/*private Document document_collection;
	public float uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_PH=0,uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_GIST1_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_PH_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_HUST_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_CHULA_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST3=0, uptime_oneday_ping_SmartX_Box_MYREN_dest_Box_GIST0=0;
	public float uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST1=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_PH=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_HUST=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_CHULA=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_MYREN=0, uptime_oneday_ping_SmartX_Box_GIST3_dest_Box_GIST0=0;

	 */
	private DecimalFormat f = new DecimalFormat("##.00");
	public get_ping_aggregate_copy(MongoDB_Connector mongoConnector2) {
		mongoConnector = mongoConnector2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************Ping Timer Called************************************************************** ");
		try {
			this.get_pinglatency();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	private void get_pinglatency() throws ParseException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_ping);

//get number of sites.

		try {
			count_site_yaml=getNumberofSites();

			count_oneday_ping= new int[count_site_yaml];
			anStringArray= new String[count_site_yaml+1][count_site_yaml+1];

			sites_name=  new String[count_site_yaml];
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("count_site_yaml"+count_site_yaml);

//get working sites list
		
		get_working_sites_list();

		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat_endDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
		String yesterday_timeStamp =getYesterdayDateString();
		String yesterday_timeStamp_simple=getYesterdayDateString_simple();
		System.out.println("Local Time: " + simpleDateFormat_endDate.format(timestamp));

		String date =simpleDateFormat_startDate.format(timestamp)+ " 00:00:01 KST"; //start of the today's date

		String date1 =simpleDateFormat_endDate.format(timestamp); //Current data and time



		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss zzz");  


		//      SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd"); 

		Date startDate= simpleDateFormat.parse(date); 
		Date endDate= simpleDateFormat.parse(date1);
		System.out.println("**************************Start time and end time*******************************: ");
		System.out.println(startDate);
		System.out.println(endDate);



		Calendar today_now = Calendar.getInstance();

		System.out.println(today_now.getTime());




		List<Document> documents = collection_process.find().into(new ArrayList<Document>());
		documents_OneDay = new ArrayList<Document>();



		System.out.println("Collection Count for todays Ping data");
		System.out.println(collection_process.count());


		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0); 


		String today_timestamp=getTodayDateString();

		for (Document document : documents) 
		{
			//System.out.println(document);


			///////////////////////////////////////////////////////set timestamp for record search///////////////////////////////////////////////////////


			if (document.getString("timestamp").contains(yesterday_timeStamp))
				//if (document.getString("timestamp").contains(today_timestamp))
			{

				if(!document.containsValue("-1"))
				{
					documents_OneDay.add(document);
				}


			}


		}
		/*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////            SmartX-Box-GIST              /////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 */		/*for (Document document : documents_OneDay) 
		 {
			 System.out.println("Printing one day (today) ping data only from documents_OneDay variable");
			 System.out.println(document);
		 }*/


		 document_pingscore = new Document();
		 document_pingcollection = new Document();

		 String date_time = "Daily-report-ping-data"+"_"+yesterday_timeStamp/*simpleDateFormat_startDate.format(yesterday_timeStamp)*/;
		 String Ping_collection = "Daily-report-ping-collection"+"_"+yesterday_timeStamp/*simpleDateFormat_startDate.format(yesterday_timeStamp)*/;
		 String csvFile = date_time.replace("/", "")+".csv";

		 String Ping_collection_day = Ping_collection.replace("/", "")+".csv";

		 try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-ping-data/"+csvFile, true);

				 BufferedWriter bw = new BufferedWriter(fw);

				 PrintWriter out = new PrintWriter(bw))
		 {
			 File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-ping-data/"+csvFile);

			 if (file.length() == 0)
			 {
				 out.print("Date"+" "+"SOURCE-SITE"+" ");
				 //out_pingcount("Date"+" "+"SOURCE-SITE"+" ");
				 for (int l = 0; l < sites_name_working.length; l++)
				 {
					 out.print(sites_name_working[l]+" ");
					 //out_pingcount.print(sites_name[l]+" ");
				 }
				 out.println();
				 //out_pingcount.println();
			 }
			 

			 //for (Document document : documents_OneDay) 
			 //{
			 HashMap<String,Integer> count_oneday_ping_hmap = new HashMap<String,Integer>();
			 HashMap<String,HashMap<String,Integer>> microbox_ping_allsites_hmap= new HashMap<String,HashMap<String,Integer>>();




			 //Map<String, Float> uptime_oneday_ping_microbox_hmap = new HashMap<String, Float>();
			 String write_to_file = "";
			 System.out.printf("-- loading from %s --%n", path);
			 Yaml yaml = new Yaml();
			 try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
				 Iterable<Object> itr = yaml.loadAll(in);
				 //Iterable<Object> itr1 = itr;
				 System.out.println("-- iterating loaded Iterable --");
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
				 timestamp = new Date();
				 int i=3;
				 count_site=0;
				 Number_of_sites=0;

				 String microbox_site_source="";
				 //					

				 //We calculate the total number of pings collected from each microbox to other microboxes
				 for (Object o : itr) 														
				 { 
					 microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
					 
					 System.out.println("sites_name length"+sites_name.length);
					
					 
					 sites_name[count_site]=microbox_site_source;
					 //microbox_ping_hmap.put("microbox-SOURCE",microbox_site_source);

					 for (Document document1 : documents_OneDay) 
					 {
						 //String microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
						 if (document1.getString("microbox-SOURCE").contains(microbox_site_source))
						 {
							 //System.out.println(document.getString("SmartX-Box-SOURCE"));
							 if(count_oneday_ping_hmap.get(microbox_site_source)==null )
							 {
								 count_oneday_ping_hmap.put(microbox_site_source,1);
							 }
							 else
								 count_oneday_ping_hmap.put(microbox_site_source,count_oneday_ping_hmap.get(microbox_site_source)+1); //Total ping for Individual sites
							 calculate_ping(document1,microbox_site_source,yesterday_timeStamp);
							 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								


							 microbox_ping_copy_hmap=(HashMap<String, Integer>) microbox_ping_hmap.clone();



							 microbox_ping_allsites_hmap.put(microbox_site_source, microbox_ping_copy_hmap);
							 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////							
							 System.out.println("microbox_ping_allsites_hmap="+microbox_ping_allsites_hmap.get(microbox_site_source));
							 System.out.println("microbox_ping_hmap.toString()="+microbox_ping_hmap.toString());
							 //output {microbox-um-2=2, microbox-um-1=2, microbox-vnu-1=2, microbox-gist-1=-1, microbox-gist-2=2}


							 count_oneday_ping_hmap.put("Total",count_oneday_ping_SmartX_Box); //Total ping for all Sites

							 System.out.println("count_oneday_singlesite_ping_SmartX_Box="+count_oneday_ping_hmap.get(microbox_site_source));
							 //count_oneday_singlesite_ping_hmap.put(microbox_site_source,count_oneday_singlesite_ping_SmartX_Box); //Total ping for Individual sites
							 //count_oneday_singlesite_ping_SmartX_Box=0;								
						 }


					 }

					 //putting zero in sites not present in the mongoDB


					 for (Document document1 : documents_OneDay) 
					 {
						 if (document1.getString("microbox-SOURCE").contains(microbox_site_source))
						 {
							 found = "TRUE";	
							 System.out.println("found is  True");
						 }
					 }
					 if (found!="TRUE")
					 {
						 System.out.println("found is Not True");
						 System.out.println(count_site);

						 anStringArray[count_site][0]=microbox_site_source;
						 System.out.println(anStringArray[count_site][0]);
						 for(int j=1; j<count_site_yaml+1; j++) 
						 {
							 anStringArray[count_site][j]= "0";									
						 }
						 count_oneday_ping_hmap.put(microbox_site_source,0); 
					 }
					 found="FALSE";

					 count_site=count_site+1;

					 for (Entry<String, Integer> entry : microbox_ping_hmap.entrySet()) {
						 entry.setValue(entry.getValue() - 0);
					 }						 

					 Iterator it = microbox_ping_hmap.entrySet().iterator();
					 Map.Entry keyValue;
					 while (it.hasNext()) {
						 keyValue = (Map.Entry)it.next();
						 microbox_ping_hmap.put((String) keyValue.getKey(), 0);
						 //Now you can have the keys and values and easily replace the values...
					 }

					 Number_of_sites=Number_of_sites+1;

				 }

				 

				 //microbox-gist-1,microbox-gist-2,microbox-um-1,microbox-um-2

				 document_pingscore.clear();
				 count_site=0;
				 
				 //reset_values();

				 //we put the site values in two dimensional array and also get the percentage liveliness of ping
				 int site_occourance=0;
				 System.out.println("Number_of_sites="+Number_of_sites);
				 System.out.println("-------------------------------------------------------------------------------------------------------------------------");
				 for(int j=0; j<Number_of_sites; j++) {

					 document_pingscore.clear();
					 document_pingscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(yesterday()/*yesterday_timeStamp*/));

					 document_pingscore.put("SmartX-Box-SOURCE",  sites_name[j]);
					 //count_oneday_ping[j]= count_oneday_ping_hmap.get(microbox_site_source); // get total ping for each site in Int array

					 for(int k=0; k<anStringArray[Number_of_sites].length; k++) {
						 System.out.println("			===================================================");
						 System.out.println("			Values at arr["+j+"]["+k+"] is "+anStringArray[j][k]); //example Values at arr[0][0] is microbox-gist-1

						 if( anStringArray[j][k].equals(null)) //at k==- we have the site name
						 {
							 System.out.println("%%%%%%%%%%%%%%%%%% anStringArray[j][k].equals(null))");
						 }

						 else if( k!=0 && !anStringArray[j][k].equals("-")) //at k==- we have the site name
						 {
							 System.out.println("sites_name["+j+"]:						"+sites_name[j]); //
							 site_occourance=count_oneday_ping_hmap.get(sites_name[j]); 
							 
							 count_oneday_ping[j]=count_oneday_ping_hmap.get(sites_name[j]);
							 document_pingcollection.put(sites_name[j], Integer.toString(count_oneday_ping[j]));
							 
							 System.out.println("site occourance in DB:						"+site_occourance);  ///number of time site data is present in DB

							 System.out.println("Value of site"+sites_name[j]+" at anStringArray["+j+"]["+k+"]:		"+anStringArray[j][k]);
							 System.out.println("(Integer.valueOf(anStringArray["+j+"]["+k+"]):"+(Integer.valueOf(anStringArray[j][k])));


							 System.out.println("Percentage of site alive arr["+j+"]["+k+"] is 				"+((float)(Integer.valueOf(anStringArray[j][k]))/site_occourance)*100+"%");
							 if(site_occourance==0)
							 {
								 livliness_site=0;
							 }
							 else
								 livliness_site=round(((float)(Integer.valueOf(anStringArray[j][k]))/site_occourance)*100,2);

							 System.out.println("Liveliness of site arr["+j+"]["+k+"] is 				"+livliness_site+"%");
							 System.out.println("");


							 document_pingscore.put(sites_name[k-1], livliness_site+"%");
							 // System.out.println("document_pingscore:"+document_pingscore.toString());


						 }
						 else if( anStringArray[j][k].equals("-"))
						 {
							 document_pingscore.put(sites_name[k-1], "-"  );
							 System.out.println("document_pingscore:"+document_pingscore.toString());
						 }
						 //				            else


					 }
					 System.out.println("document_pingscore:"+document_pingscore.toString());
					 mongoConnector.getDbConnection().getCollection(microboxMongoCollection_ping_aggregate).insertOne(document_pingscore);

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////Writing Ping Data to file////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
					
					 for (int m = 0; m < sites_name_working.length; m++)
				 		{
						 if (document_pingscore.getString("SmartX-Box-SOURCE").equals(sites_name_working[m]))
							{
							 out.print(simpleDateFormatNoTime.format(yesterday())+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" ");
							 
							 for (int l = 0; l < sites_name_working.length; l++)
							 {
								 out.print(document_pingscore.getString(sites_name_working[l])+" ");
								 
								 //out_pingcount.print(count_oneday_ping+" ");
							 }
							 out.println();
							}
						 
				 		}
					 
					 


					 // out.print(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" ");
					 
					 System.out.println("");
					 System.out.println("");

				 }
			 }
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 csvFile = Ping_collection.replace("/", "")+".csv";

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////Writing Ping Collection////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

		 try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-ping-collection/"+csvFile, true);

				 BufferedWriter bw = new BufferedWriter(fw);

				 PrintWriter out_printcount = new PrintWriter(bw))
		 {
			 File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-ping-collection/"+csvFile);

			 if (file.length() == 0)
			 {
				 out_printcount.print("Date"+" "+"Expected_Collection"+" ");

				 for (int l = 0; l < sites_name_working.length; l++)
				 {
					 /*if ((sites_name[l].equals("microbox-ptit"))|| (sites_name[l].equals("microbox-monash")) 
							 || (sites_name[l].equals("microbox-itb-1"))|| (sites_name[l].equals("microbox-iit-1"))
							 || (sites_name[l].equals("microbox-nuol-1")))
					 {*/
						 System.out.println("1-sites_name_working[l]:"+sites_name_working[l]);
					 /*}
					 else
					 {*/
						 out_printcount.print(sites_name_working[l]+" ");
						 System.out.println("1-sites_name_working[l]:"+sites_name_working[l]);
					 /*}*/

				 }
				 out_printcount.println();

			 }

			 //mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_collection).insertOne(document_pingscore);


			 document_collection = new Document();
			 document_collection.put("timestamp",  simpleDateFormatNoTime.format(yesterday()));
			 document_collection.put("Expected Collection",   "144");
			 out_printcount.print(simpleDateFormatNoTime.format(yesterday())+" "+"144"+" ");
			
			 for (int l = 0; l < sites_name.length; l++)
			 {
				 document_collection.put(sites_name[l],  Integer.toString(count_oneday_ping[l]) /*document_pingscore.getString(sites_name[l])*/);
				 System.out.println(document_collection.toString());
				 System.out.println(document_collection.getString(sites_name[l]));
			 }

			 for (int l = 0; l < sites_name_working.length; l++)
			 {
				 /*if ((sites_name[l].equals("microbox-ptit"))|| (sites_name[l].equals("microbox-monash")) 
						 || (sites_name[l].equals("microbox-itb-1"))|| (sites_name[l].equals("microbox-iit-1"))
						 || (sites_name[l].equals("microbox-nuol-1")))
				 {*/
					 System.out.println("2-sites_name_working[l]:"+sites_name_working[l]);
				/* }
				 else
				 {*/
					// out_printcount.print(Integer.toString(count_oneday_ping[l])+" ");
					 
					 out_printcount.print((document_pingcollection.getString(sites_name_working[l]))+" ");
					 
				/* }*/

			 }

			 mongoConnector.getDbConnection().getCollection(topologyMongoCollection_ping_collection).insertOne(document_collection);
			 //out_printcount.print(document_collection);
			 // out.print(simpleDateFormatNoTime.format(timestamp)+" "+document_pingscore.getString("SmartX-Box-SOURCE")+" ");
			 out_printcount.println();
			 System.out.println("");
			 System.out.println("");

		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }



		 /*try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-ping-collection/"+csvFile, true);

				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out_printcount = new PrintWriter(bw)
		{
					File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-ping-data/"+csvFile);
		}


		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}*/	










		 //we calculate the percentage of successful ping from from each microbox to other microboxes. total 144 pings in a day.

		 System.out.println("Total Count: "+ count_oneday_ping_SmartX_Box);

		 {
			 Yaml yaml1 = new Yaml();
			 try (InputStream in1 = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
				 Iterable<Object> itr1 = yaml1.loadAll(in1);

				 System.out.println();

				 //if(count_oneday_ping_hmap.get(microbox_site_source)>0) 
				 {
					 //uptime_oneday_ping_microbox_hmap.put(microbox_site_source, round((float)microbox_ping_hmap.get(microbox_site_source)/count_oneday_ping_hmap.get(microbox_site_source)*100,2));
					 //uptime_oneday_ping_microbox_hmap=round(((float) microbox_ping_hmap.get(microbox_site)/count_oneday_ping_hmap.get(microbox_site))*100,2);
					 //System.out.println(microbox_site_source+":"+uptime_oneday_ping_microbox_hmap.get(microbox_site_source));
				 }


			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }

		 List<Document> insertList = new ArrayList<Document>();

		 Date date11 = new Date();

		 
	}



	private void get_working_sites_list() {
		// TODO Auto-generated method stub
		
		try {
			count_site_working=getNumberofSitesWorking();
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

	private String getYesterdayDateString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return dateFormat.format(yesterday());
	}
	private String getYesterdayDateString_simple() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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

	private void reset_values() {
		Box_GIST1_ping=0;Box_PH_ping=0;Box_HUST_ping=0;Box_CHULA_ping=0;Box_MYREN_ping=0;Box_GIST3_ping=0; Box_GIST0_ping=0; ping_record_total_today=0; 
		count_oneday_ping_SmartX_Box=0; /*count_oneday_ping_SmartX_Box_PH=0; count_oneday_ping_SmartX_Box_HUST=0; count_oneday_ping_SmartX_Box_CHULA=0; 
				count_oneday_ping_SmartX_Box_MYREN=0; count_oneday_ping_SmartX_Box_GIST3=0; count_oneday_ping_SmartX_Box_GIST_0=0*/;

	}

	private void calculate_ping(Document document, String microbox_site_source,String yesterday_timeStamp) throws IOException {


		count_oneday_ping_SmartX_Box+=1;
		count_oneday_singlesite_ping_SmartX_Box+=1;
		System.out.println();
		System.out.printf("-- calculate_ping ,loading from %s --%n", path);
		Yaml yaml = new Yaml();

		try (InputStream in = ping_DailyreportConsumer.class.getResourceAsStream(path)) {
			Iterable<Object> itr = yaml.loadAll(in);
			System.out.println("-- iterating loaded Iterable --");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
			int i=0,count_site_vertical=1;
			document.put("timestamp",   sdf.format(yesterday()));

			//document.put("microbox-SOURCE",   record_values[2]);
			System.out.println("***********Source:"+microbox_site_source+"***********");
			anStringArray[count_site][0]=microbox_site_source;
			System.out.println("Source_site="+anStringArray[count_site][0]);
			for (Object o : itr) {
				String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
				// document.put(microbox_site,    record_values[i].isEmpty() ? "0" : record_values[i]);
				System.out.println("document.toString()"+document.toString());
				System.out.println("microbox_site"+microbox_site);
				System.out.println("document.getString(microbox_site)"+document.getString(microbox_site));
				if ((document.getString(microbox_site)!=null) && document.getString(microbox_site).contains("1"))
				{
					if (!microbox_ping_hmap.containsKey(microbox_site) || microbox_ping_hmap.get(microbox_site)==null)//Counting for the first time.
					{
						System.out.println("anStringArray[count_site][count_site_vertical]="+count_site+" "+count_site_vertical);
						System.out.println("anStringArray[count_site][count_site_vertical]="+anStringArray[count_site][count_site_vertical]);
						microbox_ping_hmap.put(microbox_site,1);
						anStringArray[count_site][count_site_vertical]= "1";
					}

					else
					{
						microbox_ping_hmap.put(microbox_site,microbox_ping_hmap.get(microbox_site)+1);
						anStringArray[count_site][count_site_vertical]= microbox_ping_hmap.get(microbox_site).toString();
					}


				}
				else if ((document.getString(microbox_site)!=null) && document.getString(microbox_site).contains("0"))
				{
					if (!microbox_ping_hmap.containsKey(microbox_site) || microbox_ping_hmap.get(microbox_site)==null)//Counting for the first time.
					{
						microbox_ping_hmap.put(microbox_site,0);
						anStringArray[count_site][count_site_vertical]= "0";
					}

					else
					{
						microbox_ping_hmap.put(microbox_site,microbox_ping_hmap.get(microbox_site)+0);
						anStringArray[count_site][count_site_vertical]= microbox_ping_hmap.get(microbox_site).toString();
					}


				}
				else if ((document.getString(microbox_site)!=null) && document.getString(microbox_site).contains("-"))
				{
					microbox_ping_hmap.put(microbox_site,-1);
					anStringArray[count_site][count_site_vertical]= "-";
				}
				else if (document.getString(microbox_site)==null)
				{
					if (!microbox_ping_hmap.containsKey(microbox_site) || microbox_ping_hmap.get(microbox_site)==null)//Counting for the first time.
					{
						microbox_ping_hmap.put(microbox_site,0);
						anStringArray[count_site][count_site_vertical]= "0";
					}

					else
					{
						microbox_ping_hmap.put(microbox_site,microbox_ping_hmap.get(microbox_site)+0);
						anStringArray[count_site][count_site_vertical]= microbox_ping_hmap.get(microbox_site).toString();
					}
				}
				else
				{
					microbox_ping_hmap.put(microbox_site,0);
					anStringArray[count_site][count_site_vertical]= "0";
				}

				System.out.println(microbox_site+"="+microbox_ping_hmap.get(microbox_site));

				System.out.println("Site_Values="+anStringArray[count_site][count_site_vertical]);
				count_site_vertical=count_site_vertical+1;
			}
		}
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}
