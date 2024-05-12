package com.stellijanos.jobfinder;

import com.stellijanos.jobfinder.models.Category;
import com.stellijanos.jobfinder.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
public class AllConcurrencyProblems {
    @Autowired
    private CategoryService categoryService;

    @Test
    void testLostUpdateIssue() throws InterruptedException {
        Long categoryId = 1L;
        Thread thread1 = new Thread(() -> categoryService.updateCategoryNameTwice(categoryId, "Initial Update", "Lost Update"));
        Thread thread2 = new Thread(() -> categoryService.updateCategoryNameTwice(categoryId, "Interrupting Update", "Final Update"));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void testTemporaryUpdateIssue() {
        Long categoryId = 1L;
        categoryService.updateCategoryNameTwice(categoryId, "Temporary Name", "Permanent Name");
    }

    @Test
    void testIncorrectSummaryIssue() throws InterruptedException {
        Thread readThread = new Thread(() -> {
            int count = categoryService.calculateTotalNumberOfCategories();
            System.out.println("Read count: " + count);
        });

        Thread writeThread = new Thread(() -> {
            Category category = new Category();
            category.setId(null);
            category.setName("new category");

            category.setCreated_at(new Timestamp(System.currentTimeMillis()));
            categoryService.createCategory(category);
        });

        readThread.start();
        Thread.sleep(500);
        writeThread.start();

        readThread.join();
        writeThread.join();
    }

    @Test
    void testUnrepeatableReadIssue() {
        Long categoryId = 1L;
        categoryService.demonstrateUnrepeatableRead(categoryId);
    }

    @Test
    void testPhantomReadIssue() {
        categoryService.demonstratePhantomRead();
    }
}
