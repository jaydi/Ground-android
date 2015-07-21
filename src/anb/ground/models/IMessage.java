package anb.ground.models;

public class IMessage {
	public static final String EXTRA_MESSAGE = "anb.ground.extra.message";

	private long id;
	private long matchId;
	private int teamId;
	private String teamName;
	private String teamImageUrl;
	private String msg;
	private boolean ack;

	public IMessage() {
		super();
	}

	public IMessage(int id, long matchId, TeamHint teamHint, String msg) {
		super();
		this.id = id;
		this.matchId = matchId;
		this.teamId = teamHint.getId();
		this.teamName = teamHint.getName();
		this.teamImageUrl = teamHint.getImageUrl();
		this.msg = msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamImageUrl() {
		return teamImageUrl;
	}

	public void setTeamImageUrl(String teamImageUrl) {
		this.teamImageUrl = teamImageUrl;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id).append(", matchId=").append(matchId).append(", teamId=").append(teamId).append(", teamName=").append(teamName)
				.append(", teamImageUrl=").append(teamImageUrl).append(", msg=").append(msg);
		return sb.toString();
	}
}
