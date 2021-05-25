package tools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import constant.LazyyConstant;
import handler.FundRefreshHandler;
import handler.TianTianFundHandler;
import org.jetbrains.annotations.NotNull;
import util.LogUtil;

import javax.swing.*;

public class LazyyWindow implements ToolWindowFactory {

    private JPanel mPanel;
    private JTable table1;
    private JButton refreshButton;
    private JLabel aLabel;
    private JLabel bLabel;
    private JLabel logoLabel;

    private ShowWindow showWindow = new ShowWindow();

    private NewsWindow newsWindow = new NewsWindow();

    FundRefreshHandler fundRefreshHandler;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 主面板
        Content lazy = contentFactory.createContent(mPanel, "lazyy", false);
        // 新闻面板
        Content news = contentFactory.createContent(newsWindow.getNewsPanel(), "newss", false);
        // 大盘面板
        Content kanPan = contentFactory.createContent(showWindow.getShowPanel(), "kkanp", false);
        // 设置
        toolWindow.getContentManager().addContent(lazy);
        toolWindow.getContentManager().addContent(news);
        toolWindow.getContentManager().addContent(kanPan);

        LogUtil.setProject(project);
        // logo
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
    }

    @Override
    public void init(ToolWindow window) {
        fundRefreshHandler = new TianTianFundHandler(table1, aLabel, bLabel);
        refreshButton.addActionListener(a -> fundRefreshHandler.refresh());
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

}
