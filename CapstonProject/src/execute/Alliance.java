package execute;

import java.io.FileNotFoundException;

import characterEditor.Abilities;
import characterEditor.Actors;
import characterEditor.CharacterEditorSystem;
import eventEditor.EventTile;

public class Alliance extends GameCharacter {

	private CharacterEditorSystem actor;
	
	//생성자
	public Alliance(String gamePath)
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
	
	//액터 배치
	@Override
	public void deployActor(int actorIndex, int xPosition, int yPosition, EventTile eventList)
	{
		try {
			actor.load(actorIndex);
			this.setxPosition(xPosition);
			this.setyPosition(yPosition);
			this.setActorEvent(eventList);
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
