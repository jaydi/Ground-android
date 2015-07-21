package anb.ground.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import anb.ground.models.Match;

public class MatchListUtils {

	public static List<Match> getSettledList(List<Match> matchList, boolean isManaged) {
		List<Match> settledList = new ArrayList<Match>();

		for (Match match : matchList)
			if (isManaged && match.getStatus() >= Match.MATCHING_COMPLETED)
				settledList.add(match);
			else if (match.getStatus() == Match.MATCHING_COMPLETED || match.getStatus() == Match.SCORE_COMPLETED)
				settledList.add(match);

		Collections.sort(settledList, new MatchComparator());

		return settledList;
	}

	public static List<Match> getUpcomingList(List<Match> settledList) {
		List<Match> upcomingMatchList = new ArrayList<Match>();

		for (Match match : settledList)
			if (match.getStatus() == Match.MATCHING_COMPLETED)
				upcomingMatchList.add(match);

		Collections.sort(upcomingMatchList, new MatchComparator());

		return upcomingMatchList;
	}

	public static List<Match> getRecordingList(List<Match> settledList) {
		List<Match> recordingMatchList = new ArrayList<Match>();

		for (Match match : settledList)
			if (match.getStatus() > Match.MATCHING_COMPLETED && match.getStatus() < Match.SCORE_COMPLETED)
				recordingMatchList.add(match);

		Collections.sort(recordingMatchList, new MatchComparator());

		return recordingMatchList;
	}

	public static List<Match> getPastList(List<Match> settledList) {
		List<Match> pastMatchList = new ArrayList<Match>();

		for (Match match : settledList)
			if (match.getStatus() == Match.SCORE_COMPLETED)
				pastMatchList.add(match);

		Collections.sort(pastMatchList, new MatchComparator());

		return pastMatchList;
	}

	public static List<Match> getUnsettledList(List<Match> matchList) {
		List<Match> unsettledList = new ArrayList<Match>();

		for (Match match : matchList)
			if (match.getStatus() < Match.MATCHING_COMPLETED)
				unsettledList.add(match);

		Collections.sort(unsettledList, new MatchComparator());

		return unsettledList;
	}

	public static List<Match> getUnsettledHomeList(List<Match> unsettledList, int teamId) {
		List<Match> unsettledHomeMatchList = new ArrayList<Match>();

		for (Match match : unsettledList)
			if (match.isHome(teamId))
				unsettledHomeMatchList.add(match);

		Collections.sort(unsettledHomeMatchList, new MatchComparator());

		return unsettledHomeMatchList;
	}

	public static List<Match> getUnsettledAwayList(List<Match> unsettledList, int teamId) {
		List<Match> unsettledAwayMatchList = new ArrayList<Match>();

		for (Match match : unsettledList)
			if (!match.isHome(teamId))
				unsettledAwayMatchList.add(match);

		Collections.sort(unsettledAwayMatchList, new MatchComparator());

		return unsettledAwayMatchList;
	}
}
