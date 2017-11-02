package com.notification.configs.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("google")
public class GoogleProperties {

    private String spreadsheetId;
    private String tableName;
    private String apiKey;

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public static void copy(GoogleProperties src, GoogleProperties dst) {
        dst.setSpreadsheetId(src.getSpreadsheetId());
        dst.setTableName(src.getTableName());
        dst.setApiKey(src.getApiKey());
    }
}