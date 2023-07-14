package ru.practicum.main.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
