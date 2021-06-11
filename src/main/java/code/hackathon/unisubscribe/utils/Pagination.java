package code.hackathon.unisubscribe.utils;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Pagination<T> {

    List<T> dataInPage;
    int countOfData;
    int pageNumber;
    int sizeOfData;
    int pageCount;
    boolean isPrevious;
    int previousPage;
    boolean isNext;
    int nextPage;
    String previousUrl;
    String nextUrl;

    public Pagination(List<T> data, int countOfData, int pageNumber,StringBuffer url) {
        this.countOfData = countOfData;
        this.pageNumber = pageNumber;
        this.sizeOfData = data.size();
        this.pageCount =(int)Math.ceil(this.sizeOfData/this.countOfData);
        isPrevious = this.pageNumber>0;
        isNext = this.pageNumber<this.pageCount;

        if (isPrevious){
            this.previousPage = this.pageNumber-1;
            this.previousUrl = url.toString()+"?pageNumber="+this.previousPage+"&countOfData="+this.countOfData+"";
        }
        if (isNext){
            nextPage = this.pageNumber+1;
            this.nextUrl = url.toString()+"?pageNumber="+this.nextPage+"&countOfData="+this.countOfData+"";
        }
        if (this.pageNumber==0){
            this.dataInPage = data.stream().limit(countOfData).collect(Collectors.toList());
        }
        else {
            this.dataInPage = data.stream().skip((this.pageNumber)*countOfData).limit(countOfData).collect(Collectors.toList());
        }

    }
}
