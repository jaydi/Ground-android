package anb.ground.server;

import java.util.List;

import anb.ground.models.Ground;
import anb.ground.models.Match;
import anb.ground.models.Post;
import anb.ground.models.SMatch;
import anb.ground.models.TeamInfo;
import anb.ground.models.UserInfo;
import anb.ground.protocols.AcceptMatchRequest;
import anb.ground.protocols.AcceptMemberRequest;
import anb.ground.protocols.AcceptRecordRequest;
import anb.ground.protocols.AcceptTeamRequest;
import anb.ground.protocols.CancelMatchRequest;
import anb.ground.protocols.CommentListRequest;
import anb.ground.protocols.CommentListResponse;
import anb.ground.protocols.CreateMatchRequest;
import anb.ground.protocols.CreateMatchResponse;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.DownloadImageRequest;
import anb.ground.protocols.DownloadImageResponse;
import anb.ground.protocols.EditProfileRequest;
import anb.ground.protocols.EditProfileResponse;
import anb.ground.protocols.EditTeamProfileRequest;
import anb.ground.protocols.EditTeamProfileResponse;
import anb.ground.protocols.FacebookLoginRequest;
import anb.ground.protocols.FacebookLoginResponse;
import anb.ground.protocols.FeedListRequest;
import anb.ground.protocols.FeedListResponse;
import anb.ground.protocols.InviteTeamRequest;
import anb.ground.protocols.InviteTeamResponse;
import anb.ground.protocols.JoinMatchRequest;
import anb.ground.protocols.JoinMemberListRequest;
import anb.ground.protocols.JoinMemberListResponse;
import anb.ground.protocols.JoinTeamRequest;
import anb.ground.protocols.JoinTeamResponse;
import anb.ground.protocols.LoginRequest;
import anb.ground.protocols.LoginResponse;
import anb.ground.protocols.LogoutRequest;
import anb.ground.protocols.MatchActionResponse;
import anb.ground.protocols.MatchHistoryRequest;
import anb.ground.protocols.MatchHistoryResponse;
import anb.ground.protocols.MatchInfoRequest;
import anb.ground.protocols.MatchInfoResponse;
import anb.ground.protocols.MatchListRequest;
import anb.ground.protocols.MatchListResponse;
import anb.ground.protocols.MemberActionResponse;
import anb.ground.protocols.MemberListRequest;
import anb.ground.protocols.MemberListResponse;
import anb.ground.protocols.MessageListRequest;
import anb.ground.protocols.MessageListResponse;
import anb.ground.protocols.NewManagerRequest;
import anb.ground.protocols.NewManagerResponse;
import anb.ground.protocols.NewPswdRequest;
import anb.ground.protocols.NewPswdResponse;
import anb.ground.protocols.PostListRequest;
import anb.ground.protocols.PostListResponse;
import anb.ground.protocols.PushSurveyRequest;
import anb.ground.protocols.PushSurveyResponse;
import anb.ground.protocols.PushTargettedSurveyRequest;
import anb.ground.protocols.PushTargettedSurveyResponse;
import anb.ground.protocols.RegisterGroundRequest;
import anb.ground.protocols.RegisterGroundResponse;
import anb.ground.protocols.RegisterRequest;
import anb.ground.protocols.RegisterResponse;
import anb.ground.protocols.RegisterTeamRequest;
import anb.ground.protocols.RegisterTeamResponse;
import anb.ground.protocols.RejectMatchRequest;
import anb.ground.protocols.RejectMemberRequest;
import anb.ground.protocols.RemoveCommentRequest;
import anb.ground.protocols.RemoveCommentResponse;
import anb.ground.protocols.RemoveFeedRequest;
import anb.ground.protocols.RemovePostRequest;
import anb.ground.protocols.RemovePostResponse;
import anb.ground.protocols.RequestMatchRequest;
import anb.ground.protocols.RequestMatchResponse;
import anb.ground.protocols.RetireTeamRequest;
import anb.ground.protocols.RetireTeamResponse;
import anb.ground.protocols.SearchGroundRequest;
import anb.ground.protocols.SearchGroundResponse;
import anb.ground.protocols.SearchMatchRequest;
import anb.ground.protocols.SearchMatchResponse;
import anb.ground.protocols.SearchTeamNearbyRequest;
import anb.ground.protocols.SearchTeamNearbyResponse;
import anb.ground.protocols.SearchTeamRequest;
import anb.ground.protocols.SearchTeamResponse;
import anb.ground.protocols.SendRecordRequest;
import anb.ground.protocols.StartSurveyRequest;
import anb.ground.protocols.TeamInfoRequest;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.protocols.TeamListRequest;
import anb.ground.protocols.TeamListResponse;
import anb.ground.protocols.UploadImageResponse;
import anb.ground.protocols.UserInfoRequest;
import anb.ground.protocols.UserInfoResponse;
import anb.ground.protocols.ValidateEmailRequest;
import anb.ground.protocols.ValidateEmailResponse;
import anb.ground.protocols.WannabeListRequest;
import anb.ground.protocols.WriteCommentRequest;
import anb.ground.protocols.WriteCommentResponse;
import anb.ground.protocols.WritePostRequest;
import anb.ground.protocols.WritePostResponse;
import android.os.Handler;

