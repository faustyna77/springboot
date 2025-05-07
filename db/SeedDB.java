package zaklad.pogrzebowy.api.db;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zaklad.pogrzebowy.api.models.*;
import zaklad.pogrzebowy.api.repositories.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class SeedDB implements InitializingBean {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (clientRepository.count() > 0) {
            System.out.println("Baza już zawiera dane - pomijanie inicjalizacji");
            return;
        }

        System.out.println("Inicjalizacja bazy przykładowymi danymi...");

        // 1. Seed Users
        List<User> users = createUsers();
        users.forEach(userRepository::save);

        // 2. Seed Clients
        List<Client> clients = createClients();
        clients.forEach(clientRepository::save);

        // 3. Seed Orders
        List<Order> orders = createOrders(clients, users);
        orders.forEach(orderRepository::save);

        // 4. Seed Tasks
        List<Task> tasks = createTasks(orders);
        tasks.forEach(taskRepository::save);

        // 5. Seed Task Assignments
        List<TaskAssignment> assignments = createTaskAssignments(tasks, users);
        assignments.forEach(taskAssignmentRepository::save);

        // 6. Seed Reports
        List<Report> reports = createReports(users);
        reports.forEach(reportRepository::save);

        System.out.println("Inicjalizacja zakończona!");
    }

    private List<User> createUsers() {
        return Arrays.asList(
                new User("Faustyna", "Misiura", "faustyna@zaklad.pl", passwordEncoder.encode("admin"), User.Role.ADMIN),
                new User("Michał", "Dyjak", "michal@zaklad.pl", passwordEncoder.encode("admin"), User.Role.ADMIN),
                new User("Mateusz", "Hołyszko", "mateusz_h@zaklad.pl", passwordEncoder.encode("admin"), User.Role.ADMIN),
                new User("Mateusz", "Florian", "mateusz_f@zaklad.pl", passwordEncoder.encode("admin"), User.Role.ADMIN),
                new User("Pracownik", "Pierwszy", "prac1@zaklad.pl", passwordEncoder.encode("user"), User.Role.USER),
                new User("Pracownik", "Drugi", "prac2@zaklad.pl", passwordEncoder.encode("user"), User.Role.USER),
                new User("Pracownik", "Trzeci", "prac3@zaklad.pl", passwordEncoder.encode("user"), User.Role.USER)
        );
    }

    private List<Client> createClients() {
        return Arrays.asList(
                new Client("Jan", "Kowalski", "123456789"),
                new Client("Anna", "Nowak", "987654321"),
                new Client("Piotr", "Wiśniewski", "555666777"),
                new Client("Marta", "Lis", "111222333"),
                new Client("Tomasz", "Zieliński", "444555666")
        );
    }

    private List<Order> createOrders(List<Client> clients, List<User> users) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
                createOrder(now.minusDays(5), "Michalina", "Kot", Order.Status.pending, users.get(1), clients.get(0)),
                createOrder(now.minusDays(3), "Agnieszka", "Włos", Order.Status.completed, users.get(2), clients.get(1)),
                createOrder(now.minusDays(2), "Tatiana", "Kieliszek", Order.Status.canceled, users.get(1), clients.get(2))
        );
    }

    private Order createOrder(LocalDateTime orderDate, String cadaverFirstName, String cadaverLastName,
                              Order.Status status, User user, Client client) {
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setCadaverFirstName(cadaverFirstName);
        order.setCadaverLastName(cadaverLastName);
        order.setStatus(status);
        order.setUser(user);
        order.setClient(client);
        return order;
    }

    private List<Task> createTasks(List<Order> orders) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
                createTask("Transport ciała", "Transport ciała z domu do chłodni",
                        Task.Status.in_progress, Task.Priority.high, now.plusDays(1), orders.get(0)),
                createTask("Zamówienie trumny", "Zamówienie trumny standardowej",
                        Task.Status.pending, Task.Priority.medium, now.plusDays(2), orders.get(0)),
                createTask("Organizacja ceremonii", "Rezerwacja kaplicy i księdza",
                        Task.Status.completed, Task.Priority.high, now.plusDays(3), orders.get(1))
        );
    }

    private Task createTask(String taskName, String description, Task.Status status,
                            Task.Priority priority, LocalDateTime dueDate, Order order) {
        Task task = new Task();
        task.setTaskName(taskName);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setOrder(order);
        return task;
    }

    private List<TaskAssignment> createTaskAssignments(List<Task> tasks, List<User> users) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
                new TaskAssignment(tasks.get(0), users.get(4), now.minusDays(1)), // Pracownik Pierwszy assigned to Transport
                new TaskAssignment(tasks.get(1), users.get(5), now.minusDays(2)), // Pracownik Drugi assigned to Trumna
                new TaskAssignment(tasks.get(2), users.get(6), now.minusDays(3))  // Pracownik Trzeci assigned to Ceremonia
        );
    }

    private List<Report> createReports(List<User> users) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
                new Report(Report.ReportType.DAILY, users.get(0), now.minusDays(1), "/reports/daily_20230501.pdf"),
                new Report(Report.ReportType.WEEKLY, users.get(0), now.minusWeeks(1), "/reports/weekly_20230424.pdf"),
                new Report(Report.ReportType.MONTHLY, users.get(0), now.minusMonths(1), "/reports/monthly_20230401.pdf"),
                new Report(Report.ReportType.CUSTOM, users.get(1), now.minusDays(2), "/reports/custom_20230429.pdf"),
                new Report(Report.ReportType.DAILY, users.get(1), now.minusDays(1), "/reports/daily_20230501_user2.pdf")
        );
    }
}