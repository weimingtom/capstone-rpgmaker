package execute;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Vector;

import characterEditor.CharacterEditorSystem;
import characterEditor.MonsterEditorSystem;

import animationEditor.Animations;

import MapEditor.DrawingTemplate;
import MapEditor.Tile;


public class GameDisplay implements Runnable{

	public static int TIMER = 30;
	//페이드 아웃 페이드 인을 위해
	
	//맵과 케릭터 이벤트 배열의 비율.
	//public static final int mapCharArrayRatio = 4;
	public static final int maxMapArrayX = 50;
	public static final int maxMapArrayY = 40;
	// 화면에 출력할 오브젝트 정보를 가지고 있는 클래스
	private GameData gameData;
	// 하드웨어 자원
	private BufferStrategy hwResource = null;
	// 출력할 객체
	private Graphics2D gameGraphics = null;

	
	//사용자 컴퓨터 스크린의 크기
	private int screenWidth;
	private int screenHeight;
	private final double baseWidth = 800;
	private final double baseHeight = 640;
	//확대 비율
	private double ratioX;
	private double ratioY;
	//맵 출력 부분
	private int mapStartingPointX;
	private int mapStartingPointY;

	//키 플래그
	private KeyFlags keyFlag;
	
	private byte[][] gameTile;

	
	//생성자
	public GameDisplay(GameData gameData) {
		// TODO Auto-generated constructor stub
		this.gameData = gameData;
	}
	//하드웨어 자원 설정
	public void setHardWareBuffer(BufferStrategy bufferStrategy) {
		// TODO Auto-generated method stub
		this.hwResource = bufferStrategy;
		if(this.hwResource == null)
		{
			System.out.println("Error in GameDisplay.java! hwResource is null!!");
			System.exit(-1);
		}
	}

