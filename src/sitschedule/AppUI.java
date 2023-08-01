package sitschedule;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUI {

    private void createGUI() {

        String username = "";
        String password = "";
        // Get credentials
        try {
            Scanner scanner = new Scanner(new File("credentials"));
            String decoded = new String(Base64.getDecoder().decode(scanner.nextLine()));
            Matcher matcher = Pattern.compile("^username=(.+)\npassword=(.+)$").matcher(decoded);
            if (matcher.find()) {
                username = matcher.group(1);
                password = matcher.group(2);
            }
        } catch (FileNotFoundException | NoSuchElementException ignored) {}

        // Create components
        AppButton button = new AppButton("Generate", 6);
        AppArea area = new AppArea(7);

        List<AppComponent> components = List.of(
                new AppLabel("Username", 0),
                new AppLabel("Password", 1),
                new AppLabel("Trimester Start", 4),
                new AppLabel("Trimester End", 5),
                new AppField("username", username, 0, "^\\d{7}@sit\\.singaporetech\\.edu\\.sg$", "Error: Invalid username format"),
                new AppPasswordField(password, 1),
                new AppField("start", "28082023", 4, "date", "Error: Invalid start date, please use the DDMMYYYY format"),
                new AppField("end", "07012024", 5, "date", "Error: Invalid end date, please use the DDMMYYYY format"),
                button,
                area
        );

        button.addListeners(e -> {

            // Reset message box
            area.setText("");

            // Check input
            Map<String, String> inputs = new HashMap<>();
            final boolean[] valid = {true};
            components.forEach(component -> {
                if (component instanceof AppField field) {
                    valid[0] = field.validateInput(area) && valid[0];
                    inputs.put(field.getKey(), field.getInput());
                } else if (component instanceof AppPasswordField field)
                    inputs.put("password", new String(field.getPassword()));
            });
            if (!valid[0])
                return false;

            new DataProcessor(inputs.get("username"), inputs.get("password"), inputs.get("start"), inputs.get("end"), area);

            return true;

        });

        // Create window
        JFrame window = new JFrame("SIT Schedule Generator");
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Resource.background);
        window.add(panel);

        // Add components
        components.forEach(component -> component.addToPanel(panel));

        // Display window
        window.setVisible(true);
        window.repaint();

    }



    AppUI() {
        createGUI();
    }


}
