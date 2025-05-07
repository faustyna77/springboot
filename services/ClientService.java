package zaklad.pogrzebowy.api.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaklad.pogrzebowy.api.models.Client;
import zaklad.pogrzebowy.api.repositories.ClientRepository;

import java.util.List;

@Service
public class ClientService implements IClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public List<Client> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    public Client create(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Long id, Client updatedClient) {
        return repository.findById(id)
                .map(existingClient -> {
                    existingClient.setFirstName(updatedClient.getFirstName());
                    existingClient.setLastName(updatedClient.getLastName());
                    existingClient.setPhone(updatedClient.getPhone());
                    return repository.save(existingClient);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
