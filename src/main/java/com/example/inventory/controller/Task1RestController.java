package com.example.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.entity.ItemStock;
import com.example.inventory.repository.InventoryRepository;

/**
 * 課題1：組織・品目在庫検索 APIコントローラー
 * 
 * 指定された組織IDおよび品目IDに基づいて在庫情報を検索し、JSON形式で返却します。
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api")
public class Task1RestController {

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 組織・品目条件による在庫一覧取得
     *
     * @param orgId 組織ID（任意）
     * @param itemId 品目ID（任意）
     * @return 検索条件に合致する在庫情報リスト
     */
    @GetMapping("/task1")
    public List<ItemStock> getTask1(
            @RequestParam(name = "orgId", required = false) String orgId,
            @RequestParam(name = "itemId", required = false) String itemId) {
        return inventoryRepository.findItemStock(orgId, itemId);
    }
}