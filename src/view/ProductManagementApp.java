package view;

import data_access.ProductRepository;
import data_access.UserRepository;
import domain.*;
import utilities.CatalogueEntry;
import utilities.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductManagementApp {
	
	public static void main(String[] args) throws Exception {
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
		//part.changeStatus(Status.COMPLETE);
		//part2.changeStatus(Status.COMPLETE);

		User admin = new Admin("admin","1234");
		User manager = new Manager("manager","1234");
		User employee1 = new Employee("emp1","1234");
		User employee2 = new Employee("emp2","1234");
		((Employee) employee1).setPart(part);
		((Employee) employee2).setPart(part2);
		((Manager)manager).setEmployees(new ArrayList<>(Arrays.asList(employee1,employee2)));
		((Manager)manager).setProduct(ass);

		((Admin) admin).setManagers(new ArrayList<>(Arrays.asList(manager)));
		((Admin)admin).setProducts(new ArrayList<>(Arrays.asList(ass)));

		List<User> users = new ArrayList<>(Arrays.asList(admin,manager,employee1,employee2));
		List<Product> products = new ArrayList<>(Arrays.asList(ass,ass2,part,part2));
		UserRepository userRepository = new UserRepository(users);
		ProductRepository productRepository = new ProductRepository(products);
		ProductManagement productManagement = new ProductManagement(userRepository,productRepository);
		productManagement.start();
	}

}