package com.uade.tpo.demo.service;

import com.uade.tpo.demo.controllers.product.ImageManager;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Size;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.entity.Category;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;

    public Page<Product> getProducts(PageRequest pageRequest) {
        return productRepository.findByIsActiveTrue(pageRequest);
    }


    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }


    //Si algo falla se hace rollback
    @Transactional(rollbackFor = Throwable.class)
    public Product createProduct(String name, String description, Size size, int stock, double price, double discount, Long categoryId, byte[] img) throws ProductDuplicateException {
        Product p = new Product(name, description, size, stock, price, discount, img);

        // Conseguimos data de usuario que realizo la request

        Authentication authdata = SecurityContextHolder.getContext().getAuthentication();
        if (authdata != null){

            UserDetails userDetails = (UserDetails) authdata.getPrincipal();

            Optional<User> user = userService.getUserByEmail(userDetails.getUsername());

            if (user.isPresent()){
                if (categoryId != null) {
                    Category c = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
                    p.setCategory(c);
                    p.setCreatorId(user.get().getId());
                }
                if (!productRepository.existsDuplicate(name, description, size, price)) {
                    return productRepository.save(p);
                }
            }

        }





        throw new ProductDuplicateException();
    }

    @Transactional
    @Override
    public Product changeProductInfo (Long prodId, String name, String description, Size size, Integer stock, Double price, Double discount, Long categoryId, byte[] img) throws ProductNotFoundException{
        Product p = productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException());

        if (name != null) p.setName(name);
        if (description != null) p.setDescription(description);
        if (size != null) p.setSize(size);
        if (stock != null) p.setStock(stock);
        if (price != null) p.setPrice(price);
        if (discount != null) p.setDiscount(discount);
        if (categoryId != null){
            Optional<Category> c = categoryRepository.findById(categoryId); 
            p.setCategory(c.get());
        }
        if(img != null){p.setImg(img);};

        return productRepository.save(p);
    }
    
    @Override
    public void deleteProduct(Long productId){
        Product p = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException());
        p.setActive(false);
        productRepository.save(p);
    }

    @Transactional
    @Override
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product updateImage(Long productId,  String base64img) {
        Product p = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());

        p.setImg(ImageManager.base64tobyteArray(base64img));
        return productRepository.save(p);
    }
}
