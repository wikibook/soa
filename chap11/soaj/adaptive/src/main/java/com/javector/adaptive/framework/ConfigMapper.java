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

import com.javector.adaptive.framework.model.ConfigurationDetailDTO;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.interfaces.*;
import com.javector.adaptive.framework.util.BaseXPathEvaluator;

import com.javector.ser.adaptive.handler.HandlerMappings;
import com.javector.ser.adaptive.handler.SoajAdaptiveConfiguration;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.StringUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: rohit agarwal
 *
 * Date: Apr 8, 2006
 * Time: 10:56:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigMapper {

    private static SoajLogger logger = LoggingFactory.getLogger(ConfigMapper.class.getName());


    public static Configurator parseConfigurationDetail(String relativeFilePath) {
        SoajAdaptiveConfiguration  handlerConfigurations;
        InputStream systemResourceAsStream = SOAUtil.loadResource(relativeFilePath); 
        try {
            JAXBContext jaxBcontext =  JAXBContext.newInstance(AdaptiveConstants.HANLDER_PACKAGE);
            handlerConfigurations = (SoajAdaptiveConfiguration)jaxBcontext.createUnmarshaller().unmarshal(systemResourceAsStream);
        } catch (JAXBException e) {
            throw new SoajRuntimeException("unable to create jaxb context ",e);
        }
        HandlerMappings mappings = handlerConfigurations.getHandlerMappings();
        return generateConfigurator(mappings);
    }

    private static Configurator generateConfigurator(HandlerMappings mappings) {
        ConfigurationDetailDTO detailDTO = new ConfigurationDetailDTO();
        populateContextHandlers(mappings,detailDTO);
        populateSerializerHandlers(mappings,detailDTO);
        populateDeserializerHandlers(mappings,detailDTO);
        populateUtilHandlers(mappings,detailDTO);
        return new Configurator(detailDTO);
    }

    private static void populateUtilHandlers(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        populateUtilHandlerForReflectionUtil(mappings, detailDTO);
        populateUtilHandlerForXpathUtil(mappings,detailDTO);
    }

    private static void populateUtilHandlerForXpathUtil(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String xpathHandler  = mappings.getUtilHandlers().getOverrideXpathEvaluatorHandler();
        if(StringUtil.stringNotEmpty(xpathHandler)&& SOAUtil.validateHandler(xpathHandler.trim(), BaseXPathEvaluator.class)) {
            logger.info("no override mapping is provided and will be used");
            detailDTO.setXpathImplClassName(xpathHandler.trim());
        } else {
        throw new SoajRuntimeException("unable to initialize default xpath util Please check the configuration file");
        }
    }

    private static void populateUtilHandlerForReflectionUtil(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String refUtil = mappings.getUtilHandlers().getOverrideReflectionHandler();
        if(StringUtil.stringNotEmpty(refUtil) && SOAUtil.validateHandler(refUtil.trim(), BaseReflectionUtil.class)) {
            logger.info("no override mapping is provided and will be used");
            detailDTO.setReflectionUtilClassName(refUtil.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default reflection util Please check the configuration file");
        }
    }

    private static void populateDeserializerHandlers(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        populateBaseDeserializerHandler(mappings,detailDTO);
        populateSimpleDeserializerHandler(mappings,detailDTO);
        populateComplexDeserializerHandler(mappings,detailDTO);
    }

    private static void populateComplexDeserializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String deserializerMapping = mappings.getDeserializerHandlers().getOverrideComplexTypeDeserializerHandler();
        if(StringUtil.stringNotEmpty(deserializerMapping)&& SOAUtil.validateHandler(deserializerMapping.trim(), BaseAdaptiveDeserializer.class)) {
            logger.info(" override mapping is provided and will be used"+deserializerMapping);
            detailDTO.setComplexTypeDeserializer(deserializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default deserializer mapping Please check the configuration file"+deserializerMapping);
        }
    }

    private static void populateSimpleDeserializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String deserializerMapping = mappings.getDeserializerHandlers().getOverrideSimpleTypeDeserializerHandler();
        if(StringUtil.stringNotEmpty(deserializerMapping)&& SOAUtil.validateHandler(deserializerMapping.trim(), BaseAdaptiveDeserializer.class)) {
            logger.info(" override mapping is provided and will be used"+deserializerMapping);
            detailDTO.setSimpleTypeDeserializer(deserializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default deserializer mapping Please check the configuration file"+deserializerMapping);
        }
    }

    private static void populateBaseDeserializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String deserializerMapping = mappings.getDeserializerHandlers().getOverrideBaseDeserializerHandler();
        if(StringUtil.stringNotEmpty(deserializerMapping)&& SOAUtil.validateHandler(deserializerMapping.trim(),BaseAdaptiveDeserializer.class)) {
            logger.info(" override mapping is provided and will be used"+deserializerMapping);
            detailDTO.setBaseAdaptiveDeserializer(deserializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default deserializer mapping Please check the configuration file"+deserializerMapping);
        }
    }

    private static void populateSerializerHandlers(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        populateBaseSerializerHandler(mappings,detailDTO);
        populateSimpleSerializerHandler(mappings,detailDTO);
        populateComplexSerializerHandler(mappings,detailDTO);
    }

    private static void populateComplexSerializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String serializerMapping = mappings.getSerializerHandlers().getOverrideComplexTypeSerializerHandler();
        if(StringUtil.stringNotEmpty(serializerMapping) && SOAUtil.validateHandler(serializerMapping.trim(),BaseAdaptiveSerializer.class)) {
            logger.info(" override mapping is provided and will be used"+serializerMapping);
            detailDTO.setComplexTypeSerializer(serializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default serializer mapping Please check the configuration file"+serializerMapping);
        }

    }

    private static void populateSimpleSerializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String serializerMapping = mappings.getSerializerHandlers().getOverrideSimpleTypeSerializerHandler();
        if(StringUtil.stringNotEmpty(serializerMapping) && SOAUtil.validateHandler(serializerMapping.trim(), BaseAdaptiveSerializer.class)) {
            logger.info(" override mapping is provided and will be used"+serializerMapping);
            detailDTO.setSimpleTypeSerializer(serializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default serializer mapping Please check the configuration file"+serializerMapping);
        }
    }

    private static void populateBaseSerializerHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String serializerMapping = mappings.getSerializerHandlers().getOverrideBaseAdaptiveSerializerHandler();
        if(StringUtil.stringNotEmpty(serializerMapping) && SOAUtil.validateHandler(serializerMapping.trim(),BaseAdaptiveSerializer.class)) {
            logger.info(" override mapping is provided and will be used"+serializerMapping);
            detailDTO.setBaseAdaptiveSerializer(serializerMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default serializer mapping Please check the configuration file"+serializerMapping);
        }
    }

    private static void populateContextHandlers(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        populateContextHandler(mappings,detailDTO);
    }

    private static void populateContextHandler(HandlerMappings mappings, ConfigurationDetailDTO detailDTO) {
        String contextMapping = mappings.getAdaptiveContextHandler().getOverrideAdaptiveContextHandler();
        if(StringUtil.stringNotEmpty(contextMapping) && SOAUtil.validateHandler(contextMapping.trim(),AdaptiveContext.class)) {
           logger.info(" override mapping is provided and will be used"+contextMapping);
            detailDTO.setConfigurationContextClassName(contextMapping.trim());

        } else {

        throw new SoajRuntimeException("unable to initialize default serializer mapping Please check the configuration file"+contextMapping);
        }
    }

}
