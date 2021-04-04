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
        throw new NotFoundException();
    }

    public User findByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException();
    }

    public User findManagerByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user instanceof Manager && user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException();
    }

    public User findEmployeeByUserName(String userName) throws NotFoundException{
        for(User user: users){
            if(user instanceof Employee && user.getUsername().equals(userName) ){
                return user;
            }
        }
        throw new NotFoundException();
    }

    public List<User> findAdmins(){
        List<User> admins = new ArrayList<>();
        for(User user: users){
            if (user instanceof Admin)
                admins.add(user);
        }
        return admins;
    }

    public User save(User user){
        users.add(user);
        return user;
    }
}
