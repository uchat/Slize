/*
 * Copyright (C) 2014 tmeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tmeta.slize.award;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.util.SparseArray;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameActivity;
import com.tmeta.slize.R;
import com.tmeta.slize.board.Piece;

public class AwardCenter {
	
	ArrayList<IAward> _roundAwards;
	int _mode;
	GameActivity _game;
	
	ArrayList<IAward> gameEndAwards = new ArrayList<IAward>();
	ArrayList<IAward> inGameAwards = new ArrayList<IAward>();
	
	private SparseArray<IAward> _awards;
	private SparseArray<IAward> _won;
	
	
	public class CustomComparator implements Comparator<IAward> {
	    @Override
	    public int compare(IAward o1, IAward o2) {
	        int d1 = o1.getPriority();
	        int d2 = o2.getPriority();
	        
	        if (d1 > d2)
	        	return 1;
	        if (d1 < d2)
	        	return -1;
	        return 0;
	    }
	}
	
	public AwardCenter(GameActivity game) {
		_game = game;
		_awards = new SparseArray<IAward>();
		
		_awards.put(MatchCross.ID, new MatchCross());
		_awards.put(MatchSQ2x2.ID, new MatchSQ2x2());
		_awards.put(MatchSQ3x3.ID, new MatchSQ3x3());
		_awards.put(MatchSQIsland.ID, new MatchSQIsland());
		_awards.put(MatchHorz7.ID, new MatchHorz7(_game.getResources().getText(R.string.achievement_horizontal_7).toString()));
		_awards.put(MatchVert7.ID, new MatchVert7(_game.getResources().getText(R.string.achievement_vertical_7).toString()));
		_awards.put(IAward.MATCH_GROUP_5, new MatchGroupN(IAward.MATCH_GROUP_5, 5, 100, IAward.NUM_PLAY_1_CHECKER));
		_awards.put(IAward.MATCH_GROUP_6, new MatchGroupN(IAward.MATCH_GROUP_6, 6, 500, IAward.MATCH_SQ_2X2));
		_awards.put(IAward.MATCH_GROUP_7, new MatchGroupN(IAward.MATCH_GROUP_7, 7, 1000, IAward.MATCH_CROSS));
		
		_awards.put(IAward.MATCH_GROUP_5_3CONSQ, new MatchGroupNConsq(IAward.MATCH_GROUP_5_3CONSQ, 5, 3, 100, IAward.MATCH_GROUP_5));
		_awards.put(IAward.MATCH_GROUP_5_5CONSQ, new MatchGroupNConsq(IAward.MATCH_GROUP_5_5CONSQ, 5, 5, 500, IAward.MATCH_GROUP_5_3CONSQ));
		_awards.put(IAward.MATCH_GROUP_6_3CONSQ, new MatchGroupNConsq(IAward.MATCH_GROUP_6_3CONSQ, 6, 3, 500, IAward.MATCH_GROUP_7));
		_awards.put(IAward.MATCH_GROUP_6_5CONSQ, new MatchGroupNConsq(IAward.MATCH_GROUP_6_5CONSQ, 6, 5, 1000, IAward.MATCH_GROUP_6_3CONSQ));

		_awards.put(IAward.NUM_PLAY_1_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_1_CHECKER, 1, 500, "New Joiner", "play Slize for the first time", "welcome aboard!", IAward.NO_LOCK));
		_awards.put(IAward.NUM_PLAY_5_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_5_CHECKER, 5, 100, "Trainee", "play Slize for the 5th time", "", IAward.NUM_PLAY_1_CHECKER));
		_awards.put(IAward.NUM_PLAY_10_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_10_CHECKER, 10, 250, "Associate", "play Slize for 10th time", "", IAward.NUM_PLAY_5_CHECKER));
		_awards.put(IAward.NUM_PLAY_25_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_25_CHECKER, 25, 500, "Jr. Slizer", "play Slize for 25th time", "", IAward.NUM_PLAY_10_CHECKER));
		_awards.put(IAward.NUM_PLAY_50_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_50_CHECKER, 50, 1500, "Sr. Slizer", "play Slize for the 50th time", "", IAward.NUM_PLAY_25_CHECKER));
		_awards.put(IAward.NUM_PLAY_100_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_100_CHECKER, 100, 5000, "Slizer Mgr.", "play Slize for the 100th time", "Century mark!", IAward.NUM_PLAY_50_CHECKER));
		_awards.put(IAward.NUM_PLAY_250_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_250_CHECKER, 250, 20000, "CEO of Slizer", "play Slize for the 250th time", "", IAward.NUM_PLAY_100_CHECKER));
		_awards.put(IAward.NUM_PLAY_500_CHECKER, new NumPlayChecker(IAward.NUM_PLAY_500_CHECKER, 500, 50000, "Slizer Guru", "play Slize for the 500th time", "", IAward.NUM_PLAY_250_CHECKER));
		_awards.put(LocalHighScore.ID, new LocalHighScore(_game.scoreCenter));
		
		_awards.put(FirstPlay20m.ID, new FirstPlay20m(_game.scoreCenter));
		
		_awards.put(IAward.SCORE_150_60S, new ScoreModeChecker(IAward.SCORE_150_60S, 150, Config.MODE_60S, 250, IAward.NUM_PLAY_5_CHECKER));
		_awards.put(IAward.SCORE_250_60S, new ScoreModeChecker(IAward.SCORE_250_60S, 250, Config.MODE_60S, 500, IAward.SCORE_150_60S));
		_awards.put(IAward.SCORE_500_60S, new ScoreModeChecker(IAward.SCORE_500_60S, 500, Config.MODE_60S, 1000, IAward.SCORE_250_60S));
		_awards.put(IAward.SCORE_500_120S, new ScoreModeChecker(IAward.SCORE_500_120S, 1000, Config.MODE_120S, 1000, IAward.SCORE_250_60S));
		_awards.put(IAward.SCORE_1000_120S, new ScoreModeChecker(IAward.SCORE_1000_120S, 2000, Config.MODE_120S, 5000, IAward.SCORE_500_120S));
		_awards.put(IAward.SCORE_150_20M, new ScoreModeChecker(IAward.SCORE_150_20M, 200, Config.MODE_120S, 250, IAward.FIRST_PLAY_20M));
		_awards.put(IAward.SCORE_300_20M, new ScoreModeChecker(IAward.SCORE_300_20M, 300, Config.MODE_120S, 500, IAward.SCORE_150_20M));

		_awards.put(IAward.EARLY_BIRD, new TimeIntervalChecker(IAward.EARLY_BIRD, 500, 6, 7, 
				"Early Bird", "Play between 6am and 7am", "", IAward.NUM_PLAY_10_CHECKER,
				_game.getResources().getText(R.string.achievement_early_bird).toString()));
		_awards.put(IAward.ALL_NIGHTER, new TimeIntervalChecker(IAward.ALL_NIGHTER, 500, 1, 5, 
				"All Nighter", "Play between 1am and 5am", "", IAward.NUM_PLAY_10_CHECKER,
				_game.getResources().getText(R.string.achievement_all_nighter).toString()));
		_awards.put(IAward.LUNCH_TIME, new TimeIntervalChecker(IAward.LUNCH_TIME, 500, 12, 13, 
				"Lunch Break", "Play between noon and 1pm", "", IAward.NUM_PLAY_10_CHECKER,
				_game.getResources().getText(R.string.achievement_lunch_break).toString()));		
	}
	
	public IAward getAward(int id) {
		return _awards.get(id);
	}
		
	public boolean isWon(int id) {
		if (id == IAward.NO_LOCK)
			return true;
				
		if (_won == null)
			refreshWonAward();
		return _won.get(id) != null;
	}
	
	public void refreshWonAward() {
		if (_won == null)
			_won = new SparseArray<IAward>();
		_won.clear();
		int[] issuedIds = _game.dbHandler.getIssuedAwards();
		if (issuedIds != null) {
			for (int i : issuedIds) {
				_won.put(i,  _awards.get(i));
			}
		}
	}
	
	public void initNewRound(int mode) {
		_roundAwards = new ArrayList<IAward>();
		_mode = mode;

		inGameAwards.clear();
		gameEndAwards.clear();
		
		refreshWonAward();
		
		for (int id : IAward.AWARD_LIST) {
			IAward a = _awards.get(id);
			if (isWon(id) && a.isSingleIssue()) {
				continue;
			}	
			if (! isWon(a.getLock())) // still locked
				continue;
			a.reset();
			if (a.getTestMode() == IAward.IN_GAME) {
				inGameAwards.add(a);
			} else { /* GAME_END */
				gameEndAwards.add(a);
			}

		}
	}
	
	public ArrayList<IAward> getAwardWon() {
		Collections.sort(_roundAwards, new CustomComparator());
		return _roundAwards;
	}
	

	public void checkInGameAward(ArrayList<ArrayList<Piece>> groups) {
		for (IAward a : inGameAwards) {
			if (a.test(groups)) {
				_roundAwards.add(a);
				_game.dbHandler.issueAward(a.getId());
			}
		}
		inGameAwards.removeAll(_roundAwards);
	}
	
	public void checkGameEndAward(int score, int numMove, int numPlay) {
		for (IAward a : gameEndAwards) {
			if (a.test(_mode, score, numMove, numPlay)) {
				_roundAwards.add(a);
				_game.dbHandler.issueAward(a.getId());
			}
		}
	}
	
}
