package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ConcurrentGUI extends JFrame{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel label = new JLabel("0");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    
    /**
     * Builds a new CGUI.
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenDimension.getWidth() * WIDTH_PERC) ,(int) (screenDimension.getHeight() * HEIGHT_PERC));
        final JPanel panel = new JPanel();
        panel.add(label);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.setContentPane(panel);
        this.setVisible(true);
        
        final Agent agent = new Agent();
        
        stop.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stopCounter();
                down.setEnabled(false);
                up.setEnabled(false);
                stop.setEnabled(false);
            }
        });
        
        down.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button down.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setDown();
            }
        });
        
        up.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button up.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setIncrement();
            }
        });
        
        new Thread(agent).start();
    }
    
    private class Agent implements Runnable {
        private volatile boolean stop;
        private volatile boolean isDown;
        private int counter;
        
        public void run() {
            try {
                while(!stop) {
                    final int count = this.counter;
                    SwingUtilities.invokeAndWait(new Runnable() {
                        
                        @Override
                        public void run() {
                            ConcurrentGUI.this.label.setText(Integer.toString(count));
                        }
                    });
                    if(isDown) {
                        this.counter--;
                    } else {
                        this.counter++;
                    }
                    Thread.sleep(100);
                }
            } catch (InvocationTargetException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        /**
         * External command to stop counting.
         */
        public void stopCounter() {
            this.stop = true;
        }
        /**
         * Set is Down true
         */
        public void setDown() {
            this.isDown = true;
        }
        /**
         * Set is Down false
         */
        public void setIncrement() {
            this.isDown = false;
        }
    }

}
