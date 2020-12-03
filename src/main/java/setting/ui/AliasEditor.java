package setting.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AliasEditor extends DialogWrapper {
    private JPanel myPanel;
    private JTextField codeField;
    private JTextField numberField;
    private JTextField moneyField;
    private JTextField remarkField;

    public interface Validator {
        boolean isOK(String name, String value);
    }

    public AliasEditor(String title, String code, String number, String money, String remark) {
        super(true);
        setTitle(title);
        codeField.setText(code);
        numberField.setText(number);
        moneyField.setText(money);
        remarkField.setText(remark);
        init();
    }

    public String getCode() {
        return codeField.getText().trim();
    }

    public String getNumber() {
        return numberField.getText().trim();
    }

    public String getMoney() {
        return moneyField.getText().trim();
    }

    public String getRemark() {
        return remarkField.getText().trim();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myPanel;
    }

}
