package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.extension.ToolItemDescription;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.DialogActionService;

public class PractiTestToolItemDescription implements ToolItemDescription {

    @Override
    public String name() {
        return "PractiTest";
    }

    @Override
    public String toolItemId() {
        return PractiTestConstants.PLUGIN_ID + ".practiTestToolItem";
    }

    @Override
    public String iconUrl() {
        return "platform:/plugin/" + PractiTestConstants.PLUGIN_ID + "/icons/practitest.png";
    }

    @Override
    public void handleEvent() {
        ApplicationManager.getInstance().getUIServiceManager().getService(DialogActionService.class).openPluginPreferencePage(
                PractiTestConstants.PREF_PAGE_ID);
    }

    @Override
    public boolean isItemEnabled() {
        return true;
    }
}