package sitschedule;

import javax.swing.*;
import java.awt.*;

public class AppPasswordField extends JPasswordField implements AppComponent {

    private final GridBagConstraints constraints = new GridBagConstraints();


    @Override
    public void addToPanel(JPanel panel) {
        panel.add(this, constraints);
    }

    AppPasswordField(String text, int row) {
        setText(text);
        setPreferredSize(Resource.field);
        setBackground(Resource.light);
        setForeground(Resource.white);
        setCaretColor(Resource.white);
        setBorder(Resource.empty);
        constraints.insets = Resource.insets;
        constraints.gridx = 1;
        constraints.gridy = row;
    }

}
