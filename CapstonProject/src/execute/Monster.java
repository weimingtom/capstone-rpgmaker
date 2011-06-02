package execute;

import java.io.FileNotFoundException;

import characterEditor.Abilities;
import characterEditor.Actors;
import characterEditor.CharacterEditorSystem;
import characterEditor.MonsterEditorSystem;
import eventEditor.EventTile;

public class Monster extends GameCharacter {

	private MonsterEditorSystem monster;
	
	
	public Monster(String gamePath)
	{
		monster = new MonsterEditorSystem(gamePath);
		maxStatus = monster.getInitAbility();

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
