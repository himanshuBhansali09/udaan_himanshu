package com.example.demo.Controller;

import com.example.demo.Controller.entity.Home;
import com.example.demo.Controller.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
