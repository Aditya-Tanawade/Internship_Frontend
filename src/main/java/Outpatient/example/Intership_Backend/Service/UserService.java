package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Entity.User;
import Outpatient.example.Intership_Backend.Repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private  PatientService patientService;

    @Autowired
    private AdminService adminService;


    public void loadUsers() {
        List<User> loadingAdmins = Arrays.asList(
                new User(1, "admin", "admin@gmail.com", passwordEncoder.encode("admin"), "ADMIN",""),
                new User(3, "Aditya", "aditya@gmail.com", passwordEncoder.encode("aditya"), "ADMIN","")
        );

        userRepo.saveAll(loadingAdmins);
    }


    public ResponseEntity<ApiError> registerNewUser(@Valid RegisterUserDTo userDto) {
        if (findByEmail(userDto.getEmail()).isPresent()) {
            ApiError apiError = ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Email already exists")
                    .build();
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            ApiError apiError = ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Passwords do not match")
                    .build();
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }



        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setEmail(userDto.getEmail());
        if(userDto.getRole().equals("DOCTOR")){
            doctorService.createDoctor(userDto);
        }else{
            patientService.createPatient(userDto);
        }


        userRepo.save(user);

        ApiError successResponse = ApiError.builder()
                .status(HttpStatus.OK)
                .message("Registered Successfully")
                .build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }


    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }


    public String getUserRoleByEmail(String email) {
        Optional<String> role = userRepo.findRoleByEmail(email);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new IllegalArgumentException("No user found with the provided email");
        }
    }


    public boolean authenticate(String email, String password) {
        // Retrieve the user by username
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;
    }

    public ApiError authenticateUser(LoginRequest loginRequest) {
        Optional<User> user = findByEmail(loginRequest.getEmail());

        if (user==null ) {
            return ApiError.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("User not found")
                    .build();
        }

        boolean isAuthenticated = authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (!isAuthenticated) {
            return ApiError.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Invalid password")
                    .build();
        }

        String userRole = getUserRoleByEmail(loginRequest.getEmail());

        switch (userRole) {
            case "ADMIN":
                adminService.loginAdmin(loginRequest);

                return ApiError.builder()
                        .status(HttpStatus.OK)
                        .message("Admin login successful")
                        .build();
            case "DOCTOR":
                doctorService.loginDoctor(loginRequest);
                return ApiError.builder()
                        .status(HttpStatus.OK)
                        .message("Doctor login successful")
                        .build();
            case "USER":
                patientService.loginPatient(loginRequest);
                return ApiError.builder()
                        .status(HttpStatus.OK)
                        .message("User login successful")
                        .build();
            default:
                return ApiError.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Unauthorized role")
                        .build();
        }
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }







}
