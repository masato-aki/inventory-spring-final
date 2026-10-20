package com.example.inventory.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * 【クラスの役割】全課題共通のデータモデル（エンティティ / DTO）
 * ----------------------------------------------------------------------------
 *  Lombokのアノテーションを使用しているため、手動の Getter / Setter は不要です。
 *  @Data : 全フィールドの Getter / Setter / toString 等を自動生成
 *  @NoArgsConstructor : 引数なしコンストラクタを自動生成
 *  @AllArgsConstructor : 全フィールド引数コンストラクタを自動生成
 * ============================================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStock {

    /** 組織ID (例: "44") */
    private String organizationId;

    /** 組織コード (例: "04") */
    private String organizationCode;

    /** 組織名 */
    private String organizationName;

    /** 商品ID (例: "10002") */
    private String itemId;

    /** 商品名 */
    private String itemName;

    /** 品目コード（例: "B-0004"） */
    private String segment1;

    /** 品目原価（例: 1200） */
    private Integer itemCost;

    /** 保管場所・棚番（例: "A01"） */
    private String subinventoryCode;

    /** 単位（例: "PCE"） */
    private String uomCode;

    /** 取引・手持在庫数量 */
    private Integer primaryQuantity;

    /** 在庫数 */
    private Integer stockQuantity;
    /** 品目マスタ件数（問6用） */
    private Integer itemCount;
    /** 品目マスタ件数（問6用） */
    /** 品目作成日（例: "2023/02/06"） */
    private String creationDate;

    /** 品目作成ユーザ（例: "01ユーザ"） */
    private String createdByUser;

    /** 品目更新日（例: "2023/02/06"） */
    private String lastUpdateDate;

    /** 品目更新ユーザ（例: "01ユーザ"） */
    private String lastUpdatedByUser;
    
    /** 品目タイプ（問8用: CASE式で加工済みの文字列） */
    private String itemTypeName;
    /** 工順なし半組立品件数（問9用） */
    private Integer noRoutingItemCount;
    /** 納入先（問10用） */
    private String shipToLocationName;

    /** 発注承認月（問10用: 例 "2022-04"） */
    private String approvedMonth;

    /** 発注ヘッダ件数（問10用） */
    private Integer poHeaderCount;
}