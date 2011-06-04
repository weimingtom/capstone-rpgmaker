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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import viewControl.editorDlg.eventContentDlg.DialogEventDlg;

import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.EventTile;
import eventEditor.eventContents.ChangeBGMEvent;
import eventEditor.eventContents.ChangeFlagEvent;
import eventEditor.eventContents.ChangeMapEvent;
import eventEditor.eventContents.DialogEvent;
import eventEditor.eventContents.EventContent;
import eventEditor.eventContents.Switch;
import eventEditor.eventContents.SwitchDialogEvent;

import bootstrap.Bootstrap;
import bootstrap.BootstrapInfo;

import MapEditor.DrawingTemplate;
import MapEditor.Map;
import MapEditor.Tile;

public class GameData implements Runnable{
	
	
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
	public static final int ACTORLOAD = 11;
//	public static final int MAPLOADENDED = 12;
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
	private GameUtilityInformation switchDialog;
	
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
	private GameEventDispatcher eventDispatcher;
	private static final int MAXFLAG = 1001;
	private boolean [] conditionFlag;
	//���ο� ����Ʈ�� �޾ƿ;��ϳ�?
	private boolean eventStart = false;
	//���� �̺�Ʈ�� �������ΰ�?
	//private boolean eventStart = false;
	private Event nowEventList = null;
	private EventContent nowEvent = null;
	private int eventContentListIndex = 0;
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
	

	//�ʵ�����
	private BufferedImage background;
	private BufferedImage foreBackImage;
	private BufferedImage foreForeImage;
	private GameMusic gameMusic;

