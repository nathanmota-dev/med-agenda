package com.ufu.gestaoConsultasMedicas.factory;

import com.ufu.gestaoConsultasMedicas.models.Patient;
import com.ufu.gestaoConsultasMedicas.validation.PatientValidator;

import java.time.LocalDate;

public class PatientFactory {

    public static Patient createPatient(String cpf, String name, LocalDate dateOfBirth, String address, String medicalHistory) {

        PatientValidator.validateCpf(cpf);
        PatientValidator.validateDateOfBirth(dateOfBirth);
        PatientValidator.validateAge(dateOfBirth);

        return new Patient(cpf, name, dateOfBirth, address, medicalHistory);
    }
}
