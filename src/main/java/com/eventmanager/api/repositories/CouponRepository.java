package com.eventmanager.api.repositories;

import com.eventmanager.api.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository  extends JpaRepository<Coupon, UUID> {
}
