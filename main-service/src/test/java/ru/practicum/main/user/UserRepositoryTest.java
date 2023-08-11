package ru.practicum.main.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.main.user.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.main.utils.Pagination.patternPageable;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Данная аннотация нужна, если необходимо тестировать на реальной БД
// Если же нет необходимости в тестирование на реальной БД, добавляем dependency H2 в pom и тестируем на H2 в памяти.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    void setUp() {
        User user = generator.nextObject(User.class);
        user.setId(1L);
        user.setEmail("email@gmail.com");
        repository.save(user);
        User user2 = generator.nextObject(User.class);
        user.setId(2L);
        user2.setEmail("emai1l@gmail.com");
        repository.save(user2);
    }

    @Test
    void findAllByIdIn_whenValidData_thenReturnedValidList() {
        List<Long> ids = List.of(1L, 2L);
        Page<User> actualUsers = repository.findAllByIdIn(ids, patternPageable(0, 10));

        assertFalse(actualUsers.isEmpty());
        assertEquals(1L, actualUsers.getContent().get(0).getId());
    }

    @Test
    void testFindAllById_whenDataNull_thenReturnedEmptyList() {
        List<Long> ids = List.of(333L);
        Page<User> actualUsers = repository.findAllByIdIn(ids, patternPageable(0, 10));

        assertTrue(actualUsers.isEmpty());
    }
}