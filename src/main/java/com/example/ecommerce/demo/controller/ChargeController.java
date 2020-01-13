package com.example.ecommerce.demo.controller;

import com.example.ecommerce.demo.model.ChargeRequest;
import com.example.ecommerce.demo.service.StripeService;
import com.example.ecommerce.demo.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
//import java.sql.Timestamp;
    //trying to mess with displaying a timestamp of the order placement on the results page

@Controller
public class ChargeController {

    @Autowired
    private StripeService paymentsService;

    @Autowired
    UserService userService;

    //Timestamp timestamp;

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model)
            throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.USD);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        //model.addAttribute("timestamp", timestamp.toInstant());
        userService.getLoggedInUser().getCart().clear();
        userService.updateCart(userService.getLoggedInUser().getCart());
        return "result";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}