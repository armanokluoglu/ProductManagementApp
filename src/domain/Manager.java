package domain;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import utilities.CatalogueEntry;

public class Manager extends User {

	private List<User> employees;
	private Product product;
	
	public Manager(String username, String password) {
		super(username, password);
	}

	public JSONObject getProductTree() {
		return ((Assembly) product).getProductTree();
	}
	
	public Product createPart(CatalogueEntry entry) {
		Product part = new Part(entry);
		Product product = getProduct();
		((Assembly) product).addProduct(part);
		return part;
	}

	public Product addPart(Product part) {
		Product product = getProduct();
		((Assembly) product).addProduct(part);
		return part;
	}
	
	public Product createAssembly(String name, long number) {
		Product assembly = new Assembly(name, number);
		Product product = getProduct();
		((Assembly) product).addProduct(assembly);
		return assembly;
	}

	public Product addAssembly(Product assembly) {
		Product product = getProduct();
		((Assembly) product).addProduct(assembly);
		return assembly;
	}
	
	public User createEmployee(String username, String password) {
		User employee = new Employee(username,password);
		employees.add(employee);
		return employee;
	}
	
	public void assignEmployeeToPart(User employee, Product part) {
		((Employee)employee).setPart(part);
	}

	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public JSONObject getJson() {
		JSONObject managerJson = new JSONObject();
		List<JSONObject> employeesJson = new ArrayList<>();
		JSONObject productJson = ((Assembly)product).getProductTree();
		for(User employee:employees){
			employeesJson.add(((Employee)employee).getJson());
		}
		managerJson.put("Id",getId());
		managerJson.put("Username",getUsername());
		managerJson.put("PRODUCT",productJson);
		managerJson.put("EMPLOYEES",employeesJson);
		return managerJson;
	}
	
}
