
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.IOException;

public class Game implements Runnable {

	@Override
	public void run() {
		try {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper: Java Edition!");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Control Panel
        final JPanel control_panel = new JPanel();
        JLabel time = new JLabel("" + court.getTime());
        frame.add(control_panel, BorderLayout.NORTH);
        
        // Reset Button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		court.reset();
            		court.setState(0);
            	} catch (IOException i) {
            	}
            }
        });
        
        //High Score Button
        final JButton hs = new JButton("Best Times");
        hs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            		court.setState(1);
            }
        });
        
        
        control_panel.add(reset);
        control_panel.add(time);
        control_panel.add(hs);
        
        //Timer to count down
        Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time.setText("" + court.getTime());
				control_panel.remove(time);
				control_panel.add(time);
			}
		});
        timer.start();

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);


        // Start game
        court.reset();

		} catch (IOException e) {
		}

	}
	
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

}
