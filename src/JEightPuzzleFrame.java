
/*
 * UIN:4469
 *Homework 1 
 *Date: 10/10/2016
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.*;

public class JEightPuzzleFrame extends JFrame implements ActionListener {

	private int[] position = new int[9];
	private BufferedImage image;

	// 8 buttons for the frame.
	private JButton buttons[] = new JButton[8];
	// the size of the icon
	private static int width;
	private static int height;
	private String path;
	private JPanel emptybutton = new JPanel();
	private int[][] frame = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
	private JPanel centerPanel;

	private int shufflecount;

	public JEightPuzzleFrame(String Title, String path) {
		super(Title);
		this.path = path;
		centerPanel = new JPanel();

		centerPanel.setLayout(new GridLayout(3, 3, 0, 0));
		add(centerPanel);
		gametime();

	}

	private BufferedImage getIcon(int leftTopX, int leftTopY) {
		getImage();
		int IconWidth = image.getWidth() / 3;
		int IconHeight = image.getHeight() / 3;

		BufferedImage part = new BufferedImage(IconWidth, IconHeight,
				BufferedImage.TYPE_4BYTE_ABGR);

		// copies the data from "image" to "part"

		for (int x = 0; x < IconWidth; x++) {
			for (int y = 0; y < IconHeight; y++) {
				part.setRGB(x, y, image.getRGB(x + leftTopX, y + leftTopY));
			}
		}
		return part;

	}

	private void getImage() {
		image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("Image not found");
			System.exit(1);
		}

		width = image.getWidth();
		height = image.getHeight();
	}

	// used to shuffle the order array that sets the button on the frame.
	private void shuffle(int[] beingOrder) {

		for (int i = beingOrder.length - 1; i > 0; i--) {
			Random rnd = ThreadLocalRandom.current();
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = beingOrder[index];
			beingOrder[index] = beingOrder[i];
			beingOrder[i] = a;

		}

	}

	// sets the buttons and icons together on the frame
	private void gametime() {

		getImage();

		int[][] placeOfPair = { { 0, 0 }, { width / 3, 0 },
				{ width / 3 * 2, 0 }, { 0, width / 3 },
				{ width / 3, width / 3 }, { width / 3 * 2, width / 3 },
				{ 0, width / 3 * 2 }, { width / 3, width / 3 * 2 } };

		for (int i = 0; i < 8; i++) {

			buttons[i] = new JButton();
			buttons[i].setIcon(new ImageIcon(
					getIcon(placeOfPair[i][0], placeOfPair[i][1])));
		}

		int[] beingOrder = { 0, 1, 4, 5, 2, 3, 6, 7 };
		// emptybutton.setOpaque(true);
		// emptybutton.setBackground(new Color(0,0 ,0, 125));
		centerPanel.add(emptybutton);// adds the emptybuttons
		position[0] = 0;
		if (shufflecount == 0) {// determine if it need to shuffle or not.
			// this is the easy game
			for (int i = 0; i < 8; i++) {
				buttons[i].addActionListener(this);
				centerPanel.add(buttons[beingOrder[i]]);
				buttons[i].setVisible(true);
				position[i + 1] = beingOrder[i] + 1;
			}

		} else {
			shuffle(beingOrder); // shuffle array
			for (int i = 0; i < 8; i++) {
				Random rand = new Random();
				int randomNum = rand.nextInt(8); // random number from 0-7
				if (i == randomNum) {
					centerPanel.add(emptybutton); // add an empty button at
													// a random location
					randomNum = 100; // random number outside of random number
										// range so it doesn't get used again
					position[i] = 0; // setting the position of the random empty
										// button
					i--; // reiterate back into the for loop
				} else {
					
					position[i + 1] = beingOrder[i] + 1;
					buttons[i].addActionListener(this);
					centerPanel.add(buttons[beingOrder[i]]);
					buttons[i].setVisible(true);
				}
			}
		}

		validate();

		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// action for swapping buttons on the frame
	public void actionPerformed(ActionEvent event) {

		JButton button = (JButton) event.getSource();
		Dimension size = button.getSize();
		int emptyX = emptybutton.getX();
		int emptyY = emptybutton.getY();
		int buttonX = button.getX();
		int buttonY = button.getY();
		int buttonPosX = buttonX / size.width;
		int buttonPosY = buttonY / size.height;
		int buttonIndex = frame[buttonPosY][buttonPosX];

		if (emptyX == buttonX && (emptyY - buttonY) == size.height) {
			int labelIndex = buttonIndex + 3;
			centerPanel.remove(buttonIndex);
			centerPanel.add(emptybutton, buttonIndex);
			centerPanel.add(button, labelIndex);
			centerPanel.validate();
			int a = position[buttonIndex];
			position[buttonIndex] = position[labelIndex];
			position[labelIndex] = a;
		}
		if (emptyX == buttonX && (emptyY - buttonY) == -size.height) {
			int labelIndex = buttonIndex - 3;
			centerPanel.remove(labelIndex);
			centerPanel.add(button, labelIndex);
			centerPanel.add(emptybutton, buttonIndex);
			centerPanel.validate();
			int a = position[buttonIndex];
			position[buttonIndex] = position[labelIndex];
			position[labelIndex] = a;
		}
		if (emptyY == buttonY && (emptyX - buttonX) == size.width) {
			int labelIndex = buttonIndex + 1;
			centerPanel.remove(buttonIndex);
			centerPanel.add(emptybutton, buttonIndex);
			centerPanel.add(button, labelIndex);
			centerPanel.validate();
			int a = position[buttonIndex];
			position[buttonIndex] = position[labelIndex];
			position[labelIndex] = a;
		}
		if (emptyY == buttonY && (emptyX - buttonX) == -size.width) {
			int labelIndex = buttonIndex - 1;
			centerPanel.remove(buttonIndex);
			centerPanel.add(emptybutton, labelIndex);
			centerPanel.add(button, labelIndex);
			centerPanel.validate();
			int a = position[buttonIndex];
			position[buttonIndex] = position[labelIndex];
			position[labelIndex] = a;
		}
		System.out.println();
		for (int i = 0; i < 9; i++) {
			System.out.println("position:" + position[i]);
		}
		// checks that the buttons of the frame are in this order to win the
		// game.
		if (position[0] == 1 && position[1] == 2 && position[2] == 3
				&& position[3] == 4 && position[4] == 5 && position[5] == 6
				&& position[6] == 7 && position[7] == 8) {

			JOptionPane.showMessageDialog(this, "Good job! Hit ok to scramble");
			centerPanel.removeAll();// removes previous buttons to add new game
			shufflecount++;// counter for number of games being done
			gametime();

		}
	}

	public static void main(String[] args) {
		new JEightPuzzleFrame("Eight Puzzle Frame", "fgcu_logo.png");

	}

}
