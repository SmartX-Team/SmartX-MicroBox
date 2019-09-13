package smartx.multiview.collectors.microbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import smartx.multiview.DataLake.MongoDB_Connector;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Snap_influxdb2 implements Runnable {
	private String bootstrapServer;
	private MongoDB_Connector mongoConnector;
	private Thread thread;
	private String ThreadName = "Intel SNAP Daily Collection report Thread";
	private int snap_value = 0;
	private static final String regex = "^\\d{1,10}$";
	private Date timestamp;
	private Date timestamp_tomorrow;
	private String[] elements = { "SmartX-Box-GIST1","SmartX-Box-PH", "SmartX-Box-HUST","SmartX-Box-MYREN","SmartX-Box-CHULA","SmartX-Box-GIST3" };
	private String[] sites_snap = new String[6];
	private int i=0;

	public Snap_influxdb2(String bootstrapserver, MongoDB_Connector MongoConn) {

		bootstrapServer = bootstrapserver;
		mongoConnector = MongoConn;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, ThreadName);
			thread.start();
		}

	}

	/*
	 * try (BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(url.openStream(), "UTF-8"))) { for (String line; (line =
	 * reader.readLine()) != null;) { System.out.println(line); } }
	 * 
	 * curl -G 'http://103.22.221.56:8086/query?pretty=true' --data-urlencode
	 * "db=snap_pbox_visibility" --data-urlencode "chunk_size=20000"
	 * --data-urlencode
	 * "q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\" WHERE \"BoxID\"='SmartX-Box-GIST1' AND \"time\">='2018-05-16' AND \"time\"<='2018-05-17'"
	 */

	/*
	 * public static String callURL(URL url2) { System.out.println("Requeted URL:" +
	 * url2); StringBuilder sb = new StringBuilder(); URLConnection urlConn = null;
	 * InputStreamReader in = null; try { URL url = new URL(url2.toString());
	 * urlConn = url.openConnection(); if (urlConn != null)
	 * urlConn.setReadTimeout(60 * 1000); if (urlConn != null &&
	 * urlConn.getInputStream() != null) { in = new
	 * InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
	 * BufferedReader bufferedReader = new BufferedReader(in); if (bufferedReader !=
	 * null) { int cp; while ((cp = bufferedReader.read()) != -1) { sb.append((char)
	 * cp); } bufferedReader.close(); } } in.close(); } catch (Exception e) { throw
	 * new RuntimeException("Exception while calling URL:"+ url2, e); }
	 * 
	 * return sb.toString(); }
	 */
	@Override
	public void run() {
		this.influxdb_fetch();
	}

	private void influxdb_fetch() {
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
		timestamp = new Date();
		timestamp_tomorrow = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(timestamp); 
		c.add(Calendar.DATE, 1);
		timestamp_tomorrow = c.getTime();
		
		String date_today =sdf.format(timestamp);
		String date_tomorrow =sdf.format(timestamp_tomorrow);
		// TODO Auto-generated method stub
		/*
		 * URL url; try { url = new
		 * URL("'http://103.22.221.56:8086/query?pretty=true' --data-urlencode \"db=snap_pbox_visibility\" --data-urlencode \"chunk_size=20000\" --data-urlencode \"q=SELECT count(\\\"value\\\") FROM \\\"/intel/linux/iostat/avg-cpu/%idle\\\" WHERE \\\"BoxID\\\"='SmartX-Box-GIST1' AND \\\"time\\\">='2018-05-16' AND \\\"time\\\"<='2018-05-17'\""
		 * ); System.out.println("\nOutput: \n" + callURL(url)); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
        i=0;
		for (String s: elements) {           
		
		// Snap_influxdb obj = new Snap_influxdb();
		String[] command = new String[] { "curl", "-G", "http://103.22.221.56:8086/query?pretty=true",
				"--data-urlencode", "db=snap_pbox_visibility", "--data-urlencode", "chunk_size=20000",
				// "--data-urlencode","q=SELECT count(\\\"value\\\") FROM
				// \\\"/intel/linux/iostat/avg-cpu/%idle\\\"WHERE
				// \\\"BoxID\\\"='SmartX-Box-GIST1' AND \\\"time\\\">='2018-05-16' AND
				// \\\"time\\\"<='2018-05-17'",
				//"--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"='SmartX-Box-GIST1' AND \"time\">=   AND \"time\"<='2018-05-17'", };
				//"--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"='SmartX-Box-GIST1' AND \"time\">="+"'2018-09-01'"+"AND \"time\"<="+"'2018-09-02'",};
		        
				
				//"--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"='SmartX-Box-GIST1' AND \"time\">="+"'"+date_today+"'"+"AND \"time\"<=" +"'"+date_tomorrow+"'"+"",};
		        "--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"="+"'"+s+"'"+"AND \"time\">="+"'"+date_today+"'"+"AND \"time\"<=" +"'"+date_tomorrow+"'"+"",};
				
		        
		        
		        
		        //"--data-urlencode","q=SELECT count(\"value\") FROM \"/intel/linux/iostat/avg-cpu/%idle\"WHERE \"BoxID\"='SmartX-Box-GIST1' AND \"time\">=\"date_today\" AND \"time\"<=\"date_today\"" };
		System.out.print("Site: ");
		System.out.println(s);
		System.out.print("Date Today and tomorrow: ");
		System.out.print(date_today+" ");
		System.out.println(date_tomorrow);
		
		String output = executeCommand(command);
		i+=1;

		// System.out.println("Output of CURL Command");
		// System.out.println(output);
		/*System.out.println("Value from CURL Command");
		System.out.println(snap_value);*/

	} 
		i=0;
		System.out.println("Printing the SNAP result Site: ");
		for (String s: elements) {  
			System.out.print(s+":");
			System.out.println(sites_snap[i]);
			i+=1;
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
				//output.append(line + "\n");
				// method 1
				boolean isInteger = isInteger(line);
				if (isInteger) {
					System.out.println(line + " is an integer method 1");
					sites_snap[i]=line;
					
				}

				/*// System.out.println(stringContainsNumber(line));
				boolean int_found = stringContainsNumber(line);
				System.out.println("1- Value from CURL is integer");
				System.out.println(int_found);
				
				String val= line;
				System.out.println(val.replaceAll("\\s+",""));
				boolean find1= Pattern.compile("[0-9]+").matcher(line.replaceAll("\\s+","")).find();
				boolean find2= Pattern.compile("\\\\d+").matcher(line.replaceAll("\\s+","")).find();
				System.out.println("2- Value from CURL is integer");
				System.out.println(find1);
				System.out.println(find2);
				
				
				

				// method 8
				try {
					Date d = dateFormat.parse(line.replaceAll("\\s+",""));
					System.out.println(line + " is Date using method 8");
					// string contains valid date
				}
				catch (ParseException ex)
				{
					// string contains invalid date
				}
				// method 7
				if (line.matches("^[0-9]*$") && line.length() > 2) {
					System.out.println(line + " is an integer using method 7");
				}
				// method 1
				boolean isInteger = isInteger(line);
				if (isInteger) {
					System.out.println(line + " is an integer method 1");
				}

				// method 2
				int a;
				try {
					a = Integer.parseInt(line); // use your variable or object in place of obj
					System.out.println(a + " is a integer number using method 2");
				} catch (NumberFormatException e) {
				}

				// method 3
				Pattern p1 = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
				Matcher m = p1.matcher(line);
				if (m.matches()) {
					System.out.println(line + " is an integer using method 3");
				}

				// method 4
				if (line.matches("[0-9]+") && line.length() > 2) {
					System.out.println(line + " is an integer using method 4");
				}

				// method 5
				System.out.println("Check for integer using method 5");
				System.out.println(line.matches(regex));

				// method 6
				System.out.println("Check for integer using method 6");
				System.out.println(containsOnlyNumbers(line));

				

				
*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	public boolean stringContainsNumber(String s) {
		return Pattern.compile("[0-9]").matcher(s).find();
	}

	public static boolean containsOnlyNumbers(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
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
