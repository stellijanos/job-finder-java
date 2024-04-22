package com.stellijanos.jobfinder.services;


import com.stellijanos.jobfinder.repositories.CategoryRepository;
import com.stellijanos.jobfinder.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, JobRepository jobRepository) {
        this.categoryRepository = categoryRepository;
        this.jobRepository = jobRepository;
    }
}
