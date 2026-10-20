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
 * 【問3用】複数品目・組織照会 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task3")
public class Task3RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task3/search?itemIds=10005,10008
     */
    @GetMapping("/search")
    public List<ItemStock> search(@RequestParam(name = "itemIds", required = false) String itemIds) {
        return inventoryService.findTask3(itemIds);
    }
}