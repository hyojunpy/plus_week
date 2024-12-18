package com.example.demo.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void createItemTest() {
        User owner = new User();
        User manage = new User();

        Item item = new Item("name", "설명", owner, manage);
        System.out.println(item.getStatus());

        assertNotNull(item.getStatus(), "아이템의 상태가 null입니다.");
    }
}
