package handler;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ServiceManager;
import constant.LazyyConstant;
import model.FundBean;
import model.TypeAlias;
import storage.LazyyHelperSettings;
import util.HttpClientPool;
import util.LogUtil;
import util.thread.CommonThreadPool;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 天天基金控制器
 * @author liaodewen
 */
public class TianTianFundHandler extends FundRefreshHandler {

    private static Gson gson = new Gson();

    private List<TypeAlias> codes = new ArrayList<>();

    private CommonThreadPool commonThreadPool = CommonThreadPool.getInstance();

    private LazyyHelperSettings settings;

    private volatile Thread worker;

    private volatile String time;
    private volatile boolean autoRunFlag;

    public TianTianFundHandler(JTable table, JLabel label1, JLabel label2) {
        super(table, label1, label2);
        settings = ServiceManager.getService(LazyyHelperSettings.class);
    }

    @Override
    public void autoRefresh() {
        // 中断
        if (null != worker) {
            worker.interrupt();
        }
        time = settings.getGeneralSettings().getTime();
        autoRunFlag = settings.getGeneralSettings().isAutoRefresh();
        if (autoRunFlag) {
            if (super.canNowRefresh()) {
                LogUtil.info("Lazyy 开启自动刷新基金数据.时间间隔:" + time + "分钟!");
            } else {
                LogUtil.info("Lazyy 不在时间段,关闭自动刷新.");
                autoRunFlag = false;
                return;
            }
        } else {
            LogUtil.info("Lazyy 关闭自动刷新基金数据.");
            return;
        }
        worker = new Thread(() -> {
            while (autoRunFlag) {
                try {
                    Thread.sleep(Integer.valueOf(time) * 60 * 1000);
                    LogUtil.info("Lazyy 自动刷新...");
                    stepAction();
                    updateSha();
                    printlnFushi();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        worker.start();
    }

    @Override
    public void refresh(String flag, List<TypeAlias> code) {
        super.clear();
        codes.clear();
        codes.addAll(code);
        stepAction();
        updateSha();
        printlnFushi();

        switch (flag) {
            case LazyyConstant.REFRESH_INIT:
                // 初始化
                LogUtil.info("Lazyy 初始化基金数据.");
                // 触发自动刷新逻辑
                autoRefresh();
                break;
            case LazyyConstant.REFRESH_UPDATE:
                // 手动更新
                LogUtil.info("Lazyy 手动更新基金数据.");
                // 不触发自动刷新逻辑
                break;
            default:
                break;
        }
    }

    private void stepAction() {
        if (codes.isEmpty()) {
            LogUtil.info("Lazyy 基金数据为空...");
            return;
        }
        // 批量更新基金信息
        List<FundBean> beans = commonThreadPool.executeTasks(()->{
            List<Supplier<FundBean>> temp = Lists.newArrayListWithCapacity(codes.size());
            codes.stream().forEach(s -> {
                temp.add(() -> {
                    FundBean bean = null;
                    try {
                        String result = HttpClientPool.getHttpClient().get("http://fundgz.1234567.com.cn/js/" + s.getCode() + ".js?rt=" + System.currentTimeMillis());
                        String json = result.substring(8, result.length() - 2);
                        bean = gson.fromJson(json, FundBean.class);
                        bean.setMoney(s.getMoney());
                        bean.setNumber(s.getNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bean;
                });
            });
            return temp;
        });
        updateDatas(beans);
        updateUI();
    }

    private void printlnFushi() {
        // 是否打印富时A50
        if (settings.getGeneralSettings().isHidenFushi()) {
            return;
        }
        commonThreadPool.execute(() -> {
            try {
                // 富时A50
                // var hq_str_hf_CHA50CFD="16762.500,,16762.000,16763.000,16767.000,16467.000,15:52:13,16491.000,16489.000,711926.000,14,13,2020-12-14,富时中国A50指数,198716";
                String fushiResult = HttpClientPool.getHttpClient().get("https://hq.sinajs.cn/list=hf_CHA50CFD");
                String[] fushiResultArr = fushiResult.split("=", -1)[1]
                        .replaceAll("\"", "")
                        .replaceAll(";", "")
                        .split("\\,", -1);
                BigDecimal now = new BigDecimal(fushiResultArr[0]);
                BigDecimal before = new BigDecimal(fushiResultArr[8]);
                BigDecimal index = now.subtract(before).multiply(BigDecimal.valueOf(100D)).divide(before, 2, BigDecimal.ROUND_HALF_UP);
                String fushi = index.toString();
                if (index.doubleValue() >= 0D) {
                    fushi = "+" + fushi + "%↑";
                } else {
                    fushi = "-" + fushi + "%↓";
                }
                // 2020-12-14 15:52:13 富时中国A50指数 +1.24%
                String show = String.format("%s %s %s %s", fushiResultArr[12], fushiResultArr[6], fushiResultArr[13], fushi);
                LogUtil.info(show);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSha() {
        commonThreadPool.execute(() -> {
            try {
                // var hq_str_s_sh000001="上证指数,3317.9847,40.5445,1.24,1822997,25641104";
                String result = HttpClientPool.getHttpClient().get("https://hq.sinajs.cn/list=s_sh000001");
                // 上证指数,3317.9847,40.5445,1.24,1822997,25641104
                result = result
                        .split("=", -1)[1]
                        .replaceAll("\"", "")
                        .replaceAll(";", "");
                String[] sha = result.split(",", -1);
                updateShaLabel(sha);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
