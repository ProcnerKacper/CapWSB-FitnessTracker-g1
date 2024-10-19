package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void deleteUserById(final Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List<User> findUserContainingEmail(final String email) {
        return userRepository.findUserContainingEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUserOlderThen(String time) {
        return userRepository.findUserOlderThen(time);
    }

    public Optional<User> updateUser(Long userId, UserDto userdata) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(userdata.firstName());
            user.setLastName(userdata.lastName());
            user.setEmail(userdata.email());
            user.setBirthdate(userdata.birthdate());
            return userRepository.save(user);
        });
    }
}