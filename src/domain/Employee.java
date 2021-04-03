package domain;

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
	
	
}
