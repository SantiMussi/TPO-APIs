package com.uade.tpo.demo.service;

import com.uade.tpo.demo.controllers.purchase.PurchaseResponse;
import com.uade.tpo.demo.controllers.purchase.PurchaseRequest;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;

public interface PurchaseService {
    PurchaseResponse purchaseProduct(PurchaseRequest request) throws ProductNotFoundException;
}
