package tools.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import constant.LazyyConstant;
import handler.FundRefreshHandler;
import handler.TianTianFundHandler;
import model.TypeAlias;
import org.jetbrains.annotations.NotNull;
import storage.LazyyHelperSettings;
import util.LogUtil;

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

    private NewsWindow newsWindow = new NewsWindow();

    FundRefreshHandler fundRefreshHandler;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content lazy = contentFactory.createContent(mPanel, "lazyy", false);
        Content news = contentFactory.createContent(newsWindow.getNewsPanel(), "newss", false);
        Content kanPan = contentFactory.createContent(showWindow.getShowPanel(), "kkanp", false);

        toolWindow.getContentManager().addContent(lazy);
        toolWindow.getContentManager().addContent(news);
        toolWindow.getContentManager().addContent(kanPan);

        LogUtil.setProject(project);
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> fundRefreshHandler.refresh(LazyyConstant.REFRESH_UPDATE, loadFunds()));
    }

    @Override
    public void init(ToolWindow window) {
        // 读取数据
        this.settings = ServiceManager.getService(LazyyHelperSettings.class);
        fundRefreshHandler = new TianTianFundHandler(table1, aLabel, bLabel);
        // 初始化数据
        fundRefreshHandler.refresh(LazyyConstant.REFRESH_INIT, loadFunds());
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

    /**
     * 获取基金数据
     * @return
     */
    private List<TypeAlias> loadFunds() {
        List<TypeAlias> alias = new ArrayList<>();
        try {
            alias = settings.getDateSettings().getTypeAliases();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return alias;
    }

}
