package net.klapatch.katalon.plugin.practitest;

import org.apache.commons.lang3.StringUtils;
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

import java.net.HttpURLConnection;
import java.net.URL;

public class PractiTestPreferencePage extends PreferencePage implements PractiTestComponent {
    private Button chckEnableIntegration;

    private Group grpAuthentication;

    private Text txtProject;

    private Text txtToken;

    private Text txtEmail;

    private Composite container;

    private Button btnTestConnection;

    private Label lblConnectionStatus;

    private Thread thread;

    @Override
    protected Control createContents(Composite composite) {
        container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        chckEnableIntegration = new Button(container, SWT.CHECK);
        chckEnableIntegration.setText("Using PractiTest");

        grpAuthentication = new Group(container, SWT.NONE);
        grpAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout glAuthentication = new GridLayout(2, false);
        glAuthentication.horizontalSpacing = 15;
        glAuthentication.verticalSpacing = 10;
        grpAuthentication.setLayout(glAuthentication);
        grpAuthentication.setText("Authentication");

        Label lblEmail = new Label(grpAuthentication, SWT.NONE);
        lblEmail.setText("Email");
        GridData gdLabel = new GridData(SWT.LEFT, SWT.TOP, false, false);
        lblEmail.setLayoutData(gdLabel);

        txtEmail = new Text(grpAuthentication, SWT.BORDER);
        GridData gdTxtEmail = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdTxtEmail.widthHint = 200;
        txtEmail.setLayoutData(gdTxtEmail);

        Label lblToken = new Label(grpAuthentication, SWT.NONE);
        lblToken.setText("API Token");
        lblToken.setLayoutData(gdLabel);

        txtToken = new Text(grpAuthentication, SWT.BORDER);
        GridData gdTxtToken = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdTxtToken.widthHint = 200;
        txtToken.setLayoutData(gdTxtToken);

        Label lblChannel = new Label(grpAuthentication, SWT.NONE);
        lblChannel.setText("Project ID");
        lblToken.setLayoutData(gdLabel);

        txtProject = new Text(grpAuthentication, SWT.BORDER);
        txtProject.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        btnTestConnection = new Button(grpAuthentication, SWT.PUSH);
        btnTestConnection.setText("Test Connection");
        btnTestConnection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                testConnection(txtToken.getText(), txtEmail.getText());
            }
        });

        lblConnectionStatus = new Label(grpAuthentication, SWT.NONE);
        lblConnectionStatus.setText("");
        lblConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

        handleControlModifyEventListeners();
        initializeInput();

        return container;
    }

    private void testConnection(String token, String email) {
        btnTestConnection.setEnabled(false);
        lblConnectionStatus.setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        lblConnectionStatus.setText("Connecting...");
        thread = new Thread(() -> {
            try {
                // https://www.practitest.com/api-v2/#authentication
                URL url = new URL("https://api.practitest.com/api/v2/projects.json?developer_email=" + email + "&api_token=" + token);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.connect();

                int code = con.getResponseCode();
                if (code == 200) {
                    syncExec(() -> {
                        lblConnectionStatus
                                .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
                        lblConnectionStatus.setText("Connection success");
                    });
                } else {
                    syncExec(() -> {
                        lblConnectionStatus
                                .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
                        lblConnectionStatus
                                .setText("Connection failed. Reason: Invalid credentials or bad response.");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                syncExec(() -> {
                    lblConnectionStatus
                            .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
                    lblConnectionStatus
                            .setText("Connection failed. Reason: " + StringUtils.defaultString(e.getMessage()));
                });
            } finally {
                syncExec(() -> btnTestConnection.setEnabled(true));
            }
        });
        thread.start();
    }

    void syncExec(Runnable runnable) {
        if (lblConnectionStatus != null && !lblConnectionStatus.isDisposed()) {
            ApplicationManager.getInstance()
                    .getUIServiceManager()
                    .getService(UISynchronizeService.class)
                    .syncExec(runnable);
        }
    }

    private void handleControlModifyEventListeners() {
        chckEnableIntegration.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                recursiveSetEnabled(grpAuthentication, chckEnableIntegration.getSelection());
            }
        });
    }

    public static void recursiveSetEnabled(Control ctrl, boolean enabled) {
        if (ctrl instanceof Composite) {
            Composite comp = (Composite) ctrl;
            for (Control c : comp.getChildren()) {
                recursiveSetEnabled(c, enabled);
                c.setEnabled(enabled);
            }
        } else {
            ctrl.setEnabled(enabled);
        }
    }

    @Override
    public boolean performOk() {
        if (!isControlCreated()) {
            return true;
        }
        try {
            PluginPreference pluginStore = getPluginStore();

            pluginStore.setBoolean(PractiTestConstants.PREF_IS_PRACTITEST_ENABLED, chckEnableIntegration.getSelection());
            pluginStore.setString(PractiTestConstants.PREF_AUTH_TOKEN, txtToken.getText());
            pluginStore.setString(PractiTestConstants.PREF_AUTH_EMAIL, txtEmail.getText());
            pluginStore.setString(PractiTestConstants.PREF_AUTH_PROJECT, txtProject.getText());

            pluginStore.save();
            return true;
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update PractiTest Integration Settings.");
            return false;
        }
    }

    private void initializeInput() {
        try {
            PluginPreference pluginStore = getPluginStore();

            chckEnableIntegration.setSelection(pluginStore.getBoolean(PractiTestConstants.PREF_IS_PRACTITEST_ENABLED, false));
            chckEnableIntegration.notifyListeners(SWT.Selection, new Event());

            txtToken.setText(pluginStore.getString(PractiTestConstants.PREF_AUTH_TOKEN, ""));
            txtEmail.setText(pluginStore.getString(PractiTestConstants.PREF_AUTH_EMAIL, ""));
            txtProject.setText(pluginStore.getString(PractiTestConstants.PREF_AUTH_PROJECT, ""));

            container.layout(true, true);
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update PractiTest Integration Settings.");
        }

    }
}