	//로고 출력
	public void displayLogoImage(Graphics2D g)
	{	
		if(gameData.getFadeTimer() < 255)
		{
			if(gameData.getFadeTimer() < 0)
				return;
			g.drawImage(gameData.getLogoScreen().getUtilImage(), 0, 0,
					screenWidth,screenHeight,null);
			g.setColor(new Color(gameData.getFadeTimer(),
					gameData.getFadeTimer(),gameData.getFadeTimer(),255-gameData.getFadeTimer()));
			g.fillRect(0, 0, screenWidth, screenHeight);
			gameData.setFadeTimer(gameData.getFadeTimer()+10);
		}

	}
	//메뉴 출력
	public void displayTitleMenu(Graphics2D g)
	{
		GameUtilityInformation titleScreen = gameData.getTitleScreen();
		GameUtilityInformation cursorImage = gameData.getCursorImage();

		//일단 화면 출력
		if(gameData.getFadeTimer() < 255)
		{
			g.drawImage(titleScreen.getUtilImage(), 0, 0,
					screenWidth, screenHeight, null);
			
			g.setColor(new Color(gameData.getFadeTimer(),
					gameData.getFadeTimer(),gameData.getFadeTimer(),255-gameData.getFadeTimer()));
			g.fillRect(0, 0, screenWidth, screenHeight);
			gameData.setFadeTimer(gameData.getFadeTimer()+30);
		}
		else
		{
			g.drawImage(titleScreen.getUtilImage(), 0, 0,
					screenWidth, screenHeight, null);
			//this.bluringImage((BufferedImage) titleScreen.getUtilImage())
			//이제 사각 형 출력
			int rectSizeWidth = screenWidth/3;
			int rectSizeHeight = screenHeight/3;
			int rectPositionX = screenWidth/2 - screenWidth/6;
			int rectPositionY = screenHeight*3/5;
			Color rectColor = new Color(70,70,240,200);
			g.setColor(rectColor);
			g.fill3DRect(rectPositionX , rectPositionY,
					rectSizeWidth , rectSizeHeight, true);
			//여기까지 메뉴출력
			if(cursorImage.getPosition() == 0)
			{
				if(keyFlag.isUp() == true)
				{
					cursorImage.setPosition(2);
				}
				else if(keyFlag.isDown()==true)
				{
					cursorImage.setPosition(1);
				}
				g.drawImage(cursorImage.getUtilImage(), 
						rectPositionX + cursorImage.getUtilImage().getWidth(null), 
						rectPositionY + titleScreen.getFontSize() - cursorImage.getUtilImage().getHeight(null) , null);
				g.setFont(titleScreen.getFont());
				g.setColor(Color.CYAN);

				g.drawString("New Game", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9
						, rectPositionY + titleScreen.getFontSize());
				
				g.setColor(Color.BLACK);
				
				g.drawString("Continue", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*2 );
				g.drawString("Exit", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*3 );
				
			}
			else if(cursorImage.getPosition() == 1)
			{
				if(keyFlag.isUp() == true)
				{
					cursorImage.setPosition(0);
				}
				else if(keyFlag.isDown()==true)
				{
					cursorImage.setPosition(2);
				}	
				g.drawImage(cursorImage.getUtilImage(), 
						rectPositionX + cursorImage.getUtilImage().getWidth(null), 
						rectPositionY + titleScreen.getFontSize()*2 - cursorImage.getUtilImage().getHeight(null) , null);
				g.setFont(titleScreen.getFont());
				g.setColor(Color.BLACK);

				g.drawString("New Game", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9
						, rectPositionY + titleScreen.getFontSize());
				
				g.setColor(Color.BLACK);
				g.setColor(Color.CYAN);
				g.drawString("Continue", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*2 );
				g.setColor(Color.BLACK);
				g.drawString("Exit", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*3 );
			}
			else
			{
				if(keyFlag.isUp() == true)
				{
					cursorImage.setPosition(1);
				}
				else if(keyFlag.isDown()==true)
				{
					cursorImage.setPosition(0);
				}	
				g.drawImage(cursorImage.getUtilImage(), 
						rectPositionX + cursorImage.getUtilImage().getWidth(null), 
						rectPositionY + titleScreen.getFontSize()*3 - cursorImage.getUtilImage().getHeight(null) , null);
				g.setFont(titleScreen.getFont());
				g.setColor(Color.BLACK);

				g.drawString("New Game", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9
						, rectPositionY + titleScreen.getFontSize());

				g.setColor(Color.BLACK);
				g.drawString("Continue", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*2 );
				g.setColor(Color.CYAN);
				g.drawString("Exit", screenWidth/2 - rectSizeWidth/2 + rectSizeWidth*2/9, 
						rectPositionY+titleScreen.getFontSize()*3 );
			}
		}
	}

	//로딩 스크린 출력
	public void displayLoadingScreen(Graphics2D g)
	{
		GameUtilityInformation loading = gameData.getLoadScreen();
		g.drawImage(loading.getUtilImage(),0,0,screenWidth,screenHeight,null);
	}
	
	/******************************************************************************/
	/***주의!! 두 쓰레드의 접속 속도가 다르기 때문에 널값에 대한 예외상황처리!!        ******************/
	/***예) 맵을 불러오는데 걸리는 시간이 있음. gameState에 로딩을 넣음으로 처리  *************************/
	/******************************************************************************/
	public void displayBackground(Graphics2D g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		//맵의 백그라운드 출력
		BufferedImage gameMap = gameData.getGameMap().getM_Background();
		int sizeW = (int) ((double)gameMap.getWidth()*ratioX);
		int sizeH = (int) ((double)gameMap.getHeight()*ratioY);
		//맵의 시작위치 설정
		this.mapStartingPointX = (screenWidth - sizeW)/2;
		this.mapStartingPointY = (screenHeight - sizeH)/2;
		//두가지 경우 
		g.drawImage(gameMap, mapStartingPointX, mapStartingPointY, sizeW, sizeH, null);

	}

