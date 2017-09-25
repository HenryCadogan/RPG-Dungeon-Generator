package dungeonGenerator;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class GenerateWindow {
	private JTextField raceTextField;
	private JTextField classTextField;
	Stocking stock = new Stocking();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void window(){
		JFrame generateWindow = new JFrame();
		generateWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		generateWindow.setSize(500, 600);
		generateWindow.setLocationRelativeTo(null);
		
		JPanel welcomePanel = new JPanel();
		generateWindow.getContentPane().add(welcomePanel, BorderLayout.CENTER);
		welcomePanel.setLayout(null);
		welcomePanel.setBackground(Color.white);
		
		JButton genDungeonBtn = new JButton("Generate Dungeon");
		genDungeonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Dungeon root = new Dungeon (0,0,800,800);
				
				root.makeLeafs(root);
				root.generateRooms();

				root.drawDungeon(root);
				
				stock.getCharacters().roomLists();
				
				stock.stock(root);
				
				stock.getCharacters().racesRoomNumber.clear();
				stock.getCharacters().classesRoomNumber.clear();
				
				HtmlCreator html = new HtmlCreator();
				html.writeToHtml(root);
				
				try{
				File htmlFile = new File ("Dungeon.html");
				Desktop.getDesktop().open(htmlFile);
				}catch(IOException e){
					System.err.println(e.getMessage());
				}
			}
		});
		genDungeonBtn.setBounds(131, 461, 236, 55);
		welcomePanel.add(genDungeonBtn);
		
		JLabel lblDungeonGenerator = new JLabel("Dungeon Generator");
		lblDungeonGenerator.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblDungeonGenerator.setBounds(131, 11, 223, 75);
		welcomePanel.add(lblDungeonGenerator);
		
		JLabel lblPlayerRace = new JLabel("Player Race:");
		lblPlayerRace.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayerRace.setBounds(71, 121, 124, 36);
		welcomePanel.add(lblPlayerRace);
		
		JLabel lblPlayerClass = new JLabel("Player Class:");
		lblPlayerClass.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlayerClass.setBounds(71, 272, 124, 43);
		welcomePanel.add(lblPlayerClass);
		
		
		//Textfields for player information
		raceTextField = new JTextField();
		raceTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		raceTextField.setBounds(226, 129, 199, 20);
		welcomePanel.add(raceTextField);
		raceTextField.setColumns(10);
		
		classTextField = new JTextField();
		classTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		classTextField.setBounds(226, 283, 199, 20);
		welcomePanel.add(classTextField);
		classTextField.setColumns(10);
		
		
		//Add to the lists buttons
		JButton addRaceBtn = new JButton("Add Race");
		addRaceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String playerRace = raceTextField.getText().trim();
				
				if(playerRace.equals("")){
					JOptionPane.showMessageDialog(null, "Text field cannot be blank", "Input error", JOptionPane.ERROR_MESSAGE);
				} else{
				
				stock.getCharacters().races.add(playerRace);
				
				JOptionPane.showMessageDialog(null, "Successfully added " + playerRace + "\n" + "Number of races stored: " + stock.getCharacters().races.size());
				
				raceTextField.setText("");
				
				}
			}
		});
		addRaceBtn.setBounds(100, 178, 95, 36);
		welcomePanel.add(addRaceBtn);
		
		JButton addClassBtn = new JButton("Add Class");
		addClassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String playerClass = classTextField.getText().trim();
				
				if (playerClass.equals("")){
					JOptionPane.showMessageDialog(null, "Text field cannot be blank", "Input error", JOptionPane.ERROR_MESSAGE);
				} else {
				
				stock.getCharacters().classes.add(playerClass);
				
				JOptionPane.showMessageDialog(null, "Successfully added " + playerClass + "\n" + "Number of classes stored: " + stock.getCharacters().classes.size());
				
				classTextField.setText("");
				
				}
			}
		});
		addClassBtn.setBounds(95, 326, 100, 36);
		welcomePanel.add(addClassBtn);
		
		
		//Clear lists buttons
		JButton clearRacesBtn = new JButton("Clear Saved Races");
		clearRacesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stock.getCharacters().races.clear();
				
				JOptionPane.showMessageDialog(null, "Successfully cleared Race list");
			}
		});
		clearRacesBtn.setBounds(237, 178, 157, 36);
		welcomePanel.add(clearRacesBtn);
		
		JButton clearClassesBtn = new JButton("Clear Saved Classes");
		clearClassesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stock.getCharacters().classes.clear();
				
				JOptionPane.showMessageDialog(null, "Successfully cleared Class list");
			}
		});
		clearClassesBtn.setBounds(237, 326, 157, 36);
		welcomePanel.add(clearClassesBtn);
		
		generateWindow.setVisible(true);
	}
}