package eventEditor.eventContents;

import java.io.Serializable;

public class ChangeActorEvent extends EventContent implements Serializable  {

	private static final long serialVersionUID = -4581852909900142557L;
	

	// actorType
	public final static int SELF = 30;					// �̺�Ʈ�� �ڱ� �ڽ����� �����ȴ�.
	public final static int PLAYER = 31;				// �̺�Ʈ�� Player�� �����ȴ�.
	public final static int NPC = 32;					// �̺�Ʈ�� NPC�� �����ȴ�.
	public final static int MONSTER = 33;				// �̺�Ʈ�� Monster�� �����ȴ�.
	
	private int actorType;
	private int actorIndex;
	
	
	/**
	 * actorType �� �Է� ������ ����
	 * - SELF
	 * - PLAYER
	 * - NPC
	 * - MONSTER
	 *  */
	public ChangeActorEvent(int actorType, int actorIndex) {
		this.contentType = CHANGE_ACTOR_EVENT;
		if(actorType != SELF || actorType != PLAYER) {
			System.err.println("error: ActorChangeEvent() (actorType: "+ actorType +")");
			actorType = 0;
		}
		
		this.contentType = CHANGE_ACTOR_EVENT;
		this.actorType = actorType;
		this.actorIndex = actorIndex;
	}
	
	
	public int getActorType()					{	return actorType;				}
	public void setActorType(int actorType)		{	this.actorType = actorType;		}
	public int getActorIndex()					{	return actorIndex;				}
	public void setActorIndex(int actorIndex)	{	this.actorIndex = actorIndex;	}
}
