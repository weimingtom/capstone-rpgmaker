/********************************************/
//���ǻ���!
//1. ���� ĳ������ ���°� �����ӻ���, ���� ���� � ���� ������ ����
//   ��, �����ӻ��¿��� �׼��� ������
//   player.setActorState(MOVESTATE)�� ���� �����ٶ�
//   player.getActorState()��
//2. �߰� ���� ��ΰ� �ʿ��� ���� ���� ex) Utilimage�� �����͵� ���� ���ɼ� ������
/**********************************************/


package execute;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import MapEditor.Map;

public class GameData implements Runnable{
	
	
	public static final int mapCharArrayRatio = 4;
	//����Ʈ Ÿ�̸�
	private int fadeTimer = 0;

	/*******���� ���� �÷��׵�****************************/
	public static final int LOGOSCREEN = 0;
	//public static final int EPILOGUE = 1;
	public static final int TITLEMENU = 2;
	public static final int PLAY = 3;
	public static final int EXIT = 4;
	public static final int SAVE = 5;
	public static final int LOAD = 6;
	public static final int PAUSE = 7;
	public static final int DIALOG = 8;
	public static final int FADEPAUSE = 9;
	public static final int ACTORLOAD = 11;
	public static final int MAPLOADENDED = 12;
	public static final int ACTORLOADENDED = 13;
	public static final int PLAYERDEATH = 14;
	public static final int LOADING = 15;
	public static final int NEWSTART = 16;
	public static final int GAMEOVER = 17;
	public static final int STATUSCALLED = 18;
	/**************************************************/
	//�� �������� Ŭ�� Ÿ�̸�
	public static int TIMER = 100;
	private static int FASTTIMER = 60;
	private static int SLOWTIMER = 100;
	
	/***��ƿ����/ �ε�ȭ��, Ÿ��Ʋȭ��/�ΰ�ȭ��***********************/
	private GameUtilityInformation titleScreen;
	private GameUtilityInformation logoScreen;
	private GameUtilityInformation loadScreen;
	private GameUtilityInformation cursorImage;
	private GameUtilityInformation dialogScreen;
	private GameUtilityInformation gameOver;
	private GameUtilityInformation statusScreen;
	private GameUtilityInformation levelUpImage;
	
	/*********************************************************/
	/****���� ���͵�, ���͵�, �÷��̾�******************************/
	/****���� �ٲ𶧸��� ���� �����Ѵ�*********************************/
	//�� Ŭ�������� �迭�� �׻� Ÿ�� ��ġ�θ� �Ѵ�. ���� �ȼ����� �ƴ�
	//�� �ִϸ��̼��� Ÿ�̸�
	
	private GameCharacter player = null;
	private Vector<GameCharacter> monsters = null;
	private Vector<GameCharacter> alliances = null;
	private Vector<GameCharacter> sortedCharacters = null;
	private Map gameMap;
	
	/****************************************************/
	//������ Ÿ��

	private int[][] gameTile;
	/****************************************************/
	
	//���� �н�
	private String gamePath;
	
	private int gameState = 0;
	private int exGameState = 0;
	private KeyFlags keyFlag;
	
	
	//���� ��ƾ�� ��Ÿ �۾��� ����
	private GameWindow gameWindow;
	private GameDisplay gameDisplay;
	
	//���� �κ� ����
	private AI gameRobot;
	
	//������ �������� �̸�
	private String musicFile = null;

	//�ִϸ��̼� ������
	private int animDelay = 100;
	//�׼� �ִϸ��̼��� �ε巯���� ����. Ŭ���ѹ���..�����Ŵ
	private boolean actionAnimFlag = false;

