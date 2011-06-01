package characterEditor;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import viewControl.MainFrame;

import animationEditor.Animations;
import characterEditor.exceptions.IllegalLevelNumber;
import characterEditor.exceptions.NullBufferedImage;
import editor.ObjectEditorSystem;

public abstract class Actors extends ObjectEditorSystem implements Serializable {

	private static final long serialVersionUID = 5903578689659669681L;

	protected int indexJob;
	protected int initLevel;
	protected int maxLevel;
	
	protected Abilities initAbility;
	protected Abilities growthCurve;
	
	protected Animations moveUpAnimation;
	protected Animations moveDownAnimation;
	protected Animations moveRightAnimation;
	protected Animations moveLeftAnimation;
	protected Animations battleMoveUpAni;
	protected Animations battleMoveDownAni;
	protected Animations battleMoveRightAni;
	protected Animations battleMoveLeftAni;
	protected Animations attackUpAnimation;
	protected Animations attackDownAnimation;
	protected Animations attackRightAnimation;
	protected Animations attackLeftAnimation;
	protected Animations damageUpAnimation;
	protected Animations damageDownAnimation;
	protected Animations damageRightAnimation;
	protected Animations damageLeftAnimation;
	protected Animations skillAnimation;
	protected Animations dieAnimation;
	protected List<Animations> eventAnimationList;
	
	protected int countEventAnimationList;			// eventAnimationList의 최대 원소의 수
	protected String defaultDataFolderPath;
	
	protected int indexInitWeapon;
	protected int indexInitTopArmor;
	protected int indexInitBottomsArmor;
	protected int indexInitHelmetArmor;
	protected int indexInitShieldArmor;
	protected int indexInitAccessoryArmor;
	
	protected int speed;
	
	
	public Actors(String projectPath, String folderName) {
		super(MainFrame.OWNER.ProjectFullPath, folderName);
		defaultDataFolderPath = projectPath+File.separator+".DefaultData";

		initLevel = 1;
		maxLevel = 99;
		countEventAnimationList = 10;

		// 능력치, 능력치 증가율 초기화
		initAbility = new Abilities();
		growthCurve = new Abilities(3, 3, 3, 3, 3, 3, 3, 3);
		
		// 애니메이션 초기화
		moveUpAnimation = new Animations("MoveUp", projectPath);
		moveDownAnimation = new Animations("MoveDown", projectPath);
		moveRightAnimation = new Animations("MoveRight", projectPath);
		moveLeftAnimation = new Animations("MoveLeft", projectPath);
		battleMoveUpAni = new Animations("BattleMoveUp", projectPath);
		battleMoveDownAni = new Animations("BattleMoveDown", projectPath);
		battleMoveRightAni = new Animations("BattleMoveRight", projectPath);
		battleMoveLeftAni = new Animations("BattleMoveLeft", projectPath);
		attackUpAnimation = new Animations("AtackUp", projectPath);
		attackDownAnimation = new Animations("AtackDown", projectPath);
		attackRightAnimation = new Animations("AtackRigh", projectPath);
		attackLeftAnimation = new Animations("AtackLeftt", projectPath);
		damageUpAnimation = new Animations("DamageUp", projectPath);
		damageDownAnimation = new Animations("DamageDown", projectPath);
		damageRightAnimation = new Animations("DamageRight", projectPath);
		damageLeftAnimation = new Animations("DamageLeft", projectPath);
		skillAnimation = new Animations("Skill", projectPath);
		dieAnimation = new Animations("Die", projectPath);
		eventAnimationList = new ArrayList<Animations>(countEventAnimationList);
		
		// 장비 초기화
		indexInitWeapon = 0;
		indexInitTopArmor = 0;
		indexInitBottomsArmor = 0;
		indexInitHelmetArmor = 0;
		indexInitShieldArmor = 0;
		indexInitAccessoryArmor = 0;
		
		// 이동 속도
		speed = 0;
		
		// 이미지 초기화
		initImgData();
	}
	
