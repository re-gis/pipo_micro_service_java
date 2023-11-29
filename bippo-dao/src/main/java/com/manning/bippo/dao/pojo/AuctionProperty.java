package com.manning.bippo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manning.bippo.dao.converter.LiensDataConverter;
import com.manning.bippo.dao.converter.TaxDataConverter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "auctions")
@Data
public class AuctionProperty
{
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "auctionProperty", fetch = FetchType.LAZY)
    private List<AuctionDocument> documents;

    private String county;
    private String address;
    private String grantee;
    private String grantor;
    private String trustee;
    private String legal;
    private String apn;

    private Date search;
    private Date effective;
    private Date auction;
    private Date notice;

    @Convert(converter = LiensDataConverter.class)
    private LiensData encumbrances;

    @Convert(converter = LiensDataConverter.class)
    private LiensData involuntaries;

    @Convert(converter = TaxDataConverter.class)
    private TaxData tax;


    public static class Lien {

        public String routedAs, docType, recDate, bkPgIn, comment;
    }

    public static class LiensData {

        public Lien[] liens;
    }

    public static class Delinquent {

        public String status, through;
        public Double delinquent;
        public Integer taxYear;

        @JsonCreator
        public Delinquent(@JsonProperty("taxYear") int taxYear, @JsonProperty("delinquent") double delinquent,
                @JsonProperty("status") String status, @JsonProperty("through") String through) {
            this.taxYear = taxYear;
            this.delinquent = delinquent;
            this.status = status;
            this.through = through;
        }
    }
    
    public static class TaxData {

        public String currentStatus, currentDue;
        public Double land, impr, total, currentAmt;
        public Delinquent[] delinquent;
    }
}
