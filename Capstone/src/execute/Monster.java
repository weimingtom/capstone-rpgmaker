package execute;

import java.io.FileNotFoundException;

import characterEditor.Actors;
import characterEditor.MonsterEditorSystem;
import eventEditor.EventTile;

public class Monster extends GameCharacter {

	private MonsterEditorSystem monster;
	
	
	public Monster(String gamePath)
	{
		monster = new MonsterEditorSystem(gamePath);
		maxStatus = monster.getInitAbility();
		nowStatus = monster.getInitAbility();
	}
	
	public void deployActor(int actorIndex, int xPosition, int yPosition, EventTile eventList)
	{
		try {
			monster.load(actorIndex);
			this.setxPosition(xPosition);
			this.setyPosition(yPosition);
			this.setActorEvent(eventList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Actors getCharacter() {
		// TODO Auto-generated method stub
		return monster;
	}
}
