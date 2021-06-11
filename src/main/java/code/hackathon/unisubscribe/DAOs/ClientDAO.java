package code.hackathon.unisubscribe.DAOs;

import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.repositories.ClientRepository;

public interface ClientDAO {


    Client getClient(long id);

    Client add(Client newClient);


}