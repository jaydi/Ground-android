package anb.ground.protocols;

import anb.ground.models.UserInfo;

public class UserInfoResponse extends DefaultResponse {
	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
}
