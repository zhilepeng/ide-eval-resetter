package io.zhile.research.intellij.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.application.impl.ApplicationInfoImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import io.zhile.research.intellij.helper.Constants;
import io.zhile.research.intellij.helper.NotificationHelper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class ResetAction extends AnAction {
    private static final String OLD_MACHINE_ID_KEY = "JetBrains.UserIdOnMachine";
    private static final String NEW_MACHINE_ID_KEY = "user_id_on_machine";
    private static final String DEVICE_ID_KEY = "device_id";
    private static final String DEFAULT_COMPANY_NAME = "jetbrains";

    public ResetAction() {
        super("Reset " + Constants.PRODUCT_NAME + "'s Eval", "Reset my IDE eval information", AllIcons.General.Reset);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        File evalFile = getEvalFile();
        if (evalFile.exists()) {
            if (!FileUtil.delete(evalFile)) {
                NotificationHelper.showError(project, "Remove eval folder failed!");
                return;
            }
        }

        File optionsFile = getOptionsFile();
        if (optionsFile.exists()) {
            try (Scanner scanner = new Scanner(optionsFile)) {
                StringBuilder sbContent = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.contains("name=\"evlsprt")) {
                        sbContent.append(line).append("\n");
                    }
                }

                Files.write(Paths.get(optionsFile.toURI()), sbContent.toString().getBytes());
            } catch (IOException e) {
                NotificationHelper.showError(project, e.getMessage());
                return;
            }
        }

        ApplicationInfoEx appInfo = ApplicationInfoImpl.getShadowInstance();

        String companyName = appInfo.getShortCompanyName();
        String node = StringUtil.isEmptyOrSpaces(companyName) ? DEFAULT_COMPANY_NAME : companyName.toLowerCase();

        Preferences prefsRoot = Preferences.userRoot();
        Preferences prefs = prefsRoot.node(node);

        prefsRoot.remove(OLD_MACHINE_ID_KEY);
        prefs.remove(NEW_MACHINE_ID_KEY);
        prefs.remove(DEVICE_ID_KEY);

        Preferences.userRoot().node(Constants.PLUGIN_NAME).put(Constants.PRODUCT_NAME + Constants.PRODUCT_HASH, Long.toString(System.currentTimeMillis()));

        if (appInfo.isVendorJetBrains() && SystemInfo.isWindows) {
            String[] names = new String[]{"PermanentUserId", "PermanentDeviceId"};
            for (String name : names) {
                if (deleteSharedFile(name)) {
                    continue;
                }

                NotificationHelper.showError(project, "Remove " + name + " file failed!");
                return;
            }
        }

        NotificationHelper.showInfo(project, "Reset successfully!\nPlease restart your IDE and enjoy it!");
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().restart());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

    protected boolean deleteSharedFile(String fileName) {
        String appData = System.getenv("APPDATA");
        if (appData == null) {
            return false;
        }

        File dir = Paths.get(appData, "JetBrains", fileName).toFile();

        return !dir.exists() || dir.delete();
    }

    protected File getEvalFile() {
        String configPath = PathManager.getConfigPath();

        return new File(configPath, "eval");
    }

    protected File getOptionsFile() {
        String configPath = PathManager.getConfigPath();

        return new File(new File(configPath, "options"), "other.xml");
    }
}
