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

import com.tmeta.slize.board.Piece;

public class MatchGroupN implements IAward {
	
	int _id, _n, _bonus, _lock;
	
	public MatchGroupN(int id, int n, int bonus, int lock) {
		_id = id;
		_n = n;
		_bonus = bonus;
		_lock = lock;
	}
	
	@Override
	public void reset() {		
		;
	}
	
	@Override
	public int getId() {
		return _id;
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public int getTestMode() {
		return IAward.IN_GAME;
	}

	@Override
	public String getHeader() {
		return "Group of " + _n;
	}

	@Override
	public String getSubHeader() {
		return "Create group of " + _n + " dots";
	}

	@Override
	public String getDesc() {
		String str = "";
		if (_n == 5)
			str = "get 2 bonus points\nfor every group of 5 created";
		if (_n >= 6)
			str = "get 5 bonus points\nfor every group of 6 or bigger";
		return str;
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
		for (ArrayList<Piece> g : groups) {
			if (g.size() == _n)
				return true;
		}
		return false;
	}

	@Override
	public boolean test(int mode, int score, int numMove, int numplay) {
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
