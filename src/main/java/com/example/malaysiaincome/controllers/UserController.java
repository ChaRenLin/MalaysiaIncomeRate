package com.example.malaysiaincome.controllers;


import com.example.malaysiaincome.entities.UserVO;
import com.example.malaysiaincome.respositories.UserRepository;
import com.example.malaysiaincome.service.SalaryService;
import com.example.malaysiaincome.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final SalaryService salaryService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody UserVO userDTO) {
        logger.info(Constants.REQUEST_LOGGER + "[ Create User - {}", userDTO + " ]");

        // Check if IC Number is already used
        if (userRepository.existsByIcNumber(userDTO.getIcNumber())) {
            logger.warn(Constants.USER_ALREADY_EXISTS, userDTO.getIcNumber());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Constants.USER_ALREADY_EXISTS);
        }

        UserVO user = new UserVO(null, userDTO.getName(), userDTO.getEmail(), userDTO.getAge(), userDTO.getSalary(), userDTO.getIcNumber());
        UserVO savedUser = userRepository.save(user);
        
        logger.info(Constants.REQUEST_LOGGER + "[ Created User ID - {}", savedUser.getId() + " ]");
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/update/{icNumber}")
    @Transactional
    public ResponseEntity<?>  updateUserByIC(@PathVariable String icNumber, @RequestBody UserVO userDTO) {
        logger.info(Constants.REQUEST_LOGGER + "[ Update User with IC Number - {}, Data - {}", icNumber, userDTO + " ]");

        // Check if IC Number is already used
        if (!userRepository.existsByIcNumber(icNumber)) {
            logger.warn(Constants.USER_NOT_FOUND + icNumber);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Constants.USER_NOT_FOUND + icNumber);
        }

        UserVO savedUser =  userRepository.findByIcNumber(icNumber)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setAge(userDTO.getAge());
                    existingUser.setSalary(userDTO.getSalary());
                    UserVO updatedUser = userRepository.save(existingUser);
                    return updatedUser;
                })
                .orElseThrow(() -> new IllegalArgumentException( Constants.USER_NOT_FOUND + icNumber));
        logger.info(Constants.REQUEST_LOGGER + "[ Response: Updated User with IC Number - {}", savedUser.getIcNumber() + " ]");
        return ResponseEntity.ok(savedUser);
    }


    @GetMapping
    public Page<UserVO> getUsers(@RequestParam(defaultValue = "0") int page) {
        logger.info(Constants.REQUEST_LOGGER + "[ Request: Get Users with pagination, Page: {}", page + "]");
        return userRepository.findAll(PageRequest.of(page, Constants.PAGE_SIZE));
    }

    @GetMapping("/external")
    public String getExternalSalaryData() {
        logger.info(Constants.RESPONSE_LOGGER + "[ Fetching data from Malaysia Salary API ]");
        System.out.println("Fetching data from Malaysia Salary API");
        return salaryService.getExternalSalaryData();
    }
}
