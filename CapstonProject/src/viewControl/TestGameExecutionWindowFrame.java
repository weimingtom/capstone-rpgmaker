package viewControl;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import MapEditor.Map;
import MapEditor.MapEditorSystem;
public class TestGameExecutionWindowFrame extends JFrame implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8488298207345353861L;
	//�׷��� ����̽�
	private GraphicsDevice graphicsDevice;
	//����� ȭ�� ������
	private DisplayMode origDisplayMode;
	private GraphicsEnvironment graphicsEnvironment;

	public TestGameExecutionWindowFrame() {
		// TODO Auto-generated constructor stub
		// �׷��� ����̽� ����
		// ���� �׷��� ����̽� ȯ�� ������
		this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// ����Ʈ ��ũ�� ����̽��� ����
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		// ����� ȭ�� ������ ����
		origDisplayMode = graphicsDevice.getDisplayMode();
		
		//��üȭ���� ������
		if (graphicsDevice.isFullScreenSupported()) {
			// Enter full-screen mode witn an undecorated,
			// non-resizable JFrame object.
			setUndecorated(true);
			setResizable(false);
			// Make it happen!
			graphicsDevice.setFullScreenWindow(this);
			validate();
		} else {
			System.out.println("Full-screen mode not supported");
		}// end else
		
		
		//Ű���� �Է� �̺�Ʈ ������
		addKeyListener(this);
		
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			//���� ��ũ�� ȯ������ ����
			graphicsDevice.setDisplayMode(origDisplayMode);
			System.exit(0);
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		//���� ���� ����
		MapEditorSystem mapSys = new MapEditorSystem();
		//�� �ε�
		mapSys.load("d:/a.map");
		Map ourMap = mapSys.getMapInfo();
		
		TestGameExecutionWindowFrame tmp = new TestGameExecutionWindowFrame();
		//���������� 2�� �ִ°��� ������۸��� �� ���̶�� ���� �ǹ��Ѵ�
		tmp.createBufferStrategy(2);
		
		//�������� ��ü����(���� �����Ѵٴ� �ǹ�)
		BufferStrategy strategy = tmp.getBufferStrategy();

		//������
		Graphics g = strategy.getDrawGraphics();
//		g.drawImage(ourMap.getM_Background(), 0, 0, null);
//		g.drawImage(ourMap.getM_Foreground(),0,0,null);
		g.drawImage(ourMap.getM_Background(),
				0, 0, tmp.getWidth(), tmp.getHeight(),
				0, 0, ourMap.getM_Background().getWidth(), 
				ourMap.getM_Background().getHeight(), null);
		g.drawImage(ourMap.getM_Foreground(),
				0, 0, tmp.getWidth(), tmp.getHeight(),
				0, 0, ourMap.getM_Foreground().getWidth(), 
				ourMap.getM_Foreground().getHeight(), null);
		g.dispose();
		strategy.show();
	}
}
