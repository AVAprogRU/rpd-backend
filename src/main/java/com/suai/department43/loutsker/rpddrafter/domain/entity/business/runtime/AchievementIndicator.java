package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import java.io.Serializable;
import java.util.Objects;

public class AchievementIndicator implements Serializable {
    private String code;
    private String description;

    public AchievementIndicator() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AchievementIndicator)) return false;
        AchievementIndicator that = (AchievementIndicator) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
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
