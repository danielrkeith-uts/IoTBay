package model.dao;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import model.Product;

public class ProductDBManagerTests {
    ProductDBManager productDBManager;

    public ProductDBManagerTests() throws ClassNotFoundException, SQLException {
        productDBManager = new ProductDBManager(new DBConnector().openConnection());
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
}
