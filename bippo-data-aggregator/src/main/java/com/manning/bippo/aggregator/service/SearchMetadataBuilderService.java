package com.manning.bippo.aggregator.service;

import com.manning.bippo.dao.*;
import com.manning.bippo.dao.pojo.*;
import com.manning.bippo.service.rets.RetsService;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchMetadataBuilderService
{

    @Autowired
    RetsService retsService;

    @Autowired
    MLSStatusRepository mlsStatusRepository;

    @Autowired
    MLSCityRepository mlsCityRepository;

    @Autowired
    MLSCountyRepository mlsCountyRepository;

    @Autowired
    MLSPropertySubTypeRepository mlsPropertySubTypeRepository;

    @Autowired
    MLSSchoolDistrictRepository mlsSchoolDistrictRepository;

    @Autowired
    MLSPropertyTypeRepository mlsPropertyTypeRepository;


    public void buildMetadataService() throws RetsException
    {
        buildMLSLookupMap("Property", "Status").forEach(mlsStatus -> {
            MLSStatus byValue = mlsStatusRepository.findByValue(mlsStatus.getValue());
            if (byValue == null)
            {
                mlsStatusRepository.save(mlsStatus);
            }
        });

        buildCityLookupMap("Property", "City").forEach(mlsCity -> {
            City byValue = mlsCityRepository.findByValue(mlsCity.getValue());
            if (byValue == null)
            {
                mlsCityRepository.save(mlsCity);
            }
        });

        buildCountyLookupMap("Property", "CountyOrParish").forEach(mlsCounty -> {
            County county = mlsCountyRepository.findByValue(mlsCounty.getValue());
            if (county == null && !mlsCounty.getValue().equals("NTREIS TEST ONLY"))
            {
                mlsCountyRepository.save(mlsCounty);
            }
        });

        buildPropertySubTypeLookupMap("Property", "PropertySubType").forEach(mlsPropertySubType -> {
            MLSPropertySubType proeprtySubType = mlsPropertySubTypeRepository.findByValue(mlsPropertySubType.getValue());
            if (proeprtySubType == null)
            {
                mlsPropertySubTypeRepository.save(mlsPropertySubType);
            }
        });

        buildSchoolDistrictLookupMap("Property", "SchoolDistrict").forEach(mlsSchoolDistrict -> {
            MLSSchoolDistrict schoolDistrict = mlsSchoolDistrictRepository.findByValue(mlsSchoolDistrict.getValue());
            if(schoolDistrict == null)
            {
                mlsSchoolDistrictRepository.save(mlsSchoolDistrict);
            }
        });

        buildPropertyTypeLookupMap("Property", "PropertyType").forEach(mlsPropertyTye -> {
            MLSPropertyType propertyType= mlsPropertyTypeRepository.findByValue(mlsPropertyTye.getValue());
            if(propertyType == null)
            {
                mlsPropertyTypeRepository.save(mlsPropertyTye);
            }
        });
    }

    private List<MLSPropertyType> buildPropertyTypeLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsPropertyTypeLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<MLSPropertyType> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsPropertyTypeLookupTypes)
        {
            MLSPropertyType mlsPropertyType = new MLSPropertyType();
            mlsPropertyType.setValue(mLookupType.getLongValue());
            mlsPropertyType.setRetsShortValue(mLookupType.getShortValue());
            dataLookup.add(mlsPropertyType);
        }
        return dataLookup;
    }

    private List<MLSSchoolDistrict> buildSchoolDistrictLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsStatusLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<MLSSchoolDistrict> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsStatusLookupTypes)
        {
            MLSSchoolDistrict mlsSchoolDistrict = new MLSSchoolDistrict();
            mlsSchoolDistrict.setValue(mLookupType.getLongValue());
            mlsSchoolDistrict.setRetsShortValue(mLookupType.getShortValue());
            dataLookup.add(mlsSchoolDistrict);
        }
        return dataLookup;
    }


    private List<MLSPropertySubType> buildPropertySubTypeLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsStatusLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<MLSPropertySubType> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsStatusLookupTypes)
        {
            MLSPropertySubType mlsPropertySubType = new MLSPropertySubType();
            mlsPropertySubType.setValue(mLookupType.getLongValue());
            mlsPropertySubType.setRetsShortValue(mLookupType.getValue());
            dataLookup.add(mlsPropertySubType);
        }
        return dataLookup;
    }


    private List<County> buildCountyLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsStatusLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<County> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsStatusLookupTypes)
        {
            County mlsCounty = new County();
            mlsCounty.setValue(mLookupType.getLongValue());
            mlsCounty.setRetsShortValue(mLookupType.getValue());
            dataLookup.add(mlsCounty);
        }
        return dataLookup;
    }

    private List<City> buildCityLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsStatusLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<City> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsStatusLookupTypes)
        {
            City mlsCity = new City();
            mlsCity.setValue(mLookupType.getLongValue());
            mlsCity.setRetsShortValue(mLookupType.getValue());
            dataLookup.add(mlsCity);
        }
        return dataLookup;
    }

    private List<MLSStatus> buildMLSLookupMap(String resourceId, String lookupName) throws RetsException
    {
        Metadata retsMetadata = retsService.getMetadata();
        MLookupType[] mlsStatusLookupTypes = retsMetadata.getLookup(resourceId, lookupName).getMLookupTypes();

        List<MLSStatus> dataLookup = new ArrayList<>();
        for (MLookupType mLookupType : mlsStatusLookupTypes)
        {
            MLSStatus mlsStatus = new MLSStatus();
            mlsStatus.setValue(mLookupType.getLongValue());
            mlsStatus.setRetsShortValue(mLookupType.getValue());
            dataLookup.add(mlsStatus);
        }
        return dataLookup;
    }
}
