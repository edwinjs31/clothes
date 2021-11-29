package com.albares.clothes.db;

import com.albares.clothes.utils.Db;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    private Integer id;
    private String name;
    private Integer price;
    private Integer stock;
    private Integer gender;

    private Integer quantity;

    public Product() {
        //this.quantity=0;
    }

    public Product(Integer id, String name, Integer price, Integer stock, Integer gender) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    //INSERT Producto
    public int insertAndGetId_DB(Db myDb) throws SQLException, NoSuchAlgorithmException {
        PreparedStatement ps = myDb.prepareStatement(
                "INSERT INTO products (name, price, stock, gender) VALUES (?, ?, ?, ?) returning id;"
        );
        ps.setString(1, this.getName());
        ps.setInt(2, this.getPrice());
        ps.setInt(3, this.getStock());
        ps.setInt(4, this.getGender());
        ResultSet rs = myDb.executeQuery(ps);
        rs.next();
        this.setId(rs.getInt(1));
        return this.getId();
    }

    //SELECT products
    public static List selectProducts_DB(Db myDb) throws SQLException {
        PreparedStatement ps = myDb.prepareStatement(
                "SELECT id, name, price, stock, gender FROM products WHERE stock >=1;");

        ResultSet rs = myDb.executeQuery(ps);
        List<Product> products = new ArrayList();

        while (rs.next()) {
            Product product = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
            products.add(product);
        }
        return products;
    }

    //UPDATE product
    public void updateProduct_DB(Db myDb) throws SQLException {

        if (this.getName() == null || this.getGender() == null) {
            PreparedStatement ps = myDb.prepareStatement("UPDATE products SET price=?, stock=? WHERE id = ?;");
            ps.setInt(1, this.getPrice());
            ps.setInt(2, this.getStock());
            ps.setInt(3, this.getId());
            ps.executeUpdate();
        } else {
            PreparedStatement ps = myDb.prepareStatement("UPDATE products SET name=?, price=?, stock=?, gender = ? WHERE id = ?;");
            ps.setString(1, this.getName());
            ps.setInt(2, this.getPrice());
            ps.setInt(3, this.getStock());
            ps.setInt(4, this.getGender());
            ps.setInt(5, this.getId());
            ps.executeUpdate();
        }

    }

    //UPDATE: devuelve el numero de registros actualizados
    public int updateStock(Db myDb, Product p) throws SQLException {
        //no resta la cantidad a comprar si el stock es inferior a dicha cantidad
        PreparedStatement ps = myDb.prepareStatement(
                "UPDATE products SET stock = stock - ? WHERE ( SELECT stock >= ? WHERE id = ?);"
        );
        ps.setInt(1, p.getQuantity());
        ps.setInt(2, p.getQuantity());
        ps.setInt(3, p.getId());
        return ps.executeUpdate();
    }

    //SELECT products con stock
    public List selectProductsStock_DB(Db myDb) throws SQLException {
        // SELECT id, name, price, stock, gender FROM products WHERE stock >= 1 and gender= 1;
        PreparedStatement ps = myDb.prepareStatement(
                "SELECT id, name, price, stock, gender FROM products WHERE stock>=1 AND gender=?;");
        ps.setInt(1, this.getGender());
        ResultSet rs = myDb.executeQuery(ps);
        List<Product> products = new ArrayList();

        while (rs.next()) {
            Product product = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
            products.add(product);
        }
        return products;
    }
}
