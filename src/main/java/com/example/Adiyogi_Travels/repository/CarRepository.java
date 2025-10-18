package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}

