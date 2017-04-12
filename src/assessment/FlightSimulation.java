//Package
package assessment;
/*
 * FlightSimulation is a flight simulator with two sliders showing coordinate X and speed of plain.
 * There is also output of plane's runway and elevation at real time(each second).
 * 
 * @author k1631383, k1631285
 */
//Imports
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FlightSimulation extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5393806414421728502L;
	//Fields
	JButton jbReset;
	JPanel jpBottom;
	static JSlider jsHorizontal;
	static JSlider jsVertical;
	static JTextArea jtaState;
	JScrollPane jspState;
	BufferedReader stdinReader;

	private static int elevation;
	private static int speed;
	private static int y;
	private int x;
	static final int H_MIN = 0;
	static final int H_MAX = 10;
	static final int H_INIT = 5;
	static final int V_MIN = 0;
	static final int V_MAX = 10;
	static final int V_INIT = 0;
	private static boolean successfulFlight;
/*
 * Constructor method
 * Sets title of Jframe and default close operation
 * Initialises widgets
 */
	public FlightSimulation() {
		super("Flight Simulation");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initWidgets();

	}
	/*
	 * Initialises widgets and sets up layout 
	 */
	private void initWidgets() {
		jbReset = new JButton("Reset");
		jsHorizontal = new JSlider(JSlider.HORIZONTAL, H_MIN, H_MAX, H_INIT);
		jsVertical = new JSlider(JSlider.VERTICAL, V_MIN, V_MAX, V_INIT);
		jpBottom = new JPanel();
		jtaState = new JTextArea();
		jspState = new JScrollPane(jtaState);
		stdinReader = new BufferedReader(new InputStreamReader(System.in));

		/**
		 * Event Listener
		 * Changes the speed and x values when their sliders are moved
		 *
		 */
		class SliderListener implements ChangeListener {
			// listener = new ChangeListener();
			@Override
			public void stateChanged(ChangeEvent e) {
				int speed = jsVertical.getValue();
				int x = jsHorizontal.getValue();
			}
		}
		
		/**
		 * Event Listener
		 * Resets the program and sets value of sliders to initial values
		 *
		 */
		class ResetListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				jsHorizontal.setValue(H_INIT);
				jsVertical.setValue(V_MIN);
				elevation=0;
				y=0;
			}
		}
		// Sets default size and basic layout of program
		this.setSize(600, 500);
		this.setLayout(new GridLayout(2, 1));
		this.add(jspState);
		this.add(jpBottom);
		
		//Adds ticks to horizontal slider
		jsHorizontal.setPaintTicks(true);
		jsHorizontal.setMajorTickSpacing(10);
		jsHorizontal.setMinorTickSpacing(1);
		jsHorizontal.setPaintLabels(true);
		jsHorizontal.setSnapToTicks(true);
		
		//Adds labels to sliders
		Dictionary<Integer, Component> labelTable = new Hashtable<Integer, Component>();
		for (int i = 0; i <= 10; i++) {
			labelTable.put(i, new JLabel(i + ""));
		}

		jsHorizontal.setLabelTable(labelTable);
		jsVertical.setLabelTable(labelTable);
		
		//Adds ticks to vertical slider
		jsVertical.setPaintTicks(true);
		jsVertical.setMajorTickSpacing(10);
		jsVertical.setMinorTickSpacing(1);
		jsVertical.setPaintLabels(true);
		jsVertical.setSnapToTicks(true);
		
		//Adds buttons and sliders to user interface
		JButton jbOK = new JButton("Reset");
		jpBottom.setLayout(new BorderLayout());
		jpBottom.add(jsHorizontal, BorderLayout.NORTH);
		jpBottom.add(jsVertical, BorderLayout.CENTER);
		jpBottom.add(jbReset, BorderLayout.SOUTH);
		
		//Adds event listeners to the program
		jbReset.addActionListener(new ResetListener());
		jsVertical.addChangeListener(new SliderListener());
		jsHorizontal.addChangeListener(new SliderListener());
		

	}

	public static void main(String[] args) {
		
		//Creates new flight simulation and displays it on screen
		new FlightSimulation().setVisible(true);
		//Current time during program
		int i = 0;

		do {
			//Program pauses for one second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
			}
			//Prints new statement in text area with current state of plane
			jtaState.append("Seconds: " + i + "\nx: " + jsHorizontal.getValue() + " y: " + y + " Speed: "
					+ jsVertical.getValue() + " Elevation: " + elevation + "\n");
			//Increases current time by one second
			i++;
			
			//Changes y value when speed increases
			if (jsVertical.getValue() > 0) {
				y += jsVertical.getValue();
			}
			
			//Increases elevation if current time is greater than 5 seconds and speed is 10
			if (i > 5 && jsVertical.getValue() == 10) {
				elevation++;
			}
			
			//IF elevation is greater than 5, the x value is 5 and the plane is at the end of the runway, the plane successfully flies
			if (elevation >= 5 && y > 110 && jsHorizontal.getValue() == 5) {
				successfulFlight = true;
				//Successful statement is printed
				jtaState.append("Plane is in the air.");
			//If conditions are not met the plane does not take off
			} else if (elevation != 5 && jsHorizontal.getValue() != 5 && y > 110) {
				successfulFlight = false;
				//Unsuccessful statement is printed
				jtaState.append("Take off failed.");
			}
		
			//The program runs as long as the plane has not taken off yet and whilst the plane is still on the runway
		} while (!successfulFlight && y <= 110);

	}
}
