package view;

import data_access.ProductRepository;
import data_access.UserRepository;
import domain.*;
import utilities.CatalogueEntry;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductManagement {

	private User currentUser;
	private UserRepository userRepository;
	private ProductRepository productRepository;

	public ProductManagement(UserRepository userRepository,ProductRepository productRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	public void start() throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("=========================================");
		System.out.println("=============== PRODUCT MANAGEMENT ===============");
		System.out.println("=========================================\n");
		try {
			login(scanner);
			mainMenu(scanner);
		} catch (NotFoundException | PasswordIncorrectException e) {
			System.out.println("User Not Found or password is wrong");
			start();
		}
	}

	private void login(Scanner scanner) throws NotFoundException, PasswordIncorrectException {
		System.out.println("================= Login =================");
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
		if(currentUser instanceof Admin)
			adminMenu(scanner);
		else if(currentUser instanceof Manager)
			managerMenu(scanner);
		else if(currentUser instanceof Employee)
			employeeMenu(scanner);
	}

	private void adminMenu(Scanner scanner) throws Exception {
		System.out.println("1. Create manager");
		System.out.println("2. Add product to manager.");
		System.out.print("Please enter a number: ");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		}catch (InputMismatchException e){
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
			default:
				adminMenu(scanner);
				break;

		}
	}

	private void managerMenu(Scanner scanner) throws NotFoundException {
		System.out.println("1. Product menu");
		System.out.println("2. Employee menu.");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		}catch (InputMismatchException e){
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
			default:
				managerMenu(scanner);
				break;

		}
	}
	private void employeeMenu(Scanner scanner){
		System.out.println("3. Change status of part");
	}

	private void createManagerMenu(Scanner scanner) throws Exception {
		System.out.print("Please enter a manager username: ");
		String username = scanner.next();
		System.out.print("Please enter a manager password: ");
		String password = scanner.next();
		User manager = ((Admin)currentUser).createManager(username,password);
		userRepository.save(manager);
		adminMenu(scanner);
	}

	private void productMenuForAdmin(Scanner scanner) throws Exception {
		System.out.println("1. Add product to manager");
		System.out.println("2. Create new product.");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		}catch (InputMismatchException e){
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
			default:
				adminMenu(scanner);
		}
	}

	private void productMenuForManager(Scanner scanner) throws NotFoundException {
		System.out.println("1. Create assembly");
		System.out.println("2. Create part.");
		System.out.println("3. Add assembly to assembly.");
		System.out.println("4. Add part to assembly.");
		System.out.println("5. Add my product to assembly.");
		System.out.println("6. Add my product to part.");


		int choice = -1;
		try {
			choice = scanner.nextInt();
		}catch (InputMismatchException e){
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
				addAssemblyToPartMenu(scanner);
				break;
			case 5:
				addMyProductToAssembly(scanner);
				break;
			case 6:
				addMyProductToPartMenu(scanner);
				break;
			default:
				managerMenu(scanner);
		}
	}

	private void addProductToManagerMenu(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter a manager username: ");
		String username = scanner.next();
		System.out.print("Please enter a product number: ");
		int productNumber = scanner.nextInt();
		User manager = userRepository.findManagerByUserName(username);
		Product assembly = productRepository.findAssemblyByNumber(productNumber);
		((Admin)currentUser).assignManagerToAssembly(manager,assembly);

	}
	private void createProductMenu(Scanner scanner) throws Exception {
		if(currentUser instanceof Admin){
			createAssemblyMenuForAdmin(scanner);
		}
		else if(currentUser instanceof Manager){
		}

	}

	private void createAssemblyMenuForAdmin(Scanner scanner) throws Exception {
		System.out.print("Please enter a manager username which you will add product: ");
		String username = scanner.next();
		System.out.print("Please enter a name of the product: ");
		String productName = scanner.next();
		System.out.print("Please enter a number of the product: ");
		int productNumber = scanner.nextInt();
		User user = userRepository.findByUserName(username);
		if(!(user instanceof Manager))
			throw new Exception();
		Product assembly = new Assembly(productName,productNumber);
		productRepository.save(assembly);
		((Manager) user).setProduct(assembly);
		adminMenu(scanner);
	}

	private void creatAssemblyMenuForManager(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter a name of the product: ");
		String productName = scanner.next();
		System.out.print("Please enter a number of the product: ");
		int productNumber = scanner.nextInt();
		Product assembly = ((Manager) currentUser).createAssembly(productName,productNumber);
		productRepository.save(assembly);
		managerMenu(scanner);
	}

	private void addMyProductToAssembly(Scanner scanner) throws NotFoundException {

//		System.out.print("Please enter number for assembly which assembly to add assembly: ");
//		int assemblyNumber = scanner.nextInt();
//		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter a number of the assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		Product newAssembly = productRepository.findAssemblyByNumber(newAssemblyNumber);
		productRepository.save(newAssembly);
		((Manager)currentUser).addAssembly(newAssembly);
		productMenuForManager(scanner);
	}

	private void addAssemblyToAssemblyMenu(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter number for assembly which assembly to add assembly: ");
		int assemblyNumber = scanner.nextInt();
		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter a number of the assembly: ");
		int newAssemblyNumber = scanner.nextInt();
		Product newAssembly = productRepository.findAssemblyByNumber(newAssemblyNumber);
		productRepository.save(newAssembly);
		((Assembly)assembly).addProduct(newAssembly);
		productMenuForManager(scanner);
	}
	private void createPartMenu(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter a name of the part: ");
		String newPartName = scanner.next();
		System.out.print("Please enter a number of the part: ");
		int newPartNumber = scanner.nextInt();
		System.out.print("Please enter a cost of the part: ");
		int newPartCost = scanner.nextInt();
		CatalogueEntry catalogueEntry = new CatalogueEntry(newPartName,newPartNumber,newPartCost);
		Product newPart = new Part(catalogueEntry);
		productRepository.save(newPart);
		productMenuForManager(scanner);
	}
	private void addAssemblyToPartMenu(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter number for assembly which assembly to add assembly: ");
		int assemblyNumber = scanner.nextInt();
		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter a number of the part: ");
		int partNumber = scanner.nextInt();
		Product newPart = productRepository.findPartByNumber(partNumber);
		((Assembly)assembly).addProduct(newPart);
		productMenuForManager(scanner);
	}
	private void addMyProductToPartMenu(Scanner scanner) throws NotFoundException {
//		System.out.print("Please enter number for assembly which assembly to add assembly: ");
//		int assemblyNumber = scanner.nextInt();
//		Product assembly = productRepository.findAssemblyByNumber(assemblyNumber);
		System.out.print("Please enter a number of the part: ");
		int partNumber = scanner.nextInt();
		Product newPart = productRepository.findPartByNumber(partNumber);
		((Manager)currentUser).addPart(newPart);
		productMenuForManager(scanner);
	}

	private void managerEmployeeMenu(Scanner scanner) throws NotFoundException {
		System.out.println("1. Create Employee");
		System.out.println("2. Assign part to employee.");
		int choice = -1;
		try {
			choice = scanner.nextInt();
		}catch (InputMismatchException e){
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
			default:
				managerMenu(scanner);
		}
	}

	private void createEmployeeMenu(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter a employee username: ");
		String username = scanner.next();
		System.out.print("Please enter a employee password: ");
		String password = scanner.next();
		User employee = ((Manager)currentUser).createEmployee(username,password);
		userRepository.save(employee);
		managerEmployeeMenu(scanner);
	}

	private void assignPartToEmployee(Scanner scanner) throws NotFoundException {
		System.out.print("Please enter username for employee: ");
		String employeeUserName = scanner.next();
		User employee = userRepository.findEmployeeByUserName(employeeUserName);
		System.out.print("Please enter a number of the part: ");
		int partNumber = scanner.nextInt();
		Product part = productRepository.findPartByNumber(partNumber);
		((Manager)currentUser).assignEmployeeToPart(employee,part);
		managerEmployeeMenu(scanner);
	}
}
