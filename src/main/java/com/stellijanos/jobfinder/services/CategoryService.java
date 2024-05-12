package com.stellijanos.jobfinder.services;


import com.stellijanos.jobfinder.models.Category;
import com.stellijanos.jobfinder.repositories.CategoryRepository;
import com.stellijanos.jobfinder.repositories.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, JobRepository jobRepository) {
        this.categoryRepository = categoryRepository;
        this.jobRepository = jobRepository;
    }


    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDetails.getName());
        category.setUpdated_at(categoryDetails.getUpdated_at());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }



    // lost update problem
    @Transactional
    public void updateCategoryNameTwice(Long id, String newName1, String newName2) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(newName1);
        categoryRepository.save(category);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        category.setName(newName2);
        categoryRepository.save(category);
    }


    // temporary update problem (dirty read)
    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public String readUncommittedCategoryName(Long id) {
        return categoryRepository.findById(id).map(Category::getName).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // incorrect summary problem
    @Transactional
    public int calculateTotalNumberOfCategories() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return categories.size();
    }


    // unrepeatable read problem
    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_COMMITTED)
    public void demonstrateUnrepeatableRead(Long id) {
        String name1 = categoryRepository.findById(id).map(Category::getName).orElse("Not Found");

        try {
            Thread.sleep(2000); // Simulate time delay for external update
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String name2 = categoryRepository.findById(id).map(Category::getName).orElse("Not Found");
        System.out.println("First read: " + name1 + ", Second read: " + name2);
    }



    // phantom read problem
    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.REPEATABLE_READ)
    public void demonstratePhantomRead() {
        List<Category> firstRead = (List<Category>) categoryRepository.findAll();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Category> secondRead = (List<Category>) categoryRepository.findAll();
        System.out.println("First read count: " + firstRead.size() + ", Second read count: " + secondRead.size());
    }

}
