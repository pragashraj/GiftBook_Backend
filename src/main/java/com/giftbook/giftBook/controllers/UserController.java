package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.auth.UserDetailsImpl;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.PaymentCardRepository;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.requests.UpdatePaymentCardRequest;
import com.giftbook.giftBook.requests.UpdateProfileRequest;
import com.giftbook.giftBook.responses.ApiResponse;
import com.giftbook.giftBook.responses.GetProfileResponse;
import com.giftbook.giftBook.usecases.GetProfileDetailsUseCase;
import com.giftbook.giftBook.usecases.UpdateOrCreatePaymentCardUseCase;
import com.giftbook.giftBook.usecases.UpdateProfileUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final UserAuthenticationRepository authRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          PaymentCardRepository paymentCardRepository,
                          UserAuthenticationRepository authRepository
    ) {
        this.userRepository = userRepository;
        this.paymentCardRepository = paymentCardRepository;
        this.authRepository = authRepository;
    }

    @GetMapping("profile-details/{email}")
    public ResponseEntity<?> getProfileDetails(@PathVariable String email) {
        try {
            GetProfileDetailsUseCase useCase = new GetProfileDetailsUseCase(
                    userRepository,
                    paymentCardRepository,
                    email
            );
            GetProfileResponse response = useCase.execute();
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get profile details for email: {}, cause: {}", email, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get profile details for email: {}, cause: {}", email, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("update-profile-details")
    public ResponseEntity<?> updateProfileDetails(@RequestBody UpdateProfileRequest request, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            UpdateProfileUseCase useCase = new UpdateProfileUseCase(
                    userRepository,
                    authRepository,
                    request,
                    userDetails.getUsername()
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update profile details, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update profile details, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("update-payment-card-details")
    public ResponseEntity<?> updateOrCreatePaymentCardDetails(@RequestBody UpdatePaymentCardRequest request) {
        try {
            UpdateOrCreatePaymentCardUseCase useCase = new UpdateOrCreatePaymentCardUseCase(
                    userRepository,
                    paymentCardRepository,
                    request
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update or create payment card details, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update or create payment card details, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}