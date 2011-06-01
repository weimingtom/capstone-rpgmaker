package viewControl.editorDlg;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import viewControl.MainFrame;

public abstract class EditorDlg extends JDialog {

	private static final long serialVersionUID = 1L;
	
	protected MainFrame owner;
	protected String projectPath;
	protected String result;
	
	public EditorDlg(MainFrame parent, String dlgName) {
		super(parent, dlgName, true);
		owner = parent;
		if(parent != null) projectPath = parent.ProjectFullPath;
		result = null;
	}
	
	public JComboBox setComboBoxList(String objectName, int maxIndex) {
		int maxLength = maxIndex;
		String[] titles = null;
		JComboBox cb_returnList = new JComboBox();
		
		// object ����Ʈ�� ������ ���� �̸����κ��� ���´�.
		File folder = new File(projectPath+"\\"+objectName);
		if (folder.exists()) {
			File[] fileList = folder.listFiles();
			
			// .(object) ���Ϸκ��� ��� ������ �����ϴ��� �о�´�.
			FileReader infoFile = null;
			try {
				infoFile = new FileReader(projectPath+"\\"+objectName+"\\."+objectName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BufferedReader inStrReader = new BufferedReader(infoFile);
		
			// ���ڷ� �Ѱܿ� ���� .(object) ���Ͽ��� ���� �� �� ū ���� maxLength�� �����Ѵ�.
			try {
				String number = null;
				if ((number = inStrReader.readLine()) != null)
					maxLength = Integer.parseInt(number);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// jobTitles�� maxLength���� default ������ �����Ѵ�.
			titles = new String[maxLength];
			for (int i = 0; i < titles.length; i++) {
				if (i < 10)			titles[i] = "00" + i + "-";
				else if (i < 100)	titles[i] = "0" + i + "-";
				else				titles[i] = "" + i + "-";
			}
			
			// object �̸� �迭�� ���Ϸ� �����ϴ� object�� �����Ѵ�.
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].getName().charAt(0) == '.')	continue;
				if (fileList[i].getName().charAt(0) >= '0' && fileList[i].getName().charAt(0) <= '9') {
					Integer fileNumber = new Integer(fileList[i].getName().substring(0, 3));
					titles[fileNumber] = fileList[i].getName().substring(0, fileList[i].getName().lastIndexOf("."));
				}
			}
		} else {
			
			// jobTitles�� maxLength���� default ������ �����Ѵ�.
			titles = new String[maxLength];
			for (int i = 0; i < titles.length; i++) {
				if (i < 10)			titles[i] = "00" + i + "-";
				else if (i < 100)	titles[i] = "0" + i + "-";
				else				titles[i] = "" + i + "-";
			}
		}
		
		// object �̸� �迭�� cb_returnList�� �����Ѵ�.
		for (int i = 0; i < titles.length; i++) {
			cb_returnList.addItem(titles[i]);
		}
		
		return cb_returnList;
	}
	
	public void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align, int fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = 100.0;
		gbc.weighty = 100.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = align;
		gbc.fill = fill;
		p.add(c, gbc);
	}
	
	public void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = 100.0;
		gbc.weighty = 100.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = align;
		gbc.fill = GridBagConstraints.NONE;
		p.add(c, gbc);
	}
	
	// str �� ������ ǥ���ϴ��� Ȯ���Ͽ� �����̸� true�� ��ȯ.
	protected boolean isIntegerString(String str) {
		int startIndex = 0;
		
		if (str.charAt(0) == '-') {	// ����
			startIndex = 1;
		} else if (str.charAt(0) < '0' || str.charAt(0) > '9') {// ù ���ڰ� ���ڰ� �ƴ� ���
			return false;
		}
		
		for (; startIndex < str.length(); startIndex++) {
			if (str.charAt(startIndex) < '0' || str.charAt(startIndex) > '9')
				return false;
		}
		
		return true;
	}

	public String getResult() {
		return result;
	}
}
