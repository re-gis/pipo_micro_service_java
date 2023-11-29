package com.manning.bippo.dao.pojo;

import com.manning.bippo.dao.itf.AbstractProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ntreis")
@Data
public class NtreisProperty implements AbstractProperty
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "ntreisProperty", fetch = FetchType.LAZY)
    @OrderBy("type, s3Url")
    List<NtreisPhoto> ntreisPhotos;

    @Column(name = "AccessibilityFeatures")
    String accessibilityFeatures;

    @Column(name = "AccessoryUnitType")
    String accessoryUnitType;

    @Column(name = "AccessoryUnitYN")
    String accessoryUnitYN;

    @Column(name = "AcresBottomLand")
    Integer acresBottomLand;

    @Column(name = "AcresCultivated")
    Integer acresCultivated;

    @Column(name = "AcresIrrigated")
    Integer acresIrrigated;

    @Column(name = "AcresPasture")
    Integer acresPasture;

    @Column(name = "ActiveContinueToShowDate")
    Date activeContinueToShowDate;

    @Column(name = "ActiveKickOutDate")
    Date activeKickOutDate;

    @Column(name = "ActiveOpenHouseCount")
    Integer activeOpenHouseCount;

    @Column(name = "ActiveOptionContractDate")
    Date activeOptionContractDate;

    @Column(name = "AerialPhotoAvailableYN")
    String aerialPhotoAvailableYN;

    @Column(name = "AGExemptionYN")
    String aGExemptionYN;

    @Column(name = "AppliancesYN")
    String appliancesYN;

    @Column(name = "ApplicationFeeYN")
    String applicationFeeYN;

    @Column(name = "AppointmentCall")
    String appointmentCall;

    @Column(name = "AppointmentPhone")
    String appointmentPhone;

    @Column(name = "AppointmentPhoneExt")
    String appointmentPhoneExt;

    @Column(name = "AppraiserName")
    String appraiserName;

    @Column(name = "ArchitecturalStyle")
    String architecturalStyle;

    @Column(name = "AssociationFee")
    Integer associationFee;

    @Column(name = "AssociationFeeFrequency")
    String associationFeeFrequency;

    @Column(name = "AssociationFeeIncludes")
    String associationFeeIncludes;

    @Column(name = "AssociationType")
    String associationType;

    @Column(name = "AverageMonthlyLease")
    Float averageMonthlyLease;

    @Column(name = "BackOnMarketDate")
    Date backOnMarketDate;

    @Column(name = "Barn1Length")
    Integer barn1Length;

    @Column(name = "Barn1Width")
    Integer barn1Width;

    @Column(name = "Barn2Length")
    Integer barn2Length;

    @Column(name = "Barn2Width")
    Integer barn2Width;

    @Column(name = "Barn3Length")
    Integer barn3Length;

    @Column(name = "Barn3Width")
    Integer barn3Width;

    @Column(name = "BarnInformation")
    String barnInformation;

    @Column(name = "BathsFull")
    Integer bathsFull;

    @Column(name = "BathsHalf")
    Integer bathsHalf;

    @Column(name = "BathsTotal")
    Float bathsTotal;

    @Column(name = "BedroomBathroomFeatures")
    String bedroomBathroomFeatures;

    @Column(name = "BedsTotal")
    Integer bedsTotal;

    @Column(name = "Block")
    String block;

    @Column(name = "BuildingAreaSource")
    String buildingAreaSource;

    @Column(name = "BuildingNumber")
    String buildingNumber;

    @Column(name = "BuildingUse")
    String buildingUse;

    @Column(name = "BusinessName")
    String businessName;

    @Column(name = "BuyersAgencyCommission")
    String buyersAgencyCommission;

    @Column(name = "CancelledDate")
    Date cancelledDate;

    @Column(name = "CapitalizationRate")
    Float capitalizationRate;

    @Column(name = "CDOM")
    Integer cDOM;

    @Column(name = "CeilingHeight")
    String ceilingHeight;

    @Column(name = "City")
    String city;

    @Column(name = "CloseDate")
    Date closeDate;

    @Column(name = "ClosePrice")
    Float closePrice;

    @Column(name = "CoListAgent_MUI")
    Integer coListAgent_MUI;

    @Column(name = "CoListAgentDirectWorkPhone")
    String coListAgentDirectWorkPhone;

    @Column(name = "CoListAgentEmail")
    String coListAgentEmail;

    @Column(name = "CoListAgentFullName")
    String coListAgentFullName;

    @Column(name = "CoListAgentMLSID")
    String coListAgentMLSID;

    @Column(name = "CoListOffice_MUI")
    Integer coListOffice_MUI;

    @Column(name = "CoListOfficeMLSID")
    String coListOfficeMLSID;

    @Column(name = "CoListOfficeName")
    String coListOfficeName;

    @Column(name = "CoListOfficePhone")
    String coListOfficePhone;

    @Column(name = "CommercialFeatures")
    String commercialFeatures;

    @Column(name = "CommunityFeatures")
    String communityFeatures;

    @Column(name = "CompensationPaid")
    String compensationPaid;

    @Column(name = "ComplexName")
    String complexName;

    @Column(name = "ConstructionMaterials")
    String constructionMaterials;

    @Column(name = "ConstructionMaterialsWalls")
    String constructionMaterialsWalls;

    @Column(name = "ContingencyInfo")
    String contingencyInfo;

    @Column(name = "CoSellingAgent_MUI")
    Integer coSellingAgent_MUI;

    @Column(name = "CoSellingAgentDirectWorkPhone")
    String coSellingAgentDirectWorkPhone;

    @Column(name = "CoSellingAgentEmail")
    String coSellingAgentEmail;

    @Column(name = "CoSellingAgentFullName")
    String coSellingAgentFullName;

    @Column(name = "CoSellingAgentMLSID")
    String coSellingAgentMLSID;

    @Column(name = "CoSellingOffice_MUI")
    Integer coSellingOffice_MUI;

    @Column(name = "CoSellingOfficeMLSID")
    Integer coSellingOfficeMLSID;

    @Column(name = "CoSellingOfficeName")
    String coSellingOfficeName;

    @Column(name = "CoSellingOfficePhone")
    String coSellingOfficePhone;

    @Column(name = "CountyOrParish")
    String countyOrParish;

    @Column(name = "CropRetireProgramYN")
    String cropRetireProgramYN;

    @Column(name = "Crops")
    String crops;

    @Column(name = "DateAvailable")
    Date dateAvailable;

    @Column(name = "DepositAmount")
    Float depositAmount;

    @Column(name = "DepositPet")
    Float depositPet;

    @Column(name = "Development")
    String development;

    @Column(name = "Directions")
    String directions;

    @Column(name = "Documents")
    String documents;

    @Column(name = "DOM")
    Integer dOM;

    @Column(name = "Easements")
    String easements;

    @Column(name = "ElementarySchoolName")
    String elementarySchoolName;

    @Column(name = "EnergySavingFeatures")
    String energySavingFeatures;

    @Column(name = "Equity")
    Integer equity;

    @Column(name = "Exclusions")
    String exclusions;

    @Column(name = "ExpirationDateOption")
    Date expirationDateOption;

    @Column(name = "ExteriorBuildings")
    String exteriorBuildings;

    @Column(name = "ExteriorFeatures")
    String exteriorFeatures;

    @Column(name = "FarmRanchFeatures")
    String farmRanchFeatures;

    @Column(name = "FencedYardYN")
    String fencedYardYN;

    @Column(name = "Fencing")
    String fencing;

    @Column(name = "FHA_VA_ApprovedComplexNumber")
    String fHA_VA_ApprovedComplexNumber;

    @Column(name = "FinancingApproved")
    String financingApproved;

    @Column(name = "FinancingProposed")
    String financingProposed;

    @Column(name = "FireplaceFeatures")
    String fireplaceFeatures;

    @Column(name = "FireplacesTotal")
    Integer fireplacesTotal;

    @Column(name = "Flooring")
    String flooring;

    @Column(name = "FloorLocationNumber")
    String floorLocationNumber;

    @Column(name = "FoundationDetails")
    String foundationDetails;

    @Column(name = "FreightDoors")
    String freightDoors;

    @Column(name = "FrontageFeet")
    Integer frontageFeet;

    @Column(name = "FurnishedYN")
    String furnishedYN;

    @Column(name = "GarageLength")
    Integer garageLength;

    @Column(name = "GarageWidth")
    Integer garageWidth;

    @Column(name = "GreenBuildingCertification")
    String greenBuildingCertification;

    @Column(name = "GreenEnergyEfficient")
    String greenEnergyEfficient;

    @Column(name = "GrossAnnualExpenses")
    Float grossAnnualExpenses;

    @Column(name = "GrossAnnualIncome")
    Float grossAnnualIncome;

    @Column(name = "GrossIncomeMultiplier")
    Float grossIncomeMultiplier;

    @Column(name = "HandicapYN")
    String handicapYN;

    @Column(name = "Heating")
    String heating;

    @Column(name = "HighSchoolName")
    String highSchoolName;

    @Column(name = "HOAManagementCompany")
    String hOAManagementCompany;

    @Column(name = "HOAManagementCompanyPhone")
    String hOAManagementCompanyPhone;

    @Column(name = "Inclusions")
    String inclusions;

    @Column(name = "IncomeExpenseSource")
    String incomeExpenseSource;

    @Column(name = "InsuranceExpense")
    Float insuranceExpense;

    @Column(name = "InteriorFeatures")
    String interiorFeatures;

    @Column(name = "IntermediateSchoolName")
    String intermediateSchoolName;

    @Column(name = "InternetExposure")
    String internetExposure;

    @Column(name = "JuniorHighSchoolName")
    String juniorHighSchoolName;

    @Column(name = "KeyboxNumber")
    String keyboxNumber;

    @Column(name = "KeyboxType")
    String keyBoxType;

    @Column(name = "LakeName")
    String lakeName;

    @Column(name = "LandLeasedYN")
    String landLeasedYN;

    @Column(name = "LastListPrice")
    Float lastListPrice;

    @Column(name = "LastStatus")
    String lastStatus;

    @Column(name = "Latitude")
    Float latitude;

    @Column(name = "LeaseConditions")
    String leaseConditions;

    @Column(name = "LeasedDate")
    Date leasedDate;

    @Column(name = "LeaseExpirationDate")
    Date leaseExpirationDate;

    @Column(name = "LeaseRateMax")
    Float leaseRateMax;

    @Column(name = "LeaseRateMin")
    Float leaseRateMin;

    @Column(name = "LeaseTerm")
    String leaseTerm;

    @Column(name = "LeaseTerms")
    String leaseTerms;

    @Column(name = "LeaseType")
    String leaseType;

    @Column(name = "LenderName")
    String lenderName;

    @Column(name = "LesseePays")
    String lesseePays;

    @Column(name = "ListAgent_MUI")
    Integer listAgent_MUI;

    @Column(name = "ListAgentDirectWorkPhone")
    String listAgentDirectWorkPhone;

    @Column(name = "ListAgentEmail")
    String listAgentEmail;

    @Column(name = "ListAgentFullName")
    String listAgentFullName;

    @Column(name = "ListAgentMLSid")
    String listAgentMLSID;

    @Column(name = "ListingAgreement")
    String listingAgreement;

    @Column(name = "ListingContractDate")
    Date listingContractDate;

    @Column(name = "ListingFinancing")
    String listingFinancing;

    @Column(name = "ListingFinancing2")
    String listingFinancing2;

    @Column(name = "ListOffice_MUI")
    Integer listOffice_MUI;

    @Column(name = "ListOfficeMLSID")
    String listOfficeMLSID;

    @Column(name = "ListOfficeName")
    String listOfficeName;

    @Column(name = "ListOfficePhone")
    String listOfficePhone;

    @Column(name = "ListPrice")
    Float listPrice;

    @Column(name = "ListPriceLow")
    Float listPriceLow;

    @Column(name = "Loan1Amount")
    Float loan1Amount;

    @Column(name = "Loan1InterestRate")
    Float loan1InterestRate;

    @Column(name = "Loan1Years")
    Float loan1Years;

    @Column(name = "Loan2Amount")
    Float loan2Amount;

    @Column(name = "Loan2InterestRate")
    Float loan2InterestRate;

    @Column(name = "Loan2Years")
    Float loan2Years;

    @Column(name = "LoanBalance")
    Float loanBalance;

    @Column(name = "LoanInterestRate")
    Float loanInterestRate;

    @Column(name = "LoanPayment")
    Float loanPayment;

    @Column(name = "LoanPaymentType")
    String loanPaymentType;

    @Column(name = "LoanType")
    String loanType;

    @Column(name = "Longitude")
    Float longitude;

    @Column(name = "LotFeatures")
    String lotFeatures;

    @Column(name = "LotNumber")
    String lotNumber;

    @Column(name = "Lotsize")
    String lotSize;

    @Column(name = "LotSizeArea")
    Float lotSizeArea;

    @Column(name = "LotSizeAreaSQFT")
    Float lotSizeAreaSQFT;

    @Column(name = "LotSizeDimensions")
    String lotSizeDimensions;

    @Column(name = "LotSizeSource")
    String lotSizeSource;

    @Column(name = "LotSizeUnits")
    String lotSizeUnits;

    @Column(name = "LotsSoldPackage")
    String lotsSoldPackage;

    @Column(name = "LotsSoldSeparate")
    String lotsSoldSeparate;

    @Column(name = "MapBook")
    String mapBook;

    @Column(name = "MapCoordinates")
    String mapCoordinates;

    @Column(name = "MapPage")
    String mapPage;

    @Column(name = "Matrix_Unique_ID")
    Integer matrix_Unique_ID;

    @Column(name = "MatrixModifiedDT")
    Date matrixModifiedDT;

    @Column(name = "MiddleSchoolName")
    String middleSchoolName;

    @Column(name = "MLS")
    String mLS;

    @Column(name = "MLSAreaMajor")
    String mLSAreaMajor;

    @Column(name = "MLSAreaMinor")
    String mLSAreaMinor;

    @Column(name = "MLSNumber")
    Integer mLSNumber;

    @Column(name = "MLSNumberSaleOrLease")
    String mLSNumberSaleOrLease;

    @Column(name = "MoniesRequired")
    String moniesRequired;

    @Column(name = "MonthlyPetFee")
    Float monthlyPetFee;

    @Column(name = "MortgageCompany")
    String mortgageCompany;

    @Column(name = "MoveInDate")
    Date moveInDate;

    @Column(name = "MultiParcelIDYN")
    String multiParcelIDYN;

    @Column(name = "MultiZoningYN")
    String multiZoningYN;

    @Column(name = "MunicipalUtilityDistrictYN")
    String municipalUtilityDistrictYN;

    @Column(name = "NetAnnualIncome")
    Float netAnnualIncome;

    @Column(name = "NonRefundablePetFeeYN")
    String nonRefundablePetFeeYN;

    @Column(name = "NumberOfBarns")
    Integer numberOfBarns;

    @Column(name = "NumberOfBuildings")
    Integer numberOfBuildings;

    @Column(name = "NumberOfDaysGuestsAllowed")
    Integer numberOfDaysGuestsAllowed;

    @Column(name = "NumberOfDiningAreas")
    Integer numberOfDiningAreas;

    @Column(name = "NumberOfLakes")
    Integer numberOfLakes;

    @Column(name = "NumberOfLeaseableSpaces")
    Integer numberOfLeaseableSpaces;

    @Column(name = "NumberOfLivingAreas")
    Integer numberOfLivingAreas;

    @Column(name = "NumberOfLots")
    Integer numberOfLots;

    @Column(name = "NumberOfParkingSpaces")
    Integer numberOfParkingSpaces;

    @Column(name = "NumberOfPetsAllowed")
    Integer numberOfPetsAllowed;

    @Column(name = "NumberOfPonds")
    Integer numberOfPonds;

    @Column(name = "NumberOfResidences")
    Integer numberOfResidences;

    @Column(name = "NumberOfSpacesLeased")
    Integer numberOfSpacesLeased;

    @Column(name = "NumberOfStallsInBarn1")
    Integer numberOfStallsInBarn1;

    @Column(name = "NumberOfStallsInBarn2")
    Integer numberOfStallsInBarn2;

    @Column(name = "NumberOfStallsInBarn3")
    Integer numberOfStallsInBarn3;

    @Column(name = "NumberOfStockTanks")
    Integer numberOfStockTanks;

    @Column(name = "NumberOfStories")
    Float numberOfStories;

    @Column(name = "NumberOfStoriesInBuilding")
    Float numberOfStoriesInBuilding;

    @Column(name = "NumberOfUnits")
    Integer numberOfUnits;

    @Column(name = "NumberOfVehicles")
    Integer numberOfVehicles;

    @Column(name = "NumberOfWaterMeters")
    Integer numberOfWaterMeters;

    @Column(name = "NumberOfWells")
    Integer numberOfWells;

    @Column(name = "Occupancy")
    String occupancy;

    @Column(name = "OccupancyRate")
    Float occupancyRate;

    @Column(name = "OfficeSupervisor")
    String officeSupervisor;

    @Column(name = "OffMarketDate")
    Date offMarketDate;

    @Column(name = "OpenHouseCount")
    Integer openHouseCount;

    @Column(name = "OpenHouseUpcoming")
    String openHouseUpcoming;

    @Column(name = "OriginalListPrice")
    Float originalListPrice;

    @Column(name = "OriginalMortgageDate")
    Date originalMortgageDate;

    @Column(name = "OtherEquipment")
    String otherEquipment;

    @Column(name = "OwnerName")
    String ownerName;

    @Column(name = "OwnerPays")
    String ownerPays;

    @Column(name = "OwnerPermissionToVideoYN")
    String ownerPermissionToVideoYN;

    @Column(name = "ParcelNumber")
    String parcelNumber;

    @Column(name = "ParkingFeatures")
    String parkingFeatures;

    @Column(name = "ParkingSpacesCarport")
    Integer parkingSpacesCarport;

    @Column(name = "ParkingSpacesCoveredTotal")
    Integer parkingSpacesCoveredTotal;

    @Column(name = "ParkingSpacesGarage")
    Integer parkingSpacesGarage;

    @Column(name = "PendingDate")
    Date pendingDate;

    @Column(name = "PermitAddressInternetYN")
    String permitAddressInternetYN;

    @Column(name = "PermitAVMYN")
    String permitAVMYN;

    @Column(name = "PermitCommentsReviewsYN")
    String permitCommentsReviewsYN;

    @Column(name = "PermitInternetYN")
    String permitInternetYN;

    @Column(name = "PetsYN")
    String petsYN;

    @Column(name = "PhotoCount")
    Integer photoCount;

    @Column(name = "PhotoModificationTimestamp")
    Date photoModificationTimestamp;

    @Column(name = "PlannedDevelopment")
    String plannedDevelopment;

    @Column(name = "PoolFeatures")
    String poolFeatures;

    @Column(name = "PoolYN")
    String poolYN;

    @Column(name = "Possession")
    String possession;

    @Column(name = "PossibleShortSaleYN")
    String possibleShortSaleYN;

    @Column(name = "PostalCode")
    String postalCode;

    @Column(name = "PostalCodePlus4")
    String postalCodePlus4;

    @Column(name = "PresentUse")
    String presentUse;

    @Column(name = "PriceChangeTimestamp")
    Date priceChangeTimestamp;

    @Column(name = "PrimarySchoolName")
    String primarySchoolName;

    @Column(name = "PrivateRemarks")
    String privateRemarks;

    @Column(name = "PropertyAssociationFees")
    String propertyAssociationFees;

    @Column(name = "PropertySubType")
    String propertySubType;

    @Column(name = "PropertyType")
    String propertyType;

    @Column(name = "ProposedUse")
    String proposedUse;

    @Column(name = "PublicRemarks")
    String publicRemarks;

    @Column(name = "RanchName")
    String ranchName;

    @Column(name = "RanchType")
    String ranchType;

    @Column(name = "RATIO_ClosePrice_By_ListPrice")
    Float rATIO_ClosePrice_By_ListPrice;

    @Column(name = "RATIO_ClosePrice_By_OriginalListPrice")
    Float rATIO_ClosePrice_By_OriginalListPrice;

    @Column(name = "RATIO_CurrentPrice_By_SQFT")
    Float rATIO_CurrentPrice_By_SQFT;

    @Column(name = "Restrictions")
    String restrictions;

    @Column(name = "RoadAssessmentYN")
    String roadAssessmentYN;

    @Column(name = "RoadFrontage")
    String roadFrontage;

    @Column(name = "RoadFrontageDistance")
    Integer roadFrontageDistance;

    @Column(name = "Roof")
    String roof;

    @Column(name = "RoomCount")
    Integer roomCount;

    @Column(name = "SchoolDistrict")
    String schoolDistrict;

    @Column(name = "SecondMortgageYN")
    String secondMortgageYN;

    @Column(name = "SecurityFeatures")
    String securityFeatures;

    @Column(name = "SecuritySystemYN")
    String securitySystemYN;

    @Column(name = "SellerContributions")
    String sellerContributions;

    @Column(name = "SellerType")
    String sellerType;

    @Column(name = "SellingAgent_MUI")
    Integer sellingAgent_MUI;

    @Column(name = "SellingAgentDirectWorkPhone")
    String sellingAgentDirectWorkPhone;

    @Column(name = "SellingAgentEmail")
    String sellingAgentEmail;

    @Column(name = "SellingAgentFullName")
    String sellingAgentFullName;

    @Column(name = "SellingAgentMLSID")
    String sellingAgentMLSID;

    @Column(name = "SellingOffice_MUI")
    Integer sellingOffice_MUI;

    @Column(name = "SellingOfficeMLSID")
    String sellingOfficeMLSID;

    @Column(name = "SellingOfficeName")
    String sellingOfficeName;

    @Column(name = "SellingOfficePhone")
    String sellingOfficePhone;

    @Column(name = "SeniorHighSchoolName")
    String seniorHighSchoolName;

    @Column(name = "ShowingInstructions")
    String showingInstructions;

    @Column(name = "ShowingInstructionsType")
    String showingInstructionsType;

    @Column(name = "SoilType")
    String soilType;

    @Column(name = "SoldTerms")
    String soldTerms;

    @Column(name = "SpecialNotes")
    String specialNotes;

    @Column(name = "SQFTBuilding")
    Float sQFTBuilding;

    @Column(name = "SQFTGross")
    Float sQFTGross;

    @Column(name = "SQFTLand")
    Float sQFTLand;

    @Column(name = "SQFTLeasable")
    Float sQFTLeasable;

    @Column(name = "SQFTLot")
    Float sQFTLot;

    @Column(name = "SqFtTotal")
    Float sqFtTotal;

    @Column(name = "StateOrProvince")
    String stateOrProvince;

    @Column(name = "Status")
    String status;

    @Column(name = "StatusChangeTimestamp")
    Date statusChangeTimestamp;

    @Column(name = "StreetDirPrefix")
    String streetDirPrefix;

    @Column(name = "StreetDirSuffix")
    String streetDirSuffix;

    @Column(name = "StreetName")
    String streetName;

    @Column(name = "StreetNumber")
    String streetNumber;

    @Column(name = "StreetNumberSearchable")
    String streetNumberSearchable;

    @Column(name = "StreetSuffix")
    String streetSuffix;

    @Column(name = "StructuralStyle")
    String structuralStyle;

    @Column(name = "SubAgencyCommission")
    String subAgencyCommission;

    @Column(name = "SubdividedYN")
    String subdividedYN;

    @Column(name = "SubdivisionName")
    String subdivisionName;

    @Column(name = "SurfaceRights")
    String surfaceRights;

    @Column(name = "TaxLegalDescription")
    String taxLegalDescription;

    @Column(name = "TempOffMarketDate")
    Date tempOffMarketDate;

    @Column(name = "Tenancy")
    String tenancy;

    @Column(name = "TenantPays")
    String tenantPays;

    @Column(name = "ThirdPartyAssistanceProgramYN")
    String thirdPartyAssistanceProgramYN;

    @Column(name = "TitleCompanyClosing")
    String titleCompanyClosing;

    @Column(name = "TitleCompanyLocation")
    String titleCompanyLocation;

    @Column(name = "TitleCompanyPhone")
    String titleCompanyPhone;

    @Column(name = "TitleCompanyPreferred")
    String titleCompanyPreferred;

    @Column(name = "Topography")
    String topography;

    @Column(name = "TotalAnnualExpensesInclude")
    String totalAnnualExpensesInclude;

    @Column(name = "TransactionType")
    String transactionType;

    @Column(name = "UnexemptTaxes")
    Float unexemptTaxes;

    @Column(name = "UnitCount")
    Integer unitCount;

    @Column(name = "UnitNumber")
    String unitNumber;

    @Column(name = "URL1")
    String uRL1;

    @Column(name = "URL2")
    String uRL2;

    @Column(name = "URL3")
    String uRL3;

    @Column(name = "URL4")
    String uRL4;

    @Column(name = "URL5")
    String uRL5;

    @Column(name = "Utilities")
    String utilities;

    @Column(name = "UtilitiesOther")
    String utilitiesOther;

    @Column(name = "VariableFeeYN")
    String variableFeeYN;

    @Column(name = "VirtualTourURLBranded")
    String virtualTourURLBranded;

    @Column(name = "VirtualTourURLUnbranded")
    String virtualTourURLUnbranded;

    @Column(name = "WillSubdivide")
    String willSubdivide;

    @Column(name = "WillSubdivideYN")
    String willSubdivideYN;

    @Column(name = "WithdrawnDate")
    Date withdrawnDate;

    @Column(name = "WithdrawnDateSubListing")
    Date withdrawnDateSubListing;

    @Column(name = "YearBuilt")
    Integer yearBuilt;

    @Column(name = "YearBuiltDetails")
    String yearBuiltDetails;

    @Column(name = "Zoning")
    String zoning;

    @Column(name = "ZoningCommercial")
    String zoningCommercial;

    @Column(name = "Created")
    Date created;

    @Column(name = "Updated")
    Date updated;

    @Transient
    public String getAddress()
    {
        return getAddressLine1() + ", " + getAddressLine2();
    }

    @Transient
    public boolean hasValidAddress()
    {
        return this.getStreetNumber() != null && !this.getStreetNumber().isEmpty()
                && this.getStreetName() != null && !this.getStreetName().isEmpty()
                && this.getStreetSuffix() != null && !this.getStreetSuffix().isEmpty()
                && this.getCity() != null && !this.getCity().isEmpty()
                && this.getStateOrProvince()!= null && !this.getStateOrProvince().isEmpty()
                && this.getPostalCode() != null && !this.getPostalCode().isEmpty();
    }

    @Transient
    public boolean hasStandardizableAddress() {
        return (this.getUnitNumber() == null || this.getUnitNumber().isEmpty())
                && this.hasValidAddress(); 
    }

    @Transient
    public String getAddressLine1()
    {
        return getStreetNumber() + " "
                + (getStreetDirPrefix().isEmpty() ? "" : getStreetDirPrefix() + " ")
                + getStreetName() + " " + getStreetSuffix()
                + (getStreetDirSuffix().isEmpty() ? "" : " " + getStreetDirSuffix())
                + (getUnitNumber().isEmpty() ? "" : " #" + getUnitNumber());
    }

    @Transient
    public String getAddressLine2()
    {
        return getCity() + ", " + getStateOrProvince() + " " + getPostalCode();
    }

    @Override
    public String toString()
    {
        return "NtreisProperty{" +
                "streetNumber='" + streetNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetSuffix='" + streetSuffix + '\'' +
                ", stateOrProvince='" + stateOrProvince + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public static int compareByListPriceAsc(NtreisProperty l, NtreisProperty r)
    {
        return l.getListPrice().compareTo(r.getListPrice());
    }

    public static int compareByListPriceDesc(NtreisProperty l, NtreisProperty r)
    {
        return r.getListPrice().compareTo(l.getListPrice());
    }

    public static int compareByClosePriceAsc(NtreisProperty l, NtreisProperty r)
    {
        return l.getClosePrice().compareTo(r.getClosePrice());
    }

    public static int compareByClosePriceDesc(NtreisProperty l, NtreisProperty r)
    {
        return r.getClosePrice().compareTo(l.getClosePrice());
    }

    public static int compareBySqFtTotalAsc(NtreisProperty l, NtreisProperty r)
    {
        return l.getSqFtTotal().compareTo(r.getSqFtTotal());
    }
}
