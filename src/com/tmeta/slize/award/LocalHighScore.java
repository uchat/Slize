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

import com.tmeta.slize.ScoreCenter;
import com.tmeta.slize.board.Piece;

public class LocalHighScore implements IAward {
	public static final int ID = LOCAL_HIGH_SCORE;
	
	int _score = 0;
	ScoreCenter _scoreCenter;
	
	public LocalHighScore(ScoreCenter scoreCenter) {
		_scoreCenter = scoreCenter;
	}

	@Override
	public void reset() {		
		;
	}

	@Override
	public int getId() {
		return ID;
	}
	
	public int getPriority() {
		return 2;
	}

	@Override
	public String getHeader() {
		return String.valueOf(_score);
	}
	
	@Override
	public String getSubHeader() {
		return "New High Score";
	}

	@Override
	public String getDesc() {
		return _score + " is your highest score..so far";
	}

	@Override
	public int getPointBonus() {
		return 200;
	}

	@Override
	public boolean isSingleIssue() {
		return false;
	}
	
	@Override
	public boolean test(ArrayList<ArrayList<Piece>> groups) {
		return false;
	}

	@Override
	public boolean test(int mode, int score, int numMove, int numplay) {
		int bestScore = _scoreCenter.getUserTopScore();
		_score = score;
		return score > bestScore;
	}
	
	@Override
	public int getTestMode() {
		return IAward.GAME_END;
	}
	
	@Override
	public int getLock() {
		return IAward.NO_LOCK;
	}
	
	@Override
	public String getPnmName() {
		return "23.pnm";
	}
	
	@Override
	public String getGoogleAchievementId() {
		return null;
	}



}
