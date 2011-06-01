package jobEditor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import editor.ObjectEditorSystem;

/*
 *Jobs �� Job�� �̸��� Ÿ�� ��ȣ, ������ �Ǵ� ��ų�� ����Ʈ�� ������. 
 * 
 * */
public class Jobs extends ObjectEditorSystem implements Serializable {

	private static final long serialVersionUID = -2699743776822513055L;

	private List<SkillInfo> skillList;	// �����Ǵ� ��ų�� ������ ����
	private int attackFrequency;		// ���� ������ ��
	
	public Jobs(String projectPath) {
		super(projectPath, "Job");
		extension = "job";
		
		name = "New_Job";
		this.skillList = new LinkedList<SkillInfo>();
		attackFrequency = 1;
	}

	public List<SkillInfo> getSkillList()				{	return skillList;	}
	public void setSkillList(List<SkillInfo> skillList)	{	this.skillList = skillList;	}
	public int getAttackFrequency()						{	return attackFrequency;	}
	public void setAttackFrequency(int attackFrequency) {
		if(attackFrequency < 0) {
			System.err.println("error: Jobs.getSkillList() (attackFrequency: "+attackFrequency+")");
			attackFrequency = 0;
		}
		this.attackFrequency = attackFrequency;
	}
	
	@Override
	protected void copyObject(ObjectEditorSystem data) {
		Jobs tmpData = (Jobs)data;
		
		this.skillList = tmpData.skillList;
		this.attackFrequency = tmpData.attackFrequency;
	}
}
