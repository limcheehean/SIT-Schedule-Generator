package sitschedule;

import javax.swing.*;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class AppArea extends JTextArea implements AppComponent {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private final JScrollPane pane = new JScrollPane(this);

    @Override
    public void addToPanel(JPanel panel) {
        panel.add(pane, constraints);
    }

    public void append(String text) {
        System.out.println(text);
        super.append(text + "\n");
    }

    AppArea(int row) {
        setBackground(Resource.light);
        setForeground(Resource.white);
        setFont(Resource.small);
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        pane.setPreferredSize(Resource.area);
        pane.setBorder(Resource.empty);
        pane.setBackground(Resource.light);
        pane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        pane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        constraints.insets = Resource.insets;
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
    }

}
