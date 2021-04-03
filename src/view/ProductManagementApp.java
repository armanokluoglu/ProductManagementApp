package view;

import domain.Assembly;
import domain.Part;
import domain.Product;
import utilities.CatalogueEntry;
import utilities.Status;

public class ProductManagementApp {
	
	public static void main(String[] args) {
//		ProductManagement pm = new ProductManagement();
//		pm.start();
		
		
		CatalogueEntry entry = new CatalogueEntry("part", 456, 10);
		CatalogueEntry entry2 = new CatalogueEntry("part2", 457, 10);
		Product ass = new Assembly("ass", 123);
		Product ass2 = new Assembly("ass2", 124);
		((Assembly) ass).addProduct(ass2);
		Product part = new Part(entry);
		Product part2 = new Part(entry2);
		((Assembly) ass2).addProduct(part);	
		((Assembly) ass2).addProduct(part2);	
		part.changeStatus(Status.COMPLETE);
		part2.changeStatus(Status.COMPLETE);
	}
}