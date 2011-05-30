package execute;

import java.util.Vector;

import MapEditor.Map;
import MapEditor.Tile;

public class AI {

	//���� �̺�Ʈ �迭
	private byte[][] gameTile;
	private GameData gameData;
	
	private int exActionType = 0;
	
	public AI(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	
	//���Ͱ� ������ �� �ִ��� ������ Ȯ��
	//x,y�� �̵��ϰ��� �ϴ� ��ǥ
	public boolean actorCanMove(int x, int y, Map gameMap, byte[][] gameTile)
	{
		int mapX = x/GameData.mapCharArrayRatio;
		int mapY = y/GameData.mapCharArrayRatio;
		
		Tile[][] back = gameMap.getM_BackgroundTile();
		Tile[][] fore = gameMap.getM_ForegroundTile();
		
		
		//���� �������� ���ϴ� ���̸� �̵� �Ұ�
		try {
			if (back[mapY][mapX].getIsMove() == false
					|| fore[mapY][mapX].getIsMove() == false)
				return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}

		//����Ÿ���� 1�� ����, �� ������ �ִٸ�
		if(gameTile[y][x] == 1)
			return false;
		
		
		//�׿ܿ��� �̵� ����
		return true;
	}
	
	
	//npc ��������
	public void randomMove(GameCharacter actor, Map gameMap, byte[][] gameTile) 
	{
		// 4 ���� ����
		int direction = (int) ((Math.random() * 10))%4;


		if (direction == GameCharacter.UP) {
			// ���� ����
			actor.setNowDirection(GameCharacter.UP);
			// ������ ���� ������
			int nextY = actor.getyPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.DOWN) {
			// ���� ����
			actor.setNowDirection(GameCharacter.DOWN);
			// ������ �Ʒ��� ������
			int nextY = actor.getyPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY > (gameTile.length - GameData.mapCharArrayRatio)) {
				nextY = gameTile.length 
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.RIGHT) {
			// ���� ����
			actor.setNowDirection(GameCharacter.RIGHT);
			// ������ ���������� ������
			int nextX = actor.getxPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX > (gameTile[0].length - GameData.mapCharArrayRatio)) {
				nextX = gameTile.length 
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		} else if (direction == GameCharacter.LEFT) {
			// ���� ����
			actor.setNowDirection(GameCharacter.LEFT);
			// ������ ���������� ������
			int nextX = actor.getxPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		} 
	}
	
	//npc ���� �̵�
	public void moveStraight(GameCharacter actor, Map gameMap, byte[][] gameTile)
	{
		if (actor.getNowDirection() == GameCharacter.UP) {
			// ������ ���� ������
			int nextY = actor.getyPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (actor.getNowDirection() == GameCharacter.DOWN) {
			// ������ �Ʒ��� ������
			int nextY = actor.getyPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY > (gameTile.length- GameData.mapCharArrayRatio)) {
				nextY = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (actor.getNowDirection() == GameCharacter.RIGHT) {
			// ������ ���������� ������
			int nextX = actor.getxPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX > (gameTile.length - GameData.mapCharArrayRatio)) {
				nextX = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		} else if (actor.getNowDirection() == GameCharacter.LEFT) {
			// ������ ���������� ������
			int nextX = actor.getxPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		}
	}
	
	
	//npc�� �̵��׼�
	public void NPCAction(Vector<GameCharacter> alliances, GameCharacter player, Map gameMap, byte[][] gameTile)
	{
		//ĳ���͵��� ������ �׳� ����, Ȥ�� �÷��̾ ���� �̺�Ʈ �������̸� �ٸ� �ִϸ��̼� ����
		if(alliances == null || player.getActorState() == GameCharacter.EVENTSTATE)
			return;
		
		/*************************************************************************/
		/*****************�÷��̾ �׼� ��ư�� ������ �̺�Ʈ�� �����Ѵ�.***********************/
		
		
		
		
		
		/*************************************************************************/

		//npc�� ������
		//����� ��� npc�� ���ؼ�
		for(int i = 0 ; i < alliances.size(); i++)
		{
			GameCharacter alliance = alliances.elementAt(i);
			
			//��������
			if(alliance.getActionType() == GameCharacter.RANDOM)
			{
				//�����̵�
				this.randomMove(alliance, gameMap, gameTile);
				// ���� �ε巯�� ���긦 �����ϱ�����
				this.exActionType = GameCharacter.RANDOM;
				// �����Ͻÿ�
				alliance.setActionType(GameCharacter.MOVESTRAIGHT);
			}
			else if(alliance.getActionType() == GameCharacter.TOPLAYER)
			{
				//�÷��̾����׷�
			}
			else if(alliance.getActionType() == GameCharacter.STOP)
			{
				//��������
			}
			else if(alliance.getActionType() == GameCharacter.RUNFROMPLAYER)
			{
				//�÷��̾�Լ� ���
			}
			else if(alliance.getActionType() == GameCharacter.STOPAFTERRANDOM)
			{
				//50%Ȯ���� ����, 50%Ȯ���� �����̵�
				if(exActionType == GameCharacter.MOVESTRAIGHT)
				{
					int p = (int)(Math.random()*100);
					if(p<50)
						alliance.setActionType(GameCharacter.STOPAFTERRANDOM);
					else
						alliance.setActionType(GameCharacter.MOVESTRAIGHT);
				}
			}
			else if(alliance.getActionType() == GameCharacter.MOVESTRAIGHT)
			{
				this.exActionType = GameCharacter.MOVESTRAIGHT;
				//10%Ȯ���� ������ȯ
				int p = (int)(Math.random()*100);
				if(p < 10)
					alliance.setActionType(GameCharacter.RANDOM);
				//50%�� Ȯ���� ����
				p = (int)(Math.random()*100);
				if( p < 50)
					alliance.setActionType(GameCharacter.STOPAFTERRANDOM);
				
				//���� ������ Ÿ���� �����̾����� ���ݴ� �����̵�
				this.moveStraight(alliance, gameMap, gameTile);
			}
			
			//Ÿ���� ����
		}
		//Ÿ���� ����
		
		
	}

	public void monsterAction(Vector<GameCharacter> monsters, GameCharacter player, Map gameMap, byte[][] gameTile)
	{
		if(monsters == null || player.getActorState() == GameCharacter.EVENTSTATE)
			return;
		
		
		/**************************************************************************/
		/**************************************************************************/
		/****************************�÷��̾� �׼ǽ�************************************/
		/**************************************************************************/
		/**************************************************************************/
		
	}
	
	public void setGameTile(byte[][] gameTile) {
		this.gameTile = gameTile;
	}

	public byte[][] getGameTile() {
		return gameTile;
	}
	
}
