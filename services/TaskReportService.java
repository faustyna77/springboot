package zaklad.pogrzebowy.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaklad.pogrzebowy.api.models.Task;
import zaklad.pogrzebowy.api.repositories.TaskRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskReportService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Generate a report based on provided filter criteria
     *
     * @param filters Map containing filter criteria
     * @param reportType Type of report ("details", "summary", "stats")
     * @return ByteArrayInputStream containing the HTML report
     */
    public ByteArrayInputStream generateReport(Map<String, Object> filters, String reportType) {
        // Get filtered tasks
        List<Task> filteredTasks = getFilteredTasks(filters);

        // Generate HTML report
        String htmlContent = generateHtmlReport(filteredTasks, reportType);

        // Convert to InputStream
        return new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Apply filters to get tasks matching criteria
     */
    private List<Task> getFilteredTasks(Map<String, Object> filters) {
        List<Task> allTasks = taskRepository.findAll();

        return allTasks.stream()
                .filter(task -> {
                    // Filter by order ID
                    if (filters.get("orderId") != null &&
                            (task.getOrder() == null || !task.getOrder().getId().equals(
                                    Long.valueOf(filters.get("orderId").toString())))) {
                        return false;
                    }

                    // Filter by status
                    if (filters.get("status") != null && !filters.get("status").toString().isEmpty() &&
                            !task.getStatus().toString().equals(filters.get("status").toString())) {
                        return false;
                    }

                    // Filter by priority
                    if (filters.get("priority") != null && !filters.get("priority").toString().isEmpty() &&
                            !task.getPriority().toString().equals(filters.get("priority").toString())) {
                        return false;
                    }

                    // Filter by employee ID
                    if (filters.get("employeeId") != null &&
                            (task.getAssignedUser() == null || !task.getAssignedUser().getId().equals(
                                    Long.valueOf(filters.get("employeeId").toString())))) {
                        return false;
                    }

                    // Filter by task name
                    if (filters.get("name") != null && !filters.get("name").toString().isEmpty() &&
                            !task.getTaskName().toLowerCase().contains(filters.get("name").toString().toLowerCase())) {
                        return false;
                    }

                    // Filter by date range
                    if (filters.get("dateFrom") != null && !filters.get("dateFrom").toString().isEmpty()
                            && task.getDueDate() != null) {
                        LocalDate fromDate = LocalDate.parse(filters.get("dateFrom").toString());
                        LocalDate taskDate = task.getDueDate().toLocalDate();
                        if (taskDate.isBefore(fromDate)) {
                            return false;
                        }
                    }

                    if (filters.get("dateTo") != null && !filters.get("dateTo").toString().isEmpty()
                            && task.getDueDate() != null) {
                        LocalDate toDate = LocalDate.parse(filters.get("dateTo").toString());
                        LocalDate taskDate = task.getDueDate().toLocalDate();
                        if (taskDate.isAfter(toDate)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Format task status for better readability
     */
    private String formatStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Oczekujące";
            case "in_progress":
                return "W trakcie";
            case "completed":
                return "Zakończone";
            case "canceled":
                return "Anulowane";
            default:
                return status;
        }
    }

    /**
     * Format task priority for better readability
     */
    private String formatPriority(String priority) {
        switch (priority.toLowerCase()) {
            case "low":
                return "Niski";
            case "medium":
                return "Średni";
            case "high":
                return "Wysoki";
            default:
                return priority;
        }
    }

    /**
     * Generate HTML report
     */
    private String generateHtmlReport(List<Task> tasks, String reportType) {
        StringBuilder html = new StringBuilder();

        // Add HTML header
        html.append("<!DOCTYPE html>\n")
                .append("<html lang=\"pl\">\n")
                .append("<head>\n")
                .append("    <meta charset=\"UTF-8\">\n")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                .append("    <title>Raport zadań</title>\n")
                .append("    <style>\n")
                .append("        body { font-family: Arial, sans-serif; margin: 20px; }\n")
                .append("        h1, h2 { color: #333; }\n")
                .append("        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }\n")
                .append("        .date { color: #666; font-size: 14px; }\n")
                .append("        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }\n")
                .append("        th { background-color: #444; color: white; padding: 10px; text-align: left; }\n")
                .append("        td { padding: 8px; border-bottom: 1px solid #ddd; }\n")
                .append("        tr:nth-child(even) { background-color: #f2f2f2; }\n")
                .append("        .summary-table { width: 50%; margin: 0 auto; }\n")
                .append("        .status-pending { color: #f39c12; }\n")
                .append("        .status-in_progress { color: #3498db; }\n")
                .append("        .status-completed { color: #2ecc71; }\n")
                .append("        .status-canceled { color: #e74c3c; }\n")
                .append("        .priority-low { color: #2ecc71; }\n")
                .append("        .priority-medium { color: #f39c12; }\n")
                .append("        .priority-high { color: #e74c3c; }\n")
                .append("        @media print { body { font-size: 12px; } }\n")
                .append("    </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("    <div class=\"header\">\n")
                .append("        <h1>Raport zadań</h1>\n")
                .append("        <p class=\"date\">Wygenerowano: ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("</p>\n")
                .append("    </div>\n");

        // Add report content based on type
        switch (reportType.toLowerCase()) {
            case "details":
                html.append(generateDetailedReport(tasks));
                break;
            case "summary":
                html.append(generateSummaryReport(tasks));
                break;
            case "stats":
                html.append(generateStatsReport(tasks));
                break;
            default:
                html.append(generateDetailedReport(tasks));
        }

        // Add HTML footer
        html.append("</body>\n")
                .append("</html>");

        return html.toString();
    }

    /**
     * Generate a detailed report with all tasks
     */
    private String generateDetailedReport(List<Task> tasks) {
        StringBuilder html = new StringBuilder();

        html.append("    <h2>Szczegółowy raport zadań (")
                .append(tasks.size())
                .append(")</h2>\n");

        if (tasks.isEmpty()) {
            html.append("    <p>Brak zadań spełniających kryteria filtrowania.</p>\n");
            return html.toString();
        }

        html.append("    <table>\n")
                .append("        <thead>\n")
                .append("            <tr>\n")
                .append("                <th>ID</th>\n")
                .append("                <th>Nazwa zadania</th>\n")
                .append("                <th>Opis</th>\n")
                .append("                <th>Status</th>\n")
                .append("                <th>Priorytet</th>\n")
                .append("                <th>Data wykonania</th>\n")
                .append("                <th>Przypisany do</th>\n")
                .append("                <th>Zamówienie</th>\n")
                .append("            </tr>\n")
                .append("        </thead>\n")
                .append("        <tbody>\n");

        for (Task task : tasks) {
            String statusClass = "status-" + task.getStatus().toString();
            String priorityClass = "priority-" + task.getPriority().toString();

            String dueDate = task.getDueDate() != null ?
                    task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "Nie określono";

            String assignedTo = task.getAssignedUser() != null ?
                    task.getAssignedUser().getFirstName() + " " + task.getAssignedUser().getLastName() : "Nie przypisano";

            String orderInfo = task.getOrder() != null ?
                    "ID: " + task.getOrder().getId() : "Brak";

            html.append("            <tr>\n")
                    .append("                <td>").append(task.getId()).append("</td>\n")
                    .append("                <td>").append(escapeHtml(task.getTaskName())).append("</td>\n")
                    .append("                <td>").append(escapeHtml(task.getDescription() != null ? task.getDescription() : "")).append("</td>\n")
                    .append("                <td class=\"").append(statusClass).append("\">").append(formatStatus(task.getStatus().toString())).append("</td>\n")
                    .append("                <td class=\"").append(priorityClass).append("\">").append(formatPriority(task.getPriority().toString())).append("</td>\n")
                    .append("                <td>").append(dueDate).append("</td>\n")
                    .append("                <td>").append(assignedTo).append("</td>\n")
                    .append("                <td>").append(orderInfo).append("</td>\n")
                    .append("            </tr>\n");
        }

        html.append("        </tbody>\n")
                .append("    </table>\n");

        return html.toString();
    }

    /**
     * Generate a summary report with counts
     */
    private String generateSummaryReport(List<Task> tasks) {
        StringBuilder html = new StringBuilder();

        html.append("    <h2>Podsumowanie zadań</h2>\n");

        if (tasks.isEmpty()) {
            html.append("    <p>Brak zadań spełniających kryteria filtrowania.</p>\n");
            return html.toString();
        }

        html.append("    <table class=\"summary-table\">\n")
                .append("        <thead>\n")
                .append("            <tr>\n")
                .append("                <th>Kategoria</th>\n")
                .append("                <th>Liczba zadań</th>\n")
                .append("            </tr>\n")
                .append("        </thead>\n")
                .append("        <tbody>\n")
                .append("            <tr>\n")
                .append("                <td>Wszystkie zadania</td>\n")
                .append("                <td>").append(tasks.size()).append("</td>\n")
                .append("            </tr>\n");

        // Tasks by status
        Map<String, Long> statusCounts = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> formatStatus(task.getStatus().toString()),
                        Collectors.counting()));

        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            html.append("            <tr>\n")
                    .append("                <td>Status: ").append(entry.getKey()).append("</td>\n")
                    .append("                <td>").append(entry.getValue()).append("</td>\n")
                    .append("            </tr>\n");
        }

        // Tasks by priority
        Map<String, Long> priorityCounts = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> formatPriority(task.getPriority().toString()),
                        Collectors.counting()));

        for (Map.Entry<String, Long> entry : priorityCounts.entrySet()) {
            html.append("            <tr>\n")
                    .append("                <td>Priorytet: ").append(entry.getKey()).append("</td>\n")
                    .append("                <td>").append(entry.getValue()).append("</td>\n")
                    .append("            </tr>\n");
        }

        // Assigned vs unassigned
        long assignedCount = tasks.stream()
                .filter(task -> task.getAssignedUser() != null)
                .count();

        html.append("            <tr>\n")
                .append("                <td>Przypisane do pracownika</td>\n")
                .append("                <td>").append(assignedCount).append("</td>\n")
                .append("            </tr>\n")
                .append("            <tr>\n")
                .append("                <td>Nieprzypisane</td>\n")
                .append("                <td>").append(tasks.size() - assignedCount).append("</td>\n")
                .append("            </tr>\n")
                .append("        </tbody>\n")
                .append("    </table>\n");

        return html.toString();
    }

    /**
     * Generate a statistics report with percentages
     */
    private String generateStatsReport(List<Task> tasks) {
        StringBuilder html = new StringBuilder();

        html.append("    <h2>Statystyki zadań</h2>\n");

        if (tasks.isEmpty()) {
            html.append("    <p>Brak zadań spełniających kryteria filtrowania.</p>\n");
            return html.toString();
        }

        html.append("    <table class=\"summary-table\">\n")
                .append("        <thead>\n")
                .append("            <tr>\n")
                .append("                <th>Metryka</th>\n")
                .append("                <th>Wartość</th>\n")
                .append("            </tr>\n")
                .append("        </thead>\n")
                .append("        <tbody>\n")
                .append("            <tr>\n")
                .append("                <td>Liczba zadań</td>\n")
                .append("                <td>").append(tasks.size()).append("</td>\n")
                .append("            </tr>\n");

        // Tasks by status
        Map<String, Long> statusCounts = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getStatus().toString(),
                        Collectors.counting()));

        // Calculate percentages for each status
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            double percentage = (entry.getValue().doubleValue() / tasks.size()) * 100;
            html.append("            <tr>\n")
                    .append("                <td>% zadań ze statusem ").append(formatStatus(entry.getKey())).append("</td>\n")
                    .append("                <td>").append(String.format("%.1f%%", percentage)).append("</td>\n")
                    .append("            </tr>\n");
        }

        // Tasks by priority
        Map<String, Long> priorityCounts = tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getPriority().toString(),
                        Collectors.counting()));

        // Calculate percentages for each priority
        for (Map.Entry<String, Long> entry : priorityCounts.entrySet()) {
            double percentage = (entry.getValue().doubleValue() / tasks.size()) * 100;
            html.append("            <tr>\n")
                    .append("                <td>% zadań z priorytetem ").append(formatPriority(entry.getKey())).append("</td>\n")
                    .append("                <td>").append(String.format("%.1f%%", percentage)).append("</td>\n")
                    .append("            </tr>\n");
        }

        // Tasks assigned to employees
        long assignedCount = tasks.stream()
                .filter(task -> task.getAssignedUser() != null)
                .count();
        double assignedPercentage = (assignedCount * 100.0) / tasks.size();

        html.append("            <tr>\n")
                .append("                <td>% zadań przypisanych</td>\n")
                .append("                <td>").append(String.format("%.1f%%", assignedPercentage)).append("</td>\n")
                .append("            </tr>\n")
                .append("        </tbody>\n")
                .append("    </table>\n");

        // Add additional visualizations or charts if needed
        html.append("    <h3>Wizualizacja statusów zadań</h3>\n")
                .append("    <div style=\"display:flex; height:30px; width:100%; margin-bottom:20px;\">\n");

        // Simple bar chart for statuses
        double totalWidth = 100.0;
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            double percentage = (entry.getValue().doubleValue() / tasks.size()) * 100;
            String statusClass = "status-" + entry.getKey();

            html.append("        <div class=\"").append(statusClass).append("\" ")
                    .append("style=\"width:").append(percentage).append("%; text-align:center;\" ")
                    .append("title=\"").append(formatStatus(entry.getKey())).append(": ").append(String.format("%.1f%%", percentage)).append("\">\n")
                    .append("            ").append(entry.getValue()).append("\n")
                    .append("        </div>\n");
        }

        html.append("    </div>\n");

        // Legend
        html.append("    <div style=\"display:flex; flex-wrap:wrap; gap:20px; margin-bottom:30px;\">\n");
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            String statusClass = "status-" + entry.getKey();

            html.append("        <div>\n")
                    .append("            <span class=\"").append(statusClass).append("\" style=\"padding:0 10px;\">■</span> ")
                    .append(formatStatus(entry.getKey()))
                    .append("\n")
                    .append("        </div>\n");
        }
        html.append("    </div>\n");

        return html.toString();
    }

    /**
     * Escape HTML special characters
     */
    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }

        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}