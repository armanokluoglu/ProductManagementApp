package data_access;

import domain.Admin;
import domain.Employee;
import domain.Manager;
import domain.User;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private List<User> users;

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public User findByUserNameAndPassword(String userName, String password) throws NotFoundException, PasswordIncorrectException {
        for(User user: users){
            if(user.getUsername().equals(userName) ){
                if(user.getPassword().equals(password))
                    return user;
                else
                    throw new PasswordIncorrectException();
            }

        }
        throw new NotFoundException("User with the username not found.");
    }

    public User findByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException("User with the given username not found.");
    }

    public User findManagerByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user instanceof Manager && user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException("Manager with the given username not found.");
    }
    
    public User findManagerById(int id) throws NotFoundException{
        for(User user: users){
            if(user instanceof Manager && user.getId() == id ){
                return user;
            }
        }
        throw new NotFoundException("Manager with the given id not found.");
    }

    public User findEmployeeByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user instanceof Employee && user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException("Employee with the given username not found.");
    }
    
    public User findEmployeeById(int id) throws NotFoundException{
        for(User user: users){
            if(user instanceof Employee && user.getId() == id ){
                return user;
            }
        }
        throw new NotFoundException("Employee with the given id not found.");
    }

    public List<User> findAdmins(){
        List<User> admins = new ArrayList<>();
        for(User user: users){
            if (user instanceof Admin)
                admins.add(user);
        }
        return admins;
    }
    
    public List<User> findManagers(){
        List<User> managers = new ArrayList<>();
        for(User user: users){
            if (user instanceof Manager)
            	managers.add(user);
        }
        return managers;
    }
    
    public List<User> findEmployees(){
        List<User> employees = new ArrayList<>();
        for(User user: users){
            if (user instanceof Employee)
            	employees.add(user);
        }
        return employees;
    }

    public User save(User user){
        users.add(user);
        return user;
    }
}
