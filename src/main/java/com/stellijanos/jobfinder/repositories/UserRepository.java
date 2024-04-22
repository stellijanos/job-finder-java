package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
