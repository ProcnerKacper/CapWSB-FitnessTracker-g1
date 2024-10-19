package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList();
    }

    @GetMapping("/simple")
    public List<UserSimpleDto> getAllSimpleUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toSimpleDto)
                          .toList();
    }


    @GetMapping("/{id}")
    public UserDetailsDto getDetailsUserById(@PathVariable("id") Long id) {
        return userService.getUser(id)
                .map(userMapper::toDetailsDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserDto userDto) throws InterruptedException {
        try {

            return userService.createUser(userMapper.toEntity(userDto));
        } catch (Exception e) {
            throw new InterruptedException(e.getMessage());
        }
    }
}