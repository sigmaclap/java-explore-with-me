package ru.practicum.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.utils.CommonPatterns.patternPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null || ids.isEmpty()) {
            return repository.findAll(patternPageable(from, size)).stream()
                    .map(userMapper::toDtoUser)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByIdIn(ids, patternPageable(from, size)).stream()
                    .map(userMapper::toDtoUser)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        return userMapper.toDtoUser(repository.save(userMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("User deleting with id {}", userId);
        repository.delete(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not deleted - failed")));
    }
}
