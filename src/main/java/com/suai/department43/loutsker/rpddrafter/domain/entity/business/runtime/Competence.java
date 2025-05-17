package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashSet;

public class Competence implements Serializable {
    private long id;
    private String name;
    private List<AchievementIndicator> achievementIndicators;
    private String categoryName;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competence)) return false;
        Competence that = (Competence) o;

        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(categoryName, that.categoryName) &&
                Objects.equals(description, that.description) &&
                new HashSet<>(getAchievementIndicators()).equals(new HashSet<>(that.getAchievementIndicators()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, new HashSet<>(getAchievementIndicators()), categoryName, description);
    }

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

    // Геттер с защитой от null
    public List<AchievementIndicator> getAchievementIndicators() {
        return achievementIndicators == null ? new ArrayList<>() : achievementIndicators;
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
