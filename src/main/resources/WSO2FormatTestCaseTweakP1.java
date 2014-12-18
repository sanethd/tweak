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

import org.openqa.selenium.*;
import org.openqa.selenium.Alert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.core.BrowserManager;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;
import static org.testng.Assert.fail;

//import org.wso2.carbon.automation.extensions.selenium.BrowserManager;

public class WSO2FormatTestCase {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        driver = BrowserManager.getWebDriver();
        baseUrl = "https://10.100.5.191:9443/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


    }


    @Test(groups = "", description = "")
    public void testWSO2FormatTestCase() throws Exception {
        //METHOD:testLoginTestCase:start
        //WINDOW:apimng.login
        driver.get(baseUrl + "/publisher/design");
        driver.findElement(By.id(UIElementMapper.getElement("apimng.login.username.id"))).clear(); //  modified by QAA Tweek 
        driver.findElement(By.id(UIElementMapper.getElement("apimng.login.username.id"))).sendKeys("admin"); //  modified by QAA Tweek 
        driver.findElement(By.id(UIElementMapper.getElement("apimng.login.pass.id"))).clear(); //  modified by QAA Tweek 
        driver.findElement(By.id(UIElementMapper.getElement("apimng.login.pass.id"))).sendKeys("admin"); //  modified by QAA Tweek 
        driver.findElement(By.id(UIElementMapper.getElement("apimng.login.loginbutton.id"))).click(); //  modified by QAA Tweek 
        //METHOD:testLoginTestCase:end
        //METHOD:testMainWindowTestCase:start
        //WINDOW:apimng.main
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (isElementPresent(By.linkText(UIElementMapper.getElement("apimng.main.allstatistics.linktext")))) { //  modified by QAA Tweek 
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.allstatistics.linktext"))).click(); //  modified by QAA Tweek 
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (isElementPresent(By.linkText(UIElementMapper.getElement("apimng.main.apiresponsetimes.linktext")))) { //  modified by QAA Tweek 
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apiresponsetimes.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apiusagebyresourcepath.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apiusagebyuser.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.browse.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.subscriptions.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.statistics.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apisubscriptions.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apiusage.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apiresponsetimes.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.apilastaccesstimes.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.browse.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.browse.linktext"))).click(); //  modified by QAA Tweek 
        driver.findElement(By.linkText(UIElementMapper.getElement("apimng.main.add.linktext"))).click(); //  modified by QAA Tweek 
        //METHOD:testMainWindowTestCase:end
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

}
