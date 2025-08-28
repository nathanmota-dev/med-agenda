package com.ufu.gestaoConsultasMedicas.service;

import com.ufu.gestaoConsultasMedicas.models.Cid;
import com.ufu.gestaoConsultasMedicas.repository.CidRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CidService {

    private final CidRepository cidRepository;

    public CidService(CidRepository cidRepository) {
        this.cidRepository = cidRepository;
    }

    private static final String URL = "http://tabnet.datasus.gov.br/cgi/sih/mxcid10lm.htm";
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; CID10Scraper-Java/1.3)";

    // A-Z + 2 dígitos + opcional ".d"
    private static final Pattern ATOMIC_CODE = Pattern.compile("^[A-Z]\\d{2}(?:\\.\\d)?$");

    // Range com mesma letra: LNN(.d)? - LNN(.d)?
    private static final Pattern RANGE_PATTERN =
            Pattern.compile("^([A-Z])(\\d{2})(?:\\.(\\d))?\\s*-\\s*([A-Z]?)(\\d{2})(?:\\.(\\d))?$");

    /* ====================== API ====================== */

    /** Faz download do site do DATASUS, extrai e salva (upsert) todos os códigos CID atômicos. */
    @Transactional
    public int refreshFromRemote() {
        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent(USER_AGENT)
                    .get();

            Element table = selectTabdados(doc);
            if (table == null) {
                throw new IllegalStateException("Tabela .tabdados não encontrada no HTML do DATASUS.");
            }

            List<String> codes = parseAtomicCodes(table);

            // Upsert simples (evita duplicatas)
            int insertedOrKept = 0;
            for (String code : codes) {
                if (!cidRepository.existsByCodeIgnoreCase(code)) {
                    cidRepository.save(new Cid(code));
                }
                insertedOrKept++;
            }
            return insertedOrKept;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar CIDs: " + e.getMessage(), e);
        }
    }

    /** Carrega um HTML local (útil p/ testes offline) e salva. */
    @Transactional
    public int refreshFromLocal(Path htmlPath) {
        try {
            byte[] bytes = Files.readAllBytes(htmlPath);
            String html = new String(bytes, StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html);

            Element table = selectTabdados(doc);
            if (table == null) {
                throw new IllegalStateException("Tabela .tabdados não encontrada no HTML local.");
            }

            List<String> codes = parseAtomicCodes(table);

            int insertedOrKept = 0;
            for (String code : codes) {
                if (!cidRepository.existsByCodeIgnoreCase(code)) {
                    cidRepository.save(new Cid(code));
                }
                insertedOrKept++;
            }
            return insertedOrKept;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar CIDs do arquivo local: " + e.getMessage(), e);
        }
    }

    public List<Cid> findAll() {
        return cidRepository.findAll();
    }

    public Optional<Cid> findOne(String code) {
        return cidRepository.findByCodeIgnoreCase(code);
    }

    @Transactional
    public void deleteOne(String code) {
        cidRepository.findById(code).ifPresent(cidRepository::delete);
    }

    @Transactional
    public void deleteAll() {
        cidRepository.deleteAllInBatch();
    }

    /* ================== Scraper Helpers ================== */

    private Element selectTabdados(Document doc) {
        Element t = doc.selectFirst("table.tabdados");
        if (t != null) return t;
        for (Element e : doc.select("table")) {
            String cls = e.className();
            if (cls != null && Arrays.stream(cls.split("\\s+"))
                    .anyMatch(s -> s.equalsIgnoreCase("tabdados"))) {
                return e;
            }
        }
        return null;
    }

    private List<String> parseAtomicCodes(Element table) {
        LinkedHashSet<String> uniq = new LinkedHashSet<>();
        List<String> dropped = new ArrayList<>();

        Element tbody = table.selectFirst("tbody");
        if (tbody == null) tbody = table;

        for (Element tr : tbody.select("> tr")) {
            Elements tds = tr.select("> td");
            if (tds.isEmpty()) continue;

            // Cabeçalhos de capítulo costumam ter 4 <td> → ignorar
            if (tds.size() == 4) continue;

            // Linhas "normais": 3 <td> (código | descrição | "Códigos da CID-10")
            if (tds.size() == 3) {
                for (String token : extractTokensFromCidCell(tds.get(2))) {
                    List<String> expanded = expandSameLetterRangeOrKeepAtomic(token);
                    if (expanded.isEmpty()) {
                        dropped.add(token);
                        continue;
                    }
                    for (String c : expanded) {
                        if (isAtomicCodeAllowed(c)) {
                            uniq.add(c);
                        } else {
                            dropped.add(c);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(uniq);
    }

    private List<String> extractTokensFromCidCell(Element cell) {
        List<String> parts = new ArrayList<>();
        for (Element a : cell.select("a")) parts.add(normalizeSpace(a.text()));
        parts.add(normalizeSpace(cell.text()));

        String raw = String.join(" | ", parts)
                .replace('•', ',')
                .replace('·', ',')
                .replace('|', ',');

        LinkedHashSet<String> out = new LinkedHashSet<>();
        for (String tok : raw.split("[,\\s]+")) {
            String t = stripPunct(tok).toUpperCase(Locale.ROOT);
            if (!t.isEmpty()) out.add(t);
        }
        return new ArrayList<>(out);
    }

    private List<String> expandSameLetterRangeOrKeepAtomic(String token) {
        String t = stripPunct(token.toUpperCase(Locale.ROOT));

        if (ATOMIC_CODE.matcher(t).matches()) {
            return Collections.singletonList(t);
        }

        Matcher m = RANGE_PATTERN.matcher(t);
        if (!m.matches()) return Collections.emptyList();

        String l1 = m.group(1);
        String n1 = m.group(2);
        String d1 = m.group(3);
        String l2 = m.group(4);
        String n2 = m.group(5);
        String d2 = m.group(6);
        if (l2 == null || l2.isEmpty()) l2 = l1;

        if (!l1.equals(l2)) return Collections.emptyList();

        if (d1 != null && d2 != null && n1.equals(n2)) {
            int start = d1.charAt(0) - '0';
            int end   = d2.charAt(0) - '0';
            if (start > end) { int tmp = start; start = end; end = tmp; }
            List<String> r = new ArrayList<>(end - start + 1);
            for (int i = start; i <= end; i++) r.add(l1 + n1 + "." + i);
            return r;
        }

        if (d1 == null && d2 == null) {
            int i1 = Integer.parseInt(n1);
            int i2 = Integer.parseInt(n2);
            if (i1 > i2) { int tmp = i1; i1 = i2; i2 = tmp; }
            List<String> r = new ArrayList<>(i2 - i1 + 1);
            for (int i = i1; i <= i2; i++) r.add(l1 + String.format("%02d", i));
            return r;
        }

        return Collections.emptyList();
    }

    private boolean isAtomicCodeAllowed(String c) {
        if (!ATOMIC_CODE.matcher(c).matches()) return false;
        if (c.equals("U99")) return false; // reservado
        return true;
    }

    private String normalizeSpace(String s) {
        if (s == null) return "";
        String t = s.replace('\u00A0', ' ').trim();
        return t.replaceAll("\\s+", " ");
    }

    private String stripPunct(String s) {
        if (s == null) return "";
        return s.replaceAll("^[\\s\\.;,]+|[\\s\\.;,]+$", "");
    }
}
