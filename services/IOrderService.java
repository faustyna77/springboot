package zaklad.pogrzebowy.api.services;

import zaklad.pogrzebowy.api.models.Client;
import zaklad.pogrzebowy.api.models.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
// Usuwa klienta

    List<Order> findAll(); // Pobierz wszystkie zamówienia
    Optional<Order> findById(Long id); // Pobierz zamówienie po ID
    List<Order> findByStatus(Order.Status status); // Pobierz zamówienia po statusie
    Order create(Order order); // Utwórz nowe zamówienie
    Order update(Long id, Order order); // Zaktualizuj zamówienie
    void delete(Long id);
}
