package ru.practicum.main.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.user.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.practicum.main.utils.Pagination.patternPageable;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository repository;
    private final EasyRandom generator = new EasyRandom();

    @Test
    void testGetUsers_whenIdsNull_thenReturnedListUsers() {
        Pageable pagination = patternPageable(0, 10);
        List<Long> ids = null;
        Page<User> users = new PageImpl<>(List.of(generator.nextObject(User.class)));
        when(repository.findAll(pagination)).thenReturn(users);
        List<UserDto> expectedUsers = users.stream()
                .map(UserMapper::toDtoUser)
                .collect(Collectors.toList());

        List<UserDto> actualUsers = userService.getUsers(ids, 0, 10);

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0), actualUsers.get(0));
        verify(repository, times(1)).findAll(pagination);
    }

    @Test
    void testGetUsers_whenIdsNotNull_thenReturnedListUsers() {
        Pageable pagination = patternPageable(0, 10);
        User userToSave = generator.nextObject(User.class);
        List<Long> ids = List.of(userToSave.getId());
        Page<User> users = new PageImpl<>(List.of(userToSave));
        when(repository.findAllByIdIn(ids, pagination)).thenReturn(users);
        List<UserDto> expectedList = users.stream()
                .map(UserMapper::toDtoUser)
                .collect(Collectors.toList());

        List<UserDto> actualUsers = userService.getUsers(ids, 0, 10);

        assertEquals(expectedList.size(), actualUsers.size());
        assertEquals(expectedList.get(0), actualUsers.get(0));
        verify(repository, times(1)).findAllByIdIn(ids, pagination);
    }

    @Test
    void createUser_whenValidData_thenReturnedCreateUser() {
        UserDto userDto = generator.nextObject(UserDto.class);
        User userToSave = UserMapper.toUser(userDto);
        when(repository.save(userToSave)).thenReturn(userToSave);
        UserDto expectedUser = UserMapper.toDtoUser(userToSave);

        UserDto actualUser = userService.createUser(userDto);

        assertEquals(expectedUser.getName(), actualUser.getName());
        verify(repository, times(1)).save(userToSave);
    }

    @Test
    void deleteUser_whenUserExists_thenSuccessDeleted() {
        User userToDelete = generator.nextObject(User.class);
        when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        userService.deleteUser(userToDelete.getId());

        verify(repository, times(1)).delete(userToDelete);
    }
}