package utilities;

public class CatalogueEntry {

	private String name;
	private long number;
	private double cost;
	
	public CatalogueEntry(String name, long number, double cost) {
		this.name = name;
		this.number = number;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getNumber() {
		return number;
	}
	
	public void setNumber(long number) {
		this.number = number;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
}
