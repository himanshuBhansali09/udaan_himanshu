package com.example.demo.Controller;

import com.example.demo.Controller.dto.DealDTO;
import com.example.demo.Controller.dto.ProductDTO;
import com.example.demo.Controller.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DealRepository dealRepository;

    @PostMapping("/add/data")
    public void addData(){
        List<User> users = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        User user = new User();
        user.setFirtsName("Himanshu");
        user.setLastName("Bhansali");
        user.setProducts(products);
        users.add(user);
        Product product = new Product();
        product.setPrice(20);
        product.setProductName("p1");
        product.setUsers(users);
        products.add(product);
        userRepository.save(user);
    }

    @PostMapping("/add/deal")
    public void addDeal(@RequestBody DealDTO dealDTO) throws ParseException {
        Deal deal = new Deal();
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        List<Deal> deals = new ArrayList<>();
        deal.setDealName(dealDTO.getDealName());
        deal.setEndDate(dateFormat.parse(dealDTO.getEndDate()));
        deal.setStartDate(dateFormat.parse(dealDTO.getStartDate()));
        deal.setQuantity(dealDTO.getQuantity());
        deal.setActive(true);
        deals.add(deal);
        List<Product> products = productRepository.findAllById(dealDTO.getProductIds());
        deal.setProducts(products);
        dealRepository.save(deal);
    }

    @PostMapping("/end/deal")
    public void updateStatus(@RequestHeader(value = "id") Integer id) throws Exception {
        Optional<Deal> deal = dealRepository.findById(id);
        if (deal.isPresent()){
            deal.get().setActive(!deal.get().isActive());
            dealRepository.save(deal.get());
        }else {
            throw new Exception("Deal with this id is not present");
        }
    }

    @PutMapping("/update/deal")
    public void updateDeal(@RequestHeader(value = "id") Integer id,
                           @RequestHeader(value = "date", required = false) String endDate,
                           @RequestHeader(value = "quantity", required = false) Integer quantity) throws Exception {
        Optional<Deal> deal = dealRepository.findById(id);
        if (deal.isPresent() && deal.get().isActive()){
            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
            deal.get().setEndDate(endDate==null ? deal.get().getEndDate() : dateFormat.parse(endDate));
            deal.get().setQuantity(quantity==null ? deal.get().getQuantity() : quantity);
            dealRepository.save(deal.get());
        }else {
            throw new Exception("Deal with this id is not present");
        }
    }

    @PostMapping("/claim/deal")
    public void claimDeal(@RequestHeader(value = "id", required = false) Integer id,
                          @RequestHeader(value = "user_id", required = false) Integer userId) throws Exception {
        Optional<Deal> deal = dealRepository.findById(id);
        Optional<User> user = userRepository.findById(userId);
        if (deal.isPresent() && user.isPresent()){
            List<Product> products = user.get().getProducts();
            List<Product> productList = deal.get().getProducts();
            for (Product product : products){
                Optional<Product> product1 = productList.stream().filter(e-> e.getId().equals(product.getId())).findAny();
                if (product1.isPresent()){
                    throw new Exception("user already has claimed the deal");
                }
            }
            products.addAll(productList);
            deal.get().setQuantity(deal.get().getQuantity()-1);
            userRepository.save(user.get());
            dealRepository.save(deal.get());
        }
    }
}
