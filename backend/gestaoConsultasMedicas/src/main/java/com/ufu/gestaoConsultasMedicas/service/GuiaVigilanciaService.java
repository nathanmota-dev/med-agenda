package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.GuiaDefinicao;
import com.ufu.gestaoConsultasMedicas.repository.GuiaDefinicaoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GuiaVigilanciaService {

    private static final String PDF_PATH = "/home/vbl/Desktop/v1/med-agenda/backend/gestaoConsultasMedicas/pdf/guia_vigilancia.pdf";

    private final GuiaDefinicaoRepository repo;

    public GuiaVigilanciaService(GuiaDefinicaoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public List<Map<String, String>> extrairSalvarERetornar() throws Exception {
        String textoTuberculose = extrairTextoTuberculose(PDF_PATH);
        List<Map<String, String>> pares = extrairTermosDefinicoes(textoTuberculose);

        for (Map<String, String> par : pares) {
            String termo = par.get("termo");
            String definicao = par.get("definicao");

            repo.findByTermoIgnoreCase(termo)
                    .ifPresentOrElse(existing -> {
                        existing.setDefinicao(definicao);
                        repo.save(existing);
                    }, () -> repo.save(new GuiaDefinicao(termo, definicao)));
        }
        return pares;
    }

    @Transactional(readOnly = true)
    public List<Map<String, String>> listarDoBanco() {
        return repo.findAll().stream()
                .map(e -> Map.of("termo", e.getTermo(), "definicao", e.getDefinicao()))
                .toList();
    }

    private String extrairTextoTuberculose(String pdfPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (PDDocument doc = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            int pages = doc.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String pageText = stripper.getText(doc);
                String pageLower = pageText.toLowerCase();
                if (pageLower.contains("tuberculose") || pageText.contains("TB")) {
                    sb.append('\n').append(pageText);
                }
            }
        }
        return sb.toString();
    }

    private boolean ehDoenca(String termoRaw) {
        String termo = limparBordas(termoRaw.toLowerCase().trim());

        Set<String> exclusao = new HashSet<>(Arrays.asList(
                "fonte","df","acesso","observação","observações","notas","nota",
                "exemplo","união","disponível","brasília","ministério","tema",
                "boletim","patologia","molecular","primeira","aguda","crônica",
                "recomenda-se","importante","passiva","ativa","ingresso","site-tb",
                "resultado","interpretação","eb","ts","mtb","pvhiv","iltb","pt",
                "rx","tb","retratamentos","dose","fase","menores","maiores",
                "gestantes","isoniazida","rifapentina","caso","cura","abandono",
                "falência","transferência","mudança","óbito","contatos","vigilância",
                "hospitalização","vacinação","vacina","tratamento","exame","exames",
                "critério","classificação","outras","medicamentos","esquema",
                "sensibilidade","considerações","fatores","amostras","métodos",
                "seguimento","rotina","gerenciamento","subsepécies","espécies",
                "manaus","santiago","genebra","zambia","são paulo","in",
                "óstico diferencial","diagnóstico diferencial","diferencial"
        ));

        for (String pal : exclusao) if (termo.contains(pal)) return false;

        Set<String> confirmadas = new HashSet<>(Arrays.asList(
                "anemia","linfopenia","trombocitopenia","candidose","leucoplasia",
                "tuberculose","hanseníase","aids","hiv","dengue","chikungunya",
                "zika","malária","leishmaniose","chagas","esquistossomose",
                "toxoplasmose","histoplasmose","paracoccidioidomicose","coccidioidomicose",
                "criptococose","blastomicose","cromoblastomicose","esporotricose",
                "pneumonia","meningite","encefalite","hepatite","cirrose",
                "diabetes","hipertensão","dermatite","eczema","psoríase",
                "artrite","artrose","osteoporose","osteomielite","sepse",
                "pneumoconiose","fibrose","enfisema","bronquite","asma",
                "rinite","sinusite","otite","conjuntivite","gastrite",
                "úlcera","colite","nefrite","cistite","prostatite",
                "endometriose","mioma","câncer","tumor","linfoma","leucemia",
                "sarcoma","carcinoma","melanoma","mieloma","metástase",
                "sífilis","gonorreia","clamídia","herpes","candidíase",
                "tricomoníase","papiloma","condiloma","molusco","escabiose",
                "pediculose","micose","tinha","impetigo","celulite",
                "erisipela","foliculite","furúnculo","carbúnculo","abscesso"
        ));
        for (String d : confirmadas) if (termo.contains(d)) return true;

        Pattern[] pads = new Pattern[] {
                Pattern.compile("\\w*(ite|ose|oma|emia|uria|algia|patia|trofia|plasia|genesis)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS),
                Pattern.compile("^(hiper|hipo|dis|micro|macro|mega|poli|mono)\\w+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS),
                Pattern.compile("\\w*(infecção|inflamação|síndrome|distúrbio|transtorno)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS),
                Pattern.compile("^(doença|mal de|síndrome de)\\s+\\w+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS)
        };
        for (Pattern p : pads) {
            if (p.matcher(termo).find()) return true;
        }
        return false;
    }

    private boolean ehDefinicaoValida(String definicaoRaw) {
        String definicao = limparDefinicao(definicaoRaw.trim());    
        String low = definicao.toLowerCase();

        if (definicao.length() < 20) return false;
        if (definicao.matches("^\\d+$")) return false;
        if (definicao.matches("^[A-Z]{2,}[/\\s]*[A-Z]*\\s*\\d*$")) return false;

        String[] exclusao = {
                "disponível em:", "acesso em:", "fonte:", "adaptado de",
                "ministério da saúde", "brasília", "df:", "união", "seção"
        };
        for (String e : exclusao) if (low.contains(e)) return false;

        return true;
    }

    private String limparDefinicao(String definicao) {
        if (definicao == null || definicao.trim().isEmpty()) {
            return definicao;
        }

        String resultado = definicao;

        // Remove referências de autores no formato (AUTOR, ano) ou (AUTOR et al., ano)
        resultado = resultado.replaceAll("\\([A-Z][A-Za-z\\s,;]+\\d{4}[a-z]?\\)", "");
        
        // Remove referências no formato AUTOR et al., ano
        resultado = resultado.replaceAll("[A-Z][A-Za-z]+\\s+et\\s+al\\.?,?\\s+\\d{4}[a-z]?", "");
        
        // Remove referências específicas como (ANDRADE; TONELLI; ORÉFFICE, 2006)
        resultado = resultado.replaceAll("\\([A-Z][A-Za-zÀ-ÿ\\s;,]+\\d{4}\\)", "");
        
        // Remove referências como (VASCONCELOS-SANTOS et al., 2012)
        resultado = resultado.replaceAll("\\([A-Z][A-Za-zÀ-ÿ\\-\\s]+et\\s+al\\.,\\s+\\d{4}\\)", "");
        
        // Remove números soltos com pontos (como ". 5", ". 2")
        resultado = resultado.replaceAll("\\.\\s*\\d+\\s*$", "");
        resultado = resultado.replaceAll("\\.\\s*\\d+\\s+", ". ");
        resultado = resultado.replaceAll("\\s+\\d+\\s*$", "");
        
        // Corrige caracteres especiais mal formados
        resultado = resultado.replace("dermato!tose", "dermatofitose");
        resultado = resultado.replace("rinosporidiose", "rinosporidiose");
        resultado = resultado.replace("rino\"ma", "rinofima");
        resultado = resultado.replace("sí\"lis", "sífilis");
        resultado = resultado.replace("síndromes", "síndromes");
        
        // Remove múltiplos espaços
        resultado = resultado.replaceAll("\\s+", " ");
        
        // Remove espaços antes de pontuação
        resultado = resultado.replaceAll("\\s+([,.;:])", "$1");
        
        // Remove vírgulas órfãs no final
        resultado = resultado.replaceAll(",\\s*$", "");
        
        // Garante que termine com ponto
        resultado = resultado.trim();
        if (!resultado.isEmpty() && !resultado.matches(".*[.!?]$")) {
            resultado += ".";
        }
        
        // Remove pontos duplos
        resultado = resultado.replaceAll("\\.+", ".");
        
        // Remove espaços extras no início e fim
        return resultado.trim();
    }

    private List<Map<String, String>> extrairTermosDefinicoes(String texto) {
        String norm = texto.replace("\r", "\n");

        Pattern padrao = Pattern.compile(
                "([A-ZÁÉÍÓÚÇÂÊÔÃÕ][A-Za-zçãõéáíóúêâôûàèìòùüñÇ\\-\\s]+?):\\s*(.+?)(?=\\n[A-ZÁÉÍÓÚÇÂÊÔÃÕ]|\\n•|\\n\\d+\\.|\\n$)",
                Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CHARACTER_CLASS
        );

        Matcher m = padrao.matcher(norm);
        List<Map<String, String>> resultados = new ArrayList<>();

        while (m.find()) {
            String termo = m.group(1).trim();
            String definicao = m.group(2).replaceAll("\\s+", " ").trim();

            // Remove caracteres especiais do final do termo, igual ao Python
            termo = termo.replaceAll("[^\\w\\sçãõéáíóúêâôûàèìòùüñÇ\\-]+$", "").trim();

            if (ehDoenca(termo) && ehDefinicaoValida(definicao)) {
                Map<String, String> obj = new LinkedHashMap<>();
                obj.put("termo", preservarCase(termo));
                obj.put("definicao", limparDefinicao(definicao));
                resultados.add(obj);
            }
        }
        return resultados;
    }

    private static String limparBordas(String s) {
        return s.replaceAll("^[^a-záéíóúçâêôãõ]+|[^a-záéíóúçâêôãõ]+$", "");
    }

    private static String preservarCase(String termo) {
        return termo.replaceAll("\\s*\\n\\s*", " ").trim();
    }
}
