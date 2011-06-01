package characterEditor;

import java.io.Serializable;

public class MonsterEditorSystem extends Actors implements Serializable {

	private static final long serialVersionUID = -8201577918614748581L;
	
	public MonsterEditorSystem(String projectPath) {
		super(projectPath, "Monster");
		
		name = "New_Monster";
		extension = "monster";
		
		indexJob = 0;
	}
}
