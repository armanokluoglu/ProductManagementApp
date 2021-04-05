package view;

import domain.*;
import utilities.AlreadyExistsException;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;
import utilities.Status;
import java.util.InputMismatchException;
import java.util.Scanner;
import controllers.AssignFunctions;
import controllers.ProductFunctions;
import controllers.UserFunctions;

public class ProductManagement {

	private User currentUser;
	private UserFunctions userFunctions;
	private ProductFunctions productFunctions;
	private AssignFunctions assignFunctions;

	public ProductManagement(UserFunctions userFunctions, ProductFunctions productFunctions, AssignFunctions assignFunctions) {
		this.userFunctions = userFunctions;
		this.productFunctions = productFunctions;
		this.assignFunctions = assignFunctions;
	}

	public void start() {
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
		String username = scanner.next();
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		User currentUser = userFunctions.getCurrentUser(username, password);
		System.out.println("\nWelcome " + currentUser.getUsername() + "!\n");
		System.out.println("What would you like to do?");
		this.currentUser = currentUser;
	}

	private void mainMenu(Scanner scanner) {
		if (currentUser instanceof Admin)
			adminMenu(scanner);
		else if (currentUser instanceof Manager)
			managerMenu(scanner);
		else if (currentUser instanceof Employee)
			employeeMenu(scanner);
	}

	private void adminMenu(Scanner scanner) {
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
			userFunctions.printAllManagers();
			adminMenu(scanner);
			break;
		case 4:
			userFunctions.printAllEmployees();
			adminMenu(scanner);
			break;
		case 5:
			userFunctions.printAllProductTrees();
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

	private void managerMenu(Scanner scanner) {
		System.out.println("1. Product menu");
		System.out.println("2. Employee menu.");
		System.out.println("3. Print product tree.");
		System.out.println("4. Print lonely parts.");
		System.out.println("5. Log out.");
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
			((Manager) currentUser).printProductTree();
			managerMenu(scanner);
			break;
		case 4:
			productFunctions.printLonelyParts();
			managerMenu(scanner);
			break;
		case 5:
			start();
			break;
		default:
			managerMenu(scanner);
			break;
		}
	}

