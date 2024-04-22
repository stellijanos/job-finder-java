package com.stellijanos.jobfinder.models.composite_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JobSkillKey implements Serializable {

    @Column(name = "job_id")
    Long jobId;

    @Column(name = "skill_id")
    Long skillId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
        if (!(o instanceof JobSkillKey that)) return false;
        return Objects.equals(getJobId(), that.getJobId()) &&
                Objects.equals(getSkillId(), that.getSkillId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobId(), getSkillId());
    }

}
