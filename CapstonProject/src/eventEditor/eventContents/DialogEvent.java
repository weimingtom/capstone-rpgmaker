package eventEditor.eventContents;

import java.io.Serializable;

public class DialogEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = -6656179943241472655L;
	private String text;
	
	
	public DialogEvent() {
		this.contentType = this.DIALOG_EVNET;
		text = "";
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
}
