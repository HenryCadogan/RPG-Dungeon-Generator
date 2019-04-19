package generator;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Creates various generator.Monster objects filling in params from the generator.Monster class
 * Creates lists for each 'theme' and adds the monsters to their respective themes
 *
 */

public class Bestiary {
	public Bestiary(){
		addMonsterThemes();
	}
	private Monster kuotoa = new Monster("Kuo-Toa", "Bite", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.25, 18, 3, 13, 199);
	private Monster merfolk = new Monster("Merfolk", "Spear", "1d6/1d8(2h)", 0.125, 11, 2, 11, 218);
    private Monster sahuagin = new Monster("Sahuagin", "Bite/Claws", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.5, 22, 3, 12, 263);
    private Monster bullywug = new Monster("Bullywug", "Bite", "1d4 + 1", "Spear", "1d6 + 1/1d8 + 1 (2h)", 0.25, 11, 3, 15, 35);
    public ArrayList<Monster> aquatic = new ArrayList<Monster>();

    private Monster goblin = new Monster("Goblin", "Scimitar", "1d6 + 2", "Shortbow", "1d6 + 2", 0.25, 7, 4, 15, 166);
    private Monster goblinBoss = new Monster("Goblin Boss", "Scimitar (2 attacks)", "1d6 + 2", "Javelin", "1d6 + 2", 1, 21, 4, 17, 166);
    private Monster grimlock = new Monster("Grimlock", "Spiked Bone Club", "1d4 + 3", 0.25, 11, 5, 11, 175);
    private Monster halfOgre = new Monster("Half-Ogre", "Battleaxe", "2d8 + 3", "Javelin", "2d6 + 3", 1, 30, 5, 12, 238);
	private Monster orc = new Monster("Orc", "Greataxe", "1d12 + 3", "Javelin", "1d6 + 3", 0.5, 15, 5, 13, 246);
	public ArrayList<Monster> humanoid = new ArrayList<Monster>();

	private Monster needleBlight = new Monster("Needle Blight", "Claws", "2d4 + 1", "Needles", "2d6 + 1", 0.25, 11, 3, 12, 32);
	private Monster twigBlight = new Monster("Twig Blight", "Claws", "1d4 + 1", 0.125, 4, 3, 13, 32);
	private Monster vineBlight = new Monster("Vine Blight", "Constrict", "2d6 + 2", "Entangling Plants (Recharge 5-6)", "special, see generator.Monster Manual page", 0.5, 26, 4, 12, 32);
	private Monster dryad = new Monster("Dryad", "Club", "1d4/1d8 + 4 (with shillelagh)", "Fey Charm", "special, see generator.Monster Manual page", 1, 22, 2, 11, 121);
	private Monster quaggoth = new Monster("Quaggoth Spore Servant", "Claw (2 attacks)", "1d6 + 3", 1, 45, 5, 13, 230);
	private Monster myconid = new Monster("Myconid Adult", "Fist", "2d4", "2x special attacks", "see generator.Monster Manual page for special attacks", 0.5, 22, 2, 12, 232);
	public ArrayList<Monster> forestry = new ArrayList<Monster>();

	private Monster kobold = new Monster("Kobold", "Dagger", "1d4 + 2", "Sling", "1d4 + 2", 0.125, 5, 4, 12, 195);
	private Monster wingKobold = new Monster("Winged Kobold", "Dagger", "1d4 + 3", "Dropped Rock", "1d6 + 3", 0.25, 7, 5, 13, 195);
	private Monster brassWyrmling = new Monster("Brass Dragon Wyrmling", "Bite", "1d10 + 2", "Breath Weapons", "see generator.Monster Manual page for special attacks", 1, 16, 4, 16, 106);
	private Monster copperWyrmling = new Monster("Copper Dragon Wyrmling", "Bite", "1d10 + 2", "Breath Weapons", "see generator.Monster Manual page for special attacks", 1, 22, 4, 16, 112);
	private Monster pseudoDragon = new Monster("Pseudodragon", "Bite", "1d4 + 2", "Sting", "1d4 + 2", 0.25, 7, 4, 13, 254);
 	public ArrayList<Monster> dragon = new ArrayList<Monster>();

	private Monster ghoul = new Monster("Ghoul", "Bite", "2d6 + 2", "Claws", "2d4 + 2", 1, 22, 4, 12, 148);
	private Monster skeleton = new Monster("Skeleton", "Shortsword", "1d6 + 2", "Shortbow", "1d6 + 2", 0.25, 13, 4, 13, 272);
	private Monster specter = new Monster("Specter", "Life Drain", "3d6", 1, 22, 4, 12, 279);
	private Monster zombie = new Monster("Zombie", "Slam", "1d6 + 1", 0.25, 22, 3, 8, 316);
	
	public ArrayList<Monster> undead = new ArrayList<Monster>();
	
	public ArrayList<Monster> random = new ArrayList<Monster>();
    
   	private void addMonsterThemes(){
    	aquatic.addAll(Arrays.asList(merfolk,sahuagin,bullywug));
    	humanoid.addAll(Arrays.asList(goblin,goblinBoss,grimlock,halfOgre,orc));
    	forestry.addAll(Arrays.asList(needleBlight,twigBlight,vineBlight,dryad,quaggoth,myconid));
    	dragon.addAll(Arrays.asList(kobold,wingKobold,brassWyrmling,copperWyrmling,pseudoDragon));

    	undead.addAll(Arrays.asList(ghoul,skeleton,specter,zombie));

		random.addAll(aquatic);

		random.addAll(humanoid);

		random.addAll(forestry);

		random.addAll(dragon);

		random.addAll(undead);
    }
	
}
