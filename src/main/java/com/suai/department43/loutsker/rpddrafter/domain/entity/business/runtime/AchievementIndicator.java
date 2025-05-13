package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import java.io.Serializable;

public class AchievementIndicator implements Serializable {
    private String code;
    private String description;

    public AchievementIndicator() {
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
