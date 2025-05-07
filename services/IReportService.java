package zaklad.pogrzebowy.api.services;

import zaklad.pogrzebowy.api.models.Report;

import java.util.List;
import java.util.Optional;

public interface IReportService {
    List<Report> findAll();
    Optional<Report> findById(Long id);
    Report create(Report report);
    Report update(Long id, Report report);
    void delete(Long id);
}
