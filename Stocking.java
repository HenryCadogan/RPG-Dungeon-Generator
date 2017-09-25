package dungeonGenerator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import bestiary.Monster;
import bestiary.Bestiary;

public class Stocking {

	private String roomType;
	private boolean isTreasure;
	private Random rng = new Random();
	private ArrayList<Monster> selectedTheme;
	private String combatRating;
	private int roomCount = 1;
	
	Characters characters = new Characters();
	
	private void determineRoomType(){
		int typeSelection = rng.nextInt(6);
		
		switch(typeSelection){
		case 0: roomType = "Monster";
				break;
		case 1: roomType = "Monster";
				break;
		case 2: roomType = "Trap";
				break;
		case 3: roomType = "Special";
				break;
		case 4: roomType = "Empty";
				break;
		case 5: roomType = "Empty";
				break;
		}
	}
	
	private void addTreasure(){
		int treasureRoll = rng.nextInt(6);
		
		switch(treasureRoll){
		case 0: isTreasure = true;
				break;
		case 1: 
				if(roomType == "Empty"){
					isTreasure = false;
				} else{
					isTreasure = true;
				}
				break;
		case 2: 
				if(roomType == "Monster"){
					isTreasure = true;
				} else {
					isTreasure = false;
				}
				break;
		case 3: isTreasure = false;
				break;
		case 4: isTreasure = false;
				break;
		case 5: isTreasure = false;
				break;
		}
	}
	
	private void themeSelection(){
		Bestiary bestiary = new Bestiary();
		bestiary.addMonsterThemes();
		
		int themeSelect = rng.nextInt(5);
		
		switch(themeSelect){
		case 0: selectedTheme = new ArrayList<Monster>(bestiary.aquatic);
				break;
		case 1: selectedTheme = new ArrayList<Monster>(bestiary.humanoid);	
				break;
		case 2: selectedTheme = new ArrayList<Monster>(bestiary.forestry);	
				break;
		case 3: selectedTheme = new ArrayList<Monster>(bestiary.dragon);	
				break;
		case 4: selectedTheme = new ArrayList<Monster>(bestiary.undead);	
				break;
		case 5: selectedTheme = new ArrayList<Monster>(bestiary.random);	
				break;
		}
	}
	
	private void monsterPlacement(PrintWriter write){
		int selectMonster = rng.nextInt(selectedTheme.size());
		
		Monster chosenMonster = selectedTheme.get(selectMonster);
		
		int numberOfMonsters = (int) (1/chosenMonster.getCombatRating());
		
		convertCR(chosenMonster.getCombatRating());
		
		if(isAlarm()){
			write.println("This monster will raise the alarm; an attack on this room will generate an attack from monsters (if any) in adjacent rooms.");
			write.println();
		}
		
		write.println("MONSTER: " + numberOfMonsters + "x " + chosenMonster.getName() + "; ");
		write.print("STATS: " + "CR: " + combatRating + "; ");
		write.print("HP: " + chosenMonster.getHealth() + "; ");
		write.print("AC: " + chosenMonster.getArmour() + "; ");
		write.println();
		write.print("COMBAT: " + "ATTACK BONUS: " + chosenMonster.getAttackBonus() + "; ");
		write.print(chosenMonster.getFirstAttackName() + ": " + chosenMonster.getFirstAttackDamage() + "; ");
		if(chosenMonster.getSecondAttackName() != null){
			write.print(chosenMonster.getSecondAttackName() + ": " + chosenMonster.getSecondAttackDamage() + "; ");
		}
		write.println();
		write.print("More information can be found about the monster on page " + chosenMonster.getPageNum() + " in the Monster Manual (5th Edition)");
		write.println();
	}
	
	private void convertCR(double decimal){
		int denominator = (int)( 1 / decimal);
		
		if(denominator == 1){
			combatRating = "1";
		} else{
			combatRating = "1/" + denominator;
		}
	}
	
	private void trapPlacement(PrintWriter write){
		write.println("This room is trapped!");
		write.print("More information can be found about the possible traps on pages 120-123 in the Dungeon Masters Guide (5th Edition)");
		write.println();
	}
	
	private void specialPlacement(PrintWriter write){
		write.println("There is something special about this room.");
		write.print("The room could have a puzzle, riddle, interesting features or an unusual effect");
		write.println();
	}
	
	private void playerPlacement(PrintWriter write){
		
		for (int i = 0; i < characters.races.size(); i++){
			if (roomCount == characters.racesRoomNumber.get(i)){
				write.println();
				write.println("There is something for the " + characters.races.get(i) + " within this room");
			}
		}
		
		for (int i = 0; i < characters.classes.size(); i++){
			if (roomCount == characters.classesRoomNumber.get(i)){
				write.println();
				write.println("There is something for the " + characters.classes.get(i) + " within this room");
			}
		}
	}
	
	public Characters getCharacters(){
		return characters;
	}
	
	private Boolean isAlarm(){
		int alarmRoll = rng.nextInt(4);
		boolean isAlarm = false;
		
		switch(alarmRoll){
		case 0: isAlarm = true;
				break;
		case 1: isAlarm = false;
				break;
		case 2: isAlarm = false;
				break;
		
		case 3: isAlarm = false;
				break;
		}
		
		return isAlarm;
	}
	
	public void stock(Dungeon root){
		File file = new File ("RoomDescription.txt");
		
		themeSelection();
		
		try{
		
			PrintWriter write = new PrintWriter(file);
			
			write.println();
			
			for(Dungeon r : root.rooms){
				determineRoomType();
				addTreasure();
				
				write.println("Room " + roomCount + ":");
				write.println("Room Type: " + roomType);
				
				if(roomType == "Monster"){
					monsterPlacement(write);
				} else if (roomType == "Trap"){
					trapPlacement(write);
				} else if (roomType == "Special"){
					specialPlacement(write);
				}
				
				if(isTreasure){
					write.println();
					write.println("There is treasure to be found within this room.");
					write.print("Treasure tables can be found in chapter 7 (p.137) of the Dungeon Master's Guide (5th Edition)");
					write.println();
				}
				
				playerPlacement(write);
				
				write.println();
				
				roomCount++;
			}
			
			roomCount = 1;
			
			write.close();
		
		} catch (IOException e){
			
		}
	}
	
}
