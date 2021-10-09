package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.auth.UserDetailsImpl;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreVoucher;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.repositories.VoucherRepository;
import com.giftbook.giftBook.usecases.FilterVouchersByDateUseCase;
import com.giftbook.giftBook.usecases.FilterVouchersByStatusUseCase;
import com.giftbook.giftBook.usecases.GetVouchersUseCase;
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
@RequestMapping("/api/voucher/")
public class VoucherController {
    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoucherController(VoucherRepository voucherRepository, UserRepository userRepository) {
        this.voucherRepository = voucherRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("getVouchers/{page}")
    public ResponseEntity<?> getVouchers(@PathVariable int page, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            GetVouchersUseCase useCase = new GetVouchersUseCase(
                    voucherRepository,
                    userRepository,
                    userDetails.getUsername(),
                    page
            );
            PageableCoreVoucher pageableCoreVoucher = useCase.execute();
            return ResponseEntity.ok(pageableCoreVoucher);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get vouchers, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get vouchers, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("filterVouchersByDate")
    public ResponseEntity<?> filterVouchersByDate(@RequestParam String start, @RequestParam String end,
                                                  @RequestParam int page, Authentication authentication
    ) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            FilterVouchersByDateUseCase useCase = new FilterVouchersByDateUseCase(
                    voucherRepository,
                    userRepository,
                    start,
                    end,
                    userDetails.getUsername(),
                    page
            );

            PageableCoreVoucher pageableCoreVoucher = useCase.execute();
            return ResponseEntity.ok(pageableCoreVoucher);
        } catch (EntityNotFoundException e) {
            log.error("Unable to filter vouchers by date, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid props passed");
        } catch (Exception e) {
            log.error("Unable to filter vouchers by date, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("filterVouchersByStatus")
    public ResponseEntity<?> filterVouchersByStatus(@RequestParam String status, @RequestParam int page, Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            FilterVouchersByStatusUseCase useCase = new FilterVouchersByStatusUseCase(
                    voucherRepository,
                    userRepository,
                    status,
                    userDetails.getUsername(),
                    page
            );
            PageableCoreVoucher pageableCoreVoucher = useCase.execute();
            return ResponseEntity.ok(pageableCoreVoucher);
        } catch (EntityNotFoundException e) {
            log.error("Unable to filter vouchers by status, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid props passed");
        } catch (Exception e) {
            log.error("Unable to filter vouchers by status, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}
