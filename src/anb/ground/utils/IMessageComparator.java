package anb.ground.utils;

import java.util.Comparator;

import anb.ground.models.IMessage;

public class IMessageComparator implements Comparator<IMessage> {

	@Override
	public int compare(IMessage lhs, IMessage rhs) {
		return (lhs.getId() < rhs.getId())? -1 : 0;
	}

}
