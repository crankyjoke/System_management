package com.example.demo.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        String baseUsername = user.getUsername();
        String username = baseUsername;
        int count = 1;
        try{
            while (userRepository.findByUsername(username).isPresent()) {
                username = baseUsername + count;
                count++;
            }

        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Choose a different username");
            return null;
        }
        user.setUsername(username);
        return userRepository.save(user);
    }
//    public boolean deleteUser(Long id) {
//
//        if (userRepository.existsById(id)) { // ✅ Check if user exists before deleting
//            userRepository.deleteById(id);
//            return true;
//        }
//        return false; // Return false if user was not found
//    }
    @Transactional
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) { // ✅ Ensure user exists
            User user = userOptional.get();

            // Prevent deletion of "admin1"
            if ("admin1".equals(user.getUsername())) {
                return false;
            }

            userRepository.deleteById(id);
            return true;
        }

        return false; // Return false if user not found
    }

    public User createUser(String baseUsername,String rawPassword, String roles) {

        User newUser = new User();

        newUser.setUsername(baseUsername);

        newUser.setPassword(rawPassword);

        newUser.setPermission(roles);

        return userRepository.save(newUser);
    }



    public User createUser(String baseUsername, String email,String rawPassword, String roles) {

        User newUser = new User();
        newUser.setEmail(email);

        newUser.setUsername(baseUsername);

        newUser.setPassword(rawPassword);

        newUser.setPermission(roles);

        return userRepository.save(newUser);
    }



    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public String modifyPermission(String newPermission, Long id) {
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
    public void createDefaultAdmin() {
        // Check if an admin already exists
        if (userRepository.findByUsername("admin1").isEmpty()) {
            System.out.println("Creating default admin user...");
            createUser("admin1", "admin123", "ADMIN");
        }
    }

}
