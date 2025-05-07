package zaklad.pogrzebowy.api.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zaklad.pogrzebowy.api.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Przykład dodatkowego zapytania: znajdź zamówienia po statusie
    List<Order> findByStatus(Order.Status status);

    // Możesz dodać inne zapytania np. po użytkowniku
    List<Order> findByUserId(Long Id);
    void deleteAllByUserId(Long userId);
}
