package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.exceptions.*;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.requests.SignInRequest;
import com.giftbook.giftBook.requests.SignUpRequest;
import com.giftbook.giftBook.responses.ApiResponse;
import com.giftbook.giftBook.responses.AuthenticationResponse;
import com.giftbook.giftBook.transport.EmailService;
import com.giftbook.giftBook.transport.templates.ForgotPasswordTemplate;
import com.giftbook.giftBook.usecases.*;
import com.giftbook.giftBook.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserAuthenticationRepository authRepository;
    private final UserRepository userRepository;
    private final ForgotPasswordTemplate forgotPasswordTemplate;
    private final EmailService emailService;

    @Value("${app.jwt.expiration}")
    private int expiration;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    PasswordEncoder passwordEncoder,
                                    JwtUtil jwtUtil,
                                    UserAuthenticationRepository authRepository,
                                    UserRepository userRepository,
                                    ForgotPasswordTemplate forgotPasswordTemplate,
                                    EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.forgotPasswordTemplate = forgotPasswordTemplate;
        this.emailService = emailService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        try {
            SignUpUseCase useCase = new SignUpUseCase(
                    passwordEncoder,
                    authRepository,
                    userRepository,
                    request
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (RegisterException e) {
            log.error("Unable to register user, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to register user, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        try {
            SignInUseCase useCase = new SignInUseCase(
                    authRepository,
                    jwtUtil,
                    authenticationManager,
                    request
            );
            AuthenticationResponse response = useCase.execute(expiration);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException | UserLoginException e) {
            log.error("Unable to login, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to login, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("send-reset-code/{email}")
    public ResponseEntity<?> sendResetCode(@PathVariable String email) {
        try {
            SendPasswordResetCodeUseCase useCase = new SendPasswordResetCodeUseCase(
                    authRepository,
                    forgotPasswordTemplate,
                    emailService,
                    email
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to send forgotPassword reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception | DispatcherException | ContentCreationException e) {
            log.error("Unable to send forgotPassword reset code, cause : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("confirm-reset-code")
    public ResponseEntity<?> confirmResetCode(@RequestParam String email, @RequestParam String code) {
        try {
            ConfirmResetCodeUseCase useCase = new ConfirmResetCodeUseCase(
                    authRepository,
                    code,
                    email
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | MismatchException e) {
            log.error("Unable to confirm forgotPassword reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to confirm forgotPassword reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            ForgotPasswordUseCase useCase = new ForgotPasswordUseCase(
                    authRepository,
                    passwordEncoder,
                    email,
                    newPassword
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}
