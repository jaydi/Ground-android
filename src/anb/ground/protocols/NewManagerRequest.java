package anb.ground.protocols;

import java.util.List;

public class NewManagerRequest implements DefaultRequest {
	private static final String protocol = "change_manager";

	private List<Integer> newManagerIdList;
	private List<Integer> oldManagerIdList;
	private int teamId;

	public NewManagerRequest(List<Integer> newManagerIdList,
			List<Integer> oldManagerIdList, int teamId) {
		super();
		this.newManagerIdList = newManagerIdList;
		this.oldManagerIdList = oldManagerIdList;
		this.teamId = teamId;
	}

	public List<Integer> getNewManagerIdList() {
		return newManagerIdList;
	}

	public void setNewManagerIdList(List<Integer> newManagerIdList) {
		this.newManagerIdList = newManagerIdList;
	}

	public List<Integer> getOldManagerIdList() {
		return oldManagerIdList;
	}

	public void setOldManagerIdList(List<Integer> oldManagerIdList) {
		this.oldManagerIdList = oldManagerIdList;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
