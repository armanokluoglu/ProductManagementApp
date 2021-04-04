package domain;

import org.json.JSONObject;
import utilities.Status;

public class Employee extends User {

	private Product part;
	
	public Employee(String username, String password) {
		super(username, password);
	}
	
	public void changeStatusOfPart(Status status) {
		getPart().changeStatus(status);
	}

	public Product getPart() {
		return part;
	}

	public void setPart(Product part) {
		this.part = part;
	}

	@Override
	public JSONObject getJson(){
		JSONObject employeeJson = new JSONObject();
		employeeJson.put("Username",getUsername());
		employeeJson.put("Part",((Part)getPart()).getJson());
		return employeeJson;
	}
	
	
}
