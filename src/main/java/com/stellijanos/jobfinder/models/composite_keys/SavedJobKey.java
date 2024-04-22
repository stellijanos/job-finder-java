package com.stellijanos.jobfinder.models.composite_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SavedJobKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "job_id")
    Long jobId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavedJobKey that)) return false;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getJobId(), that.getJobId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getJobId());
    }

}
