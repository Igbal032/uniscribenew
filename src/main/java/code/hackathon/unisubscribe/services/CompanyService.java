package code.hackathon.unisubscribe.services;

import code.hackathon.unisubscribe.DTOs.ClientDTO;
import code.hackathon.unisubscribe.DTOs.CompanyDTO;
import code.hackathon.unisubscribe.models.Company;
import code.hackathon.unisubscribe.utils.Pagination;

import java.util.List;

public interface CompanyService {

    List<CompanyDTO> allCompanies(long clientId);

    List<CompanyDTO> deletedCompanies(long clientId);

    List<CompanyDTO> addCompany(long clientId,CompanyDTO companyDTO);

    List<CompanyDTO> deleteCompany(long clientId, long companyId);

    List<CompanyDTO> updateCompany(long clientId,long companyId,CompanyDTO company);

    List<CompanyDTO> getCompanyByCategory(long clientId, String category);

    CompanyDTO getCompany(long clientId,long companyId);

    <T> Pagination<?> pagination(List<T> students, int pageNumber, int countPerPage, StringBuffer url);

    void sendEmail(String to, String subject,String content);

    }
