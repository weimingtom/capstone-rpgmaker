package eventEditor.eventContents;

import java.io.Serializable;
import java.util.List;

public class SwitchDialogEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = 8879619109652726127L;
	
	private String question;
	private List<Switch> switchList;
	
	
	public SwitchDialogEvent(String question, List<Switch> switchList) {
		this.contentType = SWITCH_DIALOG_EVNET;
		this.question = question;
		this.switchList = switchList;
	}


	public String getQuestion()					{	return question;			}
	public void setQuestion(String question)	{	this.question = question;	}
	public List<Switch> getSwitchList()			{	return switchList;			}
}
