package com.example.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.entity.ItemStock;
import com.example.inventory.service.InventoryService;

/**
 * 【問10用】納入先別・月別発注ヘッダ件数集計 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task10")
public class Task10RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task10/search
     */
    @GetMapping("/search")
    public List<ItemStock> search() {
        return inventoryService.findTask10();
    }
}