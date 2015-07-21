package anb.ground.protocols;

public class NoticeListRequest implements DefaultRequest {
	public static final String protocol = "notice_list";
	private int teamId;
	private long cur;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
