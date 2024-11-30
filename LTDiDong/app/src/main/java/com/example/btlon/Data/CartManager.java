package com.example.btlon.Data;

import com.example.btlon.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<GioHang> cartList;

    private CartManager() {
        cartList = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<GioHang> getCartList() {
        return cartList;
    }


    public void addToCart(GioHang gioHang) {
        for (GioHang item : Utils.manggiohang) {
            if (item.getIdSp() == gioHang.getIdSp()) {
                item.setSoLuong(item.getSoLuong() + gioHang.getSoLuong());
                return;
            }
        }
        Utils.manggiohang.add(gioHang);
    }
}
