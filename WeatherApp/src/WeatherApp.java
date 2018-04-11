import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.PrintWriter;
import java.io.File;


public class WeatherApp {
	 	 
	public static void main(String[] args) {
		//returnedData will store the JSON data in string format
				String returnedData = "";
			
				try
				{
					URL url = new URL("http://api.wunderground.com/api/4148f7944f7ef711/history_20171030/q/NY/New_York.json");
					//Parse URL into HttpURLConnection to open the connection and get the JSON data
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.connect();
					//Get the response status of the API
					int responsecode = conn.getResponseCode();
					System.out.println("Response code is: " +responsecode);					
					/*If response code is not 200 throw a runtime exception
					else get the JSON data*/
					if(responsecode != 200)
						throw new RuntimeException("HttpResponseCode: " +responsecode);
					else
					{
						//With scanner read the JSON data
						Scanner sc = new Scanner(url.openStream());
						while(sc.hasNext())
						{
							returnedData+=sc.nextLine();
						}
						System.out.println("\n The whole JSON Response:\n"+returnedData); 
						sc.close();
					}
					
					JSONParser parse = new JSONParser();
					//Convert parsed json data to json object
					JSONObject jobj = (JSONObject)parse.parse(returnedData);
					//Get first child
				  JSONObject parent = (JSONObject) jobj.get("history");
				  //Get second child
				  JSONArray summary = (JSONArray) parent.get("dailysummary"); 
				  System.out.println("\nSummary is: \n"+summary);
				  String table2 = " ";
				  	for (Object elem : summary) {
				  		String mhumidity="Maximum humidity  is " + ((JSONObject) elem).get("maxhumidity")+".";
				  		String maxtemp ="Maximum temperature in Celcius is " + ((JSONObject) elem).get("maxtempm")+".";
				  		String mintemp ="Minimum temperature in Celcius is " + ((JSONObject) elem).get("mintempm")+".";
				  		String precipm = "Precipitation per milimeter is " + ((JSONObject) elem).get("precipm")+".";
				  		table2 = "\n"+mhumidity+"\n"+maxtemp+"\n"+mintemp+"\n"+precipm;
				  		System.out.println("\n Table data: \n" + table2);
   
				  	}
				  	
				  	//Create file and directory
				  	File file = new File("file.txt");
				  	PrintWriter printWriter = new PrintWriter(file, "UTF-8");
				  	//Write table2 contents to the text file
				  	printWriter.println (table2);
				  	printWriter.close ();   
				  	System.out.println("A file with summary metrics has been created in your current directory!");
					
				  	//Disconnect the HttpURLConnection stream
					conn.disconnect();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	}

}