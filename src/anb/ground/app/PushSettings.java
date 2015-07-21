package anb.ground.app;

public class PushSettings {
	private boolean pushAllowed = true;
	private boolean pushToastAllowed = true;
	private boolean pushVibrationAllowed = true;

	public boolean isPushAllowed() {
		return pushAllowed;
	}

	public void setPushAllowed(boolean pushAllowed) {
		this.pushAllowed = pushAllowed;
	}

	public boolean isPushToastAllowed() {
		return pushToastAllowed;
	}

	public void setPushToastAllowed(boolean pushToastAllowed) {
		this.pushToastAllowed = pushToastAllowed;
	}

	public boolean isPushVibrationAllowed() {
		return pushVibrationAllowed;
	}

	public void setPushVibrationAllowed(boolean pushVibrationAllowed) {
		this.pushVibrationAllowed = pushVibrationAllowed;
	}
}
