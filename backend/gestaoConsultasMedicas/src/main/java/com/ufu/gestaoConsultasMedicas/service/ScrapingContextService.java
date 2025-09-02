package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Cid;
import com.ufu.gestaoConsultasMedicas.models.HabilidadeMedica;
import com.ufu.gestaoConsultasMedicas.models.News;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class ScrapingContextService {

    private final GuiaVigilanciaService guiaVigilanciaService;
    private final CidService cidService;
    private final ScrapingService scrapingService;
    private final NewsService newsService;

    public ScrapingContextService(GuiaVigilanciaService guiaVigilanciaService, 
                                CidService cidService, 
                                ScrapingService scrapingService, 
                                NewsService newsService) {
        this.guiaVigilanciaService = guiaVigilanciaService;
        this.cidService = cidService;
        this.scrapingService = scrapingService;
        this.newsService = newsService;
    }

    @SuppressWarnings("unchecked")
    public String buildContext(Integer maxItems, Integer maxChars) {
        return buildGuiaVigilanciaContext(maxItems, maxChars);
    }

    public String buildContextFromQuestion(String question, Integer maxItems, Integer maxChars) {
        if (question == null) return buildAllContext(maxItems, maxChars);
        
        String lowerQuestion = question.toLowerCase();
        
        if (containsAny(lowerQuestion, Arrays.asList("cid", "código", "doença", "diagnóstico", "classificação"))) {
            return buildCidContext(maxItems, maxChars);
        }
        
        if (containsAny(lowerQuestion, Arrays.asList("notícia", "news", "governo", "saúde pública", "ministério"))) {
            return buildNewsContext(maxItems, maxChars);
        }
        
        if (containsAny(lowerQuestion, Arrays.asList("habilidade", "médica", "especialidade", "cfm", "resolução"))) {
            return buildHabilidadesContext(maxItems, maxChars);
        }
        
        if (containsAny(lowerQuestion, Arrays.asList("vigilância", "guia", "termo", "definição"))) {
            return buildGuiaVigilanciaContext(maxItems, maxChars);
        }
        
        return buildAllContext(maxItems, maxChars);
    }

    /**
     * Contexto do Guia de Vigilância (original)
     */
    @SuppressWarnings("unchecked")
    public String buildGuiaVigilanciaContext(Integer maxItems, Integer maxChars) {
        List<?> bruto = guiaVigilanciaService.listarDoBanco();

        List<String> linhas = bruto.stream()
                .limit(maxItems == null ? Long.MAX_VALUE : maxItems)
                .map(item -> {
                    if (item instanceof Map<?, ?> m) {
                        Map<String, Object> map = (Map<String, Object>) m;
                        Object termo = map.getOrDefault("termo", "");
                        Object definicao = map.getOrDefault("definicao", "");
                        return "- " + clean(termo.toString()) + ": " + clean(definicao.toString());
                    }
                    return clean(item.toString());
                })
                .collect(Collectors.toList());

        String contexto = "=== GUIA DE VIGILÂNCIA ===\n" + String.join("\n", linhas);
        return limitChars(contexto, maxChars);
    }

    /**
     * Contexto dos códigos CID
     */
    public String buildCidContext(Integer maxItems, Integer maxChars) {
        List<Cid> cids = cidService.findAll();
        
        List<String> linhas = cids.stream()
                .limit(maxItems == null ? Long.MAX_VALUE : maxItems)
                .map(cid -> "- " + clean(cid.getCode()) + ": Código CID-10")
                .collect(Collectors.toList());

        String contexto = "=== CÓDIGOS CID-10 ===\n" + String.join("\n", linhas);
        return limitChars(contexto, maxChars);
    }

    /**
     * Contexto das habilidades médicas
     */
    public String buildHabilidadesContext(Integer maxItems, Integer maxChars) {
        List<HabilidadeMedica> habilidades = scrapingService.listarDoBanco();
        
        List<String> linhas = habilidades.stream()
                .limit(maxItems == null ? Long.MAX_VALUE : maxItems)
                .map(hab -> {
                    String linha = "- " + clean(hab.getNome());
                    if (hab.getTipo() != null) {
                        linha += " (" + clean(hab.getTipo()) + ")";
                    }
                    return linha;
                })
                .collect(Collectors.toList());

        String contexto = "=== HABILIDADES MÉDICAS ===\n" + String.join("\n", linhas);
        return limitChars(contexto, maxChars);
    }

    /**
     * Contexto das notícias
     */
    public String buildNewsContext(Integer maxItems, Integer maxChars) {
        try {
            var newsPage = newsService.getLatestNews(maxItems == null ? 20 : maxItems);
            List<News> noticias = newsPage.getContent();
            
            List<String> linhas = noticias.stream()
                    .map(news -> "- " + clean(news.getTitulo()) + 
                               " (Link: " + clean(news.getLink()) + ")")
                    .collect(Collectors.toList());

            String contexto = "=== NOTÍCIAS DE SAÚDE ===\n" + String.join("\n", linhas);
            return limitChars(contexto, maxChars);
        } catch (Exception e) {
            return "=== NOTÍCIAS DE SAÚDE ===\nErro ao carregar notícias: " + e.getMessage();
        }
    }

    /**
     * Contexto combinado de todos os scrappings
     */
    public String buildAllContext(Integer maxItems, Integer maxChars) {
        int itemsPorTipo = maxItems == null ? 125 : maxItems / 4; // Divide igualmente
        int charsPorTipo = maxChars == null ? 3000 : maxChars / 4; // Divide igualmente
        
        StringBuilder contexto = new StringBuilder();
        contexto.append(buildGuiaVigilanciaContext(itemsPorTipo, charsPorTipo)).append("\n\n");
        contexto.append(buildCidContext(itemsPorTipo, charsPorTipo)).append("\n\n");
        contexto.append(buildHabilidadesContext(itemsPorTipo, charsPorTipo)).append("\n\n");
        contexto.append(buildNewsContext(itemsPorTipo, charsPorTipo));
        
        return limitChars(contexto.toString(), maxChars);
    }

    private static String clean(String s) {
        if (s == null) return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFKC)
                .replaceAll("\\s+", " ")
                .trim();
        return normalized.replaceAll("[\\u0000-\\u001F]", "");
    }

    private String limitChars(String contexto, Integer maxChars) {
        if (maxChars != null && maxChars > 0 && contexto.length() > maxChars) {
            return contexto.substring(0, maxChars);
        }
        return contexto;
    }

    private boolean containsAny(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}
