package sitschedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

public class AppButton extends JLabel implements AppComponent {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private boolean enabled = true;

    @Override
    public void addToPanel(JPanel panel) {
        panel.add(this, constraints);
    }

    void addListeners(Function<MouseEvent, Boolean> function) {
        List<Color> colors = List.of(Resource.dark, Resource.medium, Resource.light);
        final int[] index = {0};
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enabled) {
                    enabled = false;
                    setForeground(Resource.grey);
                    setBackground(Resource.dark);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            function.apply(e);
                            return null;
                        }

                        @Override
                        protected void done() {
                            setForeground(Resource.white);
                            enabled = true;
                        }
                    };
                    worker.execute();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                index[0]++;
                if (enabled)
                    setBackground(colors.get(index[0]));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                index[0]--;
                if (enabled)
                    setBackground(colors.get(index[0]));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                index[0]++;
                if (enabled)
                    setBackground(colors.get(index[0]));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                index[0]--;
                if (enabled)
                    setBackground(colors.get(index[0]));
            }
        });
    }

    AppButton(String text, int row) {
        setText(text);
        setPreferredSize(Resource.label);
        setForeground(Resource.white);
        setBackground(Resource.dark);
        setFont(Resource.bold);
        setFocusable(true);
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        constraints.insets = Resource.insets;
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
    }
}
