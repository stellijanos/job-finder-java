package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findByEmail(String email);
    Company findByToken(String token);
}
