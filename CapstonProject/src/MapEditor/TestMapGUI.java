package MapEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import palette.*;
import viewControl.MainFrame;

public class TestMapGUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8286032434266608970L;
	//�̰� ȭ�� ��¿�
	MapIntegrateGUI totalMap;
	//�̰� ������
	MapEditorSystem mapsys;
	//�̰� �ȷ�Ʈ
	PalettePanel backPalette;
	PalettePanel forePalette;
	
	public TestMapGUI() throws IOException
	{
		//f = new MainFrame();
		//��Ŭ�������� ����!!!!!!!
		
		//������������ ���� �� z����
		//mapsys = new MapEditorSystem();
		//������ ����
		totalMap = new MapIntegrateGUI(new MapEditorSystem());
		//�ʻ���
		totalMap.createMap("�ƽθ�", 10, 10);
		//0��° ��׶��� �ȷ�Ʈ ����
	
//		mapsys.makeBackTemplate("c:/002-Woods01.png");//�̹��� ������ �Ҵ�. ���� ��׶��� �ȷ�Ʈ�� ������ �Ҵ��
//		backPalette = new PalettePanel(mapsys, true, 0);//0��° ��׶����� �ȷ�Ʈ ����
//		backPalette.setImage(mapsys.getBackTemplate(0));//�ȷ�Ʈ�� �̹��� ����
//		totalMap.setPaletteInfo(backPalette);//�ȷ�Ʈ ���� �Է�

//		totalMap.setOutputFlag(3);
		totalMap.setForegroundPalette("c:/002-Woods01.png");
		forePalette = new PalettePanel(totalMap, false, 0);
		forePalette.setImage(totalMap.getForeroundInfo(0));
		
		guiPosition();
	}
	
	public void guiPosition()
	{
		this.setLayout(new GridLayout(1,3,15,15));
		//f.canvasPanel.setPreferredSize(new Dimension(100, 100));
		totalMap.setPreferredSize(new Dimension(100, 100));
		//f.canvasPanel.add(totalMap);
		add(totalMap);
//		add(backPalette);
		add(forePalette);
	}
	
	public static void main(String ar[]) throws IOException
	{
		TestMapGUI tmg = new  TestMapGUI();
		tmg.setSize(200, 200);
		tmg.setVisible(true);
		tmg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Menus extends JPanel
{
	
}
