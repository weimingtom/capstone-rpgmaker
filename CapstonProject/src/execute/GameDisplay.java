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

	private static int TIMER = 30;
	private static int animTimer = 0;
	private static int SLOWTIMER = 60;
	private static int FASTTIMER = 30;
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
	
	//���ӿ��� ���
	public void displayGameOver(Graphics2D g)
	{
		GameUtilityInformation gameOver = gameData.getGameOver();
		
		if(gameData.getFadeTimer() < 255)
		{
			g.drawImage(gameOver.getUtilImage(), 0, 0,
					screenWidth, screenHeight, null);
			
			g.setColor(new Color(gameData.getFadeTimer(),
					gameData.getFadeTimer(),gameData.getFadeTimer(),255-gameData.getFadeTimer()));
			g.fillRect(0, 0, screenWidth, screenHeight);
			gameData.setFadeTimer(gameData.getFadeTimer()+20);
		}
		else
		{
			g.drawImage(gameOver.getUtilImage(), 0, 0,
					screenWidth, screenHeight, null);
		}
	}
	
	//�������ͽ� â ���
	public void displayStatusScreen(Graphics2D g)
	{
		//��� ����
		GameUtilityInformation status = gameData.getStatusScreen();
		GameUtilityInformation cursor = gameData.getCursorImage();
		GameUtilityInformation title = gameData.getTitleScreen();
		

		g.drawImage(title.getUtilImage(), 0,0,screenWidth,screenHeight,null);
		g.setColor(new Color(70,70,240,200));
		g.fill3DRect(screenWidth/10, screenHeight/10, screenWidth/6, screenHeight/2, true);
		g.fill3DRect((int)(screenWidth*(4.0/10.0)), screenHeight/10,
				(int)(screenWidth*(5.0/10.0)), (int)(screenHeight*(8.0/10.0)), true);
		g.setColor(Color.black);
		g.setFont(status.getFont());
		
		GameCharacter player = gameData.getPlayer();
		
		Image cursorImage = cursor.getUtilImage();
//		g.drawString("������", screenWidth/10 + 2*cursorImage.getWidth(null), status.getFontSize()+screenHeight/10);
//		g.drawString("��   ��", screenWidth/10+ 2*cursorImage.getWidth(null), status.getFontSize()*3+screenHeight/10);
		g.drawString("��    ��", screenWidth/10+ 2*cursorImage.getWidth(null), status.getFontSize()*1+screenHeight/10);
		g.drawString("��    ��", screenWidth/10+ 2*cursorImage.getWidth(null), status.getFontSize()*3+screenHeight/10);
		g.drawString("��������", screenWidth/10+ 2*cursorImage.getWidth(null), status.getFontSize()*5+screenHeight/10);
		g.drawString("��    ��", screenWidth/10+ 2*cursorImage.getWidth(null), status.getFontSize()*7+screenHeight/10);
		//ù��°�� ��ġ
		if(cursor.getPosition() == 0)
		{
			g.drawImage(cursorImage, screenWidth/10+(cursorImage.getWidth(null)), status.getFontSize()*1+screenHeight/10 - status.getFontSize()/2, null);
			g.drawString("���ΰ��� ����", (int)(screenWidth*(4.0/10.0)), status.getFontSize()*1+screenHeight/10);
			g.drawString("LEVEL : "+player.getLevel(), 
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*2+screenHeight/10);
			g.drawString("HP : "+player.getNowStatus().getHP() + " / " + player.getMaxStatus().getHP(), 
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*3+screenHeight/10);
			g.drawString("MP : "+player.getNowStatus().getMP() + " / " + player.getMaxStatus().getMP(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*4+screenHeight/10);
			g.drawString("Str : "+player.getNowStatus().getStrength() + " / " + player.getMaxStatus().getStrength(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*5+screenHeight/10);
			g.drawString("Vit : "+player.getNowStatus().getVitality() + " / " + player.getMaxStatus().getVitality(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*6+screenHeight/10);
			g.drawString("Agt : "+player.getNowStatus().getAgility()+ " / " + player.getMaxStatus().getAgility(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*7+screenHeight/10);
			g.drawString("Int : "+player.getNowStatus().getIntelligence()+ " / " + player.getMaxStatus().getIntelligence(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*8+screenHeight/10);
			g.drawString("Knw : "+player.getNowStatus().getKnowledge()+ " / " + player.getMaxStatus().getKnowledge(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*9+screenHeight/10);
			g.drawString("EXP : "+player.getNowEXP()+ " / " + player.getMaxStatus().getEXP(),
					(int)(screenWidth*(4.0/10.0))+status.getFontSize(), status.getFontSize()*10+screenHeight/10);
			
		}
		else if(cursor.getPosition() == 1)
		{
			g.drawImage(cursorImage, screenWidth/10+(cursorImage.getWidth(null)), status.getFontSize()*3+screenHeight/10 - status.getFontSize()/2, null);
		}
		else if(cursor.getPosition() == 2)
		{
			g.drawImage(cursorImage, screenWidth/10+(cursorImage.getWidth(null)), status.getFontSize()*5+screenHeight/10 - status.getFontSize()/2, null);
		}
		else if(cursor.getPosition() == 3)
		{
			g.drawImage(cursorImage, screenWidth/10+(cursorImage.getWidth(null)), status.getFontSize()*7+screenHeight/10 - status.getFontSize()/2, null);
		}
	}

	//��� ���
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
		
		Graphics2D imageG = (Graphics2D) gameMap.getGraphics();
		displayForeground(imageG, false);
		
		//�ΰ��� ��� 
		g.drawImage(gameMap, mapStartingPointX, mapStartingPointY, sizeW, sizeH, null);

	}

	//����, ��� �κп��� ��� �κ��� ����ؾ��ϴ°�?
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
						g.drawImage(mapTile[i][j].getM_TileIcon(), (int)((j*DrawingTemplate.pixel)),
								(int)((i*DrawingTemplate.pixel)),
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

	//��ȭ�� ���游 �������
	public void displayChangedForeground(Graphics2D g, boolean isUpper)
	{
		//���ĵ� ĳ�������� Ȱ��
		Tile[][] mapTile = gameData.getGameMap().getM_ForegroundTile();
		
		
		Vector<GameCharacter>actors = gameData.getSortedCharacters();
		
		for(int charIndex = 0 ; charIndex < actors.size(); charIndex++)
		{
			GameCharacter actor = actors.elementAt(charIndex);
			
			int charX = actor.getxPosition() / GameData.mapCharArrayRatio;
			int charY = actor.getyPosition() / GameData.mapCharArrayRatio;
			
//			if(actor instanceof Alliance)
//				System.out.println(""+charX + " : " + charY);
			for(int i = -1 ; i < 3; i++)
			{				
				for(int j = -1 ; j < 3; j++)
				{
					Image tile = mapTile[charY+j][charX+i].getM_TileIcon();
					if(mapTile[charY+j][charX+i].getIsUpper() == isUpper)
					{
						g.drawImage(tile, mapStartingPointX + (int)((charX+i)*DrawingTemplate.pixel*ratioX),
								mapStartingPointY+ (int)((charY+j)*DrawingTemplate.pixel*ratioY),
								(int)(tile.getWidth(null) * ratioX)+1,
								(int)(tile.getHeight(null) * ratioX)+1, null);
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

		int playerTimer = gameData.getAnimTimer();
		//���� ��ġ
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
	
	//������ ���ø��
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
//		
//		if(gameData.getGameRobot().canIchangeImage(actor, actorAnim, GameDisplay.TIMER))
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
	
	//npc�� ���
	public void displayAlliance(Graphics2D g,GameCharacter alliance)
	{
		// �ִϸ��̼� ���� ���
		CharacterEditorSystem allianceAnim = (CharacterEditorSystem) alliance
				.getCharacter();
		// ��쿡 ���� �ִϸ��̼� ���, �������̳� ������
		if (alliance.getActorState() == GameCharacter.MOVESTATE) {
			// ���� �ִϸ��̼� ���
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
			// ������ �ִϸ��̼� ���
			else {
				// �̵� �����϶�
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
	
	//��ü ĳ���͵��� ü�� ǥ��
	public void displayPlayer(Graphics2D g)
	{
		//ĳ������
		Alliance player = (Alliance) gameData.getPlayer();
		//ĳ���� �ִϸ��̼� ����
		CharacterEditorSystem playerAnim = (CharacterEditorSystem) gameData.getPlayer().getCharacter();
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

	//���͵� ���
	public void displayMonster(Graphics2D g, GameCharacter monster)
	{
		
		MonsterEditorSystem monsterAnim = (MonsterEditorSystem) monster
		.getCharacter();

		//���Ͱ� ������ �����̸�
		if(monster.getActorState() == GameCharacter.MOVESTATE)
		{
			//�����ϸ�
			if(monster.getActionType() == GameCharacter.STOP)
			{
				//���� �ִϸ��̼� ���
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
				//������ �ִϸ��̼� ���
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

	//���͵��� ���
	public void displayActors(Graphics2D g)
	{
		//�켱 ������ �ʿ���
		Vector<GameCharacter> actors = gameData.getSortedCharacters();
		
		for(int i = 0 ; i < actors.size(); i++)
		{
			//���ΰ� �޾ƿ�
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
				//���� ���
				displayMonster(g, (GameCharacter)actors.elementAt(i));
			}
			displayHealthBar(g, actors.elementAt(i));
		}
	}
	

	//���͵��� ü�� ���
	public void displayHealthBar(Graphics2D g, GameCharacter actor)
	{
		int startX = (int) ((actor.getxPosition()*GameData.mapCharArrayRatio)*ratioX)
			+mapStartingPointX - DrawingTemplate.pixel;
		int startY = (int) ((actor.getyPosition()*GameData.mapCharArrayRatio)*ratioY)
			+mapStartingPointY + DrawingTemplate.pixel + DrawingTemplate.pixel/2;
//		
		
		int fullHP = (int) (DrawingTemplate.pixel*ratioX);
		
		double ratio = (double)(actor.getMaxStatus().getHP() - actor.getNowStatus().getHP())/(double)actor.getMaxStatus().getHP();
		ratio*=fullHP;
		
		g.setColor(Color.green);
		g.fill3DRect(startX, startY, fullHP, 8, false);

		if(ratio != 0)
		{
			g.setColor(Color.red);
			g.fill3DRect(startX, startY, (int)ratio, 8, false);
		}
	}
	
	public void displayLevelUp(Graphics2D g, GameCharacter player)
	{
		int startX = (int) ((player.getxPosition()*GameData.mapCharArrayRatio)*ratioX)
			+mapStartingPointX - DrawingTemplate.pixel;
		int startY = (int) ((player.getyPosition()*GameData.mapCharArrayRatio)*ratioY)
				+mapStartingPointY - DrawingTemplate.pixel;
		
		Image levelUp = gameData.getLevelUpImage().getUtilImage();
		
		if(animTimer < GameDisplay.TIMER/2)
		{
			animTimer++;
			g.drawImage(levelUp, startX, startY,screenWidth/16,screenHeight/16, null);
		}
		else
		{
			animTimer = 0;
			player.setLevelUp(false);
		}
	}
	
	//GameRunning!!!!!!!
	@Override
	public void run() {
		// TODO Auto-generated method stub

		gameGraphics = (Graphics2D) hwResource.getDrawGraphics();
		displayLogoImage(gameGraphics);
		while (gameData.getGameState() != GameData.EXIT) 
		{
			try {
				//gameGraphics = (Graphics2D) hwResource.getDrawGraphics();
				int gameState = gameData.getGameState();
				// ���°� �ΰ��
				if (gameState == GameData.LOGOSCREEN) 
				{
					//gameGraphics = (Graphics2D) hwResource.getDrawGraphics();
					TIMER = SLOWTIMER;
					displayLogoImage(gameGraphics);
				}
				// ���°� �޴���
				else if (gameState == GameData.TITLEMENU) 
				{
					displayTitleMenu(gameGraphics);
				}
				// �ε���
				else if (gameState == GameData.LOADING) 
				{
					displayLoadingScreen(gameGraphics);
					TIMER = FASTTIMER;
				}
				// �ε�ȭ��
				else if (gameState == GameData.LOAD) 
				{
					// ���� �̱���
				}
				else if(gameState == GameData.MAPLOADENDED)
				{
					//���� �ҷȴٸ� �ѹ� Ǯ�� ���
					displayBackground(gameGraphics);
					//displayForeground(gameGraphics, false);
					displayForeground(gameGraphics, true);
				}
				// ���°� �÷����̸�
				else if (gameState == GameData.PLAY) 
				{
					//ĳ���� ���� ��� ���
					displayBackground(gameGraphics);
					//displayChangedForeground(gameGraphics, false);
					//ĳ���� ���
					displayActors(gameGraphics);
					//ĳ���� ���� ���
					///displayForeground(gameGraphics, false);
					//displayForeground(gameGraphics, true);
					displayChangedForeground(gameGraphics, true);
//					displayForeground(gameGraphics, false);
					displayForeground(gameGraphics, true);
					if(gameData.getPlayer().isLevelUp)
					{
						displayLevelUp(gameGraphics, gameData.getPlayer());
					}
				} 
				else if (gameState == GameData.GAMEOVER)
				{
					TIMER = 100;
					displayGameOver(gameGraphics);
				}
				else if(gameState == GameData.STATUSCALLED)
				{
					//�������ͽ� â
					displayStatusScreen(gameGraphics);
				}
				
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
//			hwResource.show();
			gameGraphics.dispose();

		}// end while
		
	}
	
	
	//���� ����Ʈ
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











