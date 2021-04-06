package domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import utilities.CatalogueEntry;
import utilities.Status;
import utilities.StatusState;

public class Assembly extends Product {

	private List<Product> products;

	public Assembly(String name, int number) {
		super(name, number);
		setProducts(new ArrayList<>());
		setCallback(this);
	}

	public void addProduct(Product product) {
		List<Product> products = getProducts();
		product.setCallback(this);
		products.add(product);
		setProducts(products);
	}

	public Product removeProduct(Product product) {
		List<Product> products = getProducts();
		boolean removed = products.remove(product);
		if (removed) {
			setProducts(products);
			return product;
		}
		return null;
	}

	public JSONObject getProductTree() {
		List<Product> products = getProducts();
		List<JSONObject> parts = new ArrayList<>();
		List<JSONObject> assemblies = new ArrayList<>();

		JSONObject productTree = new JSONObject();
		productTree.put("number",getNumber());
		productTree.put("name",getName());
		productTree.put("status",getStatus());
		productTree.put("cost",getCost());
		for(Product product:products){
			if(product instanceof Part){
				JSONObject part = new JSONObject();
				part.put("number",product.getNumber());
				part.put("name",product.getName());
				part.put("status",product.getStatus());
				part.put("cost",product.getCost());
				parts.add(part);
			}else
				assemblies.add(((Assembly)product).getProductTree());
		}
		productTree.put("PARTS",parts);
		productTree.put("ASSEMBLIES",assemblies);
		return productTree;
	}
	
	public void printProduct(JSONObject json, int indentation) {
		String indentString = new String(new char[indentation]).replace("\0", "  ");
		String name = (String) json.get("name");
		int number = (int) json.get("number");
		double cost = (double) json.get("cost");
		StatusState status = (StatusState) json.get("status");
		System.out.println(indentString + "Name: " + name);
		System.out.println(indentString + "Number: " + number);
		System.out.println(indentString + "Cost: " + cost);
		System.out.println(indentString + "Status: " + status.toString());

		if (json.has("PARTS")) {
			JSONArray parts = json.optJSONArray("PARTS");
			printProducts(parts, indentString, indentation, "Parts");
		}
		if (json.has("ASSEMBLIES")) {
			JSONArray assemblies = json.optJSONArray("ASSEMBLIES");
			printProducts(assemblies, indentString, indentation, "Assemblies");
		}
	}

	private void printProducts(JSONArray products, String indentString, int indentation, String print) {
		if (products != null) {
			System.out.println(indentString + print + ": " + (products.length() == 0 ? "None" : ""));
			for (int i = 0; i < products.length(); i++) {
				JSONObject product = (JSONObject) products.get(i);
				printProduct(product, indentation + 1);
				if (i != products.length() - 1)
					System.out.println();
			}
		} else {
			System.out.println(indentString + print + ": None");
		}
	}

	@Override
	public double getCost() {
		double cost = 0;
		List<Product> products = getProducts();
		for (Product product : products) {
			cost += product.getCost();
		}
		return cost;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public StatusState onChildStatusChange() {
		List<Product> products = getProducts();
		int total = 0;
		for (Product product : products) {
			total += product.getStatus().getValue();
		}
		if (total == 0 && getStatus().getEnum() != Status.NOT_STARTED) {
			changeStatus(Status.NOT_STARTED);
		} else if (total == 2 * products.size() && getStatus().getEnum() != Status.COMPLETE) {
			changeStatus(Status.COMPLETE);
		} else if (total < 2 * products.size() && total > 0 && getStatus().getEnum() != Status.IN_PROGRESS) {
			changeStatus(Status.IN_PROGRESS);
		}
		return getStatus();
	}
	
	public static Assembly parseJson(org.json.simple.JSONObject assemblyJson){
		String name = (String) assemblyJson.get("name");
		double cost = ((Long)assemblyJson.get("cost")).doubleValue();
		int number = ((Long)assemblyJson.get("number")).intValue();
		List<Product> products = new ArrayList<>();
		org.json.simple.JSONArray parts = (org.json.simple.JSONArray) assemblyJson.get("PARTS");
		org.json.simple.JSONArray assemblies = (org.json.simple.JSONArray) assemblyJson.get("ASSEMBLIES");
		if (parts.size()>0){
			parts.forEach( entry -> products.add(Part.parseJson( (org.json.simple.JSONObject) entry ) ));
		}
		if(assemblies.size()>0){
			assemblies.forEach( entry -> products.add(Assembly.parseJson( (org.json.simple.JSONObject) entry ) ));
		}
		Assembly assembly = new Assembly(name,number);
		assembly.setProducts(products);
		return assembly;
	}
}