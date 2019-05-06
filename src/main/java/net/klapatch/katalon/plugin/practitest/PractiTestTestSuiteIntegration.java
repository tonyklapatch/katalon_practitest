package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.model.Integration;

import java.util.HashMap;
import java.util.Map;

public class PractiTestTestSuiteIntegration implements Integration
{
    private String testSuiteId;

    public void setTestSuiteId(String testSuiteId)
    {
        this.testSuiteId = testSuiteId;
    }

    @Override
    public String getName()
    {
        return PractiTestConstants.TEST_SUITE_INTEGRATION;
    }

    @Override
    public Map<String, String> getProperties()
    {
        HashMap<String, String> props = new HashMap<>();
        props.put(PractiTestConstants.TEST_SUITE_INTEGRATION_ID, testSuiteId);
        return props;
    }
}
