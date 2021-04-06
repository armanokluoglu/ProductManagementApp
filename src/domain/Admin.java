package domain;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Admin extends User {
	
	private List<Product> products = new ArrayList<>();
	private List<User> managers = new ArrayList<>();
	
	public Admin(String username, String password) {
		super(username, password);
	}
	public Admin(int id, String username, String password) { super(id, username, password); }

	@Override
	public JSONObject getJson() {
		JSONObject usersJson = new JSONObject();
		List<JSONObject> managersJson = new ArrayList<>();
		for(User manager:managers){
			managersJson.add(((Manager)manager).getJson());
		}
		usersJson.put("Id",getId());
		usersJson.put("Username",getUsername());
		usersJson.put("MANAGERS",managersJson);
		usersJson.put("password",getPassword());

		return usersJson;
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
	
	public Product createAssembly(String name, int number) {
		Product assembly = new Assembly(name, number);
		List<Product> products = getProducts();
		products.add(assembly);
		setProducts(products);
		return assembly;
	}
	
	public User createManager(String username, String password) {
		User manager = new Manager(username,password);
		managers.add(manager);
		return manager;
	}
	
	public void assignManagerToAssembly(User manager, Product assembly) {
		((Manager)manager).setProduct(assembly);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		List<Product> products = getProducts();
		products.add(product);
		setProducts(products);
	}

	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}

	public static User parseJson(org.json.simple.JSONObject userJson){
		String userName = (String) userJson.get("Username");
		String password = (String)userJson.get("password");
		int id = ((Long)userJson.get("Id")).intValue();
		org.json.simple.JSONArray managersJson = (org.json.simple.JSONArray) userJson.get("MANAGERS");
		List<User> managers = new ArrayList<>();
		if(managersJson.size()>0){
			managersJson.forEach( entry -> managers.add(Manager.parseJson( (org.json.simple.JSONObject) entry)));
		}
		Admin admin = new Admin(id,userName,password);
		admin.setManagers(managers);
		return admin;
	}
}
