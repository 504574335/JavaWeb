package com.example.javaweb.controller;

import com.example.javaweb.entity.*;
import com.example.javaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class AllController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ViewsRepository viewsRepository;
    @Autowired
    BuyRepository buyRepository;


    @GetMapping("/login.html")
    public String login(HttpServletRequest request, ModelMap modelMap){
        Object old_user = request.getSession().getAttribute("loginUser");
        Object register_success = request.getSession().getAttribute("registerSuccess");
        Object login_failed = request.getSession().getAttribute("loginFailed");
        Object no_login = request.getSession().getAttribute("no-login");
        if(old_user!=null){ request.getSession().removeAttribute("loginUser"); }
        if(register_success!=null){
            request.getSession().removeAttribute("registerSuccess");
            modelMap.addAttribute("message", "注册新用户成功！请登录！");
        }
        if(login_failed!=null){
            request.getSession().removeAttribute("loginFailed");
            modelMap.addAttribute("message", "用户名或密码错误！请重新输入！");
        }
        if(no_login!=null){
            request.getSession().removeAttribute("no-login");
            modelMap.addAttribute("message", "请先登录！");
        }
        return "login";
    }

    @PostMapping("/log_in")
    public String systemLogin(User user, HttpSession session){
        List<User> users = userRepository.findAll();
        int j = 0;
        int loginNum = 0;
        while(j<users.size()){
            if((user.getName().equals(users.get(j).getName()) || user.getName().equals(users.get(j).getEmail()))
                    && user.getPassword().equals(users.get(j).getPassword())){
                loginNum = 1;
                break;
            }
            else j++;
        }
        if(loginNum == 1){
            session.setAttribute("loginUser", users.get(j).getName());
            return "redirect:/index.html";
        }
        else{
            session.setAttribute("loginFailed", "登录失败！");
            return "redirect:/login.html";
        }
    }

    @GetMapping("/register.html")
    public String register(){
        return "register";
    }

    @GetMapping("/")
    public String setIndex(){ return "redirect:/index.html"; }

    @GetMapping("/index.html")
    public String index(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        List<Goods> goods = goodsRepository.findAll();
        Object user = request.getSession().getAttribute("loginUser");
        Object name = request.getSession().getAttribute("name");
        Object jump = request.getSession().getAttribute("jump");
        Object limit = request.getSession().getAttribute("limit");
        if(user==null){
            session.setAttribute("no-login", "请先登录！");
            return "redirect:/login.html";
        }
        else{
            modelMap.addAttribute("WelcomeUser", user.toString());
            if(name==null) modelMap.addAttribute("goods", goods);
            else{
                request.getSession().removeAttribute("name");
                int i = 0;
                while(i<goods.size()){
                    if(goods.get(i).getName().contains(name.toString())){
                        i++;
                    }
                    else goods.remove(i);
                }
                modelMap.addAttribute("goods", goods);
            }
            if(jump!=null){
                modelMap.addAttribute("jumpList", "jumpList");
                request.getSession().removeAttribute("jump");
            }
            if(limit!=null){
                modelMap.addAttribute("limit", "limit");
                request.getSession().removeAttribute("limit");
            }
            return "index";
        }
    }

    @GetMapping("/cart.html")
    public String cart(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        List<Cart> carts = cartRepository.findAll();
        Object user = request.getSession().getAttribute("loginUser");
        Object pay = request.getSession().getAttribute("pay");
        if(user==null){
            session.setAttribute("no-login", "请先登录！");
            return "redirect:/login.html";
        }
        else {
            modelMap.addAttribute("WelcomeUser", user.toString());
            int i = 0;
            int totalPrice = 0;
            while(i<carts.size()){
                if(carts.get(i).getUserName().equals(user.toString())){
                    totalPrice = totalPrice + carts.get(i).getTotal();
                    i++;
                }
                else carts.remove(i);
            }
            modelMap.addAttribute("carts",carts);
            modelMap.addAttribute("listNum", carts.size());
            modelMap.addAttribute("totalPrice", totalPrice);
        }
        if(pay!=null){
            System.out.println("pay!");
            modelMap.addAttribute("pay","pay");
            request.getSession().removeAttribute("pay");
        }
        return "cart";
    }

    @GetMapping("/pay")
    public String pay(@RequestParam("name") String user_name, HttpSession session){
        List<Cart> carts = cartRepository.findByUserName(user_name);
        System.out.println(carts);
        int i = 0;
        while(i<carts.size()){
            Buy buy = new Buy();
            buy.setUserName(carts.get(i).getUserName());
            buy.setGoodsName(carts.get(i).getGoodsName());
            buy.setNum(carts.get(i).getNum());
            buy.setPrice(carts.get(i).getPrice());
            buy.setTotal(carts.get(i).getTotal());
            buy.setState("未发货");
            buyRepository.save(buy);
            cartRepository.delete(carts.get(i));
            i++;
        }
        session.setAttribute("pay","pay");
        return "redirect:/cart.html";
    }

    @GetMapping("/forms.html")
    public String form(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        List<Goods> goods = goodsRepository.findAll();
        Object user = request.getSession().getAttribute("loginUser");
        Object revise = request.getSession().getAttribute("revise");
        Object add_goods = request.getSession().getAttribute("add_goods");
        Object goods_delete = request.getSession().getAttribute("goods_delete");
        if(user==null){
            session.setAttribute("no-login", "请先登录！");
            return "redirect:/login.html";
        }
        else{
            if(user.toString().equals("Mao")){
                modelMap.addAttribute("WelcomeUser", user.toString());
                modelMap.addAttribute("goods", goods);
                if (revise!=null){
                    modelMap.addAttribute("revise", "revise");
                    request.getSession().removeAttribute("revise");
                }
                if (add_goods!=null){
                    modelMap.addAttribute("add_goods", "add_goods");
                    request.getSession().removeAttribute("add_goods");
                }
                if (goods_delete!=null){
                    modelMap.addAttribute("goods_delete", "goods_delete");
                    request.getSession().removeAttribute("goods_delete");
                }
                return "forms";
            }
            else {
                session.setAttribute("limit", "limit");
                return "redirect:/index.html";
            }
        }
    }

    @GetMapping("/records.html")
    public String records(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        Object user = request.getSession().getAttribute("loginUser");
        Object send = request.getSession().getAttribute("send");
        List<Buy> buys = buyRepository.findAll();
        List<Views> views = viewsRepository.findAll();
        if(user==null){
            session.setAttribute("no-login", "请先登录！");
            return "redirect:/login.html";
        }
        else{
            modelMap.addAttribute("WelcomeUser", user.toString());
            modelMap.addAttribute("buys", buys);
            modelMap.addAttribute("views", views);
            if(send!=null){
                modelMap.addAttribute("send","send");
                request.getSession().removeAttribute("send");
            }
            return "records";
        }
    }

    @GetMapping("/sales.html")
    public String sales(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        Object user = request.getSession().getAttribute("loginUser");
        if(user==null){
            session.setAttribute("no-login", "请先登录！");
            return "redirect:/login.html";
        }
        else {
            modelMap.addAttribute("WelcomeUser", user.toString());
            ArrayList name_list = new ArrayList();
            ArrayList num_list = new ArrayList();
            List<Goods> goods = goodsRepository.findAll();
            List<Buy> buys = buyRepository.findAll();
            int i = 0;
            while(i<goods.size()){
                name_list.add(goods.get(i).getName());
                int count = 0;
                int j = 0;
                while(j<buys.size()){
                    if(goods.get(i).getName().equals(buys.get(j).getGoodsName())) count = count + buys.get(j).getNum();
                    j++;
                }
                num_list.add(count);
                i++;
            }
            System.out.println(name_list);
            System.out.println(num_list);
            modelMap.addAttribute("nameList", name_list);
            modelMap.addAttribute("numList", num_list);
            return "sales";
        }
    }

    @GetMapping("/addViews")
    public String addViews(@RequestParam("user") String user_name, @RequestParam("good_name") String good_name, ModelMap modelMap){
        Views views = new Views();
        views.setUserName(user_name);
        views.setGoodsName(good_name);
        System.out.println(new Date());
        views.setTime(new Date());
        viewsRepository.save(views);
        List<Goods> goods = goodsRepository.findByName(good_name);
        System.out.println(goods.get(0).getName());
        modelMap.addAttribute("goods", goods);
        modelMap.addAttribute("WelcomeUser", user_name);
        return "details";
    }

    @GetMapping("/delete_view")
    public String delete_view(Integer id){
        viewsRepository.deleteById(id);
        return "redirect:/records.html";
    }

    @GetMapping("/delete_buy")
    public String delete_buy(Integer id){
        buyRepository.deleteById(id);
        return "redirect:/records.html";
    }
}
