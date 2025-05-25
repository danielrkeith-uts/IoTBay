package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import model.Product;
import model.Enums.ProductType;

public class ProductDBManagerTests {
    Connection conn;
    ProductDBManager productDBManager;

    public ProductDBManagerTests() throws ClassNotFoundException, SQLException {
        conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        productDBManager = new ProductDBManager(conn);
    }

    @Test
    public void testGetProduct() {
        Product product;
        try {
            product = productDBManager.getProduct(0);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        Assert.assertEquals(99.99, product.getCost(), 0.001);
        Assert.assertEquals("Raspberry Pi", product.getName());
        Assert.assertEquals(3, product.getStock());
    }

    @Test
    public void testSearchByProductName() {
        try {
            List<Product> allProducts = productDBManager.getAllProducts();
            String query = "google home voice controller";

            List<Product> matchedProducts = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

                if (matchedProducts.isEmpty()) {
                    Assert.fail("No products matched the search query: " + query);
                }

            matchedProducts.forEach(p ->
                Assert.assertTrue(p.getName().toLowerCase().contains(query.toLowerCase()))
            );

        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testSearchByProductType() {
        try {
            List<Product> allProducts = productDBManager.getAllProducts();
            String typeQuery = "COMPONENTS";

            List<Product> matchedProducts = allProducts.stream()
                .filter(p -> p.getType().name().equalsIgnoreCase(typeQuery))
                .collect(Collectors.toList());

            if (matchedProducts.isEmpty()) {
                Assert.fail("No products matched the product type: " + typeQuery);
            }

            matchedProducts.forEach(p ->
                Assert.assertEquals(typeQuery.toUpperCase(), p.getType().name())
            );

        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testViewProductDetails() {
        try {
            int productId = 0;
            Product product = productDBManager.getProduct(productId);

            if (product == null) {
                Assert.fail("Product with ID " + productId + " not found.");
            }

            Assert.assertEquals("Raspberry Pi", product.getName());
            Assert.assertNull(product.getDescription());
            Assert.assertEquals(model.Enums.ProductType.ELECTRONICS, product.getType());
            Assert.assertEquals(99.99, product.getCost(), 0.001);
            Assert.assertEquals(3, product.getStock());
            Assert.assertEquals("https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcSksSIv20lOBy1zAyO0r-tDFlUFCiE-8pTyqFT0WbtlUfqwt2yT31aY2_xRoCbjdcSu_FPJgL2Y", product.getImageUrl());

        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testRetrieveProductStock() {
        try {
            Product product = productDBManager.getProduct(0);
            int stock = product.getStock();
            Assert.assertTrue("Stock should be a non-negative number", stock >= 0);
        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateStockLevel() {
        try {
            Product product = productDBManager.getProduct(0); 
            int originalStock = product.getStock();
            int newStock = originalStock + 10;
    
            product.setStock(newStock);
            productDBManager.updateProduct(product.getName(), product);
    
            Product updated = productDBManager.getProduct(0);
            Assert.assertEquals(newStock, updated.getStock());
    
            // Reset
            product.setStock(originalStock);
            productDBManager.updateProduct(product.getName(), product);
        } catch (SQLException e) {
            Assert.fail("SQLException occurred: " + e.getMessage());
        }
        finally {
            try {
                conn.rollback();
            } catch (SQLException ignore) { }
        }
    }

    @Test
    public void testAddNewProduct() {
        try {
            Product newProduct = new Product(
                "ProductTest",     
                "Test description",   
                ProductType.ELECTRONICS, 
                49.99,                
                15,                   
                "https://example.com/image.jpg" 
            );
            
            productDBManager.addProduct(newProduct);

            Product retrieved = productDBManager.getProductByName("ProductTest");
            
            Assert.assertNotNull(retrieved);
            Assert.assertEquals("ProductTest", retrieved.getName());
            Assert.assertEquals("Test description", retrieved.getDescription());
            Assert.assertEquals(ProductType.ELECTRONICS, retrieved.getType());
            Assert.assertEquals(49.99, retrieved.getCost(), 0.01);
            Assert.assertEquals(15, retrieved.getStock());
            Assert.assertEquals("https://example.com/image.jpg", retrieved.getImageUrl());

            productDBManager.deleteProduct("TestProduct123");

        } catch (SQLException e) {
            Assert.fail("SQLException occurred: " + e.getMessage());
        }
        finally {
            try {
                conn.rollback();
            } catch (SQLException ignore) { }
        }
    }

    @Test
    public void testRemoveProduct() {
        try {
            Product discontinuedProduct = new Product(
                "DiscontinuedTestProduct",
                "This product will be discontinued",
                ProductType.COMPONENTS,  
                19.99,
                0,
                "https://example.com/discontinued.jpg"
            );
            
            productDBManager.addProduct(discontinuedProduct);

            Product addedProduct = productDBManager.getProductByName("DiscontinuedTestProduct");
            Assert.assertNotNull(addedProduct);
            
            productDBManager.deleteProduct("DiscontinuedTestProduct");
            
            Product deletedProduct = productDBManager.getProductByName("DiscontinuedTestProduct");
            Assert.assertNull(deletedProduct);
            
        } catch (SQLException e) {
            Assert.fail("SQLException occurred: " + e.getMessage());
        }
        finally {
            try {
                conn.rollback();
            } catch (SQLException ignore) { }
        }
    }

    @Test
    public void testUpdateProductDetails() {
        try {
            Product product = productDBManager.getProduct(0);  // assuming productId 0 exists
            
            String originalDescription = product.getDescription();
            String newDescription = "Updated description";
            
            product.setDescription(newDescription);
            productDBManager.updateProduct(product.getName(), product);
            
            Product updatedProduct = productDBManager.getProduct(0);
            Assert.assertEquals(newDescription, updatedProduct.getDescription());
            
            product.setDescription(originalDescription);
            productDBManager.updateProduct(product.getName(), product);
            
        } catch (SQLException e) {
            Assert.fail("SQLException occurred: " + e.getMessage());
        }
        finally {
            try {
                conn.rollback();
            } catch (SQLException ignore) { }
        }
    }
}
