package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.model.Integration;

import java.util.HashMap;
import java.util.Map;

public class PractiTestTestCaseIntegration implements Integration
{
    private String testId;

    public void setTestId(String testId)
    {
        this.testId = testId;
    }

    @Override
    public String getName()
    {
        return PractiTestConstants.TEST_CASE_INTEGRATION;
    }

    @Override
    public Map<String, String> getProperties()
    {
        HashMap<String, String> props = new HashMap<>();
        props.put(PractiTestConstants.TEST_CASE_INTEGRATION_ID, testId);
        return props;
    }
}
