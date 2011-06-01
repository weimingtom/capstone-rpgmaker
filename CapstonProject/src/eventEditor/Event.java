package eventEditor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import eventEditor.eventContents.EventContent;
import eventEditor.exceptions.NotExistType;

public class Event implements Serializable {

	private static final long serialVersionUID = 911158537540611648L;
	
	private final int lengthConditionArray;
	private int startType;
	private int actionType;
	
	private int[] preconditionFlagArray;
//	private int[] postconditionFlagArray;
	private List<EventContent> eventContentList;
	
	
	public Event() {
		this.startType = EventEditorSystem.PRESS_BUTTON;
		this.actionType = EventEditorSystem.NOT_MOTION;
		
		lengthConditionArray = 3;
		preconditionFlagArray = new int[lengthConditionArray];
		for (int i = 0; i < lengthConditionArray; i++) {
			preconditionFlagArray[i] = -1;
//			postconditionFlagArray[i] = -1;
		}
		eventContentList = new LinkedList<EventContent>();
	}
	


	public int getStartType()						{	return startType;						}
	public int getActionType()						{	return actionType;						}
	public int[] getPreconditionFlagArray() 		{	return preconditionFlagArray;			}
	public int getPreconditionFlag(int index) 		{	return preconditionFlagArray[index];	}
//	public int[] getPostconditionFlagArray()		{	return postconditionFlagArray;			}
//	public int getPostconditionFlag(int index)		{	return postconditionFlagArray[index];	}
	public List<EventContent> getEventContentList()	{	return eventContentList;				}
	public EventContent getEventContent(int index)	{	return eventContentList.get(index);		}
	
	/*
	 * -1�� ��Ȱ��ȭ ����
	 */
	public void setPreconditionFlag(int index, int flagIndex) {
		if(index > 1000 || index < -1) {
			System.err.println("error: Event.setPreconditionFlag() (index: " + index + ")");
		}
		preconditionFlagArray[index] = flagIndex;
	}
//	public void setPostconditionFlag(int index, int flagIndex) {
//		if(index > 1000 || index < -1) {
//			System.err.println("error: Event.setPostconditionFlag() (index: " + index + ")");
//		}
//		postconditionFlagArray[index] = flagIndex;
//	}



	/**
	 * �Ķ���ͷ� ������ EventEditorSystem Ÿ���� ������ ����
	 * - PRESS_BUTTON
	 * - CONTACT_WITH_PLAYER
	 * - ABOVE_EVENT_TILE
	 * - AUTO_START
	 * - PARALLEL_START
	 * */
	public void setStartType(int startType) throws NotExistType {
		if(startType<EventEditorSystem.PRESS_BUTTON || startType>EventEditorSystem.PARALLEL_START)
			throw new NotExistType("error: Events.setStartType() (startType: "+ startType +")");
		this.startType = startType;
	}
	
	/**
	 * �Ķ���ͷ� ������ EventEditorSystem Ÿ���� ������ ����
	 * - NOT_MOTION
	 * - RANDOM_MOTION
	 * - COME_CLOSER_TO_PLAYER
	 * - ATTACK_PLAYER
	 * - ATTACK_ENEMY
	 * */
	public void setActionType(int actionType) throws NotExistType {
		if(startType<EventEditorSystem.NOT_MOTION || startType>EventEditorSystem.ATTACK_ENEMY)
			throw new NotExistType("error: Events.setStartType() (actionType: "+ actionType +")");
		this.actionType = actionType;
	}
}
