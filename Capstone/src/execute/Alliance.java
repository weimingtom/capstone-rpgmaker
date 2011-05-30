package execute;

import java.io.FileNotFoundException;

import characterEditor.Actors;
import characterEditor.CharacterEditorSystem;
import eventEditor.EventTile;

public class Alliance extends GameCharacter {

	private CharacterEditorSystem actor;
	
	//������
	public Alliance(String gamePath)
	{
		actor = new CharacterEditorSystem(gamePath);
	}
	
	//���� ��ġ
	@Override
	public void deployActor(int actorIndex, int xPosition, int yPosition, EventTile eventList)
	{
		try {
			actor.load(actorIndex);
			this.setxPosition(xPosition);
			this.setyPosition(yPosition);
			this.setActorEvent(eventList);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Actors getChracter() {
		// TODO Auto-generated method stub
		return actor;
	}
}
