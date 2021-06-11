package code.hackathon.unisubscribe.DTOs;

import code.hackathon.unisubscribe.enums.Category;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Builder(toBuilder = true)
public class CompanyDTO {

    private long id;
    private String companyName;
    private double price;
    private String detail;
    private boolean notified;
    private int notifyDate;
    private String link;
    private String category;
    private LocalDate expiredDate;
}
