package com.ufu.gestaoConsultasMedicas.strategy;

import com.ufu.gestaoConsultasMedicas.models.Doctor;
import java.util.List;

public interface DoctorSearchStrategy {
    List<Doctor> search(String keyword, List<Doctor> doctors);
}
