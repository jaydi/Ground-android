package anb.ground.utils;

import java.util.Comparator;

import anb.ground.models.TeamHint;

public class MenuComparator implements Comparator<TeamHint> {

	@Override
	public int compare(TeamHint lhs, TeamHint rhs) {
		return (lhs.getId() > rhs.getId())? -1 : 0;
	}

}
