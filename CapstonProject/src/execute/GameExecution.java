package execute;


public class GameExecution {

	//���� ���۸����
	public static int DOUBLEBUFFERMODE = 2;
	
	private String gamePath = "D:/GameDataFiles";
	
	//���� ������, ���÷���, ������
	private GameData gameData;
	private GameDisplay gameDisplay;
	private GameWindow gameWindow;
	private GameMusic gameMusic;
	//Ű�Է� ��ü
	private KeyFlags keyFlag;
	
	public GameExecution()
	{
		//��ü ����
		gameData = new GameData();
		//���� �н� �Է�
		gameData.setGamePath(this.gamePath);
		
		//Ű���� �Է� �� �׷��� �ڿ��� ���� ������ ����, gameData ����
		gameWindow = new GameWindow(gameData);
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
	
	
	
	public static void main(String args[])
	{
		GameExecution game = new GameExecution();
		game.execute();
	}
}
