package com.eventmanager.api.service;

import com.eventmanager.api.domain.coupon.Coupon;
import com.eventmanager.api.domain.coupon.CouponRequestDTO;
import com.eventmanager.api.domain.event.Event;
import com.eventmanager.api.repositories.CouponRepository;
import com.eventmanager.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;


    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO data) {
        Event couponEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setCode(data.code());
        newCoupon.setDiscount(data.discount());
        newCoupon.setEvent(couponEvent);

        return couponRepository.save(newCoupon);
    }

    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
