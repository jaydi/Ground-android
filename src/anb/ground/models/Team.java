package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {
	private int id;
	private String name;
	private String imageUrl;
	private float avgBirth;
	private int score;
	private int win;
	private int draw;
	private int lose;

	public Team() {
		super();
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

	public float getAvgBirth() {
		return avgBirth;
	}

	public void setAvgBirth(float avgBirth) {
		this.avgBirth = avgBirth;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {

		@Override
		public Team createFromParcel(Parcel source) {
			return new Team(source);
		}

		@Override
		public Team[] newArray(int size) {
			return new Team[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(imageUrl);
		dest.writeFloat(avgBirth);
		dest.writeInt(score);
		dest.writeInt(win);
		dest.writeInt(draw);
		dest.writeInt(lose);
	}

	public Team(Parcel source) {
		id = source.readInt();
		;
		name = source.readString();
		imageUrl = source.readString();
		avgBirth = source.readFloat();
		score = source.readInt();
		win = source.readInt();
		draw = source.readInt();
		lose = source.readInt();
	}
}
