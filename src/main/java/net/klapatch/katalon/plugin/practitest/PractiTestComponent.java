package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;

public interface PractiTestComponent
{
    default PluginPreference getPluginStore() throws ResourceException {
        PluginPreference pluginStore = ApplicationManager.getInstance().getPreferenceManager().getPluginPreference(
                ApplicationManager.getInstance().getProjectManager().getCurrentProject().getId(),
                PractiTestConstants.PLUGIN_ID);

        return pluginStore;
    }
}