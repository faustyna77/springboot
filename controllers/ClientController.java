package zaklad.pogrzebowy.api.controllers;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zaklad.pogrzebowy.api.services.ClientService;
import zaklad.pogrzebowy.api.models.Client;

import java.util.List;

@RestController
@RequestMapping("/clients") // Ustalona ścieżka bazowa dla API klientów
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    @Autowired
    private ClientService service;

    // Pobranie wszystkich klientów
    @GetMapping
    public List<Client> findAll() {
        return service.findAll();
    }

    // Tworzenie nowego klienta
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        return service.create(client);
    }

    // Aktualizacja danych klienta
    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody Client client) {
        return service.update(id, client);
    }

    // Usunięcie klienta
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
