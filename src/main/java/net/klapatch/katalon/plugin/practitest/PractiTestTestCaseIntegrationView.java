package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.extension.TestCaseIntegrationViewDescription;
import com.katalon.platform.api.model.Integration;
import com.katalon.platform.api.model.TestCaseEntity;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.UISynchronizeService;

import java.awt.*;
import java.util.Map;

public class PractiTestTestCaseIntegrationView implements TestCaseIntegrationViewDescription.TestCaseIntegrationView {
    private Composite container;

    private Text txtTestId;

    private Boolean isEdited = false;

    public Control onCreateView(Composite parent, TestCaseIntegrationViewDescription.PartActionService partActionService, TestCaseEntity testCase) {
        container = new Composite(parent, SWT.NONE);

        Label lblTestId = new Label(container, SWT.NONE);
        lblTestId.setText("Test ID");
        GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
        gridData.widthHint = 100;
        lblTestId.setLayoutData(gridData);

        txtTestId = new Text(container, SWT.BORDER);
        GridData gridData1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txtTestId.setLayoutData(gridData1);

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 15;
        container.setLayout(gridLayout);



        Integration integration = testCase.getIntegration(PractiTestConstants.INTEGRATION_ID);
        if (integration != null) {
            Map<String, String> integrationProps = integration.getProperties();
            if (integrationProps.containsKey(PractiTestConstants.INTEGRATION_TEST_ID)){
                txtTestId.setText(integrationProps.get(PractiTestConstants.INTEGRATION_TEST_ID));
            }
        }

        txtTestId.addModifyListener(modifyEvent -> {
            isEdited = true;
            partActionService.markDirty();
        });

        return container;
    }

    @Override
    public Integration getIntegrationBeforeSaving()
    {
        PractiTestTestCaseIntegration integration = new PractiTestTestCaseIntegration();
        integration.setTestId(txtTestId.getText());
        isEdited = false;
        return integration;
    }

    @Override
    public boolean needsSaving() {
        return isEdited;
    }
}