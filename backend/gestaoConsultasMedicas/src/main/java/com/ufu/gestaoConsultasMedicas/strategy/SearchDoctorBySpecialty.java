package com.ufu.gestaoConsultasMedicas.strategy;

import com.ufu.gestaoConsultasMedicas.models.Doctor;
import java.util.List;
import java.util.stream.Collectors;

public class SearchDoctorBySpecialty implements DoctorSearchStrategy {
    @Override
    public List<Doctor> search(String keyword, List<Doctor> doctors) {
        return doctors.stream()
                .filter(doctor -> doctor.getSpecialty().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }
}
