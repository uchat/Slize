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

public class NumPlayChecker implements IAward {
	
	int _numplay, _bonus, _id, _lock;
	String _hdr, _subHdr, _desc;

	public NumPlayChecker(int id, int numplay, int bonus,
			String hdr, String subHdr, String desc, int lock) {
		_id = id;
		_numplay = numplay;
		_bonus = bonus;
		_hdr = hdr;
		_subHdr = subHdr;
		_desc = desc;
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
		return 1;
	}

	
	@Override
	public String getHeader() {
		return _hdr;
	}

	@Override
	public String getSubHeader() {
		return _subHdr;
	}

	@Override
	public String getDesc() {
		return _desc;
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
//		Log.d(Config.TAG, "test: " + numplay + " : " + _numplay);
		return numplay == _numplay;
	}

	@Override
	public int getTestMode() {
		return IAward.GAME_END;
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
