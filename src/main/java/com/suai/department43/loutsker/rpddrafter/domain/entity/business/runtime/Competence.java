package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import java.io.Serializable;
import java.util.List;

public class Competence implements Serializable {
    private long id;
    private String name;
    private List<AchievementIndicator> achievementIndicators;
    private String categoryName;
    private String description;

    public Competence() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AchievementIndicator> getAchievementIndicators() {
        return achievementIndicators;
    }

    public void setAchievementIndicators(List<AchievementIndicator> achievementIndicators) {
        this.achievementIndicators = achievementIndicators;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
