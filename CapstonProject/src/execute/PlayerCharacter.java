package execute;

import java.io.FileNotFoundException;
import java.util.List;

import characterEditor.Abilities;
import characterEditor.Actors;
import characterEditor.CharacterEditorSystem;
import characterEditor.NPCEditorSystem;
import eventEditor.Event;
import eventEditor.EventTile;

public class PlayerCharacter extends GameCharacter {

	private CharacterEditorSystem actor;
	
	//������
	public PlayerCharacter(String gamePath)
	{
		actor = new CharacterEditorSystem(gamePath);
		maxStatus = actor.getInitAbility();
		nowStatus = new Abilities();
		nowStatus.setAgility(maxStatus.getAgility());
		nowStatus.setEXP(maxStatus.getEXP());
		nowStatus.setHP(maxStatus.getHP());
		nowStatus.setIntelligence(maxStatus.getIntelligence());
		nowStatus.setKnowledge(maxStatus.getKnowledge());
		nowStatus.setMP(maxStatus.getMP());
		nowStatus.setStrength(maxStatus.getStrength());
		nowStatus.setVitality(maxStatus.getVitality());
	}
	
	//���� ��ġ
	@Override
	public void deployActor(int actorIndex, int xPosition, int yPosition, Event eventList)
	{
		try {
			actor.load(actorIndex);
			speed = actor.getSpeed();
			if(speed == 0 )
				speed = 1;
			this.setxPosition(xPosition);
			this.setyPosition(yPosition);
			this.setActorEvent(eventList);
			maxStatus = actor.getInitAbility();
			nowStatus = new Abilities();
			nowStatus.setAgility(maxStatus.getAgility());
			nowStatus.setEXP(0);
			nowStatus.setHP(maxStatus.getHP());
			nowStatus.setIntelligence(maxStatus.getIntelligence());
			nowStatus.setKnowledge(maxStatus.getKnowledge());
			nowStatus.setMP(maxStatus.getMP());
			nowStatus.setStrength(maxStatus.getStrength());
			nowStatus.setVitality(maxStatus.getVitality());
			level = actor.getInitLevel();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Actors getCharacter() {
		// TODO Auto-generated method stub
		return actor;
	}

	@Override
	public void changeActor(int actorIndex, int xPosition, int yPosition) {
		// TODO Auto-generated method stub
		
	}

}
