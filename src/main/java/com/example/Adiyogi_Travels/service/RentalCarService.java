package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.RentalCar;
import com.example.Adiyogi_Travels.repository.RentalCarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RentalCarService {
    private final RentalCarRepository repo;

    public List<RentalCar> getAllCars() {
        return repo.findAll();
    }
}
