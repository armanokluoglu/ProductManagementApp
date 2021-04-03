package domain;

import java.util.List;
import org.json.JSONObject;

public class Admin extends User {
	
	private List<Product> products;
	private List<User> managers;
	
	public Admin(String username, String password) {
		super(username, password);
	}
	
	public List<User> getAllManagers() {
		return null;
	}
	
	public List<User> getAllEmployees() {
		return null;
	}
	
	public JSONObject getAllProductTrees() {
		return null;
	}
	
	public Product createAssembly(String name, long number) {
		Product assembly = new Assembly(name, number);
		List<Product> products = getProducts();
		products.add(assembly);
		setProducts(products);
		return assembly;
	}
	
	public User createManager(String username, String password) {
		return null;
	}
	
	public void assignManagerToAssembly(User manager, Product assembly) {
		
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}
	
}