public class GroundClient {
	public static void validateEmail(Handler handler, String email) {
		HttpConnection.execute(handler, new ValidateEmailRequest(email), ValidateEmailResponse.class);
	}

	public static void register(Handler handler, String email, String password, String pushToken, int os, int appVer, String uuid) {
		HttpConnection.execute(handler, new RegisterRequest(email, password, pushToken, os, appVer, uuid), RegisterResponse.class);
	}

	public static void login(Handler handler, String email, String password, String pushToken, int os, int appVer, String uuid) {
		HttpConnection.execute(handler, new LoginRequest(email, password, pushToken, os, appVer, uuid), LoginResponse.class);
	}

	public static void logout(Handler handler, String deviceUuid) {
		HttpConnection.execute(handler, new LogoutRequest(deviceUuid), DefaultResponse.class);
	}

	public static void uploadFile(Handler handler, String imagePath, boolean thumbnail) {
		HttpConnectionMultipart.execute(handler, imagePath, thumbnail, UploadImageResponse.class);
	}

	public static void downloadBinary(Handler handler, String imagePath, int reqWidth, int reqHeight, boolean thumbnail) {
		HttpConnectionBinary.execute(handler, new DownloadImageRequest(imagePath, thumbnail), DownloadImageResponse.class, reqWidth, reqHeight);
	}

	public static void editProfile(Handler handler, UserInfo profile) {
		HttpConnection.execute(handler, new EditProfileRequest(profile), EditProfileResponse.class);
	}

	public static void sendNewPswd(Handler handler, String email) {
		HttpConnection.execute(handler, new NewPswdRequest(email), NewPswdResponse.class);
	}

	public static void registerTeam(Handler handler, TeamInfo info) {
		HttpConnection.execute(handler, new RegisterTeamRequest(info), RegisterTeamResponse.class);
	}

	public static void searchTeam(Handler handler, String name) {
		HttpConnection.execute(handler, new SearchTeamRequest(name), SearchTeamResponse.class);
	}

	public static void getTeamList(Handler handler) {
		HttpConnection.execute(handler, new TeamListRequest(), TeamListResponse.class);
	}

	public static void writePost(Handler handler, Post post) {
		HttpConnection.execute(handler, new WritePostRequest(post), WritePostResponse.class);
	}

	public static void removePost(Handler handler, int teamId, long id) {
		HttpConnection.execute(handler, new RemovePostRequest(teamId, id), RemovePostResponse.class);
	}

	public static void getPostList(Handler handler, int teamId, long cur) {
		HttpConnection.execute(handler, new PostListRequest(teamId, cur), PostListResponse.class);
	}

	public static void writeComment(Handler handler, int teamId, long postId, String comment) {
		HttpConnection.execute(handler, new WriteCommentRequest(teamId, postId, comment), WriteCommentResponse.class);
	}

	public static void removeComment(Handler handler, long postId, long commentId) {
		HttpConnection.execute(handler, new RemoveCommentRequest(postId, commentId), RemoveCommentResponse.class);
	}

	public static void commentList(Handler handler, int teamId, long postId) {
		HttpConnection.execute(handler, new CommentListRequest(teamId, postId), CommentListResponse.class);
	}

	public static void getMatchList(Handler handler, int teamId) {
		HttpConnection.execute(handler, new MatchListRequest(teamId), MatchListResponse.class);
	}

	public static void getHistoryList(Handler handler, int teamId, long cur, boolean order) {
		HttpConnection.execute(handler, new MatchHistoryRequest(teamId, cur, order), MatchHistoryResponse.class);
	}

	public static void registerGround(Handler handler, Ground ground) {
		HttpConnection.execute(handler, new RegisterGroundRequest(ground), RegisterGroundResponse.class);
	}

	public static void searchGround(Handler handler, String name) {
		HttpConnection.execute(handler, new SearchGroundRequest(name), SearchGroundResponse.class);
	}

	public static void createMatch(Handler handler, Match match) {
		HttpConnection.execute(handler, new CreateMatchRequest(match), CreateMatchResponse.class);
	}

	public static void getTeamInfo(Handler handler, int teamId) {
		HttpConnection.execute(handler, new TeamInfoRequest(teamId), TeamInfoResponse.class);
	}

