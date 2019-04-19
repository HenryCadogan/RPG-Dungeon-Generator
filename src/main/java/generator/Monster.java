package generator;

public class Monster {

	private String name;
	private String firstAttackName;
	private String firstDamage;
	private String secondAttackName;
	private String secondDamage;

	private double combatRating;

	private int hp;
	private int attBonus;
	private int ac;
	private int pageNumber;
	
	/**
	 * Constructor used for creating a new monster. Use this one if it has a second attack.
	 * 
	 * @param name - Name of the monster e.g Goblin
	 * @param firstAttackName - Name of first attack e.g Longsword
	 * @param firstDamage - Damage first attack does in String format e.g 1d4
	 * @param secondAttackName - Name of second attack
	 * @param secondDamage - Second attack damage
	 * @param cr - Challenge/Combat rating for monster
	 * @param hp - Health Points of generator.Monster
	 * @param attBonus - generator.Monster's attack bonus
	 * @param ac - Armour class of monster
	 * @param pageNumber - What page in the generator.Monster Manual you find the monster
	 */
	Monster(String name, String firstAttackName, String firstDamage, String secondAttackName,
			String secondDamage, double cr, int hp, int attBonus, int ac, int pageNumber){
		
		this.name = name;
		this.firstAttackName = firstAttackName;
		this.firstDamage = firstDamage;
		this.secondAttackName = secondAttackName;
		this.secondDamage = secondDamage;
		this.combatRating = cr;
		this.hp = hp;
		this.attBonus = attBonus;
		this.ac = ac;
		this.pageNumber = pageNumber;
	}
	
	/**
	 * Constructor used for creating a new monster. Use this one if it does not have a second attack.
	 * See previous constructor for parameter details
	 * 
	 * @param name
	 * @param firstAttackName
	 * @param firstDamage
	 * @param cr
	 * @param hp
	 * @param attBonus
	 * @param ac
	 * @param pageNumber
	 */
	Monster (String name, String firstAttackName, String firstDamage, double cr, int hp, int attBonus, int ac, int pageNumber){

		this.name = name;
		this.firstAttackName = firstAttackName;
		this.firstDamage = firstDamage;
		this.combatRating = cr;
		this.hp = hp;
		this.attBonus = attBonus;
		this.ac = ac;
		this.pageNumber = pageNumber;
		
		this.secondAttackName = null;
		this.secondDamage = null;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFirstAttackName(){
		return firstAttackName;
	}
	
	public String getFirstAttackDamage(){
		return firstDamage;
	}
	
	public String getSecondAttackName(){
		return secondAttackName;
	}
	
	public String getSecondAttackDamage(){
		return secondDamage;
	}
	
	public double getCombatRating(){
		return combatRating;
	}
	
	public int getHealth(){
		return hp;
	}

	public int getAttackBonus(){
		return attBonus;
	}
	
	public int getArmour(){
		return ac;
	}
	
	public int getPageNum(){
		return pageNumber;
	}

}
