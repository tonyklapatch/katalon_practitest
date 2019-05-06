package net.klapatch.katalon.plugin.practitest;

import com.katalon.platform.api.extension.TestSuiteIntegrationViewDescription;
import com.katalon.platform.api.model.Integration;

import com.katalon.platform.api.model.TestSuiteEntity;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.Map;

public class PractiTestTestSuiteIntegrationView implements TestSuiteIntegrationViewDescription.TestSuiteIntegrationView {
    private Composite container;

    private Text txtTestSuiteId;

    private Boolean isEdited = false;

    public Control onCreateView(Composite parent, TestSuiteIntegrationViewDescription.PartActionService partActionService, TestSuiteEntity testSuite) {
        container = new Composite(parent, SWT.NONE);

        Label lblTestSuiteId = new Label(container, SWT.NONE);
        lblTestSuiteId.setText("Test Suite ID");
        GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
        gridData.widthHint = 100;
        lblTestSuiteId.setLayoutData(gridData);

        txtTestSuiteId = new Text(container, SWT.BORDER);
        GridData gridData1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txtTestSuiteId.setLayoutData(gridData1);

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 15;
        container.setLayout(gridLayout);

        Integration integration = testSuite.getIntegration(PractiTestConstants.TEST_SUITE_INTEGRATION);
        if (integration != null) {
            Map<String, String> integrationProps = integration.getProperties();
            if (integrationProps.containsKey(PractiTestConstants.TEST_SUITE_INTEGRATION_ID)){
                txtTestSuiteId.setText(integrationProps.get(PractiTestConstants.TEST_SUITE_INTEGRATION_ID));
            }
        }

        txtTestSuiteId.addModifyListener(modifyEvent -> {
            isEdited = true;
            partActionService.markDirty();
        });

        return container;
    }

    @Override
    public Integration getIntegrationBeforeSaving()
    {
        PractiTestTestSuiteIntegration integration = new PractiTestTestSuiteIntegration();
        integration.setTestSuiteId(txtTestSuiteId.getText());
        isEdited = false;
        return integration;
    }

    @Override
    public boolean needsSaving() {
        return isEdited;
    }
}