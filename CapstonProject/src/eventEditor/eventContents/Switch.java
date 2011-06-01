package eventEditor.eventContents;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Switch implements Serializable {

	private static final long serialVersionUID = 6148631716848980262L;
	
	
	private String answer;
	private List<EventContent> eventContentList;
	
	
	public Switch() {
		this.answer = "Answer";
		eventContentList = new LinkedList<EventContent>();
	}

	
	public String getAnswer()						{	return answer;			}
	public void setAnswer(String switchStr)			{	this.answer = switchStr;	}
	public List<EventContent> getEventContentList()	{	return eventContentList;	}
}
