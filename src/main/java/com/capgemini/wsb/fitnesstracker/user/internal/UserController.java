package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.exception.api.NotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            throw  new InterruptedException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/email")
    public List<UserEmailDto> searchUserByEmail(@RequestParam(name = "email", required = true) String email) {
        return userService.findUserContainingEmail(email)
                .stream()
                .map(userMapper::toEmailDto)
                .toList();
    }

    @GetMapping("/older/{time}")
    public List<UserDetailsDto> searchUserByDate(@PathVariable("time") String time) {
        return userService.findUserOlderThen(time)
                .stream()
                .map(userMapper::toDetailsDto)
                .toList();
    }

    @PutMapping("/{userId}")
    public UserDetailsDto searchUserByDate(@RequestBody UserDto user, @PathVariable("userId") Long userId) {
        return userService.updateUser(userId, user)
                .map(userMapper::toDetailsDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}