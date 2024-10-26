package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.repository.DoctorRepository;
import com.ufu.gestaoConsultasMedicas.strategy.DoctorSearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private DoctorSearchStrategy searchStrategy;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

        public void setSearchStrategy(DoctorSearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> updateDoctor(String crm, Doctor updatedDoctor) {
        return doctorRepository.findById(crm).map(doctor -> {
            doctor.setName(updatedDoctor.getName());
            doctor.setSpecialty(updatedDoctor.getSpecialty());
            doctor.setTelephone(updatedDoctor.getTelephone());
            return doctorRepository.save(doctor);
        });
    }

    public boolean deleteDoctor(String crm) {
        if (doctorRepository.existsById(crm)) {
            doctorRepository.deleteById(crm);
            return true;
        }
        return false;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctors(String keyword) {
        List<Doctor> allDoctors = getAllDoctors();
        return searchStrategy.search(keyword, allDoctors);
    }
}
