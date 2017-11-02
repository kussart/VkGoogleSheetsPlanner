package com.notification.configs.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("users")
public class UserProperties {

    private int domainColumnNum;
    private int nameColumnNum;
    private int dateColumnNum;
    private int taskColumnNum;

    public int getDomainColumnNum() {
        return domainColumnNum;
    }

    public void setDomainColumnNum(int domainColumnNum) {
        this.domainColumnNum = domainColumnNum;
    }

    public int getNameColumnNum() {
        return nameColumnNum;
    }

    public void setNameColumnNum(int nameColumnNum) {
        this.nameColumnNum = nameColumnNum;
    }

    public int getDateColumnNum() {
        return dateColumnNum;
    }

    public void setDateColumnNum(int dateColumnNum) {
        this.dateColumnNum = dateColumnNum;
    }

    public int getTaskColumnNum() {
        return taskColumnNum;
    }

    public void setTaskColumnNum(int taskColumnNum) {
        this.taskColumnNum = taskColumnNum;
    }

}
