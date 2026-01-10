package com.konopka.UserService.Repositories;

import com.konopka.UserService.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAuthId(int authId);

    List<User> findAllByAuthIdNot(int authId);


}
