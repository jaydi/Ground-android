package anb.ground.protocols;

public class RegisterTeamResponse extends DefaultResponse {
	private int teamId;

	public RegisterTeamResponse() {
		super();
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("new team registerd! team id - > " + teamId);
		
		return sb.toString();
	}
}
