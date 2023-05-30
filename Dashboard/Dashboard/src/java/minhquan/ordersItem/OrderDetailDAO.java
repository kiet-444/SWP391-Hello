/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minhquan.ordersItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import minhquan.orders.OrdersDTO;
import minhquan.util.DBHelper;

/**
 *
 * @author Minh Quan
 */
public class OrderDetailDAO {

    public List<OrderDetailDTO> orderItemList;

    public List<OrderDetailDTO> getOrderItemList() {
        return orderItemList;
    }

    public float getIncome() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        OrderDetailDTO result = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT SUM(subtotal) AS Income FROM [MonShop].[dbo].[OrderItem]";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();

                if (rs.next()) {
                    float subtotal = rs.getFloat("Income");
                    return subtotal;
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
        return 0;
    }

    public void getTop5Product() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        OrderDetailDTO result = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT TOP (1000) [orderDetailID]"
                        + "      ,[quantity]"
                        + "      ,[price]"
                        + "      ,[productID]"
                        + "      ,[orderID]"
                        + "  FROM [BirdPlatform].[dbo].[OrderDetail] ORDER BY quantity DESC;";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();

                while (rs.next()) {
                    int orderDetailID = rs.getInt("orderDetailID");
                    int quantity = rs.getInt("quantity");
                    float price = rs.getFloat("price");
                    int productID = rs.getInt("productID");
                    float orderID = rs.getInt("orderID");
                    result = new OrderDetailDTO(orderDetailID, quantity, price, productID, orderID);
                    if (this.orderItemList == null) {
                        orderItemList = new ArrayList<>();
                    }
                    orderItemList.add(result);

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
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        OrderDetailDAO dao = new OrderDetailDAO();
        dao.getTop5Product();
        List<OrderDetailDTO> list = dao.getOrderItemList(); 
        System.out.println(list);
    }
}
