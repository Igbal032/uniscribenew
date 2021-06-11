package code.hackathon.unisubscribe.services;

import code.hackathon.unisubscribe.DAOs.ClientDAO;
import code.hackathon.unisubscribe.DAOs.CompanyDAO;
import code.hackathon.unisubscribe.DTOs.ClientDTO;
import code.hackathon.unisubscribe.DTOs.CompanyDTO;
import code.hackathon.unisubscribe.enums.Category;
import code.hackathon.unisubscribe.exceptions.ClientNotFound;
import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.models.Company;
import code.hackathon.unisubscribe.repositories.CompanyRepository;
import code.hackathon.unisubscribe.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDAO companyDAO;
    private final ClientDAO clientDAO;
    private final JavaMailSender javaMailSender;

    private final CompanyRepository companyRepository;
    @Override
    public List<CompanyDTO> allCompanies(long clientId) {
        List<Company> companies = companyDAO.allCompanies(clientId)
                .stream().filter(company -> company.getDeletedDate()==null).collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(companies);
        return companyDTOList;
    }

    @Override
    public List<CompanyDTO> deletedCompanies(long clientId) {
        List<Company> deletedCompanies = companyDAO.allCompanies(clientId)
                .stream().filter(company -> company.getDeletedDate()!=null).collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(deletedCompanies);

        return companyDTOList;
    }

    @Override
    public List<CompanyDTO> addCompany(long clientId, CompanyDTO companyDTO) {
        Client client = clientDAO.getClient(clientId);
        companyDTO.setNotified(false);
        Company company = convertDtoToModel(client, companyDTO);
        companyDAO.addCompany(clientId,company);
        List<Company> companies = companyDAO.allCompanies(clientId)
                .stream()
                .filter(c->c.getDeletedDate()==null)
                .collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(companies);
        return companyDTOList;
    }

    @Override
    public List<CompanyDTO> deleteCompany(long clientId, long companyId) {
        companyDAO.deleteCompany(clientId,companyId);
        List<Company> companies = companyDAO.allCompanies(clientId)
                .stream()
                .filter(c->c.getDeletedDate()==null)
                .collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(companies);
        return companyDTOList;
    }

    @Override
    public List<CompanyDTO> updateCompany(long clientId,long companyId,CompanyDTO companyDTO) {
        Client client = clientDAO.getClient(clientId);
        if (client==null)
            throw  new ClientNotFound("Not Found");
        Company company = convertDtoToModel(client,companyDTO);
        Company company1 = companyDAO.updateCompany(companyId,company);
        System.out.println(company1.getCompanyName()+"  geldi");
        List<Company> companies = companyDAO.allCompanies(clientId)
                .stream()
                .filter(c->c.getDeletedDate()==null)
                .collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(companies);
        return companyDTOList;
    }

    @Override
    public List<CompanyDTO> getCompanyByCategory(long clientId, String category) {

        Category category1 = Category.valueOf(category.toUpperCase());
        List<Company> companyLiST = companyDAO.allCompanies(clientId)
                .stream().filter(w->w.getDeletedDate()==null&&w.getCategory().equals(category1))
                .collect(Collectors.toList());
        List<CompanyDTO> companyDTOList = convertModelsToDTOs(companyLiST);
        return companyDTOList;
    }

    @Scheduled(fixedRate = 5000)
    public List<Company> checkCompany() {
        List<Company> companies = companyDAO.getAllCompanies();
        for (Company company : companies){
            LocalDate today = LocalDate.now();
            int difference = differenceOfDate(company.getExpiredDate(),today);
            if (difference< company.getNotifyDate()){
                company.setNotified(true);
                sendEmail("iqbal.hoff@list.ru","Mesaj","Maas");
            }
            else {
                company.setNotified(false);
            }
            companyRepository.save(company);
        }
        return companies;
    }

    @Override
    public CompanyDTO getCompany(long clientId,long companyId) {
        Company company = companyDAO.getCompany(clientId,companyId);
        CompanyDTO companyDTO = convertModelToDTO(company);
        return companyDTO;
    }


    public List<CompanyDTO> convertModelsToDTOs(List<Company> companies){
        List<CompanyDTO> companyDTOList = companies.stream().map(company -> {
            return  CompanyDTO.builder()
                    .id(company.getId())
                    .companyName(company.getCompanyName())
                    .price(company.getPrice())
                    .detail(company.getDetail())
                    .link(company.getLink())
                    .notified(company.isNotified())
                    .expiredDate(company.getExpiredDate())
                    .notifyDate(company.getNotifyDate())
                    .category(company.getCategory().toString()).build();
        }).collect(Collectors.toList());
        return companyDTOList;
    }

    public Company convertDtoToModel(Client client,CompanyDTO companyDTO){
        System.out.println(client+" client");
        System.out.println(companyDTO.getLink()+" companyDTO" );
        LocalDate notifyLocalDate = companyDTO.getExpiredDate().minus(Period.ofDays(companyDTO.getNotifyDate()));
        Category category = Category.valueOf(companyDTO.getCategory());
        Company company = Company.builder()
                .companyName(companyDTO.getCompanyName())
                .price(companyDTO.getPrice())
                .detail(companyDTO.getDetail())
                .notifyDate(companyDTO.getNotifyDate())
                .notificationDate(notifyLocalDate)
                .link(companyDTO.getLink())
                .client(client)
                .notified(companyDTO.isNotified())
                .expiredDate(companyDTO.getExpiredDate())
                .category(category)
                .createdDate(LocalDate.now())
                .build();
        return company;
    }

    public CompanyDTO convertModelToDTO(Company company){
        CompanyDTO companyDTO = CompanyDTO.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .category(company.getCategory().toString())
                .price(company.getPrice())
                .notified(company.isNotified())
                .notifyDate(company.getNotifyDate())
                .link(company.getLink())
                .detail(company.getDetail())
                .expiredDate(company.getExpiredDate())
                .build();
        return companyDTO;
    }

    public <T> Pagination<?> pagination(List<T> data, int pageNumber, int itemCountPerPage, StringBuffer url) {

        Pagination pagination = new Pagination(data, itemCountPerPage, pageNumber, url);
        return pagination;
    }

    @Override
    public  void sendEmail(String to, String subject,String content) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);

        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);

    }
    public void ss(){
        System.out.println("sadsadsad" +
                "");
    }

    public int differenceOfDate(LocalDate today, LocalDate expiredDate){
        Period period = Period.between(today, expiredDate);
        System.out.println(period);
        return period.getDays();
    }
}
