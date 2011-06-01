package eventEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import viewControl.MainFrame;


public class FlagList {
	
//	private final static String START_MARKER = ":";
//	private final static String DELIMITER = "@";
//	private final static String IS_FLAG_NAME = "<Flag Name List>";
//	private final static String FILE_FULL_PATH = MainFrame.OWNER.ProjectFullPath + File.separator + "Map" + File.separator + ".map";
//	private final static String FILE_FULL_PATH = "D:" + File.separator + ".map";
	
	public static String[] getFlagNames() {
		String FILE_FULL_PATH = MainFrame.OWNER.ProjectFullPath + File.separator + "Map" + File.separator + ".map";
		String START_MARKER = ":";
		String DELIMITER = "@";
		
		File file = new File(FILE_FULL_PATH);
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			boolean findFlagNames = false;
			while((line=br.readLine()) != null) {
				// flage name�� ��ϵ� �����̸� ������ �����Ѵ�.
				if(isFlagNameLine(line)) {
					findFlagNames = true;
					break;
				}
			}
			br.close();
			
			String[] flagNames;
			if(findFlagNames) {
				flagNames = (line.substring(line.lastIndexOf(START_MARKER)+1, line.length())).split(DELIMITER);
				String[] returnStrs = new String[1000];
				for (int i = 0; i < returnStrs.length; i++) {
					if(i < flagNames.length)	returnStrs[i] = flagNames[i];
					else						returnStrs[i] = "";
				}
				return returnStrs;
			} else {
				// ���ο� flag name�� �����.
				flagNames = new String[1000];
				for (int i = 0; i < flagNames.length; i++)
					flagNames[i] = "";
				
				// ���� flag name�� ���Ͽ� ����.
				writeFile(flagNames);
				return flagNames;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[] getIndexedFlagNames() {
		String[] flagNames = getFlagNames();
		String[] indexedFlagNames = new String[1000];
		
		for (int i = 0; i < indexedFlagNames.length; i++) {
			indexedFlagNames[i] = (i+1) + ". " + flagNames[i];
		}
		
		return indexedFlagNames;
	}
	
	public static boolean setFlagName(int index, String flagName) {
		if(!isValidName(flagName))
			return false;
		else {
			String[] names = getFlagNames();
			names[index] = flagName;
			writeFile(names);
			return true;
		}
	}
	
	public static void writeFile(String[] names) {
		String FILE_FULL_PATH = MainFrame.OWNER.ProjectFullPath + File.separator + "Map" + File.separator + ".map";
		String START_MARKER = ":";
		String DELIMITER = "@";
		String IS_FLAG_NAME = "<Flag Name List>";
		
		// ��ü�� ������ �ۼ��Ѵ�.
		StringBuffer fileContent = new StringBuffer(IS_FLAG_NAME+START_MARKER);
		for (int i = 0; i < names.length; i++) {
			fileContent.append(names[i]);
			if(i != names.length-1)	fileContent.append(DELIMITER);
		}
		
		// ���پ� �˻��ϸ鼭 StringBuffer�� �߰��Ѵ�.
		File file = new File(FILE_FULL_PATH);
		if(!(file.exists()))
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
//			boolean findFlagNames = false;
			while((line=br.readLine()) != null) {
				// flag name�� �ۼ��� �����̸� append�� ���� �ʴ´�.
				if(isFlagNameLine(line))
					continue;
				
				// fileContent�� �ڿ� line�� �߰��Ѵ�.
				fileContent.append("\n"+line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ���� �ۼ�
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(fileContent.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean isFlagNameLine(String line) {
		String START_MARKER = ":";
		String IS_FLAG_NAME = "<Flag Name List>";
		
		int lastIndex = line.lastIndexOf(START_MARKER);
		
		// flag name�� �ۼ��� �����̸� append�� ���� �ʴ´�.
		if(lastIndex != -1 && line.subSequence(0, lastIndex).equals(IS_FLAG_NAME))
			return true;
		
		return false;
	}
	
	private static boolean isValidName(String name) {
		String START_MARKER = ":";
		String DELIMITER = "@";
		
		// �Էµ� flag�� �̸��� ��ȿ���� Ȯ���Ѵ�. true�� ��ȯ�Ǹ� ��ȿ�� �̸�
		for (int i = 0; i < name.length(); i++) {
			if(name.charAt(i) == START_MARKER.charAt(0))
				return false;
			else if(name.charAt(i) == DELIMITER.charAt(0))
				return false;
		}
		
		return true;
	}
}
