package zaklad.pogrzebowy.api.repositories;




import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zaklad.pogrzebowy.api.models.Client;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAllByOrderByIdAsc(); // Pobiera klientów posortowanych rosnąco po ID

}
