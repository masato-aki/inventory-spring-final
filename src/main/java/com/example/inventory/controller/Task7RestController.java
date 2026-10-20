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
 * 【問7用】品目作成・更新履歴照会 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task7")
public class Task7RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task7/search?orgId=43&itemId=502357
     */
    @GetMapping("/search")
    public List<ItemStock> search(
        @RequestParam(name = "orgId", required = false) String orgId,
        @RequestParam(name = "itemId", required = false) String itemId
    ) {
        return inventoryService.findTask7(orgId, itemId);
    }
}