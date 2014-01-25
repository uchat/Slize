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

import com.tmeta.slize.Config;
import com.tmeta.slize.ScoreCenter;
import com.tmeta.slize.board.Piece;

public class FirstPlay20m implements IAward {
	public static final int ID = FIRST_PLAY_20M;

	ScoreCenter _scoreCenter;
	
	public FirstPlay20m(ScoreCenter scoreCenter) {
		_scoreCenter = scoreCenter;
	}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public void reset() {
		;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public int getTestMode() {
		return IAward.GAME_END;
	}

	@Override
	public String getHeader() {
		return "20-mover";
	}

	@Override
	public String getSubHeader() {
		return "Play 20-move mode";
	}

	@Override
	public String getDesc() {
		return "every move counts";
	}

	@Override
	public int getPointBonus() {
		return 100;
	}

	@Override
	public boolean isSingleIssue() {
		return true;
	}

	@Override
	public boolean test(ArrayList<ArrayList<Piece>> groups) {
		return false;
	}

	@Override
	public boolean test(int mode, int score, int numMove, int numplay) {
		if (mode != Config.MODE_20M)
			return false;
		
		if (_scoreCenter.getNumPlay20m() == 1) // 1 because we record number of game played before calling this function
			return true;
		
		return false;
	}

	@Override
	public int getLock() {
		return IAward.NO_LOCK;
	}

	@Override
	public String getPnmName() {
		return "26.pnm";
	}

	@Override
	public String getGoogleAchievementId() {
		return null;
	}

}