	public void initImgData() {
		try {
			// 각 Animations 변수를 Default 이미지로 설정.
			moveUpAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveUpBase.png");
			moveUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveUp00.png", 0);
			moveUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveUp01.png", 1);
			moveUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveUp02.png", 2);
			moveUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveUp03.png", 3);
			
			moveDownAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveDownBase.png");
			moveDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveDown00.png", 0);
			moveDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveDown01.png", 1);
			moveDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveDown02.png", 2);
			moveDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveDown03.png", 3);
			
			moveRightAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveRightBase.png");
			moveRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveRight00.png", 0);
			moveRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveRight01.png", 1);
			moveRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveRight02.png", 2);
			moveRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveRight03.png", 3);
			
			moveLeftAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveLeftBase.png");
			moveLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveLeft00.png", 0);
			moveLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveLeft01.png", 1);
			moveLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveLeft02.png", 2);
			moveLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "MoveLeft03.png", 3);
			
			battleMoveUpAni.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveUpBase.png");
			battleMoveUpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveUp00.png", 0);
			battleMoveUpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveUp01.png", 1);
			battleMoveUpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveUp02.png", 2);
			battleMoveUpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveUp03.png", 3);
			
			battleMoveDownAni.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveDownBase.png");
			battleMoveDownAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveDown00.png", 0);
			battleMoveDownAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveDown01.png", 1);
			battleMoveDownAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveDown02.png", 2);
			battleMoveDownAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveDown03.png", 3);
			
			battleMoveRightAni.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveRightBase.png");
			battleMoveRightAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveRight00.png", 0);
			battleMoveRightAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveRight01.png", 1);
			battleMoveRightAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveRight02.png", 2);
			battleMoveRightAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveRight03.png", 3);
			
			battleMoveLeftAni.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveLeftBase.png");
			battleMoveLeftAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveLeft00.png", 0);
			battleMoveLeftAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveLeft01.png", 1);
			battleMoveLeftAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveLeft02.png", 2);
			battleMoveLeftAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "BattleMoveLeft03.png", 3);
			
			attackUpAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackUpBase.png");
			attackUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackUp00.png", 0);
			attackUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackUp01.png", 1);
			attackUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackUp02.png", 2);
			attackUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackUp03.png", 3);
			
			attackDownAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackDownBase.png");
			attackDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackDown00.png", 0);
			attackDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackDown01.png", 1);
			attackDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackDown02.png", 2);
			attackDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackDown03.png", 3);
			
			attackRightAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackRightBase.png");
			attackRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackRight00.png", 0);
			attackRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackRight01.png", 1);
			attackRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackRight02.png", 2);
			attackRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackRight03.png", 3);
			
			attackLeftAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackLeftBase.png");
			attackLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackLeft00.png", 0);
			attackLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackLeft01.png", 1);
			attackLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackLeft02.png", 2);
			attackLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "AttackLeft03.png", 3);
			
			damageUpAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageUpBase.png");
			damageUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageUp00.png", 0);
			damageUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageUp01.png", 1);
			damageUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageUp02.png", 2);
			damageUpAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageUp03.png", 3);
			
			damageDownAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageDownBase.png");
			damageDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageDown00.png", 0);
			damageDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageDown01.png", 1);
			damageDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageDown02.png", 2);
			damageDownAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageDown03.png", 3);
			
			damageRightAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageRightBase.png");
			damageRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageRight00.png", 0);
			damageRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageRight01.png", 1);
			damageRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageRight02.png", 2);
			damageRightAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageRight03.png", 3);
			
			damageLeftAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageLeftBase.png");
			damageLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageLeft00.png", 0);
			damageLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageLeft01.png", 1);
			damageLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageLeft02.png", 2);
			damageLeftAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DamageLeft03.png", 3);
			
			skillAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "SkillBase.png");
			skillAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Skill00.png", 0);
			skillAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Skill01.png", 1);
			skillAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Skill02.png", 2);
			skillAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Skill03.png", 3);
			
			dieAnimation.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "DieBase.png");
			dieAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Die00.png", 0);
			dieAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Die01.png", 1);
			dieAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Die02.png", 2);
			dieAnimation.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Die03.png", 3);

			// eventAnimationList 초기화
			for (int i = 0; i < countEventAnimationList; i++) {
				String indexEvent = "";
				if (i < 10) 		indexEvent = "0" + i;
				else if (i < 100)	indexEvent = "" + i;
				
				Animations tmpAni = new Animations("Event"+indexEvent, projectPath);
				tmpAni.setBaseImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Event" + File.separator + "Event" + indexEvent + "-Base.png");
				tmpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Event" + File.separator + "Event" + indexEvent + "-00.png", 0);
				tmpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Event" + File.separator + "Event" + indexEvent + "-01.png", 1);
				tmpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Event" + File.separator + "Event" + indexEvent + "-02.png", 2);
				tmpAni.setAnimationImage(defaultDataFolderPath + File.separator + folderName + File.separator + "Event" + File.separator + "Event" + indexEvent + "-03.png", 3);
				eventAnimationList.add(tmpAni);
			}
		} catch (NullBufferedImage e) {
			e.printStackTrace();
		}
	}

	public int getIndexJob()				{	return indexJob;			}
	public void setIndexJob(int indexJob)	{	this.indexJob = indexJob;	}
	public int getInitLevel()				{	return initLevel;			}
	public int getMaxLevel()				{	return maxLevel;			}
	
	public Abilities getInitAbility() {	return initAbility;	}
	public void setInitAbility(Abilities initAbility) {	this.initAbility = initAbility;	}
	public Abilities getGrowthCurve() {	return growthCurve;	}
	public void setGrowthCurve(Abilities growthCurve) {	this.growthCurve = growthCurve;	}
	
	public Animations getMoveUpAnimation() {	return moveUpAnimation;	}
	public void setMoveUpAnimation(Animations moveUpAnimation) {	this.moveUpAnimation = moveUpAnimation;	}
	public Animations getMoveDownAnimation() {	return moveDownAnimation;	}
	public void setMoveDownAnimation(Animations moveDownAnimation) {	this.moveDownAnimation = moveDownAnimation;	}
	public Animations getMoveRightAnimation() {	return moveRightAnimation;	}
	public void setMoveRightAnimation(Animations moveRightAnimation) {	this.moveRightAnimation = moveRightAnimation;	}
	public Animations getMoveLeftAnimation() {	return moveLeftAnimation;	}
	public void setMoveLeftAnimation(Animations moveLeftAnimation) {	this.moveLeftAnimation = moveLeftAnimation;	}

	public Animations getAttackUpAnimation() {	return attackUpAnimation;	}
	public void setAttackUpAnimation(Animations attackUpAnimation) {	this.attackUpAnimation = attackUpAnimation;	}
	public Animations getAttackDownAnimation() {	return attackDownAnimation;	}
	public void setAttackDownAnimation(Animations attackDownAnimation) {	this.attackDownAnimation = attackDownAnimation;	}
	public Animations getAttackRightAnimation() {	return attackRightAnimation;	}
	public void setAttackRightAnimation(Animations attackRightAnimation) {	this.attackRightAnimation = attackRightAnimation;	}
	public Animations getAttackLeftAnimation() {	return attackLeftAnimation;	}
	public void setAttackLeftAnimation(Animations attackLeftAnimation) {	this.attackLeftAnimation = attackLeftAnimation;	}

	public Animations getBattleMoveUpAni() {	return battleMoveUpAni;	}
	public void setBattleMoveUpAni(Animations battleMoveUpAni) {	this.battleMoveUpAni = battleMoveUpAni;	}
	public Animations getBattleMoveDownAni() {	return battleMoveDownAni;	}
	public void setBattleMoveDownAni(Animations battleMoveDownAni) {	this.battleMoveDownAni = battleMoveDownAni;	}
	public Animations getBattleMoveRightAni() {	return battleMoveRightAni;	}
	public void setBattleMoveRightAni(Animations battleMoveRightAni) {	this.battleMoveRightAni = battleMoveRightAni;	}
	public Animations getBattleMoveLeftAni() {	return battleMoveLeftAni;	}
	public void setBattleMoveLeftAni(Animations battleMoveLeftAni) {	this.battleMoveLeftAni = battleMoveLeftAni;	}

	public Animations getDamageUpAnimation() {	return damageUpAnimation;	}
	public void setDamageUpAnimation(Animations damageUpAnimation) {	this.damageUpAnimation = damageUpAnimation;	}
	public Animations getDamageDownAnimation() {	return damageDownAnimation;	}
	public void setDamageDownAnimation(Animations damageDownAnimation) {	this.damageDownAnimation = damageDownAnimation;	}
	public Animations getDamageRightAnimation() {	return damageRightAnimation;	}
	public void setDamageRightAnimation(Animations damageRightAnimation) {	this.damageRightAnimation = damageRightAnimation;	}
	public Animations getDamageLeftAnimation() {	return damageLeftAnimation;	}
	public void setDamageLeftAnimation(Animations damageLeftAnimation) {	this.damageLeftAnimation = damageLeftAnimation;	}

	public Animations getSkillAnimation() {	return skillAnimation;	}
	public void setSkillAnimation(Animations skillAnimation) {	this.skillAnimation = skillAnimation;	}

	public Animations getDieAnimation() {	return dieAnimation;	}
	public void setDieAnimation(Animations dieAnimation) {	this.dieAnimation = dieAnimation;	}
	
	public List<Animations> getEventAnimationList() {	return eventAnimationList;	}
	public void setEventAnimationList(List<Animations> eventAnimationList) {	this.eventAnimationList = eventAnimationList;	}

	public int getCountEventAnimationList() {	return countEventAnimationList;	}
	public void setCountEventAnimationList(int countEventAnimationList) {	this.countEventAnimationList = countEventAnimationList;	}

	public int getIndexInitWeapon() {	return indexInitWeapon;	}
	public void setIndexInitWeapon(int indexInitWeapon) {	this.indexInitWeapon = indexInitWeapon;	}
	public int getIndexInitTopArmor() {	return indexInitTopArmor;	}
	public void setIndexInitTopArmor(int indexInitTopArmor) {	this.indexInitTopArmor = indexInitTopArmor;	}
	public int getIndexInitBottomsArmor() {	return indexInitBottomsArmor;	}
	public void setIndexInitBottomsArmor(int indexInitBottomsArmor) {	this.indexInitBottomsArmor = indexInitBottomsArmor;	}
	public int getIndexInitHelmetArmor() {	return indexInitHelmetArmor;	}
	public void setIndexInitHelmetArmor(int indexInitHelmetArmor) {	this.indexInitHelmetArmor = indexInitHelmetArmor;	}
	public int getIndexInitShieldArmor() {	return indexInitShieldArmor;	}
	public void setIndexInitShieldArmor(int indexInitShieldArmor) {	this.indexInitShieldArmor = indexInitShieldArmor;	}
	public int getIndexInitAccessoryArmor() {	return indexInitAccessoryArmor;	}
	public void setIndexInitAccessoryArmor(int indexInitAccessoryArmor) {	this.indexInitAccessoryArmor = indexInitAccessoryArmor;	}
	public int getSpeed() {	return speed;	}
	public void setSpeed(int speed) {	this.speed = speed;	}

	public void setInitLevel(int initLevel) throws IllegalLevelNumber {
		if (initLevel > maxLevel)
			throw new IllegalLevelNumber(
					"error: Actor.setInitLevel() (initLevel:" + initLevel
							+ ", maxLevel:" + maxLevel + ")");

		this.initLevel = initLevel;
	}

	public void setMaxLevel(int maxLevel) throws IllegalLevelNumber {
		if (initLevel > maxLevel)
			throw new IllegalLevelNumber(
					"error: Actor.setInitLevel() (initLevel:" + initLevel
							+ ", maxLevel:" + maxLevel + ")");

		this.maxLevel = maxLevel;
	}

	public void setInitAbility(int initHP, int initMP, int initEXP,
			int initStrength, int initVitality, int initIntelligence,
			int initKnowledge, int initAgility) {
		initAbility.setHP(initHP);
		initAbility.setMP(initMP);
		initAbility.setEXP(initEXP);
		initAbility.setStrength(initStrength);
		initAbility.setVitality(initVitality);
		initAbility.setIntelligence(initIntelligence);
		initAbility.setKnowledge(initKnowledge);
		initAbility.setVitality(initVitality);
		initAbility.setAgility(initAgility);
	}

	public void setGrowthCurve(int HP, int MP, int EXP, int strength,
			int vitality, int intelligence, int knowledge, int agility) {
		growthCurve.setHP(HP);
		growthCurve.setMP(MP);
		growthCurve.setEXP(EXP);
		growthCurve.setStrength(strength);
		growthCurve.setVitality(vitality);
		growthCurve.setIntelligence(intelligence);
		growthCurve.setKnowledge(knowledge);
		growthCurve.setVitality(vitality);
		growthCurve.setAgility(agility);
	}
	
	@Override
	protected void copyObject(ObjectEditorSystem data) {
		Actors tmpData = (Actors)data;
		
		this.indexJob = tmpData.indexJob;
		this.initLevel = tmpData.initLevel;
		this.maxLevel = tmpData.maxLevel;
		this.initAbility = tmpData.initAbility;
		this.growthCurve = tmpData.growthCurve;
		this.moveUpAnimation = tmpData.moveUpAnimation;
		this.moveDownAnimation = tmpData.moveDownAnimation;
		this.moveRightAnimation = tmpData.moveRightAnimation;
		this.moveLeftAnimation = tmpData.moveLeftAnimation;
		this.battleMoveUpAni = tmpData.battleMoveUpAni;
		this.battleMoveDownAni = tmpData.battleMoveDownAni;
		this.battleMoveRightAni = tmpData.battleMoveRightAni;
		this.battleMoveLeftAni = tmpData.battleMoveLeftAni;
		this.attackUpAnimation = tmpData.attackUpAnimation;
		this.attackDownAnimation = tmpData.attackDownAnimation;
		this.attackRightAnimation = tmpData.attackRightAnimation;
		this.attackLeftAnimation = tmpData.attackLeftAnimation;
		this.damageUpAnimation = tmpData.damageUpAnimation;
		this.damageDownAnimation = tmpData.damageDownAnimation;
		this.damageRightAnimation = tmpData.damageRightAnimation;
		this.damageLeftAnimation = tmpData.damageLeftAnimation;
		this.skillAnimation = tmpData.skillAnimation;
		this.dieAnimation = tmpData.dieAnimation;
		this.countEventAnimationList = tmpData.countEventAnimationList;
		this.eventAnimationList = tmpData.eventAnimationList;
		this.indexInitWeapon = tmpData.indexInitWeapon;
		this.indexInitTopArmor = tmpData.indexInitTopArmor;
		this.indexInitBottomsArmor = tmpData.indexInitBottomsArmor;
		this.indexInitHelmetArmor = tmpData.indexInitHelmetArmor;
		this.indexInitShieldArmor = tmpData.indexInitShieldArmor;
		this.indexInitAccessoryArmor = tmpData.indexInitAccessoryArmor;
		this.speed = tmpData.speed;
		
		this.moveUpAnimation.recoveryBufImg();
		this.moveDownAnimation.recoveryBufImg();
		this.moveRightAnimation.recoveryBufImg();
		this.moveLeftAnimation.recoveryBufImg();
		this.battleMoveUpAni.recoveryBufImg();
		this.battleMoveDownAni.recoveryBufImg();
		this.battleMoveRightAni.recoveryBufImg();
		this.battleMoveLeftAni.recoveryBufImg();
		this.attackUpAnimation.recoveryBufImg();
		this.attackDownAnimation.recoveryBufImg();
		this.attackRightAnimation.recoveryBufImg();
		this.attackLeftAnimation.recoveryBufImg();
		this.damageUpAnimation.recoveryBufImg();
		this.damageDownAnimation.recoveryBufImg();
		this.damageRightAnimation.recoveryBufImg();
		this.damageLeftAnimation.recoveryBufImg();
		this.skillAnimation.recoveryBufImg();
		this.dieAnimation.recoveryBufImg();
		for (int i = 0; i < this.eventAnimationList.size(); i++)
			this.eventAnimationList.get(i).recoveryBufImg();
	}
}
