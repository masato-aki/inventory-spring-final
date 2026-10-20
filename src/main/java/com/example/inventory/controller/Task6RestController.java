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
 * 【問6用】組織別品目マスタ件数集計 REST コントローラー
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task6")
public class Task6RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * URL例: http://localhost:8081/api/task6/search?suffix=9
     */
    @GetMapping("/search")
    public List<ItemStock> search(@RequestParam(name = "suffix", required = false, defaultValue = "9") String suffix) {
        return inventoryService.findTask6(suffix);
    }
}