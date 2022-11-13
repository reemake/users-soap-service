package ru.codemark.userssoapservice.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.codemark.userssoapservice.*;
import ru.codemark.userssoapservice.GetUserRequest;
import ru.codemark.userssoapservice.GetUserResponse;
import ru.codemark.userssoapservice.GetUsersRequest;
import ru.codemark.userssoapservice.GetUsersResponse;
import ru.codemark.userssoapservice.UserDetails;
import ru.codemark.userssoapservice.entity.Role;
import ru.codemark.userssoapservice.entity.User;
import ru.codemark.userssoapservice.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse processUserDetailsRequest(@RequestPayload GetUserRequest request) {

        GetUserResponse response = new GetUserResponse();
        UserDetails userDetails = new UserDetails();
        User existingUser = userService.getUser(request.getLogin());

        userDetails.setLogin(existingUser.getLogin());
        userDetails.setName(existingUser.getName());
        userDetails.setPassword(existingUser.getPassword());

        List<Role> existingUserRolesList = new ArrayList<>(existingUser.getRoles());
        for (Role role : existingUserRolesList) {
            response.getRoles().add(role.getName());
        }

        response.setUserDetails(userDetails);

        return response;
    }

    @PayloadRoot(namespace = "http://codemark.ru/userssoapservice", localPart = "GetUsersRequest")
    @ResponsePayload
    public GetUsersResponse processUsersDetailsRequest(@RequestPayload GetUsersRequest request) {

        GetUsersResponse response = new GetUsersResponse();

        List<User> users = userService.getUsers();
        for (User user: users) {
            UserDetails userDetails = new UserDetails();
            userDetails.setLogin(user.getLogin());
            userDetails.setName(user.getName());
            userDetails.setPassword(user.getPassword());

            response.getUsersDetails().add(userDetails);
        }

        return response;
    }
}
