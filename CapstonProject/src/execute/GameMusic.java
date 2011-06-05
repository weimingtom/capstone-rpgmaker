package execute;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import javazoom.jl.decoder.InputStreamSource;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class GameMusic implements Runnable{
	
	private String filePathAndName=null;
	//private AdvancedPlayer adPlayer;
	private Player player = null;
	private long musicLength = 0;
	private long nowLength = 0;
	private BufferedInputStream bis = null;
	private FileInputStream fis = null;
	private boolean isMusicPlay = false;
	private GameData gameData;
	
	public GameMusic(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	
	
	@Override
	public void run() {
		while(gameData.getGameState() != GameData.EXIT)
		{
			while(filePathAndName != null)
			{
				try {
					if(filePathAndName == null)
					{
						player.close();
						break;
					}
					if(player != null)
						player.play();
					Thread.sleep(GameData.SLOWTIMER);
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			try {
				Thread.sleep(GameData.SLOWTIMER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(player != null)
				player.close();
		}
	}


	public void startMusic(String filePathAndName) {

		this.filePathAndName = filePathAndName;
		if(filePathAndName == null)
			return;

		//파일 스트림 생성
		try {
			fis = new FileInputStream(filePathAndName);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (JavaLayerException e) {

			System.out.println("자바 음악 출력 오류");
			e.printStackTrace();
		}
		isMusicPlay = true;
	}



	public String getFilePathAndName() {
		return filePathAndName;
	}
	
	
	
}
