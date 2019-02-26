package generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class Dungeon {
	int startX;
	int startY;
	int endX;
	int endY;
	int width;
	int height;
	int minRoomSize = 60;
	Dungeon leftChild;
	Dungeon rightChild;
	Dungeon room;

	Boolean isConnected;
	
	ArrayList <Dungeon> dungeons;
	ArrayList <Dungeon> rooms;
	
	private Random rng = new Random();
	
	//Used for creating dungeon and all split sections
	Dungeon (int startX, int startY, int endX, int endY){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
		this.width = endX - startX;
		this.height = endY - startY;
		
	}

	
	private boolean split(){
		
		//already split
		if (leftChild != null){
			return false;
		}
		
		//choose direction for split
		boolean horizontal;
		
		if (height > width){
			horizontal = true;
		} else if (width > height){
			horizontal = false;
		}
		else {
			horizontal = rng.nextBoolean();
		}

		int minimum = 75;
		int maxSplit = (horizontal ? endY : endX) - minimum;
		if(maxSplit <= minimum){
			return false;
		}
		
		int minSplit = (horizontal ? startY : startX) + minimum;
		
		if(minSplit >= maxSplit){
			return false;
		}
		
		//choose location of split
		int splitLocation = ThreadLocalRandom.current().nextInt(minSplit, maxSplit);
		
		if(horizontal){
			leftChild = new Dungeon (startX, startY, endX, splitLocation);
			rightChild = new Dungeon (startX, splitLocation, endX, endY);
			
		} else {
			leftChild = new Dungeon (startX, startY, splitLocation, endY);
			rightChild = new Dungeon (splitLocation, startY, endX, endY);
			
		}
		
		return true;
		
	}
	
	void makeLeafs(Dungeon root){
		dungeons = new ArrayList<>();
	
		dungeons.add(root);
		
		//makes 20 'leaves' 
		while (dungeons.size() < 39){
			int rngIndex = rng.nextInt(dungeons.size());
			Dungeon splitting = dungeons.get(rngIndex);
			
			if(splitting.split()){
				dungeons.add(splitting.leftChild);
				dungeons.add(splitting.rightChild);
			}
		}
		
	}
	
	//create rooms within the selected leaf (see first part of drawDungeon below)
	void  generateRooms (){
		rooms = new ArrayList<>();
		
		if (leftChild != null){
			leftChild.generateRooms();
			rightChild.generateRooms();
		} else {
			int roomStartX = ThreadLocalRandom.current().nextInt(startX, endX);
			int minRoomSize = 60;
			if(roomStartX + minRoomSize > endX){
				roomStartX = endX - minRoomSize;
			}
			int roomStartY = ThreadLocalRandom.current().nextInt(startY, endY);
			if(roomStartY + minRoomSize > endY){
				roomStartY = endY - minRoomSize;
			}
			int roomEndX = ThreadLocalRandom.current().nextInt (roomStartX, endX);
			if (roomEndX - minRoomSize < roomStartX){
				roomEndX = roomStartX + minRoomSize;
			}
			int roomEndY = ThreadLocalRandom.current().nextInt (roomStartY, endY);
			if (roomEndY - minRoomSize < roomStartY){
				roomEndY = roomStartY + minRoomSize;
			};
			
			room = new Dungeon (roomStartX, roomStartY, roomEndX, roomEndY);
		}
		
	}
	
	void drawDungeon(Dungeon root){
		//For each element in the dungeons array list call generateRooms which will add a room for the leaves
		for (Dungeon r : dungeons){
			if (r.room != null){
				rooms.add(r.room);
			}
		}
		
		int[][] dungeon = new int [root.endY][root.endX];
		
		for(int i = 0; i < root.endY; i++){
			for(int j = 0; j < root.endX; j++){
				dungeon[i][j] = 0;
			}
		}
		
		//numbering the rooms
		int roomCount = 1;
		
		for (Dungeon r: rooms){
			for(int i = 0; i < r.height; i++){
				for(int j = 0; j < r.width; j++){
					dungeon[r.startY + i][r.startX + j] = roomCount;
				}
			}
			roomCount++;
		}
		
		//Create paths between rooms
		PathFinding path = new PathFinding();
		path.createPaths(root);

		for(Node n : path.closedList){
			for(int i = 0; i < root.endY; i++){
				for(int j = 0; j < root.endX; j++){
					dungeon[n.y][n.x] = -1;
				}
			}
		}
		
		saveDungeon(root, dungeon);
	}
	
	//Method for drawing the dungeon and saving as an image
	private void saveDungeon(Dungeon root, int[][] dungeon){
		BufferedImage image = new BufferedImage(root.width + 100, root.height + 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		
		for(int i =0; i<root.endY; i++){
			for(int j=0; j<root.endX; j++){
				if(dungeon[i][j] == 0){
				g2.setColor(Color.black);
				g2.fillRect(j, i, 10, 10);
			} else {
				g2.setColor(Color.white);
				g2.fillRect(j, i, 10, 10);
				}
			}
		}
		
		int roomCount = 1;
		
		for(Dungeon r : root.rooms){
			g2.setColor(Color.black);
			g2.drawString("" + roomCount, r.startX + 25, r.startY + 25);
			roomCount++;
		}
		
		try{
			ImageIO.write(image, "png", new File("generator.Dungeon.png"));
		}catch(IOException e){
			System.err.println("Could not save image");
		}
	}
	
		
	public static void main (String[] args){
		GenerateWindow window = new GenerateWindow();
		
		window.window();
	}
	
	
}