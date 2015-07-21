package anb.ground.protocols;

public class RegisterGroundResponse extends DefaultResponse {
	private int groundId;

	public int getGroundId() {
		return groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("registered ground id : " + groundId);
		
		return sb.toString();
	}
}
