package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.*;
import javax.swing.JButton;

public class TabButton extends Button {
    private String tab;
    private UIDrawer ui;

    public TabButton(String tab, UIDrawer ui) {
        super(tab);
        this.tab = tab;
        this.ui = ui;
        addListener();
    }

    protected void addListener() {
        this.addActionListener(new TabPressHandler());
    }

    //Represents a class to handle when the button is pressed
    private class TabPressHandler implements ActionListener {

        // MODIFIES: graph, graphGraphicsPane
        // EFFECTS: When the button is pressed, clears the current set of equations, wipes any active fields on the
        // drawing pane, and then redraws it.
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.updateCurrentTab(tab);
        }
    }

}