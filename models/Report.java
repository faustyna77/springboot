package zaklad.pogrzebowy.api.models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    public Report(ReportType reportType, User generatedBy, LocalDateTime generatedAt, String fileUrl) {
        this.reportType = reportType;
        this.generatedBy = generatedBy;
        this.generatedAt = generatedAt;
        this.fileUrl = fileUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @ManyToOne
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    public enum ReportType {
        DAILY, WEEKLY, MONTHLY, CUSTOM
    }
}
