package com.uade.tpo.demo.controllers.product;

import com.uade.tpo.demo.controllers.stock.StockAdjustRequest;
import com.uade.tpo.demo.controllers.stock.StockResponse;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.InvalidStockException;
import com.uade.tpo.demo.service.ProductService;
import com.uade.tpo.demo.service.StockService;
import com.uade.tpo.demo.controllers.purchase.PurchaseResponse;
import com.uade.tpo.demo.controllers.purchase.PurchaseRequest;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.service.PurchaseService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.net.URI;
import java.util.LinkedList;
import java.util.Optional;




/*


    Los metodos para get devuelven el objeto product response para poder enviar re codificar la imagen para el envio, esto tambien nos permite subir la category en las reponse
    Este metodo de respuesta permite modificar el formato de los get para devolver info diferente de la que tenemos almacenada

    Cualquier modificacion a la respuesta de algun producto debe ser modificada aca, no modificar el service

*/





@RestController
@RequestMapping("product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        Page<Product> result;

        if (page == null || size == null){
            result = productService.getProducts(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            result = productService.getProducts(PageRequest.of(page, size));
        }

        LinkedList<ProductResponse> responses = new LinkedList<>();

        for (Product product : result) {
            responses.add(new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategory().getId(),
                    product.getCategory().getDescription(),
                    product.getCreatorId(),
                    product.getSize(),
                    product.getStock(),
                    product.getPrice(),
                    product.getDiscount(),
                    ImageManager.fileToBase64(product.getImg())
            ));
        }

        return ResponseEntity.ok(new PageImpl<>(responses));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {

        Optional<Product> product = productService.getProductById(productId);
        ProductResponse productResponse = null;
        if (product.isPresent()){
            productResponse = new ProductResponse(
                    product.get().getId(),
                    product.get().getName(),
                    product.get().getDescription(),
                    product.get().getCategory().getId(),
                    product.get().getCategory().getDescription(),
                    product.get().getCreatorId(),
                    product.get().getSize(),
                    product.get().getStock(),
                    product.get().getPrice(),
                    product.get().getDiscount(),
                    ImageManager.fileToBase64(product.get().getImg())
            );
        }

        Optional<ProductResponse> result = Optional.of(productResponse);
        if (result.isPresent()){
            return ResponseEntity.ok(result.get());
        }

        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) {

        byte[] img = ImageManager.base64tobyteArray(productRequest.getBase64img());

        Product result = productService.createProduct(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getSize(),
                productRequest.getStock(),
                productRequest.getPrice(),
                productRequest.getDiscount(),
                productRequest.getCategoryId(),
                img
        );

        // System.out.println(result.getId());

        return ResponseEntity.created(URI.create("/product/" + result.getId())).body(result);
    }


    @PutMapping("/{productId}/modify")
    public ResponseEntity<Object> updateProduct(
        @PathVariable Long productId,
        @RequestBody ProductRequest req
    ) {

        try {
            Product updatedProduct = productService.changeProductInfo(
            productId,
            req.getName(),
            req.getDescription(),
            req.getSize(),
            req.getStock(),
            req.getPrice(),
            req.getDiscount(), 
            req.getCategoryId() 
        ); 
            return ResponseEntity.ok(updatedProduct); 
        } catch (ProductNotFoundException e){
            return ResponseEntity.status(404).body("Product not found"); 
        }
    }


    @DeleteMapping("/{productId}/delete") 
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().body("Product deleted");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    } 
    


    //STOCK

    //Method to get stock of a product by its ID
    // GET /product/{productId}/stock ---> Stock
    @GetMapping("/{productId}/stock")
    public ResponseEntity<StockResponse> getStock(
        @PathVariable Long productId){
            try{
                int stock = stockService.getStock(productId);
                return ResponseEntity.ok(new StockResponse(productId, stock));
            } catch (ProductNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
    }
    
    //Method to adjust stock (increase or decrease)
    // POST /product/{productId}/stock/adjust (body: {"quantity": -3} or "5" for restock )
    @PostMapping("/{productId}/stock/adjust")
    public ResponseEntity<StockResponse> adjustStock(
        @PathVariable Long productId,
        @RequestBody StockAdjustRequest request){
            try{
                int newStock = stockService.changeStock(productId, request.getQuantity());
                return ResponseEntity.ok(new StockResponse(productId, newStock));
            }catch (ProductNotFoundException e){
                return ResponseEntity.notFound().build();
        }
    }

    

    //PURCHASE

    // POST product/purchase
    // EXAMPLE Body: {"productId": 1, "quantity": 2}
    @PostMapping("/purchase") 
    public ResponseEntity<PurchaseResponse> purchaseProduct(
        @RequestBody PurchaseRequest request){
            try{
                PurchaseResponse resp = purchaseService.purchaseProduct(request);
                return ResponseEntity.ok(resp);
            }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().body(
                    new PurchaseResponse(null, null, null, null, null, null, null, null, e.getMessage())
                );
            }catch(ProductNotFoundException e){
                return ResponseEntity.status(404).body(
                    new PurchaseResponse(null, null, null, null, null, null, null, null, "Product not found")
                );
                
            } catch(InvalidStockException e){
                return ResponseEntity.badRequest().body(
                    new PurchaseResponse(null, null, null, null, null, null, null, null,"Insufficient stock for the requested quantity")
                    );
            }
    }
}
