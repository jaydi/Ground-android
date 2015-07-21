package anb.ground.utils;

import java.util.ArrayList;
import java.util.List;

import anb.ground.models.JoinUser;
import anb.ground.models.User;

public class UserListUtils {
	public static List<JoinUser> getJoinMemberList(List<JoinUser> memberList, int join) {
		List<JoinUser> list = new ArrayList<JoinUser>();
		
		for(JoinUser member : memberList) {
			if(member.getJoin() == join)
				list.add(member);
		}
		
		return list;
	}
	
	public static List<User> getManagerList(List<User> userList, boolean includeAdder) {
		List<User> list = new ArrayList<User>();
		
		for(User user : userList)
			if(user.isManager())
				list.add(user);

		if (includeAdder)
			list.add(User.newInstance(User.MEMBER_ID_NEW_MANAGER, "", ""));
		
		return list;
	}
	
	public static List<User> getNormalMemberList(List<User> userList, boolean includeAdder) {
		List<User> list = new ArrayList<User>();

		for (User user : userList)
			list.add(user);
		
		if (includeAdder)
			list.add(User.newInstance(User.MEMBER_ID_INVITE_KAKAO, "", ""));
		
		return list;
	}
}
