package com.clouddevopshub.demo.service;

import com.clouddevopshub.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    
    public UserService() {
        // Initialize with sample data
        users.add(User.builder()
                .id(idCounter.getAndIncrement())
                .name("Vikas Kumar")
                .email("vikas@clouddevopshub.com")
                .department("DevOps")
                .build());
        
        users.add(User.builder()
                .id(idCounter.getAndIncrement())
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .build());
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    public User createUser(User user) {
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }
    
    public Optional<User> updateUser(Long id, User updatedUser) {
        return getUserById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setDepartment(updatedUser.getDepartment());
            return existingUser;
        });
    }
    
    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}
