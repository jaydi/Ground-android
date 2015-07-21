package anb.ground.models;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.action.IMActivity;
import anb.ground.activity.action.team.match.MatchShowActivity;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.utils.ProtocolCodec;
import anb.ground.utils.StringProvider;

import com.fasterxml.jackson.core.type.TypeReference;

public class PushParams {
	public static final String EXTRA_PARAMS = "anb.ground.extra.pushParams";
	public static final String PUSH_SURVEY = "DO_SURVEY";
	public static final String PUSH_SURVEY_IMMIDIATE = "DO_SURVEY_RIGHT_NOW";
	public static final String PUSH_REQUEST = "REQUEST";
	public static final String PUSH_INVITE = "INVITE";
	public static final String PUSH_DENY = "DENY";
	public static final String PUSH_MATCHING_COMPLETED = "MATCHING_COMPLETED";
	public static final String PUSH_IMESSAGE = "IMESSAGE";

	private String pushKey;
	private long matchId;
	private int teamId;
	private String teamName;
	private String teamImageUrl;
	private String msg;
	private List<String> params;

	public String getPushKey() {
		return pushKey;
	}

	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamImageUrl() {
		return teamImageUrl;
	}

	public void setTeamImageUrl(String teamImageUrl) {
		this.teamImageUrl = teamImageUrl;
	}

	public String getMsg() {
		if (pushKey.equals(PUSH_IMESSAGE))
			return msg;
		else if (pushKey.equals(PUSH_SURVEY))
			return StringProvider.getString(R.string.push_survey_message);
		else if (pushKey.equals(PUSH_SURVEY_IMMIDIATE))
			return StringProvider.getString(R.string.push_survey_immidiate_message);
		else if (pushKey.equals(PUSH_REQUEST))
			return StringProvider.getString(R.string.push_request_message);
		else if (pushKey.equals(PUSH_INVITE))
			return StringProvider.getString(R.string.push_invite_message);
		else if (pushKey.equals(PUSH_DENY))
			return String.format(StringProvider.getString(R.string.push_deny_message), (params.size() > 1)? params.get(1) : " ");
		else if (pushKey.equals(PUSH_MATCHING_COMPLETED))
			return StringProvider.getString(R.string.push_matching_completed_message);
		else
			return "no message";
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(String params) {
		List<String> lists = ProtocolCodec.decode(params, new TypeReference<List<String>>() {});
		if (lists == null)
			this.params = new ArrayList<String>();
		else
			this.params = lists;
	}

	public TeamHint getTeamHint() {
		return new TeamHint(teamId, teamName, teamImageUrl, LocalUser.getInstance().managing(teamId));
	}

	public Class<?> getTargetClass() {
		if (pushKey.equals(PUSH_IMESSAGE))
			return IMActivity.class;
		else if (pushKey.equals(PUSH_DENY))
			return TeamMainActivity.class;
		else
			return MatchShowActivity.class;
	}
}
