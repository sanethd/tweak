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

package org.wso2.qaa.selenium.tweek;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.wso2.qaa.selenium.tweek.util.CommonConstants;
import org.wso2.qaa.selenium.tweek.util.FileHandler;
import org.wso2.qaa.selenium.tweek.util.PropertyFileHandler;
import org.wso2.qaa.selenium.tweek.util.TweakReporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * Main class of Tweaker application.
 * This application with automate the parametrization process of
 * recorded selenium scripts.
 * Script should be  generated through Wso2TestNG selenium formatter.
 */
public class Tweaker {
    private static final Log log = LogFactory.getLog(Tweaker.class);
    PropertyFileHandler propertyFileHandler;
    public String currentWindow = CommonConstants.DEFAULT_WINDOW_NAME;
    TweakReporter tweakReporter;
    private Map<String, String> duplicatePropertyKeys;

    public Tweaker(String propertyFile) throws IOException {
        propertyFileHandler = new PropertyFileHandler(propertyFile);
        tweakReporter = new TweakReporter();
        duplicatePropertyKeys = new HashMap<String, String>();
    }

    /**
     * @param args [0] = Java class file location to parametrise.  [1]= Property file location.l
     */
    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure(Tweaker.class.getResource("/log4j.properties").getPath());

        String fileName = args[0];
        String propertyFileName = args[1];
        if (args[0] == null || args[1] == null) {
            log.error("Please test class file path or property file path cannot be null");
            log.error("java -jar  PATH_TO_TEST_CLASS PATH_TO_PROPERTY_FILE");
            throw new IllegalArgumentException("Please test class file path or property file path cannot be null. java -jar  PATH_TO_TEST_CLASS PATH_TO_PROPERTY_FILE ");
        }

