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
 * 課題2：組織コード・品目コード取得 APIコントローラー
 * 
 * フロントエンドからの検索リクエストを受け付け、
 * 組織IDおよび品目IDに合致する組織情報・品目情報をJSON形式で返却します。
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task2")
public class Task2RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 組織ID・品目ID指定によるコード情報一覧取得
     *
     * @param orgId 組織ID（任意）
     * @param itemId 品目ID（任意）
     * @return 該当する組織コード・品目コードを含むItemStockリスト
     */
    @GetMapping("/search")
    public List<ItemStock> search(
            @RequestParam(name = "orgId", required = false) String orgId,
            @RequestParam(name = "itemId", required = false) String itemId) {
        return inventoryService.findTask2(orgId, itemId);
    }
}