/**
 * @author Muhammad Usman
 * @version 0.1
 */
 
package smartx.multiview.DataLake;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.carrotsearch.hppc.LongLongAssociativeContainer;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHits;

public class Elasticsearch_Connector {
	//private Client client;
	TransportClient client;
    private long index;
    
	@SuppressWarnings("unchecked")
	public void setClient(String dbHost, int dbPort)
	{
		//Elasticsearch Properties
		
	    //Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch").build();
	   //# Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
	    try {
	    	
	    	client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new  TransportAddress(InetAddress.getByName(dbHost), dbPort));
	   // 	System.out.println(client.connectedNodes());
	  //#  	client = new PreBuiltTransportClient(settings);
			//client = TransportClient.builder().build().addTransportAddress(new  InetSocketTransportAddress(InetAddress.getByName(dbHost), dbPort));
	    } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
	}
	
	public boolean checkIndexExist(String indexName)
	{
		//Check Elasticsearch Index Already Exists or not
	    boolean indexStatus = client.admin().indices().prepareExists(indexName).execute().actionGet().isExists();
	    //if (indexStatus)
	    //{
	    //	client.admin().indices().prepareDelete(indexName).execute().actionGet().actionGet();
	    //}
	    return indexStatus;
	}
	
    
    public long createIndex(String indexName)
    {
    	//Create Elasticsearch Index
    	if (!checkIndexExist(indexName))
    	{
    		System.out.println("Index Does not Exist");
    		CreateIndexResponse response = client.admin().indices().prepareCreate(indexName).execute().actionGet();
        }
    	
    	SearchHits resp = client.prepareSearch(indexName).get().getHits();
    	index = resp.getTotalHits();
    	System.out.println("Total Records in Index: "+index);
//    	SearchResponse response1 = client.prepareSearch(indexName).setQuery(termQuery("_type", "mirror")).get().get;// node.client.prepareCount(indexName).setQuery(termQuery("_type", "mirror")).execute().actionGet();
//    	index = response1.getAggregations().get("index").
    	//CountResponse response1 = client.prepareCount(indexName).setQuery(termQuery("_type", "mirror")).execute().actionGet();
        //index=response1.getCount();
		return index;
    }
    
    public void insertData(String indexName, Date timestamp, String flowKey, String VLANID, String tenant, String TLProtocol, String agentBox, float dataBytes, float frameSize)
    {
    	try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", timestamp)
    				.field("AgentID", agentBox)
    				.field("VLANID", VLANID)
    				.field("Tenant", tenant)
    				.field("TransportProtocol", TLProtocol)
    				.field("flowKey", flowKey)
    				.field("Bytes", dataBytes)
    				.field("FrameSize", frameSize)
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void insertData1(String indexName, Date timestamp,String dsnames, String type,  String host, String plugin_instance, String type_instance, String plugin, String values)
    {
    	System.out.println("##############################################");
    	System.out.println("indexName:"+indexName+" timestamp:"+timestamp+" host:"+host+" plugin_instance:"+plugin_instance+" plugin:"+plugin);
    	values=values.replaceAll("\\[", "").replaceAll("\\]","").replace("null", "0");
    	String rx="";
    	String tx="";
    	if (plugin.contains("interface"))
	    {
    		if (values.contains(","))
        	{
        		String input =values;
            	String[] parts = input.split(",");
            	rx=parts[1];
            	tx=parts[2];
        	}
    		
    		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "interface")
	    				.field("type_instance", "if_octets") //Count of bytes (octets) received on the interface
	    				.field("Count_bytes(octets)_received", Float.parseFloat(rx))
	    				.field("Count_bytes(octets)_transmitted", Float.parseFloat(tx))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    }
    		catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
    	
    	
    	
    	if (values.contains(","))
    	{
    		String input =values;
        	String[] parts = input.split(",");
        	values=parts[1];
    	}
    	
    	if (values == null | values == "null" | values.length() == 0) 
    	{
    		values="0";
    		System.out.println("******************Null FOUND Values*********************: "+values);
    	}
    	
    	 	System.out.println("******************NULL NOT FOUND Values*********************: "+values);
    	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	/*if (dsnames.contains("io_time"))
    	{
    		dsnames="io_time";
    		
    	}*/
    	
    	//DateTime d = DateTime(l);
    	//load average figures giving the number of jobs in the run queue (state R) or waiting for disk I/O (state D) averaged over 1 Minute
    	
    	if (plugin.contains("load"))
	    {
    		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "load")
	    				.field("type_instance", "shortterm")
	    				.field("values(No of jobs)", Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    }
    		catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    //	memory	free	The amount of physical RAM, in kibibytes, left unused by the system.
	 	//memory	used	mem_used = mem_total - (mem_free + mem_buffered + mem_cached + mem_slab_total);

    	else if (plugin.contains("memory")  && (type_instance.contains("free")) )
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "free")
	    				.field("values(KB)",Long.parseLong(values) /*Integer.valueOf	(values)*/)
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}	
	    }
    	else  if (plugin.contains("memory")  && (type_instance.contains("buffered")) )
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "buffered")
	    				.field("values(KB)", Integer.parseInt(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}	
	    }
    	else if (plugin.contains("memory")  && (type_instance.contains("slab_recl")) )
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "slab_recl")
	    				.field("values(KB)",Long.parseLong(values)/* Integer.parseInt(values)*/)
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}	
	    }
    	else if (plugin.contains("memory")  && (type_instance.contains("cached")) )
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "cached")
	    				.field("values(KB)", Long.parseLong(values)/*Integer.parseInt(values)*/)
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}	
	    }
    	else if (plugin.contains("memory")  && type_instance.contains("used")) 
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "used")
	    				.field("values(KB)", ((isLong(values.trim())==true) ? Long.parseLong(values): Integer.parseInt(values.trim())  ))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    		System.out.println("values_Exception"+values);
	    	}	
	    }
    	else  if (plugin.contains("memory")  && (type_instance.contains("total")) )
	    {
	 		try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "memory")
	    				.field("type_instance", "total")
	    				.field("values(KB)", Long.parseLong(values) /*Integer.parseInt(values)*/)
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}	
	    }
	    //CPU (A read plugin that retrieves CPU usage in Nanoseconds of as a percentage)percent/nanoseconds	idle	Time CPU spends idle. percent/nanoseconds
	    //user	Time CPU spends running un-niced user space processes.
	    else if (plugin.contains("cpu") && (type_instance).toString().contains("idle"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "idle")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("user"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "user")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    			
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("nice"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "nice")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("interrupt"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "interrupt")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("softirq"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "softriq")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("steal"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "steal")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    else if (plugin.contains("cpu") && type_instance.toString().contains("system"))
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "cpu")
	    				.field("type_instance", "system")
	    				.field("values(%)", values.isEmpty() ? "0" : Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    //disk_io_time	io_time	time spent doing I/Os (ms). You can treat this metric as a device load percentage (Value of 1 sec time spent matches 100% of load).
	    else if (type.contains("disk_io_time") )
	    {
	    	try {
	    		XContentBuilder builder = jsonBuilder()
	    			.startObject()
	    				.field("@version", "1")
	    				.field("@timestamp", /*formatter.format*/(timestamp))
	    				.field("boxid", host)
	    				.field("plugin", "disk")
	    				.field("type_instance", "disk_io_time")
	    				.field("values(ms)", Float.parseFloat(values))
	    				
	    					    			.endObject();
	    		index=index+1;
	    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }

    	/*try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", formatter.format(timestamp))
    				.field("boxid", host)
    				.field("dsnames", dsnames.replaceAll("[] ", ""))
    				.field("type", type)
    				.field("plugin", plugin)
    				.field("plugin_instance", plugin_instance)
    				.field("type_instance", type_instance)
    				.field("values", values)
    				.field("flowKey", flowKey)
    				.field("Bytes", dataBytes)
    				.field("FrameSize", frameSize)
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}*/
    }

	public void insertPingLatencyDataAggregate(String indexName, Date date, String source_site, String dest_site, float value) {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", source_site)
    				.field("dest_site", dest_site)
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public void checkCon(String indexName) throws IOException {
		
	}
	
	public void insertPingCollection(String indexName, Date date, String site,Integer value) {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", site)
    				
    				
    				
    				
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}

	public void insertPingLatencyDataRaw(String indexName, Date date, String source_site, String dest_site, float value)  throws IOException {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", source_site)
    				.field("dest_site", dest_site)
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public void insertPingcollection(String indexName, Date date, String site, String collection_value) {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", site)
    				.field("value", collection_value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
		
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public void insertLivelinessVcenter(String indexName, Date date, String site, String value) {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", site)
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
	catch (IOException e) {
		e.printStackTrace();
	}
}
	
	public void insertVcenterDataRaw(String indexName, Date date, String site, Integer value) throws IOException {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", site)
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    		
    	}
	catch (IOException e) {
		e.printStackTrace();
	}
}
	
	public void insertVcenterDataAggregate(String indexName, Date date, String site, float value) {
		// TODO Auto-generated method stub
		try {
    		XContentBuilder builder = jsonBuilder()
    			.startObject()
    				.field("@version", "1")
    				.field("@timestamp", date)
    				.field("source_site", site)
    				.field("value", value)
    				
    			.endObject();
    		index=index+1;
    		client.prepareIndex(indexName, "mirror", index+"").setSource(builder).execute().actionGet();
    	}
	catch (IOException e) {
		e.printStackTrace();
	}
}
	
	public static boolean isLong(String strNum) {
	    try {
	        long d = Long.parseLong(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}

	/*public void insertData_(String eSindex, String string, String string2, String string3, String string4,
			String string5) {
		// TODO Auto-generated method stub
		
	}*/

	

	

	
}
