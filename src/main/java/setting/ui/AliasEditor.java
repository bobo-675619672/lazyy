package setting.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import util.DateUtil;

import javax.swing.*;

public class AliasEditor extends DialogWrapper {

    private JPanel myPanel;
    private JTextField codeField;
    private JTextField numberField;
    private JTextField holdField;
    private JTextField remarkField;
    private JTextField updatedField;

    public interface Validator {
        boolean isOK(String name, String value);
    }

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

}
