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
 * ============================================================================
 * 【クラスの役割】問い2専用 API Rest Controller (受付窓口)
 * ----------------------------------------------------------------------------
 *  React（フロントエンド）からの問い2用リクエスト (/api/task2/search) を受け取り、
 *  InventoryService (司令塔) に処理を依頼して JSON 形式で結果を返却します。
 * ============================================================================
 */
@CrossOrigin(originPatterns = "*") // ReactからのCORS接続を許可
@RestController
@RequestMapping("/api/task2")
public class Task2RestController {

    @Autowired
    private InventoryService inventoryService; // 司令塔(Service)と連携

    /**
     * 【問い2用API】組織IDと品目IDから組織コード＆品目コードを取得する
     *  URL例: http://localhost:8081/api/task2/search?orgId=43&itemId=10004
     */
    @GetMapping("/search")
    public List<ItemStock> search(
        @RequestParam(name = "orgId", required = false) String orgId,
        @RequestParam(name = "itemId", required = false) String itemId
    ) {
        // ビジネスロジック層（Service）に処理を委譲
        return inventoryService.findTask2(orgId, itemId);
    }
}