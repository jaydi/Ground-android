package anb.ground.utils;

import static anb.ground.utils.ToastUtils.show;
import net.daum.mf.map.api.MapPOIItem;
import anb.ground.R;
import anb.ground.models.LocalUser;
import anb.ground.models.Match;
import anb.ground.models.Post;
import anb.ground.models.PushParams;
import anb.ground.models.SMatch;
import anb.ground.models.TeamInfo;
import anb.ground.models.UserInfo;

public class Validator {
	public static final int LENGTH_ENOUGH_KOR = 1;
	public static final int LENGTH_ENOUGH_PHONE = 10;

	// methods for general purposes
	public static boolean isMe(int userId) {
		return LocalUser.getInstance().getUser().getId() == userId;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}

	// methods for specific validation
	public static boolean validateSessionKey(String sessionKey) {
		if (isEmpty(sessionKey)) {
			show(R.string.alert_empty_session_key);
			return false;
		}

		return true;
	}

	public static boolean validateLogin(String email, String password) {
		if (isEmpty(email)) {
			show(R.string.alert_wrong_email);
			return false;
		}

		if (isEmpty(password)) {
			show(R.string.alert_wrong_password);
			return false;
		}

		return true;
	}

	public static boolean validateRegister(String email, String password, String confirmPassword) {
		if (isEmpty(email)) {
			show(R.string.alert_wrong_email);
			return false;
		}

		if (isEmpty(password)) {
			show(R.string.alert_wrong_password);
			return false;
		}

		if (isEmpty(confirmPassword)) {
			show(R.string.alert_wrong_confirm_password);
			return false;
		}

		if (!password.equals(confirmPassword)) {
			show(R.string.alert_wrong_confirm_password);
			return false;
		}
		
		if (!validateEmail(email)) {
			show(R.string.alert_wrong_email);
			return false;
		}
		
		if (!validatePassword(password)) {
			show(R.string.alert_short_password);
			return false;
		}

		return true;
	}

	private static boolean validateEmail(String email) {
		if (email.length() < 5)
			return false;
		
		if (email.indexOf("@") < 1)
			return false;
		
		if (email.indexOf("@") != email.lastIndexOf("@"))
			return false;
		
		if (email.lastIndexOf(".") < email.indexOf("@") + 2)
			return false;
		
		return true;
	}

	private static boolean validatePassword(String password) {
		if (password.length() < 7)
			return false;
		
		return true;
	}

	public static boolean validateRegisterTeam(TeamInfo info) {
		if (isEmpty(info.getName())) {
			show(R.string.alert_empty_team_name);
			return false;
		}

		if (info.getLatitude() == 0 || info.getLongitude() == 0 || isEmpty(info.getAddress())) {
			show(R.string.alert_empty_location);
			return false;
		}

		return true;
	}

	public static boolean validatePost(Post post) {
		if (post.getTeamId() == 0) {
			show(R.string.alert_empty_team_id);
			return false;
		}

		if (isEmpty(post.getMessage())) {
			show(R.string.alert_empty_message);
			return false;
		}

		return true;
	}

	public static boolean validateMatch(Match match) {
		if (match.getStartTime() == 0) {
			show(R.string.alert_empty_match_time);
			return false;
		}

		if (match.getEndTime() == 0) {
			show(R.string.alert_empty_match_time);
			return false;
		}

		if (match.getGroundId() == 0) {
			show(R.string.alert_empty_ground);
			return false;
		}

		return true;
	}

	public static boolean validateSearchMatch(SMatch sMatch) {
		if (sMatch.getStartTime() == 0) {
			show(R.string.alert_empty_match_time);
			return false;
		}

		if (sMatch.getEndTime() == 0) {
			show(R.string.alert_empty_match_time);
			return false;
		}

		if (sMatch.getLatitude() == 0 || sMatch.getLongitude() == 0) {
			show(R.string.alert_empty_location);
			return false;
		}

		return true;
	}

	public static boolean validateProfile(UserInfo profile) {
		if (isEmpty(profile.getName())) {
			show(R.string.alert_empty_name);
			return false;
		}

		if (isEmpty(profile.getPhoneNumber())) {
			show(R.string.alert_empty_phone);
			return false;
		}

		if (profile.getPhoneNumber().length() < LENGTH_ENOUGH_PHONE) {
			show(R.string.alert_wrong_phone);
			return false;
		}

		return true;
	}

	public static boolean validateDate(int year, int month, int day) {
		if (year == 0 && month == 0 && day == 0) {
			show(R.string.alert_empty_date);
			return false;
		}

		if (year == 0 || day == 0) {
			show(R.string.alert_empty_date);
			return false;
		}

		return true;
	}

	public static boolean validateDateRange(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		if (!validateDate(startYear, startMonth, startDay))
			return false;

		if (!validateDate(endYear, endMonth, endDay))
			return false;
		
		if (DateUtils.getStamp(startYear, startMonth, startDay, 0, 0) > DateUtils.getStamp(endYear, endMonth, endDay, 0, 0)) {
			show(R.string.alert_wrong_date_range);
			return false;
		}

		return true;
	}

	public static boolean validateTimeRange(int startHour, int startMin, int endHour, int endMin) {
		if (startHour == 0 && startMin == 0 && endHour == 0 && endMin == 0) {
			show(R.string.alert_empty_time);
			return false;
		}

		if (startHour > endHour) {
			show(R.string.alert_wrong_time_range);
			return false;
		} else if (startHour == endHour)
			if (startMin >= endMin) {
				show(R.string.alert_wrong_time_range);
				return false;
			}

		return true;
	}

	public static boolean validateRequestMatch(int homeTeamId, int awayTeamId) {
		if (homeTeamId == awayTeamId) {
			show(R.string.alert_my_team_match);
			return false;
		}

		return true;
	}

	public static boolean validateInviteTeam(int targetId, int ourId) {
		if (targetId == ourId) {
			show(R.string.alert_is_our_team);
			return false;
		}

		return true;
	}

	public static boolean validatePOI(MapPOIItem poi) {
		if (poi == null) {
			show(R.string.alert_empty_poi);
			return false;
		}

		if (poi.getMapPoint() == null) {
			show(R.string.alert_empty_poi);
			return false;
		}

		if (poi.getMapPoint().getMapPointGeoCoord().latitude == 0 || poi.getMapPoint().getMapPointGeoCoord().longitude == 0) {
			show(R.string.alert_empty_poi);
			return false;
		}

		return true;
	}

	public static boolean validatePushParams(PushParams params) {
		if (params == null)
			return false;
		
		if (isEmpty(params.getPushKey()))
			return false;
		
		return true;
	}
}
