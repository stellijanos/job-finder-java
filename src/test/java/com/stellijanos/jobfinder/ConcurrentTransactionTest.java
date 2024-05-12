package com.stellijanos.jobfinder;

import com.stellijanos.jobfinder.models.Category;
import com.stellijanos.jobfinder.services.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;

import java.sql.Timestamp;

@SpringBootTest
@ActiveProfiles("test")
public class ConcurrentTransactionTest {


    @Autowired
    private CategoryService categoryService;

    @Test
    @Transactional
    public void testConcurrentCategoryUpdates() throws InterruptedException {
        Category newDetails1 = new Category();
        newDetails1.setName("Updated Electronics");
        newDetails1.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        Category newDetails2 = new Category();
        newDetails2.setName("Updated Books");
        newDetails2.setUpdated_at(new Timestamp(System.currentTimeMillis() + 1000));

        Thread thread1 = new Thread(() -> {
            categoryService.updateCategory(1L, newDetails1);
            if (TestTransaction.isActive()) {
                TestTransaction.flagForCommit();
                TestTransaction.end();
            }
        });

        Thread thread2 = new Thread(() -> {
            categoryService.updateCategory(1L, newDetails2);
            if (TestTransaction.isActive()) {
                TestTransaction.flagForCommit();
                TestTransaction.end();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}

