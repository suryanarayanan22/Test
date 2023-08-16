package logbugintoTFS;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.Credentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;



public class TFS {
	public static List<Map<String, String>> bugInfoList;
	public static String URL ="http://192.168.4.222:8080/tfs/GibraltarRepos/Gibraltar_Realtor/_apis/wit/workitems/$Bug?api-version=5.1";
	public static String rawPat=":cpc5hhjsucam3ahi6yrhdn62b37iglmrlld73oawuh74jpvb3atq";
	static byte[] encodedBytes = Base64.getEncoder().encode(rawPat.getBytes());
    public static String encode = new String(encodedBytes);
    
	public static String PAT = "Basic " +encode;
	public static String Filename ="TestScripts_Sprint1.xlsx";
	public static List<File> filename;
	public static String AttactURL;
	
	public static void readOnly(String Filename) {
		XSSFWorkbook xs = null;
		XSSFSheet sheet= null;
		String Cellvalue = null;
		int rowcount;
		try {
			FileInputStream stream = new FileInputStream("./testcomponents/web/"+Filename);
			 xs = new XSSFWorkbook(stream);
			 sheet = xs.getSheetAt(0);
			 rowcount = sheet.getLastRowNum(); 
			for (int i = rowcount; i >= 0; i--) {
				XSSFRow row = sheet.getRow(i);
				// Check if the row is empty
				if (row == null || row.getLastCellNum() == -1) {
					continue;
				}				
				break;
				}				
				
					String getscriptId= sheet.getRow(0).getCell(17).toString().trim();
					System.out.println(getscriptId);
					if(getscriptId.equalsIgnoreCase("Test Result (Pass/Fail/No Run/Blocked)")) {
						bugInfoList = new ArrayList<>();
						for(int k=0;k<rowcount;k++) {
							String getscriptname=sheet.getRow(k).getCell(17).toString().trim();
//							System.out.println("Script status: "+getscriptname);
							if(getscriptname.equalsIgnoreCase("Fail")) {
								String  testscriptCell = sheet.getRow(k).getCell(2).toString();
								String actualResultCell =sheet.getRow(k).getCell(18).toString();
								String preconditionCell =sheet.getRow(k).getCell(7).toString();
								String stepsCell = sheet.getRow(k).getCell(9).toString();
								String expectedCell = sheet.getRow(k).getCell(11).toString();
								String priorityCell = sheet.getRow(k).getCell(15).toString();
								String severityCell = sheet.getRow(k).getCell(16).toString();
								//String AssignTo= sheet.getRow(k).getCell(23).toString();
								  String actualResult = actualResultCell != null ? actualResultCell.toString() : "";
			                        String precondition = preconditionCell != null ? preconditionCell.toString() : "";
			                        String steps = stepsCell != null ? stepsCell.toString() : "";
			                        String expected = expectedCell != null ? expectedCell.toString() : "";
			                        String priority = priorityCell != null ? priorityCell.toString() : "";
			                        String severity = severityCell != null ? severityCell.toString() : "";
			                        String testscriptID = testscriptCell != null ? testscriptCell.toString() : "";
								String Reposteps = 
										"<pre>Precondition:<br>"
										+ precondition+
										"<br><br>Repo Steps:<br><br>" 
										+ steps + 
										"<br><br>Expected result:<br><br>" 
										+ expected + 
										"<br><br>Actual result:<br><br>"
										+ actualResult+"<br></pre>";
								Map<String, String> bugInfo = new HashMap<>();
				                bugInfo.put("reposteps", Reposteps);
				                bugInfo.put("priority", priority);
				                bugInfo.put("severity", severity);
				                bugInfo.put("Title",actualResult);
				                bugInfo.put("TestScript", testscriptID);
				               // bugInfo.put("ACResult", Actualresult);

				                bugInfoList.add(bugInfo);
					}

			
			
		}
					}
		}




	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		try {
			xs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//return Cellvalue;
		 
}
			

	
	
	
		
	
	
	
	
	
	

   
        // Example usage
	
   
    

	public static void main(String[] args) throws URISyntaxException {
		 
		 Attachfile();
         readOnly(Filename);
         logAllBugs(URL,PAT);
        // Attachfile();
        
	}
	public static String priorityHandle(String priority) {
		String one="1.0";
		String two="2.0";
		String three="3.0";
		String four="4.0";
		String replace=null;
		if(priority.equals(one)) {
			String replace1=priority.replace(one, "1");
			return replace1;
		}else if(priority.equals(two)) {
		  String replace2=priority.replace(two, "2");
		  return replace2;
		}else if(priority.equals(three)) {
			String replace3=priority.replace(three, "3");
			 return replace3;
		}else if(priority.equals(four)) {
			String replace4=priority.replace(four, "4");
			 return replace4;
		}else {
			System.out.println("No of the condition match");
		}
		
		return replace;
		
	}
	
	public static String SeverityHandle(String priority) {
		String one="Critcal";
		String two="High";
		String three="Medium";
		String four="Low";
		String replace = null;
		if(priority.equalsIgnoreCase(one)) {
			 replace=priority.replace(one, "1 - Critical");
			
		}else if(priority.equalsIgnoreCase(two)) {
			 replace=priority.replace(two, "2 - High");
			
		}else if(priority.equalsIgnoreCase(three)) {
			 replace=priority.replace(three, "3 - Medium");
		}else if(priority.equalsIgnoreCase(four)) {
			 replace=priority.replace(four, "4 - Low");
		}
		
		return replace;
		
	}
	
	
	
	public static void logAllBugs(String URL,String PAT) {
		String today = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
		String attachmentUrl= "http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments/c0f523df-53c8-4158-9693-a368e2548201";
		try {
			for (Map<String, String> bugInfo : bugInfoList) {
			    String reposteps = bugInfo.get("reposteps");
			    String PP = bugInfo.get("priority");
			    String Priority =priorityHandle(PP);
			    String SS = bugInfo.get("severity");
			    String Severity = SeverityHandle(SS);
			    String title = bugInfo.get("Title");
			   String testID= bugInfo.get("TestScript");
			   for (File fileid : filename) {
				   
				   String filename =fileid.getName();
				   String newFilename = filename.replace(".png", "");
				   System.out.println(newFilename);
				   if(testID.contains(newFilename)) {
					   
						  File filelocation = new File("./ToUpload/"+filename);
//						  String loc =filelocation.getName();
				          System.out.println("Image File: " + testID);
						OkHttpClient client = new OkHttpClient().newBuilder()
								  .build();
								MediaType mediaType = MediaType.parse("application/octet-stream");
								RequestBody body = RequestBody.create(filelocation,mediaType);
								Request request = new Request.Builder()
								  .url("http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments?fileName="+filename+"&api-version=5.1")
								  .method("POST", body)
								  .addHeader("Content-Type", "application/octet-stream")
								  .addHeader("Authorization", "Basic OmNwYzVoaGpzdWNhbTNhaGk2eXJoZG42MmIzN2lnbG1ybGxkNzNvYXd1aDc0anB2YjNhdHE=")
								  .build();
								Response response = client.newCall(request).execute();
								String responseBody = response.body().string();
								 System.out.println("Response Body: " + responseBody);
								 JsonParser jsonParser = new JsonParser();
						            JsonObject jsonResponse = jsonParser.parse(responseBody).getAsJsonObject();
				
						            // Extract the bug ID from the response
						            String AttactURL = jsonResponse.get("url").getAsString();
						            System.out.println("URL: " + AttactURL);
					   
				   
				   
			   
			   
				   
			    System.out.println(PAT);
			    StringWriter writer = new StringWriter();
				JsonFactory jsonFactory = new JsonFactory();
				JsonGenerator gen = jsonFactory.createGenerator(writer);
				OkHttpClient client1 = new OkHttpClient().newBuilder()
						  .build();
				// Start writing JSON array
				gen.writeStartArray();

				//title
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/System.Title");
				gen.writeStringField("value", title);
				gen.writeEndObject();
				//reposteps
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/Microsoft.VSTS.TCM.ReproSteps");
				gen.writeStringField("value", reposteps);
				gen.writeEndObject();
				//AssignTo
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/System.AssignedTo");
				gen.writeStringField("value", "Surya Narayanan Radha Krishnan");
				gen.writeEndObject();  
				//priority
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/Microsoft.VSTS.Common.Priority");
				gen.writeStringField("value", Priority);
				gen.writeEndObject();
				//Severity
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/Microsoft.VSTS.Common.Severity");
				gen.writeStringField("value",Severity );
				gen.writeEndObject();
				//Tag
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/fields/System.Tags");
				gen.writeStringField("value", today);
				gen.writeEndObject();
				//InsertImage
				gen.writeStartObject();
				gen.writeStringField("op", "add");
				gen.writeStringField("path", "/relations/-");
				gen.writeObjectFieldStart("value");
		        gen.writeStringField("rel", "AttachedFile");
		        gen.writeStringField("url", AttactURL);
		        gen.writeEndObject();
				//gen.writeStringField("value", " {\r\n      \"rel\": \"AttachedFile\",\r\n      \"url\": \"http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments/c0f523df-53c8-4158-9693-a368e2548201\" \r\n    }\r\n  \r\n");
				gen.writeEndObject();		
				// End the JSON array
				gen.writeEndArray();

				// Close the generator
				gen.close();

				// Get the generated JSON array as a string
				String jsonbody = writer.toString();

		//		        System.out.println("Auth: "+PAT+ " JSON Body: " + Scren+Title+AssignedTo+PriorityBP+SeverityBP);
		//		        String jsonbody = "[\r\n"+Title+","+Description+","+AssignedTo+","+PriorityBP+","+SeverityBP+","+tagtoday+","+Scren+"\r\n]\r\n";
				      // String jsonbody ="[\r\n  {\r\n    \"op\": \"add\",\r\n    \"path\": \"/fields/System.Title\",\r\n    \"from\": null,\r\n    \"value\": \"Sample task\"\r\n  }\r\n]\r\n";
				      //  System.out.println(jsonbody);
				        MediaType mediaType1 = MediaType.parse("application/json-patch+json");
						RequestBody body1 = RequestBody.create(jsonbody,mediaType1 );
						Request request1 = new Request.Builder()
						  .url(URL)
						  .method("POST", body1)
						  .addHeader("Content-Type", "application/text")
						  .addHeader("Authorization", PAT)
						  .build();
						 Response response1 = client1.newCall(request1).execute();
						 String responseBody1 = response1.body().string();
						 System.out.println("Response Body: " + responseBody1);
						 JsonParser jsonParser1 = new JsonParser();
				            JsonObject jsonResponse1 = jsonParser1.parse(responseBody1).getAsJsonObject();

				            // Extract the bug ID from the response
				            String bugId = jsonResponse1.get("id").getAsString();
				            System.out.println("Bug ID: " + bugId);
						 
			             if (response.isSuccessful()) {
				        System.out.println("New bug created successfully.");
				    } else {
				        System.out.println("Error creating new bug: " + response.code() + " - " + response.message());
				    }
			       Thread.sleep(5000);
				   }
			   }
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
  public static List<File> Attachfile() {
	  String SS = null;
	  try {
		  String folderPath = "./ToUpload/";
	        File folder = new File(folderPath);
	        if (folder.exists() && folder.isDirectory()) {
	            File[] files = folder.listFiles();
	            filename= new ArrayList<File>();
	            for (File file : files) {
	                if (file.isFile() && isImageFile(file.getName())) {
	                	SS =file.getName();
	                    //System.out.println("Image File: " + SS);
	                    File filelocation = new File("./ToUpload/"+SS);
	                    filename.add(filelocation);
	                    
	                }
	            }
	        }
		  return filename;
		  
		//  File filelocation = new File("./ToUpload/TS_003.png");
//		  String loc =filelocation.getName();
//          System.out.println("Image File: " + filelocation);
//		OkHttpClient client = new OkHttpClient().newBuilder()
//				  .build();
//				MediaType mediaType = MediaType.parse("application/octet-stream");
//				RequestBody body = RequestBody.create(filelocation,mediaType);
//				Request request = new Request.Builder()
//				  .url("http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments?fileName="+loc+"&api-version=5.1")
//				  .method("POST", body)
//				  .addHeader("Content-Type", "application/octet-stream")
//				  .addHeader("Authorization", "Basic OmNwYzVoaGpzdWNhbTNhaGk2eXJoZG42MmIzN2lnbG1ybGxkNzNvYXd1aDc0anB2YjNhdHE=")
//				  .build();
//				Response response = client.newCall(request).execute();
//				String responseBody = response.body().string();
//				 System.out.println("Response Body: " + responseBody);
//				 JsonParser jsonParser = new JsonParser();
//		            JsonObject jsonResponse = jsonParser.parse(responseBody).getAsJsonObject();
//
//		            // Extract the bug ID from the response
//		            String bugId = jsonResponse.get("url").getAsString();
//		            System.out.println("URL: " + bugId);
				
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
  }
  
  
  private static boolean isImageFile(String fileName) {
      String[] imageExtensions = { ".png", ".jpg", ".jpeg", ".gif", ".bmp" };
      for (String extension : imageExtensions) {
          if (fileName.toLowerCase().endsWith(extension)) {
              return true;
          }
      }
      return false;
  }
	
	
	
	
	
	
		
	
	
	

	public static String readData() {
	    XSSFWorkbook xs = null;
		XSSFSheet sheet= null;
		String Cellvalue = null;
		int rowcount;
		try {
			FileInputStream stream = new FileInputStream("./testcomponents/web/TestScripts_Sprint1.xlsx");
			 xs = new XSSFWorkbook(stream);
			 sheet = xs.getSheetAt(0);
			 rowcount = sheet.getLastRowNum(); 
			for (int i = rowcount; i >= 0; i--) {
				XSSFRow row = sheet.getRow(i);
				// Check if the row is empty
				if (row == null || row.getLastCellNum() == -1) {
					continue;
				}				
				break;
				}				
				
					String getscriptId= sheet.getRow(0).getCell(16).toString().trim();
					System.out.println(getscriptId);
					if(getscriptId.equalsIgnoreCase("SIT 1")) {
						for(int k=0;k<rowcount;k++) {
							String getscriptname=sheet.getRow(k).getCell(16).toString().trim();
							//System.out.println("Script status: "+getscriptname);
							if(getscriptname.equalsIgnoreCase("Fail")) {
								String failled =sheet.getRow(k).getCell(16).toString();
								String Precondition =sheet.getRow(k).getCell(7).toString();
								String Steps = sheet.getRow(k).getCell(9).toString();
								String Expected = sheet.getRow(k).getCell(8).toString();
								String priority = sheet.getRow(k).getCell(14).toString();
								String severity = sheet.getRow(k).getCell(15).toString();
								String Reposteps = 
										"Precondition:<br>"+ System.lineSeparator()
										+ Precondition+ System.lineSeparator() + 
										"<br><br>Repo Steps:<br><br>" 
										+ Steps + 
										"<br><br>Expected result:<br><br>" 
										+ Expected + 
										"<br><br>Actual result:";
								//String escapedMultilineText = StringEscapeUtils.escapeJson(Reposteps);
							    BugPayload bugPayload = new BugPayload("add", "/fields/System.Title", Expected);
							       BugPayload bugPayload1 = new BugPayload("add", "/fields/Microsoft.VSTS.TCM.ReproSteps",Reposteps );
							       BugPayload bugPayload2 = new BugPayload("add", "/fields/System.AssignedTo", "Surya Narayanan Radha Krishnan");
							       BugPayload bugPayload3 = new BugPayload("add", "/fields/Microsoft.VSTS.Common.Priority", "2");
							       BugPayload bugPayload4 = new BugPayload("add", "/fields/Microsoft.VSTS.Common.Severity", "3 - Medium");	
								       OkHttpClient client = new OkHttpClient().newBuilder()
										  .build();
								        Gson gson = new Gson();
								        String Title = gson.toJson(bugPayload);
								        String Description = gson.toJson(bugPayload1);
								        String AssignedTo = gson.toJson(bugPayload2);
								        String Priority = gson.toJson(bugPayload3);
								        String Severity = gson.toJson(bugPayload4);
								        System.out.println("JSON Body: " + Title+Description+AssignedTo+Priority+Severity);
								        String jsonbody = "[\r\n"+Title+","+Description+","+AssignedTo+","+Priority+","+Severity+"\r\n]\r\n";
								      // String jsonbody ="[\r\n  {\r\n    \"op\": \"add\",\r\n    \"path\": \"/fields/System.Title\",\r\n    \"from\": null,\r\n    \"value\": \"Sample task\"\r\n  }\r\n]\r\n";
								        System.out.println(jsonbody);
								        MediaType mediaType = MediaType.parse("application/json-patch+json");
										RequestBody body = RequestBody.create(jsonbody,mediaType );
										Request request = new Request.Builder()
										  .url("http://192.168.4.222:8080/tfs/GibraltarRepos/Gibraltar_Realtor/_apis/wit/workitems/$Bug?api-version=5.1")
										  .method("POST", body)
										  .addHeader("Content-Type", "application/text")
										  .addHeader("Authorization", "Basic OmNwYzVoaGpzdWNhbTNhaGk2eXJoZG42MmIzN2lnbG1ybGxkNzNvYXd1aDc0anB2YjNhdHE=")
										  .build();
										
									
										 Response response = client.newCall(request).execute();
						                 if (response.isSuccessful()) {
								        System.out.println("New bug created successfully.");
								    } else {
								        System.out.println("Error creating new bug: " + response.code() + " - " + response.message());
								    }
								
								System.out.println( Reposteps);
							}
						}
						
//						}
					}

			
			
		}




	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		try {
			xs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cellvalue;
	
}
	
	
	
	
	public  String readdataTest(String ScriptId,String TestDataHeading) {
	    XSSFWorkbook xs = null;
		XSSFSheet sheet= null;
		String Cellvalue = null;
		
		try {
			FileInputStream stream = new FileInputStream("./XLData/TestData.xlsx");
			 xs = new XSSFWorkbook(stream);
			 sheet = xs.getSheetAt(0);
			int rowcount = sheet.getLastRowNum(); 
			//System.out.println(rowcount);
			//HashMap<Object,Object> data =new HashMap<Object,Object>();
			for (int i = rowcount; i >= 0; i--) {
				XSSFRow row = sheet.getRow(i);
				// Check if the row is empty
				if (row == null || row.getLastCellNum() == -1) {
					continue;
				}
				
				break;

				}				
				for(int k=0; k<rowcount; k++){
					String getscriptId= sheet.getRow(k).getCell(0).toString().trim();
					String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
					System.out.println("Current method name: " + methodName);
					if(getscriptId.equals(ScriptId)) {
						int lastcolumn= sheet.getRow(k).getLastCellNum();
						for(int j=0; j<lastcolumn; j++) {
							String getscriptname=sheet.getRow(k).getCell(j).toString().trim();
							if(getscriptname.equals(TestDataHeading)) {
								 Cellvalue=sheet.getRow(k+1).getCell(j).toString().trim();					     
								//return Cellvalue;	
							}
						}
					}

			}
			
		}




	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		try {
			xs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cellvalue;
	
}

}