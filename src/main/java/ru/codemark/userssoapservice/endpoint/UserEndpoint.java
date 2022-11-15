package ru.codemark.userssoapservice.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.codemark.userssoapservice.DeleteUserRequest;
import ru.codemark.userssoapservice.DeleteUserResponse;
import ru.codemark.userssoapservice.GetUserRequest;
import ru.codemark.userssoapservice.GetUserResponse;
import ru.codemark.userssoapservice.GetUsersRequest;
import ru.codemark.userssoapservice.GetUsersResponse;
import ru.codemark.userssoapservice.PostUserRequest;
import ru.codemark.userssoapservice.PostUserResponse;
import ru.codemark.userssoapservice.UpdateUserRequest;
import ru.codemark.userssoapservice.UpdateUserResponse;
import ru.codemark.userssoapservice.UserDetails;
import ru.codemark.userssoapservice.entity.Role;
import ru.codemark.userssoapservice.entity.User;
import ru.codemark.userssoapservice.repository.RoleRepository;
import ru.codemark.userssoapservice.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Endpoint
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {

        GetUserResponse response = new GetUserResponse();
        UserDetails userDetails = new UserDetails();
        User existingUser = userService.getUser(request.getLogin());

        if (existingUser == null) {
            response.setSuccess("false");
            response.getError().add("User with stated login does not exist");
        }
        else {
            userDetails.setLogin(existingUser.getLogin());
            userDetails.setName(existingUser.getName());
            userDetails.setPassword(existingUser.getPassword());

            List<Role> existingUserRolesList = new ArrayList<>(existingUser.getRoles());
            for (Role role : existingUserRolesList) {
                response.getRole().add(role.getName());
            }
            response.setUserDetails(userDetails);
        }

        return response;
    }

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "GetUsersRequest")
    @ResponsePayload
    public GetUsersResponse getUsers(@RequestPayload GetUsersRequest request) {

        GetUsersResponse response = new GetUsersResponse();

        List<User> users = userService.getUsers();
        for (User user : users) {
            UserDetails userDetails = new UserDetails();
            userDetails.setLogin(user.getLogin());
            userDetails.setName(user.getName());
            userDetails.setPassword(user.getPassword());

            response.getUsersDetails().add(userDetails);
        }

        return response;
    }

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "PostUserRequest")
    @ResponsePayload
    public PostUserResponse addUser(@RequestPayload PostUserRequest request) {

        PostUserResponse response = new PostUserResponse();

        if (request.getUserDetails().getLogin().equals("") ||
                request.getUserDetails().getName().equals("") ||
                request.getUserDetails().getPassword().equals("") ||
                !request.getUserDetails().getPassword().matches("(?=.*[A-Z])(?=.*\\d).{0,}")
        ) {

            response.setSuccess("false");

            if (request.getUserDetails().getLogin().equals(""))
                response.getError().add("Login is missing");

            if (request.getUserDetails().getName().equals(""))
                response.getError().add("Name is missing");

            if (request.getUserDetails().getPassword().equals(""))
                response.getError().add("Password is missing");

            if (!request.getUserDetails().getPassword().matches("(?=.*[A-Z])(?=.*\\d).{0,}") &&
                    !request.getUserDetails().getPassword().equals(""))
                response.getError().add("Password must contain at least one uppercase letter and one digit");
        }

        else {
            List<String> rolesFromRequest = request.getRole();
            Set<Role> roles = new HashSet<>();
            for (String roleString : rolesFromRequest) {
                Role eachRole = roleRepository.findByName(roleString);
                roles.add(eachRole);
            }

            User user = new User(
                    request.getUserDetails().getLogin(),
                    request.getUserDetails().getName(),
                    request.getUserDetails().getPassword(),
                    roles);

            if (!userService.addUser(user)) {
                response.setSuccess("false");
                response.getError().add("User with stated login already exists");
            }

            else
                response.setSuccess("true");
        }

        return response;
    }

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "UpdateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {

        UpdateUserResponse response = new UpdateUserResponse();

        if (request.getCurrentLogin().equals("") ||
                request.getUpdatedUserDetails().getLogin().equals("") ||
                request.getUpdatedUserDetails().getName().equals("") ||
                request.getUpdatedUserDetails().getPassword().equals("") ||
                !request.getUpdatedUserDetails().getPassword().matches("(?=.*[A-Z])(?=.*\\d).{0,}")
        ) {

            response.setSuccess("false");

            if (request.getCurrentLogin().equals(""))
                response.getError().add("Current login is missing");

            if (request.getUpdatedUserDetails().getLogin().equals(""))
                response.getError().add("New login is missing");

            if (request.getUpdatedUserDetails().getName().equals(""))
                response.getError().add("New name is missing");

            if (request.getUpdatedUserDetails().getPassword().equals(""))
                response.getError().add("New password is missing");

            if (!request.getUpdatedUserDetails().getPassword().matches("(?=.*[A-Z])(?=.*\\d).{0,}") &&
                    !request.getUpdatedUserDetails().getPassword().equals(""))
                response.getError().add("Password must contain at least one uppercase letter and one digit");
        }

        else {
            List<String> rolesFromRequest = request.getRole();
            Set<Role> roles = new HashSet<>();
            for (String roleString : rolesFromRequest) {
                Role eachRole = roleRepository.findByName(roleString);
                roles.add(eachRole);
            }

            User existingUser = userService.getUser(request.getCurrentLogin());

            if (existingUser == null) {
                response.setSuccess("false");
                response.getError().add("User with stated login does not exists");
            }

            else {
                existingUser.setLogin(request.getUpdatedUserDetails().getLogin());
                existingUser.setPassword(request.getUpdatedUserDetails().getPassword());
                existingUser.setName(request.getUpdatedUserDetails().getName());
                if (!roles.isEmpty())
                    existingUser.setRoles(roles);

                if (!userService.updateUser(request.getCurrentLogin(), existingUser)) {
                    response.setSuccess("false");
                    response.getError().add("User with stated login already exists");
                }

                else
                    response.setSuccess("true");
            }
        }

        return response;
    }

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "DeleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {

        DeleteUserResponse response = new DeleteUserResponse();

        if (userService.deleteUserByLogin(request.getLogin())) {
            response.setSuccess("true");
        }
        else {
            response.setSuccess("false");
            response.getError().add("User with stated login does not exist");
        }

        return response;
    }
}
