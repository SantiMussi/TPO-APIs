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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PurchaseService purchaseService;

    private final String UPLOAD_DIR = "uploads/products/";

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        if (page == null || size == null)
            return ResponseEntity.ok(productService.getProducts(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(productService.getProducts(PageRequest.of(page, size)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }     


    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) {
        Product result = productService.createProduct(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getSize(),
                productRequest.getStock(),
                productRequest.getPrice(),
                productRequest.getDiscount(),
                productRequest.getCategoryId()
        );

        System.out.println(result.getId());

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
                    new PurchaseResponse(null, null, null, null, e.getMessage())
                );
            }catch(ProductNotFoundException e){
                return ResponseEntity.status(404).body(
                    new PurchaseResponse(null, null, null, null, "One or more products not found")
                );
                
            } catch(InvalidStockException e){
                return ResponseEntity.badRequest().body(
                    new PurchaseResponse(null, null, null, null, "Insufficient stock for one or more products")
                    );
            }
    }

    // Post de imagenes de productos
    @PostMapping("/{productId}/image")
    public ResponseEntity<Object> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ){
        try{
            String contentType = file.getContentType();

            if (contentType == null || !contentType.contains("image")) {
                return ResponseEntity.badRequest().body("File must be an image");
            }

            if (file.getSize() > 5 * 1024 * 1024){
                return ResponseEntity.badRequest().body("File is too large");
            }

            Product product = productService.getProductById(productId).orElseThrow(ProductNotFoundException::new);

            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String fileName = generateFileName(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName).normalize();
            String oldImage = product.getImageUrl();

            file.transferTo(filePath.toFile());

            String imageUrl = "/images/" + fileName;
            productService.updateImage(productId, imageUrl);

            if (oldImage != null && !oldImage.isEmpty()){
                try{

                    deleteOldImage(oldImage);
                } catch (IOException e) {
                    System.err.println("Warning: Error deleting old image");
                }
            }

            return ResponseEntity.ok().body("Image uploaded");

        } catch(ProductNotFoundException e){
            return ResponseEntity.status(404).body("Product not found");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving image to disk");
        }
    }

    // Get de la imagen
    @GetMapping("/images/{filename}")
    public ResponseEntity<Object> getImage(
            @PathVariable String filename
    ){
        try {
            Path filePath = Paths.get(UPLOAD_DIR).toAbsolutePath().resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()){
                String contentType = Files.probeContentType(filePath);
                if (contentType == null){
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body("Invalid file name");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading image file");
        }
    }

    // Delete de la imagen
    @DeleteMapping("/{productId}/image")
    public ResponseEntity<Object> deleteImage(
            @PathVariable Long productId
    ){
        try{
            Product product = productService.getProductById(productId).orElseThrow((ProductNotFoundException::new));

            String oldImage = product.getImageUrl();
            if (oldImage == null || oldImage.isEmpty()){
                return ResponseEntity.badRequest().body("Product has no image");
            }

            deleteOldImage(oldImage);

            productService.updateImage(productId, null);

            return ResponseEntity.ok().body("Image deleted from product " + productId);

        } catch (ProductNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting image");
        }
    }

    private String generateFileName(String originalName){
        String extension = ".jpg";
        if (originalName != null && originalName.contains(".")){
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }

    private void deleteOldImage(String imageUrl) throws IOException {
        String fileName = imageUrl.replace("/images/", "");
        Path oldFile = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize().resolve(fileName);
        if (Files.exists(oldFile)){
            Files.delete(oldFile);
        }
    }
}
