package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.News;
import com.ufu.gestaoConsultasMedicas.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
public class NewsService {

    private static final String BASE = "https://www.gov.br";

    @Autowired
    private NewsRepository newsRepository;

    public Page<News> getLatestNews(int limit) throws IOException {
        // Coleta do site
        String url = BASE + "/saude/pt-br/assuntos/noticias";
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        Elements links = doc.select("a.summary.url");

        int n = Math.min(limit, links.size());

        for (int i = 0; i < n; i++) {
            Element a = links.get(i);
            String titulo = a.text();
            String link = absolutizar(a.attr("href"));

            // Verifica se já existe no banco
            if (!newsRepository.existsByLink(link)) {
                News noticia = News.builder()
                        .titulo(titulo)
                        .link(link)
                        .data(OffsetDateTime.now())
                        .build();

                newsRepository.save(noticia);
            }
        }

        // Retorna as últimas do banco (já ordenadas)
        return newsRepository.findAllByOrderByDataDesc(PageRequest.of(0, limit));
    }

    private String absolutizar(String href) {
        if (href == null) return null;
        if (href.startsWith("http://") || href.startsWith("https://")) return href;
        if (href.startsWith("/")) return BASE + href;
        return BASE + "/" + href;
    }
}