	//���� ������
	private Thread musicThread;
	private boolean autoEventCalled;
	private List<Event> autoEvents;
	
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
				runAtNewStart();
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
			System.exit(0);
		}
		//�켱 �� �ε�
		try{
		
			loadMap(bs.getMapName());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "���� ã�� ���� �����ϴ�.");
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
			System.exit(0);
		}
		//loadAlliances();
		//loadMonsters();
		loadAlliances();
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

			//�ڵ��̺�Ʈ ����
			if(autoEventCalled == false)
				computeMapAutoEvent();
			//�������� �̺�Ʈ�� ���ٸ�
			if(eventStart == false)
			{
				computeNowEvent();
			}
			runEvent();
		}
		else if(player.getActorState() == GameCharacter.EVENTSTATE)
		{
			this.animTimer++;
			
			//�ڵ��̺�Ʈ ����
			if(autoEventCalled == false)
				computeMapAutoEvent();
			//�Ѳ����� �迭�� ������ �ۼ�
			this.computeGameTile();
			//�������� �̺�Ʈ�� ���ٸ�
			if(eventStart == false)
			{
				computeNowEvent();
			}
			runEvent();
		}
		
		//ĳ���� ü�� ä����
		if(player.getNowStatus().getHP() < player.getMaxStatus().getHP() && player.getNowStatus().getHP() > 0 )
			player.getNowStatus().setHP(player.getNowStatus().getHP() + player.level);
		if(player.getNowStatus().getHP() > player.getMaxStatus().getHP())
		{
			player.getNowStatus().setHP(player.getMaxStatus().getHP());
		}
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
			return;
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
	}
	
	//����ġ ���̾�α�
	private void startSwitchDialog()
	{
		//ĳ���� ���� �̺�Ʈ�� ����
		player.setActorState(GameCharacter.EVENTSTATE);
		//��ȭâ ����
		SwitchDialogEvent switchDlgEv = (SwitchDialogEvent) nowEvent;
//		switchDialog.setText(switchDlgEv.getQuestion());
		List<Switch>switchList = switchDlgEv.getSwitchList();
		int size = switchList.size();
		switchDialog.setEndPosition(size);
		String text = switchDlgEv.getQuestion();
		for(int i = 0 ; i < size; i++)
		{
			text+="\n" + (i+1) + " " + switchDlgEv.getAnswer(i);
		}
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
			player.setActorState(GameCharacter.MOVESTATE);
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
						Thread.sleep(FASTTIMER);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					eventContentListIndex++;
					this.eventStart = false;
					switchDialog.setText(null);
					switchDialog.setPosition(0);
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
	}
	
	//���� �̺�Ʈ
	private void startMusicEvent()
	{
//		System.out.println("�ҷ���");
		//���ǽ���
		musicThread = null;
		musicThread = new Thread(gameMusic);
		musicThread.start();
//		startMusic(null);
		startMusic(((ChangeBGMEvent) nowEvent).getFileName());
		eventContentListIndex++;
		this.eventStart = false;
	}

	//�ʺ�ȯ �̺�Ʈ
	private void startChangeMapEvent()
	{
		gameState = GameData.LOADING;
		ChangeMapEvent mapChange = (ChangeMapEvent) nowEvent;
		startMusic(null);
		loadMap(mapChange.getMapName());
		autoEventCalled = false;
		
		Point next = mapChange.getStartPoint();
		player.setxPosition(next.y*mapCharArrayRatio);
		player.setyPosition(next.x*mapCharArrayRatio);
		gameState = GameData.PLAY;
		eventContentListIndex++;
		this.eventStart = false;
	}
	
	//���̾�α� �̺�Ʈ
	private void startDialogEvent()
	{
		//ĳ���� ���� �̺�Ʈ�� ����
		player.setActorState(GameCharacter.EVENTSTATE);
		//��ȭâ ����
		DialogEvent dialog = (DialogEvent)nowEvent;
//		System.out.println("���̾�1");
		dialogScreen.setText(dialog.getText());
//		System.out.println("���̾�2");
		//����Ű�� �׼� Ű�� ������
		if(keyFlag.isAction() || keyFlag.isEnter())
		{
//			System.out.println("���̾�3");
			dialogScreen.setText(null);
			eventContentListIndex++;
			this.eventStart = false;
			player.setActorState(GameCharacter.MOVESTATE);
			
			try {
				Thread.sleep(FASTTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			Thread.sleep(FASTTIMER);
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

			if(player.getAnimActionClock() > error/2)
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
//			monsters.add(new Monster(gamePath));
//			monsters.elementAt(0).deployActor(0, 60, 60, null);
//			monsters.add(new Monster(gamePath));
//			monsters.elementAt(1).deployActor(0, 20, 60, null);
//			monsters.add(new Monster(gamePath));
//			monsters.elementAt(2).deployActor(0, 30, 60, null);
//			monsters.add(new Monster(gamePath));
//			monsters.elementAt(3).deployActor(0, 40, 60, null);
		}
		catch(Exception e)
		{
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
//			alliances.add(new Alliance(gamePath));
//			alliances.elementAt(0).deployActor(0, 20, 20, null);
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
	public void loadMap(String mapName)
	{

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
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadMap()\nCan't allocate gameTile");
			System.exit(0);
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
		eventContentListIndex = 0;
		eventStart = false;
	}

	//�÷��̾�ε�
	public void loadPlayer(int charIndex, Point startPoint)
	{
		try{
			this.player = new PlayerCharacter(gamePath);
			
			int startX = startPoint.x*mapCharArrayRatio-mapCharArrayRatio/2;
			int startY = startPoint.y*mapCharArrayRatio-mapCharArrayRatio/2;
			
			player.deployActor(charIndex, startX+5, startY+5, null);
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
		utilPath = gamePath+"\\.DefaultData\\UtillImages";
		try 
		{
			titleScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\TITLE.png")));
			logoScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\LOGO.png")));
			cursorImage.setUtilImage(ImageIO.read(new File(utilPath+"\\CURSOR.png")));
			loadScreen.setUtilImage(ImageIO.read(new File(utilPath+"\\LOADING.png")));
			gameOver.setUtilImage(ImageIO.read(new File(utilPath+"\\GAMEOVER.png")));
			levelUpImage.setUtilImage(ImageIO.read(new File(utilPath+"\\LEVELUP.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//���࿡ Ÿ��Ʋ�� �ҷ����µ� �����ص� ����
			JOptionPane.showMessageDialog
			(gameWindow, "Error!! Can't find TITLE.png or LOGO.png or CURSOR.png or SomthingElse");
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


	public void setNowEventLost(Event nowEventLost) {
		this.nowEventList = nowEventLost;
	}


	public Event getNowEventLost() {
		return nowEventList;
	}


	public void setGameMusic(GameMusic gameMusic) {
		// TODO Auto-generated method stub
		this.gameMusic = gameMusic;
	}


	public void setSwitchDialog(GameUtilityInformation switchDialog) {
		this.switchDialog = switchDialog;
	}


	public GameUtilityInformation getSwitchDialog() {
		return switchDialog;
	}

	
	
}
