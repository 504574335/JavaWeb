package com.example.javaweb.controller;

import com.example.javaweb.entity.Cart;
import com.example.javaweb.entity.Goods;
import com.example.javaweb.repository.CartRepository;
import com.example.javaweb.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    GoodsRepository goodsRepository;

    @GetMapping("/add_cart")
    public String  add_cart(@RequestParam("user_name") String user_name, @RequestParam("good_name") String good_name, @RequestParam("price") Integer price ){
        System.out.println(good_name);
        System.out.println(user_name);
        System.out.println(price);
        List<Cart> carts = cartRepository.findByGoodsNameAndUserName(good_name, user_name);
        List<Goods> goods = goodsRepository.findByName(good_name);
        if(carts.size()==0){
            Cart new_cart = new Cart();
            new_cart.setGoodsName(good_name);
            new_cart.setUserName(user_name);
            new_cart.setPrice(price);
            new_cart.setNum(1);
            new_cart.setTotal(price);
            new_cart.setImage(goods.get(0).getImage());
            cartRepository.save(new_cart);
        }
        else {
            int num = carts.get(0).getNum();
            int one_price = carts.get(0).getPrice();
            carts.get(0).setNum(num+1);
            carts.get(0).setTotal(carts.get(0).getTotal()+one_price);
            cartRepository.save(carts.get(0));
        }
        return "redirect:/cart.html";
    }

    @GetMapping("/carts_delete")
    public String delete(Integer id){
        cartRepository.deleteById(id);
        return "redirect:/cart.html";
    }
}
