package com.example.finalproject.event.service.user;

import com.example.finalproject.event.config.JwtUtil;
import com.example.finalproject.event.dto.AdminLoginRequest;
import com.example.finalproject.event.dto.RegisterRequest;
import com.example.finalproject.event.exception.user.*;
import com.example.finalproject.event.model.UserModel;
import com.example.finalproject.event.model.UserRole;
import com.example.finalproject.event.repository.UserRepository;
import com.example.finalproject.event.response.admin.PatchAdminResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest request)
            throws EmailAlreadyExistsException,
            PasswordMismatchException,
            WeakPasswordException,
            PhoneNumberExistedException {

        //  Email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        //  Phone number already exist
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new PhoneNumberExistedException();
        }
        //  Password & confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        //  Password strength
        if (!isValidPassword(request.getPassword())) {
            throw new WeakPasswordException();
        }

        UserModel user = new UserModel();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);

        userRepository.save(user);

        return "Register Success";
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasNumber = password.matches(".*\\d.*");

        return hasLetter && hasNumber;
    }

    public PatchAdminResponse.updateRole promoteToAdmin(Long userId){
        UserModel user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setRole(UserRole.ROLE_ADMIN);

        userRepository.save(user);

        return new PatchAdminResponse.updateRole(
                user.getUserId(),
                user.getUserName(),
                user.getRole().name()
        );
    }

    public String login(AdminLoginRequest request) {
        UserModel admin = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (admin.getRole() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("Access denied");
        }

        return jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());
    }
}
