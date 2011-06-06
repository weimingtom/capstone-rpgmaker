package bootstrap;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import viewControl.MainFrame;
import viewControl.editorDlg.NewCharacterDlg;
import characterEditor.CharacterEditorSystem;

public class Bootstrap {

	public static BootstrapInfo getBootstrap(String projectFullPath) {
		String FILE_FULL_PATH = projectFullPath + File.separator + "Map" + File.separator + ".map";
		String DELIMITER = "@";
		
		File file = new File(FILE_FULL_PATH);
		String line = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			boolean findBootStrapInfo = false;
			while((line=br.readLine()) != null) {
				// bootstrap�� ��ϵ� �����̸� ������ �����Ѵ�.
				if(isBootstrapLine(line)) {
					findBootStrapInfo = true;
					break;
				}
			}
			br.close();

			// Bootstrap ������ ã�� ���ߴٸ� ���� ����Ʈ�� �����Ѵ�.
			if(!findBootStrapInfo) {
				// �� ���� ����
				File[] mapFiles = (new File(projectFullPath + File.separator + "Map")).listFiles();
				String defaultMapName = "null";
				if(mapFiles.length >= 2)
					defaultMapName = mapFiles[1].getName();
				
				// ĳ���� �ε��� ���� ����
				File[] charFiles = (new File(projectFullPath + File.separator + "Character")).listFiles();
				int charIndex = 0;
				if(charFiles.length >= 2)
					charIndex = new Integer(charFiles[1].getName().substring(0, 3));
				else {
					charIndex = 0;
					JOptionPane.showMessageDialog(null, "You must create Character of Index 000!");
//					new NewCharacterDlg(MainFrame.OWNER, true, null);
				}
				
				// ����Ʈ�� �ۼ��Ѵ�.
				Bootstrap.writeBootstrapInfo(defaultMapName, new Point(0,0), charIndex);
				
				line = defaultMapName+DELIMITER+"0"+DELIMITER+"0"+DELIMITER+charIndex;
			}
			
			String[] strBootstrap = parseString(line);
			int x = (new Integer(strBootstrap[1])).intValue();
			int y = (new Integer(strBootstrap[2])).intValue();
			int charIndex = (new Integer(strBootstrap[3])).intValue();
			
			return new BootstrapInfo(strBootstrap[0], new Point(x,y), charIndex);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void writeBootstrapInfo(String mapName, Point startPoint, int charIndex) {
		String FILE_FULL_PATH = MainFrame.OWNER.ProjectFullPath + File.separator + "Map" + File.separator + ".map";
		String START_MARKER = ":";
		String DELIMITER = "@";
		String IS_BOOTSTRAP = "<Bootstrap>";
		
		Integer x = new Integer(startPoint.x);
		Integer y = new Integer(startPoint.y);
		Integer index = new Integer(charIndex);
		String[] data = {mapName, x.toString(), y.toString(), index.toString()};
		
		// ��ü�� ������ �ۼ��Ѵ�.
		StringBuffer fileContent = new StringBuffer(IS_BOOTSTRAP+START_MARKER);
		for (int i = 0; i < data.length; i++) {
			fileContent.append(data[i]);
			if(i != data.length-1)	fileContent.append(DELIMITER);
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
			while((line=br.readLine()) != null) {
				// flag name�� �ۼ��� �����̸� append�� ���� �ʴ´�.
				if(isBootstrapLine(line))
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
	
	private static boolean isBootstrapLine(String line) {
		String START_MARKER = ":";
		String IS_BOOTSTRAP = "<Bootstrap>";
		
		int lastIndex = line.lastIndexOf(START_MARKER);
		
		// flag name�� �ۼ��� �����̸� append�� ���� �ʴ´�.
		if(lastIndex != -1 && line.subSequence(0, lastIndex).equals(IS_BOOTSTRAP))
			return true;
		
		return false;
	}
	
	private static String[] parseString(String line) {
		String START_MARKER = ":";
		String DELIMITER = "@";
		
		// ':'������ ���ڿ��� �����Ѵ�. 
		if(line != null && line.lastIndexOf(START_MARKER) != -1)
			line = line.substring(line.lastIndexOf(START_MARKER)+1, line.length());
		
		return line.split(DELIMITER);
	}
}
