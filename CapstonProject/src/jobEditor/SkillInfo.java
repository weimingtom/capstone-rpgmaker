package jobEditor;

import java.io.Serializable;

/*
 * SkillInfo �� Jobs ���� ����ϱ� ���� ������� Ŭ������ ������ �ִ� 
 * ��ų�� �ε����� Ȱ��ȭ ������ ���� ������ ������ �ִ´�.
 * **/
public class SkillInfo implements Serializable {
	
	private static final long serialVersionUID = -2185513198832291767L;
	
	private int indexSkill;
	private int activateLevel;

	public SkillInfo() {
		indexSkill = 0;
		activateLevel = 1;
	}
	
	public SkillInfo(SkillInfo data) {
		indexSkill = data.getIndexSkill();
		activateLevel = data.getActivateLevel();
	}

	public int getIndexSkill() {
		return indexSkill;
	}

	public void setIndexSkill(int indexSkill) {
		this.indexSkill = indexSkill;
	}

	public int getActivateLevel() {
		return activateLevel;
	}

	public void setActivateLevel(int activateLevel) {
		this.activateLevel = activateLevel;
	}

}
