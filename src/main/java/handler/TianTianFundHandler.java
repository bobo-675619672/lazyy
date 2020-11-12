package handler;

import model.FundBean;
import model.TypeAlias;
import util.HttpClientPool;
import util.LogUtil;
import com.google.gson.Gson;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TianTianFundHandler extends FundRefreshHandler {
    private static Gson gson = new Gson();
    private List<TypeAlias> codes = new ArrayList<>();

    private Thread worker;
    public TianTianFundHandler(JTable table, JLabel label1, JLabel label2) {
        super(table, label1, label2);
    }

    @Override
    public void handle(List<TypeAlias> code) {
        LogUtil.info("Lazyy 更新基金编码数据.");
        if (worker != null) {
            worker.interrupt();
        }
        if (code.isEmpty()){
            return;
        }
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (worker!=null && worker.hashCode() == Thread.currentThread().hashCode() && !Thread.currentThread().isInterrupted()){
                    stepAction();
                    updateSha();
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        //移除了中断线程的警告
                    }
                }
            }
        });
        clear();
        codes.clear();
        codes.addAll(code);
        // 排序，按加入顺序
        for (TypeAlias s : codes) {
            updateData(new FundBean(s));
        }
        worker.start();
    }

    private void stepAction(){
        LogUtil.info("Lazyy 刷新基金数据.");
        for (TypeAlias s : codes) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = HttpClientPool.getHttpClient().get("http://fundgz.1234567.com.cn/js/" + s.getCode() + ".js?rt=" + System.currentTimeMillis());
                        String json = result.substring(8, result.length() - 2);
                        FundBean bean = gson.fromJson(json, FundBean.class);
                        bean.setMoney(s.getMoney());
                        bean.setNumber(s.getNumber());
                        updateData(bean);
                        updateUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void updateSha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // var hq_str_s_sh000001="上证指数,3317.9847,40.5445,1.24,1822997,25641104";
                    String result = HttpClientPool.getHttpClient().get("http://hq.sinajs.cn/list=s_sh000001");
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
            }
        }).start();
    }

}
