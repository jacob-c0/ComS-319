package com.example.demo.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from message where send_Id = :sId AND receive_Id = :rId", nativeQuery = true)
    List<Message> findSent(@Param("sId") long sId, @Param("rId") long rId);
    Message findById(long id);

}
