/********************************************/
//주의사항!
//1. 현재 캐릭터의 상태가 움직임상태, 전투 상태 등에 대한 구분이 없음
//   즉, 움직임상태에서 액션이 가능함
//   player.setActorState(MOVESTATE)이 구문 수정바람
//   player.getActorState()도
//2. 추가 파일 경로가 필요할 수도 있음 ex) Utilimage와 같은것들 수정 가능성 농후함
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
	//이펙트 타이머
	private int fadeTimer = 0;

	private String utilPath =null;
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
	public static final int ACTORLOAD = 11;
//	public static final int MAPLOADENDED = 12;
	public static final int ACTORLOADENDED = 13;
	public static final int PLAYERDEATH = 14;
	public static final int LOADING = 15;
	public static final int NEWSTART = 16;
	public static final int GAMEOVER = 17;
	public static final int STATUSCALLED = 18;
	/**************************************************/
	//이 쓰레드의 클럭 타이머
	public static int TIMER = 100;
	public static int FASTTIMER = 60;
	public static int SLOWTIMER = 100;
	
	/***유틸인포/ 로드화면, 타이틀화면/로고화면***********************/
	private GameUtilityInformation titleScreen;
	private GameUtilityInformation logoScreen;
	private GameUtilityInformation loadScreen;
	private GameUtilityInformation cursorImage;
	private GameUtilityInformation dialogScreen;
	private GameUtilityInformation gameOver;
	private GameUtilityInformation statusScreen;
	private GameUtilityInformation levelUpImage;
	private GameUtilityInformation switchDialog;

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
	private GameEventDispatcher eventDispatcher;
	private static final int MAXFLAG = 1001;
	private boolean [] conditionFlag;
	//새로운 리스트를 받아와야하나?
	private boolean eventStart = false;
	//지금 이벤트가 수행중인가?
	//private boolean eventStart = false;
	private Event nowEventList = null;
	private EventContent nowEvent = null;
	private int eventContentListIndex = -1;
	private boolean firstEvent = false;
	private boolean charEnterMap = false;
	private boolean charActionMap = false;
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
	

	//맵데이터
	private BufferedImage background;
	private BufferedImage foreBackImage;
	private BufferedImage foreForeImage;
	private GameMusic gameMusic;

	//음악 쓰레드
	private Thread musicThread;
	private boolean autoEventCalled = false;
	private boolean autoAllianceEventCalled = false;
	private boolean autoMonsterEventCalled = false;
	private List<Event> autoEvents;
	private List<EventTile> actorEventTiles;
	private int charOnMapX;
	private int charOnMapY;
	
	//생성자
	public GameData()
	{
		//폰트설정
		titleScreen = new GameUtilityInformation();
		//로고화면
		logoScreen = new GameUtilityInformation();
		loadScreen = new GameUtilityInformation();
		//커서이미지
		cursorImage = new GameUtilityInformation();
		cursorImage.setPosition(0);
		//게임종료
		gameOver = new GameUtilityInformation();
		//다이얼로그 설정(대화)
		dialogScreen = new GameUtilityInformation();
		dialogScreen.setText(null);
		//상태창
		statusScreen = new GameUtilityInformation();
		//레벨업
		levelUpImage = new GameUtilityInformation();
		//스위치 다이얼로그
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
				//실제로는 이벤트 디스패처를 호출해서 현재 출력해야할 맵, 맵에 속한 npc등의 정보를 읽어서 로드한다.
				try {
					Thread.sleep(SLOWTIMER);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			//스테이터스 창
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
	//새로 시작일경우
	private void runAtNewStart()
	{
		exGameState = GameData.NEWSTART;
		//실제로는 이벤트 디스패처를 호출해서 현재 출력해야할 맵, 맵에 속한 npc등의 정보를 읽어서 로드한다.
		gameState = GameData.LOADING;
		//부트스트랩 이벤트 실행
		BootstrapInfo bs = null;
		try
		{
			bs = Bootstrap.getBootstrap(gamePath);
		}
		catch(NullPointerException e)
		{
			JOptionPane.showMessageDialog(null, "플레이어 초기 위치 지정 오류");
			e.printStackTrace();
			System.exit(0);
		}
		//우선 맵 로드
		try{
		
			loadMap(bs.getMapName());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "맵을 찾을 수가 없습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		//플레이어 로드
		try {
			Point startPoint = bs.getStartPoint();
			int charIndex = bs.getCharIndex();
			loadPlayer(charIndex, startPoint);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "캐릭터를 찾을 수가 없습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		//loadAlliances();
		//loadMonsters();
//		loadAlliances();
		//startMusic("D:\\Download\\Music\\Gamma Ray - Discography\\2001 - No World Order!\\01 - Introduction.mp3");
		/****************************************************/
		this.computeGameTile();
		
		gameState = GameData.PLAY;
	}
	
	//타이틀 메뉴에서
	private void runAtTitle()
	{
		//커서의 위치에 따라
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
	
	//실행중 플레이일때
	private void runAtPlay()
	{		
		if(keyFlag.isCancel())
		{
			//스테이터스 창 보여주기
			gameState = GameData.STATUSCALLED;
			statusScreen.setCalled(true);
			try {
				Thread.sleep(SLOWTIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//캐릭터 뒤짐
		if(player.getNowStatus().getHP() <=0)
		{
			player.setActorState(GameCharacter.DEATH);
			this.fadeTimer = 0;
			gameState = GameData.GAMEOVER;
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
			
			//System.out.println("호출중");
			//한꺼번에 배열에 움직임 작성
			this.computeGameTile();

			if(nowEventList == null)
			{
				if(charEnterMap == true && (player.getxPosition()/mapCharArrayRatio != charOnMapX ||
						player.getyPosition()/mapCharArrayRatio != charOnMapY))
				{
					charEnterMap = false;
				}
				//자동이벤트 생성
				if(autoEventCalled == false)
					computeMapAutoEvent();
				//실행 중인 이벤트가 없는데
				//캐릭터 자동이벤트가 있다면
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
			//실행중인 이벤트가 없다면
			if(eventStart == false)
			{
				computeNowEvent();
				return;
			}
			runEvent();
		}
		else if(player.getActorState() == GameCharacter.EVENTSTATE)
		{
			this.animTimer++;
			
			if(nowEventList == null)
			{
				if(charEnterMap == true && (player.getxPosition()/mapCharArrayRatio != charOnMapX ||
						player.getyPosition()/mapCharArrayRatio != charOnMapY))
				{
					charEnterMap = false;
				}
				//자동이벤트 생성
				if(autoEventCalled == false)
					computeMapAutoEvent();
				//실행 중인 이벤트가 없는데
				//캐릭터 자동이벤트가 있다면
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
			//실행중인 이벤트가 없다면
			if(eventStart == false)
			{
				computeNowEvent();
				return;
			}
			runEvent();
		}
		//캐릭터 체력 채워줌
		if(player.getNowStatus().getHP() < player.getMaxStatus().getHP() && player.getNowStatus().getHP() > 0 )
			player.getNowStatus().setHP(player.getNowStatus().getHP() + player.level);
		if(player.getNowStatus().getHP() > player.getMaxStatus().getHP())
		{
			player.getNowStatus().setHP(player.getMaxStatus().getHP());
		}
	}
	
	//타일 위에 있는 맵이벤트 실행
	private void runMapEventOnTile()
	{
		charOnMapX = player.getxPosition()/mapCharArrayRatio;
		charOnMapY = player.getyPosition()/mapCharArrayRatio;
		
		//맵 이벤트가 있는 경우에만 이벤트 타일 받아옴
		if(eventDispatcher.hasMapEvent(charOnMapX, charOnMapY))
		{
			//영역범위단위로 실행되므로
			//this.charEnterMap = true;
			
			//이벤트 받아올 테니 초기화
			EventTile mapEvent = eventDispatcher.getEventTile(charOnMapX, charOnMapY);
			List<Event> eventList = mapEvent.getEventList();
			//이벤트들 중에 조건에 맞는거 찾기
			for(int i = 0 ; i < eventList.size(); i++)
			{
				Event nextEvent = eventList.get(i);
				int [] condition = nextEvent.getPreconditionFlagArray();
				//실행 조건이 맞으면
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
			}//for종료
		}//if종료
		
	}
	
	private void runMapEventAction()
	{
		int charX = player.getxPosition()/mapCharArrayRatio;
		int charY = player.getyPosition()/mapCharArrayRatio;
		
		//맵 이벤트가 있는 경우에만 이벤트 타일 받아옴
		if(eventDispatcher.hasMapEvent(charX, charY))
		{
			//이벤트 받아올 테니 초기화
			EventTile mapEvent = eventDispatcher.getEventTile(charX, charY);
			List<Event> eventList = mapEvent.getEventList();
			//이벤트들 중에 조건에 맞는거 찾기
			for(int i = 0 ; i < eventList.size(); i++)
			{
				Event nextEvent = eventList.get(i);
				int [] condition = nextEvent.getPreconditionFlagArray();
				//실행 조건이 맞으면
				if(nextEvent.getStartType() == EventEditorSystem.PRESS_BUTTON&&
						conditionFlag[condition[0]+1]==true&&
						conditionFlag[condition[1]+1]==true&&
						conditionFlag[condition[2]+1]==true)
				{
					nowEventList = nextEvent;
					eventContentListIndex = 0;
					return;
				}
			}//for종료
		}//if종료
	}
	
	//맵의 자동이벤트 찾기
	private void computeMapAutoEvent()
	{
		//List<Event> autoEvents = null;
		nowEventList = null;
		if(autoEvents != null && autoEvents.size() != 0)
		{
			//System.out.println("자동이벤트 계산 호출됨");
			eventContentListIndex = 0;
			//자동 이벤트 리스트중 실행 조건이 맞는거 받아옴
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
	
	//현제 선택된 이벤트 실행
	private void computeNowEvent()
	{
		if(nowEventList == null || eventContentListIndex >= nowEventList.getEventContentList().size())
		{
			//이벤트리스트 하나가 다 끝났다면 이벤트 종료
			nowEventList = null;
			eventContentListIndex = 0;
			this.eventStart = false;
			nowEvent = null;
			//autoEvents = null;
			autoEventCalled = false;
			eventStart = false;
			autoAllianceEventCalled = false;
			autoMonsterEventCalled = false;
			//charActionMap = false;
			//charEnterMap = false;
			player.setActorState(GameCharacter.MOVESTATE);
		}
		else
		{
			//이벤트 시작
			eventStart = true;
			//현재 선택된 리스트에서 하나 받아옴
			nowEvent = nowEventList.getEventContentList().get(eventContentListIndex);
		}
	}
	
	//현재 선택된 이벤트 실행
	private void runEvent()
	{
		if(nowEvent == null)
		{
			eventContentListIndex++;
			//player.setActorState(GameCharacter.MOVESTATE);
			return;
		}
		
		int type = nowEvent.getContentType();
		//eventContentListIndex++랑 eventStart폴스 해줘야함
		
		//지금 이벤트가 뭔지 확인
		if(type == EventContent.CHANGE_BGM_EVNET)
		{
			startMusicEvent();
		}
		else if(type == EventContent.DIALOG_EVNET)
		{
			//다이얼로그 출력 이벤트
			startDialogEvent();
		}
		else if(type == EventContent.CHANGE_MAP_EVNET)
		{
			//맵 변환 이벤트
			startChangeMapEvent();
		}
		else if(type == EventContent.CHANGE_FLAG_EVENT)
		{
			//플레그 변환 이벤트
			startChangeFlagEvent();
		}
		else if(type == EventContent.GAMEOVER_EVNET)
		{
			//게임 종료
			gameState = GameData.GAMEOVER;
		}
		else if(type == EventContent.SWITCH_DIALOG_EVNET)
		{
			//스위치 다이얼로그
			startSwitchDialog();
		}
		else
		{
			nowEventList = null;
			eventContentListIndex++;
		}
	}
	
	//스위치 다이얼로그
	private void startSwitchDialog()
	{
		//캐릭터 상태 이벤트로 설정
		player.setActorState(GameCharacter.EVENTSTATE);
		//대화창 시작
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
	
	//플래그 변환 이벤트
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
	
	//음악 이벤트
	private void startMusicEvent()
	{
		ChangeBGMEvent bgm = (ChangeBGMEvent) nowEvent;
		
		//Thread musicThread = new Thread(gameMusic);
//		gameMusic = new GameMusic(this);
//		gameMusic.setFilePathAndName(gamePath+"\\"+bgm.getFileName());
//		gameMusic.setFilePathAndName(bgm.get);
		//musicThread.start();
		eventContentListIndex++;
		this.eventStart = false;
	}

	//맵변환 이벤트
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
	
	//다이얼로그 이벤트
	private void startDialogEvent()
	{
		//캐릭터 상태 이벤트로 설정
		player.setActorState(GameCharacter.EVENTSTATE);
		//대화창 시작
		DialogEvent dialog = (DialogEvent)nowEvent;
//		System.out.println("다이얼1");
		dialogScreen.setText(dialog.getText());
		try {
			Thread.sleep(FASTTIMER*2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		System.out.println("다이얼2");
		//엔터키나 액션 키가 눌리면
		if(keyFlag.isAction() || keyFlag.isEnter())
		{
			//System.out.println("다이얼3");
			
			try {
				eventContentListIndex++;
				Thread.sleep(FASTTIMER);
				dialogScreen.setText(null);
				this.eventStart = false;
				player.setActorState(GameCharacter.MOVESTATE);
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
	
	//실행중 스테이터스 화면일때
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
				if(alliance.getCharacter() == null)
					continue;
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
				if(monster != null)//&& chk == true)
				{
					sortedCharacters.add(monster);
				}
			}
		}
	}

	//타일 계산, 캐릭터를 자동정렬한다.
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
			
			if(actor.getCharacter() == null)
				continue;
			
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
		else if(keyFlag.isAction() && this.actionAnimFlag == false )//&& player.getActorState() == GameCharacter.BATTLESTATE)
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
				System.out.println("전투 애니메이션 없음");
				System.exit(-1);
			}

			if(player.getAnimActionClock() > error/2)
			{
				actionAnimFlag = false;
				player.setAnimActionClock(0);
			}
		}	
	}

	//맵에 npc들 생성
	private void createMapNPC()
	{
		if(alliances == null)
			alliances = new Vector<GameCharacter>();
		int count = 0;
		try{
			//현재 맵에 정의된 npc들 출력
			if(actorEventTiles == null)
			{
				return;
			}
			//자동이벤트가 플레그에 맞게 존재하면 배치하고 자동이벤트 쪽에 집어 넣는다.
			for(int i = 0 ; i < actorEventTiles.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = actorEventTiles.get(i);
				//몬스터이면 리턴
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
			//현재 맵에 정의된 npc들 출력
			if(actorEventTiles == null)
			{
				return;
			}
			//자동이벤트가 플레그에 맞게 존재하면 배치하고 자동이벤트 쪽에 집어 넣는다.
			for(int i = 0 ; i < actorEventTiles.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = actorEventTiles.get(i);
				//몬스터이면 리턴
				if(actorEventTile.getObjectType() != EventEditorSystem.MONSTER_EVENT)
					continue;
				
				monsters.add(new Monster(gamePath));
				monsters.elementAt(count).setTotalEvent(actorEventTile);
				monsters.elementAt(count).setActionType(GameCharacter.TOPLAYER);
				count++;
			}
			
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			alliances = null;
		}
	}
	
	//배치
	private void deployNPC()
	{
		if(alliances == null)
			return;
		try{
			//npc들중에서 자동이벤트를 제외한 조건에 맞는 애들 배치
			for(int i = 0 ; i < alliances.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = alliances.elementAt(i).getTotalEvent();
				//타일에 있는 여러 이벤트 리스트 중에서
				List<Event> actorEventList = actorEventTile.getEventList();
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

						//캐릭터 배치
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
			//npc들중에서 자동이벤트를 제외한 조건에 맞는 애들 배치
			for(int i = 0 ; i < monsters.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = monsters.elementAt(i).getTotalEvent();
				//타일에 있는 여러 이벤트 리스트 중에서
				List<Event> actorEventList = actorEventTile.getEventList();
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
						break;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
		//	monsters = null;
		}
	}
	
	//npc들 중에 자동이벤트 생성
	private void computeAutoAlliances() {
		// TODO Auto-generated method stub
		try{
			//현재 맵에 정의된 npc들 출력
			//nowEventList = null;
			eventContentListIndex = 0;
			//자동이벤트가 플레그에 맞게 존재하면 배치하고 자동이벤트 쪽에 집어 넣는다.
			for(int i = 0 ; i < alliances.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = alliances.elementAt(i).getTotalEvent();
				//몬스터이면 리턴
				if(actorEventTile.getObjectType() != EventEditorSystem.NPC_EVENT)
					continue;
				//타일에 있는 여러 이벤트 리스트 중에서
				List<Event> actorEventList = actorEventTile.getEventList();
				//자동실행 이벤트를 확인
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();
					
					//모든 조건이 만족되었으며 자동 실행 이벤트이면 하나 띄옴
					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() == EventEditorSystem.AUTO_START)
					{
						//System.out.println("여기 불림");
						alliances.elementAt(i).changeActor(eventList.getActorIndex(),
								alliances.elementAt(i).getxPosition(), alliances.elementAt(i).getyPosition());
						//선택한 이벤트 지움
						nowEventList = eventList;
						actorEventList.remove(j);
						autoAllianceEventCalled = true;
						//캐릭터 배치
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
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			alliances = null;
		}
	}
	
	//몬스터 중에 자동이벤트 생성
	private void computeAutoMonsters() {
		try{
			//현재 맵에 정의된 npc들 출력
			//nowEventList = null;
			eventContentListIndex = 0;
			//자동이벤트가 플레그에 맞게 존재하면 배치하고 자동이벤트 쪽에 집어 넣는다.
			for(int i = 0 ; i < monsters.size(); i++)
			{
				//타일 하나를 가져옴
				EventTile actorEventTile = monsters.elementAt(i).getTotalEvent();
				//몬스터이면 리턴
				if(actorEventTile.getObjectType() != EventEditorSystem.MONSTER_EVENT)
					continue;
				//타일에 있는 여러 이벤트 리스트 중에서
				List<Event> actorEventList = actorEventTile.getEventList();
				//자동실행 이벤트를 확인
				for(int j = 0 ; j < actorEventList.size(); j++)
				{
					Event eventList = actorEventList.get(j);
					int[] cond = eventList.getPreconditionFlagArray();
					
					//모든 조건이 만족되었으며 자동 실행 이벤트이면 하나 띄옴
					if(conditionFlag[cond[0]+1]==true &&
					conditionFlag[cond[1]+1]==true &&
					conditionFlag[cond[2]+1]==true && eventList.getStartType() == EventEditorSystem.AUTO_START)
					{
						//System.out.println("여기 불림");
						monsters.elementAt(i).changeActor(eventList.getActorIndex(),
								monsters.elementAt(i).getxPosition(), alliances.elementAt(i).getyPosition());
						//선택한 이벤트 지움
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
			JOptionPane.showMessageDialog(gameWindow, "Error in GameData loadCharacterNPC()");
			e.printStackTrace();
//			System.exit(0);
			monsters = null;
		}
	}
	
	//음악시작
	private void startMusic(String string) {
		// TODO Auto-generated method stub
		this.musicFile = string;
	}

	//맵로드
	public void loadMap(String mapName)
	{

		GameMapLoader mapLoader = new GameMapLoader();
		mapLoader.setMapFile(this.gamePath+"/Map/"+mapName);
		this.setGameMap(mapLoader.getMap());
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
			e.printStackTrace();
		//	System.exit(0);
		}
		//맵 이미지 설정
		//백그라운드 설정
		background = gameMap.getM_Background();
		Graphics2D g = background.createGraphics();
		foreBackImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		displayForeground(g, false);
		g.dispose();
		foreForeImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		g = foreForeImage.createGraphics();
		displayForeground(g, true);
		g.dispose();
		
		//이벤트 타일 생성
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
		//npc들과 몬스터 로드해야함
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

	//플레이어로드
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
			//System.exit(0);
		}
	}
	
	//게임 타일 초기화
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

	//게임 상태 설정
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
			//만약에 타이틀을 불러오는데 실패해도 진행
			JOptionPane.showMessageDialog
			(gameWindow, "Error!! Can't find TITLE.png or LOGO.png or CURSOR.png or SomthingElse");
			e.printStackTrace();
			System.exit(0);
		}

	}
	
	
	public void setGameWindow(GameWindow gameWindow) 
	{
		this.gameWindow = gameWindow;
		statusScreen.setFont(new Font("굴림", Font.BOLD , gameWindow.getWidth()/35 ));
		titleScreen.setFont(new Font("Courier New", Font.BOLD , gameWindow.getWidth()/25));
		dialogScreen.setFont(new Font("굴림", Font.BOLD , gameWindow.getWidth()/50));
		switchDialog.setFont(new Font("굴림", Font.BOLD , gameWindow.getWidth()/50));
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
//			//캐릭 아래에 깔리는 것 부터 출력
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
		// TODO Auto-generated method stub
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

	
	
}
