package execute;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


public class GameWindow extends JFrame implements KeyListener{

	/**
	 * ������ ��µ� �����츦 ������ ����. Ű�Է��� ���⼭ ����
	 */
	private static final long serialVersionUID = 1L;
	
	//�׷��� ����̽�
	private GraphicsDevice graphicsDevice;
	//����� ȭ�� ������
	private DisplayMode origDisplayMode;
	private GraphicsEnvironment graphicsEnvironment;
	//���� ���������� ����� ���ӵ�����, ������, �ɸ������� ����� ������
	private GameData gameData = null;
	
	/*Ű�� �������ٴ� ���� �ν��ϴ� �÷���*/
	private KeyFlags keyFlag;
	
	public GameWindow(GameData gameData, boolean isNotFullWindow)
	{
		// �׷��� ����̽� ����
		// ���� �׷��� ����̽� ȯ�� ������
		this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// ����Ʈ ��ũ�� ����̽��� ����
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
//		GraphicsDevice tmp[] = new GraphicsDevice[2];
//		tmp = graphicsEnvironment.getScreenDevices();
//		graphicsDevice = tmp[1];
		// ����� ȭ�� ������ ����
		//origDisplayMode = graphicsDevice.getDisplayMode();
		
		//��üȭ���� 
//		if (graphicsDevice.isFullScreenSupported()) {
//			// Enter full-screen mode witn an undecorated,
//			// non-resizable JFrame object.
//			setUndecorated(true);
//			setResizable(false);
//			// Make it happen!
//			graphicsDevice.setFullScreenWindow(this);
//			setIgnoreRepaint(true);
//
//			validate();
//		} else {
//			System.out.println("Full-screen mode not supported");
//		}// end else
		
		this.setSize(1024, 640);
		this.setVisible(true);
		
		//Ű���� �Է� �̺�Ʈ ������
		addKeyListener(this);
		this.gameData = gameData;	
	}
	

	public GameWindow(GameData gameData) {
		// TODO Auto-generated constructor stub

		// �׷��� ����̽� ����
		// ���� �׷��� ����̽� ȯ�� ������
		this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// ����Ʈ ��ũ�� ����̽��� ����
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
//		GraphicsDevice tmp[] = new GraphicsDevice[2];
//		tmp = graphicsEnvironment.getScreenDevices();
//		graphicsDevice = tmp[1];
		// ����� ȭ�� ������ ����
		origDisplayMode = graphicsDevice.getDisplayMode();
		
		//��üȭ���� 
		if (graphicsDevice.isFullScreenSupported()) {
			// Enter full-screen mode witn an undecorated,
			// non-resizable JFrame object.
			setUndecorated(true);
			setResizable(false);
			// Make it happen!
			graphicsDevice.setFullScreenWindow(this);
			setIgnoreRepaint(true);

			validate();
		} else {
			System.out.println("Full-screen mode not supported");
		}// end else
		
		//Ű���� �Է� �̺�Ʈ ������
		addKeyListener(this);
		this.gameData = gameData;
	}
	
	public void exitGame()
	{
		if(origDisplayMode != null)
			graphicsDevice.setDisplayMode(origDisplayMode);
	}
	
	public void setKeyFlag(KeyFlags keyFlag) {
		// TODO Auto-generated method stub
		this.keyFlag = keyFlag;
		
	}

	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub

		if (key.getKeyCode() == KeyEvent.VK_UP) 
		{
			keyFlag.setUp(true);
		} 
		else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keyFlag.setRight(true);
		}
		else if(key.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keyFlag.setLeft(true);
		}
		else if (key.getKeyCode() == KeyEvent.VK_DOWN) 
		{
			keyFlag.setDown(true);
		} else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
			
			keyFlag.setEnter(true);
			
		} else if (key.getKeyCode() == KeyEvent.VK_D) 
		{
			keyFlag.setAction(true);
			
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE
				|| key.getKeyCode() == KeyEvent.VK_C) 
		{
			keyFlag.setCancel(true);
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		keyFlag.initAsFalse();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
