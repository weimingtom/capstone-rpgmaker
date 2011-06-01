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
	//그래픽 디바이스
	private GraphicsDevice graphicsDevice;
	//종료시 화면 복구용
	private DisplayMode origDisplayMode;
	private GraphicsEnvironment graphicsEnvironment;

	public TestGameExecutionWindowFrame() {
		// TODO Auto-generated constructor stub
		// 그래픽 디바이스 설정
		// 로컬 그래픽 디바이스 환경 가져옴
		this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// 디폴트 스크린 디바이스로 설정
		graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		// 종료시 화면 복구를 위해
		origDisplayMode = graphicsDevice.getDisplayMode();
		
		//전체화면모드 ㄱㄱ싱
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
		
		
		//키보드 입력 이벤트 리스너
		addKeyListener(this);
		
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			//이전 스크린 환경으로 복구
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
		//맵을 띄우기 위해
		MapEditorSystem mapSys = new MapEditorSystem();
		//맵 로드
		mapSys.load("d:/a.map");
		Map ourMap = mapSys.getMapInfo();
		
		TestGameExecutionWindowFrame tmp = new TestGameExecutionWindowFrame();
		//버퍼전략이 2를 넣는것은 더블버퍼링을 할 것이라는 것을 의미한다
		tmp.createBufferStrategy(2);
		
		//버퍼전략 객체설정(이제 적용한다는 의미)
		BufferStrategy strategy = tmp.getBufferStrategy();

		//적용방법
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
