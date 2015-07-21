package anb.ground.models;

import anb.ground.utils.DateUtils;
import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {
	public static final String EXTRA_MATCH_ID = "anb.ground.extra.matchId";
	public static final String EXTRA_MATCH = "anb.ground.extra.match";
	
	public final static int HOME_ONLY = 0;
	public final static int INVITE = 1;
	public final static int REQUEST = 2;
	public final static int MATCHING_COMPLETED = 3;
	public final static int READY_SCORE = 4;
	public final static int HOME_SCORE = 5;
	public final static int AWAY_SCORE = 6;
	public final static int SCORE_COMPLETED = 7;

	public final static int WIN = 0;
	public final static int LOSE = 1;
	public final static int DRAW = 2;
	
	public final static int JOIN_NONE = 0;
	public final static int JOIN_NO = 1;
	public final static int JOIN_YES = 2;

	private long id;
	private int status;
	private long startTime;
	private long endTime;
	private int groundId;
	private String groundName;
	private int homeTeamId;
	private int awayTeamId;
	private String homeTeamName;
	private String awayTeamName;
	private int homeScore;
	private int awayScore;
	private String description;
	private boolean open;
	private boolean askSurvey;
	private double distance;

	public Match() {
		super();
	}

	public Match(int homeTeamId, long startTime, long endTime, int groundId, int awayTeamId, String description, boolean open) {
		super();
		this.homeTeamId = homeTeamId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.groundId = groundId;
		this.awayTeamId = awayTeamId;
		this.description = description;
		this.open = open;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getGroundId() {
		return groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}

	public String getGroundName() {
		return groundName;
	}

	public void setGroundName(String groundName) {
		this.groundName = groundName;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isAskSurvey() {
		return askSurvey;
	}

	public void setAskSurvey(boolean askSurvey) {
		this.askSurvey = askSurvey;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isHome(int teamId) {
		if (teamId == homeTeamId)
			return true;
		else
			return false;
	}

	public int isWin(int teamId) {
		if (homeScore > awayScore)
			if (isHome(teamId))
				return WIN;
			else
				return LOSE;
		else if (homeScore < awayScore)
			if (isHome(teamId))
				return LOSE;
			else
				return WIN;
		else
			return DRAW;
	}

	public String getDate() {
		return DateUtils.getDateString(startTime);
	}

	public String getOpponentName(int teamId) {
		if (homeTeamId == teamId)
			return awayTeamName;
		else
			return homeTeamName;
	}

	public String getScoreString(int teamId) {
		if (isHome(teamId))
			return homeScore + " : " + awayScore;
		else
			return awayScore + " : " + homeScore;
	}

	public int getMyScore(int teamId) {
		if (homeTeamId == teamId)
			return homeScore;
		else
			return awayScore;
	}
	
	public int getOpponentScore(int teamId) {
		if (homeTeamId == teamId)
			return awayScore;
		else
			return homeScore;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {

		@Override
		public Match createFromParcel(Parcel source) {
			return new Match(source);
		}

		@Override
		public Match[] newArray(int size) {
			return new Match[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(status);
		dest.writeLong(startTime);
		dest.writeLong(endTime);
		dest.writeInt(groundId);
		dest.writeString(groundName);
		dest.writeInt(homeTeamId);
		dest.writeInt(awayTeamId);
		dest.writeString(homeTeamName);
		dest.writeString(awayTeamName);
		dest.writeInt(homeScore);
		dest.writeInt(awayScore);
		dest.writeString(description);
		dest.writeString(String.valueOf(open));
	}

	public Match(Parcel source) {
		id = source.readLong();
		status = source.readInt();
		startTime = source.readLong();
		endTime = source.readLong();
		groundId = source.readInt();
		groundName = source.readString();
		homeTeamId = source.readInt();
		awayTeamId = source.readInt();
		homeTeamName = source.readString();
		awayTeamName = source.readString();
		homeScore = source.readInt();
		awayScore = source.readInt();
		open = Boolean.valueOf(source.readString());
	}
}
