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
	
	private String filePathAndName;
	//private AdvancedPlayer adPlayer;
	private Player player;
	private InputStreamSource input;
	private long musicLength = 0;
	private long nowLength = 0;
	private BufferedInputStream bis = null;
	private FileInputStream fis = null;
	private GameData gameData;
	private boolean isMusicPlay;
	
	public GameMusic(GameData gameData)
	{
		this.gameData = gameData;
		player = null;
		input = null;
	}

	//���� �ҷ�����
	public void setFilePathAndName(String filePathAndName) {
		close();
		this.filePathAndName = filePathAndName;
		if (filePathAndName != null) {
			try {
				fis = new FileInputStream(filePathAndName);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
				input = new InputStreamSource(bis);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "������ �����!");
				System.exit(0);
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "��¿� ������ �־��");
				System.exit(0);
			}
		}
	
	}

	public String getFilePathAndName() {
		return filePathAndName;
	}

	//�÷���
	public void play()
	{
		try {
			player.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//����
	public void close() 
	{
		if (player != null) player.close(); player=null;
		try {
			if(bis!=null)bis.close();
			if(fis!=null)fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//���� ��� ����
	public void setNowLength(long nowLength) {
		this.nowLength = nowLength;
	}

	public long getNowLength() {
		return player.getPosition();
	
	}


	public long getMusicLength() {
		return musicLength;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(gameData.getGameState() != GameData.EXIT)
		{
			if(gameData.getMusicFile()!=null)
			{
				try
				{
					playMusic(gameData.getMusicFile());
				}
				catch(NullPointerException e)
				{
					break;
				}
			}
			else
			{
				close();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.close();
	}
	public void loadMusicFile(String fileName)
	{
		setFilePathAndName(fileName);
	}
	public void playMusic(String fileName)
	{
		//���� ����
		if(isMusicPlay == false)
		{
			this.setFilePathAndName(fileName);
			play();
			isMusicPlay = true;
		}
		if(getNowLength() == getMusicLength())
		{
			isMusicPlay = false;
			close();
			loadMusicFile(fileName);
		}		
	}
	
}
