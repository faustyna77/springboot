package zaklad.pogrzebowy.api.services;




import zaklad.pogrzebowy.api.models.Client;

import java.util.List;

public interface IClientService {

    List<Client> findAll(); // Pobiera wszystkich klientów
    Client create(Client client); // Tworzy nowego klienta
    Client update(Long id, Client client); // Aktualizuje dane klienta
    void delete(Long id); // Usuwa klienta
}
