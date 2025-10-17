package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Coupon;
import com.uade.tpo.demo.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        couponRepository.deleteById(id);
    }
}