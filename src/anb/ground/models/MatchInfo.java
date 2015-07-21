package anb.ground.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MatchInfo {
	private long id;
	private int homeTeamId;
	private int awayTeamId;
	private String homeTeamName;
	private String awayTeamName;
	private String homeImageUrl;
	private String awayImageUrl;
	private int homeScore;
	private int awayScore;
	private int homeJoinedMembersCount;
	private int awayJoinedMembersCount;
	private int status;
	private long startTime;
	private long endTime;
	private Ground ground;
	private int join;
	private String description;
	private boolean open;
	private boolean askSurvey;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getHomeImageUrl() {
		return homeImageUrl;
	}

	public void setHomeImageUrl(String homeImageUrl) {
		this.homeImageUrl = homeImageUrl;
	}

	public String getAwayImageUrl() {
		return awayImageUrl;
	}

	public void setAwayImageUrl(String awayImageUrl) {
		this.awayImageUrl = awayImageUrl;
	}

	public int getHomeJoinedMembersCount() {
		return homeJoinedMembersCount;
	}

	public void setHomeJoinedMembersCount(int homeJoinedMembersCount) {
		this.homeJoinedMembersCount = homeJoinedMembersCount;
	}

	public int getAwayJoinedMembersCount() {
		return awayJoinedMembersCount;
	}

	public void setAwayJoinedMembersCount(int awayJoinedMembersCount) {
		this.awayJoinedMembersCount = awayJoinedMembersCount;
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

	public Ground getGround() {
		return ground;
	}

	public void setGround(Ground ground) {
		this.ground = ground;
	}

	public int getJoin() {
		return join;
	}

	public void setJoin(int join) {
		this.join = join;
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

	public boolean isHome(int teamId) {
		if (teamId == homeTeamId)
			return true;
		else
			return false;
	}

	public String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = new Date(startTime);

		return format.format(date);
	}

	public int getOpponent(int teamId) {
		if (homeTeamId == teamId)
			return awayTeamId;
		else
			return homeTeamId;
	}public int isWin(int teamId) {
		if (homeScore > awayScore)
			if (isHome(teamId))
				return Match.WIN;
			else
				return Match.LOSE;
		else if (homeScore < awayScore)
			if (isHome(teamId))
				return Match.LOSE;
			else
				return Match.WIN;
		else
			return Match.DRAW;
	}
}
