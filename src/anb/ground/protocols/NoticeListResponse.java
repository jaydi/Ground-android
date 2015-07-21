package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Post;

public class NoticeListResponse extends DefaultResponse {
	private List<Post> noticeList;

	public List<Post> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Post> noticeList) {
		this.noticeList = noticeList;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Noticelist : ");
		sb.append(noticeList.toString());
		return sb.toString();
	}
	
}
