package view;

import data_access.ProductRepository;
import data_access.UserRepository;
import domain.*;
import utilities.CatalogueEntry;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;
import utilities.Status;
import utilities.StatusState;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductManagement {

	private User currentUser;
	private UserRepository userRepository;
	private ProductRepository productRepository;

	public ProductManagement(UserRepository userRepository, ProductRepository productRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	public void start() throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("==================================================");
		System.out.println("=============== PRODUCT MANAGEMENT ===============");
		System.out.println("==================================================\n");
		try {
			login(scanner);
			mainMenu(scanner);
		} catch (NotFoundException | PasswordIncorrectException e) {
			System.out.println("User Not Found or password is wrong");
			start();
		}
	}

	private void login(Scanner scanner) throws NotFoundException, PasswordIncorrectException {
		System.out.println("===================== Login ======================");
		System.out.print("Please enter your username: ");
		String userName = scanner.next();
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		User currentUser = userRepository.findByUserNameAndPassword(userName, password);
		System.out.println("\nWelcome " + currentUser.getUsername() + "!\n");
		System.out.println("What would you like to do?");
		this.currentUser = currentUser;
	}

	private void mainMenu(Scanner scanner) throws Exception {
		if (currentUser instanceof Admin)
			adminMenu(scanner);
		else if (currentUser instanceof Manager)
			managerMenu(scanner);
		else if (currentUser instanceof Employee)
			employeeMenu(scanner);
	}

	private void adminMenu(Scanner scanner) throws Exception {
		System.out.println("1. Create manager");
		System.out.println("2. Assign product to manager.");
		System.out.println("3. Print all managers.");
		System.out.println("4. Print all employees.");
		System.out.println("5. Print all product trees.");
		System.out.println("6. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			mainMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			createManagerMenu(scanner);
			break;
		case 2:
			productMenuForAdmin(scanner);
			break;
		case 3:
			printAllManagers();
			adminMenu(scanner);
			break;
		case 4:
			printAllEmployees();
			adminMenu(scanner);
			break;
		case 5:
			printAllProductTrees();
			adminMenu(scanner);
			break;
		case 6:
			start();
			break;
		default:
			adminMenu(scanner);
			break;
		}
	}
	
	private void printAllManagers() throws Exception {
		List<User> managers = userRepository.findManagers();
		for (User manager : managers) {
			System.out.println(manager.getId() + ": " + manager.getUsername());
		}
		System.out.println();
	}

	private void printAllEmployees() throws Exception {
		List<User> employees = userRepository.findEmployees();
		for (User employee : employees) {
			System.out.println(employee.getId() + ": " + employee.getUsername());
		}
		System.out.println();
	}

	private void printAllProductTrees() throws Exception {
		List<User> managers = userRepository.findManagers();
		for (User manager : managers) {
			JSONObject productJson = ((Manager) manager).getProductTree();
			printProduct(productJson, 0);
			System.out.println();
		}
	}
	
	private void printProduct(JSONObject json, int indentation) {
		String indentString = new String(new char[indentation]).replace("\0", "  ");
		String name = (String) json.get("name");
		long number = (long) json.get("number");
		double cost = (double) json.get("cost");
		StatusState status = (StatusState) json.get("status");
		System.out.println(indentString + "Name: " + name);
		System.out.println(indentString + "Number: " + number);
		System.out.println(indentString + "Cost: " + cost);
		System.out.println(indentString + "Status: " + status.toString());
		
		if(json.has("PARTS")) {
			JSONArray parts = json.optJSONArray("PARTS");
			printProducts(parts, indentString, indentation, "Parts");
		} 
		if(json.has("ASSEMBLIES")) {
			JSONArray assemblies = json.optJSONArray("ASSEMBLIES");
			printProducts(assemblies, indentString, indentation, "Assemblies");
		}
	}
	
	private void printProducts(JSONArray products, String indentString, int indentation, String print) {
		if(products != null) {
			System.out.println(indentString + print + ": " + (products.length() == 0 ? "None" : ""));
			for (int i = 0; i < products.length(); i++) {
				JSONObject product = (JSONObject) products.get(i);
				printProduct(product, indentation + 1);
				if(i != products.length() - 1) 
					System.out.println();
			}		
		} else {
			System.out.println(indentString + print + ": None");
		}
	}

	private void managerMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.println("1. Product menu");
		System.out.println("2. Employee menu.");
		System.out.println("3. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			managerMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			productMenuForManager(scanner);
			break;
		case 2:
			managerEmployeeMenu(scanner);
			break;
		case 3:
			start();
			break;
		default:
			managerMenu(scanner);
			break;
		}
	}

	private void employeeMenu(Scanner scanner) throws Exception {
		System.out.println("1. Change status of part.");
		System.out.println("2. See status of part.");
		System.out.println("3. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			employeeMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			changeStatusOfPartMenu(scanner);
			break;
		case 2:
			printStatusOfPart();
			employeeMenu(scanner);
			break;
		case 3:
			start();
			break;
		default:
			employeeMenu(scanner);
		}
	}

	private void createManagerMenu(Scanner scanner) throws Exception {
		System.out.print("Please enter a manager username: ");
		String username = scanner.next();
		System.out.print("Please enter a manager password: ");
		String password = scanner.next();
		User manager = ((Admin) currentUser).createManager(username, password);
		userRepository.save(manager);
		adminMenu(scanner);
	}

	private void productMenuForAdmin(Scanner scanner) throws Exception {
		System.out.println("1. Assign product to manager");
		System.out.println("2. Create a new product and assign it to a manager.");
		System.out.println("3. Go back.");
		System.out.println("4. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			mainMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			addProductToManagerMenu(scanner);
			break;
		case 2:
			createProductMenu(scanner);
			break;
		case 3:
			adminMenu(scanner);
			break;
		case 4:
			start();
			break;
		default:
			adminMenu(scanner);
		}
	}

	private void productMenuForManager(Scanner scanner) throws NotFoundException, Exception {
		System.out.println("1. Create assembly");
		System.out.println("2. Create part.");
		System.out.println("3. Add assembly to assembly.");
		System.out.println("4. Add part to assembly.");
		System.out.println("5. Add my product to assembly.");
		System.out.println("6. Add my product to part.");
		System.out.println("7. Go back.");
		System.out.println("8. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			managerMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			creatAssemblyMenuForManager(scanner);
			break;
		case 2:
			createPartMenu(scanner);
			break;
		case 3:
			addAssemblyToAssemblyMenu(scanner);
			break;
		case 4:
			addPartToAssemblyMenu(scanner);
			break;
		case 5:
			addAssemblyToMyProductMenu(scanner);
			break;
		case 6:
			addPartToMyProductMenu(scanner);
			break;
		case 7:
			managerMenu(scanner);
			break;
		case 8:
			start();
			break;
		default:
			managerMenu(scanner);
		}
	}
	
	private void changeStatusOfPartMenu(Scanner scanner) throws Exception {
		printStatusOfPart();
		System.out.println("1. Not Started");
		System.out.println("2. In Progress");
		System.out.println("3. Complete");
		System.out.println("4. Go back.");
		System.out.println("5. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			employeeMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			((Employee) currentUser).changeStatusOfPart(Status.NOT_STARTED);
			employeeMenu(scanner);
			break;
		case 2:
			((Employee) currentUser).changeStatusOfPart(Status.IN_PROGRESS);
			employeeMenu(scanner);
			break;
		case 3:
			((Employee) currentUser).changeStatusOfPart(Status.COMPLETE);
			employeeMenu(scanner);
			break;
		case 4:
			employeeMenu(scanner);
			break;
		case 5:
			start();
			break;
		default:
			employeeMenu(scanner);
		}
	}
	
	private void printStatusOfPart() throws Exception {
		Product part = ((Employee) currentUser).getPart();
		if(part != null) {
			System.out.println("Status of " + part.getName() + ": " + part.getStatus().toString());
			System.out.println();
		} else {
			System.out.println("Employee does not have an assigned part.\n");
		}
	}

	private void addProductToManagerMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter the manager username: ");
		String username = scanner.next();
		System.out.print("Please enter the product number: ");
		int productNumber = scanner.nextInt();
		User manager = userRepository.findManagerByUserName(username);
		Product assembly = productRepository.findAssemblyByNumber(productNumber);
		((Admin) currentUser).assignManagerToAssembly(manager, assembly);
		adminMenu(scanner);
	}

	private void createProductMenu(Scanner scanner) throws Exception {
		if (currentUser instanceof Admin) {
			createAssemblyMenuForAdmin(scanner);
		} else if (currentUser instanceof Manager) {
			creatAssemblyMenuForManager(scanner);
		}

	}

	private void createAssemblyMenuForAdmin(Scanner scanner) throws Exception {
		System.out.print("Please enter the username of the manager who you will assign the product: ");
		String username = scanner.next();
		System.out.print("Please enter the name of the product: ");
		String productName = scanner.next();
		System.out.print("Please enter the number of the product: ");
		int productNumber = scanner.nextInt();
		User user = userRepository.findByUserName(username);
		if (!(user instanceof Manager))
			throw new Exception();
		Product assembly = new Assembly(productName, productNumber);
		productRepository.save(assembly);
		((Manager) user).setProduct(assembly);
		adminMenu(scanner);
	}

	private void creatAssemblyMenuForManager(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter the name of the product: ");
		String productName = scanner.next();
		System.out.print("Please enter the number of the product: ");
		int productNumber = scanner.nextInt();
		Product assembly = ((Manager) currentUser).createAssembly(productName, productNumber);
		productRepository.save(assembly);
		managerMenu(scanner);
	}

	private void addAssemblyToMyProductMenu(Scanner scanner) throws NotFoundException, Exception {

//		System.out.print("Please enter number for assembly which assembly to add assembly: ");
//		int assemblyNumber = scanner.nextInt();
//		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter the number of the assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		Product newAssembly = productRepository.findAssemblyByNumber(newAssemblyNumber);
		productRepository.save(newAssembly);
		((Manager) currentUser).addAssembly(newAssembly);
		productMenuForManager(scanner);
	}

	private void addAssemblyToAssemblyMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter the number of the main assembly: ");
		int assemblyNumber = scanner.nextInt();
		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter the number of the other assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		Product newAssembly = productRepository.findAssemblyByNumber(newAssemblyNumber);
		productRepository.save(newAssembly);
		((Assembly) assembly).addProduct(newAssembly);
		productMenuForManager(scanner);
	}

	private void createPartMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter the name of the part: ");
		String newPartName = scanner.next();
		System.out.print("Please enter the number of the part: ");
		int newPartNumber = scanner.nextInt();
		System.out.print("Please enter the cost of the part: ");
		int newPartCost = scanner.nextInt();
		CatalogueEntry catalogueEntry = new CatalogueEntry(newPartName, newPartNumber, newPartCost);
		Product newPart = new Part(catalogueEntry);
		productRepository.save(newPart);
		productMenuForManager(scanner);
	}

	private void addPartToAssemblyMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter the number of the assembly: ");
		int assemblyNumber = scanner.nextInt();
		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter the number of the part: ");
		int partNumber = scanner.nextInt();
		Product newPart = productRepository.findPartByNumber(partNumber);
		((Assembly) assembly).addProduct(newPart);
		productMenuForManager(scanner);
	}

	private void addPartToMyProductMenu(Scanner scanner) throws NotFoundException, Exception {
//		System.out.print("Please enter number for assembly which assembly to add assembly: ");
//		int assemblyNumber = scanner.nextInt();
//		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter the number of the part: ");
		int partNumber = scanner.nextInt();
		Product newPart = productRepository.findPartByNumber(partNumber);
		((Manager) currentUser).addPart(newPart);
		productMenuForManager(scanner);
	}

	private void managerEmployeeMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.println("1. Create Employee");
		System.out.println("2. Assign part to employee.");
		System.out.println("3. Go back.");
		System.out.println("4. Log out.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Please choose a valid option.\n");
			managerMenu(scanner);
		}
		System.out.println();
		switch (choice) {
		case 1:
			createEmployeeMenu(scanner);
			break;
		case 2:
			assignPartToEmployee(scanner);
			break;
		case 3:
			managerMenu(scanner);
			break;
		case 4:
			start();
			break;
		default:
			managerMenu(scanner);
		}
	}

	private void createEmployeeMenu(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter a employee username: ");
		String username = scanner.next();
		System.out.print("Please enter a employee password: ");
		String password = scanner.next();
		User employee = ((Manager) currentUser).createEmployee(username, password);
		userRepository.save(employee);
		managerEmployeeMenu(scanner);
	}

	private void assignPartToEmployee(Scanner scanner) throws NotFoundException, Exception {
		System.out.print("Please enter username for employee: ");
		String employeeUserName = scanner.next();
		User employee = userRepository.findEmployeeByUserName(employeeUserName);
		System.out.print("Please enter a number of the part: ");
		int partNumber = scanner.nextInt();
		Product part = productRepository.findPartByNumber(partNumber);
		((Manager) currentUser).assignEmployeeToPart(employee, part);
		managerEmployeeMenu(scanner);
	}
}
