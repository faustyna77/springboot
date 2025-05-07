package zaklad.pogrzebowy.api.repositories;

import zaklad.pogrzebowy.api.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // Znajdź wszystkie raporty wygenerowane przez użytkownika
    List<Report> findByGeneratedById(Long userId);

    // Znajdź wszystkie raporty danego typu
    List<Report> findByReportType(Report.ReportType reportType);
}
