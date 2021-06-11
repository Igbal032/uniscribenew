package code.hackathon.unisubscribe.repositories;

import code.hackathon.unisubscribe.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository  extends CrudRepository<Company, Long> {

    Company getCompanyById(long companyId);

    @Query(value = "select * from companies c where c.user_id = ?1 ", nativeQuery = true)
    List<Company> getCompanies(long clientId);

    @Query(value = "select c from Company c where c.deletedDate is null")
    List<Company> getAllCompanies();

}