package com.example.demo.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public List<String> modifyPermission(List<String> newPermission, Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPermission();
        }
        return null;
    }
    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User originalUser = userOptional.get();
            originalUser.setEmail(user.getEmail());
            originalUser.setPermission(user.getPermission());

        }
        return null;
    }
}
