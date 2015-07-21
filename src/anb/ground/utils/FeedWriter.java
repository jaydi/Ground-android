package anb.ground.utils;

import anb.ground.R;
import anb.ground.models.Feed;
import anb.ground.models.FeedMessage;

public class FeedWriter {

	public static CharSequence write(int type, FeedMessage message) {
		StringBuilder sb = new StringBuilder();

		switch (type) {
		case Feed.TYPE_GUIDE:
			sb.append(StringProvider.getString(R.string.feed_message_guide));
			break;
		case Feed.TYPE_JOIN_TEAM:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_join_team), message.getMemberName(), message.getTeamName()));
			break;
		case Feed.TYPE_ACCEPT_MEMBER:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_accept_member), message.getMemberName(), message.getTeamName()));
			break;
		case Feed.TYPE_DENY_MEMBER:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_deny_member), message.getTeamName(), message.getMemberName()));
			break;
		case Feed.TYPE_DENY_TEAM:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_deny_team), message.getMemberName(), message.getTeamName()));
			break;
		case Feed.TYPE_LEAVE_TEAM:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_leave_team), message.getMemberName(), message.getTeamName()));
			break;
		case Feed.TYPE_REQUEST_MATCH:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_request_match), message.getRequestedTeamName()));
			break;
		case Feed.TYPE_INVITE_TEAM:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_invite_team), message.getRequestedTeamName()));
			break;
		case Feed.TYPE_MATCHING_COMPLETED:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_matching_completed)));
			break;
		case Feed.TYPE_DENY_MATCH:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_deny_match), message.getRequestedTeamName()));
			break;
		case Feed.TYPE_DO_SURVEY:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_do_survey)));
			break;
		case Feed.TYPE_SET_SCORE:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_set_score)));
			break;
		case Feed.TYPE_ACCEPT_SCORE:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_accept_score)));
			break;
		case Feed.TYPE_SCORE_COMPLETED:
			sb.append(String.format(StringProvider.getString(R.string.feed_message_score_completed)));
			break;
		default:
			sb.append(message);
			break;
		}

		return sb.toString();
	}

	public static int getIconType(int type) {
		switch (type) {
		case Feed.TYPE_GUIDE:
			return Feed.ICON_ALARM;
		case Feed.TYPE_JOIN_TEAM:
			return Feed.ICON_ALARM;
		case Feed.TYPE_ACCEPT_MEMBER:
			return Feed.ICON_CHECKED;
		case Feed.TYPE_DENY_MEMBER:
			return Feed.ICON_NOTICE;
		case Feed.TYPE_DENY_TEAM:
			return Feed.ICON_NOTICE;
		case Feed.TYPE_LEAVE_TEAM:
			return Feed.ICON_NOTICE;
		case Feed.TYPE_REQUEST_MATCH:
			return Feed.ICON_ALARM;
		case Feed.TYPE_INVITE_TEAM:
			return Feed.ICON_ALARM;
		case Feed.TYPE_MATCHING_COMPLETED:
			return Feed.ICON_CHECKED;
		case Feed.TYPE_DENY_MATCH:
			return Feed.ICON_NOTICE;
		case Feed.TYPE_DO_SURVEY:
			return Feed.ICON_ALARM;
		case Feed.TYPE_SET_SCORE:
			return Feed.ICON_ALARM;
		case Feed.TYPE_ACCEPT_SCORE:
			return Feed.ICON_ALARM;
		case Feed.TYPE_SCORE_COMPLETED:
			return Feed.ICON_CHECKED;
		default:
			return Feed.ICON_ALARM;
		}
	}

}
