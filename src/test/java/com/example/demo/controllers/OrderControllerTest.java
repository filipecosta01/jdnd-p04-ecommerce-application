package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.InjectDependencies;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        InjectDependencies.injectObjects(orderController, "userRepository", userRepository);
        InjectDependencies.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void shouldCreateOrderForUser() {
        // given

        // create user
        final long userId = 1L;
        final String username = "test1";
        final String password = "testPassword1";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 1L;
        final String name = "itemTest1";
        final String price = "2.99";
        final String description = "precious item1";
        final Item item = createItem(itemId, name, price, description);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // create cart and add user
        final Cart cart = new Cart();
        final long cartId = 1L;
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());
        cart.setId(cartId);
        user.setCart(cart);

        // when
        final ResponseEntity<UserOrder> response = orderController.submit(username);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

       final UserOrder responseUserOrder = response.getBody();
       assertEquals(cart.getItems().size(), responseUserOrder.getItems().size());
        assertEquals(cart.getTotal(), responseUserOrder.getTotal());
        assertEquals(cart.getUser(), responseUserOrder.getUser());
    }

    @Test
    public void shouldThrowWhenCreateOrderForUserAndUserNotFound() {
        // given

        // create user
        final long userId = 2L;
        final String username = "test2";
        final String password = "testPassword2";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 2L;
        final String name = "itemTest1";
        final String price = "3.99";
        final String description = "precious item2";
        final Item item = createItem(itemId, name, price, description);

        when(userRepository.findByUsername(username)).thenReturn(null);

        // create cart and add user
        final Cart cart = new Cart();
        final long cartId = 2L;
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());
        cart.setId(cartId);
        user.setCart(cart);

        // when
        final ResponseEntity<UserOrder> response = orderController.submit(username);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testCreateOrderForUser() {
        // given

        // create user
        final long userId = 3L;
        final String username = "test3";
        final String password = "testPassword3";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 3L;
        final String name = "itemTest3";
        final String price = "4.99";
        final String description = "precious item3";
        final Item item = createItem(itemId, name, price, description);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // create cart and add user
        final Cart cart = new Cart();
        final long cartId = 3L;
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());
        cart.setId(cartId);
        user.setCart(cart);

        // when
        final ResponseEntity<UserOrder> response = orderController.submit(username);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final UserOrder responseUserOrder = response.getBody();
        assertEquals(cart.getItems().size(), responseUserOrder.getItems().size());
        assertEquals(cart.getTotal(), responseUserOrder.getTotal());
        assertEquals(cart.getUser(), responseUserOrder.getUser());
    }

    @Test
    public void shouldGetOrdersForUser() {
        // given

        // create user
        final long userId = 3L;
        final String username = "test3";
        final String password = "testPassword3";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 3L;
        final String name = "itemTest3";
        final String price = "6.99";
        final String description = "precious item3";
        final Item item = createItem(itemId, name, price, description);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // create cart and add user
        final Cart cart = new Cart();
        final long cartId = 1L;
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());
        cart.setId(cartId);
        user.setCart(cart);

        final UserOrder userOrder = new UserOrder();
        userOrder.setItems(Collections.singletonList(item));
        userOrder.setTotal(item.getPrice());
        userOrder.setUser(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));

        // when
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<UserOrder> responseListUserOrder = response.getBody();
        assertEquals(cart.getItems().size(), responseListUserOrder.size());
    }

    @Test
    public void shouldThrowWhenGetOrdersForUserAndUserNotFound() {
        // given

        // create user
        final long userId = 3L;
        final String username = "test3";
        final String password = "testPassword3";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 3L;
        final String name = "itemTest3";
        final String price = "6.99";
        final String description = "precious item3";
        final Item item = createItem(itemId, name, price, description);

        when(userRepository.findByUsername(username)).thenReturn(null);

        // create cart and add user
        final Cart cart = new Cart();
        final long cartId = 1L;
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(item.getPrice());
        cart.setId(cartId);
        user.setCart(cart);

        final UserOrder userOrder = new UserOrder();
        userOrder.setItems(Collections.singletonList(item));
        userOrder.setTotal(item.getPrice());
        userOrder.setUser(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));

        // when
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    public User createUser(Long id, String username, String password) {
        final User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public Item createItem(Long id, String name, String price, String description) {
        final Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(new BigDecimal(price));
        item.setDescription(description);

        return item;
    }

}
