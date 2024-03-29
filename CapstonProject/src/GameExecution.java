import execute.*;


public class GameExecution {

	//希鷺 獄遁元乞球
	public static int DOUBLEBUFFERMODE = 2;
	
	//食奄拭 拭巨斗 覗稽詮闘 虹希 旋嬢操....しせし?しせしせ
	private String gamePath = "D:\\ca";
	
	//惟績 汽戚斗, 巨什巴傾戚, 制亀酔
	private GameData gameData;
	private GameDisplay gameDisplay;
	private GameWindow gameWindow;
	private GameMusic gameMusic;
	//徹脊径 梓端
	private KeyFlags keyFlag;
	
	public GameExecution()
	{
		//梓端 持失
		gameData = new GameData();
		//惟績 鳶什 脊径
		System.getProperty("user.dir");
		gameData.setGamePath(System.getProperty("user.dir"));
		
		//徹左球 脊径 貢 益掘波 切据聖 条聖 制亀酔 持失, gameData 爽脊
		gameWindow = new GameWindow(gameData);
		//益掘波 切据 持失, 窒径 乞球澗 希鷺 獄遁元 乞球
		gameWindow.createBufferStrategy(DOUBLEBUFFERMODE);
		gameData.setGameWindow(gameWindow);

		
		//惟績 鉢檎 窒径拝 梓端, gameData 爽脊
		gameDisplay = new GameDisplay(gameData, gameWindow);
		//益掘波 切据 脊径
		gameDisplay.setHardWareBuffer(gameWindow.getBufferStrategy());
		//制亀酔 滴奄 竺舛
		gameDisplay.setScreenHeight(gameWindow.getHeight());
		gameDisplay.setScreenWidth(gameWindow.getWidth());
		gameDisplay.computeRatio();
		gameData.setGameDisplay(gameDisplay);
		
		//徹左球 脊径聖 是廃 梓端
		keyFlag = new KeyFlags();
		//徹 巴傾益 爽脊
		gameWindow.setKeyFlag(keyFlag);
		gameData.setKeyFlag(keyFlag);
		gameDisplay.setKeyFlag(keyFlag);
		
		//惟績税 雌殿研 段奄雌殿稽 竺舛廃陥.
		gameData.setGameState(GameData.LOGOSCREEN);
		gameMusic = new GameMusic(gameData);
		//gameData.setGameMusic(gameMusic);
		//益軒壱 段奄拭 惟績 汽戚斗拭辞 戚坤闘 稽球研 叔楳廃陥.
	}
	
	
	//唖 床傾球 叔楳
	public void execute()
	{
		Thread displayThread = new Thread(this.gameDisplay);
		Thread dataThread = new Thread(this.gameData);
//		Thread musicThread = new Thread(gameMusic);
		dataThread.start();
		displayThread.start();
//		musicThread.start();
		
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
