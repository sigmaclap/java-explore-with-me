package ru.practicum.main.user;

import org.springframework.stereotype.Service;
import ru.practicum.main.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        return null;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
