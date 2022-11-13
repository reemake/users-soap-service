package ru.codemark.userssoapservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.userssoapservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
