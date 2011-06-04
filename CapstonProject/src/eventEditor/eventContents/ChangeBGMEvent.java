package eventEditor.eventContents;

import java.io.File;
import java.io.Serializable;

public class ChangeBGMEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = 7153307307926449426L;
	
	private String srcFileName;
	private String fileName;
	private int volumn;
	
	public ChangeBGMEvent(String srcFileName, String fileName, int volumn) {
		if(volumn > 100)	volumn = 100;
		else if(volumn < 0)	volumn = 0;
		
		this.srcFileName = srcFileName;
		this.contentType = CHANGE_BGM_EVNET;
		this.fileName = fileName;
		this.volumn = volumn;
	}

	
	public String getSrcFileName()					{	return srcFileName;				}
	public void setSrcFileName(String srcFileName) {	this.srcFileName = srcFileName;	}
	public String getFileName()						{	return fileName;				}
	public void setFileName(String fileName)		{	this.fileName = fileName;		}
	public int getVolumn()							{	return volumn;					}

	/**
	 * ������ 0~100�� ���̴�.
	 * ������ ����� ����� �Ѱ谪���� ���� �����ϰ� �����Ѵ�.
	 * 
	 * ex) setVolumn(150) -> volumn = 100
	 *     setVolumn(50)  -> volumn = 50
	 *     setVolumn(-10) -> volumn = 0
	 * */
	public void setVolumn(int volumn) {
		if(volumn > 100) {
			System.err.println("error: BGMEvent.setVolumn() (volumn: "+ volumn +")");
			volumn = 100;
		} else if(volumn < 0) {
			System.err.println("error: BGMEvent.setVolumn() (volumn: "+ volumn +")");
			volumn = 0;
		}
		
		this.volumn = volumn;
	}
	
	/**
	 * ���� ���������� �����Ǿ����� �ʴٸ� null�� ��ȯ�Ѵ�.
	 * null�� ��ȯ�ȴٸ� ��������� ������� �ʴ´�.
	 * */
	public File getBGMFile() {
		if(fileName.length() > 0)
			return new File(fileName);
		else
			return null;
	}
}
