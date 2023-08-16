package logbugintoTFS;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class BugPayload {
	@SerializedName("op") // Use annotation to map class field to JSON property
    private String operation;

    @SerializedName("path")
    private String path;

    @SerializedName("value")
    private String value;
    
 //   @SerializedName("value")
 //   private JsonObject value1;
    
    public BugPayload(String operation, String path, String value) {
        this.operation = operation;
        this.path = path;
        this.value = value;
    }

//    public BugPayload(String operation2, String path2, JsonObject relationValue) {
//		// TODO Auto-generated constructor stub
//    	this.operation = operation2;
//        this.path = path2;
//        this.value1 = relationValue;
//	}s

	// Setters and getters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
