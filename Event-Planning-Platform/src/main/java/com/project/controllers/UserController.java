package com.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.entities.User;
import com.project.services.UserService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestParam("user") String userData,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userData, User.class);
            User savedUser = userService.createUser(user, profilePicture);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Kayıt başarılı!");
            response.put("userId", savedUser.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Profil resmi yüklenirken bir hata oluştu!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Kayıt sırasında bir hata oluştu!");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Giriş başarılı!");
            response.put("userId", user.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bilgiler hatalı!");
        }
    }


    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId)
    {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public User updateUserById(@PathVariable Long userId, @RequestBody User upUser)
    {
        return userService.updateUserById(userId, upUser);
    }

    @PostMapping("/{userId}/uploadProfilePicture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long userId, // URL'den userId al
            @RequestParam("profilePicture") MultipartFile file) {

        try {
            String profilePicturePath = userService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(new ResponseMessage("Profil fotoğrafı başarıyla yüklendi!", profilePicturePath));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fotoğraf yüklenirken bir hata oluştu.");
        }
    }

    // ResponseMessage sınıfı ile başarılı cevap formatı
    public static class ResponseMessage {
        private String message;
        private String profilePicturePath;

        public ResponseMessage(String message, String profilePicturePath) {
            this.message = message;
            this.profilePicturePath = profilePicturePath;
        }

        public String getMessage() {
            return message;
        }

        public String getProfilePicturePath() {
            return profilePicturePath;
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId)
    {
        userService.deleteUserById(userId);
    }

}
