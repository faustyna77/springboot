package zaklad.pogrzebowy.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaklad.pogrzebowy.api.models.Report;
import zaklad.pogrzebowy.api.repositories.ReportRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements IReportService {

    @Autowired
    private ReportRepository repository;

    @Override
    public List<Report> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Report> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Report create(Report report) {
        return repository.save(report);
    }

    @Override
    public Report update(Long id, Report updatedReport) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setReportType(updatedReport.getReportType());
                    existing.setGeneratedAt(updatedReport.getGeneratedAt());
                    existing.setFileUrl(updatedReport.getFileUrl());
                    existing.setGeneratedBy(updatedReport.getGeneratedBy());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Raport nie znaleziony"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
