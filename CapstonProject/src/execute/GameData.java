
package execute;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import characterEditor.Abilities;

import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.EventTile;
import eventEditor.eventContents.ChangeBGMEvent;
import eventEditor.eventContents.ChangeFlagEvent;
import eventEditor.eventContents.ChangeMapEvent;
import eventEditor.eventContents.DialogEvent;
import eventEditor.eventContents.EventContent;
import eventEditor.eventContents.MotionEvent;
import eventEditor.eventContents.Switch;
import eventEditor.eventContents.SwitchDialogEvent;

import bootstrap.Bootstrap;
import bootstrap.BootstrapInfo;

import MapEditor.DrawingTemplate;
import MapEditor.Map;
import MapEditor.Tile;

public class GameData implements Runnable, Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -720498563671166298L;
	public static final int mapCharArrayRatio = 4;
	//����Ʈ Ÿ�̸�
	private int fadeTimer = 0;

	private String utilPath =null;
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
//	public static final int ACTORLOAD = 11;
//	public static final int MAPLOADENDED = 12;
//	public static final int ACTORLOADENDED = 13;
//	public static final int PLAYERDEATH = 14;
	public static final int LOADING = 15;
	public static final int NEWSTART = 16;
	public static final int GAMEOVER = 17;
	public static final int STATUSCALLED = 18;
	/**************************************************/
	//�� �������� Ŭ�� Ÿ�̸�
	public static int TIMER = 100;
	public static int FASTTIMER = 60;
	public static int SLOWTIMER = 100;
	
	/***��ƿ����/ �ε�ȭ��, Ÿ��Ʋȭ��/�ΰ�ȭ��***********************/
	private transient GameUtilityInformation titleScreen;
	private transient GameUtilityInformation logoScreen;
	private transient GameUtilityInformation loadScreen;
	private transient GameUtilityInformation cursorImage;
	private transient GameUtilityInformation dialogScreen;
	private transient GameUtilityInformation gameOver;
	private transient GameUtilityInformation statusScreen;
	private transient GameUtilityInformation levelUpImage;
	private transient GameUtilityInformation switchDialog;

	//�� Ŭ�������� �迭�� �׻� Ÿ�� ��ġ�θ� �Ѵ�. ���� �ȼ����� �ƴ�
	//�� �ִϸ��̼��� Ÿ�̸�
	
	private GameCharacter player = null;
	private transient Vector<GameCharacter> monsters = null;
	private transient Vector<GameCharacter> alliances = null;
	private transient Vector<GameCharacter> sortedCharacters = null;
	private transient Map gameMap;
	
	/****************************************************/
	//������ Ÿ��

	private transient int[][] gameTile;
	private transient GameEventDispatcher eventDispatcher;
	private static final int MAXFLAG = 1001;
	private boolean [] conditionFlag;
	//���ο� ����Ʈ�� �޾ƿ;��ϳ�?
	private transient boolean eventStart = false;
	//���� �̺�Ʈ�� �������ΰ�?
	//private boolean eventStart = false;
	private transient Event nowEventList = null;
	private transient EventContent nowEvent = null;
	private transient int eventContentListIndex = -1;
	private transient boolean charEnterMap = false;
	private transient boolean charActionMap = false;
	/****************************************************/
	
	//���� �н�
	private String gamePath;
	
	private transient int gameState = 0;
	private transient int exGameState = 0;
	private transient KeyFlags keyFlag;
	
	
	//���� ��ƾ�� ��Ÿ �۾��� ����
	private transient GameWindow gameWindow;
	private transient GameDisplay gameDisplay;
	
	//���� �κ� ����
	private transient AI gameRobot;
	
	//������ �������� �̸�
	private transient String musicFile = null;

	//�ִϸ��̼� ������
	private transient int animDelay = 100;
	//�׼� �ִ�transient ���̼��� �ε巯���� ����. Ŭ���ѹ���..�����Ŵ
	private transient boolean actionAnimFlag = false;

	private transient int animTimer;
	

	//�ʵ�����
	private transient BufferedImage background;
	private transient BufferedImage foreBackImage;
	private transient BufferedImage foreForeImage;
	private transient GameMusic gameMusic;


	private transient boolean autoEventCalled = false;
	private transient boolean autoAllianceEventCalled = false;
	private transient boolean autoMonsterEventCalled = false;
	private transient List<Event> autoEvents;
	private transient List<EventTile> actorEventTiles;
	private int charOnMapX;
	private int charOnMapY;
	private transient boolean moveEventCalled;
	private transient int moveEventSpeed;
	private transient int moveEventDirection;
	private transient int moveEventDestination;

	private transient int moveEventcounter;
	private transient EventTile nowEventTile;
	private transient GameCharacter moveEventActor;
	private transient boolean playerAutoMove;
	private transient boolean musicStartFlag;
	private transient Thread musicThread;
	private transient boolean saveState;
	private String mapName;
	private Abilities playerMaxAbilities;
	private boolean cannotReadSaveFile;
	
	
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
		dialogScreen.setText(null);
		//����â
		statusScreen = new GameUtilityInformation();
		//������
		levelUpImage = new GameUtilityInformation();
		//����ġ ���̾�α�
		switchDialog = new GameUtilityInformation();
		switchDialog.setText(null);
		switchDialog.setPosition(0);
		
		gameWindow = null;
		gameDisplay = null;
		gameRobot = new AI(this);
		
		conditionFlag = new boolean[MAXFLAG];
		for(int i = 0 ; i < MAXFLAG; i++)
			conditionFlag[i] = false;
		conditionFlag[0] = true;
		eventDispatcher = new GameEventDispatcher();
		gameMusic = null;
	}
	
	
	//���� ������ ����
	@Override
	public void run() {
		
		while(this.gameState != GameData.EXIT)
		{
			
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
				runAtNewStart();
			}
			else if(gameState == GameData.SAVE)
			{
				this.saveState = true;
				charOnMapX = player.getxPosition()/mapCharArrayRatio;
				charOnMapY = player.getyPosition()/mapCharArrayRatio;
				playerMaxAbilities = new Abilities();
				playerMaxAbilities.setAgility(player.getMaxStatus().getAgility());
				playerMaxAbilities.setEXP(player.getMaxStatus().getEXP());
				playerMaxAbilities.setHP(player.getMaxStatus().getHP());
				playerMaxAbilities.setIntelligence(player.getMaxStatus().getIntelligence());
				playerMaxAbilities.setKnowledge(player.getMaxStatus().getKnowledge());
				playerMaxAbilities.setMP(player.getMaxStatus().getMP());
				playerMaxAbilities.setStrength(player.getMaxStatus().getStrength());
				playerMaxAbilities.setVitality(player.getMaxStatus().getVitality());
				try {
					File file = new File("record.save");
					file.createNewFile();
					FileOutputStream fis = new FileOutputStream(file);
					ObjectOutputStream ois = new ObjectOutputStream(fis);
					ois.writeObject(this);
					Thread.sleep(SLOWTIMER*5);
					ois.close();
					fis.close();
					
				} catch (FileNotFoundException e) {
					
					System.out.println("������ ã�� �� �����ϴ�.");
					e.printStackTrace();
					System.exit(0);
				} catch (IOException e) {
					
					System.out.println("������ ã�� �� �����ϴ�.");
					e.printStackTrace();
					System.exit(0);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				saveState = false;
				gameState = GameData.STATUSCALLED;
			}
			else if(gameState == GameData.LOAD)
			{
				File file = new File("record.save");
				if(!file.canRead())
				{
					gameState = GameData.NEWSTART;
				}
				try {
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					
					GameData tmp = (GameData) ois.readObject();
					this.conditionFlag = tmp.conditionFlag;
					this.mapName = tmp.mapName;
					this.player = tmp.player;
//					System.out.println(tmp.player.getMaxStatus().getEXP());
//					this.player.getMaxStatus().setEXP(tmp.player.getMaxStatus().getEXP());
					this.playerMaxAbilities = tmp.playerMaxAbilities;
					ois.close();
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					cannotReadSaveFile = true;
					try {
						Thread.sleep(SLOWTIMER*2);
					} catch (InterruptedException e3) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cannotReadSaveFile = false;
					gameState = GameData.NEWSTART;
					continue;
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					gameState = GameData.NEWSTART;
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameState = GameData.LOADING;
			}
			else if(gameState == GameData.LOADING)
			{
				BootstrapInfo bs = null;
				try
				{
					bs = Bootstrap.getBootstrap(gamePath);
				}
				catch(NullPointerException e)
				{
					JOptionPane.showMessageDialog(null, "�÷��̾� �ʱ� ��ġ ���� ����");
					e.printStackTrace();
					System.exit(0);
				}
				
				try {
					//�����δ� �̺�Ʈ ����ó�� ȣ���ؼ� ���� ����ؾ��� ��, �ʿ� ���� npc���� ������ �о �ε��Ѵ�.
					player.deployActorAbliity(gamePath, bs.getCharIndex(), player.getxPosition(), 
							player.getyPosition(), playerMaxAbilities);
					player.setActorState(GameCharacter.MOVESTATE);
					loadMap(this.mapName);
//					System.out.println(bs.getCharIndex());
					this.computeGameTile();
					Thread.sleep(SLOWTIMER);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch(NullPointerException e1)
				{
					cannotReadSaveFile = true;
					try {
						Thread.sleep(SLOWTIMER*2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cannotReadSaveFile = false;
					gameState = GameData.NEWSTART;
					continue;
				}
				TIMER = FASTTIMER;
				gameState = GameData.PLAY;
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
				Thread.sleep(TIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		this.exitGame();
	}
	//���� �����ϰ��
	private void runAtNewStart()
	{
		exGameState = GameData.NEWSTART;
		//�����δ� �̺�Ʈ ����ó�� ȣ���ؼ� ���� ����ؾ��� ��, �ʿ� ���� npc���� ������ �о �ε��Ѵ�.
		gameState = GameData.LOADING;
		//��Ʈ��Ʈ�� �̺�Ʈ ����
		BootstrapInfo bs = null;
		try
		{
			bs = Bootstrap.getBootstrap(gamePath);
		}
		catch(NullPointerException e)
		{
			JOptionPane.showMessageDialog(null, "�÷��̾� �ʱ� ��ġ ���� ����");
			e.printStackTrace();
			System.exit(0);
		}
		//�켱 �� �ε�
		try{
		
			loadMap(bs.getMapName());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "���� ã�� ���� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
		//�÷��̾� �ε�
		try {
			Point startPoint = bs.getStartPoint();
			int charIndex = bs.getCharIndex();
			loadPlayer(charIndex, startPoint);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "ĳ���͸� ã�� ���� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
		//startMusic("D:\\Download\\Music\\Gamma Ray - Discography\\2001 - No World Order!\\01 - Introduction.mp3");
		/****************************************************/
		this.computeGameTile();
		
		gameState = GameData.PLAY;
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
				Thread.sleep(SLOWTIMER);
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
		if(player.getNowStatus().getHP() < player.getMaxStatus().getHP() && player.getNowStatus().getHP() > 0 )
		{
			player.getNowStatus().setHP(player.getNowStatus().getHP() + player.level);
		}
		if(player.getNowStatus().getHP() > player.getMaxStatus().getHP())
		{
			player.getNowStatus().setHP(player.getMaxStatus().getHP());
		}
		//�÷��̾��� �׼ǻ���Ȯ��, ����ų� ��Ʋ�̸�
		if (player.getActorState() == GameCharacter.MOVESTATE
				|| player.getActorState() == GameCharacter.BATTLESTATE) 
		{
			//�ִϸ��̼��� �ε巴��
			this.animTimer++;
			
			//�÷��̾��� Ű�Է¿� ���� ������ ����
			actionPlayer(player.getActorState());
			//npc���� ������
			gameRobot.NPCAction(alliances, player, gameMap, gameTile);

			//���� ������
			gameRobot.monsterAction(monsters, player, gameMap, gameTile);
			
			//System.out.println("ȣ����");
			//�Ѳ����� �迭�� ������ �ۼ�
			this.computeGameTile();

			if(nowEventList == null)
			{
				if(charEnterMap == true && (player.getxPosition()/mapCharArrayRatio != charOnMapX ||
						player.getyPosition()/mapCharArrayRatio != charOnMapY))
				{
					charEnterMap = false;
				}
				//�ڵ��̺�Ʈ ����
				if(autoEventCalled == false)
					computeMapAutoEvent();
				//���� ���� �̺�Ʈ�� ���µ�
				//ĳ���� �ڵ��̺�Ʈ�� �ִٸ�
				if(autoAllianceEventCalled == false && nowEventList == null)
					computeAutoAlliances();
				if(autoMonsterEventCalled == false&& nowEventList == null)
					computeAutoMonsters();
				if(charEnterMap == false && nowEventList == null)
					runMapEventOnTile();
				if((keyFlag.isAction() || keyFlag.isEnter())&& nowEventList == null)
				{
					runMapEventAction();
					try {
						Thread.sleep(SLOWTIMER);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//�������� �̺�Ʈ�� ���ٸ�
			if(eventStart == false)
			{
				computeNowEvent();
				return;
			}
			runEvent();
		}
		else if(player.getActorState() == GameCharacter.EVENTSTATE || player.getActorState() == GameCharacter.MOVEEVENTSTATE)
		{
			this.animTimer++;
			
			if(nowEventList == null)
			{
				if(charEnterMap == true && (player.getxPosition()/mapCharArrayRatio != charOnMapX ||
						player.getyPosition()/mapCharArrayRatio != charOnMapY))
				{
					charEnterMap = false;
				}
				//�ڵ��̺�Ʈ ����
				if(autoEventCalled == false)
					computeMapAutoEvent();
				//���� ���� �̺�Ʈ�� ���µ�
				//ĳ���� �ڵ��̺�Ʈ�� �ִٸ�
				if(autoAllianceEventCalled == false && nowEventList == null)
					computeAutoAlliances();
				if(autoMonsterEventCalled == false&& nowEventList == null)
					computeAutoMonsters();
				if(charEnterMap == false && nowEventList == null)
					runMapEventOnTile();
				if((keyFlag.isAction() || keyFlag.isEnter())&& nowEventList == null)
				{
					runMapEventAction();
					try {
						Thread.sleep(SLOWTIMER);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//�������� �̺�Ʈ�� ���ٸ�
			if(eventStart == false)
			{
				computeNowEvent();
				return;
			}
			runEvent();
		}
	}
	
	//Ÿ�� ���� �ִ� ���̺�Ʈ ����
	private void runMapEventOnTile()
	{
		charOnMapX = player.getxPosition()/mapCharArrayRatio;
		charOnMapY = player.getyPosition()/mapCharArrayRatio;
		
		//�� �̺�Ʈ�� �ִ� ��쿡�� �̺�Ʈ Ÿ�� �޾ƿ�
		if(eventDispatcher.hasMapEvent(charOnMapX, charOnMapY))
		{
			//�������������� ����ǹǷ�
			//this.charEnterMap = true;
			
			//�̺�Ʈ �޾ƿ� �״� �ʱ�ȭ
			nowEventTile = eventDispatcher.getEventTile(charOnMapX, charOnMapY);
			List<Event> eventList = nowEventTile.getEventList();
			//�̺�Ʈ�� �߿� ���ǿ� �´°� ã��
			for(int i = 0 ; i < eventList.size(); i++)
			{
				Event nextEvent = eventList.get(i);
				int [] condition = nextEvent.getPreconditionFlagArray();
				//���� ������ ������
				if((nextEvent.getStartType() == EventEditorSystem.ABOVE_EVENT_TILE ||
						nextEvent.getStartType() == EventEditorSystem.CONTACT_WITH_PLAYER)&&
						conditionFlag[condition[0]+1]==true&&
						conditionFlag[condition[1]+1]==true&&
						conditionFlag[condition[2]+1]==true)
				{
					int now = i;
					this.charEnterMap = true;
					nowEventList = nextEvent;
					eventContentListIndex = 0;
					i = now;
					return;
				}
			}//for����
		}//if����
		
	}
	
	private void runMapEventAction()
	{
		int charX = player.getxPosition()/mapCharArrayRatio;
		int charY = player.getyPosition()/mapCharArrayRatio;
		
		//�� �̺�Ʈ�� �ִ� ��쿡�� �̺�Ʈ Ÿ�� �޾ƿ�
		if(eventDispatcher.hasMapEvent(charX, charY))
		{
			//�̺�Ʈ �޾ƿ� �״� �ʱ�ȭ
			nowEventTile = eventDispatcher.getEventTile(charX, charY);
			List<Event> eventList = nowEventTile.getEventList();
			//�̺�Ʈ�� �߿� ���ǿ� �´°� ã��
			for(int i = 0 ; i < eventList.size(); i++)
			{
				Event nextEvent = eventList.get(i);
				int [] condition = nextEvent.getPreconditionFlagArray();
				//���� ������ ������
				if(nextEvent.getStartType() == EventEditorSystem.PRESS_BUTTON&&
						conditionFlag[condition[0]+1]==true&&
						conditionFlag[condition[1]+1]==true&&
						conditionFlag[condition[2]+1]==true)
				{
					nowEventList = nextEvent;
					eventContentListIndex = 0;
					return;
				}
			}//for����
		}//if����
	}
	
	//���� �ڵ��̺�Ʈ ã��
	private void computeMapAutoEvent()
	{
		//List<Event> autoEvents = null;
		nowEventList = null;
		if(autoEvents != null && autoEvents.size() != 0)
		{
			//System.out.println("�ڵ��̺�Ʈ ��� ȣ���");
			eventContentListIndex = 0;
			//�ڵ� �̺�Ʈ ����Ʈ�� ���� ������ �´°� �޾ƿ�
			for(int i = 0 ; i < autoEvents.size(); i++)
			{
				int [] conditionIndex = autoEvents.get(i).getPreconditionFlagArray();
				if(this.conditionFlag[conditionIndex[0]+1] == true&&
						this.conditionFlag[conditionIndex[1]+1] == true&&
						this.conditionFlag[conditionIndex[2]+1] == true)
				{
					nowEventList = autoEvents.get(i);
					autoEvents.remove(i);
					autoEventCalled = true;
					break;
				}
			}
		}
	}
	
	//���� ���õ� �̺�Ʈ ����
	private void computeNowEvent()
	{
		if(nowEventList == null || eventContentListIndex >= nowEventList.getEventContentList().size())
		{
			//�̺�Ʈ����Ʈ �ϳ��� �� �����ٸ� �̺�Ʈ ����
			nowEventList = null;
			eventContentListIndex = 0;
			this.eventStart = false;
			nowEvent = null;
			//autoEvents = null;
			autoEventCalled = false;
			eventStart = false;
			autoAllianceEventCalled = false;
			autoMonsterEventCalled = false;
			moveEventCalled = false;
			//charActionMap = false;
			//charEnterMap = false;
//			player.setActorState(GameCharacter.MOVESTATE);
		}
		else
		{
			//�̺�Ʈ ����
			eventStart = true;
			//���� ���õ� ����Ʈ���� �ϳ� �޾ƿ�
			nowEvent = nowEventList.getEventContentList().get(eventContentListIndex);
		}
	}
	
	//���� ���õ� �̺�Ʈ ����
	private void runEvent()
	{
		if(nowEvent == null)
		{
			eventContentListIndex++;
			//player.setActorState(GameCharacter.MOVESTATE);
			return;
		}
		
		int type = nowEvent.getContentType();
		//eventContentListIndex++�� eventStart���� �������
		
		//���� �̺�Ʈ�� ���� Ȯ��
		if(type == EventContent.CHANGE_BGM_EVNET)
		{
			startMusicEvent();
		}
		else if(type == EventContent.DIALOG_EVNET)
		{
			//���̾�α� ��� �̺�Ʈ
			startDialogEvent();
		}
		else if(type == EventContent.CHANGE_MAP_EVNET)
		{
			//�� ��ȯ �̺�Ʈ
			startChangeMapEvent();
		}
		else if(type == EventContent.CHANGE_FLAG_EVENT)
		{
			//�÷��� ��ȯ �̺�Ʈ
			startChangeFlagEvent();
		}
		else if(type == EventContent.GAMEOVER_EVNET)
		{
			//���� ����
			gameState = GameData.GAMEOVER;
		}
		else if(type == EventContent.SWITCH_DIALOG_EVNET)
		{
			//����ġ ���̾�α�
			startSwitchDialog();
		}
		else if(type == EventContent.MOTION_EVNET)
		{
			startMoveEvent();
		}
		else
		{
			nowEventList = null;
			eventContentListIndex++;
		}
	}
	
	//���� �̵� �̺�Ʈ
	private void startMoveEvent()
	{
		player.setActorState(GameCharacter.MOVEEVENTSTATE);
//		System.out.println(player.getActorState());
		MotionEvent moveEvent = (MotionEvent)nowEvent;
		//ó�� �ҷǳ�?
		if(moveEventCalled == false)
		{
			moveEventSpeed = moveEvent.getSpeed();
			moveEventDirection = moveEvent.getDirection();
			moveEventDestination = moveEvent.getCountMove()*mapCharArrayRatio;
			moveEventcounter=0;
		}
		//�÷��̾ ������������ ���
		if(moveEvent.getActorType() == MotionEvent.PLAYER)
		{
			if(moveEventCalled == false)
			{
				playerAutoMove = true;
				moveEventCalled = true;
			}
			
			player.setNowDirection(moveEventDirection);
			//player.setSpeed(moveEventSpeed);
			if(player.getNowDirection() == GameCharacter.UP)
				player.setyPosition(player.getyPosition()-moveEventSpeed);
			else if(player.getNowDirection() == GameCharacter.DOWN)
				player.setyPosition(player.getyPosition()+moveEventSpeed);
			else if(player.getNowDirection() == GameCharacter.LEFT)
				player.setxPosition(player.getxPosition()-moveEventSpeed);
			else if(player.getNowDirection() == GameCharacter.RIGHT)
				player.setxPosition(player.getxPosition()+moveEventSpeed);
			moveEventcounter++;
			if(moveEventcounter >= moveEventDestination)
			{
				player.setActorState(GameCharacter.MOVESTATE);
				eventContentListIndex++;
				eventStart = false;
				playerAutoMove = false;
			}
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			if(moveEventCalled == false)
			{
				moveEventActor = null;
				for(int i = 0 ; i < sortedCharacters.size(); i++)
				{
					if(nowEventTile.equals(sortedCharacters.elementAt(i).getTotalEvent()))
					{
						moveEventActor = sortedCharacters.elementAt(i);
						break;
					}
				}
				moveEventCalled = true;
			}
			moveEventActor.setNowDirection(moveEventDirection);
			if(moveEventActor.getNowDirection() == GameCharacter.UP)
				moveEventActor.setyPosition(moveEventActor.getyPosition()-moveEventSpeed);
			else if(moveEventActor.getNowDirection() == GameCharacter.DOWN)
				moveEventActor.setyPosition(moveEventActor.getyPosition()+moveEventSpeed);
			else if(moveEventActor.getNowDirection() == GameCharacter.LEFT)
				moveEventActor.setxPosition(moveEventActor.getxPosition()-moveEventSpeed);
			else if(moveEventActor.getNowDirection() == GameCharacter.RIGHT)
				moveEventActor.setxPosition(moveEventActor.getxPosition()+moveEventSpeed);
			moveEventcounter++;
			
			if(moveEventcounter >= moveEventDestination)
			{
				player.setActorState(GameCharacter.MOVESTATE);
				eventContentListIndex++;
				eventStart = false;
			}
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(player.getActorState() != GameCharacter.MOVEEVENTSTATE)
			player.setActorState(GameCharacter.MOVESTATE);
	}
	
	//����ġ ���̾�α�
	private void startSwitchDialog()
	{
		try {
			Thread.sleep(GameData.FASTTIMER);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ĳ���� ���� �̺�Ʈ�� ����
		player.setActorState(GameCharacter.EVENTSTATE);
		//��ȭâ ����
		SwitchDialogEvent switchDlgEv = (SwitchDialogEvent) nowEvent;
//		switchDialog.setText(switchDlgEv.getQuestion());
		List<Switch>switchList = switchDlgEv.getSwitchList();
		int size = switchList.size();
		switchDialog.setEndPosition(size);
		String text = switchDlgEv.getQuestion();

		switchDialog.setText(text);
		if(keyFlag.isUp())
		{
			if(switchDialog.getPosition() == 0) switchDialog.setPosition(size-1);
			else switchDialog.setPosition(switchDialog.getPosition() - 1);
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(keyFlag.isDown())
		{
			if(switchDialog.getPosition() == size-1) switchDialog.setPosition(0);
			else switchDialog.setPosition(switchDialog.getPosition() + 1);
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(keyFlag.isAction() || keyFlag.isEnter())
		{
			for(int i = 0 ; i < size; i++)
			{
				if(switchDialog.getPosition() == i)
				{
					if(switchDlgEv.getSwitch(i).getState() == true)
					{
						conditionFlag[switchDlgEv.getSwitch(i).getFlagIndex()+1] = true;
					}
					else
					{
						conditionFlag[switchDlgEv.getSwitch(i).getFlagIndex()+1] = false;
					}
					try {
						deployNPC();
						deployMonsters();
						Thread.sleep(FASTTIMER);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					eventContentListIndex++;
					this.eventStart = false;
					switchDialog.setText(null);
					switchDialog.setPosition(0);
					player.setActorState(GameCharacter.MOVESTATE);
					break;
				}
			}
		}
		
	
	}
	
	//�÷��� ��ȯ �̺�Ʈ
	private void startChangeFlagEvent()
	{
		ChangeFlagEvent flagChange = (ChangeFlagEvent) nowEvent;
		if(flagChange.isState())
		{
			conditionFlag[flagChange.getIndexFlag()+1] = true;
		}
		else
			conditionFlag[flagChange.getIndexFlag()+1] = false;
					
		eventContentListIndex++;
		this.eventStart = false;
		deployNPC();
		deployMonsters();
		try {
			Thread.sleep(FASTTIMER);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//���� �̺�Ʈ
	private void startMusicEvent()
	{
		ChangeBGMEvent bgm = (ChangeBGMEvent) nowEvent;
		
		if(gameMusic != null)
		{
			gameMusic.setIsMusicStart(false);
			gameMusic.startMusic(null);
			
		}

		//gameMusic.startMusic(null);
		gameMusic = new GameMusic(this);
		gameMusic.setIsMusicStart(false);
		gameMusic.startMusic(gamePath + "/"+bgm.getFileName());
		musicThread = new Thread(gameMusic);
		try {
			Thread.sleep(SLOWTIMER*2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		musicThread.start();
//		gameMusic.startMusic(bgm.getSrcFileName());
		
		eventContentListIndex++;
		eventStart = false;
	}

	//�ʺ�ȯ �̺�Ʈ
	private void startChangeMapEvent()
	{
		gameState = GameData.LOADING;
		ChangeMapEvent mapChange = (ChangeMapEvent) nowEvent;
//		startMusic(null);
		loadMap(mapChange.getMapName());
		autoEventCalled = false;
		
		Point next = mapChange.getStartPoint();
		player.setxPosition(next.x*mapCharArrayRatio);
		player.setyPosition(next.y*mapCharArrayRatio);
		gameState = GameData.PLAY;
		eventContentListIndex++;
		this.eventStart = false;
	}
	
	//���̾�α� �̺�Ʈ
	private void startDialogEvent()
	{
		try {
			Thread.sleep(GameData.FASTTIMER);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ĳ���� ���� �̺�Ʈ�� ����
		player.setActorState(GameCharacter.EVENTSTATE);
		//��ȭâ ����
		DialogEvent dialog = (DialogEvent)nowEvent;
//		System.out.println("���̾�1");
		dialogScreen.setText(dialog.getText());
		try {
			Thread.sleep(SLOWTIMER);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		System.out.println("���̾�2");
		//����Ű�� �׼� Ű�� ������
		if(keyFlag.isAction() || keyFlag.isEnter())
		{
			//System.out.println("���̾�3");
			
			eventContentListIndex++;
			// Thread.sleep(FASTTIMER);
			dialogScreen.setText(null);
			this.eventStart = false;
			player.setActorState(GameCharacter.MOVESTATE);
			return;
		}
		try {
			Thread.sleep(FASTTIMER/2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				gameState = GameData.PLAY;
				player.setActorState(GameCharacter.MOVESTATE);
				TIMER = FASTTIMER;
			}
			else if(cursorImage.getPosition() == 1)
			{
				//����
				gameState = GameData.SAVE;
				saveState = true;
			}
			else if(cursorImage.getPosition() == 3)
			{
				gameState = GameData.EXIT;
			}
		}
		else if(keyFlag.isCancel())
		{
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
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
				if(alliance.getCharacter() == null)
					continue;
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
				if(alliance != null )//&& chk == true)
				{
					sortedCharacters.add(alliance);
				}
			}
//			chk = false;
		}
		if(monsters!=null)
		{
			for(int indexOfMonsters = 0 ; indexOfMonsters < monsters.size(); indexOfMonsters++)
			{
				GameCharacter monster = monsters.elementAt(indexOfMonsters);
				if(monster.getCharacter() == null)
					continue;
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
				if(monster != null)//&& chk == true)
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
			
			if(actor.getCharacter() == null)
				continue;
			
			for(int i = 0 ; i < ratio; i++)
			{
				for(int j = 0 ; j < ratio; j++)
				{
					try{
					gameTile[actor.getyPosition()-ratio/2+i][actor.getxPosition()-ratio/2+i] = count;
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
//						e.printStackTrace();
//						System.out.println("" + (actor.getyPosition()-ratio/2+i) +" : " + (actor.getxPosition()-ratio/2+i));
//						System.exit(0);1
						continue;
					}
				}
			}
			count++;
		}
	}

	//�÷��̾��� Ű�Է¿� ���� ������ ����
	public void actionPlayer(int playerState)
	{	
//		System.out.println(player.getActorState());
		if(player.getActorState() == GameCharacter.MOVEEVENTSTATE)
		{
//			System.out.println("���� �Ҹ�");
			return;
		}
		
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
		else if(keyFlag.isAction() && this.actionAnimFlag == false )//&& player.getActorState() == GameCharacter.BATTLESTATE)
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
				System.out.println("���� �ִϸ��̼� ����");
				System.exit(-1);
			}

			if(player.getAnimActionClock() > error/2)
			{
				actionAnimFlag = false;
				player.setAnimActionClock(0);
			}
		}	
	}

	//�ʿ� npc�� ����
	private void createMapNPC()
	{
		if(alliances == null)
			alliances = new Vector<GameCharacter>();
		int count = 0;
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			if(actorEventTiles == null)
			{
				return;
			}
			//�ڵ��̺�Ʈ�� �÷��׿� �°� �����ϸ� ��ġ�ϰ� �ڵ��̺�Ʈ �ʿ� ���� �ִ´�.
			for(int i = 0 ; i < actorEventTiles.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				EventTile actorEventTile = actorEventTiles.get(i);
				//�����̸� ����
				if(actorEventTile.getObjectType() != EventEditorSystem.NPC_EVENT)
					continue;
				
				alliances.add(new Alliance(gamePath));
				alliances.elementAt(count).setTotalEvent(actorEventTile);
				alliances.elementAt(count).setActionType(GameCharacter.STOP);
				count++;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("loadCharNPC����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			alliances = null;
		}
	}
	private void createMapMonsters()
	{
		if(monsters == null)
			monsters = new Vector<GameCharacter>();
		int count = 0;
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			if(actorEventTiles == null)
			{
				return;
			}
			//�ڵ��̺�Ʈ�� �÷��׿� �°� �����ϸ� ��ġ�ϰ� �ڵ��̺�Ʈ �ʿ� ���� �ִ´�.
			for(int i = 0 ; i < actorEventTiles.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				EventTile actorEventTile = actorEventTiles.get(i);
				//�����̸� ����
				if(actorEventTile.getObjectType() != EventEditorSystem.MONSTER_EVENT)
					continue;
				
				monsters.add(new Monster(gamePath));
				monsters.elementAt(count).setTotalEvent(actorEventTile);
				monsters.elementAt(count).setActorState(GameCharacter.EVENTSTATE);
				monsters.elementAt(count).setActionType(GameCharacter.TOPLAYER);
				count++;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("createMonsters ����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			monsters = null;
		}
	}
	
	//��ġ
	private void deployNPC()
	{
		if(alliances == null)
			return;
		try{
			//npc���߿��� �ڵ��̺�Ʈ�� ������ ���ǿ� �´� �ֵ� ��ġ
			for(int i = 0 ; i < alliances.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				nowEventTile = alliances.elementAt(i).getTotalEvent();
				//Ÿ�Ͽ� �ִ� ���� �̺�Ʈ ����Ʈ �߿���
				List<Event> actorEventList = nowEventTile.getEventList();
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();

					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() != EventEditorSystem.AUTO_START)
					{
						alliances.elementAt(i).changeActor(eventList.getActorIndex(),
								alliances.elementAt(i).getxPosition(), alliances.elementAt(i).getyPosition());

						//ĳ���� ��ġ
						if(eventList.getActionType() == EventEditorSystem.RANDOM_MOTION)
						{
							alliances.elementAt(i).setActionType(GameCharacter.RANDOM);
						}
						else if(eventList.getActionType() == EventEditorSystem.COME_CLOSER_TO_PLAYER)
						{
							alliances.elementAt(i).setActionType(GameCharacter.TOPLAYER);
						}
						else if(eventList.getActionType() == EventEditorSystem.NOT_MOTION)
						{
							alliances.elementAt(i).setActionType(GameCharacter.STOP);
						}
						else
							alliances.elementAt(i).setActionType(GameCharacter.TOPLAYER);
						break;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("deployNPC ����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
		//	alliances = null;
		}
	}
	
	private void deployMonsters()
	{
		if(monsters == null)
			return;
		try{
			//npc���߿��� �ڵ��̺�Ʈ�� ������ ���ǿ� �´� �ֵ� ��ġ
			for(int i = 0 ; i < monsters.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				nowEventTile = monsters.elementAt(i).getTotalEvent();
				//Ÿ�Ͽ� �ִ� ���� �̺�Ʈ ����Ʈ �߿���
				List<Event> actorEventList = nowEventTile.getEventList();
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();

					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() != EventEditorSystem.AUTO_START)
					{
						monsters.elementAt(i).changeActor(eventList.getActorIndex(),
								monsters.elementAt(i).getxPosition(), monsters.elementAt(i).getyPosition());
						monsters.elementAt(i).setActorState(GameCharacter.MOVESTATE);
						break;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("deployMonsters ����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
		//	monsters = null;
		}
	}
	
	//npc�� �߿� �ڵ��̺�Ʈ ����
	private void computeAutoAlliances() {
		// TODO Auto-generated method stub
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			//nowEventList = null;
			eventContentListIndex = 0;
			//�ڵ��̺�Ʈ�� �÷��׿� �°� �����ϸ� ��ġ�ϰ� �ڵ��̺�Ʈ �ʿ� ���� �ִ´�.
			for(int i = 0 ; i < alliances.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				nowEventTile = alliances.elementAt(i).getTotalEvent();
				//�����̸� ����
				if(nowEventTile.getObjectType() != EventEditorSystem.NPC_EVENT)
					continue;
				//Ÿ�Ͽ� �ִ� ���� �̺�Ʈ ����Ʈ �߿���
				List<Event> actorEventList = nowEventTile.getEventList();
				//�ڵ����� �̺�Ʈ�� Ȯ��
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();
					
					//��� ������ �����Ǿ����� �ڵ� ���� �̺�Ʈ�̸� �ϳ� ���
					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() == EventEditorSystem.AUTO_START)
					{
						//System.out.println("���� �Ҹ�");
						alliances.elementAt(i).changeActor(eventList.getActorIndex(),
								alliances.elementAt(i).getxPosition(), alliances.elementAt(i).getyPosition());
						//������ �̺�Ʈ ����
						nowEventList = eventList;
						actorEventList.remove(j);
						autoAllianceEventCalled = true;
						//ĳ���� ��ġ
						if(eventList.getActionType() == EventEditorSystem.RANDOM_MOTION)
						{
							alliances.elementAt(i).setActionType(GameCharacter.RANDOM);
						}
						else if(eventList.getActionType() == EventEditorSystem.COME_CLOSER_TO_PLAYER)
						{
							alliances.elementAt(i).setActionType(GameCharacter.TOPLAYER);
						}
						else if(eventList.getActionType() == EventEditorSystem.NOT_MOTION)
						{
							alliances.elementAt(i).setActionType(GameCharacter.STOP);
						}
						else
							alliances.elementAt(i).setActionType(GameCharacter.TOPLAYER);

						return;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("npc�ڵ��̺�Ʈ����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			alliances = null;
		}
	}
	
	//���� �߿� �ڵ��̺�Ʈ ����
	private void computeAutoMonsters() {
		try{
			//���� �ʿ� ���ǵ� npc�� ���
			//nowEventList = null;
			eventContentListIndex = 0;
			if(monsters == null)
				return;
			//�ڵ��̺�Ʈ�� �÷��׿� �°� �����ϸ� ��ġ�ϰ� �ڵ��̺�Ʈ �ʿ� ���� �ִ´�.
			for(int i = 0 ; i < monsters.size(); i++)
			{
				//Ÿ�� �ϳ��� ������
				nowEventTile = monsters.elementAt(i).getTotalEvent();
				//�����̸� ����
				if(nowEventTile.getObjectType() != EventEditorSystem.MONSTER_EVENT)
					continue;
				//Ÿ�Ͽ� �ִ� ���� �̺�Ʈ ����Ʈ �߿���
				List<Event> actorEventList = nowEventTile.getEventList();
				//�ڵ����� �̺�Ʈ�� Ȯ��
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();
					
					//��� ������ �����Ǿ����� �ڵ� ���� �̺�Ʈ�̸� �ϳ� ���
					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() == EventEditorSystem.AUTO_START)
					{
						//System.out.println("���� �Ҹ�");
						monsters.elementAt(i).changeActor(eventList.getActorIndex(),
								monsters.elementAt(i).getxPosition(), monsters.elementAt(i).getyPosition());
						monsters.elementAt(i).setActorState(GameCharacter.MOVESTATE);
						//������ �̺�Ʈ ����
						nowEventList = eventList;
						actorEventList.remove(j);
						autoMonsterEventCalled = true;
						return;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("�����ڵ�����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			monsters = null;
		}
	}
	
	//�ʷε�
	public void loadMap(String mapName)
	{
		this.mapName = mapName;
		GameMapLoader mapLoader = new GameMapLoader();
		mapLoader.setMapFile(this.gamePath+"/Map/"+mapName);
		this.setGameMap(mapLoader.getMap());
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
			System.out.println("�ʷε忡��");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMap()\nCan't allocate gameTile");
			e.printStackTrace();
		//	System.exit(0);
		}
		//�� �̹��� ����
		//��׶��� ����
		background = gameMap.getM_Background();
		Graphics2D g = background.createGraphics();
		foreBackImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		displayForeground(g, false);
		g.dispose();
		foreForeImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		g = foreForeImage.createGraphics();
		displayForeground(g, true);
		g.dispose();
		
		//�̺�Ʈ Ÿ�� ����
		eventDispatcher.refrash();
		eventDispatcher.setEventLoader(gameMap.getEventEditSys(), this);
		eventDispatcher.makeMapEvent(gameMap.getM_Width(), gameMap.getM_Height());
		//computeMapAutoEvent();
		autoEvents = null;
		autoEvents = eventDispatcher.getAutoEvents();
		autoEventCalled = false;
		autoAllianceEventCalled = false;
		autoMonsterEventCalled = false;
		eventContentListIndex = 0;
		eventStart = false;
		nowEventList = null;
		
		eventDispatcher.makeAlliances();
		//npc��� ���� �ε��ؾ���
		//computeAutoAlliances();
		if(alliances!= null)
			alliances = null;
		alliances = new Vector<GameCharacter>();
		if(monsters != null)
			monsters = null;
		monsters = new Vector<GameCharacter>();
		actorEventTiles= eventDispatcher.getActors();
		createMapNPC();
		createMapMonsters();
		deployNPC();
		deployMonsters();
		sortCharacters();
	}

	//�÷��̾�ε�
	public void loadPlayer(int charIndex, Point startPoint)
	{
		try{
			this.player = new PlayerCharacter(gamePath);
			
			int startX = startPoint.x*mapCharArrayRatio-mapCharArrayRatio/2;
			int startY = startPoint.y*mapCharArrayRatio-mapCharArrayRatio/2;
			
			player.deployActor(charIndex, startX+5, startY+5, null);
//			player.setNowEXP(1909);
//			player.getMaxStatus().setEXP(10);
		}
		catch(Exception e)
		{
			System.out.println("�÷��̾�ε� ����");
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadPlayer()");
			e.printStackTrace();
			//System.exit(0);
		}
	}
	
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

		utilPath = gamePath+"\\.UtilImages";
		try 
		{
			titleScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\TITLE.png")));
			logoScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\LOGO.png")));
			cursorImage.setUtilImage(ImageIO.read(new File(utilPath+"\\CURSOR.png")));
			loadScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\LOADING.png")));
			gameOver.setUtilImage(ImageIO.read(new File(utilPath+"\\GAMEOVER.png")));
			levelUpImage.setUtilImage(ImageIO.read(new File(utilPath+"\\levelUp.Png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//���࿡ Ÿ��Ʋ�� �ҷ����µ� �����ص� ����
			System.out.println("Ÿ��Ʋ����");
			JOptionPane.showMessageDialog
			(gameWindow, "Error!! Can't find TITLE.png or LOGO.png or CURSOR.png or SomthingElse");
			e.printStackTrace();
			System.exit(0);
		}

	}
	
	
	public void setGameWindow(GameWindow gameWindow) 
	{
		this.gameWindow = gameWindow;
		statusScreen.setFont(new Font("����", Font.BOLD , gameWindow.getWidth()/35 ));
		titleScreen.setFont(new Font("Courier New", Font.BOLD , gameWindow.getWidth()/25));
		dialogScreen.setFont(new Font("����", Font.BOLD , gameWindow.getWidth()/50));
		switchDialog.setFont(new Font("����", Font.BOLD , gameWindow.getWidth()/50));
	}

	public GameWindow getGameWindow() {
		return gameWindow;
	}

	public void exitGame() {
		// TODO Auto-generated method stub
		gameDisplay.exitGame();
		gameWindow.dispose();
		try
		{
			gameMusic.setIsMusicStart(false);
			gameMusic.startMusic(null);
		}
		catch(NullPointerException e)
		{
			return;
		}
		//System.exit(0);
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

	public GameUtilityInformation getCursorImage() {
		return cursorImage;
	}

	public int getExGameState() {
		return exGameState;
	}


	public void setExGameState(int exGameState) {
		this.exGameState = exGameState;
	}

	public GameCharacter getPlayer() {
		return player;
	}

	public Vector<GameCharacter> getMonsters() {
		return monsters;
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
		numb*=5;
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



	public GameUtilityInformation getLevelUpImage() {
		return levelUpImage;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public BufferedImage getForeBackImage() {
		return foreBackImage;
	}
	public BufferedImage getForeForeImage() {
		return foreForeImage;
	}
	public void displayForeground( Graphics2D g, boolean isUpper)
	{
		Tile[][] mapTile = gameMap.getM_ForegroundTile();

		if(isUpper == false)
		{
//			//ĳ�� �Ʒ��� �򸮴� �� ���� ���
			for(int i = 0 ; i < mapTile.length; i++)
			{
				for(int j = 0 ; j < mapTile[0].length; j++)
				{
					if(mapTile[i][j].getIsUpper() == false)
					{
						g.drawImage(mapTile[i][j].getM_TileIcon(), j*DrawingTemplate.pixel,
								i*DrawingTemplate.pixel,
								mapTile[i][j].getM_TileIcon().getWidth(),
								mapTile[i][j].getM_TileIcon().getHeight(),
								null);
					}
				}
			}
		}
		else
		{
			for(int i = 0 ; i < mapTile.length; i++)
			{
				for(int j = 0 ; j < mapTile[0].length; j++)
				{
					if(mapTile[i][j].getIsUpper() == true)
					{
						g.drawImage(mapTile[i][j].getM_TileIcon(), j*DrawingTemplate.pixel,
								i*DrawingTemplate.pixel,
								mapTile[i][j].getM_TileIcon().getWidth(),
								mapTile[i][j].getM_TileIcon().getHeight(),
								null);
					}
				}
			}
		}
	}


	public void setNowEvent(EventContent nowEvent) {
		this.nowEvent = nowEvent;
	}


	public EventContent getNowEvent() {
		return nowEvent;
	}


	public void setEventStart(boolean eventStart) {
		this.eventStart = eventStart;
	}


	public boolean isEventStart() {
		return eventStart;
	}


	public void setEventContentListIndex(int eventContentListIndex) {
		this.eventContentListIndex = eventContentListIndex;
	}


	public int getEventContentListIndex() {
		return eventContentListIndex;
	}


	public void setNowEventList(Event nowEventLost) {
		this.nowEventList = nowEventLost;
	}


	public Event getNowEventList() {
		return nowEventList;
	}


	public void setGameMusic(GameMusic gameMusic) {
		this.gameMusic = gameMusic;
	}


	public void setSwitchDialog(GameUtilityInformation switchDialog) {
		this.switchDialog = switchDialog;
	}


	public GameUtilityInformation getSwitchDialog() {
		return switchDialog;
	}


	public void setAutoMonsterEventCalled(boolean autoMonsterEventCalled) {
		this.autoMonsterEventCalled = autoMonsterEventCalled;
	}


	public boolean isAutoMonsterEventCalled() {
		return autoMonsterEventCalled;
	}


	public void setAutoAllianceEventCalled(boolean autoAllianceEventCalled) {
		this.autoAllianceEventCalled = autoAllianceEventCalled;
	}


	public boolean isAutoAllianceEventCalled() {
		return autoAllianceEventCalled;
	}


	public void setCharActionMap(boolean charActionMap) {
		this.charActionMap = charActionMap;
	}


	public boolean isCharActionMap() {
		return charActionMap;
	}


	public void setCharEnterMap(boolean charEnterMap) {
		this.charEnterMap = charEnterMap;
	}


	public boolean isCharEnterMap() {
		return charEnterMap;
	}


	public KeyFlags getKeyFlag() {
		return keyFlag;
	}


	public boolean[] getConditionFlag() {
		return conditionFlag;
	}


	public boolean isPlayerAutoMove() {
		return playerAutoMove;
	}
	
	public boolean isMusicStart()
	{
		return this.musicStartFlag;
	}


	public boolean isSaveState() {
		return saveState;
	}


	public boolean cannotReadSaveFile() {
		return cannotReadSaveFile;
	}
	
}
