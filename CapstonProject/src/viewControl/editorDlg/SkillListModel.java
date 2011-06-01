package viewControl.editorDlg;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import jobEditor.SkillInfo;

public class SkillListModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private String projectPath;
	private Vector<String[]> list;
	private String[] data;
	
	int index;
	
	public SkillListModel(List<SkillInfo> skillList, boolean isNew, String projectPath) {
		this.projectPath = projectPath;
		list = new Vector<String[]>();
		data = new String[2];
		
		if (!isNew && skillList != null) {
			for (int i = 0; i < skillList.size(); i++) {
				regist((SkillInfo)skillList.get(i));
			}
		}
	}
	
	public void regist(SkillInfo data) {
		// list ������ �����ϱ� ���� �ӽ� String �迭 ����
		String af[] = new String[2];
		
		// indexSkill�� �����Ѵ�.
		int indexSkill = data.getIndexSkill();
		
		// projectPath/Job ������ ���� ����Ʈ�� �޾ƿ´�.
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathName) {
				if(pathName.getName().endsWith("skill")) {
					return true;
				} else
					return false;
			}
		};
		File[] fileList = (new File(projectPath+"\\Skill")).listFiles(filter);
		
		// �޾ƿ� ���� ����Ʈ���� indexSkill�� �ش��ϴ� ������ �̸��� ��´�.
		// ���� �̸��� af �迭�� �����Ѵ�.
		String number;
		if (indexSkill < 10)		number = "00" + indexSkill;
		else if (indexSkill < 100)	number = "0" + indexSkill;
		else						number = "" + indexSkill;
		af[0] = number + "-";
		for (int i = 0; i < fileList.length; i++) {
			String nameFile = fileList[i].getName();
			if (nameFile.charAt(0) == '.') continue;
			int indexFile = new Integer(nameFile.substring(0, 3));
			if (indexFile == indexSkill) {
				af[0] += (nameFile.substring(3, nameFile.lastIndexOf(".")));
				break;
			}
		}
		// Ȱ��ȭ ������ af �迭�� �����Ѵ�.
		af[1] = (new Integer(data.getActivateLevel())).toString();
		list.add(af);
	}
	
	public void regist(SkillInfo data, int index) {
		// list ������ �����ϱ� ���� �ӽ� String �迭 ����
		String af[] = new String[2];
		
		// indexSkill�� �����Ѵ�.
		int indexSkill = data.getIndexSkill();
		
		// projectPath/Job ������ ���� ����Ʈ�� �޾ƿ´�.
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathName) {
				if(pathName.getName().endsWith("skill")) {
					return true;
				} else
					return false;
			}
		};
		File[] fileList = (new File(projectPath+"\\Skill")).listFiles(filter);
		
		// �޾ƿ� ���� ����Ʈ���� indexSkill�� �ش��ϴ� ������ �̸��� ��´�.
		// ���� �̸��� af �迭�� �����Ѵ�.
		String number;
		if (indexSkill < 10)		number = "00" + indexSkill;
		else if (indexSkill < 100)	number = "0" + indexSkill;
		else						number = "" + indexSkill;
		af[0] = number + "-";
		for (int i = 0; i < fileList.length; i++) {
			String nameFile = fileList[i].getName();
			if (nameFile.charAt(0) == '.') continue;
			int indexFile = new Integer(nameFile.substring(0, 3));
			if (indexFile == indexSkill) {
				af[0] += (nameFile.substring(3, nameFile.lastIndexOf(".")));
				break;
			}
		}
		// Ȱ��ȭ ������ af �迭�� �����Ѵ�.
		af[1] = (new Integer(data.getActivateLevel())).toString();
		list.add(index, af);
	}
	
	// index ��°�� �����͸� �����Ѵ�.
	public void delete(int index) {
		this.index = index;
		list.remove(this.index);
	}

	@Override
	public int getColumnCount() {
		return data.length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rows, int cols) {
		String[][] list2 = new String[rows][cols];
		list2 = (String[][]) list.toArray(list2);
		return list2[rows][cols];
	}

	public Vector<String[]> getList() {
		return list;
	}
}
