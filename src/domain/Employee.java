package domain;

import org.json.JSONObject;
import utilities.Status;

import java.util.ArrayList;
import java.util.List;

public class Employee extends User {

	private Product part;
	
	public Employee(String username, String password) {
		super(username, password);
	}
	public Employee(int id, String username, String password) {
		super(id, username, password);
	}


	public void changeStatusOfPart(Status status) {
		Product part = getPart();
		if(part == null) {
			System.out.println("Employee does not have an assigned part.");
			return;
		}
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
		employeeJson.put("password",getPassword());
		employeeJson.put("Id",getId());

		return employeeJson;
	}

	public static User parseJson(org.json.simple.JSONObject userJson){
		String userName = (String) userJson.get("Username");
		String password = (String)userJson.get("password");
		int id = ((Long)userJson.get("Id")).intValue();
		org.json.simple.JSONArray employeesJson = (org.json.simple.JSONArray) userJson.get("EMPLOYEES");
		Product part = Part.parseJson((org.json.simple.JSONObject) userJson.get("Part"));
		Employee employee = new Employee(id,userName,password);
		employee.setPart(part);
		return employee;
	}
}
