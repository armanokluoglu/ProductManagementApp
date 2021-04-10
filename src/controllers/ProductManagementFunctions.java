package controllers;

import java.util.List;
import data_access.InputOutputOperations;
import data_access.ProductRepository;
import data_access.UserRepository;
import domain.*;
import utilities.AlreadyExistsException;
import utilities.CatalogueEntry;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

public class ProductManagementFunctions {

	private ProductRepository productRepository;
	private UserRepository userRepository;
	private InputOutputOperations io;

	public ProductManagementFunctions(InputOutputOperations io) {
		this.io = io;
		this.productRepository = io.inputProducts();
		this.userRepository = io.inputUsers();
	}

	public ProductRepository getProductRepository() {
		return productRepository;
	}

	public void createPartAndEmployeeForAssemblyOfManager(int catalogueNumberForNewPart, String username,
			String password, User currentUser) throws NotFoundException, AlreadyExistsException {
		CatalogueEntry entry = productRepository.findCatalogueEntryByNumber(catalogueNumberForNewPart);
		Product newPart = new Part(entry);
		if(userRepository.existUserName(username))
			throw new AlreadyExistsException("employee already exist");
		User user = new Employee(username,password);
		userRepository.save(user);
		((Manager) currentUser).createEmployeeAndAssignPart(user.getUsername(), user.getPassword(), newPart);
		((Manager) currentUser).addAnotherProductToProduct(newPart);
	}

	public void createAssemblyAndAssignToManagerForAssemblyOfManager(String newAssemblyName, int newAssemblyNumber,
			int managerNumber, User currentUser) throws NotFoundException, AlreadyExistsException {

		if(productRepository.isAssemblyExistByNameAndNumber(newAssemblyName,newAssemblyNumber))
			throw new AlreadyExistsException("assembly already exist");
		Product newAssembly = new Assembly(newAssemblyName, newAssemblyNumber);
		User manager = userRepository.findManagerById(managerNumber);
		((Manager) currentUser).addAnotherProductToProduct(newAssembly);
		((Manager) currentUser).assignAssemblyToManager(newAssembly, manager);
	}

	public void createAssemblyForAdmin(String productName, int productNumber, User currentUser)
			throws AlreadyExistsException {
		try {
			productRepository.findAssemblyByNumber(productNumber);
			throw new AlreadyExistsException("This assembly already exists.");
		} catch (NotFoundException e) {
			Product assembly = new Assembly(productName, productNumber);
			((Admin) currentUser).addProduct(assembly);
			productRepository.save(assembly);
		}
	}

	public void printCatalogues() {
		for (CatalogueEntry entry : productRepository.getEntries()) {
			System.out.println("Number: " + entry.getNumber());
			System.out.println("Name: " + entry.getName());
			System.out.println("Cost: " + entry.getCost());
			System.out.println();
		}
	}

	public void printAllAssemblies() {
		for (Product assembly : productRepository.findAllAssemblies()) {
			System.out.println("Number: " + assembly.getNumber());
			System.out.println("Name: " + assembly.getName());
			System.out.println("Cost: " + assembly.getCost());
			System.out.println();
		}
	}

	public void createCatalogueEntryForManager(int number, String name, double cost) throws AlreadyExistsException {
		try {
			productRepository.findCatalogueEntryByNumber(number);
			throw new AlreadyExistsException("This catalogue entry already exists.");
		} catch (NotFoundException e) {
			CatalogueEntry entry = new CatalogueEntry(name, number, cost);
			productRepository.saveEntry(entry);
		}
	}

	public void saveProducts() {
		io.outputProducts(productRepository);
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public User createManagerForAdmin(String username, String password, User currentUser) throws AlreadyExistsException {
		User manager = ((Admin) currentUser).createManager(username, password);
		userRepository.save(manager);
		return manager;
	}

	public void printProductTreeOfManager(User manager) {
		((Manager) manager).printProductTree();
	}

	public void printAllManagersForAdmin(User admin) {
		List<User> managers = ((Admin) admin).getManagers();
		for (User manager : managers) {
			System.out.println(manager.getId() + ": " + manager.getUsername());
		}
		System.out.println();
	}

	public void printAllEmployeesForAdmin(User admin) {
		List<User> employees = ((Admin) admin).getAllEmployees();
		for (User employee : employees) {
			System.out.println(employee.getId() + ": " + employee.getUsername());
		}
		System.out.println();
	}

	public void printAllProductTreesForAdmin(User admin) {
		List<User> managers = ((Admin) admin).getManagers();
		for (User manager : managers) {
			((Manager) manager).printProductTree();
			System.out.println();
		}
	}

	public void printEmployeesOfManager(User currentUser) {
		List<User> employees = ((Manager) currentUser).getEmployees();
		for (User employee : employees) {
			System.out.println(employee.getId() + ": " + employee.getUsername());
			System.out.println();
		}
	}

	public void printAllManagersWithoutProducts() {
		List<User> managers = userRepository.findManagers();
		for (User manager : managers) {
			if (((Manager) manager).getProduct() == null)
				System.out.println(manager.getId() + ": " + manager.getUsername());
		}
		System.out.println();
	}

	public User getCurrentUser(String username, String password) throws NotFoundException, PasswordIncorrectException {
		return userRepository.findByUserNameAndPassword(username, password);
	}

	public void saveUsers() {
		io.outputUsers(userRepository);
	}

	public void assignAssemblyToManagerForAdmin(int productNumber, int managerId, User currentUser)
			throws NotFoundException {
		User manager = userRepository.findManagerById(managerId);
		Product product = productRepository.findAssemblyByNumber(productNumber);
		((Admin) currentUser).assignManagerToAssembly(manager, product);
	}
}
