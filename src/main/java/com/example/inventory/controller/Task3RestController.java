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
 * 課題3：複数品目・組織照会 APIコントローラー
 * 
 * 指定された品目IDリスト（IN句検索）に基づいて対象品目の在庫・組織情報を抽出し、
 * JSON形式で返却します。
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task3")
public class Task3RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 複数品目ID指定による品目・組織情報一覧取得
     *
     * @param itemIds カンマ区切りの品目ID文字列（例: "10005,10008" / 任意）
     * @return 該当する品目・組織情報リスト
     */
    @GetMapping("/search")
    public List<ItemStock> search(@RequestParam(name = "itemIds", required = false) String itemIds) {
        return inventoryService.findTask3(itemIds);
    }
}