package com.example.demo.event;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select * from event where owner_id = :ownerid", nativeQuery = true)
    List<Event> findByOwnerId(@Param("ownerid") long ownerid);
}
