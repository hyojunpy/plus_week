package com.example.demo.controller;

import com.example.demo.dto.ItemRequestDto;
import com.example.demo.service.ItemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 물건 생성
    @PostMapping
    public void createItem(@RequestBody ItemRequestDto itemRequestDto) {
         itemService.createItem(itemRequestDto.getName(),
                                itemRequestDto.getDescription(),
                                itemRequestDto.getOwnerId(),
                                itemRequestDto.getManagerId());
    }
}