	//전경, 어느 부분에서 어느 부분을 출력해야하는가?
	//여기서 매개변수는 게임의 타일
	public void displayForeground(Graphics2D g, boolean isUpper)
	{
		Tile[][] mapTile = gameData.getGameMap().getM_ForegroundTile();

		if(isUpper == false)
		{
			//캐릭 아래에 깔리는 것 부터 출력
			for(int i = 0 ; i < mapTile.length; i++)
			{
				for(int j = 0 ; j < mapTile[0].length; j++)
				{
					if(mapTile[i][j].getIsUpper() == false)
					{
						g.drawImage(mapTile[i][j].getM_TileIcon(), mapStartingPointX+(int)((j*DrawingTemplate.pixel)*ratioX),
								mapStartingPointY+(int)((i*DrawingTemplate.pixel)*ratioY),
								(int)(mapTile[i][j].getM_TileIcon().getWidth() * ratioX)+1,
								(int)(mapTile[i][j].getM_TileIcon().getHeight()* ratioY)+1,
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
						g.drawImage(mapTile[i][j].getM_TileIcon(), mapStartingPointX+(int)((j*DrawingTemplate.pixel)*ratioX),
								mapStartingPointY+(int)((i*DrawingTemplate.pixel)*ratioY),
								(int)(mapTile[i][j].getM_TileIcon().getWidth() * ratioX)+1,
								(int)(mapTile[i][j].getM_TileIcon().getHeight()* ratioY)+1,
								null);
					}
				}
			}
		}
	}

	
	//액터 출력 플레이어 무브모션 출력
	public void displayActorMoveMotion(Graphics2D g, Animations actorAnim, boolean isStop , GameCharacter actor)
	{
		//우선 플레이어 부터
		//플레이어위 위치 확인
		int positionX = actor.getxPosition();
		int positionY = actor.getyPosition();

		int playerTimer = gameData.getAnimTimer();
		//맵의 위치
		if(isStop == false)
		{
			BufferedImage actorImage = null;
			try{
				actorImage = actorAnim.getCurrentImage();
			}catch(ArrayIndexOutOfBoundsException e)
			{
				actorImage = actorAnim.getNextImage();
			}

			if(gameData.isChangeCharacterAnim(playerTimer, actorAnim.getCountImg()))
			{
				actorImage = actorAnim.getNextImage();
			}
			
			// 출력 기준점 확인
			int charX = actorAnim.getPointX(actorAnim.getCallIndex());
			int charY = actorAnim.getPointY(actorAnim.getCallIndex());

			g.drawImage((Image) actorImage, mapStartingPointX + (int)((positionX
					* (DrawingTemplate.pixel/GameData.mapCharArrayRatio)- charX)*ratioX), mapStartingPointY
					+ (int)((positionY *(DrawingTemplate.pixel/GameData.mapCharArrayRatio)- charY)*ratioY),
					(int) (actorImage.getWidth() * ratioX),
					(int)(actorImage.getHeight()*ratioY), null);
		}
		else
		{
			BufferedImage actorImage = actorAnim.getBaseImage();
			// 출력 기준점 확인
			//getPointxyBaseImg()
			int charX = actorAnim.getPointXBaseImg();
			int charY = actorAnim.getPointYBaseImg();

			g.drawImage((Image) actorImage, mapStartingPointX + (int)((positionX
					* (DrawingTemplate.pixel/GameData.mapCharArrayRatio)- charX)*ratioX), mapStartingPointY
					+ (int)((positionY *(DrawingTemplate.pixel/GameData.mapCharArrayRatio)- charY)*ratioY),
					(int) (actorImage.getWidth() * ratioX),
					(int)(actorImage.getHeight()*ratioY), null);
		}
	}
	
	//액터의 어택모션
	public void displayActorAttackMotion(Graphics2D g, Animations actorAnim, GameCharacter actor)
	{
		int positionX = actor.getxPosition();
		int positionY = actor.getyPosition();

		BufferedImage actorImage = null;

//		try{
//			actorImage = actorAnim.getCurrentImage();
//		}
//		catch(Exception e)
//		{
//			actorImage = actorAnim.getNextImage();
//		}
		
//		if(actor.getAnimActionClock()== 0)
//		{
			actorImage = actorAnim.getNextImage();
//		}


		// 출력 기준점 확인
		int charX = actorAnim.getPointX(actorAnim.getCallIndex());
		int charY = actorAnim.getPointY(actorAnim.getCallIndex());

		g.drawImage(
				(Image) actorImage,mapStartingPointX
						+ (int) ((positionX *(DrawingTemplate.pixel/GameData.mapCharArrayRatio) - charX) * ratioX),
				mapStartingPointY + (int) ((positionY *(DrawingTemplate.pixel/GameData.mapCharArrayRatio) - charY) * ratioY),
				(int) (actorImage.getWidth() * ratioX),
				(int) (actorImage.getHeight() * ratioY), null);

	}
	
	
	public void displayAlliance(Graphics2D g,GameCharacter alliance)
	{
		// 애니메이션 정보 얻기
		CharacterEditorSystem allianceAnim = (CharacterEditorSystem) alliance
				.getCharacter();
		// 경우에 따른 애니메이션 출력, 움직임이냐 전투냐
		if (alliance.getActorState() == GameCharacter.MOVESTATE) {
			// 정지 애니메이션 출력
			if (alliance.getActionType() == GameCharacter.STOP
					|| alliance.getActionType() == GameCharacter.STOPAFTERRANDOM) {
				if (alliance.getNowDirection() == GameCharacter.UP)
					displayActorMoveMotion(g,
							allianceAnim.getMoveUpAnimation(), true, alliance);
				else if (alliance.getNowDirection() == GameCharacter.DOWN)
					displayActorMoveMotion(g,
							allianceAnim.getMoveDownAnimation(), true, alliance);
				else if (alliance.getNowDirection() == GameCharacter.LEFT)
					displayActorMoveMotion(g,
							allianceAnim.getMoveLeftAnimation(), true, alliance);
				else if (alliance.getNowDirection() == GameCharacter.RIGHT)
					displayActorMoveMotion(g,
							allianceAnim.getMoveRightAnimation(), true,
							alliance);
			}
			// 움직임 애니메이션 출력
			else {
				// 이동 상태일때
				if (alliance.getNowDirection() == GameCharacter.UP)
					displayActorMoveMotion(g,
							allianceAnim.getMoveUpAnimation(), false, alliance);
				else if (alliance.getNowDirection() == GameCharacter.DOWN)
					displayActorMoveMotion(g,
							allianceAnim.getMoveDownAnimation(), false,
							alliance);
				else if (alliance.getNowDirection() == GameCharacter.LEFT)
					displayActorMoveMotion(g,
							allianceAnim.getMoveLeftAnimation(), false,
							alliance);
				else if (alliance.getNowDirection() == GameCharacter.RIGHT)
					displayActorMoveMotion(g,
							allianceAnim.getMoveRightAnimation(), false,
							alliance);
			}
		} else if (alliance.getActorState() == GameCharacter.BATTLESTATE) {

		}
		
	}
	
	
	public void displayPlayer(Graphics2D g)
	{
		//캐릭정보
		Alliance player = (Alliance) gameData.getPlayer();
		//캐릭터 애니메이션 정보
		CharacterEditorSystem playerAnim = (CharacterEditorSystem) gameData.getPlayer().getCharacter();
		/********플레이어 출력****************************************************************/
		//세팅정보 필요
		if(player.getActorState() == GameCharacter.MOVESTATE)
		{
			//이동 상태일때
			if(keyFlag.isUp() == true)
			{	
				displayActorMoveMotion(g, playerAnim.getMoveUpAnimation(), false, player);			
			}
			else if(keyFlag.isDown() == true)
			{
				displayActorMoveMotion(g, playerAnim.getMoveDownAnimation(), false,player);
			}
			else if(keyFlag.isRight() == true)
			{	
				displayActorMoveMotion(g, playerAnim.getMoveRightAnimation(), false,player);
			}
			else if(keyFlag.isLeft() == true)
			{	
				displayActorMoveMotion(g, playerAnim.getMoveLeftAnimation(), false,player);
			}
			//액션버튼이 눌렷다면
			else if ( gameData.isActionAnimFlag()) 
			{
				if (player.getNowDirection() == GameCharacter.UP) {
					displayActorAttackMotion(g, playerAnim.getAttackUpAnimation(), player);
				} else if (player.getNowDirection() == GameCharacter.DOWN) {
					displayActorAttackMotion(g, playerAnim.getAttackDownAnimation(),player);
				} else if (player.getNowDirection() == GameCharacter.LEFT) {
					displayActorAttackMotion(g, playerAnim.getAttackLeftAnimation(),player);
				} else if (player.getNowDirection() == GameCharacter.RIGHT) {
					displayActorAttackMotion(g, playerAnim.getAttackRightAnimation(),player);
				}
			}
			else if(keyFlag.isUp() == false && keyFlag.isDown() == false &&
					keyFlag.isRight() == false && keyFlag.isLeft() == false)
			{
				//정지 애니메이션 출력
				if(player.getNowDirection() == GameCharacter.UP)
				{
					displayActorMoveMotion(g, playerAnim.getMoveUpAnimation(), true,player);
				}
				else if(player.getNowDirection() == GameCharacter.DOWN)
				{
					displayActorMoveMotion(g, playerAnim.getMoveDownAnimation(), true,player);
				}
				else if(player.getNowDirection() == GameCharacter.LEFT)
				{
					displayActorMoveMotion(g, playerAnim.getMoveLeftAnimation(), true,player);
				}
				else if(player.getNowDirection() == GameCharacter.RIGHT)
				{
					displayActorMoveMotion(g, playerAnim.getMoveRightAnimation(), true,player);
				}
			}
			
		}//무브애니와 어택 애니 나눠야함, 무브쪽 앤드
	}

	/*****************************************************************/
	/*****************************************************************/
	public void displayMonster(Graphics2D g, GameCharacter monster)
	{
		
		MonsterEditorSystem monsterAnim = (MonsterEditorSystem) monster
		.getCharacter();

		//몬스터가 움직임 상태이면
		if(monster.getActorState() == GameCharacter.MOVESTATE)
		{
			//정지하면
			if(monster.getActionType() == GameCharacter.STOP)
			{
				//정지 애니메이션 출력
				if (monster.getNowDirection() == GameCharacter.UP)
					displayActorMoveMotion(g,
							monsterAnim.getMoveUpAnimation(), true, monster);
				else if (monster.getNowDirection() == GameCharacter.DOWN)
					displayActorMoveMotion(g,
							monsterAnim.getMoveDownAnimation(), true, monster);
				else if (monster.getNowDirection() == GameCharacter.LEFT)
					displayActorMoveMotion(g,
							monsterAnim.getMoveLeftAnimation(), true, monster);
				else if (monster.getNowDirection() == GameCharacter.RIGHT)
					displayActorMoveMotion(g,
							monsterAnim.getMoveRightAnimation(), true,
							monster);
			}
			else
			{
				//움직임 애니메이션 출력
				if (monster.getNowDirection() == GameCharacter.UP)
					displayActorMoveMotion(g,
							monsterAnim.getMoveUpAnimation(), false, monster);
				else if (monster.getNowDirection() == GameCharacter.DOWN)
					displayActorMoveMotion(g,
							monsterAnim.getMoveDownAnimation(), false, monster);
				else if (monster.getNowDirection() == GameCharacter.LEFT)
					displayActorMoveMotion(g,
							monsterAnim.getMoveLeftAnimation(), false, monster);
				else if (monster.getNowDirection() == GameCharacter.RIGHT)
					displayActorMoveMotion(g,
							monsterAnim.getMoveRightAnimation(), false,
							monster);
			}
		}
		else if(monster.getActorState() == GameCharacter.BATTLESTATE)
		{

			if(monster.getActionType() == GameCharacter.ATTACK)
			{
				if (monster.getNowDirection() == GameCharacter.UP) {
					displayActorAttackMotion(g, monsterAnim.getAttackUpAnimation(), monster);
				} else if (monster.getNowDirection() == GameCharacter.DOWN) {
					displayActorAttackMotion(g, monsterAnim.getAttackDownAnimation(),monster);
				} else if (monster.getNowDirection() == GameCharacter.LEFT) {
					displayActorAttackMotion(g, monsterAnim.getAttackLeftAnimation(),monster);
				} else if (monster.getNowDirection() == GameCharacter.RIGHT) {
					displayActorAttackMotion(g, monsterAnim.getAttackRightAnimation(),monster);
				}
			}
//			else if(monster.getActionType() == GameCharacter.DAMAGED)
//			{
//				
//			}
		}
//		else if(monster.getActorState() == GameCharacter.DEATH)
//		{
//			
//		}
	}

	
	
	
	
	//액터들의 출력
	public void displayActors(Graphics2D g)
	{
		//우선 정렬이 필요함
		Vector<GameCharacter> actors = gameData.getSortedCharacters();
		
		for(int i = 0 ; i < actors.size(); i++)
		{
			//주인공 받아옴
			if(actors.elementAt(i).equals(gameData.getPlayer()))
			{
				displayPlayer(g);
			}
			else if( actors.elementAt(i) instanceof Alliance )
			{
				displayAlliance(g, (GameCharacter)actors.elementAt(i));
			}
			else if( actors.elementAt(i) instanceof Monster)
			{
				//몬스터 출력
				/*****************************************************************/
				/*****************************************************************/
				/*****************************************************************/
				displayMonster(g, (GameCharacter)actors.elementAt(i));
				/*****************************************************************/
				/*****************************************************************/
				/*****************************************************************/
			}
		}
	}
	

	
	
	
	
	//GameRunning!!!!!!!
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (gameData.getGameState() != GameData.EXIT) 
		{
			gameGraphics = (Graphics2D) hwResource.getDrawGraphics();
			int gameState = gameData.getGameState();
			// 상태가 로고면
			if (gameState == GameData.LOGOSCREEN) 
			{
				TIMER = 60;
				displayLogoImage(gameGraphics);
			}
			// 상태가 메뉴면
			else if (gameState == GameData.TITLEMENU)
			{
				displayTitleMenu(gameGraphics);
			}
			// 로딩중
			else if (gameState == GameData.LOADING) 
			{
				displayLoadingScreen(gameGraphics);
				TIMER = 30;
			}
			// 로드화면
			else if (gameState == GameData.LOAD) {
				// 아직 미구현
			}
			// 상태가 플레이이면
			else if (gameState == GameData.PLAY)
			{
				// 백그라운드 출력
				this.displayBackground(gameGraphics);
				// 케릭터 뒤로 깔릴 전경 출력
				displayForeground(gameGraphics, false);
			
				//캐릭터 출력순서를 잡아줘야함
			
				displayActors(gameGraphics);

				
				// 케릭터 앞에 깔릴 전경 출력
				displayForeground(gameGraphics, true);
			}

			//
			try {
				gameGraphics.dispose();
				hwResource.show();
				Thread.sleep(GameDisplay.TIMER);
			} catch (NullPointerException e) {
				// 게임 데이터가 먼저 종료된 경우
				System.exit(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// end while
		
	}
	
	
	//블러링 이펙트
	public BufferedImage bluringImage(BufferedImage source)
	{
		BufferedImage bi = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
				0.0625f, 0.125f, 0.0625f };
		Kernel kernel = new Kernel(3, 3, data);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
				null);
		convolve.filter(source, bi);

		return bi;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
	//get set

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	public void setKeyFlag(KeyFlags keyFlag) {
		this.keyFlag = keyFlag;
	}
	public KeyFlags getKeyFlag() {
		return keyFlag;
	}
	public void exitGame()
	{
		hwResource.dispose();
	}
	//비율계산
	public void computeRatio() {
		// TODO Auto-generated method stub
		this.setRatioX((double)screenWidth/this.baseWidth);
		this.setRatioY((double)screenHeight/this.baseHeight);
	}
	public void setRatioX(double ratioX) {
		this.ratioX = ratioX;
	}
	public double getRatioX() {
		return ratioX;
	}
	public void setRatioY(double ratioY) {
		this.ratioY = ratioY;
	}
	public double getRatioY() {
		return ratioY;
	}
	public void setGameTile(byte[][] gameTile) {
		this.gameTile = gameTile;
	}
	public byte[][] getGameTile() {
		return gameTile;
	}

	
}// end class











