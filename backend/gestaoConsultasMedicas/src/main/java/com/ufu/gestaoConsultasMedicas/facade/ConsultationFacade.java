package com.ufu.gestaoConsultasMedicas.facade;

import com.ufu.gestaoConsultasMedicas.models.Consultation;
import com.ufu.gestaoConsultasMedicas.models.Doctor;
import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.service.ConsultationService;
import com.ufu.gestaoConsultasMedicas.service.DoctorService;
import com.ufu.gestaoConsultasMedicas.service.PatientService;
import com.ufu.gestaoConsultasMedicas.strategy.SearchDoctorByCrm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ConsultationFacade {

    private final ConsultationService consultationService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public ConsultationFacade(ConsultationService consultationService, DoctorService doctorService, PatientService patientService) {
        this.consultationService = consultationService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Optional<Consultation> scheduleConsultation(String patientCpf, String doctorCrm, LocalDate date, boolean isUrgent, String observation) {
        Optional<Patient> patient = patientService.getPatientByCpf(patientCpf);

        // Configura a estratégia de busca para CRM e busca o médico
        doctorService.setSearchStrategy(new SearchDoctorByCrm());
        List<Doctor> doctors = doctorService.searchDoctors(doctorCrm);
        Optional<Doctor> doctor = doctors.isEmpty() ? Optional.empty() : Optional.of(doctors.get(0));

        if (patient.isEmpty() || doctor.isEmpty()) {
            return Optional.empty(); // Retorna vazio se paciente ou médico não existirem
        }

        return Optional.of(consultationService.createConsultation(patient.get(), doctor.get(), date, isUrgent, observation));
    }

    public boolean cancelConsultationById(UUID consultationId) {
        return consultationService.cancelConsultation(consultationId);
    }
}
