package com.example.btlon.Data;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cart = new ArrayList<>();

    // Thêm sản phẩm vào giỏ hàng
    public static void addToCart(Product product) {
        if (!cart.contains(product)) { // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
            cart.add(product);
        }
    }

    // Lấy danh sách giỏ hàng (trả về bản sao của danh sách)
    public static List<Product> getCart() {
        return new ArrayList<>(cart); // Trả về bản sao của giỏ hàng
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public static void removeFromCart(Product product) {
        if (cart.contains(product)) { // Kiểm tra sản phẩm có trong giỏ hàng không
            cart.remove(product);
        }
    }

    // Tính tổng giá trị của giỏ hàng
    public static double getTotalPrice() {
        double total = 0;
        for (Product product : cart) {
            try {
                total += Double.parseDouble(product.getPrice()); // Chuyển giá thành double
            } catch (NumberFormatException e) {
                e.printStackTrace(); // In ra lỗi nếu không thể chuyển đổi giá trị
            }
        }
        return total;
    }

    // Kiểm tra giỏ hàng có trống không
    public static boolean isCartEmpty() {
        return cart.isEmpty();
    }
}
