package com.uade.tpo.demo.controllers.purchase;

import com.uade.tpo.demo.entity.PurchaseItem;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest{
    private List<PurchaseItem> items;
    private Long userId;
}