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

import animationEditor.Animations;

import MapEditor.DrawingTemplate;
import MapEditor.Tile;


public class GameDisplay implements Runnable{

	public static int TIMER = 30;
	//���̵� �ƿ� ���̵� ���� ����
	
	//�ʰ� �ɸ��� �̺�Ʈ �迭�� ����.
	//public static final int mapCharArrayRatio = 4;
	public static final int maxMapArrayX = 50;
	public static final int maxMapArrayY = 40;
	// ȭ�鿡 ����� ������Ʈ ������ ������ �ִ� Ŭ����
	private GameData gameData;
	// �ϵ���� �ڿ�
	private BufferStrategy hwResource = null;
	// ����� ��ü
	private Graphics2D gameGraphics = null;

	
	//����� ��ǻ�� ��ũ���� ũ��
	private int screenWidth;
	private int screenHeight;
	private final double baseWidth = 800;
	private final double baseHeight = 640;
	//Ȯ�� ����
	private double ratioX;
	private double ratioY;
	//�� ��� �κ�
	private int mapStartingPointX;
	private int mapStartingPointY;

	//Ű �÷���
	private KeyFlags keyFlag;
	
	private byte[][] gameTile;

	
	//������
	public GameDisplay(GameData gameData) {
		// TODO Auto-generated constructor stub
		this.gameData = gameData;
	}
	//�ϵ���� �ڿ� ����
	public void setHardWareBuffer(BufferStrategy bufferStrategy) {
		// TODO Auto-generated method stub
		this.hwResource = bufferStrategy;
		if(this.hwResource == null)
		{
			System.out.println("Error in GameDisplay.java! hwResource is null!!");
			System.exit(-1);
		}
	}

