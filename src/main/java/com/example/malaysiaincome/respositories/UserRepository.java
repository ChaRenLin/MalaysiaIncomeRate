package com.example.malaysiaincome.respositories;


import com.example.malaysiaincome.entities.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserVO, Long> {
    Page<UserVO> findAll(Pageable pageable);
    Optional<UserVO> findByIcNumber(String icNumber);
    boolean existsByIcNumber(String icNumber);
}
