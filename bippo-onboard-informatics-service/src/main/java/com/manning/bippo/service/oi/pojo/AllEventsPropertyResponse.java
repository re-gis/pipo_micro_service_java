package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AllEventsPropertyResponse
{

    public Status status;
    public Property[] property;

    @JsonCreator
    public AllEventsPropertyResponse(@JsonProperty("status") Status status, @JsonProperty("property") Property[] property)
    {
        this.status = status;
        this.property = property;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status
    {
        public String version;
        public Integer code;
        public String msg;
        public Integer total;
        public long page;

        @JsonCreator
        public Status(@JsonProperty("version") String version, @JsonProperty("code") Integer code,
                      @JsonProperty("msg") String msg, @JsonProperty("total") Integer total, @JsonProperty("page") long page)
        {
            this.version = version;
            this.code = code;
            this.msg = msg;
            this.total = total;
            this.page = page;
        }
    }

    public static class Property
    {
        public Identifier identifier;
        public Lot lot;
        public Area area;
        public Address address;
        public Location location;
        public PropertySummary summary;
        public Utilities utilities;
        public Building building;
        public Vintage vintage;
        public Avm avm;
        public Sale sale;
        public Assessment assessment;

        @JsonCreator
        public Property(@JsonProperty("identifier") Identifier identifier, @JsonProperty("lot") Lot lot,
                        @JsonProperty("area") Area area, @JsonProperty("address") Address address,
                        @JsonProperty("location") Location location, @JsonProperty("summary") PropertySummary summary,
                        @JsonProperty("utilities") Utilities utilities, @JsonProperty("building") Building building,
                        @JsonProperty("vintage") Vintage vintage, @JsonProperty("avm") Avm avm, @JsonProperty("sale") Sale sale,
                        @JsonProperty("assessment") Assessment assessment)
        {
            this.identifier = identifier;
            this.lot = lot;
            this.area = area;
            this.address = address;
            this.location = location;
            this.summary = summary;
            this.utilities = utilities;
            this.building = building;
            this.vintage = vintage;
            this.avm = avm;
            this.sale = sale;
            this.assessment = assessment;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Identifier
        {
            public long obPropId;
            public String fips;
            public String apn;
            public String apnOrig;
            public long attomId;

            @JsonCreator
            public Identifier(@JsonProperty("obPropId") long obPropId, @JsonProperty("fips") String fips,
                              @JsonProperty("apn") String apn, @JsonProperty("apnOrig") String apnOrig,
                              @JsonProperty("attomId") long attomId)
            {
                this.obPropId = obPropId;
                this.fips = fips;
                this.apn = apn;
                this.apnOrig = apnOrig;
                this.attomId = attomId;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Lot
        {
            public long depth;
            public long frontage;
            public String lotNum;
            public float lotSizeInAcres;
            public long lotSizeInSQFT;
            public String lotType;
            public String poolInd;
            public String poolType;

            @JsonCreator
            public Lot(@JsonProperty("depth") long depth, @JsonProperty("frontage") long frontage,
                       @JsonProperty("lotnum") String lotNum, @JsonProperty("lotsize1") float lotSizeInAcres,
                       @JsonProperty("lotsize2") long lotSizeInSQFT, @JsonProperty("lottype") String lotType,
                       @JsonProperty("poolind") String poolInd, @JsonProperty("pooltype") String poolType)
            {
                this.depth = depth;
                this.frontage = frontage;
                this.lotNum = lotNum;
                this.lotSizeInAcres = lotSizeInAcres;
                this.lotSizeInSQFT = lotSizeInSQFT;
                this.lotType = lotType;
                this.poolInd = poolInd;
                this.poolType = poolType;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Area
        {
            public String blockNum;
            public String locType;
            public String countrysecsubd;
            public String countyUse1;
            public String countyUse2;
            public String munCode;
            public String munName;
            public String subdName;
            public String taxCodeArea;
            public String subdTractNum;
            public String srvyRange;
            public String srvySection;
            public String srvyTownship;

            @JsonCreator
            public Area(@JsonProperty("blockNum") String blockNum, @JsonProperty("loctype") String locType,
                        @JsonProperty("countrysecsubd") String countrysecsubd, @JsonProperty("countyuse1") String countyUse1, @JsonProperty("countyuse2") String countyUse2,
                        @JsonProperty("muncode") String munCode, @JsonProperty("munname") String munName,
                        @JsonProperty("subdname") String subdName, @JsonProperty("taxcodearea") String taxCodeArea,
                        @JsonProperty("subdtractnum") String subdTractNum, @JsonProperty("srvyRange") String srvyRange,
                        @JsonProperty("srvySection") String srvySection, @JsonProperty("srvyTownship") String srvyTownship)
            {
                this.blockNum = blockNum;
                this.locType = locType;
                this.countrysecsubd = countrysecsubd;
                this.countyUse1 = countyUse1;
                this.countyUse2 = countyUse2;
                this.munCode = munCode;
                this.munName = munName;
                this.subdName = subdName;
                this.taxCodeArea = taxCodeArea;
                this.subdTractNum = subdTractNum;
                this.srvyRange = srvyRange;
                this.srvySection = srvySection;
                this.srvyTownship = srvyTownship;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address
        {
            public String country;
            public String countrySubd;
            public String line1;
            public String line2;
            public String locality;
            public String matchCode;
            public String oneLine;
            public String postal1;
            public String postal2;
            public String postal3;

            @JsonCreator
            public Address(@JsonProperty("country") String country, @JsonProperty("countrySubd") String countrySubd,
                           @JsonProperty("line1") String line1, @JsonProperty("line2") String line2,
                           @JsonProperty("locality") String locality, @JsonProperty("matchCode") String matchCode,
                           @JsonProperty("oneLine") String oneLine, @JsonProperty("postal1") String postal1,
                           @JsonProperty("postal2") String postal2, @JsonProperty("postal3") String postal3)
            {
                this.country = country;
                this.countrySubd = countrySubd;
                this.line1 = line1;
                this.line2 = line2;
                this.locality = locality;
                this.matchCode = matchCode;
                this.oneLine = oneLine;
                this.postal1 = postal1;
                this.postal2 = postal2;
                this.postal3 = postal3;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location
        {
            public String accuracy;
            public long elevation;
            public String latitude;
            public String longitude;
            public long distance;
            public String geoid;

            @JsonCreator
            public Location(@JsonProperty("accuracy") String accuracy, @JsonProperty("elevation") long elevation,
                            @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude,
                            @JsonProperty("distance") long distance, @JsonProperty("geoid") String geoid)
            {
                this.accuracy = accuracy;
                this.elevation = elevation;
                this.latitude = latitude;
                this.longitude = longitude;
                this.distance = distance;
                this.geoid = geoid;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PropertySummary
        {
            public String absenteeInd;
            public String propClass;
            public String propSubType;
            public String propType;
            public long yearBuilt;
            public String propLandUse;
            public int propIndicator;
            public String legal1;

            @JsonCreator
            public PropertySummary(@JsonProperty("absenteeInd") String absenteeInd, @JsonProperty("propclass") String propClass,
                                   @JsonProperty("propsubtype") String propSubType, @JsonProperty("proptype") String propType,
                                   @JsonProperty("yearbuilt") long yearBuilt, @JsonProperty("propLandUse") String propLandUse,
                                   @JsonProperty("propIndicator") int propIndicator, @JsonProperty("legal1") String legal1)
            {
                this.absenteeInd = absenteeInd;
                this.propClass = propClass;
                this.propSubType = propSubType;
                this.propType = propType;
                this.yearBuilt = yearBuilt;
                this.propLandUse = propLandUse;
                this.propIndicator = propIndicator;
                this.legal1 = legal1;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Utilities
        {
            public String coolingType;
            public String energyType;
            public String heatingFuel;
            public String heatingType;
            public String sewerType;

            public String wallType;

            @JsonCreator
            public Utilities(@JsonProperty("coolingtype") String coolingType, @JsonProperty("energyType") String energyType,
                             @JsonProperty("heatingfuel") String heatingFuel, @JsonProperty("heatingtype") String heatingType,
                             @JsonProperty("sewertype") String sewerType, @JsonProperty("wallType") String wallType)
            {
                this.coolingType = coolingType;
                this.energyType = energyType;
                this.heatingFuel = heatingFuel;
                this.heatingType = heatingType;
                this.wallType = wallType;
                this.sewerType = sewerType;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Building
        {
            public Size size;
            public Rooms rooms;
            public Interior interior;
            public Construction construction;
            public Parking parking;
            public BuildingSummary summary;

            @JsonCreator
            public Building(@JsonProperty("size") Size size, @JsonProperty("rooms") Rooms rooms,
                            @JsonProperty("interior") Interior interior, @JsonProperty("construction") Construction construction,
                            @JsonProperty("parking") Parking parking, @JsonProperty("summary") BuildingSummary summary)
            {
                this.size = size;
                this.rooms = rooms;
                this.interior = interior;
                this.construction = construction;
                this.parking = parking;
                this.summary = summary;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Size
            {
                public long bldgSize;
                public long grossSize;
                public long grossSizeAdjusted;
                public long groundFloorSize;
                public long livingSize;
                public String sizeInd;
                public long universalSize;

                @JsonCreator
                public Size(@JsonProperty("bldgsize") long bldgSize, @JsonProperty("grosssize") long grossSize,
                            @JsonProperty("grosssizeadjusted") long grossSizeAdjusted, @JsonProperty("groundfloorsize") long groundFloorSize,
                            @JsonProperty("livingsize") long livingSize, @JsonProperty("sizeInd") String sizeInd,
                            @JsonProperty("universalsize") long universalSize)
                {
                    this.bldgSize = bldgSize;
                    this.grossSize = grossSize;
                    this.grossSizeAdjusted = grossSizeAdjusted;
                    this.groundFloorSize = groundFloorSize;
                    this.livingSize = livingSize;
                    this.sizeInd = sizeInd;
                    this.universalSize = universalSize;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Rooms
            {
                public long bathFixtures;
                public long baths1qtr;
                public long baths3qtr;
                public long bathsCalc;
                public long bathsFull;
                public long bathsHalf;
                public long bathsTotal;
                public long beds;
                public long roomsTotal;

                @JsonCreator
                public Rooms(@JsonProperty("bathfixtures") long bathFixtures, @JsonProperty("baths1qtr") long baths1qtr,
                             @JsonProperty("baths3qtr") long baths3qtr, @JsonProperty("bathscalc") long bathsCalc,
                             @JsonProperty("bathsfull") long bathsFull, @JsonProperty("bathshalf") long bathsHalf,
                             @JsonProperty("bathstotal") long bathsTotal, @JsonProperty("beds") long beds,
                             @JsonProperty("roomsTotal") long roomsTotal)
                {
                    this.bathFixtures = bathFixtures;
                    this.baths1qtr = baths1qtr;
                    this.baths3qtr = baths3qtr;
                    this.bathsCalc = bathsCalc;
                    this.bathsFull = bathsFull;
                    this.bathsHalf = bathsHalf;
                    this.bathsTotal = bathsTotal;
                    this.beds = beds;
                    this.roomsTotal = roomsTotal;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Interior
            {
                public long bsmtSize;
                public String bsmtType;
                public long fplcCount;
                public String floors;
                public String fplcInd;
                public String fplcType;

                @JsonCreator
                public Interior(@JsonProperty("bsmtsize") long bsmtSize, @JsonProperty("bsmttype") String bsmtType,
                                @JsonProperty("fplccount") long fplcCount, @JsonProperty("floors") String floors,
                                @JsonProperty("fplcind") String fplcInd, @JsonProperty("fplctype") String fplcType)
                {
                    this.bsmtSize = bsmtSize;
                    this.bsmtType = bsmtType;
                    this.fplcCount = fplcCount;
                    this.floors = floors;
                    this.fplcInd = fplcInd;
                    this.fplcType = fplcType;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Construction
            {
                public String foundationType;
                public String roofShape;
                public String roofCover;

                public String condition;
                public String wallType;

                public String constructionType;


                @JsonCreator
                public Construction(@JsonProperty("foundationtype") String foundationType, @JsonProperty("roofShape") String roofShape,
                                    @JsonProperty("roofcover") String roofCover, @JsonProperty("wallType") String wallType,
                                    @JsonProperty("condition") String condition, @JsonProperty("constructiontype") String constructionType)
                {
                    this.foundationType = foundationType;
                    this.roofShape = roofShape;
                    this.roofCover = roofCover;
                    this.condition = condition;
                    this.wallType = wallType;
                    this.constructionType = constructionType;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Parking
            {
                public String garageType;
                public long prkgSize;
                public String prkgSpaces;
                public String prkgType;

                @JsonCreator
                public Parking(@JsonProperty("garagetype") String garageType, @JsonProperty("prkgSize") long prkgSize,
                               @JsonProperty("prkgSpaces") String prkgSpaces, @JsonProperty("prkgType") String prkgType)
                {
                    this.garageType = garageType;
                    this.prkgSize = prkgSize;
                    this.prkgSpaces = prkgSpaces;
                    this.prkgType = prkgType;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class BuildingSummary
            {
                public String archStyle;
                public long bldgsNum;
                public String bldgType;
                public String imprType;
                public long levels;
                public String storyDesc;
                public String unitsCount;
                public long yearBuiltEffective;
                public String mobileHomeInd;
                public String quality;


                @JsonCreator
                public BuildingSummary(@JsonProperty("archStyle") String archStyle, @JsonProperty("bldgsNum") long bldgsNum,
                                       @JsonProperty("bldgType") String bldgType, @JsonProperty("imprType") String imprType, @JsonProperty("levels") long levels,
                                       @JsonProperty("storyDesc") String storyDesc, @JsonProperty("unitsCount") String unitsCount,
                                       @JsonProperty("yearbuilteffective") long yearBuiltEffective, @JsonProperty("mobileHomeInd") String mobileHomeInd,
                                       @JsonProperty("quality") String quality)
                {
                    this.archStyle = archStyle;
                    this.bldgsNum = bldgsNum;
                    this.bldgType = bldgType;
                    this.imprType = imprType;
                    this.levels = levels;
                    this.storyDesc = storyDesc;
                    this.unitsCount = unitsCount;
                    this.yearBuiltEffective = yearBuiltEffective;
                    this.mobileHomeInd = mobileHomeInd;
                    this.quality = quality;
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Vintage
        {
            public String lastModified;
            public String pubDate;

            @JsonCreator
            public Vintage(@JsonProperty("lastModified") String lastModified, @JsonProperty("pubDate") String pubDate)
            {
                this.lastModified = lastModified;
                this.pubDate = pubDate;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Avm
        {
            public Date eventDate;

            public Amount amount;
            public Calculations calculations;

            @JsonCreator
            public Avm(@JsonProperty("eventDate") Date eventDate, @JsonProperty("amount") Amount amount,
                       @JsonProperty("calculations") Calculations calculations)
            {
                this.eventDate = eventDate;
                this.amount = amount;
                this.calculations = calculations;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Amount
            {
                public long scr;
                public long value;
                public long high;
                public long low;
                public long valueRange;

                @JsonCreator
                public Amount(@JsonProperty("scr") long scr, @JsonProperty("value") long value, @JsonProperty("high") long high,
                              @JsonProperty("low") long low, @JsonProperty("valueRange") long valueRange)
                {
                    this.scr = scr;
                    this.value = value;
                    this.high = high;
                    this.low = low;
                    this.valueRange = valueRange;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Calculations
            {
                public long perSizeUnit;
                public long ratioTaxAmt;
                public long ratioTaxValue;
                public long monthlyChgPct;
                public long monthlyChgValue;
                public long rangePctOfValue;

                @JsonCreator
                public Calculations(@JsonProperty("perSizeUnit") long perSizeUnit, @JsonProperty("ratioTaxAmt") long ratioTaxAmt,
                                    @JsonProperty("ratioTaxValue") long ratioTaxValue, @JsonProperty("monthlyChgPct") long monthlyChgPct,
                                    @JsonProperty("monthlyChgValue") long monthlyChgValue, @JsonProperty("rangePctOfValue") long rangePctOfValue)
                {
                    this.perSizeUnit = perSizeUnit;
                    this.ratioTaxAmt = ratioTaxAmt;
                    this.ratioTaxValue = ratioTaxValue;
                    this.monthlyChgPct = monthlyChgPct;
                    this.monthlyChgValue = monthlyChgValue;
                    this.rangePctOfValue = rangePctOfValue;
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Sale
        {
            public String saleSearchDate;
            public String saleTransDate;
            public Amount amount;
            public Calculation calculation;
            public double ownershipTransferPercentage;
            public String resaleOrNewConstruction;
            public String cashOrMortgagePurchase;
            public String foreclosure;

            @JsonCreator
            public Sale(@JsonProperty("salesearchdate") String saleSearchDate, @JsonProperty("saleTransDate") String saleTransDate,
                        @JsonProperty("amount") Amount amount, @JsonProperty("calculation") Calculation calculation,
                        @JsonProperty("ownershiptransferpercentage") double ownership, @JsonProperty("foreclosure") String foreclosure,
                        @JsonProperty("resaleornewconstruction") String resaleOrNewConstruction,
                        @JsonProperty("cashormortgagepurchase") String cashOrMortgagePurchase)
            {
                this.saleSearchDate = saleSearchDate;
                this.saleTransDate = saleTransDate;
                this.amount = amount;
                this.calculation = calculation;
                this.ownershipTransferPercentage = ownership;
                this.foreclosure = foreclosure;
                this.resaleOrNewConstruction = resaleOrNewConstruction;
                this.cashOrMortgagePurchase = cashOrMortgagePurchase;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Amount
            {
                public long saleAmt;
                public String saleRecDate;
                public long saleDisclosureType;
                public String saleDocNum;
                public String saleTransType;

                @JsonCreator
                public Amount(@JsonProperty("saleamt") long saleAmt, @JsonProperty("salerecdate") String saleRecDate,
                              @JsonProperty("saledisclosuretype") long saleDisclosureType, @JsonProperty("saledocnum") String saleDocNum,
                              @JsonProperty("saletranstype") String saleTransType)
                {
                    this.saleAmt = saleAmt;
                    this.saleRecDate = saleRecDate;
                    this.saleDisclosureType = saleDisclosureType;
                    this.saleDocNum = saleDocNum;
                    this.saleTransType = saleTransType;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Calculation
            {
                public long pricePerBed;
                public long pricePerSizeUnit;

                @JsonCreator
                public Calculation(@JsonProperty("priceperbed") long pricePerBed, @JsonProperty("pricepersizeunit") long pricePerSizeUnit)
                {
                    this.pricePerBed = pricePerBed;
                    this.pricePerSizeUnit = pricePerSizeUnit;
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Assessment
        {
            public Appraised appraised;
            public Assessed assessed;
            public Calculations calculations;
            public Market market;
            public Tax tax;

            @JsonCreator
            public Assessment(@JsonProperty("appraised") Appraised appraised, @JsonProperty("assessed") Assessed assessed,
                              @JsonProperty("calculations") Calculations calculations, @JsonProperty("market") Market market, @JsonProperty("tax") Tax tax)
            {
                this.appraised = appraised;
                this.assessed = assessed;
                this.calculations = calculations;
                this.market = market;
                this.tax = tax;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Appraised
            {
                public long apprImprValue;
                public long apprLandValue;
                public long apprTtlValue;

                @JsonCreator
                public Appraised(@JsonProperty("apprimprvalue") long apprImprValue, @JsonProperty("apprlandvalue") long apprLandValue,
                                 @JsonProperty("apprttlvalue") long apprTtlValue)
                {
                    this.apprImprValue = apprImprValue;
                    this.apprLandValue = apprLandValue;
                    this.apprTtlValue = apprTtlValue;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Assessed
            {
                public double assdImprPerSizeUnit;
                public long assdImprValue;
                public double assdLandPerSizeUnit;
                public long assdLandValue;
                public double assdTtlPerSizeUnit;
                public long assdTtlValue;

                @JsonCreator
                public Assessed(@JsonProperty("assdimprpersizeunit") double assdImprPerSizeUnit, @JsonProperty("assdimprvalue") long assdImprValue,
                                @JsonProperty("assdlandpersizeunit") double assdLandPerSizeUnit, @JsonProperty("assdlandvalue") long assdLandValue,
                                @JsonProperty("assdttlpersizeunit") double assdTtlPerSizeUnit, @JsonProperty("assdttlvalue") long assdTtlValue)
                {
                    this.assdImprPerSizeUnit = assdImprPerSizeUnit;
                    this.assdImprValue = assdImprValue;
                    this.assdLandPerSizeUnit = assdLandPerSizeUnit;
                    this.assdLandValue = assdLandValue;
                    this.assdTtlPerSizeUnit = assdTtlPerSizeUnit;
                    this.assdTtlValue = assdTtlValue;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Calculations
            {
                public String calcImprInd;
                public double calcImprPerSizeUnit;
                public long calcImprValue;
                public String calcLandInd;
                public double calcLandPerSizeUnit;
                public long calcLandValue;
                public String calcTtlInd;
                public long calcTtlValue;
                public double calcValuePerSizeUnit;

                @JsonCreator
                public Calculations(@JsonProperty("calcimprind") String calcImprInd, @JsonProperty("calcimprpersizeunit") double calcImprPerSizeUnit,
                                    @JsonProperty("calcimprvalue") long calcImprValue, @JsonProperty("calclandind") String calcLandInd,
                                    @JsonProperty("calclandpersizeunit") double calcLandPerSizeUnit, @JsonProperty("calclandvalue") long calcLandValue,
                                    @JsonProperty("calcttlind") String calcTtlInd, @JsonProperty("calcttlvalue") long calcTtlValue,
                                    @JsonProperty("calcvaluepersizeunit") double calcValuePerSizeUnit)
                {
                    this.calcImprInd = calcImprInd;
                    this.calcImprPerSizeUnit = calcImprPerSizeUnit;
                    this.calcImprValue = calcImprValue;
                    this.calcLandInd = calcLandInd;
                    this.calcLandPerSizeUnit = calcLandPerSizeUnit;
                    this.calcLandValue = calcLandValue;
                    this.calcTtlInd = calcTtlInd;
                    this.calcTtlValue = calcTtlValue;
                    this.calcValuePerSizeUnit = calcValuePerSizeUnit;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Market
            {
                public long mktImprValue;
                public long mktLandValue;
                public long mktTtlValue;

                @JsonCreator
                public Market(@JsonProperty("mktimprvalue") long mktImprValue, @JsonProperty("mktlandvalue") long mktLandValue,
                              @JsonProperty("mktttlvalue") long mktTtlValue)
                {
                    this.mktImprValue = mktImprValue;
                    this.mktLandValue = mktLandValue;
                    this.mktTtlValue = mktTtlValue;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Tax
            {
                public double taxAmt;
                public double taxPerSizeUnit;
                public long taxYear;

                @JsonCreator
                public Tax(@JsonProperty("taxamt") double taxAmt, @JsonProperty("taxpersizeunit") double taxPerSizeUnit,
                           @JsonProperty("taxyear") long taxYear)
                {
                    this.taxAmt = taxAmt;
                    this.taxPerSizeUnit = taxPerSizeUnit;
                    this.taxYear = taxYear;
                }
            }
        }
    }
}
