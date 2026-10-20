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
 * 【クラスの役割】問4専用 API Rest Controller (受付窓口)
 * ----------------------------------------------------------------------------
 *  Reactからの問4用リクエスト (/api/task4/search) を受け取り、
 *  InventoryService (司令塔) に処理を依頼して JSON 形式で結果を返却します。
 * ============================================================================
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/task4")
public class Task4RestController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 【問4用API】組織別品目原価照会API
     *  URL例: http://localhost:8081/api/task4/search?orgId=44&itemId=10005
     */
    @GetMapping("/search")
    public List<ItemStock> search(
        @RequestParam(name = "orgId", required = false) String orgId,
        @RequestParam(name = "itemId", required = false) String itemId
    ) {
        return inventoryService.findTask4(orgId, itemId);
    }
}