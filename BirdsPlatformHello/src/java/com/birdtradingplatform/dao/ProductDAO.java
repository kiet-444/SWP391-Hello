/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.birdtradingplatform.dao;

import com.birdtradingplatform.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.birdtradingplatform.dao.OrderDetailDAO;
import com.birdtradingplatform.model.OrderDetail;
import com.birdtradingplatform.model.Shop;
import minhquan.util.DBHelper;

/**
 *
 * @author Minh Quan
 */
public class ProductDAO {

    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public ArrayList<Product> printProductList() throws ClassNotFoundException, SQLException {
        ArrayList<Product> productDashList = new ArrayList<>();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT * FROM Product";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();

                while (rs.next()) {
                    int productID = rs.getInt("productID");
                    String productName = rs.getString("productName");
                    float priceIn = rs.getFloat("priceIn");
                    String type = rs.getString("type");
                    String category = rs.getString("category");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String img = rs.getString("img");
                    String sku = rs.getString("sku");
                    int shopID = rs.getInt("shopID");
                    float priceOut = rs.getFloat("priceOut");
                    float pSale = rs.getFloat("pSale");
                    Product result = new Product(productID, productName, priceIn, type, category, quantity, description, status, img, sku, null, priceOut, pSale);
                    productDashList.add(result);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return productDashList;
    }

    public void searchProduct(List<OrderDetail> orderDetailsList) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Product result = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT * From Product WHERE productID = ?";
                stm = con.prepareStatement(sql);
                for (OrderDetail orderDetail : orderDetailsList) {
                    stm.setInt(1, orderDetail.getProductID());
                    rs = stm.executeQuery();
           
                while (rs.next()) {

                    int productID = rs.getInt("productID");
                    String productName = rs.getString("productName");
                    float priceIn = rs.getFloat("priceIn");
                    String type = rs.getString("type");
                    String category = rs.getString("category");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String img = rs.getString("img");
                    String sku = rs.getString("sku");
                    int shopID = rs.getInt("shopID");
                    float priceOut = rs.getFloat("priceOut");
                    float pSale = rs.getFloat("pSale");

                    result = new Product(productID, productName, priceIn, type, category, quantity, description, status, img, sku, null, priceOut, pSale);
                    if (this.productList == null) {
                        productList = new ArrayList<>();
                    }
                    this.productList.add(result);
                }
                    }
            }
        } catch (Exception e) {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public Product getProduct(String id) throws SQLException {
        Product product = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = DBHelper.makeConnection();
            if (conn != null) {
                String sql = "select *"
                        + " from [Product] left join [Shop] on "
                        + "product.shopID = shop.shopID where productID = ?";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, id);

                rs = pstm.executeQuery();
                if (rs.next()) {

                    product = new Product(rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getDouble("priceIn"),
                            rs.getString("type"),
                            rs.getString("category"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("img"),
                            rs.getString("sku"),
                            new Shop(rs.getInt("shopID"),
                                    rs.getString("shopName"),
                                    rs.getString("avatar"),
                                    rs.getDouble("rate"),
                                    rs.getString("contact"),
                                    rs.getInt("accountID"),
                                    rs.getInt("addressID")),
                            rs.getDouble("priceOut"),
                            rs.getDouble("pSale"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return product;
    }

    public List<Product> getSuggestion(String relation, int limit, int page) throws SQLException {
        List<Product> suggestedList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "select *"
                        + " from [Product] left join [Shop] on "
                        + " product.shopID = shop.shopID where category = ?"
                        + " order by rate " + " offset " + (page - 1) * limit + " rows "
                        + " fetch next ? rows only";
                pstm = con.prepareStatement(sql);
                pstm.setString(1, relation);
                pstm.setInt(2, limit);

                rs = pstm.executeQuery();
                while (rs.next()) {
                    suggestedList.add(new Product(rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getDouble("priceIn"),
                            rs.getString("type"),
                            rs.getString("category"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("img"),
                            rs.getString("sku"),
                            new Shop(rs.getInt("shopID"),
                                    rs.getString("shopName"),
                                    rs.getString("avatar"),
                                    rs.getDouble("rate"),
                                    rs.getString("contact"),
                                    rs.getInt("accountID"),
                                    rs.getInt("addressID")),
                            rs.getDouble("priceOut"),
                            rs.getDouble("pSale")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return suggestedList;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        ProductDAO productDAO = new ProductDAO();
        OrderDetailDAO dao = new OrderDetailDAO();
        dao.getTop5Product();
        List<OrderDetail> orderList = dao.getOrderItemList();
        productDAO.searchProduct(orderList);
        List<Product> list = productDAO.getProductList();
        System.out.println(list.get(1).getProductName());
    }
}