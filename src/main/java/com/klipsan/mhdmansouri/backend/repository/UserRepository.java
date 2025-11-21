package com.klipsan.mhdmansouri.backend.repository;
import com.klipsan.mhdmansouri.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    // This generates the SQL: SELECT * FROM _user WHERE email = ?
    Optional<User> findByEmail(String email);
}