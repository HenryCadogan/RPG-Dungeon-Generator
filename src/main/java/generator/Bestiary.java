package generator;

import java.util.ArrayList;

/**
 * Creates various generator.Monster objects filling in params from the generator.Monster class
 * Creates lists for each 'theme' and adds the monsters to their respective themes
 *
 */

public class Bestiary {
	
	Monster kuotoa = new Monster("Kuo-Toa", "Bite", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.25, 18, 3, 13, 199);
	Monster merfolk = new Monster("Merfolk", "Spear", "1d6/1d8(2h)", 0.125, 11, 2, 11, 218);
    Monster sahuagin = new Monster("Sahuagin", "Bite/Claws", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.5, 22, 3, 12, 263);
    Monster bullywug = new Monster("Bullywug", "Bite", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.25, 11, 3, 15, 35);
    
    public ArrayList<Monster> aquatic = new ArrayList<Monster>();
    
    Monster goblin = new Monster("Goblin", "Scimitar", "1d6 + 2", "Shortbow", "1d6 + 2", 0.25, 7, 4, 15, 166);
    Monster goblinBoss = new Monster("Goblin Boss", "Scimitar (2 attacks)", "1d6 + 2", "Javelin", "1d6 + 2", 1, 21, 4, 17, 166);
    Monster grimlock = new Monster("Grimlock", "Spiked Bone Club", "1d4 + 3", 0.25, 11, 5, 11, 175);
    Monster halfOgre = new Monster("Half-Ogre", "Battleaxe", "2d8 + 3", "Javelin", "2d6 + 3", 1, 30, 5, 12, 238);
	Monster orc = new Monster("Orc", "Greataxe", "1d12 + 3", "Javelin", "1d6 + 3", 0.5, 15, 5, 13, 246);
	
	public ArrayList<Monster> humanoid = new ArrayList<Monster>();
	
	Monster needleBlight = new Monster("Needle Blight", "Claws", "2d4 + 1", "Needles", "2d6 + 1", 0.25, 11, 3, 12, 32);
	Monster twigBlight = new Monster("Twig Blight", "Claws", "1d4 + 1", 0.125, 4, 3, 13, 32);
	Monster vineBlight = new Monster("Vine Blight", "Constrict", "2d6 + 2", "Entangling Plants (Recharge 5-6)", "special, see generator.Monster Manual page", 0.5, 26, 4, 12, 32);
	Monster dryad = new Monster("Dryad", "Club", "1d4/1d8 + 4 (with shillelagh)", "Fey Charm", "special, see generator.Monster Manual page", 1, 22, 2, 11, 121);
	Monster quaggoth = new Monster("Quaggoth Spore Servant", "Claw (2 attacks)", "1d6 + 3", 1, 45, 5, 13, 230);
	Monster myconid = new Monster("Myconid Adult", "Fist", "2d4", "2x special attacks", "see generator.Monster Manual page for special attacks", 0.5, 22, 2, 12, 232);
	
	public ArrayList<Monster> forestry = new ArrayList<Monster>();
	
	Monster kobold = new Monster("Kobold", "Dagger", "1d4 + 2", "Sling", "1d4 + 2", 0.125, 5, 4, 12, 195);
	Monster wingKobold = new Monster("Winged Kobold", "Dagger", "1d4 + 3", "Dropped Rock", "1d6 + 3", 0.25, 7, 5, 13, 195);
	Monster brassWyrmling = new Monster("Brass Dragon Wyrmling", "Bite", "1d10 + 2", "Breath Weapons", "see generator.Monster Manual page for special attacks", 1, 16, 4, 16, 106);
	Monster copperWyrmling = new Monster("Copper Dragon Wyrmling", "Bite", "1d10 + 2", "Breath Weapons", "see generator.Monster Manual page for special attacks", 1, 22, 4, 16, 112);
	Monster pseudoDragon = new Monster("Pseudodragon", "Bite", "1d4 + 2", "Sting", "1d4 + 2", 0.25, 7, 4, 13, 254);

	public ArrayList<Monster> dragon = new ArrayList<Monster>();
	
	Monster ghoul = new Monster("Ghoul", "Bite", "2d6 + 2", "Claws", "2d4 + 2", 1, 22, 4, 12, 148);
	Monster skeleton = new Monster("Skeleton", "Shortsword", "1d6 + 2", "Shortbow", "1d6 + 2", 0.25, 13, 4, 13, 272);
	Monster specter = new Monster("Specter", "Life Drain", "3d6", 1, 22, 4, 12, 279);
	Monster zombie = new Monster("Zombie", "Slam", "1d6 + 1", 0.25, 22, 3, 8, 316);
	
	public ArrayList<Monster> undead = new ArrayList<Monster>();
	
	public ArrayList<Monster> random = new ArrayList<Monster>();
    
    public void addMonsterThemes(){
    	aquatic.add(kuotoa);
    	aquatic.add(merfolk);
    	aquatic.add(sahuagin);
    	aquatic.add(bullywug);
    	
    	humanoid.add(goblin);
    	humanoid.add(goblinBoss);
    	humanoid.add(grimlock);
    	humanoid.add(halfOgre);
    	humanoid.add(orc);
    	
    	forestry.add(needleBlight);
    	forestry.add(twigBlight);
    	forestry.add(vineBlight);
    	forestry.add(dryad);
    	forestry.add(quaggoth);
    	forestry.add(myconid);
    	
    	dragon.add(kobold);
    	dragon.add(wingKobold);
    	dragon.add(brassWyrmling);
    	dragon.add(copperWyrmling);
    	dragon.add(pseudoDragon);
    	
    	undead.add(ghoul);
    	undead.add(skeleton);
    	undead.add(specter);
    	undead.add(zombie);
    	
    	for(Monster m : aquatic){
    		random.add(m);
    	}
    	
    	for(Monster m : humanoid){
    		random.add(m);
    	}
    	
    	for(Monster m : forestry){
    		random.add(m);
    	}
    	
    	for(Monster m : dragon){
    		random.add(m);
    	}
    	
    	for(Monster m : undead){
    		random.add(m);
    	}
    }
	
}
