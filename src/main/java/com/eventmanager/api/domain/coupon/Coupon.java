package com.eventmanager.api.domain.coupon;

import com.eventmanager.api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;

    private String code;

    private Integer discount;

    private Date valid;

    @ManyToOne //Relação de muitos para um com a tabela eventos
    @JoinColumn(name = "event_id") //Mapeando no banco
    private Event event;
}
