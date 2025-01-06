package Outpatient.example.Intership_Backend.Controller;




import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/loadAdmins")
    public String loadAdmins() {
        userService.loadUsers();
        return  "Admins Loaded Successfully";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiError> registerUser(@RequestBody @Valid RegisterUserDTo userDto) {
        return userService.registerNewUser(userDto);
    }



    @PostMapping("/login")
    public ResponseEntity<ApiError> login(@RequestBody LoginRequest loginRequest) {
        ApiError apiError = userService.authenticateUser(loginRequest);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



}

