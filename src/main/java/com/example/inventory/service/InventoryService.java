package com.example.inventory.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory.entity.ItemStock;
import com.example.inventory.repository.InventoryRepository;

/**
 * ============================================================================
 * 【クラスの役割】ビジネスロジック・仲介処理クラス (Service層)
 * ----------------------------------------------------------------------------
 *  ■ どんな仕事をしているクラス？
 *    画面からの受付口である「Controller（コントローラー）」と、
 *    データベースを操作する「Repository（リポジトリ）」の間を取り持つ【司令塔】です。
 * 
 *  ■ 主な役割：
 *    1. 入力チェック（値が空っぽ・nullでないかなどの確認）
 *    2. 必要に応じたデータの加工や計算処理
 *    3. Repositoryを呼び出してデータベースからデータを取得・連携する
 * ============================================================================
 */
@Service // Spring Bootに「ビジネスロジックを担当するServiceクラスだよ」と教えるアノテーション
public class InventoryService {

    @Autowired // データベース操作を行うRepositoryを自動セットアップ
    private InventoryRepository inventoryRepository;

    /**
     * ------------------------------------------------------------------------
     * 🟦【問1用メソッド】組織情報・在庫検索処理
     * ------------------------------------------------------------------------
     *  画面（Controller）から渡された組織IDをもとに、
     *  Repositoryを呼び出してデータベースから組織情報を取得します。
     */
    public List<ItemStock> searchItemStock(String orgId, String itemId) {
        return inventoryRepository.findItemStock(orgId, itemId);
    }

    /* ========================================================================
     * 🟩 【問い2で使うメソッド】組織IDと品目IDによる組織コード＆品目コード取得処理
     * ------------------------------------------------------------------------
     *  【処理の流れ】
     *   1. 画面から送られてきた「組織ID」と「品目ID」のチェックを行う
     *   2. どちらか一方でも未入力（nullまたは空文字）の場合は、DBに問い合わせず空のリストを返す
     *   3. 両方正しく入力されている場合のみ、Repositoryを呼んでDBからデータを取得する
     * ======================================================================== */
    public List<ItemStock> findTask2(String orgId, String itemId) {
        
        // 入力値のチェック：組織IDまたは品目IDが空の場合は処理を行わず空リストを返却
        if (orgId == null || orgId.trim().isEmpty() || itemId == null || itemId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        // 問題なければデータベース操作クラス(Repository)へ検索依頼を出す
        return inventoryRepository.findItemOrg(orgId, itemId);
    }
    /* ========================================================================
     * 🟨 【問3用メソッド】複数品目・組織照会のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask3(String itemIds) {
        return inventoryRepository.findTask3(itemIds);
    }
    /* ========================================================================
     * 🟦 【問4用メソッド】組織別品目原価照会のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask4(String orgId, String itemId) {
        return inventoryRepository.findTask4(orgId, itemId);
    }
    /* ========================================================================
     * 🟪 【問5用メソッド】手持在庫照会のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask5(String orgId, String itemId) {
        return inventoryRepository.findTask5(orgId, itemId);
    }
    /* ========================================================================
     * 🟧 【問6用メソッド】組織別品目件数集計のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask6(String suffix) {
        return inventoryRepository.findTask6(suffix);
    }
    /* ========================================================================
     * 🟥 【問7用メソッド】品目作成・更新履歴照会のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask7(String orgId, String itemId) {
        return inventoryRepository.findTask7(orgId, itemId);
    }
    /* ========================================================================
     * 🟪 【問8用メソッド】品目タイプ条件付き文字結合のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask8(String orgId, String itemIdsStr) {
        return inventoryRepository.findTask8(orgId, itemIdsStr);
    }
   
    /* ========================================================================
     * 🟦 【問9用メソッド】工順なし半組立品集計のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask9() {
        return inventoryRepository.findTask9();
    }
    /* ========================================================================
     * 🟨 【問10用メソッド】納入先別・月別発注集計のビジネスロジック
     * ======================================================================== */
    public List<ItemStock> findTask10() {
        return inventoryRepository.findTask10();
    }
}