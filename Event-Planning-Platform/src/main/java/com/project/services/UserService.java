package com.project.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.project.entities.User;
import com.project.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    String uploadDir = "C:\\Code VS\\smart-event-planning-platform\\public\\profile-pics\\";
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User createUser(User user, MultipartFile profilePicture) throws IOException {
        // Kullanıcı adı kontrolü
        if (existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten alınmış.");
        }
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayıtlı.");
        }
        if (existsByPhone(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Bu telefon numarası zaten kayıtlı.");
        }

        // Profil resmi yükleme işlemi
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String fileExtension = Objects.requireNonNull(profilePicture.getOriginalFilename())
                    .substring(profilePicture.getOriginalFilename().lastIndexOf('.'));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDir + uniqueFileName);

            // Dosyayı kaydet
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, profilePicture.getBytes());

            // Veritabanı için göreceli dosya yolunu ayarla
            user.setProfilePicturePath("profile-pics/" + uniqueFileName);
        }

        // Kullanıcıyı kaydet
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long userId)
    {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User updateUserById(Long userId, User upUser) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            User foundUser = user.get();

            // E-posta benzersizliği kontrolü
            if (upUser.getEmail() != null && !upUser.getEmail().isEmpty()) {
                boolean emailExists = userRepository.existsByEmailAndUserIDNot(upUser.getEmail(), userId);
                if (emailExists) {
                    throw new RuntimeException("E-posta başka bir kullanıcı tarafından kullanılmaktadır.");
                }
                foundUser.setEmail(upUser.getEmail());
            }

            // Kullanıcı adı benzersizliği kontrolü
            if (upUser.getUsername() != null && !upUser.getUsername().isEmpty()) {
                boolean usernameExists = userRepository.existsByUsernameAndUserIDNot(upUser.getUsername(), userId);
                if (usernameExists) {
                    throw new RuntimeException("Kullanıcı adı başka bir kullanıcı tarafından kullanılmaktadır.");
                }
                foundUser.setUsername(upUser.getUsername());
            }

            // Telefon numarası benzersizliği kontrolü
            if (upUser.getPhoneNumber() != null && !upUser.getPhoneNumber().isEmpty()) {
                boolean phoneExists = userRepository.existsByPhoneNumberAndUserIDNot(upUser.getPhoneNumber(), userId);
                if (phoneExists) {
                    throw new RuntimeException("Telefon numarası başka bir kullanıcı tarafından kullanılmaktadır.");
                }
                foundUser.setPhoneNumber(upUser.getPhoneNumber());
            }

            // Diğer alanların güncellenmesi
            if (upUser.getFirstName() != null && !upUser.getFirstName().isEmpty()) {
                foundUser.setFirstName(upUser.getFirstName());
            }
            if (upUser.getLastName() != null && !upUser.getLastName().isEmpty()) {
                foundUser.setLastName(upUser.getLastName());
            }
            if (upUser.getPassword() != null && !upUser.getPassword().isEmpty()) {
                foundUser.setPassword(upUser.getPassword());
            }
            if (upUser.getLocation() != null && !upUser.getLocation().isEmpty()) {
                foundUser.setLocation(upUser.getLocation());
            }
            if (upUser.getInterests() != null && !upUser.getInterests().isEmpty()) {
                foundUser.setInterests(upUser.getInterests());
            }
            if (upUser.getDateOfBirth() != null) {
                foundUser.setDateOfBirth(upUser.getDateOfBirth());
            }
            if (upUser.getGender() != null && !upUser.getGender().isEmpty()) {
                foundUser.setGender(upUser.getGender());
            }
            if (upUser.getProfilePicturePath() != null && !upUser.getProfilePicturePath().isEmpty()) {
                foundUser.setProfilePicturePath(upUser.getProfilePicturePath());
            }

            return userRepository.save(foundUser);
        } else {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex) {
        return ex.getMessage();
    }


    public String uploadProfilePicture(Long userId, MultipartFile profilePicture) throws IOException {
        // Kullanıcıyı veritabanından al
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        // Fotoğraf kontrolü ve yükleme
        if (profilePicture != null && !profilePicture.isEmpty()) {
            // Dosya uzantısını al
            String fileExtension = Objects.requireNonNull(profilePicture.getOriginalFilename())
                    .substring(profilePicture.getOriginalFilename().lastIndexOf('.'));
            // Benzersiz dosya adı oluştur
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            // Dosyanın kaydedileceği tam yol
            Path filePath = Paths.get(uploadDir + uniqueFileName);
            // Dosya dizinini oluştur
            Files.createDirectories(filePath.getParent());
            // Dosyayı kaydet
            Files.write(filePath, profilePicture.getBytes());
            // Veritabanı için göreceli dosya yolunu ayarla
            user.setProfilePicturePath("profile-pics/" + uniqueFileName);
            // Kullanıcıyı güncelle
            userRepository.save(user);
            return "profile-pics/" + uniqueFileName; // Yüklenen fotoğrafın yolunu döndür
        }

        throw new IOException("Fotoğraf yüklenemedi.");
    }

    public void deleteUserById(Long userId)
    {
        userRepository.deleteById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null); // Kullanıcı bulunamazsa null dön
    }
}