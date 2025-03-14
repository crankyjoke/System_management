package com.example.demo.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.UserPosition;
import com.example.demo.Service.UserPositionService;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPositionService userPositionService;
    public UserService(UserRepository userRepository,UserPositionService userPositionService){
        this.userPositionService = userPositionService;
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
        User createdUser = userRepository.save(user);
        UserPosition userPosition = new UserPosition(createdUser.getId());
        userPositionService.createUserPosition(userPosition);
        return createdUser;
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

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Prevent deletion of "admin1"
            if ("admin1".equals(user.getUsername())) {
                return false;
            }
            userPositionService.deleteUserPosition(id);

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
    public boolean updateUsername(String newUsername,Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setUsername(newUsername);
            userRepository.save(user);
            return true;
        }

        return false;
    }
    public boolean updatePassword(String newPassword,Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    public boolean updateEmail(String newEmail,Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEmail(newEmail);
            return true;
        }
        return false;
    }
    public boolean updatePermission(String newPermission,Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setPermission(newPermission);
            return true;
        }

        return false;
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
            user.setPermission(newPermission);
            userRepository.save(user);
            return "yes";
        }

        return null;
    }
    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userPayload) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if(existingUser.getUsername().equals("ADMIN1")){
                return null;
            }
            // Update only the fields you want to allow changes to:
            existingUser.setEmail(userPayload.getEmail());
            existingUser.setPermission(userPayload.getPermission());
            System.out.println(userPayload.getPermission());
            existingUser.setUsername(userPayload.getUsername());
            existingUser.setAccesspage(userPayload.getAccesspage());
            // optionally handle password if needed:
            // existingUser.setPassword(userPayload.getPassword());

            // Save the updated user to the DB
            return userRepository.save(existingUser);
        }

        // Return null if user not found
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
