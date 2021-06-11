package code.hackathon.unisubscribe.controllers;


import code.hackathon.unisubscribe.DTOs.ClientDTO;
import code.hackathon.unisubscribe.DTOs.CompanyDTO;
import code.hackathon.unisubscribe.enums.Category;
import code.hackathon.unisubscribe.models.Client;
import code.hackathon.unisubscribe.services.ClientService;
import code.hackathon.unisubscribe.services.CompanyService;
import code.hackathon.unisubscribe.services.CompanyServiceImpl;
import code.hackathon.unisubscribe.utils.MailExtension;
import code.hackathon.unisubscribe.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final CompanyService companyService;
    private final JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    /*
    get user with id
     */
    @PostMapping("/setUser")
    public ResponseEntity<Client> getClient(@RequestBody Client client){
        logger.info("Get Client");
        Client client1 = clientService.add(client);
        return new ResponseEntity<>(client1,HttpStatus.OK);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable long clientId){
        logger.info("Get Client 11");
        ClientDTO clientDTO = clientService.getClient(clientId);
        logger.info("Get Client");
        return new ResponseEntity<>(clientDTO,HttpStatus.OK);
    }
    /*
    get all companies with user id
     */
    @GetMapping("/{clientId}/companies")
    public ResponseEntity<?> getCompanies(@PathVariable long clientId, @RequestParam(required = false) Integer pageNumber,
                                                         @RequestParam(required = false) Integer countOfData,
                                                         HttpServletRequest httpServletRequest){

        List<CompanyDTO> companyDTOList = companyService.allCompanies(clientId);
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }


    /*
    create company
     */
    @PostMapping("/{clientId}/companies")
    public ResponseEntity<?> createCompany(@PathVariable long clientId,@RequestBody CompanyDTO companyDTO,
                                                          @RequestParam(required = false) Integer pageNumber,
                                                          @RequestParam(required = false) Integer countOfData,
                                                          HttpServletRequest httpServletRequest){
        companyService.addCompany(clientId,companyDTO);
        List<CompanyDTO> companyDTOList = companyService.allCompanies(clientId);
        logger.info("Create Company");
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }
    /*
    get company
     */
    @GetMapping("/{clientId}/companies/{companyId}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable long clientId,@PathVariable long companyId){
        CompanyDTO newCompanyDTO = companyService.getCompany(clientId,companyId);
        logger.info("Get Company");
        return new ResponseEntity<>(newCompanyDTO,HttpStatus.OK);
    }

    @DeleteMapping("{clientId}/companies/delete/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable long clientId,@PathVariable long companyId,
                                                          @RequestParam(required = false) Integer pageNumber,
                                                          @RequestParam(required = false) Integer countOfData,
                                                          HttpServletRequest httpServletRequest){
        List<CompanyDTO> companyDTOList =  companyService.deleteCompany(clientId,companyId);
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }
    @GetMapping("/{clientId}/companies/deletedCompanies")
    public ResponseEntity<?> getDeletedCompanies(@PathVariable long clientId,
                                                 @RequestParam(required = false) Integer pageNumber,
                                                 @RequestParam(required = false) Integer countOfData,
                                                 HttpServletRequest httpServletRequest){
        List<CompanyDTO> companyDTOList = companyService.deletedCompanies(clientId);
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }

    @PutMapping("/{clientId}/companies/update/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable long clientId,@PathVariable long companyId,@RequestBody CompanyDTO companyDTO,
                                           @RequestParam(required = false) Integer pageNumber,
                                           @RequestParam(required = false) Integer countOfData,
                                           HttpServletRequest httpServletRequest){
        System.out.println(companyDTO.getCompanyName()+"   oaopasio;sdifjbsduipfbD");
        List<CompanyDTO> companyDTOList = companyService.updateCompany(clientId,companyId,companyDTO);
        logger.info("Get Deleted Companies");
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<String>> allCategories(){
//        new ResponseEntity<>(companyDTOList,HttpStatus.OK)
        List<String> categories = Stream.of(Category.values())
                .map(Category::name)
                .collect(Collectors.toList());
        System.out.println(categories);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/getCompanyByCategory/{category}/{clientId}")
    public ResponseEntity<?> getCompanyByCategory(@PathVariable long clientId,@PathVariable String category,
                                                             @RequestParam(required = false) Integer pageNumber,
                                                             @RequestParam(required = false) Integer countOfData,
                                                             HttpServletRequest httpServletRequest){
        List<CompanyDTO> companyDTOList = companyService.getCompanyByCategory(clientId,category);
        if (pageNumber!=null&&countOfData!=null){
            Pagination<?> pagination = companyService.pagination(companyDTOList,pageNumber,countOfData,httpServletRequest.getRequestURL());
            return new ResponseEntity<>(pagination, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(companyDTOList, HttpStatus.OK);

    }

    @PostMapping("/sendMail")
    public ResponseEntity<String> sendEmailToClients(@RequestParam String email,@RequestParam String subject,@RequestParam String content) throws IOException, MessagingException {

        companyService.sendEmail(email,subject,content);
        System.out.println("Success");
        return new ResponseEntity<>(HttpStatus.OK);
    }





}
