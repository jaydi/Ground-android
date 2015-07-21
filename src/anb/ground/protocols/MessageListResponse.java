package anb.ground.protocols;

import java.util.List;

import anb.ground.models.IMessage;

public class MessageListResponse extends DefaultResponse {
	private List<IMessage> messageList;

	public List<IMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<IMessage> messageList) {
		this.messageList = messageList;
	}
}
