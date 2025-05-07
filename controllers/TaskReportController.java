package zaklad.pogrzebowy.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zaklad.pogrzebowy.api.services.TaskReportService;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")  // Dodane dla obsługi CORS
public class TaskReportController {

    @Autowired
    private TaskReportService reportService;

    @PostMapping(value = "/tasks", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<InputStreamResource> generateTaskReport(@RequestBody Map<String, Object> request,
                                                                  @RequestHeader("Authorization") String authHeader) {
        // Pobierz parametry z żądania
        Map<String, Object> filters = (Map<String, Object>) request.get("filters");
        String reportType = (String) request.get("reportType");

        // Generowanie nazwy pliku z bieżącą datą
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "zadania_raport_" + timestamp + ".html";

        // Generowanie raportu HTML
        ByteArrayInputStream reportStream = reportService.generateReport(filters, reportType);

        // Konfiguracja nagłówków odpowiedzi
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + filename);
        headers.add("Access-Control-Allow-Origin", "*");  // Dodane dla CORS

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_HTML)
                .body(new InputStreamResource(reportStream));
    }

    // Dodajemy metodę OPTIONS dla CORS preflight requests
    @RequestMapping(value = "/tasks", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        return ResponseEntity
                .ok()
                .headers(headers)
                .build();
    }
}