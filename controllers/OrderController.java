package zaklad.pogrzebowy.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zaklad.pogrzebowy.api.models.Order;
import zaklad.pogrzebowy.api.services.OrderService;
import zaklad.pogrzebowy.api.services.UserService;
import zaklad.pogrzebowy.api.models.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Pobranie wszystkich użytkowników
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // Pobranie użytkownika po ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable Order.Status status) {
        return orderService.findByStatus(status);
    }
    // Dodanie nowego użytkownika
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        return orderService.create(order);
    }

    // Aktualizacja użytkownika
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    // Usunięcie użytkownika
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }
}

