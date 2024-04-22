package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Long> {

}
