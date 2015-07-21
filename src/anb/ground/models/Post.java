package anb.ground.models;

import java.util.HashMap;

import anb.ground.utils.ProtocolCodec;
import anb.ground.utils.Validator;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.type.TypeReference;

public class Post implements Parcelable {
	public static final String EXTRA_POST = "anb.ground.extra.post";
	public static final String EXTRA_PATH = "anb.ground.extra.imagePath";

	public static final int TYPE_ARTICLE = 0;
	public static final int TYPE_FEED = 1;
	public static final int TYPE_AD = 2;

	private long id;
	private int type;
	private int userId;
	private String userName;
	private String userImageUrl;
	private int teamId;
	private String message;
	private String extra;
	private String imagePath;
	private FeedMessage feedMessage;
	private long createdAt;
	private int commentCount;

	public Post() {
		super();
	}

	public Post(int teamId, int type, String message, String imagePath) {
		this.teamId = teamId;
		this.type = type;
		this.message = message;
		this.imagePath = imagePath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getExtra() {
		if (Validator.isEmpty(extra)) {
			if (type == TYPE_ARTICLE) {
				HashMap<String, String> map  = new HashMap<String, String>();
				map.put(EXTRA_PATH, imagePath);
				extra = ProtocolCodec.encode(map);
				return extra;
			} else {
				extra = ProtocolCodec.encode(feedMessage);
				return extra;
			}
		} else
			return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getImagePath() {
		if (Validator.isEmpty(imagePath)) {
			HashMap<String, String> map = ProtocolCodec.decode(extra, new TypeReference<HashMap<String, String>>() {});
			imagePath = map.get(EXTRA_PATH);
			return imagePath;
		} else
			return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public FeedMessage getFeedMessage() {
		if (feedMessage == null) {
			feedMessage = ProtocolCodec.decode(extra, FeedMessage.class);
			return feedMessage;
		} else
			return feedMessage;
	}

	public void setFeedMessage(FeedMessage feedMessage) {
		this.feedMessage = feedMessage;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// this is used to regenerate object
	public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
		public Post createFromParcel(Parcel in) {
			return new Post(in);
		}

		public Post[] newArray(int size) {
			return new Post[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(userId);
		dest.writeString(userName);
		dest.writeString(userImageUrl);
		dest.writeInt(teamId);
		dest.writeString(message);
		dest.writeLong(createdAt);
		dest.writeString(extra);
		dest.writeInt(commentCount);
	}

	public Post(Parcel in) {
		id = in.readLong();
		userId = in.readInt();
		userName = in.readString();
		userImageUrl = in.readString();
		teamId = in.readInt();
		message = in.readString();
		createdAt = in.readLong();
		extra = in.readString();
		commentCount = in.readInt();
	}
}