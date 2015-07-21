package anb.ground.models;

public class FeedMessage {
	private int type;
	private int memberId;
	private String memberName;
	private int teamId;
	private String teamName;
	private int requestedTeamId;
	private String requestedTeamName;
	private int homeTeamId;
	private String homeTeamName;
	private int awayTeamId;
	private String awayTeamName;
	private long matchId;

	public FeedMessage() {
		super();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public int getRequestedTeamId() {
		return requestedTeamId;
	}

	public void setRequestedTeamId(int requestedTeamId) {
		this.requestedTeamId = requestedTeamId;
	}

	public String getRequestedTeamName() {
		return requestedTeamName;
	}

	public void setRequestedTeamName(String requestedTeamName) {
		this.requestedTeamName = requestedTeamName;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public static FeedMessage guideMessage() {
		FeedMessage guide = new FeedMessage();
		guide.setTeamName("GROUND");
		return guide;
	}
}
