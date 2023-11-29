package com.manning.bippo.dao.pojo;

import com.manning.bippo.dao.itf.Addressable;
import java.util.Date;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "realty_trac_auction")
@Data
public class RealtyTracAuction implements Addressable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="RTPropertyUniqueIdentifer")
    Integer RTPropertyUniqueIdentifer;

    @Column(name = "semantics_id")
    Long semanticsId;

    @Column(name="SitusAddress")
    String SitusAddress;

    @Column(name="SitusCity")
    String SitusCity;

    @Column(name="SitusState")
    String SitusState;

    @Column(name="SitusZip")
    String SitusZip;

    @Column(name="SitusCounty")
    String SitusCounty;

    @Column(name="SitusFIPS")
    String SitusFIPS;

    @Column(name="Longitude")
    Float Longitude;

    @Column(name="Latitude")
    Float Latitude;

    @Column(name="APN")
    String APN;

    @Column(name="Tax_Bill_MailingAddress")
    String Tax_Bill_MailingAddress;

    @Column(name="Tax_Bill_MailingCity")
    String Tax_Bill_MailingCity;

    @Column(name="Tax_Bill_MailingState")
    String Tax_Bill_MailingState;

    @Column(name="Tax_Bill_MailingZip")
    String Tax_Bill_MailingZip;

    @Column(name="Tax_Bill_MailingZip4")
    String Tax_Bill_MailingZip4;

    @Column(name="Tax_Bill_MailingCounty")
    String Tax_Bill_MailingCounty;

    @Column(name="Tax_Bill_MailingFIPs")
    String Tax_Bill_MailingFIPs;

    @Column(name="NCOA_MailingAddress")
    String NCOA_MailingAddress;

    @Column(name="NCOA_MailingCity")
    String NCOA_MailingCity;

    @Column(name="NCOA_MailingState")
    String NCOA_MailingState;

    @Column(name="NCOA_MailingZip")
    String NCOA_MailingZip;

    @Column(name="NCOA_MailingZip4")
    String NCOA_MailingZip4;

    @Column(name="NCOA_MailingCounty")
    String NCOA_MailingCounty;

    @Column(name="NCOA_MailingFIPs")
    String NCOA_MailingFIPs;

    @Column(name="PropertyGroup")
    String PropertyGroup;

    @Column(name="PropertyType")
    String PropertyType;

    @Column(name="Property_Zoning")
    String Property_Zoning;

    @Column(name="Bedrooms")
    Integer Bedrooms;

    @Column(name="Bathrooms")
    Double Bathrooms;

    @Column(name="SquareFootage")
    Integer SquareFootage;

    @Column(name="LotSize")
    Double LotSize;

    @Column(name="YearBuilt")
    Integer YearBuilt;

    @Column(name="Effective_Year_Built")
    Integer Effective_Year_Built;

    @Column(name="EnteredDate")
    Date EnteredDate;

    @Column(name="RTUniqueFCIdentifier")
    Integer RTUniqueFCIdentifier;

    @Column(name="RecordType")
    String RecordType;

    @Column(name="BorrowersName_Owner")
    String BorrowersName_Owner;

    @Column(name="LenderName")
    String LenderName;

    @Column(name="LenderAddress")
    String LenderAddress;

    @Column(name="LenderCity")
    String LenderCity;

    @Column(name="LenderState")
    String LenderState;

    @Column(name="LenderZip")
    String LenderZip;

    @Column(name="LenderPhone")
    String LenderPhone;

    @Column(name="CleanLenderName")
    String CleanLenderName;

    @Column(name="ParentLenderName")
    String ParentLenderName;

    @Column(name="MergerParentName")
    String MergerParentName;

    @Column(name="ServicerName")
    String ServicerName;

    @Column(name="ServicerAddress")
    String ServicerAddress;

    @Column(name="ServicerCity")
    String ServicerCity;

    @Column(name="ServicerPhone")
    String ServicerPhone;

    @Column(name="ServicerState")
    String ServicerState;

    @Column(name="ServicerZip")
    String ServicerZip;

    @Column(name="TrusteeName")
    String TrusteeName;

    @Column(name="TrusteeAddress")
    String TrusteeAddress;

    @Column(name="TrusteeCity")
    String TrusteeCity;

    @Column(name="TrusteeState")
    String TrusteeState;

    @Column(name="TrusteeZip")
    String TrusteeZip;

    @Column(name="TrusteePhone")
    String TrusteePhone;

    @Column(name="FCDocRecordingDate")
    Date FCDocRecordingDate;

    @Column(name="FCDocInstrumentNumber")
    String FCDocInstrumentNumber;

    @Column(name="FCDocBookPage")
    String FCDocBookPage;

    @Column(name="FCDocInstrumentDate")
    Date FCDocInstrumentDate;

    @Column(name="RelatedDocumentInstrumentNumber")
    String RelatedDocumentInstrumentNumber;

    @Column(name="RelatedDocDocumentBookPage")
    String RelatedDocDocumentBookPage;

    @Column(name="RelatedDocumentRecordingDate")
    String RelatedDocumentRecordingDate;

    @Column(name="RecordedAuctionDate")
    Date RecordedAuctionDate;

    @Column(name="RecordedOpeningBid")
    Double RecordedOpeningBid;

    @Column(name="PublicationDate")
    Date PublicationDate;

    @Column(name="CaseNumber")
    String CaseNumber;

    @Column(name="TrusteeReferenceNumber")
    String TrusteeReferenceNumber;

    @Column(name="Payment")
    Double Payment;

    @Column(name="LoanNumber")
    String LoanNumber;

    @Column(name="LoanMaturityDate")
    Date LoanMaturityDate;

    @Column(name="DefaultAmount")
    Double DefaultAmount;

    @Column(name="OriginalLoanAmount")
    Double OriginalLoanAmount;

    @Column(name="PenaltyInterest")
    Double PenaltyInterest;

    @Column(name="LoanBalance")
    Double LoanBalance;

    @Column(name="InterestRate")
    Double InterestRate;

    @Column(name="JudgementDate")
    Date JudgementDate;

    @Column(name="JudgmentAmount")
    Double JudgmentAmount;

    @Column(name="AuctionCourthouse")
    String AuctionCourthouse;

    @Column(name="AuctionAddress")
    String AuctionAddress;

    @Column(name="AuctionCityState")
    String AuctionCityState;

    @Column(name="AuctionTime")
    String AuctionTime;

    @Column(name="ProcessIndicator")
    String ProcessIndicator;

    @Column(name="EstimatedValue")
    Double EstimatedValue;

    public String getFirstLine() {
        return this.SitusAddress;
    }

    public String getLastLine() {
        return String.format("%s %s %s", this.SitusCity, this.SitusState, this.SitusZip);
    }

    @Override
    public String getAddress() {
        return this.getFirstLine() + ", " + this.getLastLine();
    }

    public boolean hasStandardizableAddress() {
        return this.SitusAddress != null && !this.SitusAddress.isEmpty()
                && this.SitusCity != null && !this.SitusCity.isEmpty()
                && this.SitusState != null && !this.SitusState.isEmpty()
                && this.SitusZip != null && !this.SitusZip.isEmpty();
    }
}