	//�ΰ� ���
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
	//�޴� ���
	public void displayTitleMenu(Graphics2D g)
	{
		GameUtilityInformation titleScreen = gameData.getTitleScreen();
		GameUtilityInformation cursorImage = gameData.getCursorImage();

		//�ϴ� ȭ�� ���
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
			//���� �簢 �� ���
			int rectSizeWidth = screenWidth/3;
			int rectSizeHeight = screenHeight/3;
			int rectPositionX = screenWidth/2 - screenWidth/6;
			int rectPositionY = screenHeight*3/5;
			Color rectColor = new Color(70,70,240,200);
			g.setColor(rectColor);
			g.fill3DRect(rectPositionX , rectPositionY,
					rectSizeWidth , rectSizeHeight, true);
			//������� �޴����
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

	//�ε� ��ũ�� ���
	public void displayLoadingScreen(Graphics2D g)
	{
		GameUtilityInformation loading = gameData.getLoadScreen();
		g.drawImage(loading.getUtilImage(),0,0,screenWidth,screenHeight,null);
	}
	
	/******************************************************************************/
	/***����!! �� �������� ���� �ӵ��� �ٸ��� ������ �ΰ��� ���� ���ܻ�Ȳó��!!        ******************/
	/***��) ���� �ҷ����µ� �ɸ��� �ð��� ����. gameState�� �ε��� �������� ó��  *************************/
	/******************************************************************************/
	public void displayBackground(Graphics2D g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		//���� ��׶��� ���
		BufferedImage gameMap = gameData.getGameMap().getM_Background();
		int sizeW = (int) ((double)gameMap.getWidth()*ratioX);
		int sizeH = (int) ((double)gameMap.getHeight()*ratioY);
		//���� ������ġ ����
		this.mapStartingPointX = (screenWidth - sizeW)/2;
		this.mapStartingPointY = (screenHeight - sizeH)/2;
		//�ΰ��� ��� 
		g.drawImage(gameMap, mapStartingPointX, mapStartingPointY, sizeW, sizeH, null);

	}

	//����, ��� �κп��� ��� �κ��� ����ؾ��ϴ°�?
	//���⼭ �Ű������� ������ Ÿ��
	public void displayForeground(Graphics2D g, boolean isUpper)
	{
		Tile[][] mapTile = gameData.getGameMap().getM_ForegroundTile();

		if(isUpper == false)
		{
			//ĳ�� �Ʒ��� �򸮴� �� ���� ���
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

	
	//���� ��� �÷��̾� ������ ���
	public void displayActorMoveMotion(Graphics2D g, Animations actorAnim, boolean isStop , GameCharacter actor)
	{
		//�켱 �÷��̾� ����
		//�÷��̾��� ��ġ Ȯ��
		int positionX = actor.getxPosition();
		int positionY = actor.getyPosition();

		int playerTimer = gameData.getPlayerAnimTimer();
		//���� ��ġ
		if(isStop == false)
		{
			BufferedImage actorImage = actorAnim.getNextImage();

			if(gameData.isChangeCharacterAnim(playerTimer))
			{
				actorImage = actorAnim.getNextImage();
			}
			
			// ��� ������ Ȯ��
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
			// ��� ������ Ȯ��
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
	
	
	public void displayActorAttackMotion(Graphics2D g, Animations actorAnim, GameCharacter actor)
	{
		int positionX = actor.getxPosition();
		int positionY = actor.getyPosition();

		//���� ��ġ
//		if(gameData.getPlayerActionTimer() == 0)
//			actorAnim.resetCallIndex();
		BufferedImage //actorImage = //actorAnim.getCurrentImage();
//		
//		if(gameData.getPlayerActionTimer() % (40/actorAnim.getCountImage()) == 0)//&& gameData.getPlayerAnimTimer() % 10 == 0 )
//		{
				actorImage = actorAnim.getNextImage();
//		}

		// ��� ������ Ȯ��
		int charX = actorAnim.getPointX(actorAnim.getCallIndex());
		int charY = actorAnim.getPointY(actorAnim.getCallIndex());

		g.drawImage(
				(Image) actorImage,mapStartingPointX
						+ (int) ((positionX *(DrawingTemplate.pixel/GameData.mapCharArrayRatio) - charX) * ratioX),
				mapStartingPointY + (int) ((positionY *(DrawingTemplate.pixel/GameData.mapCharArrayRatio) - charY) * ratioY),
				(int) (actorImage.getWidth() * ratioX),
				(int) (actorImage.getHeight() * ratioY), null);

	}
	
	
	public void displayNpcs(Graphics2D g)
	{
		//ĳ�� ���� ���
		Vector<GameCharacter> alliances = gameData.getAlliances();
		if(alliances == null)
			return;
		Alliance alliance = null;
		
		for(int i = 0 ; i < alliances.size(); i++)
		{
			//ĳ�� ���� �ޱ�
			alliance = (Alliance) alliances.elementAt(i);
			//�ִϸ��̼� ���� ���
			CharacterEditorSystem allianceAnim = (CharacterEditorSystem) alliance.getChracter();
			//��쿡 ���� �ִϸ��̼� ���, �������̳� ������
			if(alliance.getActorState() == GameCharacter.MOVESTATE)
			{
				//���� �ִϸ��̼� ���
				if(alliance.getActionType()==GameCharacter.STOP ||alliance.getActionType()==GameCharacter.STOPAFTERRANDOM )
				{
					if(alliance.getNowDirection() == GameCharacter.UP)
						displayActorMoveMotion(g, allianceAnim.getMoveUpAnimation(), true,alliance);
					else if(alliance.getNowDirection() == GameCharacter.DOWN)
						displayActorMoveMotion(g, allianceAnim.getMoveDownAnimation(), true,alliance);
					else if(alliance.getNowDirection() == GameCharacter.LEFT)
						displayActorMoveMotion(g, allianceAnim.getMoveLeftAnimation(), true,alliance);
					else if(alliance.getNowDirection() == GameCharacter.RIGHT)
						displayActorMoveMotion(g, allianceAnim.getMoveRightAnimation(), true,alliance);
				}
				//������ �ִϸ��̼� ���
				else
				{
					//�̵� �����϶�
					if(alliance.getNowDirection() == GameCharacter.UP)
						displayActorMoveMotion(g, allianceAnim.getMoveUpAnimation(), false,alliance);
					else if(alliance.getNowDirection() == GameCharacter.DOWN)
						displayActorMoveMotion(g, allianceAnim.getMoveDownAnimation(), false,alliance);
					else if(alliance.getNowDirection() == GameCharacter.LEFT)
						displayActorMoveMotion(g, allianceAnim.getMoveLeftAnimation(), false,alliance);
					else if(alliance.getNowDirection() == GameCharacter.RIGHT)
						displayActorMoveMotion(g, allianceAnim.getMoveRightAnimation(), false,alliance);
				}
			}
			else if(alliance.getActorState() == GameCharacter.BATTLESTATE)
			{
				
			}
		}
	}
	
	
	public void displayPlayer(Graphics2D g)
	{
		//ĳ������
		Alliance player = (Alliance) gameData.getPlayer();
		//ĳ���� �ִϸ��̼� ����
		CharacterEditorSystem playerAnim = (CharacterEditorSystem) gameData.getPlayer().getChracter();
		/********�÷��̾� ���****************************************************************/
		//�������� �ʿ�
		if(player.getActorState() == GameCharacter.MOVESTATE)
		{
			//�̵� �����϶�
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
			//�׼ǹ�ư�� ���Ǵٸ�
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
				//���� �ִϸ��̼� ���
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
			
		}//����ִϿ� ���� �ִ� ��������, ������ �ص�
	}

	
	//GameRunning!!!!!!!
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (gameData.getGameState() != GameData.EXIT) 
		{
			gameGraphics = (Graphics2D) hwResource.getDrawGraphics();
			int gameState = gameData.getGameState();
			// ���°� �ΰ���
			if (gameState == GameData.LOGOSCREEN) 
			{
				displayLogoImage(gameGraphics);
			}
			// ���°� �޴���
			else if (gameState == GameData.TITLEMENU)
			{
				TIMER = 60;
				displayTitleMenu(gameGraphics);
			}
			// �ε���
			else if (gameState == GameData.LOADING) 
			{
				displayLoadingScreen(gameGraphics);
			}
			// �ε�ȭ��
			else if (gameState == GameData.LOAD) {
				// ���� �̱���
			}
			// ���°� �÷����̸�
			else if (gameState == GameData.PLAY)
			{
				TIMER = 30;
				// ��׶��� ���
				this.displayBackground(gameGraphics);
				// �ɸ��� �ڷ� �� ���� ���
				displayForeground(gameGraphics, false);
			
				//ĳ���� ��¼����� ��������
				
				
				this.displayPlayer(gameGraphics);
				this.displayNpcs(gameGraphics);

				// �ɸ��� �տ� �� ���� ���
				displayForeground(gameGraphics, true);
			}

			//
			try {
				gameGraphics.dispose();
				hwResource.show();
				Thread.sleep(GameDisplay.TIMER);
			} catch (NullPointerException e) {
				// ���� �����Ͱ� ���� ����� ���
				System.exit(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// end while
		
	}
	
	
	//������ ����Ʈ
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
	//�������
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










