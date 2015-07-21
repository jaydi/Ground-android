package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamHint implements Parcelable {
	public final static String EXTRA_TEAM_HINT = "anb.ground.extra.teamHint";
	public final static String EXTRA_TEAM_ID = "anb.ground.extra.teamId";
	public final static String EXTRA_TEAM_NAME = "anb.ground.extra.teamName";
	public static final String EXTRA_TEAM_IMAGE_URL = "anb.ground.extra.teamImageUrl";
	public final static String EXTRA_DENIED_TEAM_NAME = "anb.ground.extra.deniedTeamName";

	private int id;
	private String name;
	private String imageUrl;
	private boolean isManaged;

	public TeamHint() {

	}

	public TeamHint(int id, String name, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
	}
	
	public TeamHint(int id, String name, String imageUrl, boolean isManaged) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.isManaged= isManaged;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isManaged() {
		return isManaged;
	}

	public void setManaged(boolean isManaged) {
		this.isManaged = isManaged;
	}

	@Override
	public String toString() {
		return "{" + id + ", " + name + ", " + imageUrl + "}";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<TeamHint> CREATOR = new Parcelable.Creator<TeamHint>() {

		@Override
		public TeamHint createFromParcel(Parcel source) {
			return new TeamHint(source);
		}

		@Override
		public TeamHint[] newArray(int size) {
			return new TeamHint[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(imageUrl);
		dest.writeInt((isManaged)? 1 : 0);
	}

	public TeamHint(Parcel source) {
		id = source.readInt();
		name = source.readString();
		imageUrl = source.readString();
		isManaged = (source.readInt() == 1)? true : false;
	}
}
