package com.giftbook.giftBook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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

    private BigDecimal value;

    private String senderType;

    @OneToOne
    private Receiver receiver;

    @OneToOne
    private Sender sender;

    @OneToOne
    private PaymentCard paymentCard;

    @OneToOne
    private Merchant merchant;

    @OneToOne
    private Item item;

    @ManyToOne
    private User user;
}
