package execute;

import java.io.FileNotFoundException;
import java.util.List;

import characterEditor.Abilities;
import characterEditor.Actors;
import characterEditor.CharacterEditorSystem;
import characterEditor.NPCEditorSystem;
import eventEditor.Event;
import eventEditor.EventTile;

public class Alliance extends GameCharacter {

	private NPCEditorSystem actor;
	
	//생성자
	public Alliance(String gamePath)
	{
		actor = new NPCEditorSystem(gamePath);
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
	
	//액터 배치
	@Override
	public void deployActor(int actorIndex, int xPosition, int yPosition, Event eventContentList)
	{
		try {
			actor.load(actorIndex);
			this.setxPosition(xPosition);
			this.setyPosition(yPosition);
			this.setActorEvent(eventContentList);
			maxStatus = actor.getInitAbility();
			nowStatus.setAgility(maxStatus.getAgility());
			nowStatus.setEXP(maxStatus.getEXP());
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
}