        Tweaker tweaker = new Tweaker(propertyFileName);
        FileHandler fileHandler = new FileHandler();
        fileHandler.copyFile(fileName, fileName + "." + CommonConstants.BACK);
        String p1Filename = tweaker.tweakTestCaseFilePhaseOne(fileName, propertyFileName);
        tweaker.tweakTestCaseFilePhaseTwo(p1Filename);
        tweaker.tweakReporter.logReport();
    }

    /**
     * Analyses the lines one by one and identify the lines that need to be tweak and call tweak method.
     *
     * @param testClassPath    Absolute path to the Test case Java class.
     * @param propertyFilePath Absolute path to the the property file.
     * @return String Absolute path to the file created after phase 1 tweak.
     * @throws IOException
     */
    public String tweakTestCaseFilePhaseOne(String testClassPath, String propertyFilePath)
            throws IOException {
        log.info("Tweak test case phase one : Start");
        String tweakFileName = testClassPath.replace(".java", "TweakP1.java");
        FileInputStream fis = new FileInputStream(testClassPath);
        Scanner scanner = new Scanner(fis, "UTF-8");
        File fileOut = new File(tweakFileName);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8"));
        // read file line by line.
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(CommonConstants.WINDOW)) {
                currentWindow = line.split(":")[1];
            }
            if (needTweak(line)) {
                String tweetLine = tweakLine(line);
                writer.println(tweetLine);
            } else {
                writer.println(line);
            }
        }
        propertyFileHandler.savePropertyFile(propertyFilePath);
        scanner.close();
        writer.close();
        log.info("Tweak test case phase One : Finish");
        return tweakFileName;
    }

    /**
     * Tweak the given line. Parameterise the ui elements ,  add them to  given property file.
     *
     * @param line Line that need to tweak
     * @return String after tweak
     * @throws FileNotFoundException
     */
    public String tweakLine(String line) throws FileNotFoundException {
        String identifierType;
        String identifier;

        log.info("Original line :" + line);
        identifierType = line.substring(line.indexOf("By.") + 3, line.indexOf("(\""));
        identifier = line.substring(line.indexOf("(\"") + 2, line.indexOf("\")"));
        String propertyKey = generatePropertyKey(currentWindow, identifier, identifierType);
        if (!propertyFileHandler.isKeyAvailable(propertyKey)) {
            propertyFileHandler.addProperty(propertyKey, identifier);
            log.info("New property added Key:" + propertyKey + " Value:" + identifier);
        } else if (propertyFileHandler.getValue(propertyKey).equals(identifier)) {
            log.info("Property already available Key:" + propertyKey + " Value:" + identifier);
        } else {
            if (!duplicatePropertyKeys.containsKey(propertyKey)) {
                log.info("Property already available, But value is different :" + propertyKey + " New Value:" + identifier + ", Available Value :" +
                         propertyFileHandler.getValue(propertyKey));
                String oldPropertyKey = propertyKey;
                UUID uuid = UUID.randomUUID();
                propertyKey = propertyKey + uuid.toString().replaceAll("-", "");
                propertyFileHandler.addProperty(propertyKey, identifier);
                tweakReporter.addDuplicateProperty(oldPropertyKey, propertyFileHandler.getValue(oldPropertyKey), propertyKey, identifier);
                duplicatePropertyKeys.put(oldPropertyKey, propertyKey);
            } else {
                propertyKey = duplicatePropertyKeys.get(propertyKey);
            }
        }
        String tweetLine = line.replace("\"" + identifier + "\"", "UIElementMapper.getElement(\"" + propertyKey + "\")") + " //  modified by QAA Tweek ";
        log.info("New Line :" + tweetLine);
        return tweetLine;
    }

    /**
     * Braking into test methods according to  the comments given in the recorded script.
     *
     * @param fullFilPath Path to the file  that need to run tweak phase 2.
     * @throws IOException
     */
    public void tweakTestCaseFilePhaseTwo(String fullFilPath) throws IOException {
        log.info("Tweak test case phase Two : Start");
        boolean testMethodStart = false;
        int methodLineCount = 1;
        String currentMethodName;
        String currentLine;
        String prvLine = "";
        String tweakFileName = fullFilPath.replace("TweakP1", "TweakP2");
        FileInputStream fis = new FileInputStream(fullFilPath);
        Scanner scanner = new Scanner(fis, "UTF-8");
        File fileOut = new File(tweakFileName);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            currentLine = line;
            String[] tempTestClassFileArray = fullFilPath.replace(".java", "").replaceAll("TweakP1", "").split(String.valueOf(File.separatorChar));
            String testClassname = tempTestClassFileArray[tempTestClassFileArray.length - 1];
            if (line.contains("test" + testClassname)) {
                testMethodStart = true;
            }
            if (line.contains("@AfterClass")) {
                testMethodStart = false;
            }
            if (testMethodStart) {
                if (methodLineCount == 2) {
                    if (!line.contains("METHOD")) {
                        throw new RuntimeException("Method tag not found  as the first line in the recorded test method");
                    }
                }
                if (methodLineCount > 1) {
                    if (line.contains("METHOD")) {
                        String[] tempArray = line.split(":");
                        if (tempArray[2].equals("start")) {
                            currentMethodName = tempArray[1];
                            if (!prvLine.contains("test" + testClassname)) {
                                writer.println("    @Test(groups = \"\", description = \"\")");
                            }
                            writer.println("    public void " + currentMethodName + " throws Exception {");
                            writer.println(line);
                        } else if (tempArray[2].equals("end")) {
                            writer.println(line);
                            writer.println("    }");
                        } else {
                            throw new RuntimeException("Illegal method status. Mathod status can only be start or stop. METHOD:methodName:start or METHOD:methodName:stop");
                        }
                    } else {
                        if (!(prvLine.contains(":end") && currentLine.contains("}"))) {
                            writer.println(line);
                        }
                    }
                }
                methodLineCount++;
            } else {
                writer.println(line);
            }
            prvLine = currentLine;
        }
        scanner.close();
        writer.close();
        log.info("Tweak test case phase Two : Finish");
    }

    /**
     * Generate the  property key for the given Ui element.
     *
     * @param windowName     current window name
     * @param identifier     identifier of the ui element.
     * @param identifierType type of the identifier.
     * @return String generated property key.
     */
    private String generatePropertyKey(String windowName, String identifier,
                                       String identifierType) {
        return windowName.toLowerCase() + CommonConstants.PERIOD + identifier.toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + CommonConstants.PERIOD + identifierType.toLowerCase();

    }

    /**
     * check the given line is need to tweak.
     *
     * @param line String to check.
     * @return boolean true if line is need to tweak.
     */
    private boolean needTweak(String line) {
        boolean result = false;
        Pattern findElement = Pattern.compile(".*By\\..*");
        if (findElement.matcher(line).matches()) {
            result = true;
        }
        return result;
    }

}