	public static void getMatchInfo(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new MatchInfoRequest(matchId, teamId), MatchInfoResponse.class);
	}

	public static void inviteTeam(Handler handler, long matchId, int homeTeamId, int awayTeamId) {
		HttpConnection.execute(handler, new InviteTeamRequest(matchId, homeTeamId, awayTeamId), InviteTeamResponse.class);
	}

	public static void requestMatch(Handler handler, long matchId, int homeTeamId, int awayTeamId) {
		HttpConnection.execute(handler, new RequestMatchRequest(matchId, homeTeamId, awayTeamId), RequestMatchResponse.class);
	}

	public static void acceptMatch(Handler handler, long matchId) {
		HttpConnection.execute(handler, new AcceptMatchRequest(matchId), MatchActionResponse.class);
	}

	public static void rejectMatch(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new RejectMatchRequest(matchId, teamId), MatchActionResponse.class);
	}

	public static void cancelMatch(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new CancelMatchRequest(matchId, teamId), MatchActionResponse.class);
	}

	public static void sendRecord(Handler handler, long matchId, int teamId, int homeScore, int awayScore) {
		HttpConnection.execute(handler, new SendRecordRequest(matchId, teamId, homeScore, awayScore), MatchActionResponse.class);
	}

	public static void acceptRecord(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new AcceptRecordRequest(matchId, teamId), MatchActionResponse.class);
	}

	public static void startSurvey(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new StartSurveyRequest(matchId, teamId), MatchActionResponse.class);
	}

	public static void joinMatch(Handler handler, long matchId, int teamId, boolean join) {
		HttpConnection.execute(handler, new JoinMatchRequest(matchId, teamId, join), MatchActionResponse.class);
	}

	public static void getJoinMemberList(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new JoinMemberListRequest(matchId, teamId), JoinMemberListResponse.class);
	}

	public static void getMemberList(Handler handler, int teamId) {
		HttpConnection.execute(handler, new MemberListRequest(teamId), MemberListResponse.class);
	}

	public static void getWannabeList(Handler handler, int teamId) {
		HttpConnection.execute(handler, new WannabeListRequest(teamId), MemberListResponse.class);
	}

	public static void joinTeam(Handler handler, int teamId) {
		HttpConnection.execute(handler, new JoinTeamRequest(teamId), JoinTeamResponse.class);
	}

	public static void getUserInfo(Handler handler, int userId) {
		HttpConnection.execute(handler, new UserInfoRequest(userId), UserInfoResponse.class);
	}

	public static void acceptMember(Handler handler, int memberId, int teamId) {
		HttpConnection.execute(handler, new AcceptMemberRequest(memberId, teamId), MemberActionResponse.class);
	}

	public static void rejectMember(Handler handler, int memberId, int teamId) {
		HttpConnection.execute(handler, new RejectMemberRequest(memberId, teamId), MemberActionResponse.class);
	}

	public static void newManager(Handler handler, List<Integer> newManagerIdList, List<Integer> oldManagerIdList, int teamId) {
		HttpConnection.execute(handler, new NewManagerRequest(newManagerIdList, oldManagerIdList, teamId), NewManagerResponse.class);
	}

	public static void editTeamProfile(Handler handler, TeamInfo teamInfo) {
		HttpConnection.execute(handler, new EditTeamProfileRequest(teamInfo), EditTeamProfileResponse.class);
	}

	public static void searchMatch(Handler handler, SMatch sMatch) {
		HttpConnection.execute(handler, new SearchMatchRequest(sMatch), SearchMatchResponse.class);
	}

	public static void pushSurvey(Handler handler, long matchId, int teamId) {
		HttpConnection.execute(handler, new PushSurveyRequest(matchId, teamId), PushSurveyResponse.class);
	}

	public static void retireTeam(Handler handler, int teamId) {
		HttpConnection.execute(handler, new RetireTeamRequest(teamId), RetireTeamResponse.class);
	}

	public static void feedList(Handler handler, long cur) {
		HttpConnection.execute(handler, new FeedListRequest(cur), FeedListResponse.class);
	}

	public static void facebookLogin(Handler handler, String email, String name, String imageUrl, String regId, int os, int appVer, String uuid) {
		HttpConnection.execute(handler, new FacebookLoginRequest(email, name, imageUrl, regId, os, appVer, uuid), FacebookLoginResponse.class);
	}

	public static void messageList(Handler handler, long matchId, int teamId, long cur) {
		HttpConnection.execute(handler, new MessageListRequest(matchId, teamId, cur), MessageListResponse.class);
	}

	public static void pushTargettedSurvey(Handler handler, long matchId, int teamId, List<Integer> pushIds) {
		HttpConnection.execute(handler, new PushTargettedSurveyRequest(matchId, teamId, pushIds), PushTargettedSurveyResponse.class);
	}

	public static void removeFeed(Handler handler, long feedId) {
		HttpConnection.execute(handler, new RemoveFeedRequest(feedId), DefaultResponse.class);
	}

	public static void acceptTeam(Handler handler, int teamId) {
		HttpConnection.execute(handler, new AcceptTeamRequest(teamId), DefaultResponse.class);
	}

	public static void searchTeamNearby(Handler handler, double latitude, double longitude, int distance) {
		HttpConnection.execute(handler, new SearchTeamNearbyRequest(latitude, longitude, distance), SearchTeamNearbyResponse.class);
	}
}