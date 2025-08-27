package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.HabilidadeMedica;
import com.ufu.gestaoConsultasMedicas.repository.HabilidadeMedicaRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScrapingService {

    private final HabilidadeMedicaRepository repository;

    public ScrapingService(HabilidadeMedicaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<HabilidadeMedica> extrairESalvarHabilidades() throws Exception {
        List<HabilidadeMedica> habilidadesDoPdf = extrairHabilidadesDoPdf();

        for (HabilidadeMedica habilidade : habilidadesDoPdf) {
            repository.findByNomeIgnoreCase(habilidade.getNome())
                    .ifPresentOrElse(
                            existing -> System.out.println("Habilidade '" + existing.getNome() + "' já existe no banco."),
                            () -> repository.save(habilidade)
                    );
        }
        System.out.println("✅ Processo de extração e salvamento concluído!");
        return habilidadesDoPdf;
    }

    @Transactional(readOnly = true)
    public List<HabilidadeMedica> listarDoBanco() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public long contarHabilidades() {
        return repository.count();
    }

    private List<HabilidadeMedica> extrairHabilidadesDoPdf() throws Exception {
        List<HabilidadeMedica> habilidades = new ArrayList<>();

        // 🔹 Acessa direto do resources
        ClassPathResource resource = new ClassPathResource("RESOLUCAO-CFM-no-2.380-DE-18-DE-JUNHO-DE-2024-RESOLUCAO-CFM-no-2.380-DE-18-DE-JUNHO-DE-2024-DOU-Imprensa-Nacional.pdf");

        try (InputStream inputStream = resource.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            habilidades.addAll(parseSection(
                    text,
                    "A) RELAÇÃO DAS ESPECIALIDADES MÉDICAS RECONHECIDAS",
                    "B) RELAÇÃO DAS ÁREAS DE ATUAÇÃO",
                    "Especialidade"
            ));

            habilidades.addAll(parseSection(
                    text,
                    "B) RELAÇÃO DAS ÁREAS DE ATUAÇÃO MÉDICAS RECONHECIDAS",
                    "C) TITULAÇÕES DE ESPECIALIDADES",
                    "Área de Atuação"
            ));

            System.out.println("📄 Sucesso! Foram extraídas " + habilidades.size() + " habilidades do PDF.");
        }

        return habilidades;
    }

    private List<HabilidadeMedica> parseSection(String fullText, String startMarker, String endMarker, String type) {
        List<HabilidadeMedica> results = new ArrayList<>();
        try {
            int startIdx = fullText.indexOf(startMarker);
            int endIdx = fullText.indexOf(endMarker);

            if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) {
                System.err.println("⚠️ Não encontrei os marcadores para '" + type + "'.");
                return results;
            }

            String sectionText = fullText.substring(startIdx, endIdx);
            Pattern pattern = Pattern.compile("^\\d+\\.\\s+(.*)", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(sectionText);

            while (matcher.find()) {
                String nome = matcher.group(1).trim();
                if (!nome.isEmpty()) {
                    results.add(new HabilidadeMedica(nome, type));
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao processar a seção '" + type + "': " + e.getMessage());
        }
        return results;
    }
}
