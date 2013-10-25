/*
* Copyright 2006-2007 Javector Software LLC
*
* Licensed under the GNU General Public License, Version 2 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.gnu.org/copyleft/gpl.html
*
* THE SOURCE CODE AND ACCOMPANYING FILES ARE PROVIDED WITHOUT ANY WARRANTY,
* WRITTEN OR IMPLIED.
*
* The copyright holder provides this software under other licenses for those
* wishing to include it with products or systems not licensed under the GPL.
* Contact licenses@javector.com for more information.
*/
package com.javector.adaptive.framework.util;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.framework.XmlNameMapping;
import com.javector.adaptive.framework.interfaces.XMLMapping;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class SOAContextUtil {

    public static List<StrategyDTO> getSerializersForMapping(AdaptiveMapDTO adaptiveMapDTO, XMLMapping typeMapping) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        // TODO type checking should also check inclusion in type hierarchies
        // not just simple type equality
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (strategyDTO.getXmlType().equals(typeMapping.getXmlType())
                && strategyDTO.getJavaClass().equals(typeMapping.getJavaClassName())) {
                arrayList.add(strategyDTO);
            }
            if(arrayList.size()==0 &&strategyDTO.getXmlName()!=null) {
                if (strategyDTO.getXmlName().equals(typeMapping.getXmlName())
                    && strategyDTO.getJavaClass().equals(typeMapping.getJavaClassName())) {
                    arrayList.add(strategyDTO);
                }
            }
        }
        return arrayList;
    }
    
    public static List<StrategyDTO> getSerializersForXmlName(AdaptiveMapDTO adaptiveMapDTO, QName qName) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (qName.equals(strategyDTO.getXmlName())) {
                arrayList.add(strategyDTO);
            }
        }
        return arrayList;
    }
    public static List<StrategyDTO> getDeserializersForXmlName(AdaptiveMapDTO adaptiveMapDTO, QName qName) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (qName.equals(strategyDTO.getXmlName())) {
                arrayList.add(strategyDTO);
            }
        }
        return arrayList;
    }



    public static List<StrategyDTO> getSerializersForXmlType(AdaptiveMapDTO adaptiveMapDTO, QName qName) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (qName.equals(strategyDTO.getXmlType())) {
                arrayList.add(strategyDTO);
            }
        }
        return arrayList;
    }
    public static List<StrategyDTO> getSerializersForJavaClass(AdaptiveMapDTO adaptiveMapDTO, QName qName) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (strategyDTO.getXmlName().equals(qName)) {
                arrayList.add(strategyDTO);
            }
        }
        return arrayList;
    }

    public static List<StrategyDTO> getDeserializersForTypeMapping(AdaptiveMapDTO adaptiveMapDTO, XMLMapping typeMapping) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (strategyDTO.getXmlType().equals(typeMapping.getXmlType()) &&
                    strategyDTO.getJavaClass().equals(typeMapping.getJavaClassName())) {
                arrayList.add(strategyDTO);
            }
            if(arrayList.size()==0 && strategyDTO.getXmlName()!=null) {
                if (strategyDTO.getXmlName().equals(typeMapping.getXmlName()) &&
                        strategyDTO.getJavaClass().equals(typeMapping.getJavaClassName())) {
                    arrayList.add(strategyDTO);
                }

            }
        }
        return arrayList;
    }
    public static List<StrategyDTO> getDeserializersForNameMapping(AdaptiveMapDTO adaptiveMapDTO, XmlNameMapping nameMapping) {
        StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
        ArrayList<StrategyDTO> arrayList = new ArrayList<StrategyDTO>();
        for (StrategyDTO strategyDTO : strategyDTOs) {
            if (strategyDTO.getJavaClass().equalsIgnoreCase(nameMapping.getJavaClassName()) &&
                    strategyDTO.getXmlName().equals(nameMapping.getXmlName())) {
                arrayList.add(strategyDTO);
            }
        }
        return arrayList;
    }

}
