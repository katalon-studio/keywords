package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.io.File
import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.util.internal.TestOpsUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.exception.BrowserNotOpenedException
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil

@Action(value = "takeScreenshot")
public class TakeScreenshotKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object... params) {
        if (!isValidData(params)) {
            return null
        }

        String fileName = (String)params[0]
        boolean isTestOpsVisionCheckPoint = (boolean)params[1]
        if (!isTestOpsVisionCheckPoint && fileName == null) {
            fileName = defaultFileName()
        }
        FailureHandling flowControl = params[2] == null ?
                RunConfiguration.getDefaultFailureHandling() : (FailureHandling)params[2]
        return takeScreenshot(fileName, isTestOpsVisionCheckPoint, flowControl)
    }

    private boolean isValidData(Object... params) {
        if (params.length != 3) {
            return false
        }

        if (params[0] != null && !(params[0] instanceof String)) {
            return false;
        }

        if (params[1] != null && !(params[1] instanceof Boolean)) {
            return false;
        }

        if (params[2] != null && !(params[2] instanceof FailureHandling)) {
            return false;
        }

        return true;
    }

    private String defaultFileName() {
        return logger.getLogFolderPath() + File.separator + System.currentTimeMillis() + ".png";
    }

    @CompileStatic
    public String takeScreenshot(String fileName, boolean isTestOpsVisionCheckPoint, FailureHandling flowControl) {
        return WebUIKeywordMain.runKeyword({
            String screenFileName = FileUtil.takesScreenshot(fileName, isTestOpsVisionCheckPoint)
            if (screenFileName != null) {
                String fileNameForLog = TestOpsUtil.getRelativePathForLog(screenFileName)
                Map<String, String> attributes = new HashMap<>()
                attributes.put(StringConstants.XML_LOG_ATTACHMENT_PROPERTY, fileNameForLog)
                logger.logPassed("Taking screenshot successfully", attributes)
            }
            return screenFileName;
        }, flowControl, false, StringConstants.KW_LOG_WARNING_CANNOT_TAKE_SCREENSHOT)
    }
}
