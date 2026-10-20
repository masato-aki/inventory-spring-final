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
 * 【問8用】品目タイプ条件付き文字結合照会 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task8")
public class Task8RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task8/search?orgId=42&itemIds=10003,10013
     */
    @GetMapping("/search")
    public List<ItemStock> search(
        @RequestParam(name = "orgId", required = false, defaultValue = "42") String orgId,
        @RequestParam(name = "itemIds", required = false, defaultValue = "10003,10013") String itemIds
    ) {
        return inventoryService.findTask8(orgId, itemIds);
    }
}