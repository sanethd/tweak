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
import org.wso2.qaa.selenium.tweek.deta.DuplicateProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that generate Reports in tweak application. this class has the variables to store necessary  information to report generation.
 */
public class TweakReporter {
    private static final Log log = LogFactory.getLog(TweakReporter.class);
    private List<DuplicateProperty> duplicateProperties = new ArrayList<DuplicateProperty>();

    /**
     * add duplicate property information.
     *
     * @param oldKey   Original key of the attribute.
     * @param oldValue available value of the original key in property file.
     * @param newKey   newly generated key due to difference in the vales.
     * @param newValue new vale of the key.
     */
    public void addDuplicateProperty(String oldKey, String oldValue, String newKey,
                                     String newValue) {
        duplicateProperties.add(new DuplicateProperty(oldKey, oldValue, newKey, newValue));

    }

    /**
     * Generate tke Tweak report
     */
    public void logReport() {
        log.info("\t\t********************Tweak Report********************");

        if (duplicateProperties.size() > 0) {


            log.warn("\t\t*** Property  Conflicts ***");

            log.warn("Same property key was found in the property file but with different value.");
            log.warn("Unique key was attach at the end of the property key to save new vale.");
            log.warn("Manual attention is highly recommended in following properties.");

            for (DuplicateProperty duplicateProperty : duplicateProperties) {
                log.warn("Old Property :" + duplicateProperty.getOldKey() + "=" + duplicateProperty.getOldValue() + "\tNew Key: " + duplicateProperty.getNewKey() + "=" + duplicateProperty.getNewValue());

            }
            log.warn("\t\t*** Property  Conflicts: END  ***");
        }

        log.info("\t\t****************************************************");


    }


}
