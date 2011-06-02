package bootstrap;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import viewControl.MainFrame;

public class Bootstrap {

	public static BootstrapInfo getBootstrap(String projectFullPath) {
		String FILE_FULL_PATH = projectFullPath + File.separator + "Map" + File.separator + ".map";
		
		File file = new File(FILE_FULL_PATH);
		String line = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			boolean findBootstrap = false;
			while((line=br.readLine()) != null) {
				// bootstrap�� ��ϵ� �����̸� ������ �����Ѵ�.
				if(isBootstrapLine(line)) {
					findBootstrap = true;
					break;
				}
			}
			br.close();
			
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
		if(line.lastIndexOf(START_MARKER) != -1)
			line = line.substring(line.lastIndexOf(START_MARKER)+1, line.length());
		
		return line.split(DELIMITER);
	}
}
