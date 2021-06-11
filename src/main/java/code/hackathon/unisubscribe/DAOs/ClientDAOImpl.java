package code.hackathon.unisubscribe.DAOs;

import code.hackathon.unisubscribe.exceptions.ClientNotFound;
import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientDAOImpl implements ClientDAO{
    private final ClientRepository clientRepository;

    @Override
    public Client getClient(long id) {
        Client client = clientRepository.getClientById(id);
        if (client==null)
            throw new ClientNotFound("Not Fount");
        return client;
    }

    @Override
    public Client add(Client newClient) {
        clientRepository.save(newClient);
        return newClient;
    }
}