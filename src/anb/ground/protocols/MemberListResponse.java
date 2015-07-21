package anb.ground.protocols;

import java.util.List;

import anb.ground.models.User;

public class MemberListResponse extends DefaultResponse {
	private List<User> userList;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}
