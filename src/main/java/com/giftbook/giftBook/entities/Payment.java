package com.giftbook.giftBook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime paymentAt;

    @OneToOne
    private PaymentCard paymentCard;

    @OneToOne
    private Merchant merchant;

    @ManyToOne
    private User user;
}
