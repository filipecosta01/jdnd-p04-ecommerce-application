package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.InjectDependencies;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();

        InjectDependencies.injectObjects(cartController, "userRepository", userRepository);
        InjectDependencies.injectObjects(cartController, "cartRepository", cartRepository);
        InjectDependencies.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void shouldAddToCart() {
        // given

        // create user
        final long userId = 1L;
        final String username = "test1";
        final String password = "testPassword1";
        final User user = createUser(userId, username, password);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // create Item
        final long itemId = 1L;
        final String name = "itemTest1";
        final String price = "2.99";
        final String description = "precious item1";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // create cart and add user
        final Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.addToCart(cartRequest);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(1, cartResponse.getItems().size());

        assertEquals(new BigDecimal("2.99"), cartResponse.getTotal());
        assertEquals(user, cartResponse.getUser());
    }

    @Test
    public void shouldThrowWhenAddToCartAndUserNotFound() {
        // given

        // create user
        final long userId = 2L;
        final String username = "test2";
        final String password = "testPassword2";
        final User user = createUser(userId, username, password);

        when(userRepository.findByUsername(username)).thenReturn(null);

        // create Item
        final long itemId = 2L;
        final String name = "itemTest2";
        final String price = "3.99";
        final String description = "precious item2";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // create cart and add user
        final Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.addToCart(cartRequest);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldThrowWhenAddToCartAndItemNotFound() {
        // given

        // create user
        final long userId = 3L;
        final String username = "test3";
        final String password = "testPassword3";
        final User user = createUser(userId, username, password);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // create Item
        final long itemId = 3L;
        final String name = "itemTest3";
        final String price = "5.99";
        final String description = "precious item3";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // create cart and add user
        final Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.addToCart(cartRequest);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldRemoveFromCart() {
        // given

        // create user
        final long userId = 4L;
        final String username = "test4";
        final String password = "testPassword4";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 4L;
        final String name = "itemTest4";
        final String price = "6.99";
        final String description = "precious item4";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // create cart and add user
        final Cart cart = new Cart();
        // avoid issues with unmodifiable wrapper (fixed-size list)
        cart.setItems(new LinkedList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.removeFromCart(cartRequest);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(0, cartResponse.getItems().size());
    }

    @Test
    public void shouldThrowWhenRemoveFromCartAndUserNotFound() {
        // given

        // create user
        final long userId = 4L;
        final String username = "test4";
        final String password = "testPassword4";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 4L;
        final String name = "itemTest4";
        final String price = "6.99";
        final String description = "precious item4";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // create cart and add user
        final Cart cart = new Cart();
        // avoid issues with unmodifiable wrapper (fixed-size list)
        cart.setItems(new LinkedList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(null);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.removeFromCart(cartRequest);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldThrowWhenRemoveFromCartAndItemNotFound() {
        // given

        // create user
        final long userId = 4L;
        final String username = "test4";
        final String password = "testPassword4";
        final User user = createUser(userId, username, password);

        // create Item
        final long itemId = 4L;
        final String name = "itemTest4";
        final String price = "6.99";
        final String description = "precious item4";
        final Item item = createItem(itemId, name, price, description);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // create cart and add user
        final Cart cart = new Cart();
        // avoid issues with unmodifiable wrapper (fixed-size list)
        cart.setItems(new LinkedList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // request body
        final ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(1);
        cartRequest.setUsername(username);

        // when
        final ResponseEntity<Cart> response = cartController.removeFromCart(cartRequest);

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
