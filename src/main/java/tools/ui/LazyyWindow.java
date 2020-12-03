package tools.ui;

import com.intellij.openapi.components.ServiceManager;
import constant.LazyyConstant;
import handler.FundRefreshHandler;
import handler.TianTianFundHandler;
import model.TypeAlias;
import storage.LazyyHelperSettings;
import util.LogUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LazyyWindow implements ToolWindowFactory {

    private JPanel mPanel;
    private JTable table1;
    private JButton refreshButton;
    private JLabel aLabel;
    private JLabel bLabel;
    private JLabel logoLabel;

    private LazyyHelperSettings settings;

    private ShowWindow showWindow = new ShowWindow();

    FundRefreshHandler fundRefreshHandler;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mPanel, "lazyy", false);
        Content other = contentFactory.createContent(showWindow.getShowPanel(), "kkanp", false);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().addContent(other);

        LogUtil.setProject(project);

        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> {
                    fundRefreshHandler.handle(loadFunds());
                }
        );

        this.settings = ServiceManager.getService(LazyyHelperSettings.class);
    }

    @Override
    public void init(ToolWindow window) {
        fundRefreshHandler = new TianTianFundHandler(table1, aLabel, bLabel);
        fundRefreshHandler.handle(loadFunds());

    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

    private List<TypeAlias> loadFunds() {
        List<TypeAlias> alias = new ArrayList<>();

        try {
            alias = settings.getDateSettings().getTypeAliases();
        } catch (NullPointerException e) {

        }
        return alias;
    }

}
