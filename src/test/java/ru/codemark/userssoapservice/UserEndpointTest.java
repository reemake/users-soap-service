package ru.codemark.userssoapservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import ru.codemark.userssoapservice.entity.Role;
import ru.codemark.userssoapservice.entity.User;
import ru.codemark.userssoapservice.repository.RoleRepository;
import ru.codemark.userssoapservice.repository.UserRepository;
import ru.codemark.userssoapservice.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@WebServiceServerTest
public class UserEndpointTest {

    @Autowired
    private MockWebServiceClient client;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserService userService;

    @Test
    public void postUserSuccessTest() throws IOException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("Admin"));
        roles.add(roleRepository.findByName("Operator"));
        User user = new User("Vasily123", "Vasya", "fsfsKLl3", roles);
        when(userService.addUser(user)).thenReturn(true);

        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login>Vasily123</user:login>" +
                                "<user:password>fsfsKLl3</user:password>" +
                                "<user:name>Vasya</user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>true</ns2:success>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailMissingLoginTest() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login></user:login>" +
                                "<user:password>fsfsKLl3</user:password>" +
                                "<user:name>Vasya</user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Login is missing</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailMissingPasswordTest() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login>Vasily123</user:login>" +
                                "<user:password></user:password>" +
                                "<user:name>Vasya</user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Password is missing</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailMissingNameTest() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login>Vasily123</user:login>" +
                                "<user:password>fsfsKLl3</user:password>" +
                                "<user:name></user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Name is missing</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailMissingEverythingTest() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login></user:login>" +
                                "<user:password></user:password>" +
                                "<user:name></user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Login is missing</ns2:error>" +
                            "<ns2:error>Name is missing</ns2:error>" +
                            "<ns2:error>Password is missing</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailWrongPasswordTest1() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login>Vasily123</user:login>" +
                                "<user:password>fsfsKLl</user:password>" +
                                "<user:name>Vasya</user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Password must contain at least one uppercase letter and one digit</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void postUserFailWrongPasswordTest2() throws IOException {
        StringSource request = new StringSource(
                "<user:PostUserRequest xmlns:user='http://codemark.ru/userssoapservice'>" +
                            "<user:UserDetails>" +
                                "<user:login>Vasily123</user:login>" +
                                "<user:password>ggg33dd</user:password>" +
                                "<user:name>Vasya</user:name>" +
                            "</user:UserDetails>" +
                            "<user:role>Admin</user:role>" +
                            "<user:role>Operator</user:role>" +
                        "</user:PostUserRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:PostUserResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:success>false</ns2:success>" +
                            "<ns2:error>Password must contain at least one uppercase letter and one digit</ns2:error>" +
                        "</ns2:PostUserResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("wsdl/users.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    public void getUsersSuccessTest() throws IOException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("Admin"));
        roles.add(roleRepository.findByName("Operator"));
        User user1 = new User("Vasily123", "Vasya", "fsfsKLl3", roles);
        User user2 = new User("Petr321", "Petya", "ggg333dDd", roles);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userService.getUsers()).thenReturn(users);

        StringSource request = new StringSource(
                "<user:GetUsersRequest xmlns:user='http://codemark.ru/userssoapservice'/>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:GetUsersResponse xmlns:ns2='http://codemark.ru/userssoapservice'>" +
                            "<ns2:UsersDetails>" +
                                "<ns2:login>Vasily123</ns2:login>" +
                                "<ns2:password>fsfsKLl3</ns2:password>" +
                                "<ns2:name>Vasya</ns2:name>" +
                            "</ns2:UsersDetails>" +
                            "<ns2:UsersDetails>" +
                                "<ns2:login>Petr321</ns2:login>" +
                                "<ns2:password>ggg333dDd</ns2:password>" +
                                "<ns2:name>Petya</ns2:name>" +
                            "</ns2:UsersDetails>" +
                        "</ns2:GetUsersResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(payload(expectedResponse));
    }

}
