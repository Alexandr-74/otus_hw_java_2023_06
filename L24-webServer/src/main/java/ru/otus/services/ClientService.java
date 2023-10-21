package ru.otus.services;

import ru.otus.db.repository.ClientRepository;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.List;
import java.util.Map;

public class ClientService {
    private ClientRepository clientRepository;

    public void createClient(Map<String, String> clientHashMap) {
        if (clientRepository!= null) {
            clientRepository.createClient(mapClient(clientHashMap));
        }
    }

    public Client findById(Long id) {
        if (clientRepository!= null) {
            return clientRepository.findById(id);
        }
        throw new IllegalArgumentException();
    }

    public List<Client> findAll() {
        if (clientRepository!= null) {
            return clientRepository.findAll();
        }
        throw new IllegalArgumentException();
    }

    public Client mapClient(Map<String, String> clientHashMap) {
        return new Client(null, clientHashMap.get("name"),
                new Address(null, clientHashMap.get("street")),
                List.of(new Phone(null,clientHashMap.get("phone"))),
                clientHashMap.get("login"),
                clientHashMap.get("password"));
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


}
