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

import com.tmeta.slize.board.IMatchWithinBound;
import com.tmeta.slize.board.Piece;

public class MatchSQ2x2 extends IMatchWithinBound implements IAward {
	public static final int ID = IAward.MATCH_SQ_2X2;
	
	public MatchSQ2x2() {
		super(new int[][] {{0,0},{0,1},{1,0},{1,1}});
	}

	@Override
	public void reset() {		
		;
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getPriority() {
		return 30;
	}

	@Override
	public String getHeader() {
		return "Little Square";
	}

	@Override
	public String getSubHeader() {
		return "Create 2x2 square";
	}

	@Override
	public String getDesc() {
		return "";
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
			if (this.match(g))
				return true;
		}
		return false;
	}

	@Override
	public boolean test(int mode, int score, int numMove, int numplay) {
		return false;
	}

	@Override
	public int getTestMode() {
		return IAward.IN_GAME;
	}

	@Override
	public int getLock() {
		return IAward.MATCH_GROUP_5;
	}
	
	@Override
	public String getPnmName() {
		return "2.pnm";
	}

	@Override
	public String getGoogleAchievementId() {
		return null;
	}

}
