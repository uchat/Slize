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
import com.tmeta.slize.board.Piece;

public class ScoreModeChecker implements IAward {
	//public static final int ID = SCORE_150_60S;

	int _id, _score, _mode, _bonus;
	int _lock;

	
	public ScoreModeChecker(int id, int score, int mode, int bonus, int lock) {
		_id = id;
		_score = score;
		_mode = mode;
		_bonus = bonus;
		_lock = lock;
	}

	
	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void reset() {
		;
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public int getTestMode() {
		return IAward.GAME_END;
	}

	@Override
	public String getHeader() {
		return "reach " + _score;
	}

	@Override
	public String getSubHeader() {
		String mode = "20-move";
		if (_mode == Config.MODE_60S)
			mode = "60 s.";
		if (_mode == Config.MODE_120S)
			mode = "120 s.";
		return "score " + _score + " in " + mode + " mode";
	}

	@Override
	public String getDesc() {
		return "";
	}

	@Override
	public int getPointBonus() {
		return _bonus;
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
		if (mode == _mode && score >= _score) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getLock() {
		return _lock;
	}
	
	@Override
	public String getPnmName() {
		return _id + ".pnm";
	}
	
	@Override
	public String getGoogleAchievementId() {
		return null;
	}



}
