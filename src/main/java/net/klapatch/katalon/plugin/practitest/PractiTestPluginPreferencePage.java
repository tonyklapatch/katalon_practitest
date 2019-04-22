package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.extension.PluginPreferencePage;
import org.eclipse.jface.preference.PreferencePage;

public class PractiTestPluginPreferencePage implements PluginPreferencePage {

    @Override
    public String getName()
    {
        return "PractiTest";
    }

    @Override
    public String getPageId()
    {
        return PractiTestConstants.PREF_PAGE_ID;
    }

    @Override
    public Class<? extends PreferencePage> getPreferencePageClass()
    {
        return PractiTestPreferencePage.class;
    }
}
