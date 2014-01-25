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


public class MatchCross extends IMatchWithinBound implements IAward {
	public static final int ID = IAward.MATCH_CROSS;
	
	public MatchCross() {
		super(new int[][] {{0,0},{-1,1},{0,1},{1,1},{0,2}});
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
		return 30;
	}

	
	@Override
	public String getHeader() {
		return "Cross";
	}

	@Override
	public String getSubHeader() {
		return "create a cross pattern";
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public int getPointBonus() {
		return 300;
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
		return "1.pnm";
	}

	@Override
	public String getGoogleAchievementId() {
		return null;
	}


}
