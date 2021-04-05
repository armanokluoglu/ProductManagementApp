package view;

import data_access.InputOutputOperations;
import data_access.ProductRepository;
import data_access.UserRepository;
import domain.*;
import org.json.JSONObject;
import controllers.AssignFunctions;
import controllers.ProductFunctions;
import controllers.UserFunctions;
import utilities.CatalogueEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductManagementApp {
	
	public static void main(String[] args) throws Exception {
//		ProductManagement pm = new ProductManagement();
//		pm.start();

		CatalogueEntry entry = new CatalogueEntry("part", 456, 10);
		CatalogueEntry entry2 = new CatalogueEntry("part2", 457, 10);
		CatalogueEntry entry3 = new CatalogueEntry("part3", 458, 10);
		CatalogueEntry entry4 = new CatalogueEntry("part4", 459, 10);
		Product ass = new Assembly("ass", 123);
		Product ass2 = new Assembly("ass2", 124);
		((Assembly) ass).addProduct(ass2);
		Product part = new Part(entry);
		Product part2 = new Part(entry2);
		Product part3 = new Part(entry3);
		Product part4 = new Part(entry4);
		((Assembly) ass2).addProduct(part);
		((Assembly) ass2).addProduct(part2);

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
		List<Product> products = new ArrayList<>(Arrays.asList(ass,ass2,part,part2,part3,part4));

		JSONObject x = ((Assembly) ass).getProductTree();
		System.out.println(x.toString());
		UserRepository userRepository = new UserRepository(users);
		ProductRepository productRepository = new ProductRepository(products, new ArrayList<>(Arrays.asList(entry, entry2, entry3, entry4)));
		UserFunctions userFunctions = new UserFunctions(userRepository);
		ProductFunctions productFunctions = new ProductFunctions(productRepository);
		AssignFunctions assignFunctions = new AssignFunctions(userRepository, productRepository);
		ProductManagement productManagement = new ProductManagement(userFunctions, productFunctions, assignFunctions);
		InputOutputOperations io = new InputOutputOperations(productRepository,userRepository);
		io.outputProducts();
		io.outputUsers();

		productManagement.start();
	}

}