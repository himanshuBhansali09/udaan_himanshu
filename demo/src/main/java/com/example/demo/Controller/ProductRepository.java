package com.example.demo.Controller;

import com.example.demo.Controller.entity.Product;
import com.example.demo.Controller.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
