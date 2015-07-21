	package anb.ground.enums;

public enum POSITION {
	GK(0), CB(1), SB(2), MF(3), WF(4), CF(5);

	private final int value;

	POSITION(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public static POSITION valueOf(int value) {
		switch (value) {
		case 0:
			return GK;
		case 1:
			return CB;
		case 2:
			return SB;
		case 3:
			return MF;
		case 4:
			return WF;
		case 5:
			return CF;
		default:
			throw new AssertionError("Unknown value: " + value);
		}
	}
}