	private void employeeMenu(Scanner scanner) {
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

	private void createManagerMenu(Scanner scanner) {
		System.out.print("Please enter a manager username: ");
		String username = scanner.next();
		System.out.print("Please enter a manager password: ");
		String password = scanner.next();
		User createdManager = userFunctions.createManagerForAdmin(username, password, currentUser);
		System.out.println("Manager with username " + createdManager.getUsername() + " created.");
		adminMenu(scanner);
	}

	private void productMenuForAdmin(Scanner scanner) {
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

	private void productMenuForManager(Scanner scanner) {
		System.out.println("1. Create part.");
		System.out.println("2. Create catalogue entry.");
		System.out.println("3. Create and add assembly to an assembly.");
		System.out.println("4. Add part to an assembly.");
		System.out.println("5. Create and add assembly to my product.");
		System.out.println("6. Add part to my product.");
		System.out.println("7. Print catalogue entries.");
		System.out.println("8. Go back.");
		System.out.println("9. Log out.");
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
			createPartMenu(scanner);
			break;
		case 2:
			createCatalogueMenu(scanner);
			break;
		case 3:
			createAndAddAssemblyToAssemblyMenu(scanner);
			break;
		case 4:
			addPartToAssemblyMenu(scanner);
			break;
		case 5:
			createAndAddAssemblyToMyProductMenu(scanner);
			break;
		case 6:
			addPartToMyProductMenu(scanner);
			break;
		case 7:
			productFunctions.printCatalogue();
			productMenuForManager(scanner);
			break;
		case 8:
			managerMenu(scanner);
			break;
		case 9:
			start();
			break;
		default:
			managerMenu(scanner);
		}
	}

	private void changeStatusOfPartMenu(Scanner scanner) {
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

	private void printStatusOfPart() {
		Product part = ((Employee) currentUser).getPart();
		if (part != null) {
			System.out.println("Status of " + part.getName() + " (" + part.getNumber() + "): " + part.getStatus().toString());
			System.out.println();
		} else {
			System.out.println("Employee does not have an assigned part.\n");
		}
	}

	private void addProductToManagerMenu(Scanner scanner) {
		System.out.print("Please enter the manager username: ");
		String username = scanner.next();
		System.out.print("Please enter the product number: ");
		int productNumber = scanner.nextInt();
		try {
			assignFunctions.assignAssemblyToManagerForAdmin(productNumber, username, currentUser);
			System.out.println("Product has been assigned to " + username + ".\n");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		adminMenu(scanner);
	}

	private void createProductMenu(Scanner scanner) {
		if (currentUser instanceof Admin) {
			createAssemblyAndAssignToManagerMenuForAdmin(scanner);
		}
	}

	private void createAssemblyAndAssignToManagerMenuForAdmin(Scanner scanner) {
		System.out.print("Please enter the username of the manager who you will assign the product: ");
		String username = scanner.next();
		System.out.print("Please enter the name of the product: ");
		String productName = scanner.next();
		System.out.print("Please enter the number of the product: ");
		int productNumber = scanner.nextInt();
		try {
			productFunctions.createAssemblyForAdmin(productName, productNumber, currentUser);
			System.out.println("Product created with name " + productName + " and number " + productNumber + ".\n");
		} catch (AlreadyExistsException e1) {
			System.out.println(e1.getMessage());
			adminMenu(scanner);
		}
		try {
			assignFunctions.assignAssemblyToManagerForAdmin(productNumber, username, currentUser);
			System.out.println("Created product has been assigned to " + username + ".\n");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		adminMenu(scanner);
	}

	private void createAndAddAssemblyToAssemblyMenu(Scanner scanner)  {
		System.out.print("Please enter the name of the new assembly: ");
		String newAssemblyName = scanner.next();
		System.out.print("Please enter the number of the new assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		System.out.print("Please enter the number of the assembly which you will add this product: ");
		int assemblyNumber = scanner.nextInt();
		
		try {
			productFunctions.createAssemblyAndAddToAssemblyForManager(newAssemblyName, newAssemblyNumber, assemblyNumber, currentUser);
			System.out.println("Assembly created with name " + newAssemblyName + " and number " + newAssemblyNumber + ".\n");
			System.out.println("Created assembly added to assembly with the number " + assemblyNumber + ".\n");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		productMenuForManager(scanner);
	}

	private void createAndAddAssemblyToMyProductMenu(Scanner scanner) {
		System.out.print("Please enter the name of the new assembly: ");
		String newAssemblyName = scanner.next();
		System.out.print("Please enter the number of the new assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		
		try {
			productFunctions.createAssemblyAndAddToProductForManager(newAssemblyName, newAssemblyNumber, currentUser);
			System.out.println("Assembly created with name " + newAssemblyName + " and number " + newAssemblyNumber + ".\n");
			System.out.println("Created assembly added to product.\n");
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		productMenuForManager(scanner);
	}

	private void createPartMenu(Scanner scanner) {
		productFunctions.printCatalogue();
		System.out.print("Please enter the number of the part to create: ");
		int newPartNumber = scanner.nextInt();
		try {
			productFunctions.createPartForManager(newPartNumber);
			System.out.println("Part created.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}	
		productMenuForManager(scanner);
	}
	
	private void createCatalogueMenu(Scanner scanner) {
		System.out.print("Please enter the number: ");
		int number = scanner.nextInt();
		System.out.print("Please enter the name: ");
		String name = scanner.next();
		System.out.print("Please enter the cost: ");
		double cost = scanner.nextDouble();
		try {
			productFunctions.createCatalogueEntryForManager(number, name, cost);
			System.out.println("Catalogue entry created.");
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}	
		productMenuForManager(scanner);
	}

	private void addPartToAssemblyMenu(Scanner scanner) {
		System.out.print("Please enter the number of the assembly: ");
		int assemblyNumber = scanner.nextInt();
		System.out.print("Please enter the number of the part: ");
		int partNumber = scanner.nextInt();
		try {
			productFunctions.addPartToAssemblyForManager(assemblyNumber, partNumber, currentUser);
			System.out.println("Part has been added to assembly.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		productMenuForManager(scanner);
	}

	private void addPartToMyProductMenu(Scanner scanner) {
		System.out.print("Please enter the number of the part: ");
		int partNumber = scanner.nextInt();
		try {
			productFunctions.addPartToAssemblyOfManager(partNumber, currentUser);
			System.out.println("Part has been added to product.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		productMenuForManager(scanner);
	}

	private void managerEmployeeMenu(Scanner scanner) {
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

	private void createEmployeeMenu(Scanner scanner) {
		System.out.print("Please enter a employee username: ");
		String username = scanner.next();
		System.out.print("Please enter a employee password: ");
		String password = scanner.next();
		User createdEmployee = userFunctions.createEmployeeForManager(username, password, currentUser);
		System.out.println("Employee with username " + createdEmployee.getUsername() + " created.");
		managerEmployeeMenu(scanner);
	}

	private void assignPartToEmployee(Scanner scanner) {
		System.out.print("Please enter username for employee: ");
		String employeeUserName = scanner.next();
		System.out.print("Please enter the number of the part: ");
		int partNumber = scanner.nextInt();
		try {
			assignFunctions.assignPartToEmployeeForManager(employeeUserName, partNumber, currentUser);
			System.out.println("Part has been assigned to employee.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		managerEmployeeMenu(scanner);
	}
}
