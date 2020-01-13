package com.example.ecommerce.demo.controller;

import com.example.ecommerce.demo.model.ChargeRequest;
import com.example.ecommerce.demo.model.Product;
import com.example.ecommerce.demo.model.User;
import com.example.ecommerce.demo.service.ProductService;
import com.example.ecommerce.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice
public class CartController {
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @ModelAttribute("loggedInUser")
    public User loggedInUser() {
        return userService.getLoggedInUser();
    }

    @ModelAttribute("cart")
    public Map<Product, Integer> cart() {
        User user = loggedInUser();
        if(user == null) return null;
        System.out.println("Getting cart");
        return user.getCart();
    }

    /**Puts an empty list in the model that thymeleaf can use to sum up the cart total.*/
    @ModelAttribute("list")
    public List<Double> list() {
        return new ArrayList<>();
    }

//    the next two @ModelAttributes protect against when you edit the cart and are told
//    during checkout that they don't have a public stripe key

    @ModelAttribute("stripePublicKey")
    public String stripePublicKey () { return stripePublicKey; }

    @ModelAttribute("currency")
    public ChargeRequest.Currency getCurrency() {return ChargeRequest.Currency.USD; }

    @GetMapping("/cart")
    public String showCart(Model model) {
        double total = 0;
//        sets total payment in checkout
        for(Map.Entry<Product, Integer> e: cart().entrySet()) total += e.getKey().getPrice() *e.getValue();
        model.addAttribute("amount", (long)total * 100);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.USD);
        return "cart";
    }

    @PostMapping("/cart")
    public String addToCart(@RequestParam long id) {
        Product p = productService.findById(id);
        setQuantity(p, cart().getOrDefault(p, 0) + 1);
        return "cart";
    }

    @PatchMapping("/cart")
    public String updateQuantities(@RequestParam long[] id, @RequestParam int[] quantity) {
        for(int i = 0; i < id.length; i++) {
            Product p = productService.findById(id[i]);
            setQuantity(p, quantity[i]);
        }
        return "cart";
    }

    @DeleteMapping("/cart")
    public String removeFromCart(@RequestParam long id) {
        Product p = productService.findById(id);
        setQuantity(p, 0);
        return "cart";
    }

    private void setQuantity(Product p, int quantity) {
        if(quantity > 0)
            cart().put(p, quantity);
        else
            cart().remove(p);

        userService.updateCart(cart());
    }
}