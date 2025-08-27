package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.News;
import com.ufu.gestaoConsultasMedicas.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public Page<News> listarNoticias(@RequestParam(defaultValue = "5") int limit) throws IOException {
        return newsService.getLatestNews(limit);
    }
}
