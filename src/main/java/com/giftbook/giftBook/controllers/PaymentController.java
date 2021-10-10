package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.auth.UserDetailsImpl;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCorePayment;
import com.giftbook.giftBook.repositories.*;
import com.giftbook.giftBook.requests.CreateNewPaymentRequest;
import com.giftbook.giftBook.responses.ApiResponse;
import com.giftbook.giftBook.usecases.CreateNewPaymentUseCase;
import com.giftbook.giftBook.usecases.FilterPaymentsByDateUseCase;
import com.giftbook.giftBook.usecases.GetPaymentsUseCase;
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
@RequestMapping("/api/payment/")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ReceiverRepository receiverRepository;
    private final MerchantRepository merchantRepository;
    private final ItemRepository itemRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final VoucherRepository voucherRepository;
    private final SenderRepository senderRepository;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository,
                             UserRepository userRepository,
                             ReceiverRepository receiverRepository,
                             MerchantRepository merchantRepository,
                             ItemRepository itemRepository,
                             PaymentCardRepository paymentCardRepository,
                             VoucherRepository voucherRepository,
                             SenderRepository senderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.receiverRepository = receiverRepository;
        this.merchantRepository = merchantRepository;
        this.itemRepository = itemRepository;
        this.paymentCardRepository = paymentCardRepository;
        this.voucherRepository = voucherRepository;
        this.senderRepository = senderRepository;
    }

    @GetMapping("getPayments/{page}")
    public ResponseEntity<?> getPayments(@PathVariable int page, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            GetPaymentsUseCase useCase = new GetPaymentsUseCase(
                    paymentRepository,
                    userRepository,
                    userDetails.getUsername(),
                    page
            );
            PageableCorePayment pageableCorePayment = useCase.execute();
            return ResponseEntity.ok(pageableCorePayment);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get payments, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get payments, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("create")
    public ResponseEntity<?> createPayment(@RequestBody CreateNewPaymentRequest request) {
        try {
            CreateNewPaymentUseCase useCase = new CreateNewPaymentUseCase(
                    paymentRepository,
                    userRepository,
                    receiverRepository,
                    merchantRepository,
                    itemRepository,
                    paymentCardRepository,
                    voucherRepository,
                    senderRepository,
                    request
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to create new payment, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to create new payment, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("filterPaymentsByDate")
    public ResponseEntity<?> filterPaymentsByDate(@RequestParam String start, @RequestParam String end,
                                                  @RequestParam int page, Authentication authentication
    ) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            FilterPaymentsByDateUseCase useCase = new FilterPaymentsByDateUseCase(
                    paymentRepository,
                    userRepository,
                    start,
                    end,
                    userDetails.getUsername(),
                    page
            );

            PageableCorePayment pageableCorePayment = useCase.execute();
            return ResponseEntity.ok(pageableCorePayment);
        } catch (EntityNotFoundException e) {
            log.error("Unable to filter payments by date, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid props passed");
        } catch (Exception e) {
            log.error("Unable to filter payments by date, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}
