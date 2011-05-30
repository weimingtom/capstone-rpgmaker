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
	public static final int MAPLOAD = 10;
	public static final int ACTORLOAD = 11;
	public static final int MAPLOADENDED = 12;
	public static final int ACTORLOADENDED = 13;
	public static final int PLAYERDEATH = 14;
	public static final int LOADING = 15;
	public static final int NEWSTART = 16;
	/**************************************************/
	//�� �������� Ŭ�� Ÿ�̸�
	public static int TIMER = 60;
	
	/***��ƿ����/ �ε�ȭ��, Ÿ��Ʋȭ��/�ΰ�ȭ��***********************/
	private GameUtilityInformation titleScreen;
	private GameUtilityInformation logoScreen;
	private GameUtilityInformation loadScreen;
	private GameUtilityInformation cursorImage;
	private GameUtilityInformation dialogScreen;
	
	/*********************************************************/
	/****���� ���͵�, ���͵�, �÷��̾�******************************/
	/****���� �ٲ𶧸��� ���� �����Ѵ�*********************************/
	//�� Ŭ�������� �迭�� �׻� Ÿ�� ��ġ�θ� �Ѵ�. ���� �ȼ����� �ƴ�
	//�� �ִϸ��̼��� Ÿ�̸�
	private int playerAnimTimer = 0;

	
	private GameCharacter player;
	private Vector<GameCharacter> monsters;
	private Vector<GameCharacter> alliances;
	private Map gameMap;
	
	/****************************************************/
	//������ Ÿ��

	private byte[][] gameTile;
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
	private int animDelay = 5;
	//�׼� �ִϸ��̼��� �ε巯���� ����. Ŭ���ѹ���..�����Ŵ
	private boolean actionAnimFlag = false;

	private int playerActionTimer;

	
	//������
	public GameData()
	{
		//��Ʈ����
		titleScreen = new GameUtilityInformation();
		Font screenFont = new Font("Courier New", Font.BOLD , titleScreen.getFontSize());
		titleScreen.setFont(screenFont);
		logoScreen = new GameUtilityInformation();
		loadScreen = new GameUtilityInformation();
		cursorImage = new GameUtilityInformation();
		cursorImage.setPosition(0);
		
		dialogScreen = new GameUtilityInformation();
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
				TIMER = 100;
				//Ŀ���� ��ġ�� ����
				if(cursorImage.getPosition() == 0)
				{
					if(keyFlag.isAction() || keyFlag.isEnter())
					{
						exGameState = GameData.TITLEMENU;
						gameState = GameData.NEWSTART;
						TIMER = 60;
					}
				}
				else if(cursorImage.getPosition() == 1)
				{
					if(keyFlag.isAction() || keyFlag.isEnter())
					{
						gameState = GameData.LOAD;
					}
				}
				else if(cursorImage.getPosition() == 2)
				{
					if(keyFlag.isAction() || keyFlag.isEnter())
					{
						gameState = GameData.EXIT;
					}				
				}
			}
			else if(gameState == GameData.NEWSTART)
			{
				exGameState = GameData.NEWSTART;
				/*****************************************************************/
				/*****************************************************************/
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
				loadCharacterNPC();
				loadMonsters();
				startMusic("D:\\Download\\Music\\Gamma Ray - Discography\\2001 - No World Order!\\01 - Introduction.mp3");

				/****************************************************/
				gameState = GameData.PLAY;
				this.initGameTile();

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
				//���� Ÿ�� �ʱ�ȭ
				if(keyFlag.isEnter())
				{
					gameState = GameData.EXIT;
				}
				//�÷��̾��� �׼ǻ���Ȯ��, ����ų� ��Ʋ�̸�
				if (player.getActorState() == player.MOVESTATE
						|| player.getActorState() == player.BATTLESTATE) 
				{
					//�ִϸ��̼��� �ε巴��
					this.playerAnimTimer++;
					
					//�÷��̾��� Ű�Է¿� ���� ������ ����
					actionPlayer(player.getActorState());
					//npc���� ������
					gameRobot.NPCAction(alliances, player, gameMap, gameTile);
					//���� ������
					gameRobot.monsterAction(monsters, player, gameMap, gameTile);
					
					//�Ѳ����� �迭�� ������ �ۼ�
					this.computeGameTile();

				}
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
	
	
	private void computeGameTile() {
		// TODO Auto-generated method stub
		this.initGameTile();
		//ĳ���Ͱ� �ִ� Ÿ�� ���θ� 1�� ��ȯ
		
		
		
		gameTile[player.getyPosition()][player.getxPosition()] = 1;
		
		if(alliances != null)
		{
			for (int i = 0; i < alliances.size(); i++) 
			{
				Alliance alliance = (Alliance) alliances.elementAt(i);
				gameTile[alliance.getyPosition()][alliance.getxPosition()] = 1;
			}
		}
		if(monsters != null)
		{
			for (int i = 0; i < monsters.size(); i++) 
			{
				Monster monster = (Monster) monsters.elementAt(i);
				gameTile[monster.getyPosition()][monster.getxPosition()] = 1;
			}
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
					gameTile)) {
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
					gameTile)) {
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
					gameTile)) {
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
					gameTile)) {
				// �̵������ϸ�
				player.setyPosition(nextY);
			}
		}
		else if(keyFlag.isAction() && this.actionAnimFlag == false)
		{
			playerActionTimer = 0;
			if(this.actionAnimFlag == false)
				this.actionAnimFlag = true;
			//���� ����
		}
		//�׻�
		if(this.actionAnimFlag == true)
		{
			playerActionTimer ++;
			int error = 0;
			if(player.getNowDirection() == GameCharacter.DOWN)
				error = player.getChracter().getAttackDownAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.UP)
				error = player.getChracter().getAttackUpAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.RIGHT)
				error = player.getChracter().getAttackRightAnimation().getCountImage();
			else if(player.getNowDirection() == GameCharacter.LEFT)
				error = player.getChracter().getAttackLeftAnimation().getCountImage();
			if(error == 0)
			{
				JOptionPane.showMessageDialog(gameWindow, "���� �ִϸ��̼��� �����!");
				System.exit(-1);
			}
			if(playerActionTimer % error == 0)
			{
				actionAnimFlag = false;
				playerActionTimer = 0;
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
			monsters.elementAt(0).deployActor(2, 30, 30, null);
		}
		catch(Exception e)
		{
//			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMonster()");
//			e.printStackTrace();
//			System.exit(0);
			//���Ͱ� ���� ��쿡
			monsters = null;
		}
	}
	

	private void loadCharacterNPC() {
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


	private void startMusic(String string) {
		// TODO Auto-generated method stub
		this.musicFile = string;
	}


	public void loadMap()
	{

		GameMapLoader mapLoader = new GameMapLoader();
		this.setGameMap(mapLoader.loadMap(this.gamePath+"/Map/caucse.map"));
		//���� ũ�⿡ ���� ���� �迭 ����
		try 
		{
			this.gameTile = new byte[this.gameMap.getM_Height() * GameData.mapCharArrayRatio][];
			for (int i = 0; i < this.gameMap.getM_Height() * GameData.mapCharArrayRatio; i++) 
			{
				gameTile[i] = new byte[gameMap.getM_Width() * GameData.mapCharArrayRatio];
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMap()\nCan't allocate gameTile");
			System.exit(0);
		}
	}
	public void loadPlayer()
	{
		try{
			this.player = new Alliance(gamePath);
			player.deployActor(0, 70, 70, null);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadPlayer()");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/*******************************************************************/
	public void initGameTile()
	{
		int h = gameTile.length;
		int w = gameTile[0].length;
		
		for(int i = 0; i < h; i ++)
		{
			for(int j = 0 ; j < w; j++)
				gameTile[i][j] = 0;
		}
	}
	//setter getter
	public void setKeyFlag(KeyFlags keyFlag) {
		// TODO Auto-generated method stub
		this.keyFlag = keyFlag;
	}

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
		//(new Font("AR BERKLE", Font.BOLD, menuInfo.getFontSize()
		try 
		{
			titleScreen.setUtilImage(ImageIO.read(new File(gamePath+"/TITLE.png")));
			logoScreen.setUtilImage(ImageIO.read(new File(gamePath+"/LOGO.png")));
			cursorImage.setUtilImage(ImageIO.read(new File(gamePath+"/CURSOR.png")));
			loadScreen.setUtilImage(ImageIO.read(new File(gamePath+"/LOADING.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//���࿡ Ÿ��Ʋ�� �ҷ����µ� �����ص� ����
			JOptionPane.showMessageDialog
			(gameWindow, "Error!! Can't find TITLE.png or LOGO.png or CURSOR.png");
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


	public byte[][] getGameTile() {
		return gameTile;
	}


	public void setGameTile(byte[][] gameTile) {
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
	
	public void setPlayerAnimTimer(int count)
	{
		this.playerAnimTimer = count;
	}
	public int getPlayerAnimTimer()
	{
		return this.playerAnimTimer;
	}
	
	public boolean isChangeCharacterAnim(int timer)
	{
		//4���� �ѹ���
		if(timer < 0)
			timer = 0;
		if(timer%animDelay == 0)
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


	public int getPlayerActionTimer() {
		return playerActionTimer;
	}


	public void setPlayerActionTimer(int playerActionTimer) {
		this.playerActionTimer = playerActionTimer;
	}


	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}


	public String getMusicFile() {
		return musicFile;
	}
	
}
