package anb.ground.enums;

public enum OCCUPATION {
	ST(0), WK(1), PF(2), OT(3);

	private final int value;

	OCCUPATION(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public static OCCUPATION valueOf(int value) {
		switch (value) {
		case 0:
			return ST;
		case 1:
			return WK;
		case 2:
			return PF;
		case 3:
			return OT;
		default:
			throw new AssertionError("Unknown value: " + value);
		}
	}
}
