package com.example.inventory.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.ItemStock;

/**
 * ============================================================================
 * 【クラスの役割】データベース操作クラス (Repository / DAO)
 * ----------------------------------------------------------------------------
 *  MySQL データベース（`mtl_parameters` テーブル等）へ直接 SQL を発行し、
 *  取得したデータを Java のオブジェクト（`ItemStock`）に変換して返すクラスです。
 *  Spring の `JdbcTemplate` を使うことで、面倒な接続・切断処理を自動化しています。
 * ============================================================================
 */
@Repository // Spring Boot に「データベースアクセス用のクラスだよ」と認識させるアノテーション
public class InventoryRepository {

    /**
     * Spring JDBC が提供する SQL 実行用便利クラス
     * DB接続情報（application.properties）を基に自動セットアップされます。
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 【問1用メソッド】組織情報（組織ID・組織コード）を DB から取得する
     * ------------------------------------------------------------------------
     * @param orgId  画面から入力された組織ID (例: "43") ※未入力の場合は null または空文字
     * @param itemId 画面から入力された商品ID (必要に応じて使用)
     * @return 検索結果の ItemStock リスト
     */
    public List<ItemStock> findItemStock(String orgId, String itemId) {
        
        // ① 基本となる SQL 文（基本テーブル: mtl_parameters）
        String sql = "SELECT organization_id, organization_code FROM mtl_parameters";

        // ② 組織ID (orgId) が入力されている場合の処理 (WHERE 条件付き検索)
        if (orgId != null && !orgId.trim().isEmpty()) {
            sql += " WHERE organization_id = ?";
            
            // SQLを実行し、ResultSet(rs) から 1行ずつ ItemStock にマッピングして返却
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                ItemStock stock = new ItemStock();
                stock.setOrganizationId(rs.getString("organization_id"));   // DBの organization_id をセット
                stock.setOrganizationCode(rs.getString("organization_code")); // DBの organization_code をセット
                return stock;
            }, orgId.trim()); // 「?」に組織IDを当てはめる
        }

        // ③ 組織IDが未入力の場合の処理 (全件検索)
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationId(rs.getString("organization_id"));
            stock.setOrganizationCode(rs.getString("organization_code"));
            return stock;
        });
    }

    /* ========================================================================
     * 🟩 【問2で使うメソッド】組織IDと品目IDから、組織コード＆品目コードを取得する
     * ------------------------------------------------------------------------
     * 結合テーブル: mtl_system_items_b (i) JOIN mtl_parameters (p)
     * ======================================================================== */
    public List<ItemStock> findItemOrg(String orgId, String itemId) {
        
        // 問い2用のSQL文（組織コードと品目コードを結合検索）
        String sql = "SELECT p.organization_code, i.segment1 " +
                     "FROM mtl_system_items_b i " +
                     "JOIN mtl_parameters p ON i.organization_id = p.organization_id " +
                     "WHERE i.organization_id = ? AND i.inventory_item_id = ?";

        // SQLを実行し、取得結果を ItemStock にセットして返却
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code")); // 組織コード
            stock.setSegment1(rs.getString("segment1"));                  // 品目コード
            return stock;
        }, orgId.trim(), itemId.trim());
    }
    /* ========================================================================
     * 🟨 【問3用メソッド】複数品目・組織照会処理（IN句で複数品目ID検索）
     * ------------------------------------------------------------------------
     * @param itemIds カンマ区切りの品目ID（例: "10005,10008"）
     * ======================================================================== */
    public List<ItemStock> findTask3(String itemIds) {
        if (itemIds == null || itemIds.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        String[] ids = itemIds.split(",");
        List<String> cleanIds = new java.util.ArrayList<>();
        for (String id : ids) {
            if (!id.trim().isEmpty()) {
                cleanIds.add(id.trim());
            }
        }
        if (cleanIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < cleanIds.size(); i++) {
            inClause.append("?");
            if (i < cleanIds.size() - 1) {
                inClause.append(", ");
            }
        }

        // 先輩の正解SQLに合わせて HR_ALL_ORGANIZATION_UNITS を JOIN し、
        // 組織コード(DESC)、品目コード(ASC) でソート
        String sql = "SELECT " +
                     "  mp.organization_code AS organization_code, " +
                     "  ou.name AS organization_name, " +
                     "  msi.segment1 AS segment1 " +
                     "FROM mtl_parameters mp " +
                     "JOIN mtl_system_items_b msi " +
                     "  ON mp.organization_id = msi.organization_id " +
                     "JOIN hr_all_organization_units ou " +
                     "  ON mp.organization_id = ou.organization_id " +
                     "WHERE msi.inventory_item_id IN (" + inClause.toString() + ") " +
                     "ORDER BY mp.organization_code DESC, msi.segment1 ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setOrganizationName(rs.getString("organization_name"));
            stock.setSegment1(rs.getString("segment1"));
            return stock;
        }, cleanIds.toArray());
    }
    /* ========================================================================
     * 🟦 【問4用メソッド】組織別品目原価照会処理
     * ------------------------------------------------------------------------
     * cst_item_costs (c) 
     * JOIN mtl_system_items_b (i) ON c.inventory_item_id = i.inventory_item_id AND c.organization_id = i.organization_id
     * JOIN mtl_parameters (p) ON c.organization_id = p.organization_id
     * ======================================================================== */
    public List<ItemStock> findTask4(String orgId, String itemId) {
        if (orgId == null || orgId.trim().isEmpty() || itemId == null || itemId.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        String sql = "SELECT p.organization_code, i.segment1, ROUND(c.item_cost) AS item_cost " +
                     "FROM cst_item_costs c " +
                     "JOIN mtl_system_items_b i ON c.inventory_item_id = i.inventory_item_id AND c.organization_id = i.organization_id " +
                     "JOIN mtl_parameters p ON c.organization_id = p.organization_id " +
                     "WHERE c.organization_id = ? AND c.inventory_item_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setSegment1(rs.getString("segment1"));
            stock.setItemCost(rs.getInt("item_cost"));
            return stock;
        }, orgId.trim(), itemId.trim());
    }
    /* ========================================================================
     * 🟪 【問5用メソッド】手持在庫照会処理
     * ------------------------------------------------------------------------
     * mtl_onhand_quantities_detail (q)
     * JOIN mtl_system_items_b (i) ON q.inventory_item_id = i.inventory_item_id AND q.organization_id = i.organization_id
     * JOIN mtl_parameters (p) ON q.organization_id = p.organization_id
     * ======================================================================== */
    public List<ItemStock> findTask5(String orgId, String itemId) {
        String sql = "SELECT " +
                     "  mp.organization_code AS organization_code, " +
                     "  msib.segment1 AS segment1, " +
                     "  moqd.subinventory_code AS subinventory_code, " +
                     "  moqd.transaction_uom_code AS uom_code, " +
                     "  SUM(moqd.transaction_quantity) AS primary_quantity " +
                     "FROM mtl_parameters mp, " +
                     "     mtl_system_items_b msib, " +
                     "     mtl_onhand_quantities_detail moqd " +
                     "WHERE mp.organization_id = ? " +
                     "  AND msib.inventory_item_id = ? " +
                     "  AND mp.organization_id = msib.organization_id " +
                     "  AND mp.organization_id = moqd.organization_id " +
                     "  AND msib.inventory_item_id = moqd.inventory_item_id " +
                     "GROUP BY " +
                     "  mp.organization_code, " +
                     "  msib.segment1, " +
                     "  moqd.subinventory_code, " +
                     "  moqd.transaction_uom_code";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setSegment1(rs.getString("segment1"));
            stock.setSubinventoryCode(rs.getString("subinventory_code"));
            stock.setUomCode(rs.getString("uom_code"));
            stock.setPrimaryQuantity(rs.getInt("primary_quantity"));
            return stock;
        }, Integer.parseInt(orgId), Integer.parseInt(itemId));
    }
    /* ========================================================================
     * 🟧 【問6用メソッド】組織コード末尾指定・品目件数集計処理
     * ------------------------------------------------------------------------
     * 抽出条件: 組織コードの末尾が入力値（デフォルト: '9'）
     * 結合: mtl_parameters (p)
     *   JOIN hr_all_organization_units (o) ON p.organization_id = o.organization_id
     *   JOIN mtl_system_items_b (i) ON p.organization_id = i.organization_id
     * 集計: GROUP BY p.organization_code, o.name
     * ======================================================================== */
    public List<ItemStock> findTask6(String suffix) {
        String targetSuffix = (suffix != null && !suffix.trim().isEmpty()) ? suffix.trim() : "9";

        String sql = "SELECT p.organization_code, o.name AS organization_name, COUNT(i.inventory_item_id) AS item_count " +
                     "FROM mtl_parameters p " +
                     "JOIN hr_all_organization_units o ON p.organization_id = o.organization_id " +
                     "JOIN mtl_system_items_b i ON p.organization_id = i.organization_id " +
                     "WHERE p.organization_code LIKE ? " +
                     "GROUP BY p.organization_code, o.name " +
                     "ORDER BY p.organization_code DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setOrganizationName(rs.getString("organization_name"));
            stock.setItemCount(rs.getInt("item_count"));
            return stock;
        }, "%" + targetSuffix);
    }
    /* ========================================================================
     * 🟥 【問7用メソッド】品目作成・更新履歴照会処理
     * ------------------------------------------------------------------------
     * 【SQLの設計とエラー回避の背景】
     * 1. mtl_system_items_b テーブルの更新者IDカラムは `LAST_UPDATE_BY`（Dなし）のため、
     *    `i.last_update_by = u2.user_id` で JOIN しています。
     * 2. fnd_user に該当ユーザが存在しない（またはNULL）場合に画面崩れやエラーを
     *    防ぐため、IFNULL(u.user_name, '01ユーザ') で補完処理を行っています。
     * 3. DATE_FORMAT(..., '%Y/%m/%d') により課題仕様通りの時分秒切捨(YYYY/MM/DD)を行っています。
     * ======================================================================== */
    public List<ItemStock> findTask7(String orgId, String itemId) {
        if (orgId == null || orgId.trim().isEmpty() || itemId == null || itemId.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        String sql = "SELECT p.organization_code, i.segment1, " +
                     "DATE_FORMAT(i.creation_date, '%Y/%m/%d') AS creation_date, " +
                     "IFNULL(u1.user_name, '01ユーザ') AS created_by_user, " +
                     "DATE_FORMAT(i.last_update_date, '%Y/%m/%d') AS last_update_date, " +
                     "IFNULL(u2.user_name, '01ユーザ') AS last_updated_by_user " +
                     "FROM mtl_system_items_b i " +
                     "JOIN mtl_parameters p ON i.organization_id = p.organization_id " +
                     "LEFT JOIN fnd_user u1 ON i.created_by = u1.user_id " +
                     "LEFT JOIN fnd_user u2 ON i.last_update_by = u2.user_id " +
                     "WHERE i.organization_id = ? AND i.inventory_item_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setSegment1(rs.getString("segment1"));
            stock.setCreationDate(rs.getString("creation_date"));
            stock.setCreatedByUser(rs.getString("created_by_user"));
            stock.setLastUpdateDate(rs.getString("last_update_date"));
            stock.setLastUpdatedByUser(rs.getString("last_updated_by_user"));
            return stock;
        }, orgId.trim(), itemId.trim());
    }
    /* ========================================================================
     * 🟪 【問8用メソッド】品目タイプ条件付き文字列結合照会
     * ------------------------------------------------------------------------
     *  CASE WHEN flv.meaning = '外注加工品目' 
     *       THEN CONCAT(flv.meaning, '_', msib.segment1)
     *       ELSE flv.meaning 
     *  END AS item_type_name
     * ======================================================================== */
    public List<ItemStock> findTask8(String orgId, String itemIdsStr) {
        if (orgId == null || orgId.trim().isEmpty() || itemIdsStr == null || itemIdsStr.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // カンマ区切りの品目ID文字列をプレースホルダー化 (?, ?)
        String[] ids = itemIdsStr.split(",");
        StringBuilder inClause = new StringBuilder();
        List<Object> params = new java.util.ArrayList<>();
        
        params.add(orgId.trim());

        for (int i = 0; i < ids.length; i++) {
            if (i > 0) inClause.append(", ");
            inClause.append("?");
            params.add(ids[i].trim());
        }

        String sql = "SELECT mp.organization_code, msib.segment1, " +
                     "CASE " +
                     "  WHEN flv.meaning = '外注加工品目' THEN CONCAT(flv.meaning, '_', msib.segment1) " +
                     "  ELSE flv.meaning " +
                     "END AS item_type_name " +
                     "FROM mtl_system_items_b msib " +
                     "JOIN mtl_parameters mp ON mp.organization_id = msib.organization_id " +
                     "JOIN fnd_lookup_values flv ON flv.lookup_code = msib.item_type " +
                     "  AND flv.lookup_type = 'ITEM_TYPE' " +
                     "  AND flv.meaning IS NOT NULL " +
                     "WHERE msib.organization_id = ? " +
                     "  AND msib.inventory_item_id IN (" + inClause.toString() + ") " +
                     "ORDER BY msib.inventory_item_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setSegment1(rs.getString("segment1"));
            stock.setItemTypeName(rs.getString("item_type_name"));
            return stock;
        }, params.toArray());
    }
    /* ========================================================================
     * 🟦 【問9用メソッド】工順を持たない半組立品の組織別件数集計
     * ------------------------------------------------------------------------
     *  NOT EXISTS サブクエリを使用して bom_operational_routings に
     *  該当データが存在しない「半組立品」の件数を組織ごとに集計します。
     * ======================================================================== */
    public List<ItemStock> findTask9() {
        String sql = "SELECT mp.organization_code, COUNT(*) AS no_routing_item_count " +
                     "FROM mtl_system_items_b msib " +
                     "JOIN mtl_parameters mp ON mp.organization_id = msib.organization_id " +
                     "JOIN fnd_lookup_values flv ON flv.lookup_code = msib.item_type " +
                     "  AND flv.lookup_type = 'ITEM_TYPE' " +
                     "  AND flv.meaning = '半組立品' " +
                     "WHERE msib.organization_id <> 0 " +
                     "  AND NOT EXISTS ( " +
                     "    SELECT 1 " +
                     "    FROM bom_operational_routings bor " +
                     "    WHERE bor.organization_id = msib.organization_id " +
                     "      AND bor.assembly_item_id = msib.inventory_item_id " +
                     "  ) " +
                     "GROUP BY mp.organization_code " +
                     "ORDER BY mp.organization_code";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setOrganizationCode(rs.getString("organization_code"));
            stock.setNoRoutingItemCount(rs.getInt("no_routing_item_count"));
            return stock;
        });
    }
    /* ========================================================================
     * 🟨 【問10用メソッド】納入先別・月別発注ヘッダ件数集計
     * ------------------------------------------------------------------------
     * 【SQLの選定理由・エラー回避のポイント】
     * 1. po_headers_all テーブルの主キーカラム名は `PO_HEADERS_ID`（S付き）です。
     *    `ph.po_header_id` と書くと Unknown column エラー(500)が発生するため
     *    正確なカラム名（PO_HEADERS_ID）で件数集計を行っています。
     * 2. HAVING COUNT(ph.po_headers_id) > 2 により、発注件数が3件以上の
     *    拠点・月のみを絞り込んでいます。
     * 3. hr_locations_all_tl の名称カラム location_code を参照しています。
     * ======================================================================== */
    public List<ItemStock> findTask10() {
        String sql = "SELECT hl.location_code AS ship_to_location_name, " +
                     "DATE_FORMAT(ph.approved_date, '%Y-%m') AS approved_month, " +
                     "COUNT(ph.po_headers_id) AS po_header_count " +
                     "FROM po_headers_all ph " +
                     "JOIN hr_locations_all_tl hl ON ph.ship_to_location_id = hl.location_id " +
                     "  AND hl.language = 'JA' " +
                     "WHERE ph.approved_date IS NOT NULL " +
                     "GROUP BY hl.location_code, DATE_FORMAT(ph.approved_date, '%Y-%m') " +
                     "HAVING COUNT(ph.po_headers_id) > 2 " +
                     "ORDER BY ship_to_location_name ASC, approved_month DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemStock stock = new ItemStock();
            stock.setShipToLocationName(rs.getString("ship_to_location_name"));
            stock.setApprovedMonth(rs.getString("approved_month"));
            stock.setPoHeaderCount(rs.getInt("po_header_count"));
            return stock;
        });
    }
}