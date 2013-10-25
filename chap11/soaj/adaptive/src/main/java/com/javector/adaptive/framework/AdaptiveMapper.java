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
package com.javector.adaptive.framework;
import com.javector.adaptive.framework.model.*;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.ser.adaptive.*;
import com.javector.soaj.SoajRuntimeException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.*;
import java.util.logging.Logger;
import java.io.InputStream;

public class AdaptiveMapper {
     public static final Logger logger = Logger.getLogger(AdaptiveMapper.class.getName());

    public static AdaptiveMapDTO parseAdaptiveXML(String xmlPath) {
          if (xmlPath == null) {
              throw new SoajRuntimeException("xmlPath is null");
          }
          AdaptiveMap adaptiveMap;
          try {
              JAXBContext jc = JAXBContext.newInstance("com.javector.ser.adaptive");
              Unmarshaller u = jc.createUnmarshaller();
              InputStream systemResourceAsStream = SOAUtil.loadResource(xmlPath);
              adaptiveMap = (AdaptiveMap) u.unmarshal(systemResourceAsStream);
          } catch (Exception e) {
              e.printStackTrace();
              throw new SoajRuntimeException("Exception while parsing mapping file " + xmlPath, e);
          }
          AdaptiveMapDTO adaptiveMapDTO = new AdaptiveMapDTO();

          assert adaptiveMap != null;
          adaptiveMapDTO.setStrategyDTOs(generateStrategyDTOs(adaptiveMap.getStrategy()));

          return adaptiveMapDTO;
      }

 

    private static StrategyDTO[] generateStrategyDTOs(List<Strategy> strategies) {
        StrategyDTO[] strategyDTOs = null;
        if (!strategies.isEmpty()) {
            strategyDTOs = new StrategyDTO[strategies.size()];
            for (int i = 0; i < strategies.size(); i++) {
                strategyDTOs[i] = generateStrategyDTO(strategies.get(i));
            }
        }
        return strategyDTOs;
    }

      private static StrategyDTO generateStrategyDTO(Strategy strategy) {
          StrategyDTO strategyDTO = new StrategyDTO();
          strategyDTO.setJavaClass(strategy.getJavaClass());
          strategyDTO.setXmlType(strategy.getXmlType());
          strategyDTO.setXmlName(strategy.getXmlName());
          strategyDTO.setRuleDTOs(generateRuleDTOs(strategy.getRule()));
          List<Property> property = strategy.getProperty();
          for (Property currentProperty : property) {
              strategyDTO.getPropertyMappings().put(currentProperty.getName(), currentProperty.getValue());
          }

          return strategyDTO;

      }


      private static RuleDTO[] generateRuleDTOs(List<Rule> rules) {
          RuleDTO[] ruleDTOs = null;
          if (!rules.isEmpty()) {
              ruleDTOs = new RuleDTO[rules.size()];
              for (int i = 0; i < rules.size(); i++) {
                  Rule rule1 = rules.get(i);
                  ruleDTOs[i] = generateRuleDTO(rule1);
              }
          }
          return ruleDTOs;
      }


      private static RuleDTO generateRuleDTO(Rule rule) {
          RuleDTO ruleDTO = new RuleDTO();
          ruleDTO.setJavaName(rule.getJavaName());
          ruleDTO.setJavaClass(rule.getJavaClass());
          ruleDTO.setXmlName(rule.getXmlName());
          ruleDTO.setXmlType(rule.getXmlType());
          if(rule.getWrap()!=null) {
          ruleDTO.setWrapDTO(generateWrapDTO(rule.getWrap()));
          }
          if(AdaptiveConstants.FIELD.equalsIgnoreCase(rule.getJavaType())){
              ruleDTO.setField(true);
              ruleDTO.setMethod(false);
          }else if(AdaptiveConstants.METHOD.equalsIgnoreCase(rule.getJavaType())){
              ruleDTO.setField(false);
             ruleDTO.setMethod(true);
          }

          if (AdaptiveConstants.ELEMENT.equalsIgnoreCase(rule.getXmlNodeType())) {
              ruleDTO.setElement(true);
              ruleDTO.setAttribute(false);
          } else  if (AdaptiveConstants.ATTRIBUTE.equalsIgnoreCase(rule.getXmlNodeType())) {
              ruleDTO.setAttribute(true);
              ruleDTO.setElement(false);
          }

          if (AdaptiveConstants.ALL.equals(rule.getRestrictTo())) {
              ruleDTO.setSerialization(true);
              ruleDTO.setDeserialization(true);
          }else if (AdaptiveConstants.SERIALIZATION.equals(rule.getRestrictTo())) {
              ruleDTO.setSerialization(true);
              ruleDTO.setDeserialization(false);
          }else if (AdaptiveConstants.DESERIALIZATION.equals(rule.getRestrictTo())) {
              ruleDTO.setSerialization(false);
              ruleDTO.setDeserialization(true);
          }

          if (rule.getScript() != null) {
              ScriptDTO scriptDTO = new ScriptDTO();
              scriptDTO.setScriptData(rule.getScript());
          }

          if (rule.getScript() != null) {
              ScriptDTO scriptDTO = new ScriptDTO();
              scriptDTO.setScriptData(rule.getScript());
              if (AdaptiveConstants.DESERIALIZATION.equalsIgnoreCase(rule.getRestrictTo())) {
                  scriptDTO.setGroovy(true);
                  scriptDTO.setXSLT(false);
              } else {
                  scriptDTO.setGroovy(false);
                  scriptDTO.setXSLT(true);
              }

              ruleDTO.setScriptDTO(scriptDTO);
          }
          List<Property> property = rule.getProperty();
          Map<String, String> propertyMappings = ruleDTO.getPropertyMappings();
          for (Property currentProperty : property) {
              propertyMappings.put(currentProperty.getName(), currentProperty.getValue());
          }

          return ruleDTO;
      }

    private static WrapDTO generateWrapDTO(Wrap wrap) {
        WrapDTO wrapDTO = new WrapDTO();
        JavaWrapDTO javaWrapDTO = new JavaWrapDTO();
        XmlWrapDTO xmlWrapDTO = new XmlWrapDTO();
        javaWrapDTO.setJavaName(wrap.getJavaWrap().getName());
        javaWrapDTO.setJavaClass(wrap.getJavaWrap().getType());
        xmlWrapDTO.setXmlName(wrap.getXmlWrap().getName());
        xmlWrapDTO.setXmlType(wrap.getXmlWrap().getType());
        wrapDTO.setJavaWrapDTO(javaWrapDTO);
        wrapDTO.setXmlWrapDTO(xmlWrapDTO);
        return wrapDTO;
    }

}
