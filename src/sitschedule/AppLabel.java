package sitschedule;

import javax.swing.*;
import java.awt.*;

public class AppLabel extends JLabel implements AppComponent {

    private final GridBagConstraints constraints = new GridBagConstraints();

    @Override
    public void addToPanel(JPanel panel) {
        panel.add(this, constraints);
    }

    AppLabel(String text, int row) {
        setText(text);
        setPreferredSize(Resource.label);
        setForeground(Resource.white);
        setFont(Resource.bold);
        setHorizontalAlignment(RIGHT);
        constraints.insets = Resource.insets;
        constraints.gridx = 0;
        constraints.gridy = row;
    }

}
