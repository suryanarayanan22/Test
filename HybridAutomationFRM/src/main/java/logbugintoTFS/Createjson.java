package logbugintoTFS;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;

public class Createjson extends TFS {
	public static String Filename ="TestScripts_Sprint1.xlsx";
	public static String CreateJS() {
	try {
		
		
		TFS.readOnly(Filename);
		String today = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
		String attachmentUrl= "http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments/c0f523df-53c8-4158-9693-a368e2548201";
		
			for (Map<String, String> bugInfo : bugInfoList) {
			    String reposteps = bugInfo.get("reposteps");
			    String PP = bugInfo.get("priority");
			    String Priority =priorityHandle(PP);
			    String SS = bugInfo.get("severity");
			    String Severity = SeverityHandle(SS);
			    String title = bugInfo.get("Title");
			    System.out.println(PAT);
			    
		StringWriter writer = new StringWriter();
		JsonFactory jsonFactory = new JsonFactory();
		JsonGenerator gen = jsonFactory.createGenerator(writer);

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
		gen.writeStringField("value", title);
		gen.writeEndObject();
		//AssignTo
		gen.writeStartObject();
		gen.writeStringField("op", "add");
		gen.writeStringField("path", "/fields/System.AssignedTo");
		gen.writeStringField("value", title);
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
		gen.writeStringField("value", " {\r\n      \"rel\": \"AttachedFile\",\r\n      \"url\": \"http://192.168.4.222:8080/tfs/GibraltarRepos/_apis/wit/attachments/c0f523df-53c8-4158-9693-a368e2548201\" \r\n    }\r\n  \r\n");
		gen.writeEndObject();		
		// End the JSON array
		gen.writeEndArray();

		// Close the generator
		gen.close();

		// Get the generated JSON array as a string
		String jsonArrayString = writer.toString();
		
		System.out.println(jsonArrayString);
		return jsonArrayString;
			}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	
	}
	
	private static String getNestedJsonObjectAsString() {
        try {
			StringWriter nestedWriter = new StringWriter();
			JsonFactory nestedJsonFactory = new JsonFactory();
			JsonGenerator nestedGen = nestedJsonFactory.createGenerator(nestedWriter);

			// Start writing nested JSON object
			nestedGen.writeStartObject();
			nestedGen.writeStringField("rel", "Attached File");
			nestedGen.writeStringField("url", "abddfghjkljytrtyuikjhbvcxcvbnmnbv");
			nestedGen.writeEndObject();

			// Close the nested generator
			nestedGen.close();

			// Return the nested JSON object as a string
		return nestedWriter.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
//	   // String AResult =bugInfo.get("ACResult");
//    BugPayload bugPayload = new BugPayload("add", "/fields/System.Title", title);
//       BugPayload bugPayload1 = new BugPayload("add", "/fields/Microsoft.VSTS.TCM.ReproSteps",reposteps );
//       BugPayload bugPayload2 = new BugPayload("add", "/fields/System.AssignedTo", "Surya Narayanan Radha Krishnan");
//       BugPayload bugPayload3 = new BugPayload("add", "/fields/Microsoft.VSTS.Common.Priority",Priority );
//       BugPayload bugPayload4 = new BugPayload("add", "/fields/Microsoft.VSTS.Common.Severity", Severity);	
//       BugPayload bugPayload5 = new BugPayload("add", "/fields/System.Tags", today);
//    // Create a JsonObject for the relation value
//        JsonObject relationValue = new JsonObject();
//        relationValue.addProperty("rel", "AttachedFile");
//        relationValue.addProperty("url", attachmentUrl);
////        JsonObject attributes = new JsonObject();
////        attributes.addProperty("comment", "Attachment comment");
////        relationValue.add("attributes", attributes);
//        Gson gson1 = new Gson();
//       String val = "\"rel\":\"Attached File\",\"url\":\"abddfghjkljytrtyuikjhbvcxcvbnmnbv\"";
//       System.out.println(createjson.CreateJS());
//     // String val= relationValue.toString();
//       BugPayload bugPayload6 = new BugPayload("add", "/relations/-",val);
//	       OkHttpClient client = new OkHttpClient().newBuilder()
//			  .build();
//	        Gson gson = new Gson();
//	        String Title = gson.toJson(bugPayload);
//	        String Description = gson.toJson(bugPayload1);
//	        String AssignedTo = gson.toJson(bugPayload2);
//	        String PriorityBP = gson.toJson(bugPayload3);
//	        String SeverityBP = gson.toJson(bugPayload4);
//	        String tagtoday = gson.toJson(bugPayload5);
//	        String Scren=gson.toJson(bugPayload6);

}
