package com.ecommerce.usersservice.service;

import com.ecommerce.usersservice.model.User;
import com.ecommerce.usersservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Konstruktor — Spring automatski ubacuje UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Vrati sve korisnike
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Vrati korisnika po ID-ju, ili baci grešku ako ne postoji
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Korisnik sa ID " + id + " nije pronađen"));
    }

    // Kreiraj novog korisnika
    public User create(User user) {
        return userRepository.save(user);
    }

    // Izmeni postojećeg korisnika
    public User update(Long id, User updatedUser) {
        User existing = findById(id); // ako ne postoji, baciće grešku
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setEmail(updatedUser.getEmail());
        existing.setAddress(updatedUser.getAddress());
        return userRepository.save(existing);
    }

    // Obriši korisnika po ID-ju
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Korisnik sa ID " + id + " nije pronađen");
        }
        userRepository.deleteById(id);
    }
}