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

public class MatchGroupNConsq implements IAward {
	int _id, _n, _bonus, _consq, _count, _lock;
	
	public MatchGroupNConsq(int id, int n, int consq, int bonus, int lock) {
		_id = id;
		_n = n;
		_bonus = bonus;
		_consq = consq;
		_lock = lock;
		_count = 0;
	}

	@Override
	public void reset() {		
		_count = 0;
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
		String s1 = (_n == 5) ? "Group Hug" : "In Stream";
		String s2 = (_consq == 3) ? " Tiny" : " Big";
		return s1 + s2;
	}

	@Override
	public String getSubHeader() {
		return "Group of " + _n + ", " + _consq + " times in a row";
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
		for (ArrayList<Piece> g : groups) {
			if (g.size() == _n) {
				_count++;
				return _count == _consq;
			}
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
