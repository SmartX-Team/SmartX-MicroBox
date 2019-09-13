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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import smartx.multiview.DataLake.Elasticsearch_Connector;
import smartx.multiview.DataLake.MongoDB_Connector;

public class get_latency_aggregate  extends TimerTask {
	private Thread thread;
	private String topologyMongoCollection_latency = "microbox-latency-data-raw"; //Change collection name
	private String microboxMongoCollection_latency_aggregate = "microbox-latency-data-aggregate"; //Change collection name
	private String topologyMongoCollection_latency_collection = "microbox-latency-collection"; //Change collection name
	//private String topologyMongoCollection_latency = "daily-report-latency-data-raw"; //Change collection name
	private String ThreadName = "TCP Daily report Thread";
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private String topic = "daily_report_latency"; 
	private Document document;
	private Date timestamp;
	public int Box_GIST1_latency=0,Box_PH_latency=0,Box_HUST_latency=0,Box_CHULA_latency=0,Box_MYREN_latency=0,Box_GIST3_latency=0, Box_GIST0_latency=0, latency_record_total_today=0; 
	private int[] microbox_latency;
	public String path="/microbox_sites.yaml";
	private String sites_name_working[];
	private int count_site_working=0;
	public String path_sites_working="/microbox_sites_working.yaml";


	//public HashMap<String,HashMap<String,Integer>> microbox_latency_allsites_hmap= new HashMap<String,HashMap<String,Integer>>();

	public HashMap<String, String> microbox_latency_hmap= new HashMap<String, String>();
	public HashMap<String, Integer> microbox_latency_copy_hmap= new HashMap<String, Integer>();

