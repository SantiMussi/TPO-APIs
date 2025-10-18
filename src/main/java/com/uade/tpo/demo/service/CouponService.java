package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.demo.entity.Coupon;

public interface CouponService {
    Coupon save(Coupon coupon);
    Optional<Coupon> findByCode(String code);
    List<Coupon> findAll();
    boolean delete(Long id);
}