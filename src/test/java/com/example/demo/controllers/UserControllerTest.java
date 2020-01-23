package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.utils.InjectDependencies;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        InjectDependencies.injectObjects(userController, "userRepository", userRepository);
        InjectDependencies.injectObjects(userController, "cartRepository", cartRepository);
        InjectDependencies.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void shouldCreateUser() {
        // given
        final String username = "test";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        // when
        final ResponseEntity<User> response = createUser(username, password, mirrorPassword);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final User userResponse = response.getBody();
        assertNotNull(userResponse);
        assertEquals(0, userResponse.getId());
        assertEquals("test", userResponse.getUsername());
        assertEquals("hashedPassword", userResponse.getPassword());
    }

    @Test
    public void shouldNotCreateUserWhenPasswordIsEmpty() {
        // given
        final String username = "test2";
        final String password = "";
        final String mirrorPassword = "";

        // when
        final ResponseEntity<User> response = createUser(username, password, mirrorPassword);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldNotCreateUserWhenPasswordLengthBelowEight() {
        // given
        final String username = "test3";
        final String password = "bla1";
        final String mirrorPassword = "bla1";

        // when
        final ResponseEntity<User> response = createUser(username, password, mirrorPassword);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldNotCreateUserWhenConfirmationPasswordIsDifferent() {
        // given
        final String username = "test4";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword2";

        // when
        final ResponseEntity<User> response = createUser(username, password, mirrorPassword);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void shouldFindUserById() {
        // given
        final String username = "test5";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");
        final ResponseEntity<User> createdUser = createUser(username, password, mirrorPassword);

        // when
        when(userRepository.findById(0L)).thenReturn(Optional.of(createdUser.getBody()));
        final ResponseEntity<User> response = userController.findById(0L);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final User userResponse = response.getBody();
        final User createdUserResponse = createdUser.getBody();
        assertEquals(createdUserResponse.getId(), userResponse.getId());
    }

    @Test
    public void shouldNotFindUserById() {
        // given
        final String username = "test6";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");
        createUser(username, password, mirrorPassword);

        // when
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        final ResponseEntity<User> response = userController.findById(0L);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldFindUserByUsername() {
        // given
        final String username = "test7";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");
        final ResponseEntity<User> createdUser = createUser(username, password, mirrorPassword);

        // when
        when(userRepository.findByUsername(username)).thenReturn(createdUser.getBody());
        final ResponseEntity<User> response = userController.findByUserName(username);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final User userResponse = response.getBody();
        final User createdUserResponse = createdUser.getBody();
        assertEquals(createdUserResponse.getId(), userResponse.getId());
        assertEquals(createdUserResponse.getUsername(), userResponse.getUsername());
    }

    @Test
    public void shouldNotFindUserByUsername() {
        // given
        final String username = "test8";
        final String password = "testPassword";
        final String mirrorPassword = "testPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");
        final ResponseEntity<User> createdUser = createUser(username, password, mirrorPassword);

        // when
        when(userRepository.findByUsername(username)).thenReturn(null);
        final ResponseEntity<User> response = userController.findByUserName(username);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    ResponseEntity<User> createUser(String username, String password, String mirrorPassword) {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setMirrorPassword(mirrorPassword);

        return userController.createUser(userRequest);
    }
}
