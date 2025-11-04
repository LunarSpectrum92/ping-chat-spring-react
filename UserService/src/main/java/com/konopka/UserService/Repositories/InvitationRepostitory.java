package com.konopka.UserService.Repositories;

import com.konopka.UserService.Entities.Invitation;
import com.konopka.UserService.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepostitory extends JpaRepository<Invitation,Integer> {

    Optional<Invitation> findBySender(User sender);

    Optional<Invitation> findByReceiver(User receiver);

    Optional<Invitation> findBySender_IdAndReceiver_Id(Integer senderId, Integer reciverId);


}
