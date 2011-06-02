package eventEditor.eventContents;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SwitchDialogEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = 8879619109652726127L;
	
	private String question;
	private List<Switch> switchList;
	
	
	public SwitchDialogEvent() {
		this.contentType = this.SWITCH_DIALOG_EVNET;
		this.question = "Question";
		switchList = new LinkedList<Switch>();
	}


	public String getQuestion()					{	return question;			}
	public void setQuestion(String question)	{	this.question = question;	}
	public List<Switch> getSwitchList()			{	return switchList;			}
}
