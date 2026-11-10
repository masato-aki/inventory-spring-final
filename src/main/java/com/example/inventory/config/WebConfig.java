package com.example.inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC共通設定クラス
 * 
 * フロントエンド（React / SPA）とバックエンド（Spring Boot API）が
 * 異なるオリジン（別URL）で稼働しているため、CORS（クロスオリジンリソース共有）を設定して
 * ブラウザによる通信ブロックを回避します。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS通信の許可ルールを設定
     * 全APIエンドポイントに対し、外部からのHTTPリクエストを許可します。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // React（Vercel/Render/ローカル環境など）からのアクセスを許可
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}