	private int animTimer;
	private int screenHeight;

	
	//������
	public GameData()
	{
		//��Ʈ����
		titleScreen = new GameUtilityInformation();
		//�ΰ�ȭ��
		logoScreen = new GameUtilityInformation();
		loadScreen = new GameUtilityInformation();
		//Ŀ���̹���
		cursorImage = new GameUtilityInformation();
		cursorImage.setPosition(0);
		//��������
		gameOver = new GameUtilityInformation();
		//���̾�α� ����(��ȭ)
		dialogScreen = new GameUtilityInformation();
		//����â
		statusScreen = new GameUtilityInformation();
		//������
		levelUpImage = new GameUtilityInformation();
		
		gameWindow = null;
		gameDisplay = null;
		gameRobot = new AI(this);
	}
	
	
	//���� ������ ����
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.gameState != GameData.EXIT)
		{
			//TODO
			//���α׷� ���� �ʱ�
			if(gameState == GameData.LOGOSCREEN)
			{
				//Ű�� �����ų� ����Ʈ�� ������
				if(fadeTimer >= 255 || keyFlag.isAction() || keyFlag.isEnter() || keyFlag.isCancel())
				{
					gameState = GameData.FADEPAUSE;
					exGameState = GameData.LOGOSCREEN;
				}
			}
			//����Ʈ ����
			else if(gameState == GameData.FADEPAUSE)
			{
				//����Ʈ ����Ʈ �缳��
				if(exGameState == GameData.LOGOSCREEN)
				{
					setFadeTimer(0);
					setGameState(GameData.TITLEMENU);
				}

			}
			else if(gameState == GameData.TITLEMENU)
			{
				runAtTitle();
			}
			else if(gameState == GameData.NEWSTART)
			{
				exGameState = GameData.NEWSTART;
				/*****************************************************************/
				/*****************************************************************/
				//�����δ� �̺�Ʈ ����ó�� ȣ���ؼ� ���� ����ؾ��� ��, �ʿ� ���� npc���� ������ �о �ε��Ѵ�.
				/*****************************************************************/
				/*****************************************************************/
				/*****************************************************************/
				gameState = GameData.LOADING;
				/****************************************************/
				/****************************************************/
				//�׽�Ʈ�� ���� ���� �ڵ� �ۼ�
				//�켱 �� �ε�
				loadMap();
				loadPlayer();
				//loadAlliances();
				loadMonsters();
				//startMusic("D:\\Download\\Music\\Gamma Ray - Discography\\2001 - No World Order!\\01 - Introduction.mp3");
				/****************************************************/
				this.computeGameTile();
				
				gameState = GameData.PLAY;
			}
			else if(gameState == GameData.LOAD)
			{
				gameState = GameData.EXIT;
			}
			else if(gameState == GameData.LOADING)
			{
				//�����δ� �̺�Ʈ ����ó�� ȣ���ؼ� ���� ����ؾ��� ��, �ʿ� ���� npc���� ������ �о �ε��Ѵ�.
			}
			else if(gameState == GameData.PLAY)
			{
				runAtPlay();
			}
			else if(gameState == GameData.GAMEOVER)
			{
				if(keyFlag.isCancel() || keyFlag.isEnter())
				{
					gameState = GameData.EXIT;
				}
			}
			//�������ͽ� â
			else if(gameState == GameData.STATUSCALLED)
			{
				runAtStatus();
			}
			
			try {
				Thread.sleep(GameData.TIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		this.exitGame();
	}
	
	//Ÿ��Ʋ �޴�����
	private void runAtTitle()
	{
		//Ŀ���� ��ġ�� ����
		if(keyFlag.isUp())
		{
			if(cursorImage.getPosition() == 0)
				cursorImage.setPosition(2);
			else
				cursorImage.setPosition(cursorImage.getPosition() - 1);
			try {
				Thread.sleep(SLOWTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(keyFlag.isDown())
		{
			if(cursorImage.getPosition() == 2)
				cursorImage.setPosition(0);
			else
				cursorImage.setPosition(cursorImage.getPosition() + 1);
			try {
				Thread.sleep(SLOWTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(cursorImage.getPosition() == 0)
		{
			if(keyFlag.isAction() || keyFlag.isEnter())
			{
				exGameState = GameData.TITLEMENU;
				gameState = GameData.NEWSTART;
				cursorImage.setPosition(0);
				TIMER = FASTTIMER;
			}
		}
		else if(cursorImage.getPosition() == 1)
		{
			if(keyFlag.isAction() || keyFlag.isEnter())
			{
				gameState = GameData.LOAD;
				cursorImage.setPosition(0);
			}
		}
		else if(cursorImage.getPosition() == 2)
		{
			if(keyFlag.isAction() || keyFlag.isEnter())
			{
				gameState = GameData.EXIT;
				cursorImage.setPosition(0);
			}				
		}
	}
	
	//������ �÷����϶�
	private void runAtPlay()
	{		
		if(keyFlag.isCancel())
		{
			//�������ͽ� â �����ֱ�
			gameState = GameData.STATUSCALLED;
			statusScreen.setCalled(true);
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//ĳ���� ����
		if(player.getNowStatus().getHP() <=0)
		{
			player.setActorState(GameCharacter.DEATH);
			this.fadeTimer = 0;
			gameState = GameData.GAMEOVER;
		}
		//�÷��̾��� �׼ǻ���Ȯ��, ����ų� ��Ʋ�̸�
		if (player.getActorState() == player.MOVESTATE
				|| player.getActorState() == player.BATTLESTATE) 
		{
			//�ִϸ��̼��� �ε巴��
			this.animTimer++;
			
			//�÷��̾��� Ű�Է¿� ���� ������ ����
			actionPlayer(player.getActorState());
			//npc���� ������
			gameRobot.NPCAction(alliances, player, gameMap, gameTile);
			//���� ������
			gameRobot.monsterAction(monsters, player, gameMap, gameTile);
			
			//�Ѳ����� �迭�� ������ �ۼ�
			this.computeGameTile();

		}
		
		//ĳ���� ü�� ä����
		if(player.getNowStatus().getHP() < player.getMaxStatus().getHP() && player.getNowStatus().getHP() > 0 )
			player.getNowStatus().setHP(player.getNowStatus().getHP() + player.level);
		if(player.getNowStatus().getHP() > player.getMaxStatus().getHP())
		{
			player.getNowStatus().setHP(player.getMaxStatus().getHP());
		}
	}
	
	//������ �������ͽ� ȭ���϶�
	private void runAtStatus()
	{
		player.setActorState(GameCharacter.STATUSCALLED);
		
		if(keyFlag.isDown())
		{
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(cursorImage.getPosition() < 3)
			{
				cursorImage.setPosition(cursorImage.getPosition()+1);
			}
			else if(cursorImage.getPosition() == 3)
			{
				cursorImage.setPosition(0);
			}
		}
		else if(keyFlag.isUp())
		{
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(cursorImage.getPosition() != 0)
			{
				cursorImage.setPosition(cursorImage.getPosition()-1);
			}
			else if(cursorImage.getPosition() == 0)
			{
				cursorImage.setPosition(3);
			}
		}
		else if(keyFlag.isAction() || keyFlag.isEnter())
		{
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(cursorImage.getPosition() == 2)
			{
				gameState = GameData.MAPLOADENDED;
				try {
					Thread.sleep(SLOWTIMER);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameState = GameData.PLAY;
				player.setActorState(GameCharacter.MOVESTATE);
				TIMER = FASTTIMER;
			}
			else if(cursorImage.getPosition() == 3)
			{
				gameState = GameData.EXIT;
			}
		}
		else if(keyFlag.isCancel())
		{
			gameState = GameData.MAPLOADENDED;
			try {
				Thread.sleep(SLOWTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gameState = GameData.PLAY;
			player.setActorState(GameCharacter.MOVESTATE);
			TIMER = FASTTIMER;
		}
	}
	
	
	//ĳ���͵� ����
	private void sortCharacters() {
		// TODO Auto-generated method stub
		
		sortedCharacters = new Vector<GameCharacter>();
		
		//���� ���� ����, y���� ��������
		if(player!=null)
		{
			sortedCharacters.add(player);
		}
		//npc�ӷ�
		if(alliances!=null)
		{
			for(int indexOfAlliances = 0 ; indexOfAlliances < alliances.size(); indexOfAlliances++)
			{
				GameCharacter alliance = alliances.elementAt(indexOfAlliances);
				//�߰��� ����
				for(int sortedIndex = 0 ; sortedIndex < sortedCharacters.size(); sortedIndex++)
				{
					if(alliance.getyPosition() < sortedCharacters.elementAt(sortedIndex).getyPosition())
					{
						sortedCharacters.add(sortedIndex, alliance);
						alliance = null;
						break;
					}
				}
				//���� ���� ū�ſ��ٸ�
				if(alliance != null)
				{
					sortedCharacters.add(alliance);
				}
			}
		}
		if(monsters!=null)
		{
			for(int indexOfMonsters = 0 ; indexOfMonsters < monsters.size(); indexOfMonsters++)
			{
				GameCharacter monster = monsters.elementAt(indexOfMonsters);
				//�߰��� ����
				for(int sortedIndex = 0 ; sortedIndex < sortedCharacters.size(); sortedIndex++)
				{
					if(monster.getyPosition() < sortedCharacters.elementAt(sortedIndex).getyPosition())
					{
						sortedCharacters.add(sortedIndex, monster);
						monster = null;
						break;
					}
				}
				//���� ���� ū�ſ��ٸ�
				if(monster != null)
				{
					sortedCharacters.add(monster);
				}
			}
		}
		
	}

	//Ÿ�� ���, ĳ���͸� �ڵ������Ѵ�.
	private void computeGameTile() {
		// TODO Auto-generated method stub
		this.initGameTile();
		sortCharacters();
		//ĳ���Ͱ� �ִ� Ÿ�� ���θ� 1�� ��ȯ
		int ratio = mapCharArrayRatio;
		//���ĵ� ���Ϳ� ����
		
		int count = 0;
		//���� ����
		for(int k = 0 ; k < sortedCharacters.size(); k++)
		{
			GameCharacter actor = (GameCharacter)sortedCharacters.elementAt(k);
			for(int i = 0 ; i < ratio; i++)
			{
				for(int j = 0 ; j < ratio; j++)
				{
					gameTile[actor.getyPosition()-ratio/2+i][actor.getxPosition()-ratio/2+i] = count;
				}
			}
			count++;
		}
	}


	//�÷��̾��� Ű�Է¿� ���� ������ ����
	public void actionPlayer(int playerState)
	{	
		if (keyFlag.isLeft()) 
		{
			player.setNowDirection(GameCharacter.LEFT);
			//����Ǵ� ��ġ����
			int nextX = player.getxPosition() - player.getSpeed();
			
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (gameRobot.actorCanMove(nextX, player.getyPosition(), gameMap,
					gameTile, player)) {
				// �̵������ϸ�
				player.setxPosition(nextX);
			}
			
		} else if (keyFlag.isRight()) {
			player.setNowDirection(GameCharacter.RIGHT);
			//����Ǵ� ��ġ����
			int nextX = player.getxPosition() + player.getSpeed();
			
			if (nextX > (gameTile[0].length  - GameData.mapCharArrayRatio))
			{ 
				nextX = (gameTile[0].length  - GameData.mapCharArrayRatio);
			}
			// �̵������Ѱ�?
			if (gameRobot.actorCanMove(nextX, player.getyPosition(), gameMap,
					gameTile,player)) {
				// �̵������ϸ�
				player.setxPosition(nextX);
			}
			
		} else if (keyFlag.isUp()) {
			//�÷��̾� ���� ����
			player.setNowDirection(GameCharacter.UP);
			//����Ǵ� ��ġ����
			int nextY = player.getyPosition() - player.getSpeed();
			
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (gameRobot.actorCanMove(player.getxPosition(), nextY, gameMap,
					gameTile,player)) {
				// �̵������ϸ�
				player.setyPosition(nextY);
			}
			
		} else if (keyFlag.isDown()) {
			// ���� ����
			player.setNowDirection(GameCharacter.DOWN);
			// ������ �Ʒ��� ������
			int nextY = player.getyPosition() + player.getSpeed();

			// �׷��� �ʺ��� ũ��
			if (nextY > (gameTile.length)-GameData.mapCharArrayRatio) {
				nextY = gameTile.length - GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (gameRobot.actorCanMove(player.getxPosition(), nextY, gameMap,
					gameTile,player)) {
				// �̵������ϸ�
				player.setyPosition(nextY);
			}
		}
		else if(keyFlag.isAction() && this.actionAnimFlag == false)
		{
			player.setAnimActionClock(0);
			if(this.actionAnimFlag == false)
				this.actionAnimFlag = true;
			//���� ����
		}
		//�׻�
		if(this.actionAnimFlag == true)
		{
			player.setAnimActionClock(player.getAnimActionClock()+1);
			int error = 0;
			if(player.getNowDirection() == GameCharacter.DOWN)
				error = player.getCharacter().getAttackDownAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.UP)
				error = player.getCharacter().getAttackUpAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.RIGHT)
				error = player.getCharacter().getAttackRightAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.LEFT)
				error = player.getCharacter().getAttackLeftAnimation().getCountImage();
			if(error == 0)
			{
				JOptionPane.showMessageDialog(gameWindow, "���� �ִϸ��̼��� �����!");
				System.exit(-1);
			}

			if(player.getAnimActionClock() > error)
			{
				actionAnimFlag = false;
				player.setAnimActionClock(0);
			}
		}	
	}


	//������ �ε�
	private void loadMonsters() {
		// TODO Auto-generated method stub
		//���� �ٲ���� ������ �� ��ƾ�� ȣ��Ǹ� ������ �ִ� ĳ���͵��� �����.
		if(this.monsters!=null)
			monsters = null;
		
		//����� ������ ���鼭 ĳ�� ������ �ε��ؾ��Ѵ�.
		//ĳ�� �ε�, �̿� ���� ������� �ε��Ѵ�
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			monsters = new Vector<GameCharacter>();
			monsters.add(new Monster(gamePath));
			monsters.elementAt(0).deployActor(2, 60, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(1).deployActor(2, 10, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(2).deployActor(2, 20, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(3).deployActor(2, 30, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(4).deployActor(2, 40, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(5).deployActor(2, 50, 60, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(6).deployActor(2, 70, 60, null);}
		catch(Exception e)
		{
//			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMonster()");
//			e.printStackTrace();
//			System.exit(0);
			//���Ͱ� ���� ��쿡
			monsters = null;
		}
	}
	
	//npc�� �ε�
	private void loadAlliances() {
		// TODO Auto-generated method stub
		//���� �ٲ���� ������ �� ��ƾ�� ȣ��Ǹ� ������ �ִ� ĳ���͵��� �����.
		if(this.alliances!=null)
			alliances = null;
		
		//����� ������ ���鼭 ĳ�� ������ �ε��ؾ��Ѵ�.
		//ĳ�� �ε�, �̿� ���� ������� �ε��Ѵ�
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			alliances = new Vector<GameCharacter>();
			alliances.add(new Alliance(gamePath));
			alliances.elementAt(0).deployActor(1, 100, 100, null);
		}
		catch(Exception e)
		{
//			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
//			e.printStackTrace();
//			System.exit(0);
			alliances = null;
		}
	}

	//���ǽ���
	private void startMusic(String string) {
		// TODO Auto-generated method stub
		this.musicFile = string;
	}

	//�ʷε�
	public void loadMap()
	{

		GameMapLoader mapLoader = new GameMapLoader();
		this.setGameMap(mapLoader.loadMap(this.gamePath+"/Map/caucse.map"));
		//���� ũ�⿡ ���� ���� �迭 ����
		try 
		{
			this.gameTile = new int[this.gameMap.getM_Height() * GameData.mapCharArrayRatio][];
			for (int i = 0; i < this.gameMap.getM_Height() * GameData.mapCharArrayRatio; i++) 
			{
				gameTile[i] = new int[gameMap.getM_Width() * GameData.mapCharArrayRatio];
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMap()\nCan't allocate gameTile");
			System.exit(0);
		}
		
		gameState = GameData.MAPLOADENDED;
	}

	//�÷��̾�ε�
	public void loadPlayer()
	{
		try{
			this.player = new Alliance(gamePath);
			
			//player.setNowStatus(nowStatus);
			player.deployActor(0,100, 100, null);
			player.setNowEXP(1909);
			player.getMaxStatus().setEXP(10);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadPlayer()");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/*******************************************************************/
	//���� Ÿ�� �ʱ�ȭ
	public void initGameTile()
	{
		int h = gameTile.length;
		int w = gameTile[0].length;
		
		for(int i = 0; i < h; i ++)
		{
			for(int j = 0 ; j < w; j++)
				gameTile[i][j] = -1;
		}
	}
	
	//setter getter
	public void setKeyFlag(KeyFlags keyFlag) {
		// TODO Auto-generated method stub
		this.keyFlag = keyFlag;
	}

	//���� ���� ����
	public void setGameState(int gameState) {
		// TODO Auto-generated method stub
		this.gameState = gameState;
	}
	
	public int getGameState()
	{
		return this.gameState;
	}
	
	//���� �н�����
	public void setGamePath(String gamePath)
	{
		this.gamePath = gamePath;
		//������Ʈ �н� ������ �ΰ� �̹����� ����
		String utilPath = gamePath+"/UtilImage";
		try 
		{
			titleScreen.setUtilImage(ImageIO.read(new File(utilPath+"/TITLE.png")));
			logoScreen.setUtilImage(ImageIO.read(new File(utilPath+"/LOGO.png")));
			cursorImage.setUtilImage(ImageIO.read(new File(utilPath+"/CURSOR.png")));
			loadScreen.setUtilImage(ImageIO.read(new File(utilPath+"/LOADING.png")));
			gameOver.setUtilImage(ImageIO.read(new File(utilPath+"/GAMEOVER.png")));
			levelUpImage.setUtilImage(ImageIO.read(new File(utilPath+"/LEVELUP.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//���࿡ Ÿ��Ʋ�� �ҷ����µ� �����ص� ����
			JOptionPane.showMessageDialog
			(gameWindow, "Error!! Can't find TITLE.png or LOGO.png or CURSOR.png or SomthingElse");
			System.exit(0);
		}

	}
	
	
	
	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public GameWindow getGameWindow() {
		return gameWindow;
	}

	public void exitGame() {
		// TODO Auto-generated method stub
		gameDisplay.exitGame();
		//gameWindow.exitGame();
		System.exit(0);
	}

	public void setGameDisplay(GameDisplay gameDisplay) {
		this.gameDisplay = gameDisplay;
	}

	public GameDisplay getGameDisplay() {
		return gameDisplay;
	}

	public void setFadeTimer(int fadeTimer) {
		this.fadeTimer = fadeTimer;
	}

	public int getFadeTimer() {
		return fadeTimer;
	}


	
	public GameUtilityInformation getTitleScreen() {
		return titleScreen;
	}


	public void setTitleScreen(GameUtilityInformation titleScreen) {
		this.titleScreen = titleScreen;
	}


	public GameUtilityInformation getLogoScreen() {
		return logoScreen;
	}


	public void setLogoScreen(GameUtilityInformation logoScreen) {
		this.logoScreen = logoScreen;
	}


	public GameUtilityInformation getLoadScreen() {
		return loadScreen;
	}


	public void setLoadScreen(GameUtilityInformation loadScreen) {
		this.loadScreen = loadScreen;
	}


	public GameUtilityInformation getCursorImage() {
		return cursorImage;
	}


	public void setCursorImage(GameUtilityInformation cursorImage) {
		this.cursorImage = cursorImage;
	}


	public int getExGameState() {
		return exGameState;
	}


	public void setExGameState(int exGameState) {
		this.exGameState = exGameState;
	}


	public void setPlayer(GameCharacter player) {
		this.player = player;
	}


	public GameCharacter getPlayer() {
		return player;
	}


	public void setMonsters(Vector<GameCharacter> monsters) {
		this.monsters = monsters;
	}


	public Vector<GameCharacter> getMonsters() {
		return monsters;
	}


	public void setAlliances(Vector<GameCharacter> alliances) {
		this.alliances = alliances;
	}


	public Vector<GameCharacter> getAlliances() {
		return alliances;
	}


	public void setGameMap(Map gameMap) {
		this.gameMap = gameMap;
	}


	public Map getGameMap() {
		return gameMap;
	}


	public int[][] getGameTile() {
		return gameTile;
	}


	public void setGameTile(int[][] gameTile) {
		this.gameTile = gameTile;
	}
	
	public int getPlayerPositionX()
	{
		return this.player.getxPosition();
	}
	
	public int getPlayerPositionY()
	{
		return this.player.getyPosition();
	}

	
	public boolean isChangeCharacterAnim(int timer, int numb)
	{
		//4���� �ѹ���
		if(timer < 0)
			timer = 0;
		numb*=10;
		if(timer%(animDelay/numb) == 0)
			return true;
		else
			return false;
	}


	public void setActionAnimFlag(boolean actionAnimFlag) {
		this.actionAnimFlag = actionAnimFlag;
	}


	public boolean isActionAnimFlag() {
		return actionAnimFlag;
	}

	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}


	public String getMusicFile() {
		return musicFile;
	}


	public GameUtilityInformation getDialogScreen() {
		return dialogScreen;
	}


	public Vector<GameCharacter> getSortedCharacters() {
		return sortedCharacters;
	}


	public int getAnimTimer() {
		return animTimer;
	}


	public void setAnimTimer(int animTimer) {
		this.animTimer = animTimer;
	}


	public GameUtilityInformation getGameOver() {
		return gameOver;
	}


	public AI getGameRobot() {
		return gameRobot;
	}


	public void setGameRobot(AI gameRobot) {
		this.gameRobot = gameRobot;
	}


	public GameUtilityInformation getStatusScreen() {
		return statusScreen;
	}


	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
		statusScreen.setFont(new Font("����", Font.BOLD , screenHeight/20 ));
		titleScreen.setFont(new Font("Courier New", Font.BOLD , screenHeight/15));
	}


	public GameUtilityInformation getLevelUpImage() {
		return levelUpImage;
	}
}
