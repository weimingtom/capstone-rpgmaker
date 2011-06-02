package bootstrap;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import viewControl.MainFrame;

public class Bootstrap {

	public BootstrapInfo getBootstrap() {
		String FILE_FULL_PATH = MainFrame.OWNER.ProjectFullPath + File.separator + "Map" + File.separator + ".map";
		String START_MARKER = ":";
		String DELIMITER = "@";
		
		File file = new File(FILE_FULL_PATH);
		String line = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			boolean findBootstrap = false;
			while((line=br.readLine()) != null) {
				// bootstrap이 기록된 라인이면 루프를 종료한다.
				if(isBootstrapLine(line)) {
					findBootstrap = true;
					break;
				}
			}
			br.close();
			
			String[] strBootstrap = parseString(line);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void writeBootstrapInfo(String mapName, Point startPoint) {
		
	}
	
	private static boolean isBootstrapLine(String line) {
		String START_MARKER = ":";
		String IS_BOOTSTRAP = "<Bootstrap>";
		
		int lastIndex = line.lastIndexOf(START_MARKER);
		
		// flag name이 작성된 라인이면 append를 하지 않는다.
		if(lastIndex != -1 && line.subSequence(0, lastIndex).equals(IS_BOOTSTRAP))
			return true;
		
		return false;
	}
	
	private static boolean isValidName(String name) {
		String START_MARKER = ":";
		String DELIMITER = "@";
		
		// 입력된 flag의 이름이 유효한지 확인한다. true가 반환되면 유효한 이름
		for (int i = 0; i <  name.length();i++) {
			if(name.charAt(i) == START_MARKER.charAt(0))
				return false;
			else if(name.charAt(i) == DELIMITER.charAt(0))
				return false;
		}
		
		return true;
	}
	
	private String[] parseString(String line) {
		return null;
	}
}
