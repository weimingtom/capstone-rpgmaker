package eventEditor.eventContents;

import java.io.Serializable;

public class ChangeFlagEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = -5164341198757453241L;
	
	private int indexFlag;	// ������ flag�� �ε���
	private boolean state;	// flag�� ��� ��ȯ�� �� ����
	
	public ChangeFlagEvent(int indexFlag, boolean state) {
		this.contentType = CHANGE_FLAG_EVENT;
		this.indexFlag = indexFlag;
		this.state = state;
	}
	
//	/**
//	 * flag list�� ���ڷ� ������ ���� ��ü�� ������ �ִ� �����͸� ���� ���ϴ� �ε����� flag�� �����Ѵ�.
//	 * @param fl : flag list
//	 */
//	public void changeFlag(FlagList fl) {
//		fl.getFlagList().get(indexFlag).setFlag(state);
//	}

	public int getIndexFlag() {	return indexFlag;	}
	public void setIndexFlag(int indexFlag) {	this.indexFlag = indexFlag;	}
	public boolean isState() {	return state;	}
	public void setState(boolean state) {	this.state = state;	}
}
