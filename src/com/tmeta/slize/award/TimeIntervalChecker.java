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

import android.text.format.Time;

import com.tmeta.slize.board.Piece;

public class TimeIntervalChecker implements IAward {
	
	int _id, _bonus, _fromHr, _toHr;
	String _hdr, _subHdr, _desc, _gid;
	int _lock;

	public TimeIntervalChecker(int id, int bonus, int fromHr, int toHr, String hdr, String subHdr, String desc, int lock, String gid) {
		_id = id;
		_bonus = bonus;
		_fromHr = fromHr;
		_toHr = toHr;
		_hdr = hdr;
		_subHdr = subHdr;
		_desc = desc;
		_lock = lock;
		_gid = gid;
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
		return 40;
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
		// http://stackoverflow.com/a/9240796
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		int h = now.hour;
		return h >= _fromHr && h <= _toHr;
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
		return _gid;
	}

}