package zaklad.pogrzebowy.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zaklad.pogrzebowy.api.models.Report;
import zaklad.pogrzebowy.api.services.ReportService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Pobierz wszystkie raporty
    @GetMapping
    public List<Report> getAllReports() {
        return reportService.findAll();
    }

    // Pobierz raport po ID
    @GetMapping("/{id}")
    public Optional<Report> getReportById(@PathVariable Long id) {
        return reportService.findById(id);
    }

    // Dodaj nowy raport
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report createReport(@RequestBody Report report) {
        return reportService.create(report);
    }

    // Aktualizuj raport
    @PutMapping("/{id}")
    public Report updateReport(@PathVariable Long id, @RequestBody Report report) {
        return reportService.update(id, report);
    }

    // Usuń raport
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable Long id) {
        reportService.delete(id);
    }
}
