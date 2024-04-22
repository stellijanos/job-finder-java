package com.stellijanos.jobfinder.repositories;

import com.stellijanos.jobfinder.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
