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
 * ============================================================================
 * 【クラスの役割】問1専用 API Rest Controller (総合受付 / 窓口)
 * ----------------------------------------------------------------------------
 *  React（フロントエンド）からの HTTP リクエストを受け取る「受付窓口」です。
 *  画面から送られてきたパラメータ（組織IDなど）を受け取り、
 *  裏方の Repository（職人）へ処理を依頼して、取得結果を JSON 形式で React へ返します。
 * ============================================================================
 */
// ① CORS（異ポート間通信）の許可設定
//    origins = "*" だと認証情報エラーになるため、originPatterns = "*" を使用して
//    React (http://localhost:5173) からのアクセスを安全に許可します。
@CrossOrigin(originPatterns = "*")

// ② REST API 用のコントローラーであることを宣言
//    戻り値（List<ItemStock> など）を自動的に JSON 形式に変換してブラウザに返却します。
@RestController

// ③ このクラス全体のベースとなる URL パスを指定
//    例: http://localhost:8081/api/... へのリクエストをこのクラスで扱います。
@RequestMapping("/api")
public class Task1RestController {

    /**
     * DB操作を行う Repository（職人クラス）を自動的に注入（連携）します。
     * new InventoryRepository() と自分で書かなくても Spring が自動準備してくれます。
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 【メソッドの役割】問1：組織検索リクエストを受信して結果を返す
     * ------------------------------------------------------------------------
     *  React 側の axios.get('http://localhost:8081/api/task1?orgId=43') 
     *  という呼び出し（GETリクエスト）に対応します。
     *
     * @param orgId  URLのパラメータ "?orgId=43" から取得（未指定でもエラーにしないため required = false）
     * @param itemId URLのパラメータ "?itemId=10002" から取得（任意パラメータ）
     * @return 検索結果（ItemStockオブジェクトのリスト。自動でJSONに変換されます）
     */
    @GetMapping("/task1") // http://localhost:8081/api/task1 への GET 通信を受け付ける
    public List<ItemStock> getTask1(
        @RequestParam(name = "orgId", required = false) String orgId,   // 組織IDを受け取る
        @RequestParam(name = "itemId", required = false) String itemId  // 商品IDを受け取る
    ) {
        // 実際の DB 検索処理は Repository（職人）に丸投げして、その結果をそのまま React へ返す
        return inventoryRepository.findItemStock(orgId, itemId);
        
    }
}