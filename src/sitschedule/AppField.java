package sitschedule;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppField extends JTextField implements AppComponent {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private String key, input, regex, error;

    public boolean validateInput(JTextArea area) {
        input = getText().trim().toLowerCase();
        if (regex.equals("date")) {
            try {
                DateTimeFormatter.ofPattern("ddMMyyyy").parse(input);
            } catch (DateTimeParseException e) {
                area.append(error);
                return false;
            }
        } else if (!input.matches(regex)) {
            area.append(error);
            return false;
        }
        return true;
    }

    String getKey() {
        return key;
    }

    String getInput() {
        return input;
    }

    @Override
    public void addToPanel(JPanel panel) {
        panel.add(this, constraints);
    }

    AppField(String key, String text, int row, String regex, String error) {
        setText(text);
        setPreferredSize(Resource.field);
        setBackground(Resource.light);
        setForeground(Resource.white);
        setCaretColor(Resource.white);
        setBorder(Resource.empty);
        constraints.insets = Resource.insets;
        constraints.gridx = 1;
        constraints.gridy = row;
        this.key = key;
        this.regex = regex;
        this.error = error;
    }

}