	public int count_oneday_singlesite_latency_SmartX_Box=0,count_oneday_latency_SmartX_Box=0,count_oneday_latency_SmartX_Box_GIST1=0, count_oneday_latency_SmartX_Box_PH=0, count_oneday_latency_SmartX_Box_HUST=0, count_oneday_latency_SmartX_Box_CHULA=0, 
			count_oneday_latency_SmartX_Box_MYREN=0, count_oneday_latency_SmartX_Box_GIST3=0, count_oneday_latency_SmartX_Box_GIST_0=0;
	public List<Document> documents_OneDay;
	private Document document_latencyscore;
	int count_site=0,Number_of_sites=0, count_site_yaml;
	float livliness_site=0;
	String found="";
	String[][] anStringArray;
	SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat ("yyyy/MM/dd");
	private String ESindex = "latency_aggregate_es";
	private Elasticsearch_Connector ESConnector;
	String sites_name[];//= new String[];
	/*private Document document_collection;
	public float uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH=0,uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST=0, uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA=0, uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN=0, uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3=0, uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST0=0;
	public float uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST1=0, uptime_oneday_latency_SmartX_Box_PH_dest_Box_HUST=0, uptime_oneday_latency_SmartX_Box_PH_dest_Box_CHULA=0, uptime_oneday_latency_SmartX_Box_PH_dest_Box_MYREN=0, uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST3=0, uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST0=0;
	public float uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1=0, uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH=0, uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA=0, uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN=0, uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3=0, uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0=0;
	public float uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1=0, uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_PH=0, uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST=0, uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN=0, uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3=0, uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST0=0;
	public float uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1=0, uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_PH=0, uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST=0, uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA=0, uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3=0, uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST0=0;
	public float uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1=0, uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_PH=0, uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST=0, uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA=0, uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN=0, uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST0=0;

	 */
	private DecimalFormat f = new DecimalFormat("##.00");
	public get_latency_aggregate(MongoDB_Connector mongoConnector2/*,Elasticsearch_Connector eSConnector2*/) {
		mongoConnector = mongoConnector2;
		/*ESConnector         = eSConnector2;
		ESConnector.createIndex(ESindex);*/
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("************************************************latency Timer Called************************************************************** ");
		try {
			
			this.get_latencylatency();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	private void get_latencylatency() throws ParseException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		MongoCollection<Document> collection_process = mongoConnector.getDbConnection()
				.getCollection(topologyMongoCollection_latency);



		try {
			count_site_yaml=getNumberofSites();
			sites_name=  new String[count_site_yaml];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("count_site_yaml"+count_site_yaml);
		anStringArray= new String[count_site_yaml+1][count_site_yaml+1];


		//get working sites list

		get_working_sites_list();

		SimpleDateFormat simpleDateFormat_startDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat_endDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
		timestamp = new Date();
		System.out.println("Local Tix`me: " + simpleDateFormat_endDate.format(timestamp));

		String date =simpleDateFormat_startDate.format(timestamp)+ " 00:00:01 KST"; //start of the today's date

		String date1 =simpleDateFormat_endDate.format(timestamp); //Current data and time



		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");  


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



		System.out.println("Collection Count for todays latency data");
		System.out.println(collection_process.count());


		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0); 

		String yesterday_timeStamp =getYesterdayDateString();
		String today_timestamp=getTodayDateString();

		for (Document document : documents) 
		{
			//System.out.println(document);

			///////////////////////////////////////////////////////set timestamp for record search///////////////////////////////////////////////////////


			//			if (document.getString("timestamp").contains(/*timeStamp*/yesterday_timeStamp/*"2019/03/06"*/))
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
		 */		for (Document document : documents_OneDay) 
		 {
			 System.out.println("Printing one day (today) latency data only from documents_OneDay variable");
			 System.out.println(document);
		 }


		 document_latencyscore = new Document();

		 String date_time = "Daily-report-latency-data"+"_"+simpleDateFormat_startDate.format(yesterday()/*yesterday_timeStamp*/);
		 String latency_collection = "Daily-report-total_latency"+"_"+simpleDateFormat_startDate.format(timestamp);
		 String csvFile = date_time.replace("/", "")+".csv";
		 String latency_collection_day = latency_collection.replace("/", "")+".csv";

		 new File("/home/netcs/active_monitoring/microbox/Daily-report-latency-data/").mkdirs();

		 try(FileWriter fw = new FileWriter("/home/netcs/active_monitoring/microbox/Daily-report-latency-data/"+csvFile, true);

				 BufferedWriter bw = new BufferedWriter(fw);

				 PrintWriter out = new PrintWriter(bw))
		 {
			 File file = new File("/home/netcs/active_monitoring/microbox/Daily-report-latency-data/"+csvFile);


			 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			 ///////////////////////////////////////////            SmartX-Box-GIST1              /////////////////////////////////////////
			 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


			 //for (Document document : documents_OneDay) 
			 //{
			 HashMap<String,Integer> count_oneday_latency_hmap = new HashMap<String,Integer>();
			 HashMap<String,HashMap<String,Integer>> microbox_latency_allsites_hmap= new HashMap<String,HashMap<String,Integer>>();




			 Map<String, Float> uptime_oneday_latency_microbox_hmap = new HashMap<String, Float>();
			 String write_to_file = "";
			 System.out.printf("-- loading from %s --%n", path);
			 Yaml yaml = new Yaml();
			 try (InputStream in = latency_DailyreportConsumer.class.getResourceAsStream(path)) {
				 Iterable<Object> itr = yaml.loadAll(in);
				 //Iterable<Object> itr1 = itr;
				 System.out.println("-- iterating loaded Iterable --");
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
				 timestamp = new Date();
				 int i=3;
				 Number_of_sites=0;
				 count_site=0;
				 //					

				 //We calculate the total number of latencys collected from each microbox to other microboxes
				 for (Object o : itr) 														
				 { 
					 String microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
					 sites_name[count_site]=microbox_site_source;
					 //microbox_latency_hmap.put("microbox-SOURCE",microbox_site_source);

					 for (Document document1 : documents_OneDay) 
					 {
						 //String microbox_site_source = ((String) o).substring(0, ((String) o).indexOf('='));
						 if (document1.getString("microbox-SOURCE").contains(microbox_site_source))
						 {
							 //System.out.println(document.getString("SmartX-Box-SOURCE"));
							 if(count_oneday_latency_hmap.get(microbox_site_source)==null )
							 {
								 count_oneday_latency_hmap.put(microbox_site_source,1);
							 }
							 else
								 count_oneday_latency_hmap.put(microbox_site_source,count_oneday_latency_hmap.get(microbox_site_source)+1); //Total latency for Individual sites


							 calculate_latency(document1,microbox_site_source);
							 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
							 microbox_latency_copy_hmap=(HashMap<String, Integer>) microbox_latency_hmap.clone();

							 microbox_latency_allsites_hmap.put(microbox_site_source, microbox_latency_copy_hmap);
							 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////							
							 System.out.println("microbox_latency_allsites_hmap="+microbox_latency_allsites_hmap.get(microbox_site_source));
							 System.out.println("microbox_latency_hmap.toString()="+microbox_latency_hmap.toString());
							 //output {microbox-um-2=2, microbox-um-1=2, microbox-vnu-1=2, microbox-gist-1=-1, microbox-gist-2=2}


							 count_oneday_latency_hmap.put("Total",count_oneday_latency_SmartX_Box); //Total latency for all Sites

							 System.out.println("count_oneday_singlesite_latency_SmartX_Box="+count_oneday_latency_hmap.get(microbox_site_source));
							 //count_oneday_singlesite_latency_hmap.put(microbox_site_source,count_oneday_singlesite_latency_SmartX_Box); //Total latency for Individual sites
							 //count_oneday_singlesite_latency_SmartX_Box=0;								
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
						 count_oneday_latency_hmap.put(microbox_site_source,0); 
					 }
					 found="FALSE";

					 count_site=count_site+1;

					 for (Entry<String, String> entry : microbox_latency_hmap.entrySet()) {
						 entry.setValue(entry.getValue());
					 }						 

					 Iterator it = microbox_latency_hmap.entrySet().iterator();
					 Map.Entry keyValue;
					 while (it.hasNext()) {
						 keyValue = (Map.Entry)it.next();
						 microbox_latency_hmap.put((String) keyValue.getKey(), "0");
						 //Now you can have the keys and values and easily replace the values...
					 }

					 Number_of_sites=Number_of_sites+1;

				 }

				 if (file.length() == 0)
				 {
					 out.print("Date"+" "+"SOURCE-SITE"+" ");
					 for (int l = 0; l < sites_name_working.length; l++)
					 {
						 out.print(sites_name_working[l]+" ");
					 }
					 out.println();
				 }

				 //microbox-gist-1,microbox-gist-2,microbox-um-1,microbox-um-2

				 document_latencyscore.clear();
				 
				 //reset_values();

				 //we put the site values in two dimensional array and also get the percentage liveliness of latency
				 int site_occourance=0;
				 System.out.println("Number_of_sites="+Number_of_sites);
				 System.out.println("-------------------------------------------------------------------------------------------------------------------------");
				 for(int j=0; j<Number_of_sites; j++) {

					 document_latencyscore.clear();
					 document_latencyscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(yesterday()));
					 document_latencyscore.put("SmartX-Box-SOURCE",  sites_name[j]);
					 

					 for(int k=0; k<anStringArray[Number_of_sites].length; k++) {
						 System.out.println("			===================================================");
						 System.out.println("			Values at arr["+j+"]["+k+"] is "+anStringArray[j][k]); //example Values at arr[0][0] is microbox-gist-1

						 if( anStringArray[j][k].equals(null)) //at k==0 we have the site name
						 {
							 System.out.println("%%%%%%%%%%%%%%%%%% anStringArray[j][k].equals(null))");
						 }

						 if( k!=0 && !anStringArray[j][k].equals("-")) //at k==0 we have the site name
						 {
							 System.out.println("sites_name["+j+"]:						"+sites_name[j]); //
							 site_occourance=count_oneday_latency_hmap.get(sites_name[j]);          
							 System.out.println("site occourance in DB:						"+site_occourance);  ///number of time site data is present in DB

							 System.out.println("Latency Value of site"+sites_name[j]+" at anStringArray["+j+"]["+k+"]:		"+anStringArray[j][k]);


							 float f = Float.parseFloat(anStringArray[j][k]);
							 System.out.println("(Integer.valueOf(anStringArray["+j+"]["+k+"]):"+f);
							


							 System.out.println("Average Site Latency arr["+j+"]["+k+"] is 				"+Float.toString(f/site_occourance));
							 if(site_occourance==0)
							 {
								 livliness_site=0;
							 }
							 else
								 livliness_site=round(f/site_occourance/*((float)(Integer.valueOf(anStringArray[j][k]))/site_occourance)*/,2);

							 System.out.println("Avg Latency of site arr["+j+"]["+k+"] is 				"+Float.toString(livliness_site));
							 System.out.println("");


							 document_latencyscore.put(sites_name[k-1], livliness_site);
							 // System.out.println("document_latencyscore:"+document_latencyscore.toString());

						 }
						 if( anStringArray[j][k].equals("-"))
						 {
							 document_latencyscore.put(sites_name[k-1], "-"  );
							 System.out.println("document_latencyscore:"+document_latencyscore.toString());
						 }
						 //				            else


					 }
					 System.out.println("document_latencyscore:"+document_latencyscore.toString());
					//uncomment
					  mongoConnector.getDbConnection().getCollection(microboxMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					 

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////Writing Latency Data to file////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
					 for (int m = 0; m < sites_name_working.length; m++)
					 {
						 if (document_latencyscore.getString("SmartX-Box-SOURCE").equals(sites_name_working[m]))
						 {
							 out.print(simpleDateFormatNoTime.format(yesterday())+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" ");
							 for (int l = 0; l < sites_name_working.length; l++)
							 {
								 try{
									 
									 out.print(document_latencyscore.get(sites_name_working[l])+" ");
								 }
								 catch(NumberFormatException ex){ // handle your exception
									// out.print(document_latencyscore.getString(sites_name[l])+" ");
								 }
							 }
							 out.println();
						 }

					 }
					 //uncomment
					 // out.print(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" ");
					 
					 System.out.println("");
					 System.out.println("");

				 }
			 }
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }









		 //we calculate the percentage of successful latency from from each microbox to other microboxes. total 144 latencys in a day.

		 System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);

		 {
			 Yaml yaml1 = new Yaml();
			 try (InputStream in1 = latency_DailyreportConsumer.class.getResourceAsStream(path)) {
				 Iterable<Object> itr1 = yaml1.loadAll(in1);

				 //????




				 /*	

							for (Object o1 : itr1) 
								{
								System.out.println("								***************************************************");
								String microbox_site_source = ((String) o1).substring(0, ((String) o1).indexOf('='));

									System.out.println("							microbox_site_source:"+microbox_site_source);

									System.out.println("microbox_latency_allsites_hmap:"+microbox_latency_allsites_hmap.get(microbox_site_source));




									System.out.println("Total latency Count:"+count_oneday_latency_hmap.get("Total"));									
									System.out.println("latency Count for:"+microbox_site_source+"="+count_oneday_latency_hmap.get(microbox_site_source));



									System.out.println("***Printing the Hashmap inside Hashmap***");
									microbox_latency_allsites_hmap.forEach((K,V)->{                 // mapofmaps entries
								         V.forEach((X,Y)->{                     // inner Hashmap enteries
								             System.out.println(X+" "+Y);       // print key and value of inner Hashmap 
								         });
								     });
				  */










				 System.out.println();

				 //if(count_oneday_latency_hmap.get(microbox_site_source)>0) 
				 {
					 //uptime_oneday_latency_microbox_hmap.put(microbox_site_source, round((float)microbox_latency_hmap.get(microbox_site_source)/count_oneday_latency_hmap.get(microbox_site_source)*100,2));
					 //uptime_oneday_latency_microbox_hmap=round(((float) microbox_latency_hmap.get(microbox_site)/count_oneday_latency_hmap.get(microbox_site))*100,2);
					 //System.out.println(microbox_site_source+":"+uptime_oneday_latency_microbox_hmap.get(microbox_site_source));
				 }


			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }

		 //document_latencyscore.put("timestamp",   /*sdf.format*/simpleDateFormat.format(timestamp));
		 //document_latencyscore.put("SmartX-Box-SOURCE",   microbox_site_source);
		 //???????????????
		 /*for (Object o1 : itr) 
								{
									String microbox_site = ((String) o1).substring(0, ((String) o1).indexOf('='));
									document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
									//document_latencyscore.put("SmartX-Box-SOURCE",   microbox_site_source);
									document_latencyscore.put(microbox_site,uptime_oneday_latency_microbox_hmap.get(microbox_site) );
								}

								mongoConnector.getDbConnection().getCollection(microboxMongoCollection_latency_aggregate).insertOne(document_latencyscore);

								System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
								System.out.println("*************Printing the latency Score***************");
								System.out.println(document_latencyscore.getString("timestamp")+" "+document_latencyscore.getString("SmartX-Box-SOURCE"));
								write_to_file= (document_latencyscore.getString("timestamp")+" "+document_latencyscore.getString("SmartX-Box-SOURCE"));
								for (Object o1 : itr) 
								{
									String microbox_site = ((String) o1).substring(0, ((String) o1).indexOf('='));
									System.out.println(document_latencyscore.getString(microbox_site));
									write_to_file+=document_latencyscore.getString(microbox_site);
								}*/







		 //   document.put(microbox_site,    record_values[i].isEmpty() ? "0" : record_values[i]);
		 //  System.out.println(microbox_site+","+document.get(microbox_site));



		 //			                    System.out.println(document.get(microbox_site));
		 //	i=i+1;



		 // mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency).insertOne(document);





		 //}
		 /*			        System.out.println("Printing count_oneday_latency_SmartX_Box and latency count at each site" );
			        System.out.println("SmartX_Box_GIST1:"+"-"+ " " +Box_PH_latency+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_GIST1=count_oneday_latency_SmartX_Box;


			        //float uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST1= (Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {



			        uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH=round(((float) Box_PH_latency/count_oneday_latency_SmartX_Box)*100,2);

			        //uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH=(Float.isNaN(uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH)) ? 0 : uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH;


			        //uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST= ((float) Box_HUST_latency/(count_oneday_latency_SmartX_Box)*100);
			        uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST=round(((float) Box_HUST_latency/count_oneday_latency_SmartX_Box)*100,2);
			        uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA=round(((float) Box_CHULA_latency/count_oneday_latency_SmartX_Box)*100,2);
			        uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN=round(((float) Box_MYREN_latency/count_oneday_latency_SmartX_Box)*100,2);
			        uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3=round(((float) Box_GIST3_latency/count_oneday_latency_SmartX_Box)*100,2);
			        //uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST0=round(((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100,2);
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println("-"+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH+ " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3 + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST0);
			        }			        
			        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");

			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST1");
			        document_latencyscore.put("SmartX-Box-GIST1",   "-");
			        document_latencyscore.put("SmartX-Box-PH",  uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH);
			        document_latencyscore.put("SmartX-Box-HUST",  uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST);
			        document_latencyscore.put("SmartX-Box-CHULA",  uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA);
			        document_latencyscore.put("SmartX-Box-MYREN",  uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN);
			        document_latencyscore.put("SmartX-Box-GIST3",  uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3);

					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					System.out.println(document_latencyscore.getString("timestamp")+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+document_latencyscore.getDouble("SmartX_Box_GIST1")+" "+document_latencyscore.getDouble("SmartX_Box_PH")+" "+document_latencyscore.getDouble("SmartX_Box_HUST")+" "+document_latencyscore.getDouble("SmartX_Box_CHULA")+" "+document_latencyscore.getDouble("SmartX_Box_MYREN")+" "+document_latencyscore.getDouble("SmartX_Box_GIST3"));


				    out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+"-"+ " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_PH+"%"+ " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_HUST+"%" + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_CHULA+"%" + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_MYREN+"%" + " "+uptime_oneday_latency_SmartX_Box_GIST1_dest_Box_GIST3+"%");
						//more code

						//more code


					document_latencyscore.clear();
			        reset_values();
			        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        ///////////////////////////////////////////            SmartX-Box-PH              /////////////////////////////////////////
			        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-PH"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_latency(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_latency_SmartX_Box_PH and latency count at each site" );
			        System.out.println("SmartX_Box_PH:"+ " " +Box_GIST1_latency+" "+"-"+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_PH=count_oneday_latency_SmartX_Box;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {



			        uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST1=round((((float) Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //float uptime_oneday_latency_SmartX_Box_PH_dest_Box_PH= (Box_PH_latency/count_oneday_latency_SmartX_Box)*100;
			        uptime_oneday_latency_SmartX_Box_PH_dest_Box_HUST=round((((float) Box_HUST_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_PH_dest_Box_CHULA=round((((float) Box_CHULA_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_PH_dest_Box_MYREN=round((((float) Box_MYREN_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST3=round((((float) Box_GIST3_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST0=round((((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST1+ " "+"-"+uptime_oneday_latency_SmartX_Box_PH_dest_Box_HUST + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_CHULA + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_MYREN + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST3 + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST0);
			        }
			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_PH");
			        document_latencyscore.put("SmartX-Box-GIST1",   uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST1);
			        document_latencyscore.put("SmartX-Box-PH",  "-");
			        document_latencyscore.put("SmartX-Box-HUST",  uptime_oneday_latency_SmartX_Box_PH_dest_Box_HUST);
			        document_latencyscore.put("SmartX-Box-CHULA",  uptime_oneday_latency_SmartX_Box_PH_dest_Box_CHULA);
			        document_latencyscore.put("SmartX-Box-MYREN",  uptime_oneday_latency_SmartX_Box_PH_dest_Box_MYREN);
			        document_latencyscore.put("SmartX-Box-GIST3",  uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST3);

					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST1+"%"+ " "+"-"+ " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_HUST+"%" + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_CHULA+"%" + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_MYREN+"%" + " "+uptime_oneday_latency_SmartX_Box_PH_dest_Box_GIST3+"%");
					document_latencyscore.clear();
			        reset_values();

					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-HUST              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-HUST"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_latency(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_latency_SmartX_Box_HUST and latency count at each site" );
			        System.out.println("SmartX_Box_HUST:"+ " " +Box_GIST1_latency+" "+" "+Box_PH_latency+" -"+" "+Box_CHULA_latency+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_HUST=count_oneday_latency_SmartX_Box;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {

			        uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1=round((((float) Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1; 

			        uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH=round((((float) Box_PH_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH;
			        //float uptime_oneday_latency_SmartX_Box_HUST_dest_Box_HUST= Box_HUST_latency/(count_oneday_latency_SmartX_Box)*100;

			        uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA=round((((float) Box_CHULA_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA;

			        uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN=round((((float) Box_MYREN_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN;

			        uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3=round((((float) Box_GIST3_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3;

			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0=round((((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0= Float.isNaN(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0) ? 0: uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0;
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1+ " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH + " "+"-"+ " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA + " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN + " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3 + " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST0);
			        }
			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_HUST");
			        document_latencyscore.put("SmartX-Box-GIST1",   uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1);
			        document_latencyscore.put("SmartX-Box-PH",  uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH);
			        document_latencyscore.put("SmartX-Box-HUST",  "-");
			        document_latencyscore.put("SmartX-Box-CHULA",  uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA);
			        document_latencyscore.put("SmartX-Box-MYREN",  uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN);
			        document_latencyscore.put("SmartX-Box-GIST3",  uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3);

					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST1+"%"+ " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_PH+"%" + " "+"-"+ " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_CHULA+"%" + " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_MYREN+"%" + " "+uptime_oneday_latency_SmartX_Box_HUST_dest_Box_GIST3+"%");
					document_latencyscore.clear();
			        reset_values();

					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-CHULA              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-CHULA"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_latency(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_latency_SmartX_Box_CHULA and latency count at each site" );
			        System.out.println("SmartX_Box_CHULA:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+" "+"-"+" "+Box_MYREN_latency+" "+Box_GIST3_latency+ " " +Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_CHULA=count_oneday_latency_SmartX_Box;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {


			       	uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1=round((((float) Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_PH=round((((float) Box_PH_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST=round((((float) Box_HUST_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //float uptime_oneday_latency_SmartX_BoxCHULA_dest_Box_CHULA=(float) Box_CHULA_latency/(count_oneday_latency_SmartX_Box)*100;
			        uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN=round((((float) Box_MYREN_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3=round((((float) Box_GIST3_latency/count_oneday_latency_SmartX_Box)*100),2);
			       // uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST0=round((((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1+ " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_PH +" "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST+ " "+"-"+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN + " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3 + " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST0);
			        }
			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_CHULA");
			        document_latencyscore.put("SmartX-Box-GIST1",   uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1);
			        document_latencyscore.put("SmartX-Box-PH",  uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_PH);
			        document_latencyscore.put("SmartX-Box-HUST",  uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST);
			        document_latencyscore.put("SmartX-Box-CHULA",  "-");
			        document_latencyscore.put("SmartX-Box-MYREN",  uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN);
			        document_latencyscore.put("SmartX-Box-GIST3",  uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3);

					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST1+"%"+ " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_PH+"%" +" "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_HUST+"%"+ " "+"-"+ " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_MYREN+"%" + " "+uptime_oneday_latency_SmartX_Box_CHULA_dest_Box_GIST3+"%");
					document_latencyscore.clear();
			        reset_values();

					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-MYREN              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-MYREN"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_latency(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_latency_SmartX_Box_MYREN and latency count at each site" );
			        System.out.println("SmartX_Box_MYREN:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+"-"+Box_GIST3_latency+ " " +Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_MYREN=count_oneday_latency_SmartX_Box;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {


			       	uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1=round((((float) Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_PH=round((((float) Box_PH_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST= round((((float) Box_HUST_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA=round((((float) Box_CHULA_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //float uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_MYREN=(float) Box_MYREN_latency/(count_oneday_latency_SmartX_Box)*100;
			        uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3=round((((float) Box_GIST3_latency/count_oneday_latency_SmartX_Box)*100),2);
			       // uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST0=round((((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1+ " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_PH + " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST + " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA+" "+"-"+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3 + " "+"-"+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST0);
			        }
			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_MYREN");
			        document_latencyscore.put("SmartX-Box-GIST1",   uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1);
			        document_latencyscore.put("SmartX-Box-PH",  uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_PH);
			        document_latencyscore.put("SmartX-Box-HUST",  uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST);
			        document_latencyscore.put("SmartX-Box-CHULA",  uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA);
			        document_latencyscore.put("SmartX-Box-MYREN",  "-");
			        document_latencyscore.put("SmartX-Box-GIST3",  uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3);

					mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST1+"%"+ " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_PH+"%" + " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_HUST+"%" + " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_CHULA+"%"+" "+"-"+ " "+uptime_oneday_latency_SmartX_Box_MYREN_dest_Box_GIST3+"%");
					document_latencyscore.clear();
			        reset_values();

					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					///////////////////////////////////////////            SmartX-Box-GIST3              /////////////////////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			        for (Document document : documents_OneDay) 
					{
			        	if (document.getString("SmartX-Box-SOURCE").contains("SmartX-Box-GIST3"))
			        	{
			        		//System.out.println(document.getString("SmartX-Box-SOURCE"));
			        		calculate_latency(document);
			        	}
					}
			        System.out.println("");
			        System.out.println("Printing count_oneday_latency_SmartX_Box_GIST3 and latency count at each site" );
			        System.out.println("SmartX_Box_GIST3:"+ " " +Box_GIST1_latency+" "+Box_PH_latency+" "+Box_HUST_latency+" "+Box_CHULA_latency+" "+Box_MYREN_latency+ " "+"- "+Box_GIST0_latency);
			        System.out.println("Total Count: "+ count_oneday_latency_SmartX_Box);
			        count_oneday_latency_SmartX_Box_GIST3=count_oneday_latency_SmartX_Box;
			        if(count_oneday_latency_SmartX_Box>0) 
			        {



			       	uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1=round((((float) Box_GIST1_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_PH=round((((float) Box_PH_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST= round((((float) Box_HUST_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA=round((((float) Box_CHULA_latency/count_oneday_latency_SmartX_Box)*100),2);
			        uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN=round((((float) Box_MYREN_latency/count_oneday_latency_SmartX_Box)*100),2);
			        //float uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST3=(float) Box_GIST3_latency/(count_oneday_latency_SmartX_Box)*100;
			       // uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST0=round((((float) Box_GIST0_latency/count_oneday_latency_SmartX_Box)*100),2);
			        System.out.println("Printing count_oneday_latency_SmartX_Box, uptime, Downtime" );
			        System.out.println(uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1+ " "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_PH + " "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST +" "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA+ " "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN + " "+"-");
			        }

			        document_latencyscore.put("timestamp",   sdf.formatsimpleDateFormat.format(timestamp));
			        document_latencyscore.put("SmartX-Box-SOURCE",   "SmartX_Box_GIST3");
			        document_latencyscore.put("SmartX-Box-GIST1",   uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1);
			        document_latencyscore.put("SmartX-Box-PH",      uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_PH);
			        document_latencyscore.put("SmartX-Box-HUST",    uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST);
			        document_latencyscore.put("SmartX-Box-CHULA",   uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA);
			        document_latencyscore.put("SmartX-Box-MYREN",   uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN);
			        document_latencyscore.put("SmartX-Box-GIST3",    "-");

					try
					{
						mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_aggregate).insertOne(document_latencyscore);
					out.println(simpleDateFormatNoTime.format(timestamp)+" "+document_latencyscore.getString("SmartX-Box-SOURCE")+" "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_GIST1+"%"+" "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_PH+"%" + " "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_HUST+"%" +" "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_CHULA+"%"+ " "+uptime_oneday_latency_SmartX_Box_GIST3_dest_Box_MYREN+"%" + " "+"-");

					}
					catch (Exception e) {
						//exception handling left as an exercise for.the reader
					}


					document_latencyscore.clear();
			        reset_values();

					try(FileWriter fw_collection = new FileWriter("/home/netcs/active_monitoring/Daily-report-latency-collection/"+latency_collection_day, true);

							BufferedWriter bw_collection = new BufferedWriter(fw_collection);

							PrintWriter out_collection = new PrintWriter(bw_collection))
					{
						File file_collection = new File("/home/netcs/active_monitoring/Daily-report-latency-collection/"+latency_collection_day);
						if (file_collection.length() == 0)
				        {
							out_collection.println("Date"+" "+"Expected_Collection"+" "+"SmartX-Box-GIST1"+" "+"SmartX-Box-PH"+" "+"SmartX-Box-HUST"+" "+"SmartX-Box-CHULA"+" "+"SmartX-Box-MYREN"+" "+"SmartX-Box-GIST3");	
				        }

						System.out.println(simpleDateFormatNoTime.format(timestamp)+" "+"144"+" "+count_oneday_latency_SmartX_Box_GIST1+" "+count_oneday_latency_SmartX_Box_PH+" "+count_oneday_latency_SmartX_Box_HUST+" "+count_oneday_latency_SmartX_Box_CHULA+" "+count_oneday_latency_SmartX_Box_MYREN+" "+count_oneday_latency_SmartX_Box_GIST3);
						out_collection.println(simpleDateFormatNoTime.format(timestamp)+" "+"144"+" "+count_oneday_latency_SmartX_Box_GIST1+" "+count_oneday_latency_SmartX_Box_PH+" "+count_oneday_latency_SmartX_Box_HUST+" "+count_oneday_latency_SmartX_Box_CHULA+" "+count_oneday_latency_SmartX_Box_MYREN+" "+count_oneday_latency_SmartX_Box_GIST3);
						document_collection = new Document();

						document_collection.put("timestamp",  simpleDateFormatNoTime.format(timestamp)"2018/11/28");
						document_collection.put("Expected Collection",   "144");
						document_collection.put("SmartX-Box-GIST1",   count_oneday_latency_SmartX_Box_GIST1);
						document_collection.put("SmartX-Box-PH",      count_oneday_latency_SmartX_Box_PH);
						document_collection.put("SmartX-Box-HUST",    count_oneday_latency_SmartX_Box_HUST);
						document_collection.put("SmartX-Box-CHULA",   count_oneday_latency_SmartX_Box_CHULA);
						document_collection.put("SmartX-Box-MYREN",   count_oneday_latency_SmartX_Box_MYREN);
						document_collection.put("SmartX-Box-GIST3",    count_oneday_latency_SmartX_Box_GIST3);
						mongoConnector.getDbConnection().getCollection(topologyMongoCollection_latency_collection).insertOne(document_collection);
					}
					 catch (IOException e) {
							//exception handling left as an exercise for the reader
						}


					document_latencyscore.clear();
			        reset_values();

		  */

		 List<Document> insertList = new ArrayList<Document>();

		 Date date11 = new Date();

		 //collection_process.insertMany(documents);


		 // MongoCursor<Document> doc = collection_process.find(new Document("date", date)).iterator();
		 //System.out.println(doc.next().getDate("date"));

		 /*for (Document document1 : findCursor) 
					{


							System.out.println("Printing last 2 hours data only using cursor");
							System.out.println(document1);
							//System.out.println(document1.getString("timestamp"));
						}*/   

		 /*for (Document document : documents) 
					{
			        	System.out.println("Printing one day (today) latency data only");
			        	System.out.println(document);
					}*/
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

	private int getNumberofSites() throws IOException {
		// TODO Auto-generated method stub
		Map<String, Float> uptime_oneday_latency_microbox_hmap = new HashMap<String, Float>();
		String write_to_file = "";
		System.out.printf("-- loading from %s --%n", path);
		Yaml yaml = new Yaml();
		int siteCount = 0;
		try (InputStream in = latency_DailyreportConsumer.class.getResourceAsStream(path)) {
			Iterable<Object> itr = yaml.loadAll(in);

			System.out.println("-- iterating loaded Iterable --");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();





			for (Object o : itr) 															//We calcualcuate the total number of latencys collected from each microbox to other microboxes
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

	private void reset_values() {
		Box_GIST1_latency=0;Box_PH_latency=0;Box_HUST_latency=0;Box_CHULA_latency=0;Box_MYREN_latency=0;Box_GIST3_latency=0; Box_GIST0_latency=0; latency_record_total_today=0; 
		count_oneday_latency_SmartX_Box=0; /*count_oneday_latency_SmartX_Box_PH=0; count_oneday_latency_SmartX_Box_HUST=0; count_oneday_latency_SmartX_Box_CHULA=0; 
				count_oneday_latency_SmartX_Box_MYREN=0; count_oneday_latency_SmartX_Box_GIST3=0; count_oneday_latency_SmartX_Box_GIST_0=0*/;

	}

	private void calculate_latency(Document document, String microbox_site_source) throws IOException {


		count_oneday_latency_SmartX_Box+=1;
		count_oneday_singlesite_latency_SmartX_Box+=1;
		System.out.println();
		System.out.printf("-- calculate_latency ,loading from %s --%n", path);
		Yaml yaml = new Yaml();
		try (InputStream in = latency_DailyreportConsumer.class.getResourceAsStream(path)) {
			Iterable<Object> itr = yaml.loadAll(in);
			System.out.println("-- iterating loaded Iterable --");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzz");
			timestamp = new Date();
			int i=0,count_site_vertical=1;
			document.put("timestamp",   sdf.format(timestamp));

			//document.put("microbox-SOURCE",   record_values[2]);
			System.out.println("***********Source:"+microbox_site_source+"***********");
			anStringArray[count_site][0]=microbox_site_source;
			System.out.println("Source_site="+anStringArray[count_site][0]);
			for (Object o : itr) {
				String microbox_site = ((String) o).substring(0, ((String) o).indexOf('='));
				// document.put(microbox_site,    record_values[i].isEmpty() ? "0" : record_values[i]);


				if (document.getString(microbox_site).contains("-")|| microbox_latency_hmap.get(microbox_site)=="-")
				{
					microbox_latency_hmap.put(microbox_site,"-");
					anStringArray[count_site][count_site_vertical]= "-";
				}
				else if (!microbox_latency_hmap.containsKey(microbox_site) || microbox_latency_hmap.get(microbox_site)==null)//Counting for the first time.
				{
					microbox_latency_hmap.put(microbox_site,document.getString(microbox_site));
					anStringArray[count_site][count_site_vertical]= document.getString(microbox_site);
				}
				else if (document.getString(microbox_site)==null)
				{
					if (!microbox_latency_hmap.containsKey(microbox_site) || microbox_latency_hmap.get(microbox_site)==null)//Counting for the first time.
					{
						microbox_latency_hmap.put(microbox_site,"0");
						anStringArray[count_site][count_site_vertical]= "0";
					}

					else
					{
						microbox_latency_hmap.put(microbox_site,microbox_latency_hmap.get(microbox_site));
						anStringArray[count_site][count_site_vertical]= microbox_latency_hmap.get(microbox_site).toString();
					}
				}		
				else if (microbox_latency_hmap.get(microbox_site)!="-")
				{
					System.out.println("Float.parseFloat(document.getString(microbox_site).toString():"+Float.parseFloat(document.getString(microbox_site).toString()));
					System.out.println(microbox_latency_hmap.get(microbox_site));
					System.out.println(Float.parseFloat(document.getString(microbox_site).toString()));
					float f=  Float.parseFloat(microbox_latency_hmap.get(microbox_site))+ Float.parseFloat(document.getString(microbox_site).toString());

					microbox_latency_hmap.put(microbox_site,Float.toString(f));
					anStringArray[count_site][count_site_vertical]= microbox_latency_hmap.get(microbox_site).toString();
				}








				/*if (!document.getString(microbox_site).contains(microbox_site))	
				{
					anStringArray[count_site][0]=microbox_site_source;
					anStringArray[count_site][count_site_vertical]= "0";
				}*/



				System.out.println(microbox_site+"="+microbox_latency_hmap.get(microbox_site));

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
