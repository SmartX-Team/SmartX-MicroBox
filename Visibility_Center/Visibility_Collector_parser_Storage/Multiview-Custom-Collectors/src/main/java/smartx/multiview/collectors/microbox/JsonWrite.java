package smartx.multiview.collectors.microbox;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject obj = new JSONObject();
		
		//JSONObject class creates a json object,
		//provides a put function to insert the details into json object
		obj.put("Date","1");
		obj.put("HOUR_OF_DAY","1");
		obj.put("MINUTE","0");
		obj.put("year", 3);

		JSONArray list = new JSONArray();
		
		//This is a JSON Array List , 
		//It creates an array and then add the values in it  
		list.add("remark 1");
		list.add("remark 2");
		list.add("remark 3");

		obj.put("remarks", list);//adding the list to our JSON Object

		try (FileWriter file = new FileWriter("newfile.json")) {
			//File Writer creates a file in write mode at the given location 
			file.write(obj.toJSONString());

			//write function is use to write in file,
			//here we write the Json object in the file
			file.flush();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(obj);
		//to print our JSon object
	}

}
