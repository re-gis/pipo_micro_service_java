package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AreaFullPropertyResponse
{
    public Response response;

    @JsonCreator
    public AreaFullPropertyResponse(@JsonProperty("response") Response response)
    {
        this.response = response;
    }

    public AreaFullPropertyResponse()
    {}

    public static class Response
    {
        public InputParameter inputparameter;
        public Result result;
        public Status status;

        @JsonCreator
        public Response(@JsonProperty("inputparameter") InputParameter ipar,
                      @JsonProperty("result") Result result,
                      @JsonProperty("status") Status status,
                      @JsonProperty("xmlns") String xmlns)
        {
            this.inputparameter = ipar;
            this.result = result;
            this.status = status;
        }
    }

    public static class InputParameter
    {
        public String areaid, pkg, resource, service;

        @JsonCreator
        public InputParameter(@JsonProperty("AreaId") String area, @JsonProperty("package") String pkg,
                @JsonProperty("resource") String resource, @JsonProperty("service") String service)
        {
            this.areaid = area;
            this.pkg = pkg;
            this.resource = resource;
            this.service = service;
        }
    }

    public static class Result
    {
        public ResultPackage pkg;

        @JsonCreator
        public Result(@JsonProperty("package") ResultPackage pkg, @JsonProperty("xml_record") String xmlrecord)
        {
            this.pkg = pkg;
        }
    }

    public static class ResultPackage
    {
        public ResultItem[] items;
        public String descr, name, notice, resource, service, version;

        @JsonCreator
        public ResultPackage(@JsonProperty("item") ResultItem[] items,
                @JsonProperty("descr") String descr, @JsonProperty("name") String name,
                @JsonProperty("notice") String notice, @JsonProperty("resource") String resource,
                @JsonProperty("service") String service, @JsonProperty("version") String version)
        {
            this.items = items;
            this.descr = descr;
            this.name = name;
            this.notice = notice;
            this.resource = resource;
            this.service = service;
            this.version = version;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultItem
    {
        public String age00_04;
        public String age05_09;
        public String age10_14;
        public String age15_19;
        public String age20_24;
        public String age25_29;
        public String age30_34;
        public String age35_39;
        public String age40_44;
        public String age45_49;
        public String age50_54;
        public String age55_59;
        public String age60_64;
        public String age65_69;
        public String age70_74;
        public String age75_79;
        public String age80_84;
        public String ageavepy_10;
        public String ageavepy_5;
        public String agegt85;
        public String aimcy25_44;
        public String aimcy45_64;
        public String aimcygt_65;
        public String aimcylt_25;
        public String airport;
        public String airportdist;
        public String airx;
        public String ancestamer;
        public String ancestamind;
        public String ancestasian;
        public String ancesteuro;
        public String ancesthawai;
        public String ancesthisp;
        public String ancestother;
        public String ancestunclassified;
        public String avg_prop_tax;
        public String avg_prop_tax_year;
        public String avg_prop_tax_year_count;
        public String avgsaleprice;
        public String carbmono;
        public String city500_closest_name;
        public String cocrmcyasst;
        public String cocrmcyburg;
        public String cocrmcylarc;
        public String cocrmcymurd;
        public String cocrmcymveh;
        public String cocrmcyperc;
        public String cocrmcyproc;
        public String cocrmcyrape;
        public String cocrmcyrobb;
        public String cocrmcytotc;
        public String county3;
        public String county5;
        public String countyname;
        public String crmcyasst;
        public String crmcyburg;
        public String crmcylarc;
        public String crmcymurd;
        public String crmcymveh;
        public String crmcyperc;
        public String crmcyproc;
        public String crmcyrape;
        public String crmcyrobb;
        public String crmcytotc;
        public String daypop;
        public String dwlowned;
        public String dwlrent;
        public String dwltotal;
        public String dwlvacnt;
        public String eduagelt9_00;
        public String eduagelt9_90;
        public String eduassoc;
        public String eduassoc_00;
        public String edubach;
        public String edubach_00;
        public String educoll_00;
        public String educoll_90;
        public String edudegree_00;
        public String edugrad;
        public String eduhsch;
        public String eduhsch_00;
        public String eduhsch_90;
        public String eduindex;
        public String edultgr9;
        public String eduscoll;
        public String eduscoll_00;
        public String eduscoll_90;
        public String edushsch;
        public String edutotalpop;
        public String edutotalpop_00;
        public String edutotalpop_90;
        public String empadmin;
        public String empagric;
        public String emparts;
        public String empconst;
        public String empedu;
        public String empexec;
        public String empfire;
        public String empfood;
        public String emphcsp;
        public String emphome;
        public String empinfo;
        public String empmanuf;
        public String empmil;
        public String empmil_00;
        public String empmine;
        public String empprof;
        public String emppubad;
        public String empre;
        public String emprtrad;
        public String empsrv;
        public String emptotal;
        public String emptrans;
        public String empuncla;
        public String empunemp;
        public String emputl;
        public String empwtrad;
        public String enrollcoll_00;
        public String enrollcoll_90;
        public String enrollelem_00;
        public String enrollelem_90;
        public String enrollhsch_00;
        public String enrollprek_00;
        public String enrollprek_90;
        public String enrolltotal_00;
        public String enrolltotal_90;
        public String expappar;
        public String expcontrib;
        public String expeduc;
        public String expent;
        public String expfoodbev;
        public String expgift;
        public String exphealth;
        public String exphh;
        public String exphhfurn;
        public String exphhops;
        public String expinsur;
        public String expmisc;
        public String exppers;
        public String expread;
        public String exptob;
        public String exptotal;
        public String exptransport;
        public String exputil;
        public String famavesz;
        public String four_bed_county;
        public String four_bedindex;
        public String fouryr;
        public String fouryrdist;
        public String geo_code;
        public String geo_key;
        public String geo_type;
        public String hhd;
        public String hhd_00;
        public String hhd_90;
        public String hhdavesz;
        public String hhdavesz_00;
        public String hhdch;
        public String hhdfam;
        public String hhdhighpy_5;
        public String hhdlowpy_5;
        public String hhdnch;
        public String hhdnfm;
        public String hhdpy_10;
        public String hhdpy_5;
        public String hhdsingle;
        public String hhnwcy1_5;
        public String hhnwcy10_25;
        public String hhnwcy100_250;
        public String hhnwcy25_50;
        public String hhnwcy250_500;
        public String hhnwcy5_10;
        public String hhnwcy50_100;
        public String hhnwcygt_500;
        public String hhnwcylt_0;
        public String hhnwmeancy;
        public String hhnwmedcy;
        public String hhpop_00;
        public String hhpoppy_10;
        public String hhpoppy_5;
        public String hhppy_10;
        public String hhppy_5;
        public String hincy00_10;
        public String hincy10_15;
        public String hincy100_125;
        public String hincy125_150;
        public String hincy15_20;
        public String hincy150_200;
        public String hincy20_25;
        public String hincy200_250;
        public String hincy25_30;
        public String hincy250_500;
        public String hincy30_35;
        public String hincy35_40;
        public String hincy40_45;
        public String hincy45_50;
        public String hincy50_60;
        public String hincy60_75;
        public String hincy75_100;
        public String hincygt_500;
        public String houmedage;
        public String idxexptotal;
        public String incavehhpy_5;
        public String inccyavehh;
        public String inccymedd;
        public String inccymeddh;
        public String inccypcap;
        public String incdiffbasecy;
        public String incdiffcurrcy;
        public String incmedpy_5;
        public String jc;
        public String jcdist;
        public String langasian;
        public String langasian_00;
        public String langeng;
        public String langeng_00;
        public String langeuro;
        public String langeuro_00;
        public String langother;
        public String langother_00;
        public String langspan;
        public String langspan_00;
        public String langtotal;
        public String latitude;
        public String lbf_00;
        public String lbfaf_00;
        public String lbfciv_00;
        public String lbfemp_00;
        public String lbfwf_00;
        public String lead;
        public String longitude;
        public String lorturn;
        public String mardivor;
        public String market_id;
        public String market_name;
        public String marmarr;
        public String marnever;
        public String marsep;
        public String martotalpop;
        public String marwidow;
        public String medianage;
        public String medianagepy_10;
        public String medianagepy_5;
        public String medsaleprice;
        public String name;
        public String nine_to_12_1990;
        public String nine_to_12_2000;
        public String no2;
        public String ob_id;
        public String occarts;
        public String occbfin;
        public String occbgmt;
        public String occbluco;
        public String occclero;
        public String occcomp;
        public String occcons;
        public String occcsrv;
        public String occeduc;
        public String occengr;
        public String occfood;
        public String occhcso;
        public String occhcsp;
        public String occinstal;
        public String occlegal;
        public String occmilt;
        public String occpcar;
        public String occprim;
        public String occprod;
        public String occprot;
        public String occsalpr;
        public String occscns;
        public String occtotpop;
        public String occtran;
        public String occuncl;
        public String occwhtco;
        public String occyexec;
        public String one_bed_county;
        public String one_bedindex;
        public String ozone;
        public String pm10;
        public String pop_00;
        public String pop_10;
        public String pop_90;
        public String popcorr_00;
        public String popcy;
        public String popcygqcoll;
        public String popcygqmil;
        public String popdiffbasecy;
        public String popdiffcurrcy;
        public String popdnsty;
        public String popfemale;
        public String popfemale_00;
        public String popfemalepy_10;
        public String popfemalepy_5;
        public String popgq_00;
        public String popgq_90;
        public String popgqpy_10;
        public String popgqpy_5;
        public String pophighpy_5;
        public String popinst_00;
        public String poplowpy_5;
        public String popmale;
        public String popmale_00;
        public String popmalepy_10;
        public String popmalepy_5;
        public String popnoninst_00;
        public String popnurshm_00;
        public String popotherinst_00;
        public String popothernoninst_00;
        public String poppy_10;
        public String poppy_5;
        public String precipann;
        public String raceamerind;
        public String raceamerind_00;
        public String raceasian;
        public String raceasian_00;
        public String raceasian_90;
        public String raceasianpy_5;
        public String raceblack;
        public String raceblack_00;
        public String raceblack_90;
        public String raceblackpy_5;
        public String racehawai;
        public String racehawai_00;
        public String racehisp;
        public String racehisp_90;
        public String racehisppy_5;
        public String racemulti;
        public String racemulti_00;
        public String racenonhisp;
        public String raceother;
        public String raceother_00;
        public String raceother_90;
        public String raceotherpy_5;
        public String racetotalpop;
        public String racewhite;
        public String racewhite_00;
        public String racewhite_90;
        public String racewhitepy_5;
        public String rskcyhanx;
        public String rskcyhunx;
        public String rskcyquak;
        public String rskcyrisk;
        public String rskcytonx;
        public String rskcywinx;
        public String salavecy;
        public String salecount;
        public String salestaxrate;
        public String salestaxtype;
        public String salmedcy;
        public String seasonpop;
        public String state;
        public String statename;
        public String studio_county;
        public String studioindex;
        public String team;
        public String teamdist;
        public String three_bed_county;
        public String three_bedindex;
        public String tmpavejan;
        public String tmpavejul;
        public String tmpmaxjan;
        public String tmpmaxjul;
        public String tmpminjan;
        public String tmpminjul;
        public String trw0_5;
        public String trw10_15;
        public String trw15_20;
        public String trw20_30;
        public String trw30_45;
        public String trw45_60;
        public String trw5_10;
        public String trwave;
        public String trwbyc;
        public String trwcpool;
        public String trwdrive;
        public String trwgt_60;
        public String trwhome;
        public String trwmoto;
        public String trwother;
        public String trwpublic;
        public String trwself;
        public String two_bed_county;
        public String two_bedindex;
        public String vph1;
        public String vphgt1;
        public String vphnone;
    }

    public static class Status
    {
        public String code, longdescription, shortdescription;

        @JsonCreator
        public Status(@JsonProperty("code") String code,
                @JsonProperty("long_description") String longd, @JsonProperty("short_description") String shortd)
        {
            this.code = code;
            this.longdescription = longd;
            this.shortdescription = shortd;
        }
    }
}
