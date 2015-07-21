package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class JoinUser extends User implements Parcelable {
	private int join;
	private boolean toPush;

	public JoinUser() {
		super();
	}

	public int getJoin() {
		return join;
	}

	public void setJoin(int join) {
		this.join = join;
	}

	public boolean isToPush() {
		return toPush;
	}

	public void setToPush(boolean toPush) {
		this.toPush = toPush;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static Parcelable.Creator<JoinUser> CREATOR = new Parcelable.Creator<JoinUser>() {

		@Override
		public JoinUser createFromParcel(Parcel source) {
			return new JoinUser(source);
		}

		@Override
		public JoinUser[] newArray(int size) {
			return new JoinUser[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(imageUrl);
		dest.writeInt(join);
	}

	public JoinUser(Parcel source) {
		id = source.readInt();
		name = source.readString();
		imageUrl = source.readString();
		join = source.readInt();
	}
}
