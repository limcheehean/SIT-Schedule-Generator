package sitschedule;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Map;

public class Resource {

    // Colours
    static Color white = new Color(255, 255, 255);
    static Color grey = new Color(130, 130, 130);
    static Color background = new Color(37, 73, 102);
    static Color dark = new Color(47, 83, 112);
    static Color medium = new Color(57, 93, 122);
    static Color light = new Color(67, 103, 132);

    // Fonts
    static Font normal = new Font("Arial", Font.PLAIN, 16);
    static Font bold = new Font("Arial", Font.BOLD, 16);
    static Font small = new Font("Arial", Font.PLAIN, 12);

    // Dimensions
    static Dimension label = new Dimension(120, 30);
    static Dimension field = new Dimension(300, 30);
    static Dimension area = new Dimension(450, 200);

    // Borders
    static Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    // Insets
    static Insets insets = new Insets(5, 5, 5, 5);

    // Modules
    static Map<String, String> modules = Map.of(
            "ICT 1011", "ICT 1011 Computer Organization & Architecture",
            "ICT 4012A", "ICT 4012A Integrated Work Study Programme (Career Skills)",
            "UDE 1001", "UDE 1001 Introduction to Design Innovation",
            "UCS 1001", "UCS 1001 Critical Thinking and Communication",
            "INF 1007", "INF 1007 Ethics and Professional Conducts",
            "INF 2001", "INF 2001 Introduction to Software Engineering",
            "UDE 2001", "UDE 2001 Interdisciplinary Design Innovation"
    );

}
