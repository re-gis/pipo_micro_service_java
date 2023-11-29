package com.manning.bippo.dao.pojo;

import com.manning.bippo.dao.itf.TrestleProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trestle_har")
@Data
public class TrestleHAR implements TrestleProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "AboveGradeFinishedArea")
    Double aboveGradeFinishedArea;

    @Column(name = "AboveGradeFinishedAreaSource")
    String aboveGradeFinishedAreaSource;

    @Column(name = "AboveGradeFinishedAreaUnits")
    String aboveGradeFinishedAreaUnits;

    @Column(name = "AccessCode")
    String accessCode;

    @Column(name = "AccessibilityFeatures")
    String accessibilityFeatures;

    @Column(name = "AdditionalParcelsDescription")
    String additionalParcelsDescription;

    @Column(name = "AdditionalParcelsYN")
    Boolean additionalParcelsYN;

    @Column(name = "AnchorsCoTenants")
    String anchorsCoTenants;

    @Column(name = "Appliances")
    String appliances;

    @Column(name = "ArchitecturalStyle")
    String architecturalStyle;

    @Column(name = "AssociationAmenities")
    String associationAmenities;

    @Column(name = "AssociationFee")
    Double associationFee;

    @Column(name = "AssociationFee2")
    Double associationFee2;

    @Column(name = "AssociationFee2Frequency")
    String associationFee2Frequency;

    @Column(name = "AssociationFeeFrequency")
    String associationFeeFrequency;

    @Column(name = "AssociationFeeIncludes")
    String associationFeeIncludes;

    @Column(name = "AssociationName")
    String associationName;

    @Column(name = "AssociationName2")
    String associationName2;

    @Column(name = "AssociationPhone")
    String associationPhone;

    @Column(name = "AssociationPhone2")
    String associationPhone2;

    @Column(name = "AssociationYN")
    Boolean associationYN;

    @Column(name = "AttachedGarageYN")
    Boolean attachedGarageYN;

    @Column(name = "AvailabilityDate")
    Date availabilityDate;

    @Column(name = "Basement")
    String basement;

    @Column(name = "BasementYN")
    Boolean basementYN;

    @Column(name = "BathroomsFull")
    Integer bathroomsFull;

    @Column(name = "BathroomsHalf")
    Integer bathroomsHalf;

    @Column(name = "BathroomsOneQuarter")
    Integer bathroomsOneQuarter;

    @Column(name = "BathroomsPartial")
    Integer bathroomsPartial;

    @Column(name = "BathroomsThreeQuarter")
    Integer bathroomsThreeQuarter;

    @Column(name = "BathroomsTotalInteger")
    Integer bathroomsTotalInteger;

    @Column(name = "BedroomsPossible")
    Integer bedroomsPossible;

    @Column(name = "BedroomsTotal")
    Integer bedroomsTotal;

    @Column(name = "BelowGradeFinishedArea")
    Double belowGradeFinishedArea;

    @Column(name = "BelowGradeFinishedAreaSource")
    String belowGradeFinishedAreaSource;

    @Column(name = "BelowGradeFinishedAreaUnits")
    String belowGradeFinishedAreaUnits;

    @Column(name = "BodyType")
    String bodyType;

    @Column(name = "BuilderModel")
    String builderModel;

    @Column(name = "BuilderName")
    String builderName;

    @Column(name = "BuildingAreaSource")
    String buildingAreaSource;

    @Column(name = "BuildingAreaTotal")
    Double buildingAreaTotal;

    @Column(name = "BuildingAreaUnits")
    String buildingAreaUnits;

    @Column(name = "BuildingFeatures")
    String buildingFeatures;

    @Column(name = "BuildingName")
    String buildingName;

    @Column(name = "BusinessName")
    String businessName;

    @Column(name = "BusinessType")
    String businessType;

    @Column(name = "BuyerAgencyCompensation")
    String buyerAgencyCompensation;

    @Column(name = "BuyerAgencyCompensationType")
    String buyerAgencyCompensationType;

    @Column(name = "BuyerAgentAOR")
    String buyerAgentAOR;

    @Column(name = "BuyerAgentDesignation")
    String buyerAgentDesignation;

    @Column(name = "BuyerAgentDirectPhone")
    String buyerAgentDirectPhone;

    @Column(name = "BuyerAgentEmail")
    String buyerAgentEmail;

    @Column(name = "BuyerAgentFax")
    String buyerAgentFax;

    @Column(name = "BuyerAgentFirstName")
    String buyerAgentFirstName;

    @Column(name = "BuyerAgentFullName")
    String buyerAgentFullName;

    @Column(name = "BuyerAgentHomePhone")
    String buyerAgentHomePhone;

    @Column(name = "BuyerAgentKeyNumeric")
    Integer buyerAgentKeyNumeric;

    @Column(name = "BuyerAgentLastName")
    String buyerAgentLastName;

    @Column(name = "BuyerAgentMiddleName")
    String buyerAgentMiddleName;

    @Column(name = "BuyerAgentMlsId")
    String buyerAgentMlsId;

    @Column(name = "BuyerAgentMobilePhone")
    String buyerAgentMobilePhone;

    @Column(name = "BuyerAgentNamePrefix")
    String buyerAgentNamePrefix;

    @Column(name = "BuyerAgentNameSuffix")
    String buyerAgentNameSuffix;

    @Column(name = "BuyerAgentOfficePhone")
    String buyerAgentOfficePhone;

    @Column(name = "BuyerAgentOfficePhoneExt")
    String buyerAgentOfficePhoneExt;

    @Column(name = "BuyerAgentPager")
    String buyerAgentPager;

    @Column(name = "BuyerAgentPreferredPhone")
    String buyerAgentPreferredPhone;

    @Column(name = "BuyerAgentPreferredPhoneExt")
    String buyerAgentPreferredPhoneExt;

    @Column(name = "BuyerAgentStateLicense")
    String buyerAgentStateLicense;

    @Column(name = "BuyerAgentTollFreePhone")
    String buyerAgentTollFreePhone;

    @Column(name = "BuyerAgentURL")
    String buyerAgentURL;

    @Column(name = "BuyerAgentVoiceMail")
    String buyerAgentVoiceMail;

    @Column(name = "BuyerAgentVoiceMailExt")
    String buyerAgentVoiceMailExt;

    @Column(name = "BuyerFinancing")
    String buyerFinancing;

    @Column(name = "BuyerOfficeAOR")
    String buyerOfficeAOR;

    @Column(name = "BuyerOfficeEmail")
    String buyerOfficeEmail;

    @Column(name = "BuyerOfficeFax")
    String buyerOfficeFax;

    @Column(name = "BuyerOfficeKeyNumeric")
    Integer buyerOfficeKeyNumeric;

    @Column(name = "BuyerOfficeMlsId")
    String buyerOfficeMlsId;

    @Column(name = "BuyerOfficeName")
    String buyerOfficeName;

    @Column(name = "BuyerOfficePhone")
    String buyerOfficePhone;

    @Column(name = "BuyerOfficePhoneExt")
    String buyerOfficePhoneExt;

    @Column(name = "BuyerOfficeURL")
    String buyerOfficeURL;

    @Column(name = "BuyerTeamKeyNumeric")
    Integer buyerTeamKeyNumeric;

    @Column(name = "BuyerTeamName")
    String buyerTeamName;

    @Column(name = "CableTvExpense")
    Double cableTvExpense;

    @Column(name = "CancellationDate")
    Date cancellationDate;

    @Column(name = "CapRate")
    Double capRate;

    @Column(name = "CarportSpaces")
    Double carportSpaces;

    @Column(name = "CarportYN")
    Boolean carportYN;

    @Column(name = "CarrierRoute")
    String carrierRoute;

    @Column(name = "City")
    String city;

    @Column(name = "CityRegion")
    String cityRegion;

    @Column(name = "CLIP")
    Long cLIP;

    @Column(name = "CloseDate")
    Date closeDate;

    @Column(name = "ClosePrice")
    Double closePrice;

    @Column(name = "CoBuyerAgentAOR")
    String coBuyerAgentAOR;

    @Column(name = "CoBuyerAgentDesignation")
    String coBuyerAgentDesignation;

    @Column(name = "CoBuyerAgentDirectPhone")
    String coBuyerAgentDirectPhone;

    @Column(name = "CoBuyerAgentEmail")
    String coBuyerAgentEmail;

    @Column(name = "CoBuyerAgentFax")
    String coBuyerAgentFax;

    @Column(name = "CoBuyerAgentFirstName")
    String coBuyerAgentFirstName;

    @Column(name = "CoBuyerAgentFullName")
    String coBuyerAgentFullName;

    @Column(name = "CoBuyerAgentHomePhone")
    String coBuyerAgentHomePhone;

    @Column(name = "CoBuyerAgentKeyNumeric")
    Integer coBuyerAgentKeyNumeric;

    @Column(name = "CoBuyerAgentLastName")
    String coBuyerAgentLastName;

    @Column(name = "CoBuyerAgentMiddleName")
    String coBuyerAgentMiddleName;

    @Column(name = "CoBuyerAgentMlsId")
    String coBuyerAgentMlsId;

    @Column(name = "CoBuyerAgentMobilePhone")
    String coBuyerAgentMobilePhone;

    @Column(name = "CoBuyerAgentNamePrefix")
    String coBuyerAgentNamePrefix;

    @Column(name = "CoBuyerAgentNameSuffix")
    String coBuyerAgentNameSuffix;

    @Column(name = "CoBuyerAgentOfficePhone")
    String coBuyerAgentOfficePhone;

    @Column(name = "CoBuyerAgentOfficePhoneExt")
    String coBuyerAgentOfficePhoneExt;

    @Column(name = "CoBuyerAgentPager")
    String coBuyerAgentPager;

    @Column(name = "CoBuyerAgentPreferredPhone")
    String coBuyerAgentPreferredPhone;

    @Column(name = "CoBuyerAgentPreferredPhoneExt")
    String coBuyerAgentPreferredPhoneExt;

    @Column(name = "CoBuyerAgentStateLicense")
    String coBuyerAgentStateLicense;

    @Column(name = "CoBuyerAgentTollFreePhone")
    String coBuyerAgentTollFreePhone;

    @Column(name = "CoBuyerAgentURL")
    String coBuyerAgentURL;

    @Column(name = "CoBuyerAgentVoiceMail")
    String coBuyerAgentVoiceMail;

    @Column(name = "CoBuyerAgentVoiceMailExt")
    String coBuyerAgentVoiceMailExt;

    @Column(name = "CoBuyerOfficeAOR")
    String coBuyerOfficeAOR;

    @Column(name = "CoBuyerOfficeEmail")
    String coBuyerOfficeEmail;

    @Column(name = "CoBuyerOfficeFax")
    String coBuyerOfficeFax;

    @Column(name = "CoBuyerOfficeKeyNumeric")
    Integer coBuyerOfficeKeyNumeric;

    @Column(name = "CoBuyerOfficeMlsId")
    String coBuyerOfficeMlsId;

    @Column(name = "CoBuyerOfficeName")
    String coBuyerOfficeName;

    @Column(name = "CoBuyerOfficePhone")
    String coBuyerOfficePhone;

    @Column(name = "CoBuyerOfficePhoneExt")
    String coBuyerOfficePhoneExt;

    @Column(name = "CoBuyerOfficeURL")
    String coBuyerOfficeURL;

    @Column(name = "CoListAgentAOR")
    String coListAgentAOR;

    @Column(name = "CoListAgentDesignation")
    String coListAgentDesignation;

    @Column(name = "CoListAgentDirectPhone")
    String coListAgentDirectPhone;

    @Column(name = "CoListAgentEmail")
    String coListAgentEmail;

    @Column(name = "CoListAgentFax")
    String coListAgentFax;

    @Column(name = "CoListAgentFirstName")
    String coListAgentFirstName;

    @Column(name = "CoListAgentFullName")
    String coListAgentFullName;

    @Column(name = "CoListAgentHomePhone")
    String coListAgentHomePhone;

    @Column(name = "CoListAgentKeyNumeric")
    Integer coListAgentKeyNumeric;

    @Column(name = "CoListAgentLastName")
    String coListAgentLastName;

    @Column(name = "CoListAgentMiddleName")
    String coListAgentMiddleName;

    @Column(name = "CoListAgentMlsId")
    String coListAgentMlsId;

    @Column(name = "CoListAgentMobilePhone")
    String coListAgentMobilePhone;

    @Column(name = "CoListAgentNamePrefix")
    String coListAgentNamePrefix;

    @Column(name = "CoListAgentNameSuffix")
    String coListAgentNameSuffix;

    @Column(name = "CoListAgentOfficePhone")
    String coListAgentOfficePhone;

    @Column(name = "CoListAgentOfficePhoneExt")
    String coListAgentOfficePhoneExt;

    @Column(name = "CoListAgentPager")
    String coListAgentPager;

    @Column(name = "CoListAgentPreferredPhone")
    String coListAgentPreferredPhone;

    @Column(name = "CoListAgentPreferredPhoneExt")
    String coListAgentPreferredPhoneExt;

    @Column(name = "CoListAgentStateLicense")
    String coListAgentStateLicense;

    @Column(name = "CoListAgentTollFreePhone")
    String coListAgentTollFreePhone;

    @Column(name = "CoListAgentURL")
    String coListAgentURL;

    @Column(name = "CoListAgentVoiceMail")
    String coListAgentVoiceMail;

    @Column(name = "CoListAgentVoiceMailExt")
    String coListAgentVoiceMailExt;

    @Column(name = "CoListOfficeAOR")
    String coListOfficeAOR;

    @Column(name = "CoListOfficeEmail")
    String coListOfficeEmail;

    @Column(name = "CoListOfficeFax")
    String coListOfficeFax;

    @Column(name = "CoListOfficeKeyNumeric")
    Integer coListOfficeKeyNumeric;

    @Column(name = "CoListOfficeMlsId")
    String coListOfficeMlsId;

    @Column(name = "CoListOfficeName")
    String coListOfficeName;

    @Column(name = "CoListOfficePhone")
    String coListOfficePhone;

    @Column(name = "CoListOfficePhoneExt")
    String coListOfficePhoneExt;

    @Column(name = "CoListOfficeURL")
    String coListOfficeURL;

    @Column(name = "CommonInterest")
    String commonInterest;

    @Column(name = "CommonWalls")
    String commonWalls;

    @Column(name = "CommunityFeatures")
    String communityFeatures;

    @Column(name = "Concessions")
    String concessions;

    @Column(name = "ConcessionsAmount")
    Integer concessionsAmount;

    @Column(name = "ConcessionsComments")
    String concessionsComments;

    @Column(name = "ConstructionMaterials")
    String constructionMaterials;

    @Column(name = "ContinentRegion")
    String continentRegion;

    @Column(name = "Contingency")
    String contingency;

    @Column(name = "ContingentDate")
    Date contingentDate;

    @Column(name = "ContractStatusChangeDate")
    Date contractStatusChangeDate;

    @Column(name = "Cooling")
    String cooling;

    @Column(name = "CoolingYN")
    Boolean coolingYN;

    @Column(name = "CopyrightNotice")
    String copyrightNotice;

    @Column(name = "Country")
    String country;

    @Column(name = "CountryRegion")
    String countryRegion;

    @Column(name = "CountyOrParish")
    String countyOrParish;

    @Column(name = "CoveredSpaces")
    Double coveredSpaces;

    @Column(name = "CropsIncludedYN")
    Boolean cropsIncludedYN;

    @Column(name = "CrossStreet")
    String crossStreet;

    @Column(name = "CultivatedArea")
    Double cultivatedArea;

    @Column(name = "CumulativeDaysOnMarket")
    Integer cumulativeDaysOnMarket;

    @Column(name = "CurrentFinancing")
    String currentFinancing;

    @Column(name = "CurrentUse")
    String currentUse;

    @Column(name = "DaysOnMarket")
    Integer daysOnMarket;

    @Column(name = "DaysOnMarketReplication")
    Integer daysOnMarketReplication;

    @Column(name = "DaysOnMarketReplicationDate")
    Date daysOnMarketReplicationDate;

    @Column(name = "DaysOnMarketReplicationIncreasingYN")
    Boolean daysOnMarketReplicationIncreasingYN;

    @Column(name = "DevelopmentStatus")
    String developmentStatus;

    @Column(name = "DirectionFaces")
    String directionFaces;

    @Column(name = "Directions")
    String directions;

    @Column(name = "Disclaimer")
    String disclaimer;

    @Column(name = "Disclosures")
    String disclosures;

    @Column(name = "DistanceToBusComments")
    String distanceToBusComments;

    @Column(name = "DistanceToBusNumeric")
    Integer distanceToBusNumeric;

    @Column(name = "DistanceToBusUnits")
    String distanceToBusUnits;

    @Column(name = "DistanceToElectricComments")
    String distanceToElectricComments;

    @Column(name = "DistanceToElectricNumeric")
    Integer distanceToElectricNumeric;

    @Column(name = "DistanceToElectricUnits")
    String distanceToElectricUnits;

    @Column(name = "DistanceToFreewayComments")
    String distanceToFreewayComments;

    @Column(name = "DistanceToFreewayNumeric")
    Integer distanceToFreewayNumeric;

    @Column(name = "DistanceToFreewayUnits")
    String distanceToFreewayUnits;

    @Column(name = "DistanceToGasComments")
    String distanceToGasComments;

    @Column(name = "DistanceToGasNumeric")
    Integer distanceToGasNumeric;

    @Column(name = "DistanceToGasUnits")
    String distanceToGasUnits;

    @Column(name = "DistanceToPhoneServiceComments")
    String distanceToPhoneServiceComments;

    @Column(name = "DistanceToPhoneServiceNumeric")
    Integer distanceToPhoneServiceNumeric;

    @Column(name = "DistanceToPhoneServiceUnits")
    String distanceToPhoneServiceUnits;

    @Column(name = "DistanceToPlaceofWorshipComments")
    String distanceToPlaceofWorshipComments;

    @Column(name = "DistanceToPlaceofWorshipNumeric")
    Integer distanceToPlaceofWorshipNumeric;

    @Column(name = "DistanceToPlaceofWorshipUnits")
    String distanceToPlaceofWorshipUnits;

    @Column(name = "DistanceToSchoolBusComments")
    String distanceToSchoolBusComments;

    @Column(name = "DistanceToSchoolBusNumeric")
    Integer distanceToSchoolBusNumeric;

    @Column(name = "DistanceToSchoolBusUnits")
    String distanceToSchoolBusUnits;

    @Column(name = "DistanceToSchoolsComments")
    String distanceToSchoolsComments;

    @Column(name = "DistanceToSchoolsNumeric")
    Integer distanceToSchoolsNumeric;

    @Column(name = "DistanceToSchoolsUnits")
    String distanceToSchoolsUnits;

    @Column(name = "DistanceToSewerComments")
    String distanceToSewerComments;

    @Column(name = "DistanceToSewerNumeric")
    Integer distanceToSewerNumeric;

    @Column(name = "DistanceToSewerUnits")
    String distanceToSewerUnits;

    @Column(name = "DistanceToShoppingComments")
    String distanceToShoppingComments;

    @Column(name = "DistanceToShoppingNumeric")
    Integer distanceToShoppingNumeric;

    @Column(name = "DistanceToShoppingUnits")
    String distanceToShoppingUnits;

    @Column(name = "DistanceToStreetComments")
    String distanceToStreetComments;

    @Column(name = "DistanceToStreetNumeric")
    Integer distanceToStreetNumeric;

    @Column(name = "DistanceToStreetUnits")
    String distanceToStreetUnits;

    @Column(name = "DistanceToWaterComments")
    String distanceToWaterComments;

    @Column(name = "DistanceToWaterNumeric")
    Integer distanceToWaterNumeric;

    @Column(name = "DistanceToWaterUnits")
    String distanceToWaterUnits;

    @Column(name = "DocumentsAvailable")
    String documentsAvailable;

    @Column(name = "DocumentsChangeTimestamp")
    Date documentsChangeTimestamp;

    @Column(name = "DocumentsCount")
    Integer documentsCount;

    @Column(name = "DOH1")
    String dOH1;

    @Column(name = "DOH2")
    String dOH2;

    @Column(name = "DOH3")
    String dOH3;

    @Column(name = "DoorFeatures")
    String doorFeatures;

    @Column(name = "DualVariableCompensationYN")
    Boolean dualVariableCompensationYN;

    @Column(name = "Electric")
    String electric;

    @Column(name = "ElectricExpense")
    Double electricExpense;

    @Column(name = "ElectricOnPropertyYN")
    Boolean electricOnPropertyYN;

    @Column(name = "ElementarySchool")
    String elementarySchool;

    @Column(name = "ElementarySchoolDistrict")
    String elementarySchoolDistrict;

    @Column(name = "Elevation")
    Integer elevation;

    @Column(name = "ElevationUnits")
    String elevationUnits;

    @Column(name = "EntryLevel")
    Integer entryLevel;

    @Column(name = "EntryLocation")
    String entryLocation;

    @Column(name = "EstimatedActiveDate")
    Date estimatedActiveDate;

    @Column(name = "Exclusions")
    String exclusions;

    @Column(name = "ExistingLeaseType")
    String existingLeaseType;

    @Column(name = "ExpirationDate")
    Date expirationDate;

    @Column(name = "ExteriorFeatures")
    String exteriorFeatures;

    @Column(name = "FarmCreditServiceInclYN")
    Boolean farmCreditServiceInclYN;

    @Column(name = "FarmLandAreaSource")
    String farmLandAreaSource;

    @Column(name = "FarmLandAreaUnits")
    String farmLandAreaUnits;

    @Column(name = "Fencing")
    String fencing;

    @Column(name = "FinancialDataSource")
    String financialDataSource;

    @Column(name = "FireplaceFeatures")
    String fireplaceFeatures;

    @Column(name = "FireplacesTotal")
    Integer fireplacesTotal;

    @Column(name = "FireplaceYN")
    Boolean fireplaceYN;

    @Column(name = "Flooring")
    String flooring;

    @Column(name = "FoundationArea")
    Double foundationArea;

    @Column(name = "FoundationDetails")
    String foundationDetails;

    @Column(name = "FrontageLength")
    String frontageLength;

    @Column(name = "FrontageType")
    String frontageType;

    @Column(name = "FuelExpense")
    Double fuelExpense;

    @Column(name = "Furnished")
    String furnished;

    @Column(name = "FurnitureReplacementExpense")
    Double furnitureReplacementExpense;

    @Column(name = "GarageSpaces")
    Double garageSpaces;

    @Column(name = "GarageYN")
    Boolean garageYN;

    @Column(name = "GardenerExpense")
    Double gardenerExpense;

    @Column(name = "GrazingPermitsBlmYN")
    Boolean grazingPermitsBlmYN;

    @Column(name = "GrazingPermitsForestServiceYN")
    Boolean grazingPermitsForestServiceYN;

    @Column(name = "GrazingPermitsPrivateYN")
    Boolean grazingPermitsPrivateYN;

    @Column(name = "GreenEnergyEfficient")
    String greenEnergyEfficient;

    @Column(name = "GreenEnergyGeneration")
    String greenEnergyGeneration;

    @Column(name = "GreenIndoorAirQuality")
    String greenIndoorAirQuality;

    @Column(name = "GreenLocation")
    String greenLocation;

    @Column(name = "GreenSustainability")
    String greenSustainability;

    @Column(name = "GreenWaterConservation")
    String greenWaterConservation;

    @Column(name = "GrossIncome")
    Double grossIncome;

    @Column(name = "GrossScheduledIncome")
    Double grossScheduledIncome;

    @Column(name = "HabitableResidenceYN")
    Boolean habitableResidenceYN;

    @Column(name = "Heating")
    String heating;

    @Column(name = "HeatingYN")
    Boolean heatingYN;

    @Column(name = "HighSchool")
    String highSchool;

    @Column(name = "HighSchoolDistrict")
    String highSchoolDistrict;

    @Column(name = "HomeWarrantyYN")
    Boolean homeWarrantyYN;

    @Column(name = "HorseAmenities")
    String horseAmenities;

    @Column(name = "HorseYN")
    Boolean horseYN;

    @Column(name = "HoursDaysOfOperation")
    String hoursDaysOfOperation;

    @Column(name = "HoursDaysOfOperationDescription")
    String hoursDaysOfOperationDescription;

    @Column(name = "Inclusions")
    String inclusions;

    @Column(name = "IncomeIncludes")
    String incomeIncludes;

    @Column(name = "InsuranceExpense")
    Double insuranceExpense;

    @Column(name = "InteriorFeatures")
    String interiorFeatures;

    @Column(name = "InternetAddressDisplayYN")
    Boolean internetAddressDisplayYN;

    @Column(name = "InternetAutomatedValuationDisplayYN")
    Boolean internetAutomatedValuationDisplayYN;

    @Column(name = "InternetConsumerCommentYN")
    Boolean internetConsumerCommentYN;

    @Column(name = "InternetEntireListingDisplayYN")
    Boolean internetEntireListingDisplayYN;

    @Column(name = "IrrigationSource")
    String irrigationSource;

    @Column(name = "IrrigationWaterRightsAcres")
    Double irrigationWaterRightsAcres;

    @Column(name = "IrrigationWaterRightsYN")
    Boolean irrigationWaterRightsYN;

    @Column(name = "LaborInformation")
    String laborInformation;

    @Column(name = "LandLeaseAmount")
    Double landLeaseAmount;

    @Column(name = "LandLeaseAmountFrequency")
    String landLeaseAmountFrequency;

    @Column(name = "LandLeaseExpirationDate")
    Date landLeaseExpirationDate;

    @Column(name = "LandLeaseYN")
    Boolean landLeaseYN;

    @Column(name = "Latitude")
    Double latitude;

    @Column(name = "LaundryFeatures")
    String laundryFeatures;

    @Column(name = "LeasableArea")
    Double leasableArea;

    @Column(name = "LeasableAreaUnits")
    String leasableAreaUnits;

    @Column(name = "LeaseAmount")
    Double leaseAmount;

    @Column(name = "LeaseAmountFrequency")
    String leaseAmountFrequency;

    @Column(name = "LeaseAssignableYN")
    Boolean leaseAssignableYN;

    @Column(name = "LeaseConsideredYN")
    Boolean leaseConsideredYN;

    @Column(name = "LeaseExpiration")
    Date leaseExpiration;

    @Column(name = "LeaseRenewalCompensation")
    String leaseRenewalCompensation;

    @Column(name = "LeaseRenewalOptionYN")
    Boolean leaseRenewalOptionYN;

    @Column(name = "LeaseTerm")
    String leaseTerm;

    @Column(name = "Levels")
    String levels;

    @Column(name = "License1")
    String license1;

    @Column(name = "License2")
    String license2;

    @Column(name = "License3")
    String license3;

    @Column(name = "LicensesExpense")
    Double licensesExpense;

    @Column(name = "ListAgentAOR")
    String listAgentAOR;

    @Column(name = "ListAgentDesignation")
    String listAgentDesignation;

    @Column(name = "ListAgentDirectPhone")
    String listAgentDirectPhone;

    @Column(name = "ListAgentEmail")
    String listAgentEmail;

    @Column(name = "ListAgentFax")
    String listAgentFax;

    @Column(name = "ListAgentFirstName")
    String listAgentFirstName;

    @Column(name = "ListAgentFullName")
    String listAgentFullName;

    @Column(name = "ListAgentHomePhone")
    String listAgentHomePhone;

    @Column(name = "ListAgentKeyNumeric")
    Integer listAgentKeyNumeric;

    @Column(name = "ListAgentLastName")
    String listAgentLastName;

    @Column(name = "ListAgentMiddleName")
    String listAgentMiddleName;

    @Column(name = "ListAgentMlsId")
    String listAgentMlsId;

    @Column(name = "ListAgentMobilePhone")
    String listAgentMobilePhone;

    @Column(name = "ListAgentNamePrefix")
    String listAgentNamePrefix;

    @Column(name = "ListAgentNameSuffix")
    String listAgentNameSuffix;

    @Column(name = "ListAgentOfficePhone")
    String listAgentOfficePhone;

    @Column(name = "ListAgentOfficePhoneExt")
    String listAgentOfficePhoneExt;

    @Column(name = "ListAgentPager")
    String listAgentPager;

    @Column(name = "ListAgentPreferredPhone")
    String listAgentPreferredPhone;

    @Column(name = "ListAgentPreferredPhoneExt")
    String listAgentPreferredPhoneExt;

    @Column(name = "ListAgentStateLicense")
    String listAgentStateLicense;

    @Column(name = "ListAgentTollFreePhone")
    String listAgentTollFreePhone;

    @Column(name = "ListAgentURL")
    String listAgentURL;

    @Column(name = "ListAgentVoiceMail")
    String listAgentVoiceMail;

    @Column(name = "ListAgentVoiceMailExt")
    String listAgentVoiceMailExt;

    @Column(name = "ListAOR")
    String listAOR;

    @Column(name = "ListingAgreement")
    String listingAgreement;

    @Column(name = "ListingContractDate")
    Date listingContractDate;

    @Column(name = "ListingId")
    String listingId;

    @Column(name = "ListingKey")
    String listingKey;

    @Column(name = "ListingKeyNumeric")
    Integer listingKeyNumeric;

    @Column(name = "ListingService")
    String listingService;

    @Column(name = "ListingTerms")
    String listingTerms;

    @Column(name = "ListOfficeAOR")
    String listOfficeAOR;

    @Column(name = "ListOfficeEmail")
    String listOfficeEmail;

    @Column(name = "ListOfficeFax")
    String listOfficeFax;

    @Column(name = "ListOfficeKeyNumeric")
    Integer listOfficeKeyNumeric;

    @Column(name = "ListOfficeMlsId")
    String listOfficeMlsId;

    @Column(name = "ListOfficeName")
    String listOfficeName;

    @Column(name = "ListOfficePhone")
    String listOfficePhone;

    @Column(name = "ListOfficePhoneExt")
    String listOfficePhoneExt;

    @Column(name = "ListOfficeURL")
    String listOfficeURL;

    @Column(name = "ListPrice")
    Double listPrice;

    @Column(name = "ListPriceLow")
    Double listPriceLow;

    @Column(name = "ListTeamKeyNumeric")
    Integer listTeamKeyNumeric;

    @Column(name = "ListTeamName")
    String listTeamName;

    @Column(name = "LivingArea")
    Double livingArea;

    @Column(name = "LivingAreaSource")
    String livingAreaSource;

    @Column(name = "LivingAreaUnits")
    String livingAreaUnits;

    @Column(name = "LockBoxLocation")
    String lockBoxLocation;

    @Column(name = "LockBoxSerialNumber")
    String lockBoxSerialNumber;

    @Column(name = "LockBoxType")
    String lockBoxType;

    @Column(name = "Longitude")
    Double longitude;

    @Column(name = "LotDimensionsSource")
    String lotDimensionsSource;

    @Column(name = "LotFeatures")
    String lotFeatures;

    @Column(name = "LotSizeAcres")
    Double lotSizeAcres;

    @Column(name = "LotSizeArea")
    Double lotSizeArea;

    @Column(name = "LotSizeDimensions")
    String lotSizeDimensions;

    @Column(name = "LotSizeSource")
    String lotSizeSource;

    @Column(name = "LotSizeSquareFeet")
    Double lotSizeSquareFeet;

    @Column(name = "LotSizeUnits")
    String lotSizeUnits;

    @Column(name = "MainLevelBathrooms")
    Integer mainLevelBathrooms;

    @Column(name = "MainLevelBedrooms")
    Integer mainLevelBedrooms;

    @Column(name = "MaintenanceExpense")
    Double maintenanceExpense;

    @Column(name = "MajorChangeTimestamp")
    Date majorChangeTimestamp;

    @Column(name = "MajorChangeType")
    String majorChangeType;

    @Column(name = "Make")
    String make;

    @Column(name = "ManagerExpense")
    Double managerExpense;

    @Column(name = "MapCoordinate")
    String mapCoordinate;

    @Column(name = "MapCoordinateSource")
    String mapCoordinateSource;

    @Column(name = "MapURL")
    String mapURL;

    @Column(name = "MiddleOrJuniorSchool")
    String middleOrJuniorSchool;

    @Column(name = "MiddleOrJuniorSchoolDistrict")
    String middleOrJuniorSchoolDistrict;

    @Column(name = "MLSAreaMajor")
    String mLSAreaMajor;

    @Column(name = "MLSAreaMinor")
    String mLSAreaMinor;

    @Column(name = "MlsStatus")
    String mlsStatus;

    @Column(name = "MobileDimUnits")
    String mobileDimUnits;

    @Column(name = "MobileHomeRemainsYN")
    Boolean mobileHomeRemainsYN;

    @Column(name = "MobileLength")
    Integer mobileLength;

    @Column(name = "MobileWidth")
    Integer mobileWidth;

    @Column(name = "Model")
    String model;

    @Column(name = "ModificationTimestamp")
    Date modificationTimestamp;

    @Column(name = "NetOperatingIncome")
    Double netOperatingIncome;

    @Column(name = "NewConstructionYN")
    Boolean newConstructionYN;

    @Column(name = "NewTaxesExpense")
    Double newTaxesExpense;

    @Column(name = "NumberOfBuildings")
    Integer numberOfBuildings;

    @Column(name = "NumberOfFullTimeEmployees")
    Integer numberOfFullTimeEmployees;

    @Column(name = "NumberOfLots")
    Integer numberOfLots;

    @Column(name = "NumberOfPads")
    Integer numberOfPads;

    @Column(name = "NumberOfPartTimeEmployees")
    Integer numberOfPartTimeEmployees;

    @Column(name = "NumberOfSeparateElectricMeters")
    Integer numberOfSeparateElectricMeters;

    @Column(name = "NumberOfSeparateGasMeters")
    Integer numberOfSeparateGasMeters;

    @Column(name = "NumberOfSeparateWaterMeters")
    Integer numberOfSeparateWaterMeters;

    @Column(name = "NumberOfUnitsInCommunity")
    Integer numberOfUnitsInCommunity;

    @Column(name = "NumberOfUnitsLeased")
    Integer numberOfUnitsLeased;

    @Column(name = "NumberOfUnitsMoMo")
    Integer numberOfUnitsMoMo;

    @Column(name = "NumberOfUnitsTotal")
    Integer numberOfUnitsTotal;

    @Column(name = "NumberOfUnitsVacant")
    Integer numberOfUnitsVacant;

    @Column(name = "OccupantName")
    String occupantName;

    @Column(name = "OccupantPhone")
    String occupantPhone;

    @Column(name = "OccupantType")
    String occupantType;

    @Column(name = "OffMarketDate")
    Date offMarketDate;

    @Column(name = "OffMarketTimestamp")
    Date offMarketTimestamp;

    @Column(name = "OnMarketDate")
    Date onMarketDate;

    @Column(name = "OnMarketTimestamp")
    Date onMarketTimestamp;

    @Column(name = "OpenParkingSpaces")
    Double openParkingSpaces;

    @Column(name = "OpenParkingYN")
    Boolean openParkingYN;

    @Column(name = "OperatingExpense")
    Double operatingExpense;

    @Column(name = "OperatingExpenseIncludes")
    String operatingExpenseIncludes;

    @Column(name = "OriginalEntryTimestamp")
    Date originalEntryTimestamp;

    @Column(name = "OriginalListPrice")
    Double originalListPrice;

    @Column(name = "OriginatingSystemID")
    String originatingSystemID;

    @Column(name = "OriginatingSystemKey")
    String originatingSystemKey;

    @Column(name = "OriginatingSystemName")
    String originatingSystemName;

    @Column(name = "OriginatingSystemSubName")
    String originatingSystemSubName;

    @Column(name = "OtherEquipment")
    String otherEquipment;

    @Column(name = "OtherExpense")
    Double otherExpense;

    @Column(name = "OtherParking")
    String otherParking;

    @Column(name = "OtherStructures")
    String otherStructures;

    @Column(name = "OwnerName")
    String ownerName;

    @Column(name = "OwnerPays")
    String ownerPays;

    @Column(name = "OwnerPhone")
    String ownerPhone;

    @Column(name = "Ownership")
    String ownership;

    @Column(name = "OwnershipType")
    String ownershipType;

    @Column(name = "ParcelNumber")
    String parcelNumber;

    @Column(name = "ParkingFeatures")
    String parkingFeatures;

    @Column(name = "ParkingTotal")
    Double parkingTotal;

    @Column(name = "ParkManagerName")
    String parkManagerName;

    @Column(name = "ParkManagerPhone")
    String parkManagerPhone;

    @Column(name = "ParkName")
    String parkName;

    @Column(name = "PastureArea")
    Double pastureArea;

    @Column(name = "PatioAndPorchFeatures")
    String patioAndPorchFeatures;

    @Column(name = "PendingTimestamp")
    Date pendingTimestamp;

    @Column(name = "Permission")
    String permission;

    @Column(name = "PermissionPrivate")
    String permissionPrivate;

    @Column(name = "PestControlExpense")
    Double pestControlExpense;

    @Column(name = "PetsAllowed")
    String petsAllowed;

    @Column(name = "PhotosChangeTimestamp")
    Date photosChangeTimestamp;

    @Column(name = "PhotosCount")
    Integer photosCount;

    @Column(name = "PoolExpense")
    Double poolExpense;

    @Column(name = "PoolFeatures")
    String poolFeatures;

    @Column(name = "PoolPrivateYN")
    Boolean poolPrivateYN;

    @Column(name = "Possession")
    String possession;

    @Column(name = "PossibleUse")
    String possibleUse;

    @Column(name = "PostalCity")
    String postalCity;

    @Column(name = "PostalCode")
    String postalCode;

    @Column(name = "PostalCodePlus4")
    String postalCodePlus4;

    @Column(name = "PreviousListPrice")
    Double previousListPrice;

    @Column(name = "PriceChangeTimestamp")
    Date priceChangeTimestamp;

    @Column(name = "PrivateOfficeRemarks")
    String privateOfficeRemarks;

    @Column(name = "PrivateRemarks")
    String privateRemarks;

    @Column(name = "ProfessionalManagementExpense")
    Double professionalManagementExpense;

    @Column(name = "PropertyAttachedYN")
    Boolean propertyAttachedYN;

    @Column(name = "PropertyCondition")
    String propertyCondition;

    @Column(name = "PropertySubType")
    String propertySubType;

    @Column(name = "PropertyType")
    String propertyType;

    @Column(name = "PublicRemarks")
    String publicRemarks;

    @Column(name = "PublicSurveyRange")
    String publicSurveyRange;

    @Column(name = "PublicSurveySection")
    String publicSurveySection;

    @Column(name = "PublicSurveyTownship")
    String publicSurveyTownship;

    @Column(name = "PurchaseContractDate")
    Date purchaseContractDate;

    @Column(name = "RangeArea")
    Double rangeArea;

    @Column(name = "RentControlYN")
    Boolean rentControlYN;

    @Column(name = "RentIncludes")
    String rentIncludes;

    @Column(name = "RoadFrontageType")
    String roadFrontageType;

    @Column(name = "RoadResponsibility")
    String roadResponsibility;

    @Column(name = "RoadSurfaceType")
    String roadSurfaceType;

    @Column(name = "Roof")
    String roof;

    @Column(name = "RoomsTotal")
    Integer roomsTotal;

    @Column(name = "RVParkingDimensions")
    String rVParkingDimensions;

    @Column(name = "SeatingCapacity")
    Integer seatingCapacity;

    @Column(name = "SecurityFeatures")
    String securityFeatures;

    @Column(name = "SeniorCommunityYN")
    Boolean seniorCommunityYN;

    @Column(name = "SerialU")
    String serialU;

    @Column(name = "SerialX")
    String serialX;

    @Column(name = "SerialXX")
    String serialXX;

    @Column(name = "Sewer")
    String sewer;

    @Column(name = "ShowingAdvanceNotice")
    Integer showingAdvanceNotice;

    @Column(name = "ShowingAttendedYN")
    Boolean showingAttendedYN;

    @Column(name = "ShowingContactName")
    String showingContactName;

    @Column(name = "ShowingContactPhone")
    String showingContactPhone;

    @Column(name = "ShowingContactPhoneExt")
    String showingContactPhoneExt;

    @Column(name = "ShowingContactType")
    String showingContactType;

    @Column(name = "ShowingDays")
    String showingDays;

    @Column(name = "ShowingEndTime")
    Date showingEndTime;

    @Column(name = "ShowingInstructions")
    String showingInstructions;

    @Column(name = "ShowingRequirements")
    String showingRequirements;

    @Column(name = "ShowingStartTime")
    Date showingStartTime;

    @Column(name = "SignOnPropertyYN")
    Boolean signOnPropertyYN;

    @Column(name = "Skirt")
    String skirt;

    @Column(name = "SourceSystemID")
    String sourceSystemID;

    @Column(name = "SourceSystemKey")
    String sourceSystemKey;

    @Column(name = "SourceSystemName")
    String sourceSystemName;

    @Column(name = "SpaFeatures")
    String spaFeatures;

    @Column(name = "SpaYN")
    Boolean spaYN;

    @Column(name = "SpecialLicenses")
    String specialLicenses;

    @Column(name = "SpecialListingConditions")
    String specialListingConditions;

    @Column(name = "StandardStatus")
    String standardStatus;

    @Column(name = "StateOrProvince")
    String stateOrProvince;

    @Column(name = "StateRegion")
    String stateRegion;

    @Column(name = "StatusChangeTimestamp")
    Date statusChangeTimestamp;

    @Column(name = "Stories")
    Integer stories;

    @Column(name = "StoriesTotal")
    Integer storiesTotal;

    @Column(name = "StreetAdditionalInfo")
    String streetAdditionalInfo;

    @Column(name = "StreetDirPrefix")
    String streetDirPrefix;

    @Column(name = "StreetDirSuffix")
    String streetDirSuffix;

    @Column(name = "StreetName")
    String streetName;

    @Column(name = "StreetNumber")
    String streetNumber;

    @Column(name = "StreetNumberNumeric")
    Integer streetNumberNumeric;

    @Column(name = "StreetSuffix")
    String streetSuffix;

    @Column(name = "StreetSuffixModifier")
    String streetSuffixModifier;

    @Column(name = "StructureType")
    String structureType;

    @Column(name = "SubAgencyCompensation")
    String subAgencyCompensation;

    @Column(name = "SubAgencyCompensationType")
    String subAgencyCompensationType;

    @Column(name = "SubdivisionName")
    String subdivisionName;

    @Column(name = "SuppliesExpense")
    Double suppliesExpense;

    @Column(name = "SyndicateTo")
    String syndicateTo;

    @Column(name = "SyndicationRemarks")
    String syndicationRemarks;

    @Column(name = "TaxAnnualAmount")
    Double taxAnnualAmount;

    @Column(name = "TaxAssessedValue")
    Integer taxAssessedValue;

    @Column(name = "TaxBlock")
    String taxBlock;

    @Column(name = "TaxBookNumber")
    String taxBookNumber;

    @Column(name = "TaxLegalDescription")
    String taxLegalDescription;

    @Column(name = "TaxLot")
    String taxLot;

    @Column(name = "TaxMapNumber")
    String taxMapNumber;

    @Column(name = "TaxOtherAnnualAssessmentAmount")
    Double taxOtherAnnualAssessmentAmount;

    @Column(name = "TaxParcelLetter")
    String taxParcelLetter;

    @Column(name = "TaxStatusCurrent")
    String taxStatusCurrent;

    @Column(name = "TaxTract")
    String taxTract;

    @Column(name = "TaxYear")
    Integer taxYear;

    @Column(name = "TenantPays")
    String tenantPays;

    @Column(name = "Topography")
    String topography;

    @Column(name = "TotalActualRent")
    Double totalActualRent;

    @Column(name = "Township")
    String township;

    @Column(name = "TransactionBrokerCompensation")
    String transactionBrokerCompensation;

    @Column(name = "TransactionBrokerCompensationType")
    String transactionBrokerCompensationType;

    @Column(name = "TrashExpense")
    Double trashExpense;

    @Column(name = "UnitNumber")
    String unitNumber;

    @Column(name = "UnitsFurnished")
    String unitsFurnished;

    @Column(name = "UniversalPropertyId")
    String universalPropertyId;

    @Column(name = "UniversalPropertySubId")
    String universalPropertySubId;

    @Column(name = "UnparsedAddress")
    String unparsedAddress;

    @Column(name = "Utilities")
    String utilities;

    @Column(name = "VacancyAllowance")
    Integer vacancyAllowance;

    @Column(name = "VacancyAllowanceRate")
    Double vacancyAllowanceRate;

    @Column(name = "Vegetation")
    String vegetation;

    @Column(name = "VideosChangeTimestamp")
    Date videosChangeTimestamp;

    @Column(name = "VideosCount")
    Integer videosCount;

    @Column(name = "View")
    String view;

    @Column(name = "ViewYN")
    Boolean viewYN;

    @Column(name = "VirtualTourURLBranded")
    String virtualTourURLBranded;

    @Column(name = "VirtualTourURLUnbranded")
    String virtualTourURLUnbranded;

    @Column(name = "WalkScore")
    Integer walkScore;

    @Column(name = "WaterBodyName")
    String waterBodyName;

    @Column(name = "WaterfrontFeatures")
    String waterfrontFeatures;

    @Column(name = "WaterfrontYN")
    Boolean waterfrontYN;

    @Column(name = "WaterSewerExpense")
    Double waterSewerExpense;

    @Column(name = "WaterSource")
    String waterSource;

    @Column(name = "WindowFeatures")
    String windowFeatures;

    @Column(name = "WithdrawnDate")
    Date withdrawnDate;

    @Column(name = "WoodedArea")
    Double woodedArea;

    @Column(name = "WorkmansCompensationExpense")
    Double workmansCompensationExpense;

    @Column(name = "X_LocationLatitude")
    Double X_LocationLatitude;

    @Column(name = "X_LocationLongitude")
    Double X_LocationLongitude;

    @Column(name = "X_GeocodeSource")
    String X_GeocodeSource;

    @Column(name = "X_LandTenure")
    String X_LandTenure;

    @Column(name = "X_LivingAreaRange")
    String X_LivingAreaRange;

    @Column(name = "YearBuilt")
    Integer yearBuilt;

    @Column(name = "YearBuiltDetails")
    String yearBuiltDetails;

    @Column(name = "YearBuiltEffective")
    Integer yearBuiltEffective;

    @Column(name = "YearBuiltSource")
    String yearBuiltSource;

    @Column(name = "YearEstablished")
    Integer yearEstablished;

    @Column(name = "YearsCurrentOwner")
    Integer yearsCurrentOwner;

    @Column(name = "Zoning")
    String zoning;

    @Column(name = "ZoningDescription")
    String zoningDescription;

    
    @Transient
    public String getAddress()
    {
        return getAddressLine1() + ", " + getAddressLine2();
    }

    @Transient
    public boolean hasValidAddress()
    {
        return !isNullOrEmpty(this.getStreetNumber())
                && !isNullOrEmpty(this.getStreetName())
                && !isNullOrEmpty(this.getStreetSuffix())
                && !isNullOrEmpty(this.getCity())
                && !isNullOrEmpty(this.getStateOrProvince())
                && !isNullOrEmpty(this.getPostalCode());
    }

    @Transient
    public boolean hasStandardizableAddress() {
        /*
         * Require the unit number to be null or empty, otherwise
         * we can't skip more involved address verification here.
         */
        return isNullOrEmpty(this.getUnitNumber()) && this.hasValidAddress(); 
    }

    @Transient
    public String getAddressLine1()
    {
        final String dirprefix = getStreetDirPrefix(), dirsuffix = getStreetDirSuffix(), unit = getUnitNumber();

        return getStreetNumber() + " " + (isNullOrEmpty(dirprefix) ? "" : dirprefix + " ")
                + getStreetName() + " " + getStreetSuffix()
                + (isNullOrEmpty(dirsuffix) ? "" : " " + dirsuffix)
                + (isNullOrEmpty(unit) ? "" : " #" + unit);
    }

    @Transient
    public String getAddressLine2()
    {
        return getCity() + ", " + getStateOrProvince() + " " + getPostalCode();
    }

    @Transient
    public int getTrestleTableOrdinal()
    {
        return 1;
    }


    private static boolean isNullOrEmpty(String s)
    {
        return s == null || s.isEmpty();
    }
}
