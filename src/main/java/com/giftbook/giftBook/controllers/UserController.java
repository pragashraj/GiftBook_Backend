package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.exceptions.UserLoginException;
import com.giftbook.giftBook.repositories.PaymentCardRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.responses.GetProfileResponse;
import com.giftbook.giftBook.usecases.GetProfileDetailsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;

    @Autowired
    public UserController(UserRepository userRepository, PaymentCardRepository paymentCardRepository) {
        this.userRepository = userRepository;
        this.paymentCardRepository = paymentCardRepository;
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
}