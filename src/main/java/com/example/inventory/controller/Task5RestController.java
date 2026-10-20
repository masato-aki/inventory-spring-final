package com.example.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.entity.ItemStock;
import com.example.inventory.service.InventoryService;

/**
 * 【問5用】手持在庫照会 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task5")
public class Task5RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task5/search?orgId=44&itemId=10002
     */
    @GetMapping("/search")
    public List<ItemStock> search(
        @RequestParam(name = "orgId", required = false) String orgId,
        @RequestParam(name = "itemId", required = false) String itemId
    ) {
        return inventoryService.findTask5(orgId, itemId);
    }
}