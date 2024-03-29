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
	//이펙트 타이머
	private int fadeTimer = 0;

	/*******게임 상태 플래그들****************************/
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
	public static final int GAMEOVER = 17;
	/**************************************************/
	//이 쓰레드의 클럭 타이머
	public static int TIMER = 100;
	
	/***유틸인포/ 로드화면, 타이틀화면/로고화면***********************/
	private GameUtilityInformation titleScreen;
	private GameUtilityInformation logoScreen;
	private GameUtilityInformation loadScreen;
	private GameUtilityInformation cursorImage;
	private GameUtilityInformation dialogScreen;
	
	/*********************************************************/
	/****게임 액터들, 몬스터들, 플레이어******************************/
	/****맵이 바뀔때마다 새로 세팅한다*********************************/
	//이 클래으에서 배열은 항상 타일 위치로만 한다. 실제 픽셀값이 아님
	//각 애니메이션의 타이머
	
	private GameCharacter player = null;
	private Vector<GameCharacter> monsters = null;
	private Vector<GameCharacter> alliances = null;
	private Vector<GameCharacter> sortedCharacters = null;
	private Map gameMap;
	
	/****************************************************/
	//게임의 타일

	private int[][] gameTile;
	/****************************************************/
	
	//게임 패스
	private String gamePath;
	
	private int gameState = 0;
	private int exGameState = 0;
	private KeyFlags keyFlag;
	
	
	//종료 루틴및 기타 작업을 위해
	private GameWindow gameWindow;
	private GameDisplay gameDisplay;
	
	//게임 로봇 생성
	private AI gameRobot;
	
	//실행할 음악파일 이름
	private String musicFile = null;

	//애니메이션 딜레이
	private int animDelay = 100;
	//액션 애니메이션의 부드러움을 위해. 클릭한번만..적용시킴
	private boolean actionAnimFlag = false;

	private int animTimer;

	
	//생성자
	public GameData()
	{
		//폰트설정
		titleScreen = new GameUtilityInformation();
		Font screenFont = new Font("Courier New", Font.BOLD , titleScreen.getFontSize());
		titleScreen.setFont(screenFont);
		logoScreen = new GameUtilityInformation();
		loadScreen = new GameUtilityInformation();
		cursorImage = new GameUtilityInformation();
		cursorImage.setPosition(0);
		
		setDialogScreen(new GameUtilityInformation());
		gameWindow = null;
		gameDisplay = null;
		gameRobot = new AI(this);
	}
	
	
	//게임 데이터 실행
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.gameState != GameData.EXIT)
		{
			//TODO
			//프로그램 실행 초기
			if(gameState == GameData.LOGOSCREEN)
			{
				//키가 눌리거나 이펙트가 끝나면
				if(fadeTimer >= 255 || keyFlag.isAction() || keyFlag.isEnter() || keyFlag.isCancel())
				{
					gameState = GameData.FADEPAUSE;
					exGameState = GameData.LOGOSCREEN;
				}
			}
			//이펙트 종료
			else if(gameState == GameData.FADEPAUSE)
			{
				//페이트 이펙트 재설정
				if(exGameState == GameData.LOGOSCREEN)
				{
					setFadeTimer(0);
					setGameState(GameData.TITLEMENU);
				}

			}
			else if(gameState == GameData.TITLEMENU)
			{
				TIMER = 100;
				//커서의 위치에 따라
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
				//실제로는 이벤트 디스패처를 호출해서 현재 출력해야할 맵, 맵에 속한 npc등의 정보를 읽어서 로드한다.
				/*****************************************************************/
				/*****************************************************************/
				/*****************************************************************/
				gameState = GameData.LOADING;
				/****************************************************/
				/****************************************************/
				//테스트를 위해 임의 코드 작성
				//우선 맵 로드
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
				//실제로는 이벤트 디스패처를 호출해서 현재 출력해야할 맵, 맵에 속한 npc등의 정보를 읽어서 로드한다.
			}
			else if(gameState == GameData.PLAY)
			{
				//캐릭터 뒤짐
				if(player.getNowStatus().getHP() <=0)
				{
					player.setActorState(GameCharacter.DEATH);
					gameState = GameData.GAMEOVER;
				}
				//게임 타일 초기화
				if(keyFlag.isEnter())
				{
					gameState = GameData.EXIT;
				}
				//플레이어의 액션상태확인, 무브거나 배틀이면
				if (player.getActorState() == player.MOVESTATE
						|| player.getActorState() == player.BATTLESTATE) 
				{
					//애니메이션을 부드럽게
					this.animTimer++;
					
					//플레이어의 키입력에 따른 움직임 설정
					actionPlayer(player.getActorState());
					//npc들의 움직임
					gameRobot.NPCAction(alliances, player, gameMap, gameTile);
					//몬스터 움직임
					gameRobot.monsterAction(monsters, player, gameMap, gameTile);
					
					//한꺼번에 배열에 움직임 작성
					this.computeGameTile();

				}
				else if(gameState == GameData.GAMEOVER)
				{
					gameState = GameData.EXIT;
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
	
	
	//캐릭터들 정렬
	private void sortCharacters() {
		// TODO Auto-generated method stub
		
		sortedCharacters = new Vector<GameCharacter>();
		
		//삽입 정렬 시작, y값의 오름차순
		if(player!=null)
		{
			sortedCharacters.add(player);
		}
		//npc임력
		if(alliances!=null)
		{
			for(int indexOfAlliances = 0 ; indexOfAlliances < alliances.size(); indexOfAlliances++)
			{
				GameCharacter alliance = alliances.elementAt(indexOfAlliances);
				//중간에 삽입
				for(int sortedIndex = 0 ; sortedIndex < sortedCharacters.size(); sortedIndex++)
				{
					if(alliance.getyPosition() < sortedCharacters.elementAt(sortedIndex).getyPosition())
					{
						sortedCharacters.add(sortedIndex, alliance);
						alliance = null;
						break;
					}
				}
				//만약 제일 큰거였다면
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
				//중간에 삽입
				for(int sortedIndex = 0 ; sortedIndex < sortedCharacters.size(); sortedIndex++)
				{
					if(monster.getyPosition() < sortedCharacters.elementAt(sortedIndex).getyPosition())
					{
						sortedCharacters.add(sortedIndex, monster);
						monster = null;
						break;
					}
				}
				//만약 제일 큰거였다면
				if(monster != null)
				{
					sortedCharacters.add(monster);
				}
			}
		}
		
	}

	private void computeGameTile() {
		// TODO Auto-generated method stub
		this.initGameTile();
		sortCharacters();
		//캐릭터가 있는 타일 전부를 1로 전환
		int ratio = mapCharArrayRatio;
		//정렬된 벡터에 대해
		
		int count = 0;
		//액터 설정
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


	//플레이어의 키입력에 따른 움직임 설정
	public void actionPlayer(int playerState)
	{	
		if (keyFlag.isLeft()) 
		{
			player.setNowDirection(GameCharacter.LEFT);
			//예상되는 위치설정
			int nextX = player.getxPosition() - player.getSpeed();
			
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (gameRobot.actorCanMove(nextX, player.getyPosition(), gameMap,
					gameTile, player)) {
				// 이동가능하면
				player.setxPosition(nextX);
			}
			
		} else if (keyFlag.isRight()) {
			player.setNowDirection(GameCharacter.RIGHT);
			//예상되는 위치설정
			int nextX = player.getxPosition() + player.getSpeed();
			
			if (nextX > (gameTile[0].length  - GameData.mapCharArrayRatio))
			{ 
				nextX = (gameTile[0].length  - GameData.mapCharArrayRatio);
			}
			// 이동가능한가?
			if (gameRobot.actorCanMove(nextX, player.getyPosition(), gameMap,
					gameTile,player)) {
				// 이동가능하면
				player.setxPosition(nextX);
			}
			
		} else if (keyFlag.isUp()) {
			//플레이어 방향 설정
			player.setNowDirection(GameCharacter.UP);
			//예상되는 위치설정
			int nextY = player.getyPosition() - player.getSpeed();
			
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (gameRobot.actorCanMove(player.getxPosition(), nextY, gameMap,
					gameTile,player)) {
				// 이동가능하면
				player.setyPosition(nextY);
			}
			
		} else if (keyFlag.isDown()) {
			// 방향 설정
			player.setNowDirection(GameCharacter.DOWN);
			// 방향이 아래로 나오면
			int nextY = player.getyPosition() + player.getSpeed();

			// 그런데 맵보다 크면
			if (nextY > (gameTile.length)-GameData.mapCharArrayRatio) {
				nextY = gameTile.length - GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (gameRobot.actorCanMove(player.getxPosition(), nextY, gameMap,
					gameTile,player)) {
				// 이동가능하면
				player.setyPosition(nextY);
			}
		}
		else if(keyFlag.isAction() && this.actionAnimFlag == false)
		{
			player.setAnimActionClock(0);
			if(this.actionAnimFlag == false)
				this.actionAnimFlag = true;
			//적을 공격
		}
		//항상
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
				JOptionPane.showMessageDialog(gameWindow, "전투 애니메이션이 없어요!");
				System.exit(-1);
			}

			if(player.getAnimActionClock() > error)
			{
				actionAnimFlag = false;
				player.setAnimActionClock(0);
			}
		}	
	}


	//괴물들 로드
	private void loadMonsters() {
		// TODO Auto-generated method stub
		//맵이 바뀌엇기 때문에 이 루틴이 호출되면 이전에 있던 캐릭터들은 지운다.
		if(this.monsters!=null)
			monsters = null;
		
		//사실은 루프를 돌면서 캐릭 정보를 로드해야한다.
		//캐릭 로드, 이와 같은 방식으로 로드한다
		try{
			//현재 맵에 정의된 npc들 출력
			monsters = new Vector<GameCharacter>();
			monsters.add(new Monster(gamePath));
			monsters.elementAt(0).deployActor(2, 30, 30, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(1).deployActor(2, 40, 30, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(2).deployActor(2, 50, 30, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(3).deployActor(2, 60, 30, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(4).deployActor(2, 70, 30, null);
			monsters.add(new Monster(gamePath));
			monsters.elementAt(5).deployActor(2, 30, 50, null);
		}
		catch(Exception e)
		{
//			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMonster()");
//			e.printStackTrace();
//			System.exit(0);
			//몬스터가 없을 경우에
			monsters = null;
		}
	}
	
	//npc들 로드
	private void loadAlliances() {
		// TODO Auto-generated method stub
		//맵이 바뀌엇기 때문에 이 루틴이 호출되면 이전에 있던 캐릭터들은 지운다.
		if(this.alliances!=null)
			alliances = null;
		
		//사실은 루프를 돌면서 캐릭 정보를 로드해야한다.
		//캐릭 로드, 이와 같은 방식으로 로드한다
		try{
			//현재 맵에 정의된 npc들 출력
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
		//맵의 크기에 따라 게임 배열 설정
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
	}

	public void loadPlayer()
	{
		try{
			this.player = new Alliance(gamePath);
			
			//player.setNowStatus(nowStatus);
			player.deployActor(0,100, 100, null);
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
				gameTile[i][j] = -1;
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
	
	//게임 패스설정
	public void setGamePath(String gamePath)
	{
		this.gamePath = gamePath;
		//프로젝트 패스 설정시 로고 이미지등 설정
		//(new Font("AR BERKLE", Font.BOLD, menuInfo.getFontSize()
		try 
		{
			titleScreen.setUtilImage(ImageIO.read(new File(gamePath+"/TITLE.png")));
			logoScreen.setUtilImage(ImageIO.read(new File(gamePath+"/LOGO.png")));
			cursorImage.setUtilImage(ImageIO.read(new File(gamePath+"/CURSOR.png")));
			loadScreen.setUtilImage(ImageIO.read(new File(gamePath+"/LOADING.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//만약에 타이틀을 불러오는데 실패해도 진행
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
		//4번당 한번씩
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


	public void setDialogScreen(GameUtilityInformation dialogScreen) {
		this.dialogScreen = dialogScreen;
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

}
