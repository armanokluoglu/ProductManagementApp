package controllers;

import data_access.InputOutputOperations;
import data_access.ProductRepository;
import domain.Admin;
import domain.Assembly;
import domain.Manager;
import domain.Part;
import domain.Product;
import domain.User;
import utilities.AlreadyExistsException;
import utilities.CatalogueEntry;
import utilities.NotFoundException;

public class ProductFunctions {
	
	private ProductRepository productRepository;
	private InputOutputOperations io;

	public ProductFunctions(InputOutputOperations io) {
		this.io = io;
		this.productRepository = io.inputProducts();
	}
	
	public ProductRepository getProductRepository() {
		return productRepository;
	}

	public void addPartToAssemblyOfManager(int partNumber, User currentUser) throws NotFoundException {
		Product newPart = productRepository.findPartByNumber(partNumber);
		((Manager) currentUser).addPart(newPart);
	}
	
	public void addAssemblyToAssemblyOfManager(int assemblyNumber, User currentUser) throws NotFoundException {
		Product newAssembly = productRepository.findAssemblyByNumber(assemblyNumber);
		((Manager) currentUser).addAssembly(newAssembly);
	}
	
	public void createAssemblyForAdmin(String productName, int productNumber, User currentUser) throws AlreadyExistsException {
		try {
			productRepository.findAssemblyByNumber(productNumber);
			throw new AlreadyExistsException("This assembly already exists.");
		} catch(NotFoundException e) {
			Product assembly = new Assembly(productName, productNumber);
			((Admin) currentUser).addProduct(assembly);
			productRepository.save(assembly);
		}
	}
	
	public void createAssemblyAndAddToAssemblyForManager(String newAssemblyName, int newAssemblyNumber, int assemblyNumber, User currentUser) throws NotFoundException, AlreadyExistsException {
		try {
			productRepository.findAssemblyByNumber(newAssemblyNumber);
			throw new AlreadyExistsException("This assembly already exists.");
		} catch(NotFoundException e) {
			Product newAssembly = new Assembly(newAssemblyName, newAssemblyNumber);
			((Manager) currentUser).addProductToAssembly(newAssembly, assemblyNumber);
			productRepository.save(newAssembly);
		}
	}
	
	public void createAssemblyAndAddToProductForManager(String newAssemblyName, int newAssemblyNumber, User currentUser) throws AlreadyExistsException {
		try {
			productRepository.findAssemblyByNumber(newAssemblyNumber);
			throw new AlreadyExistsException("This assembly already exists.");
		} catch(NotFoundException e) {
			Product newAssembly = new Assembly(newAssemblyName, newAssemblyNumber);
			((Manager) currentUser).addAssembly(newAssembly);
			productRepository.save(newAssembly);
		}
	}
	
	public void createPartForManager(int number) throws NotFoundException {
		CatalogueEntry entry = productRepository.findCatalogueEntryByNumber(number);
		Product newPart = new Part(entry);
		productRepository.save(newPart);
	}
	
	public void addPartToAssemblyForManager(int assemblyNumber, int partNumber, User currentUser) throws NotFoundException {
		Product part = productRepository.findPartByNumber(partNumber);
		((Manager) currentUser).addProductToAssembly(part, assemblyNumber);
		
	}
	
	public void printCatalogues() {
		for(CatalogueEntry entry : productRepository.getEntries()) {
			System.out.println("Number: " + entry.getNumber());
			System.out.println("Name: " + entry.getName());
			System.out.println("Cost: " + entry.getCost());
			System.out.println();
		}
	}
	
	public void printLonelyParts() {
		for(Product part : productRepository.findLonelyParts()) {
			System.out.println("Number: " + part.getNumber());
			System.out.println("Name: " + part.getName());
			System.out.println("Cost: " + part.getCost());
			System.out.println();
		}
	}
	
	public void printAllAssemblies() {
		for(Product assembly : productRepository.findAllAssemblies()) {
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
		} catch(NotFoundException e) {
			CatalogueEntry entry = new CatalogueEntry(name, number, cost);
			productRepository.saveEntry(entry);
		}
	}
	
	public void saveProducts() {
		io.outputProducts();
	}
}
