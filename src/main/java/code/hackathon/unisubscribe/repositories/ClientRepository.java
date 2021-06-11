package code.hackathon.unisubscribe.repositories;

import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Client getClientById(long clientId);
}