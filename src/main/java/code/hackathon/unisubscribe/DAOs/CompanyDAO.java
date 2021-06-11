package code.hackathon.unisubscribe.DAOs;

import code.hackathon.unisubscribe.models.Company;

import java.util.List;

public interface CompanyDAO {

    List<Company> allCompanies(long clientId);

    List<Company> getAllCompanies();

    Company addCompany(long clientId, Company company);

    Company deleteCompany(long clientId,long companyId);

    Company updateCompany(long clientId,Company company);

    Company getCompany(long clientId,long companyId);
}