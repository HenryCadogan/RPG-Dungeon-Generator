package generator;

import java.util.ArrayList;

public class PathFinding {
	private ArrayList <Node> openList;
	public ArrayList <Node> closedList;
	private ArrayList <Node> startPoints;
	private ArrayList <Node> endPoints;
	private boolean found = false;
	
	private int iterations = 1;
	private Node current;
	private Node start;
	private Node end;
	
	private void createStartPoints (Dungeon root){
		
		startPoints = new ArrayList<Node>();
		
		for(Dungeon r : root.rooms){
			
			int startPointX = r.startX + (root.minRoomSize / 2);
			int startPointY = r.startY + (root.minRoomSize / 2);

			Node startPoint = new Node (startPointY, startPointX);
			
			if(iterations == root.rooms.size()){
				break;
			}
			
			startPoints.add(startPoint);
			
			iterations++;
				
		}	
	}
	
	private void createEndPoints (Dungeon root){
		
		endPoints = new ArrayList<Node>();
		
		for(Dungeon r : root.rooms){
			
			int endPointX = 0;
			int endPointY = 0;
			int control = 10000;
			
			for(int i = 0; i < root.rooms.size(); i++){
				
				if(root.rooms.indexOf(r) == i){
					continue;
				}
				
				if (root.rooms.get(i).isConnected){
					continue;
				}
				
				int tempVal = (Math.abs (r.startX - root.rooms.get(i).startX)) + (Math.abs(r.startY - root.rooms.get(i).startY));

				if (tempVal < control){
					control = tempVal;

					endPointX = root.rooms.get(i).startX + (root.minRoomSize / 2);
					endPointY = root.rooms.get(i).startY + (root.minRoomSize / 2);
				}
			}

			r.isConnected = true;

			Node endPoint = new Node (endPointY, endPointX);
			endPoints.add(endPoint);

		}
	}

	private void getNeighbours(Dungeon root){
		Node north;
		Node east;
		Node south;
		Node west;


		if(current.y-1 < 0){
			north = null;
		} else{
		north = new Node (current.y -1, current.x);

		north.h = (Math.abs (north.x - end.x)) + (Math.abs(north.y - end.y));

		north.g = current.g + 1;

		north.f = north.h + north.g;

		openList.add(north);
		}

		if(current.x+1 > root.width){
			east = null;
		} else{
		east = new Node (current.y, current.x + 1);

		east.h = Math.abs (east.x - end.x) + Math.abs(east.y - end.y);

		east.g = current.g + 1;

		east.f = east.h + east.g;
		openList.add(east);
		}

		if(current.y+1 > root.height){
			south = null;
		} else{
		south = new Node (current.y +1, current.x);

		south.h = Math.abs (south.x - end.x) + Math.abs(south.y - end.y);

		south.g = current.g + 1;

		south.f = south.h + south.g;

		openList.add(south);
		}

		if(current.x-1 < 0){
			west = null;
		} else{
		west = new Node (current.y, current.x - 1);

		west.h = Math.abs (west.x - end.x) + Math.abs(west.y - end.y);

		west.g = current.g + 1;

		west.f = west.h + west.g;

		openList.add(west);
		}
	}

	public void createPaths(Dungeon root){
		createStartPoints(root);
		createEndPoints(root);
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();

		for (int i = 0; i < root.rooms.size(); i++){
			
			openList.clear();
			found = false;
			
			if(i >= startPoints.size()){
				break;
			}
			
			start = startPoints.get(i);
			
			end = (endPoints.get(i));
	
			closedList.add(start);
			
			current = start;
			
			while(!found){
				getNeighbours(root);
				
				Node temp = new Node (0,0);
				temp.f = 10000;
				
				for(Node n : openList){
					if(n.f < temp.f){
						temp = n;
					}
				}
				
				closedList.add(temp);
				current = temp;
				
				openList.clear();
				
				if(current.x == end.x && current.y == end.y){
					found = true;
				}
				
			}
		}
	}
}
