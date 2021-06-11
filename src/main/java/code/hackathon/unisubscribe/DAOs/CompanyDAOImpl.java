package code.hackathon.unisubscribe.DAOs;

import code.hackathon.unisubscribe.exceptions.ClientNotFound;
import code.hackathon.unisubscribe.exceptions.CompanyNotFound;
import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.models.Company;
import code.hackathon.unisubscribe.repositories.ClientRepository;
import code.hackathon.unisubscribe.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyDAOImpl implements CompanyDAO{

    @PersistenceContext
    EntityManager entityManager;

    private final CompanyRepository  companyRepository;
    private final ClientRepository clientRepository;

    @Override
    public List<Company> allCompanies(long clientId) {
        Client client = clientRepository.getClientById(clientId);
        System.out.println(client.getName()+" Name");
        if (client==null)
            throw new CompanyNotFound("NOt Found");
        List<Company> companyList = client.getCompanies();
        return companyList;
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = companyRepository.getAllCompanies();
        if (companies==null){
            throw new CompanyNotFound("Not Fount");
        }
        return companies;
    }

    @Override
    public Company addCompany(long clientId, Company company) {
        Client client = clientRepository.getClientById(clientId);
        company.setClient(client);
        company.setNotified(false);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company deleteCompany(long clientId,long companyId) {
        Company findCompany = companyRepository.getCompanyById(companyId);
        findCompany.setDeletedDate(LocalDate.now());
        companyRepository.save(findCompany);

        return findCompany;
    }

    @Transactional
    @Override
    public Company updateCompany(long companyId,Company company) {
        System.out.println(company);
        Company comp = companyRepository.getCompanyById(companyId);
        if (comp==null)
            throw new CompanyNotFound("Not Fount");
        comp.setCompanyName(company.getCompanyName());
        comp.setPrice(company.getPrice());
        comp.setDetail(company.getDetail());
        comp.setCategory(company.getCategory());
        comp.setLink(company.getLink());
        comp.setClient(company.getClient());
        comp.setExpiredDate(company.getExpiredDate());
        comp.setNotified(company.isNotified());
        comp.setNotifyDate(company.getNotifyDate());
        comp.setNotificationDate(company.getNotificationDate());
        companyRepository.save(comp);
        return comp;
    }

    @Override
    public Company getCompany(long clientId, long companyId) {
        Client client = clientRepository.getClientById(clientId);
        Optional<Company> companies = client.getCompanies().stream().filter(w->w.getId()==companyId&&w.getDeletedDate()==null).findFirst();
        if (!companies.isPresent()){
            throw new CompanyNotFound("Company not found");
        }
        return companies.get();
    }

}