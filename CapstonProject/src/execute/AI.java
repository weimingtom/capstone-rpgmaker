package execute;

import java.util.Vector;

import characterEditor.Abilities;

import animationEditor.Animations;

import MapEditor.Map;
import MapEditor.Tile;

public class AI {

	//���� �̺�Ʈ �迭
	private int[][] gameTile;
	private GameData gameData;
	
	private int exActionType = 0;
	
	public AI(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	
	
	//���Ͱ� ������ �� �ִ��� ������ Ȯ��
	//x,y�� �̵��ϰ��� �ϴ� ��ǥ
	public boolean actorCanMove(int x, int y, Map gameMap, int[][] gameTile, GameCharacter actor)
	{
		int mapX = x/GameData.mapCharArrayRatio;
		int mapY = y/GameData.mapCharArrayRatio;
		
		Tile[][] back = gameMap.getM_BackgroundTile();
		Tile[][] fore = gameMap.getM_ForegroundTile();
		
		
		int charNum = 0;
		Vector<GameCharacter> total = gameData.getSortedCharacters();
		
		//ĳ������ ��ȣ ã��
		for(int i = 0 ; i < total.size(); i++)
		{
			GameCharacter tmp = total.elementAt(i);
			if(actor.equals(tmp))
			{
				break;
			}
			charNum++;
		}
		
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

		//�ڱ� �ε����� �ƴϸ�
		try
		{
			int ratio = GameData.mapCharArrayRatio;
			
			for(int i = 0 ; i < ratio; i++)
			{
				for(int j = 0 ; j < ratio; j++)
				{
					if(gameTile[y-ratio/2+i][x-ratio/2+j]!= -1 && gameTile[y-ratio/2+i][x-ratio/2+j]!=charNum)
					{
						return false;
					}
				}
				
			}
			if(gameTile[y][x] != -1 && gameTile[y][x] != charNum)
				return  false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		
		
		//�׿ܿ��� �̵� ����
		return true;
	}
	
	//�÷��̾�� ���Ͱ� ���� �����Ѱ�?
	public boolean canAttack(GameCharacter attack, GameCharacter defend)
	{
		
		//�Ÿ��� ��Ÿ��� ������
		int direction = attack.getNowDirection();
		int ratio = GameData.mapCharArrayRatio;
		ratio/=2;
		int range = attack.getAttackRange()+ratio;
		
		//�� ���������� �Ÿ��� ��Ÿ����� �������
		
		int difX = Math.abs(attack.getxPosition() - defend.getxPosition())+ratio/2;
		int difY = Math.abs(attack.getyPosition() - defend.getyPosition())+ratio/2;
		
		if(direction == GameCharacter.UP)
		{
			if(difY <= ratio+range && difX <= ratio*2 && defend.getyPosition()<=attack.getyPosition())
				return true;
		}
		else if(direction == GameCharacter.DOWN)
		{
			if(difY <= ratio+range && difX <= ratio*2 && defend.getyPosition()>=attack.getyPosition())
				return true;		
		}
		else if(direction == GameCharacter.LEFT)
		{
			if(difX <= ratio+range && difY <= ratio*2 && defend.getxPosition()<=attack.getxPosition())
				return true;
		}
		else if(direction == GameCharacter.RIGHT)
		{
			if(difX <= ratio+range && difY <= ratio*2 && defend.getxPosition()>=attack.getxPosition())
				return true;		
		}	
		return false;
	}
	

	//�þ߳��� �ɸ��� �մ��� Ȯ��
	public boolean canWatch(GameCharacter monster, GameCharacter player)
	{
		//�⺻������ �þߴ� 5ĭ�̶�� ������
		int ratio = GameData.mapCharArrayRatio * 6;
		
		int difX = Math.abs(monster.getxPosition() - player.getxPosition());
		int difY = Math.abs(monster.getyPosition() - player.getyPosition());
		
		if(difX + difY < ratio)
		{
			return true;
		}
		else
			return false;
	}
	
	//�̵�
	public void moveActor(GameCharacter actor, Map gameMap, int[][]gameTile, int direction)
	{
		actor.setNowDirection(direction);
		if (direction == GameCharacter.UP) {
			// ������ ���� ������
			int nextY = actor.getyPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap, gameTile,actor)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (direction== GameCharacter.DOWN) {
			// ������ �Ʒ��� ������
			int nextY = actor.getyPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextY > (gameTile.length- GameData.mapCharArrayRatio)) {
				nextY = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile,actor)) {
				// �̵������ϸ�
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.RIGHT) {
			// ������ ���������� ������
			int nextX = actor.getxPosition() + actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX > (gameTile[0].length - GameData.mapCharArrayRatio)) {
				nextX = gameTile[0].length
						- GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile,actor)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		} else if (direction == GameCharacter.LEFT) {
			// ������ ���������� ������
			int nextX = actor.getxPosition() - actor.getSpeed();
			// �׷��� �ʺ��� ũ��
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// �̵������Ѱ�?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile,actor)) {
				// �̵������ϸ�
				actor.setxPosition(nextX);
			}
		}	
	}
	
	//npc ��������
	public void randomMove(GameCharacter actor, Map gameMap, int[][] gameTile) 
	{
		// 4 ���� ����
		int direction = (int) ((Math.random() * 10))%4;
		moveActor(actor, gameMap, gameTile, direction);
	}
	
	//npc ���� �̵�
	public void moveStraight(GameCharacter actor, Map gameMap, int[][] gameTile)
	{
		moveActor(actor, gameMap, gameTile, actor.getNowDirection());
	}
	
	
	//�÷��̾����� ����
	public void moveToPlayer(GameCharacter actor, GameCharacter player, Map gameMap, int[][] gameTile)
	{
		int difX = Math.abs(player.getxPosition() - actor.getxPosition());
		int difY = Math.abs(player.getyPosition() - actor.getyPosition());
		
		if(actor.getxPosition() >= player.getxPosition() && difX >= difY)
		{
			moveActor(actor, gameMap, gameTile, GameCharacter.LEFT);
		}
		else if(actor.getxPosition() <= player.getxPosition() && difX >= difY)
		{
			moveActor(actor, gameMap, gameTile, GameCharacter.RIGHT);
		}
		else if(actor.getyPosition() >= player.getyPosition() && difY >= difX)
		{
			moveActor(actor, gameMap, gameTile, GameCharacter.UP);
		}
		else if(actor.getyPosition() <= player.getyPosition()&& difY >= difX)
		{
			moveActor(actor, gameMap, gameTile, GameCharacter.DOWN);
		}
	}
	
	
	//npc�� �׼� - �̵��ۿ� ����
	public void NPCAction(Vector<GameCharacter> alliances, GameCharacter player, Map gameMap, int[][] gameTile)
	{
		//ĳ���͵��� ������ �׳� ����, Ȥ�� �÷��̾ ���� �̺�Ʈ �������̸� �ٸ� �ִϸ��̼� ����
		if(alliances == null || player.getActorState() == GameCharacter.EVENTSTATE
				|| GameCharacter.STATUSCALLED == player.getActorState())
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
				//�÷��̾ �þ߿� ������
				if(canWatch(alliance, gameData.getPlayer()) == true)
				{
					//�÷��̾����׷� ����
					alliance.setActorState(GameCharacter.MOVESTATE);
					alliance.setActionType(GameCharacter.MOVESTRAIGHT);
					moveToPlayer(alliance, player, gameMap, gameTile);
					//System.out.println("�÷��̾����� ������");
				}
				else
				{
					//System.out.println("��� Ȥ�� ������");
					alliance.setActorState(GameCharacter.MOVESTATE);
					alliance.setActionType(GameCharacter.MOVESTRAIGHT);
					//��� Ȥ�� ���� ����
					int p = (int)(Math.random()*100);
					//System.out.println(p);
					//�����Ӽ��� ����
					if(p < 85)
					{
						//����
						alliance.setActionType(GameCharacter.STOP);
						//System.out.println("���� ����");
					}
					else
					{
						p = (int)(Math.random()*10)%4;
						//p�� ������
						//System.out.println("���� �����̵�");
						alliance.setNowDirection(p);
						moveStraight(alliance, gameMap, gameTile);
					}
				}
				
				
			}
			else if(alliance.getActionType() == GameCharacter.STOP)
			{
				//��������
				alliance.setActionType(GameCharacter.STOP);
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

	//���� �׼� - ���ΰ����� ������ ���� �ʹ� �ָ� ���� �������� ����..
	public void monsterAction(Vector<GameCharacter> monsters, GameCharacter player, Map gameMap, int[][] gameTile)
	{
		if(monsters == null || player.getActorState() == GameCharacter.EVENTSTATE
				|| GameCharacter.STATUSCALLED == player.getActorState())
			return;
		
		for(int i = 0 ; i < monsters.size(); i++)
		{	
			GameCharacter monster = monsters.elementAt(i);
			int now = i;
			
			/****************************�÷��̤��� �׼ǽ�************************************/
			if(gameData.isActionAnimFlag() == true)
			{
				if(canAttack(player, monster) == true)
				{
					player.attack(player, monster);
					if(monster.getNowStatus().getHP() <= 0)
					{
						monsters.remove(i);
						i = now;
						//����ġ ��
						player.setNowEXP((player.getNowEXP() + monster.getMaxStatus().getHP()/10));
						if(player.getNowEXP() >= player.getMaxStatus().getEXP())
						{
							leverUp(player);
						}
						continue;
					}
				}
			}
			
			//���Ͱ� �÷��̾� ���� �����ϸ�
			if (canAttack(monster, gameData.getPlayer()) == true) 
			{
				//40%Ȯ���� ����, ���̵�����
				int p = (int)(Math.random()*100);
				if(p > 60)
				{
					//����
					monster.setActionType(GameCharacter.ATTACK);
					monster.setActorState(GameCharacter.BATTLESTATE);
					monster.attack(monster, player);
					//System.out.println("���� ����");
				}
				else
				{
					monster.setActorState(GameCharacter.MOVESTATE);
					monster.setActionType(GameCharacter.STOP);
					//System.out.println("���� ����");
				}
			}
			else
			{
				//�÷��̾ �þ߿� ������
				if(canWatch(monster, gameData.getPlayer()) == true)
				{
					//�÷��̾����׷� ����
					monster.setActorState(GameCharacter.MOVESTATE);
					monster.setActionType(GameCharacter.MOVESTRAIGHT);
					moveToPlayer(monster, player, gameMap, gameTile);
					//System.out.println("�÷��̾����� ������");
				}
				else
				{
					//System.out.println("��� Ȥ�� ������");
					monster.setActorState(GameCharacter.MOVESTATE);
					monster.setActionType(GameCharacter.MOVESTRAIGHT);
					//��� Ȥ�� ���� ����
					int p = (int)(Math.random()*100);
					//System.out.println(p);
					//�����Ӽ��� ����
					if(p < 85)
					{
						//����
						monster.setActionType(GameCharacter.STOP);
						//System.out.println("���� ����");
					}
					else
					{
						p = (int)(Math.random()*10)%4;
						//p�� ������
						//System.out.println("���� �����̵�");
						monster.setNowDirection(p);
						moveStraight(monster, gameMap, gameTile);
					}
				}
			}
		}
		
	}
	
	private void leverUp(GameCharacter player) {
		//������ �� �մٸ�
		if(player.getLevel() != player.getCharacter().getMaxLevel())
		{
			player.setLevel(player.getLevel() + 1);
			Abilities max = player.getMaxStatus();
			Abilities curve = player.getCharacter().getGrowthCurve();
			max.setAgility(max.getAgility() + curve.getAgility());
			max.setHP((max.getHP() + curve.getHP()));
			max.setIntelligence(max.getIntelligence() + curve.getIntelligence());
			max.setKnowledge(max.getKnowledge() + curve.getKnowledge());
			max.setMP(max.getMP() + curve.getMP());
			max.setStrength(max.getStrength() + curve.getStrength());
			max.setVitality(max.getVitality() + curve.getVitality());
			Abilities nowStatus = player.getNowStatus();
			nowStatus.setAgility(max.getAgility());
			nowStatus.setEXP(max.getEXP());
			nowStatus.setHP(max.getHP());
			nowStatus.setIntelligence(max.getIntelligence());
			nowStatus.setKnowledge(max.getKnowledge());
			nowStatus.setMP(max.getMP());
			nowStatus.setStrength(max.getStrength());
			nowStatus.setVitality(max.getVitality());
			max.setEXP(max.getEXP()*2);
			player.setLevelUp(true);
		}
	}



	public void setGameTile(int[][] gameTile) {
		this.gameTile = gameTile;
	}

	public int[][] getGameTile() {
		return gameTile;
	}
	
}
