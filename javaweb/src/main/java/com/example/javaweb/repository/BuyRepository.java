package com.example.javaweb.repository;

import com.example.javaweb.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyRepository extends JpaRepository<Buy, Integer> {
}
