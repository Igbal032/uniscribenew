package code.hackathon.unisubscribe.services;

import code.hackathon.unisubscribe.DAOs.ClientDAO;
import code.hackathon.unisubscribe.DTOs.ClientDTO;
import code.hackathon.unisubscribe.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientDAO clientDAO;

    @Override
    public ClientDTO getClient(long id) {
        Client client = clientDAO.getClient(id);
        ClientDTO clientDTO = ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .build();
        return clientDTO;
    }

    @Override
    public Client add(Client newClient) {
        Client client = clientDAO.add(newClient);
        return client;
    }
}
