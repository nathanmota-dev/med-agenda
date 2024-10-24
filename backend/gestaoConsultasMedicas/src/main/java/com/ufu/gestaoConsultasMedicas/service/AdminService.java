package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Admin;
import com.ufu.gestaoConsultasMedicas.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<Admin> authenticateAdmin(String email, String rawPassword) {
        // Busca o admin pelo email
        Optional<Admin> admin = adminRepository.findByEmail(email);

        // Verifica se o admin existe e se a senha está correta
        if (admin.isPresent() && passwordEncoder.matches(rawPassword, admin.get().getPassword())) {
            return admin;
        }

        return Optional.empty();
    }

    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(UUID userId) {
        return adminRepository.findById(userId);
    }

    public void deleteAdmin(UUID userId) {
        adminRepository.deleteById(userId);
    }
}