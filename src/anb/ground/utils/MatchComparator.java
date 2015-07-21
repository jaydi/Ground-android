package anb.ground.utils;

import java.util.Comparator;

import anb.ground.models.Match;

public class MatchComparator implements Comparator<Match> {

	@Override
	public int compare(Match lhs, Match rhs) {
		return (lhs.getStartTime() < rhs.getStartTime())? 0 : -1;
	}	

}
