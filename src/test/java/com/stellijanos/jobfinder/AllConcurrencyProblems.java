package com.stellijanos.jobfinder;

import com.stellijanos.jobfinder.models.Category;
import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.services.CategoryService;
import com.stellijanos.jobfinder.services.UserService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class AllConcurrencyProblems {
    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setEmail(RandomString.make(15)+ "@gmail.com");
        testUser.setPassword("123");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setToken_expires_at(LocalDateTime.now());

        userService.createUser(testUser);
    }

    @Test
    public void testLostUpdateProblem() throws InterruptedException {
        Long userId = testUser.getId();

        User update1 = new User();
        update1.setFirstname("Jane");
        update1.setLastname("Doe");
        update1.setEmail("jane.doe@example.com");
        update1.setPassword("123");
        update1.setToken(UUID.randomUUID().toString());
        update1.setToken_expires_at(LocalDateTime.now());

        User update2 = new User();
        update2.setFirstname("Alice");
        update2.setLastname("Smith");
        update2.setEmail("alice.smith@example.com");
        update2.setPassword("123");
        update2.setToken(UUID.randomUUID().toString());
        update2.setToken_expires_at(LocalDateTime.now());

        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                latch.await();
                synchronized (this) {
                    userService.updateUser(userId, update1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                synchronized (this) {
                    userService.updateUser(userId, update2);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }

        User updatedUser = userService.getUserById(userId);

        // Check which update was applied last and assert the final state
        // Since update2 was submitted last, its values should be the ones persisted
        assertEquals("Alice", updatedUser.getFirstname());
        assertEquals("Smith", updatedUser.getLastname());
        assertEquals("alice.smith@example.com", updatedUser.getEmail());
    }



    // dirty read
    @Test
    public void testTemporaryUpdateProblem() throws InterruptedException {
        Long userId = testUser.getId();

        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                latch.await();
                User user = userService.getUserById(userId);
                user.setFirstname("Jane");
                userService.updateUser(userId, user);
                latch2.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                latch2.await();
                User user = userService.getUserById(userId);
                assertEquals("Jane", user.getFirstname());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }
    }


    @Test
    public void testIncorrectSummaryProblem() throws InterruptedException {
        Long userId1 = testUser.getId();
        User anotherUser = new User();
        anotherUser.setFirstname("Alice");
        anotherUser.setLastname("Smith");
        anotherUser.setEmail("alice2.smith@example.com");
        anotherUser.setPassword("123");
        anotherUser.setToken(UUID.randomUUID().toString());
        anotherUser.setToken_expires_at(LocalDateTime.now());
        userService.createUser(anotherUser);
        Long userId2 = anotherUser.getId();

        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                latch.await();
                synchronized (this) {
                    User user = userService.getUserById(userId1);
                    user.setFirstname("Jane");
                    userService.updateUser(userId1, user);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                synchronized (this) {
                    Iterable<User> users = userService.getAll();
                    int count = 0;
                    for (User u : users) {
                        count++;
                    }
                    assertEquals(2, count); // Initially 2 users should be counted
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }
    }




    @Test
    public void testUnrepeatableReadProblem() throws InterruptedException {
        Long userId = testUser.getId();

        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                latch.await();
                User user = userService.getUserById(userId);
                String firstRead = user.getFirstname();
                Thread.sleep(1000);
                user = userService.getUserById(userId);
                String secondRead = user.getFirstname();
                assertNotEquals(firstRead, secondRead);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                Thread.sleep(500);
                User user = userService.getUserById(userId);
                user.setFirstname("Jane");
                userService.updateUser(userId, user);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }
    }


    @Test
    public void testPhantomReadProblem() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                latch.await();
                Iterable<User> users = userService.getAll();
                int count = 0;
                for (User u : users) {
                    count++;
                }
                assertEquals(1, count); // Initially 1 user should be counted
                Thread.sleep(1000);
                users = userService.getAll();
                count = 0;
                for (User u : users) {
                    count++;
                }
                assertEquals(2, count); // After the new user is added, count should be 2
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                latch.await();
                Thread.sleep(500);
                User newUser = new User();
                newUser.setFirstname("Alice");
                newUser.setLastname("Smith");
                newUser.setEmail("alice.smith@example.com");
                userService.createUser(newUser);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.countDown();
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }
    }





}
