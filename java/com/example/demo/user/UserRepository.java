package com.example.demo.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where username = :name", nativeQuery = true)
    User findByUsername(@Param("name") String name);
    @Operation(summary = "Returns user from database by their id")
    User findById(long id);


}