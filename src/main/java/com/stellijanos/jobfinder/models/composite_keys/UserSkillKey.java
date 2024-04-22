package com.stellijanos.jobfinder.models.composite_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserSkillKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "skill_id")
    Long skillId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSkillKey that)) return false;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getSkillId(), that.getSkillId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getSkillId());
    }

}
