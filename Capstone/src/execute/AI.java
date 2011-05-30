package execute;

import java.util.Vector;

import MapEditor.Map;
import MapEditor.Tile;

public class AI {

	//게임 이벤트 배열
	private byte[][] gameTile;
	private GameData gameData;
	
	private int exActionType = 0;
	
	public AI(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	
	//액터가 움직일 수 있는지 없는지 확인
	//x,y는 이동하고자 하는 좌표
	public boolean actorCanMove(int x, int y, Map gameMap, byte[][] gameTile)
	{
		int mapX = x/GameData.mapCharArrayRatio;
		int mapY = y/GameData.mapCharArrayRatio;
		
		Tile[][] back = gameMap.getM_BackgroundTile();
		Tile[][] fore = gameMap.getM_ForegroundTile();
		
		
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

		//게임타일이 1로 설정, 즉 누군가 있다면
		if(gameTile[y][x] == 1)
			return false;
		
		
		//그외에는 이동 가능
		return true;
	}
	
	
	//npc 랜덤무브
	public void randomMove(GameCharacter actor, Map gameMap, byte[][] gameTile) 
	{
		// 4 방향 설정
		int direction = (int) ((Math.random() * 10))%4;


		if (direction == GameCharacter.UP) {
			// 방향 설정
			actor.setNowDirection(GameCharacter.UP);
			// 방향이 위로 나오면
			int nextY = actor.getyPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.DOWN) {
			// 방향 설정
			actor.setNowDirection(GameCharacter.DOWN);
			// 방향이 아래로 나오면
			int nextY = actor.getyPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY > (gameTile.length - GameData.mapCharArrayRatio)) {
				nextY = gameTile.length 
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (direction == GameCharacter.RIGHT) {
			// 방향 설정
			actor.setNowDirection(GameCharacter.RIGHT);
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX > (gameTile[0].length - GameData.mapCharArrayRatio)) {
				nextX = gameTile.length 
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		} else if (direction == GameCharacter.LEFT) {
			// 방향 설정
			actor.setNowDirection(GameCharacter.LEFT);
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		} 
	}
	
	//npc 직진 이동
	public void moveStraight(GameCharacter actor, Map gameMap, byte[][] gameTile)
	{
		if (actor.getNowDirection() == GameCharacter.UP) {
			// 방향이 위로 나오면
			int nextY = actor.getyPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY < GameData.mapCharArrayRatio) {
				nextY = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (actor.getNowDirection() == GameCharacter.DOWN) {
			// 방향이 아래로 나오면
			int nextY = actor.getyPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextY > (gameTile.length- GameData.mapCharArrayRatio)) {
				nextY = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(actor.getxPosition(), nextY, gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setyPosition(nextY);
			}
		} else if (actor.getNowDirection() == GameCharacter.RIGHT) {
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() + actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX > (gameTile.length - GameData.mapCharArrayRatio)) {
				nextX = gameTile.length
						- GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		} else if (actor.getNowDirection() == GameCharacter.LEFT) {
			// 방향이 오른쪽으로 나오면
			int nextX = actor.getxPosition() - actor.getSpeed();
			// 그런데 맵보다 크면
			if (nextX < GameData.mapCharArrayRatio) {
				nextX = GameData.mapCharArrayRatio;
			}
			// 이동가능한가?
			if (this.actorCanMove(nextX, actor.getyPosition(), gameMap,
					gameTile)) {
				// 이동가능하면
				actor.setxPosition(nextX);
			}
		}
	}
	
	
	//npc의 이동액션
	public void NPCAction(Vector<GameCharacter> alliances, GameCharacter player, Map gameMap, byte[][] gameTile)
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

	public void monsterAction(Vector<GameCharacter> monsters, GameCharacter player, Map gameMap, byte[][] gameTile)
	{
		if(monsters == null || player.getActorState() == GameCharacter.EVENTSTATE)
			return;
		
		
		/**************************************************************************/
		/**************************************************************************/
		/****************************플레이어 액션시************************************/
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
