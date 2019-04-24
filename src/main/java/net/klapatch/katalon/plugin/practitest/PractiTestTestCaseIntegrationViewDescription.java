package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.extension.TestCaseIntegrationViewDescription;
import com.katalon.platform.api.model.ProjectEntity;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;

public class PractiTestTestCaseIntegrationViewDescription implements TestCaseIntegrationViewDescription {

    public String getName() {
        return "PractiTest";
    }

    @Override
    public Class<? extends TestCaseIntegrationView> getTestCaseIntegrationView() {
        return PractiTestTestCaseIntegrationView.class;
    }

    @Override
    public boolean isEnabled(ProjectEntity projectEntity)
    {
        try {
            PluginPreference pluginPreference = ApplicationManager.getInstance()
                    .getPreferenceManager()
                    .getPluginPreference(projectEntity.getId(), PractiTestConstants.PLUGIN_ID);

            if (pluginPreference == null) {
                return false;
            }

            return pluginPreference.getBoolean(PractiTestConstants.PREF_IS_PRACTITEST_ENABLED, false);
        } catch (ResourceException e) {
            return false;
        }
    }
}
