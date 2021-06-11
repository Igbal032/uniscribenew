package code.hackathon.unisubscribe.services;

import code.hackathon.unisubscribe.DTOs.ClientDTO;
import code.hackathon.unisubscribe.models.Client;

public interface ClientService {

    ClientDTO getClient(long id);

    Client add(Client newClient);

}
