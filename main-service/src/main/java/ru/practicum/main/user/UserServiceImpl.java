package ru.practicum.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.utils.Pagination.patternPageable;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pagination = patternPageable(from, size);
        if (ids == null || ids.isEmpty()) {
            return repository.findAll(pagination).stream()
                    .map(UserMapper::toDtoUser)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByIdIn(ids, pagination).stream()
                    .map(UserMapper::toDtoUser)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toDtoUser(repository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("User deleting with id {}", userId);
        repository.delete(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not deleted - failed")));
    }
}
