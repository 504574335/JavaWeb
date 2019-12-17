package com.example.javaweb.controller;

import com.example.javaweb.entity.Buy;
import com.example.javaweb.entity.Goods;
import com.example.javaweb.entity.SendEmailService;
import com.example.javaweb.entity.User;
import com.example.javaweb.repository.BuyRepository;
import com.example.javaweb.repository.GoodsRepository;
import com.example.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class GoodsController {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    BuyRepository buyRepository;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/goods_query")
    public String query(HttpSession session, @RequestParam("goods_name") String goods_name){
        session.setAttribute("name", goods_name);
        session.setAttribute("jump", "jump");
        return "redirect:/index.html";
    }

    @GetMapping("/goods_delete")
    public String delete(HttpSession session, Integer id){
        goodsRepository.deleteById(id);
        session.setAttribute("goods_delete", "goods_delete");
        return "redirect:/forms.html";
    }

    @GetMapping("/goods_back")
    public String back(HttpSession session){
        session.setAttribute("jump", "jump");
        return "redirect:/index.html";
    }

    @PostMapping("/add_goods")
    public String insertGoods(HttpSession session, @ModelAttribute Goods goods){
        goods.setImage("image/liebiao_xiaomi6.jpg");
        goodsRepository.save(goods);
        session.setAttribute("add_goods", "add_goods");
        return "redirect:/forms.html";
    }

    @PostMapping("/revise_goods")
    public String reviseGoods(HttpSession session, @RequestParam("name") String name, @RequestParam("price") String price,
                              @RequestParam("num") String num, @RequestParam("activity") String activity, @RequestParam("detail") String detail){
        List<Goods> goods = goodsRepository.findByName(name);
        if(!price.equals("keep")) goods.get(0).setPrice(Integer.parseInt(price));
        if(!num.equals("keep")) goods.get(0).setNum(Integer.parseInt(num));
        if(!activity.equals("keep")) goods.get(0).setActivity(activity);
        if(!detail.equals("keep")) goods.get(0).setDetail(detail);
        goodsRepository.save(goods.get(0));
        session.setAttribute("revise", "revise");
        return "redirect:/forms.html";
    }

    @GetMapping("/sendGoods")
    public String sendGoods(Integer id, HttpSession session){
        List<Buy> buys = buyRepository.findAll();
        int i = 0;
        while (i<buys.size()){
            if(buys.get(i).getId().intValue()==id) break;
            i++;
        }
        buys.get(i).setState("已发货");
        buyRepository.save(buys.get(i));
        List<Goods> goods = goodsRepository.findByName(buys.get(i).getGoodsName());
        if(goods.size()!=0) {
            goods.get(0).setNum(goods.get(0).getNum() - buys.get(i).getNum());
            goodsRepository.save(goods.get(0));
        }
        List<User> users = userRepository.findByName(buys.get(i).getUserName());
        System.out.println(users.get(0).getEmail());
        StringBuilder builder = new StringBuilder();
        builder.append("您购买的商品信息为： 商品名： ");
        builder.append(buys.get(i).getGoodsName());
        builder.append("  数量： ");
        builder.append(buys.get(i).getNum());
        builder.append("  总价： ");
        builder.append(buys.get(i).getTotal());
        builder.append("   商品已发货，请耐心等待。 ");
        String sender="504574335@qq.com";
        String receiver = users.get(0).getEmail();
        String title="您购买的物品已发货！发货人：晏易茂";
        String text = builder.toString();
        try{
            sendEmailService.send(sender, receiver, title, text);
        } catch (Exception e){
            System.out.println("邮件发送失败！");
        }
        session.setAttribute("send","send");
        return "redirect:/records.html";
    }
}
