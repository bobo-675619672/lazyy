package setting.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.DateUtil;
import util.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AliasEditor extends DialogWrapper {

    private JPanel myPanel;
    private JTextField codeField;
    private JTextField numberField;
    private JTextField holdField;
    private JTextField remarkField;
    private JTextField updatedField;

    /**
     * 确定按钮
     * 退出按钮
      */
    private CustomOkAction okAction;
    private DialogWrapperExitAction exitAction;

    public AliasEditor(String title, String code, String number, String hold, String remark, String updated) {
        super(true);
        setTitle(title);
        codeField.setText(code);
        numberField.setText(number);
        holdField.setText(hold);
        remarkField.setText(remark);
        updatedField.setText(updated);
        init();
    }

    public String getCode() {
        return codeField.getText().trim();
    }

    public String getNumber() {
        return numberField.getText().trim();
    }

    public String getHold() {
        return holdField.getText().trim();
    }

    public String getRemark() {
        return remarkField.getText().trim();
    }

    public String getUpdated() {
        return DateUtil.getCurDateStr();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myPanel;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        // 基金编号，6位全数字字符串
        String code = this.getCode();
        if (6 != code.length() || !StringUtils.isNumeric(code)) {
            return new ValidationInfo("基金编号必须6位全数字!\nExample:\n123456");
        }
        // 持有份数，必须有效数字
        String number = this.getNumber();
        if (!StringUtil.isNumber(number)) {
            return new ValidationInfo("持有份数必须为数字!\nExample:\n100\n100.1");
        }
        // 持有价，必须有效数字
        String hold = this.getHold();
        if (!StringUtil.isNumber(hold)) {
            return new ValidationInfo("持有价必须为数字!\nExample:\n1.23");
        }
        return null;
    }

    /**
     * 覆盖默认的ok/cancel按钮
     * @return
     */
    @NotNull
    @Override
    protected Action[] createActions() {
        okAction = new CustomOkAction();
        exitAction = new DialogWrapperExitAction("退出按钮", CANCEL_EXIT_CODE);
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction, exitAction};
    }

    /**
     * 自定义 ok Action
     */
    protected class CustomOkAction extends DialogWrapperAction {

        protected CustomOkAction() {
            super("确定按钮");
        }

        @Override
        protected void doAction(ActionEvent e) {
            // 点击ok的时候进行数据校验
            ValidationInfo validationInfo = doValidate();
            if (validationInfo != null) {
                Messages.showMessageDialog(validationInfo.message,"校验不通过", Messages.getInformationIcon());
            } else {
                // 确认-关闭
                close(OK_EXIT_CODE);
            }
        }
    }

}
