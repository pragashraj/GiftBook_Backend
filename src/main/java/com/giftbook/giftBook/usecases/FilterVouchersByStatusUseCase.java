package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.entities.Voucher;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.core.CoreVoucher;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreVoucher;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.repositories.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

public class FilterVouchersByStatusUseCase {
    private static final Logger log = LoggerFactory.getLogger(FilterVouchersByStatusUseCase.class);

    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final String status;
    private final String email;
    private final int page;

    public FilterVouchersByStatusUseCase(VoucherRepository voucherRepository,
                                         UserRepository userRepository,
                                         String status,
                                         String email,
                                         int page
    ) {
        this.voucherRepository = voucherRepository;
        this.userRepository = userRepository;
        this.status = status;
        this.email = email;
        this.page = page;
    }

    public PageableCoreVoucher execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found");
            throw new EntityNotFoundException("User not found");
        }

        Page<Voucher> voucherPage = voucherRepository.findAllByUserAndStatusOrderByCreatedAt(
                user, status, PageRequest.of(page, 10)
        );

        return new PageableCoreVoucher(
                voucherPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                voucherPage.getTotalPages(),
                voucherPage.getNumber()
        );
    }

    private CoreVoucher convertToCoreEntity(Voucher voucher) {
        return new CoreVoucher(
                voucher.getId(),
                voucher.getCreatedAt(),
                voucher.getValue(),
                voucher.getOwner(),
                voucher.getStatus(),
                voucher.getDescription(),
                voucher.getPayment().getMerchant().getName(),
                voucher.getPayment().getItem().getName()
        );
    }
}
