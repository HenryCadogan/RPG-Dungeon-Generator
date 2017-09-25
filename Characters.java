package dungeonGenerator;

import java.util.ArrayList;
import java.util.Random;

public class Characters {
	public ArrayList<String> races = new ArrayList<String>();
	public ArrayList<String> classes = new ArrayList<String>();
	public ArrayList<Integer> racesRoomNumber = new ArrayList<Integer>();
	public ArrayList<Integer> classesRoomNumber = new ArrayList<Integer>();
	
	private Random rng = new Random();
	
	//Assign room numbers to each race and class
	public void roomLists(){
		for(int i = 0; i < races.size(); i++){
			int roomNumber = rng.nextInt(20) + 1;
			
			racesRoomNumber.add(roomNumber);
		}
		
		for(int i = 0; i < classes.size(); i++){
			int roomNumber = rng.nextInt(20) + 1;
			
			classesRoomNumber.add(roomNumber);
		}
	}
}
