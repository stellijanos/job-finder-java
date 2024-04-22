package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.Application;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
