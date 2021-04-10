package domain;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class Manager extends User {

	private List<User> employees;
	private Product product;

	public Manager(String username, String password) {
		super(username, password);
	}

	public Manager(int id, String username, String password) {
		super(id, username, password);
	}

	public JSONObject getProductTree() {
		return getProduct() != null ? ((Assembly) getProduct()).getProductTree() : null;
	}

	public void printProductTree() {
		if (getProduct() != null) {
			((Assembly) product).printProduct(getProductTree(), 0);
		}
	}

	public void addAnotherProductToProduct(Product anotherProduct) {
		Product product = getProduct();
		((Assembly) product).addProduct(anotherProduct);
	}

	public void createEmployeeAndAssignPart(String username, String password, Product newPart) {
		User employee = new Employee(username, password);
		((Employee) employee).setPart(newPart);
	}

	public void assignAssemblyToManager(Product newAssembly, User manager) {
		((Manager) manager).setProduct(newAssembly);
	}

	public Product findAssemblyConnectedToProduct(int assemblyNumber, Product product) {
		if (product instanceof Part) {
			return null;
		}
		if (product.getNumber() == assemblyNumber) {
			return product;
		} else {
			List<Product> products = ((Assembly) product).getProducts();
			for (Product prod : products) {
				return findAssemblyConnectedToProduct(assemblyNumber, prod);
			}
		}
		return null;
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
		JSONObject productJson;
		if (product != null)
			productJson = ((Assembly) product).getProductTree();
		else
			productJson = null;
		if (employees != null) {
			for (User employee : employees) {
				employeesJson.add(((Employee) employee).getJson());
			}
		} else
			employeesJson = null;

		managerJson.put("Id", getId());
		managerJson.put("Username", getUsername());
		managerJson.put("password", getPassword());
		managerJson.put("PRODUCT", productJson);
		managerJson.put("EMPLOYEES", employeesJson);
		return managerJson;
	}

	public static User parseJson(org.json.simple.JSONObject userJson) {
		String userName = (String) userJson.get("Username");
		String password = (String) userJson.get("password");
		int id = ((Long) userJson.get("Id")).intValue();
		org.json.simple.JSONArray employeesJson = (org.json.simple.JSONArray) userJson.get("EMPLOYEES");

		org.json.simple.JSONObject productJson = (org.json.simple.JSONObject) userJson.get("PRODUCT");
		Product assembly;
		if (productJson != null)
			assembly = Assembly.parseJson(productJson);
		else
			assembly = null;

		List<User> employees = new ArrayList<>();
		if (employeesJson.size() > 0) {
			employeesJson.forEach(entry -> employees.add(Employee.parseJson((org.json.simple.JSONObject) entry)));
		}
		Manager manager = new Manager(id, userName, password);
		manager.setEmployees(employees);
		manager.setProduct(assembly);
		return manager;
	}

}
