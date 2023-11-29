package com.manning.bippo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Adjustment implements Serializable
{
    double listPrice = 0d;
    double soldPrice = 0d;

    double salesFinancing;

    double dom = 0d;

    double location = 0d;

    double leaseholdFeeSimple = 0d;

    double site = 0d;

    double view = 0d;

    double design = 0d;
    double quality = 0d;
    double condition = 0d;

    double rooms = 0d;
    double basement = 0d;
    double functionalUtility = 0d;
    double heatingCooling = 0d;
    double garageCarport = 0d;
    double ppdf = 0d;
    double other = 0d;
    double yearBuiltEffective = 0d;

    double gla = 0d;

    double verificationSource = 0d;
    double proposedFinancing = 0d;
    double energyEfficient = 0d;
    double fences = 0d;
    double pool= 0d;

    public double getListPrice()
    {
        return listPrice;
    }

    public void setListPrice(double listPrice)
    {
        this.listPrice = listPrice;
    }

    public double getSoldPrice()
    {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice)
    {
        this.soldPrice = soldPrice;
    }

    public double getSalesFinancing()
    {
        return salesFinancing;
    }

    public void setSalesFinancing(double salesFinancing)
    {
        this.salesFinancing = salesFinancing;
    }

    public double getDom()
    {
        return dom;
    }

    public void setDom(double dom)
    {
        this.dom = dom;
    }

    public double getLocation()
    {
        return location;
    }

    public void setLocation(double location)
    {
        this.location = location;
    }

    public double getLeaseholdFeeSimple()
    {
        return leaseholdFeeSimple;
    }

    public void setLeaseholdFeeSimple(double leaseholdFeeSimple)
    {
        this.leaseholdFeeSimple = leaseholdFeeSimple;
    }

    public double getSite()
    {
        return site;
    }

    public void setSite(double site)
    {
        this.site = site;
    }

    public double getView()
    {
        return view;
    }

    public void setView(double view)
    {
        this.view = view;
    }

    public double getDesign()
    {
        return design;
    }

    public void setDesign(double design)
    {
        this.design = design;
    }

    public double getQuality()
    {
        return quality;
    }

    public void setQuality(double quality)
    {
        this.quality = quality;
    }

    public double getCondition()
    {
        return condition;
    }

    public void setCondition(double condition)
    {
        this.condition = condition;
    }

    public double getRooms()
    {
        return rooms;
    }

    public void setRooms(double rooms)
    {
        this.rooms = rooms;
    }

    public double getBasement()
    {
        return basement;
    }

    public void setBasement(double basement)
    {
        this.basement = basement;
    }

    public double getFunctionalUtility()
    {
        return functionalUtility;
    }

    public void setFunctionalUtility(double functionalUtility)
    {
        this.functionalUtility = functionalUtility;
    }

    public double getHeatingCooling()
    {
        return heatingCooling;
    }

    public void setHeatingCooling(double heatingCooling)
    {
        this.heatingCooling = heatingCooling;
    }

    public double getGarageCarport()
    {
        return garageCarport;
    }

    public void setGarageCarport(double garageCarport)
    {
        this.garageCarport = garageCarport;
    }

    public double getPpdf()
    {
        return ppdf;
    }

    public void setPpdf(double ppdf)
    {
        this.ppdf = ppdf;
    }

    public double getOther()
    {
        return other;
    }

    public void setOther(double other)
    {
        this.other = other;
    }

    public double getYearBuiltEffective()
    {
        return yearBuiltEffective;
    }

    public void setYearBuiltEffective(double yearBuiltEffective)
    {
        this.yearBuiltEffective = yearBuiltEffective;
    }

    public double getGla()
    {
        return gla;
    }

    public void setGla(double gla)
    {
        this.gla = gla;
    }

    public double getVerificationSource()
    {
        return verificationSource;
    }

    public void setVerificationSource(double verificationSource)
    {
        this.verificationSource = verificationSource;
    }

    public double getProposedFinancing()
    {
        return proposedFinancing;
    }

    public void setProposedFinancing(double proposedFinancing)
    {
        this.proposedFinancing = proposedFinancing;
    }

    public double getEnergyEfficient()
    {
        return energyEfficient;
    }

    public void setEnergyEfficient(double energyEfficient)
    {
        this.energyEfficient = energyEfficient;
    }

    public double getFences()
    {
        return fences;
    }

    public void setFences(double fences)
    {
        this.fences = fences;
    }

    public double getPool()
    {
        return pool;
    }

    public void setPool(double pool)
    {
        this.pool = pool;
    }
}
