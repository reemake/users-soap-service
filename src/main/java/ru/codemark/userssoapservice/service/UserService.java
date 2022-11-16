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

    public boolean isUserExists(String login) {
        return userRepository.findById(login).isPresent();
    }

    public boolean addUser(User user) {
        if (isUserExists(user.getLogin()))
            return false;
        else {
            user.setName(user.getName());
            user.setPassword(user.getPassword());
            user.setRoles(user.getRoles());
            userRepository.save(user);
            return true;
        }
    }

    public boolean updateUser(String currentLogin, User user) {
        if (isUserExists(user.getLogin()))
            return false;
        else {
            userRepository.deleteById(currentLogin);
            userRepository.save(user);
            return true;
        }
    }

    public User getUser(String login) {
        return userRepository.findById(login).get();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserByLogin(String login) {
        if (isUserExists(login)) {
            userRepository.deleteById(login);
            return true;
        }
        return false;
    }

}
