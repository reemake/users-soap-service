package ru.codemark.userssoapservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codemark.userssoapservice.entity.User;
import ru.codemark.userssoapservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserExist(String login) {
        User userByLogin = userRepository.findById(login).orElse(null);
        if (userByLogin != null)
            return true;
        else
            return false;
    }

    public boolean addUser(User user) {
        if (isUserExist(user.getLogin()))
            return false;
        else {
            user.setName(user.getName());
            user.setPassword(user.getPassword());
            user.setRoles(user.getRoles());
            userRepository.save(user);
            return true;
        }
    }

    public boolean updateUser(String oldLogin, User user) {
        if (isUserExist(oldLogin))
            return false;
        else {
            User existingUser = userRepository.findById(oldLogin).orElse(null);
            existingUser.setLogin(user.getLogin());
            existingUser.setName(user.getName());
            existingUser.setPassword(user.getPassword());
            existingUser.setRoles(user.getRoles());
            userRepository.save(existingUser);
            return true;
        }
    }

    public User getUser(String login) {
        return userRepository.findById(login).orElse(null);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserByLogin(String login) {
        if (isUserExist(login)) {
            userRepository.deleteById(login);
            return true;
        }
        return false;
    }

}
