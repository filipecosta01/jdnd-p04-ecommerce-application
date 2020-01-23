package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        InjectDependencies.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void shouldReturnItems() {
        // given
        final Long id = 1L;
        final String name = "item1";
        final String price = "2.99";
        final String description = "my precious item1";
        final Item item1 = createItem(id, name, price, description);
        final List<Item> items = Collections.singletonList(item1);
        when(itemRepository.findAll()).thenReturn(items);

        // when
        final ResponseEntity<List<Item>> response = itemController.getItems();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<Item> currentItems = response.getBody();
        assertNotNull(currentItems);
        assertEquals(1, currentItems.size());

        final Item onlyItem = currentItems.get(0);
        assertEquals(onlyItem.getId(), id);
        assertEquals(onlyItem.getPrice(), new BigDecimal(price));
        assertEquals(onlyItem.getName(), name);
        assertEquals(onlyItem.getDescription(), description);
    }

    @Test
    public void shouldReturnEmptyItems() {
        // given
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final ResponseEntity<List<Item>> response = itemController.getItems();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<Item> currentItems = response.getBody();
        assertNotNull(currentItems);
        assertEquals(0, currentItems.size());
    }

    @Test
    public void shouldReturnItemById() {
        // given
        final Long id = 1L;
        final String name = "item2";
        final String price = "4.99";
        final String description = "my precious item2";
        final Item item1 = createItem(id, name, price, description);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item1));

        // when
        final ResponseEntity<Item> response = itemController.getItemById(id);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final Item onlyItem = response.getBody();
        assertNotNull(onlyItem);
        assertEquals(onlyItem.getId(), id);
        assertEquals(onlyItem.getPrice(), new BigDecimal(price));
        assertEquals(onlyItem.getName(), name);
        assertEquals(onlyItem.getDescription(), description);
    }

    @Test
    public void shouldReturnEmptyItemById() {
        // given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        final ResponseEntity<Item> response = itemController.getItemById(1L);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnItemsByName() {
        // given
        final Long id = 1L;
        final String name = "item3";
        final String price = "3.99";
        final String description = "my precious item3";
        final Item item1 = createItem(id, name, price, description);
        final List<Item> items = Collections.singletonList(item1);
        when(itemRepository.findByName(name)).thenReturn(items);

        // when
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(name);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<Item> currentItems = response.getBody();
        assertNotNull(currentItems);
        assertEquals(1, currentItems.size());

        final Item onlyItem = currentItems.get(0);
        assertEquals(onlyItem.getId(), id);
        assertEquals(onlyItem.getPrice(), new BigDecimal(price));
        assertEquals(onlyItem.getName(), name);
        assertEquals(onlyItem.getDescription(), description);
    }

    @Test
    public void shouldReturnEmptyItemsByName() {
        // given
        when(itemRepository.findByName("bla")).thenReturn(Collections.emptyList());

        // when
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("bla");

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnEmptyItemsByNameNull() {
        // given
        when(itemRepository.findByName("bla")).thenReturn(null);

        // when
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("bla");

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    Item createItem(Long id, String name, String price, String description) {
        final Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(new BigDecimal(price));
        item.setDescription(description);
        return item;
    }
}
