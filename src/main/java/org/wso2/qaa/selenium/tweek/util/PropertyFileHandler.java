/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.qaa.selenium.tweek.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * class the Handle the property file.
 */
public class PropertyFileHandler {
    private static final Log log = LogFactory.getLog(PropertyFileHandler.class);

    private static Properties uiProperties = null;

    /**
     * Load the property file if available ble inb the given location. If not create a new property file wi th ethe given name.
     *
     * @param fullFilePath Path of the property file.
     * @throws IOException
     */
    public PropertyFileHandler(String fullFilePath) throws IOException {
        File file = new File(fullFilePath);
        if (!file.exists()) {
            file.createNewFile();
            log.info("Property file created :" + fullFilePath);
        } else {
            log.info("Property already available at :" + fullFilePath);
        }
        if (uiProperties == null) {
            uiProperties = new Properties();
            InputStream inputStream = new FileInputStream(fullFilePath);
            try {
                if (inputStream.available() > 0) {
                    uiProperties.load(inputStream);
                }
            } catch (IOException ioE) {
                log.error(ioE);
                throw new ExceptionInInitializerError("Mapper stream not set. Failed to read file");
            } finally {
                try {
                    inputStream.close();
                } catch (Exception ioE) {
                    log.error(ioE);//TODO chack
                    throw new ExceptionInInitializerError("Mapper stream not closed correctly. Failed to close the stream");
                }
            }
        }

    }

    /**
     * CHeck the availability  of the key in the property file.
     *
     * @param key key that need to check the availability.
     * @return true if given key is available in the property file.
     */
    public boolean isKeyAvailable(String key) {
        return uiProperties.containsKey(key);
    }

    /**
     * Get the value of the given key.
     *
     * @param key key the nee to retrieve the value.
     * @return String, value of the given string.
     */
    public String getValue(String key) {
        return uiProperties.getProperty(key);
    }

    /**
     * Add a property to the property file.
     *
     * @param key   key of the property
     * @param value value of the property
     */
    public void addProperty(String key, String value) {
        uiProperties.put(key, value);
    }

    /**
     * Save the property file in the given location.
     *
     * @param fullFilePath path to store the property file.
     * @throws IOException
     */
    public void savePropertyFile(String fullFilePath) throws IOException {
        OutputStream outputStream = new FileOutputStream(fullFilePath);
        uiProperties.store(outputStream, null);
        outputStream.close();
    }


}

