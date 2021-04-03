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
		return null;
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