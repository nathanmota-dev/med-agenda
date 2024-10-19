package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Admin;
import com.ufu.gestaoConsultasMedicas.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> authenticateAdmin(String email, String password) {
        // Busca o admin pelo email
        Optional<Admin> admin = adminRepository.findByEmail(email);

        // Verifica se o admin existe e se a senha está correta
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return admin;
        }

        return Optional.empty();
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(UUID userId) {
        return adminRepository.findById(userId);
    }

    public void deleteAdmin(UUID userId) {
        adminRepository.deleteById(userId);
    }
}
