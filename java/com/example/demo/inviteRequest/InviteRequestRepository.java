package com.example.demo.inviteRequest;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InviteRequestRepository extends JpaRepository<InviteRequest, Long>
{
    @Query(value = "select * from invite_requests where owner_id = :id", nativeQuery = true)
    List<InviteRequest> findSent(@Param("id") long id);
    InviteRequest findById(long id);

}
