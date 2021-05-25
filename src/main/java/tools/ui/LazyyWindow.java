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
import org.jetbrains.annotations.NotNull;
import storage.LazyyHelperSettings;
import util.LogUtil;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    private LazyyHelperSettings settings;

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
        settings = ServiceManager.getService(LazyyHelperSettings.class);
        // 刷新间隔
        refreshButton.addActionListener(a -> {
            fundRefreshHandler.refresh();
            // 设置倒计时
            int refreshTime = Integer.valueOf(settings.getAdvancedSettings().getRefreshTime());
            AtomicInteger time = new AtomicInteger(refreshTime);
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
            exec.scheduleAtFixedRate(() -> {
                refreshButton.setEnabled(false);
                refreshButton.setText("等待(" + time + "s)");
                time.getAndDecrement();
                if (time.intValue() < 0) {
                    exec.shutdown();
                    refreshButton.setEnabled(true);
                    refreshButton.setText("刷新");
                }
            },0,1, TimeUnit.SECONDS);
        });
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
