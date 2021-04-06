package controllers;

import data_access.ProductRepository;
import data_access.UserRepository;
import domain.Admin;
import domain.Manager;
import domain.Product;
import domain.User;
import utilities.NotFoundException;

public class AssignFunctions {

	private UserRepository userRepository;
	private ProductRepository productRepository;

	public AssignFunctions(UserRepository userRepository, ProductRepository productRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}
	
	public void assignPartToEmployeeForManager(int employeeId, int partNumber, User currentUser) throws NotFoundException {
		User employee = userRepository.findEmployeeById(employeeId);
		Product part = productRepository.findPartByNumber(partNumber);
		((Manager) currentUser).assignPartToEmployee(employee, part);
	}	
	
	public void assignAssemblyToManagerForAdmin(int productNumber, int managerId, User currentUser) throws NotFoundException {
		User manager = userRepository.findManagerById(managerId);
		Product product = productRepository.findAssemblyByNumber(productNumber);
		((Admin) currentUser).assignManagerToAssembly(manager, product);
	}
}
