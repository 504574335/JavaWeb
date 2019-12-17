package com.example.javaweb.repository;

import com.example.javaweb.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByGoodsNameAndUserName(String goodsName, String userName);
    List<Cart> findByUserName(String name);
}
