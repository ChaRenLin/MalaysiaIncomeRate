package com.example.malaysiaincome.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.example.malaysiaincome.respositories.UserRepository;
import com.example.malaysiaincome.entities.UserVO;
@Component
@RequiredArgsConstructor
public class DataInitializerConfig {

    private final UserRepository userRepository;
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) { // Ensure it only runs if DB is empty
            List<UserVO> users = IntStream.range(0, 5000)
                .mapToObj(i -> new UserVO(
                    null,
                    "User" + i,  // Random name
                    "user" + i + "@example.com",  // Unique email
                    random.nextInt(50) + 18,  // Age between 18-67
                    random.nextDouble() * 10000 + 2000, // Salary between 2000 - 12000
                    generateRandomIC() // Random IC Number
                ))
                .collect(Collectors.toList());

            userRepository.saveAll(users);
            System.out.println("Inserted 2000 random users into the database.");
        }
    }

    private String generateRandomIC() {
        int year = random.nextInt(99); // 00-99
        int month = random.nextInt(12) + 1; // 1-12
        int day = random.nextInt(28) + 1; // 1-28 (simplified)
        int region = random.nextInt(100); // 00-99
        int last4 = random.nextInt(9000) + 1000; // 1000-9999

        return String.format("%02d%02d%02d-%02d-%04d", year, month, day, region, last4);
    }
}

