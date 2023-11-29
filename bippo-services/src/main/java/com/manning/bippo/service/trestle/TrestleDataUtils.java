package com.manning.bippo.service.trestle;

public class TrestleDataUtils {

    public static String mapTrestleStatusToNtreis(String propertyType, String trestleStatus) {
        if (trestleStatus == null) {
            return null;
        }

        /*
         * Active
         * Active Under Contract, Active Option Contract
         * Coming Soon, Pending
         * Contingent, Active Contingent
         * Canceled, Cancelled
         * Delete, Expired
         * Expired
         * Hold, Pending
         * Incomplete, Pending
         * Pending
         * Closed, Sold
         * Withdrawn
         */

        switch (trestleStatus) {
            case "ActiveUnderContract":
            case "Active Under Contract":
                return "Active Option Contract";
            case "ComingSoon":
            case "Coming Soon":
                return "Pending";
            case "Contingent":
                return "Active Contingent";
            case "Canceled": // Note the spelling difference between Trestle and NTREIS
                return "Cancelled";
            case "Delete":
                return "Expired";
            case "Hold":
                return "Pending";
            case "Incomplete":
                return "Pending";
            case "Closed":
                return "ResidentialLease".equals(propertyType) ? "Leased" : "Sold";
            default:
                return trestleStatus;
        }
    }

    public static String mapNtreisStatusToTrestle(String ntreisStatus) {
        if (ntreisStatus == null) {
            return null;
        }

        switch (ntreisStatus) {
            case "Active Contingent":
                return "?";
            case "Cancelled":
                return "Canceled";
            case "Active Kick Out":
                return "?";
            case "Incoming":
                return "?";
            case "Leased":
                return "Closed";
            case "Sold":
                return "Closed";
            case "Temp Off Market":
                return "?";
            case "Active Option Contract":
                return "ActiveUnderContract";
            case "Withdrawn Sublisting":
                return "Withdrawn";
            default:
                return ntreisStatus;
        }
    }

    public static String mapNtreisSubTypeToTrestle(String ntreisSubType) {
        if (ntreisSubType == null) {
            return null;
        }

        switch (ntreisSubType) {
            case "RES-Condo":
                return "Condominium";
            case "RES-Farm/Ranch":
                return "Ranch";
            case "RES-Half Duplex":
                return "Duplex";
            case "RES-Single Family":
                return "SingleFamilyResidence";
            case "RES-Townhouse":
                return "Townhouse";
            default:
                return ntreisSubType;
        }
    }

    public static boolean acceptNullForNtreisSubType(String ntreisSubType) {
        return "RES-Single Family".equals(ntreisSubType);
    }
}
