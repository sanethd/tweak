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

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

/**
 * class to handle the files.
 */
public class FileHandler {
    private static final Log log = LogFactory.getLog(FileHandler.class);

    /**
     * Copy a file from one location to another location.
     *
     * @param from original file that need to copy.
     * @param to   new file location.
     * @throws IOException
     */
    public void copyFile(String from, String to) throws IOException {
        File file1 = new File(from);
        File file2 = new File(to);
        log.info("Copying " + file1.getName() + " to " + file2.getName());
        FileUtils.copyFile(file1, file2);


    }
}
