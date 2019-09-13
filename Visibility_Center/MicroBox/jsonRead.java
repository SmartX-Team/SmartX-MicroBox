package smartx.multiview.collectors.microbox;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class jsonRead {

	public static void main(String[] args) {
	JSONParser parser = new JSONParser();
	//JsonParser to convert JSON string into Json Object
	
	try {
		Object obj = parser.parse(new FileReader("newfile.json"));
		//parsing the JSON string inside the file that we created earlier.

		JSONObject jsonObject = (JSONObject) obj;
		System.out.println(jsonObject);
		//Json string has been converted into JSONObject

		String name = (String) jsonObject.get("Date");
		System.out.println(name);

		String department = (String) jsonObject.get("HOUR_OF_DAY");
		System.out.println(department);

		String branch = (String) jsonObject.get("MINUTE");
		System.out.println(branch);

		long year = (long) jsonObject.get("year");
		System.out.println(year);
		//Displaying values from JSON OBject by using Keys

		JSONArray remarks = (JSONArray) jsonObject.get("remarks");
		//converting the JSONObject into JSONArray as remark was an array.
		Iterator<String> iterator = remarks.iterator();
		//Iterator is used to access the each element in the list 
		//loop will continue as long as there are elements in the array.
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			//accessing each elemnt by using next function.
		}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
}
}
