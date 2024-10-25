package com.ufu.gestaoConsultasMedicas.validation;

import java.time.LocalDate;
import java.time.Period;

public class PatientValidator {

    public static void validateAge(LocalDate dateOfBirth) {
        if (calculateAge(dateOfBirth) < 18) {
            throw new IllegalArgumentException("O paciente deve ter pelo menos 18 anos");
        }
    }

    private static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public static void validateCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("O CPF precisa ter 11 dígitos");
        }
    }

    public static void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A Data de nascimento não pode ser do futuro");
        }
    }
}

