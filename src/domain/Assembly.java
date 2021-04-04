package domain;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import utilities.Status;
import utilities.StatusState;

public class Assembly extends Product {

	private List<Product> products;

	public Assembly(String name, long number) {
		super(name, number);
		setProducts(new ArrayList<>());
		setCallback(this);
	}

	public void addProduct(Product product) {
		List<Product> products = getProducts();
		product.setCallback(this); //CHANGED FROM getCallback()
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
		productTree.put("ASSEMBLİES",assemblies);
		return productTree;
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
}