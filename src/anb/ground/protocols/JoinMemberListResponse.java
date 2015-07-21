package anb.ground.protocols;

import java.util.List;

import anb.ground.models.JoinUser;

public class JoinMemberListResponse extends DefaultResponse {
	private List<JoinUser> userList;

	public List<JoinUser> getUserList() {
		return userList;
	}

	public void setUserList(List<JoinUser> userList) {
		this.userList = userList;
	}

}
