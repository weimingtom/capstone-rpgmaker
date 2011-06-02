package execute;


public class GameDemoExecution {

	//���� ���۸����
	public static int DOUBLEBUFFERMODE = 2;
	
	private String gamePath = null;
	private static GameDemoExecution instanse = null;
	
	public static GameDemoExecution getInstanse(String gamePath) {
		instanse = new  GameDemoExecution(gamePath);
		return instanse;
	}



	//���� ������, ���÷���, ������
	private GameData gameData;
	private GameDisplay gameDisplay;
	private GameWindow gameWindow;
	private GameMusic gameMusic;
	//Ű�Է� ��ü
	private KeyFlags keyFlag;
	
	private GameDemoExecution(String gamePath)
	{
		this.gamePath = gamePath;
		//��ü ����
		gameData = new GameData();
		//���� �н� �Է�
		gameData.setGamePath(this.gamePath);
		
		//Ű���� �Է� �� �׷��� �ڿ��� ���� ������ ����, gameData ����
		gameWindow = new GameWindow(gameData, false);
		//�׷��� �ڿ� ����, ��� ���� ���� ���۸� ���
		gameWindow.createBufferStrategy(DOUBLEBUFFERMODE);
		gameData.setGameWindow(gameWindow);

		
		//���� ȭ�� ����� ��ü, gameData ����
		gameDisplay = new GameDisplay(gameData, gameWindow);
		//�׷��� �ڿ� �Է�
		gameDisplay.setHardWareBuffer(gameWindow.getBufferStrategy());
		//������ ũ�� ����
		gameDisplay.setScreenHeight(gameWindow.getHeight());
		gameDisplay.setScreenWidth(gameWindow.getWidth());
		gameDisplay.computeRatio();
		gameData.setGameDisplay(gameDisplay);
		
		//���� ������ü ����
		gameMusic = new GameMusic(gameData);
		
		
		//Ű���� �Է��� ���� ��ü
		keyFlag = new KeyFlags();
		//Ű �÷��� ����
		gameWindow.setKeyFlag(keyFlag);
		gameData.setKeyFlag(keyFlag);
		gameDisplay.setKeyFlag(keyFlag);
		
		//������ ���¸� �ʱ���·� �����Ѵ�.
		gameData.setGameState(GameData.LOGOSCREEN);
		
		//�׸��� �ʱ⿡ ���� �����Ϳ��� �̺�Ʈ �ε带 �����Ѵ�.
	}
	
	
	//�� ������ ����
	public void execute()
	{
		Thread displayThread = new Thread(this.gameDisplay);
		Thread dataThread = new Thread(this.gameData);
		Thread musicThread = new Thread(gameMusic);
		dataThread.start();
		displayThread.start();
		musicThread.start();
		
	}
	
	public void setGamePath(String gamePath) {
		this.gamePath = gamePath;
	}


	public String getGamePath() {
		return gamePath;
	}	
}
