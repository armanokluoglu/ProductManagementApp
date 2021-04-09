package controllers;

import java.util.List;

import data_access.InputOutputOperations;
import data_access.UserRepository;
import domain.Admin;
import domain.Manager;
import domain.User;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

public class UserFunctions {
	
	private UserRepository userRepository;
	private InputOutputOperations io;
	
	public UserFunctions(InputOutputOperations io) {
		this.io = io;
		this.userRepository = io.inputUsers();
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public User createManagerForAdmin(String username, String password, User currentUser) {
		User manager = ((Admin) currentUser).createManager(username, password);
		userRepository.save(manager);
		return manager;
	}
	
	public User createEmployeeForManager(String username, String password, User currentUser) {
		User employee = ((Manager) currentUser).createEmployee(username, password);
		userRepository.save(employee);
		return employee;
	}
	
	public void printProductTreeOfManager(User manager) {
		((Manager) manager).printProductTree();
	}
	
	public void printAllManagers() {
		List<User> managers = userRepository.findManagers();
		for (User manager : managers) {
			System.out.println(manager.getId() + ": " + manager.getUsername());
		}
		System.out.println();
	}
	
	public void printAllEmployees() {
		List<User> employees = userRepository.findEmployees();
		for (User employee : employees) {
			System.out.println(employee.getId() + ": " + employee.getUsername());
		}
		System.out.println();
	}
	
	public void printAllProductTrees() {
		List<User> managers = userRepository.findManagers();
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
	
	public User getCurrentUser(String username, String password) throws NotFoundException, PasswordIncorrectException {
		return userRepository.findByUserNameAndPassword(username, password);
	}
	
	public void saveUsers() {
		io.outputUsers();
	}
}
