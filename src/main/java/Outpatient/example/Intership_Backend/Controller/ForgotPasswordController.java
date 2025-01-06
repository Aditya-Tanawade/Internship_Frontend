package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.User;
import Outpatient.example.Intership_Backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender; // Inject JavaMailSender

    @Autowired
    public ForgotPasswordController(UserRepo userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String otp = generateOtp();
        user.setOtp(otp);
        userRepository.save(user);

        sendOtpEmail(user, otp);

        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String inputOtp = requestBody.get("otp");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        System.out.println("Stored OTP: " + user.getOtp());
        System.out.println("Input OTP: " + inputOtp);

        if (user.getOtp().equals(inputOtp.trim())) {
            return ResponseEntity.status(HttpStatus.OK).body("OTP verified. Please set your new password.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully.");
    }

    private void sendOtpEmail(User user, String otp) {
        String subject = "Your OTP for Password Reset";
        String body = "Use the following OTP to reset your password: " + otp;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(otp);
    }
}