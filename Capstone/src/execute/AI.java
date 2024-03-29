package execute;

import java.util.Vector;

import MapEditor.Map;
import MapEditor.Tile;

public class AI {

	//게임 이벤트 배열
	private int[][] gameTile;
	private GameData gameData;
	
	private int exActionType = 0;
	
	public AI(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	
	
	//액터가 움직일 수 있는지 없는지 확인
	//x,y는 이동하고자 하는 좌표
	public boolean actorCanMove(int x, int y, Map gameMap, int[][] gameTile, GameCharacter actor)
	{
		int mapX = x/GameData.mapCharArrayRatio;
		int mapY = y/GameData.mapCharArrayRatio;
		
		Tile[][] back = gameMap.getM_BackgroundTile();
		Tile[][] fore = gameMap.getM_ForegroundTile();
		
		
		int charNum = 0;
		Vector<GameCharacter> total = gameData.getSortedCharacters();
		
		//캐릭터의 번호 찾음
		for(int i = 0 ; i < total.size(); i++)
		{
			GameCharacter tmp = total.elementAt(i);
			if(actor.equals(tmp))
			{
				break;
			}
			charNum++;
		}
		
		//맵이 움직이지 못하는 곳이면 이동 불가
		try {
			if (back[mapY][mapX].getIsMove() == false
					|| fore[mapY][mapX].getIsMove() == false)
				return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}

		//자기 인덱스가 아니면
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
		
		
		//그외에는 이동 가능
		return true;
	}
	
	//플레이어와 몬스터가 공격 가능한가?
	public boolean canAttack(GameCharacter attack, GameCharacter defend)
	{
		
		//거리가 사거리에 닫으면
		int direction = attack.getNowDirection();
		int ratio = GameData.mapCharArrayRatio;
		ratio/=2;
		int range = attack.getAttackRange()+ratio;
		
		//두 지점사이의 거리가 사거리보다 적어야함
		
		int difX = Math.abs(attack.getxPosition() - defend.getxPosition());
		int difY = Math.abs(attack.getyPosition() - defend.getyPosition());
		
		if(direction == GameCharacter.UP)
		{
			if(difY <= ratio+range && difX <= ratio && defend.getyPosition()<attack.getyPosition())
				return true;
		}
		else if(direction == GameCharacter.DOWN)
		{
			if(difY <= ratio+range && difX <= ratio && defend.getyPosition()>attack.getyPosition())
				return true;		
		}
		else if(direction == GameCharacter.LEFT)
		{
			if(difX <= ratio+range && difY <= ratio && defend.getxPosition()<attack.getxPosition())
				return true;
		}
		else if(direction == GameCharacter.RIGHT)
		{
			if(difX <= ratio+range && difY <= ratio && defend.getxPosition()>attack.getxPosition())
				return true;		
		}	
		return false;
	}
	
	//시야내에 케릭터 잇는지 확인
	public boolean canWatch(GameCharacter monster, GameCharacter player)
	{
		//기본적으로 시야는 5칸이라고 생각함
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
	
	//이동
	public void moveActor(GameCharacter actor, Map gameMap, int[][]gameTile, int direction)
	{
		actor.setNowDirection(direction);
		if (direction == GameCharacter.UP) {
			// 방향이 위로 나오면
			int nextY = actor.getyPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap, gameTile,actor)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (direction== GameCharacter.DOWN) {
			// 방향이 아래로 나오면
			int nextY = actor.getyPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY > (gameTile.length- GameData.mapCharArrayRatio)) {
				nextY = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile,actor)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.RIGHT) {
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX > (gameTile[0].length - GameData.mapCharArrayRatio)) {
				nextX = gameTile[0].length
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile,actor)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		} else if (direction == GameCharacter.LEFT) {
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile,actor)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		}	
	}
	
	//npc 랜덤무브
	public void randomMove(GameCharacter actor, Map gameMap, int[][] gameTile) 
	{
		// 4 방향 설정
		int direction = (int) ((Math.random() * 10))%4;
		moveActor(actor, gameMap, gameTile, direction);
	}
	
	//npc 직진 이동
	public void moveStraight(GameCharacter actor, Map gameMap, int[][] gameTile)
	{
		moveActor(actor, gameMap, gameTile, actor.getNowDirection());
	}
	
	
	//플레이어한테 무브
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
	
	
	//npc의 액션 - 이동밖에 없음
	public void NPCAction(Vector<GameCharacter> alliances, GameCharacter player, Map gameMap, int[][] gameTile)
	{
		//캐릭터들이 없으면 그냥 리턴, 혹은 플레이어가 지금 이벤트 진행중이면 다른 애니메이션 안함
		if(alliances == null || player.getActorState() == GameCharacter.EVENTSTATE)
			return;
		
		/*************************************************************************/
		/*****************플레이어가 액션 버튼을 누르면 이벤트를 실행한다.***********************/
		
		
		
		
		
		/*************************************************************************/

		//npc를 움직임
		//저장된 모든 npc에 대해서
		for(int i = 0 ; i < alliances.size(); i++)
		{
			GameCharacter alliance = alliances.elementAt(i);
			
			//랜덤무브
			if(alliance.getActionType() == GameCharacter.RANDOM)
			{
				//랜덤이동
				this.randomMove(alliance, gameMap, gameTile);
				// 좀더 부드러운 무브를 유지하기위해
				this.exActionType = GameCharacter.RANDOM;
				// 직진하시오
				alliance.setActionType(GameCharacter.MOVESTRAIGHT);
			}
			else if(alliance.getActionType() == GameCharacter.TOPLAYER)
			{
				//플레이어한테로
			}
			else if(alliance.getActionType() == GameCharacter.STOP)
			{
				//정지상태
				alliance.setActionType(GameCharacter.STOP);
			}
			else if(alliance.getActionType() == GameCharacter.RUNFROMPLAYER)
			{
				//플레이어에게서 벗어남
			}
			else if(alliance.getActionType() == GameCharacter.STOPAFTERRANDOM)
			{
				//50%확률로 정지, 50%확률로 랜덤이동
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
				//10%확률로 방향전환
				int p = (int)(Math.random()*100);
				if(p < 10)
					alliance.setActionType(GameCharacter.RANDOM);
				//50%의 확률로 정지
				p = (int)(Math.random()*100);
				if( p < 50)
					alliance.setActionType(GameCharacter.STOPAFTERRANDOM);
				
				//이전 움직임 타입이 랜덤이었으면 조금더 직진이동
				this.moveStraight(alliance, gameMap, gameTile);
			}
			
			//타일의 설정
		}
		//타일의 설정
		
		
	}

	//몬스터 액션 - 주인공한테 가까이 오고 너무 멀리 있음 움직이지 않음..
	public void monsterAction(Vector<GameCharacter> monsters, GameCharacter player, Map gameMap, int[][] gameTile)
	{
		if(monsters == null || player.getActorState() == GameCharacter.EVENTSTATE)
			return;
		
		
		for(int i = 0 ; i < monsters.size(); i++)
		{
			GameCharacter monster = monsters.elementAt(i);
			int now = i;
			
			/****************************플레이어 액션시************************************/
			if(gameData.isActionAnimFlag() == true)
			{
				if(canAttack(player, monster) == true)
				{
					monsters.remove(i);
					i = now;
					continue;
				}
			}
			
			//몬스터가 플레이어 공격 가능하면
			if (canAttack(monster, gameData.getPlayer()) == true) 
			{
				//40%확률로 공격, 난이도때문
				int p = (int)(Math.random()*100);
				if(p > 60)
				{
					//어택
					monster.setActionType(GameCharacter.ATTACK);
					monster.setActorState(GameCharacter.BATTLESTATE);
					monster.attack(monster, player);
					//System.out.println("몬스터 공격");
				}
				else
				{
					monster.setActorState(GameCharacter.MOVESTATE);
					monster.setActionType(GameCharacter.STOP);
					//System.out.println("몬스터 정지");
				}
			}
			else
			{
				//플레이어가 시야에 들어오면
				if(canWatch(monster, gameData.getPlayer()) == true)
				{
					//플레이어한테로 무브
					monster.setActorState(GameCharacter.MOVESTATE);
					moveToPlayer(monster, player, gameMap, gameTile);
					//System.out.println("플레이어한테 움직임");
				}
				else
				{
					//System.out.println("대기 혹은 움직임");
					monster.setActorState(GameCharacter.MOVESTATE);
					
					//대기 혹은 랜덤 무브
					int p = (int)(Math.random()*100);
					//System.out.println(p);
					//정지속성이 강함
					if(p < 85)
					{
						//정지
						monster.setActionType(GameCharacter.STOP);
						//System.out.println("몬스터 정지");
					}
					else
					{
						p = (int)(Math.random()*10)%4;
						//p가 방향임
						//System.out.println("몬스터 랜덤이동");
						monster.setNowDirection(p);
						moveStraight(monster, gameMap, gameTile);
					}
				}
			}
		}
		
	}
	
	public void setGameTile(int[][] gameTile) {
		this.gameTile = gameTile;
	}

	public int[][] getGameTile() {
		return gameTile;
	}
	
}
