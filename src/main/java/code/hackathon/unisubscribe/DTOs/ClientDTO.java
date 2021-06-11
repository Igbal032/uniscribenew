package code.hackathon.unisubscribe.DTOs;

import code.hackathon.unisubscribe.models.Company;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ClientDTO {

    private long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private LocalDate createdDate;
}
