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

public interface IAward {
		
	/* in game */
	public static final int MATCH_SQ_2X2 = 2;
	public static final int MATCH_SQ_3X3 = 3;
	public static final int MATCH_GROUP_5 = 7;
	public static final int MATCH_GROUP_6 = 8;
	public static final int MATCH_GROUP_7 = 9;
	
	
	public static final int MATCH_CROSS = 1;
	public static final int MATCH_SQ_ISLAND = 4;
	public static final int MATCH_VERT_7 = 5;
	public static final int MATCH_HORZ_7 = 6;
	public static final int FIRST_PLAY_20M = 26;

	public static final int LOCAL_HIGH_SCORE = 23;
	public static final int MATCH_GROUP_5_3CONSQ = 11;
	public static final int MATCH_GROUP_5_5CONSQ = 12;
	public static final int MATCH_GROUP_6_3CONSQ = 13;
	public static final int MATCH_GROUP_6_5CONSQ = 14;
	
	/* game end */
	public static final int NUM_PLAY_1_CHECKER = 15;
	public static final int NUM_PLAY_5_CHECKER = 16;
	public static final int NUM_PLAY_10_CHECKER = 17;
	public static final int NUM_PLAY_25_CHECKER = 18;
	public static final int NUM_PLAY_50_CHECKER = 19;

	public static final int NUM_PLAY_100_CHECKER = 20;
	public static final int NUM_PLAY_250_CHECKER = 21;
	public static final int NUM_PLAY_500_CHECKER = 22;
	public static final int SCORE_150_20M = 32;
	public static final int SCORE_300_20M = 33;
	
	public static final int SCORE_150_60S = 27;
	public static final int SCORE_250_60S = 28;
	public static final int SCORE_500_60S = 29;
	public static final int SCORE_500_120S = 30;
	public static final int SCORE_1000_120S = 31;
	
	public static final int EARLY_BIRD = 38;
	public static final int ALL_NIGHTER = 39;
	public static final int LUNCH_TIME = 40;
	
	public static final int[] AWARD_LIST = new int[] {
		MATCH_CROSS,
		MATCH_SQ_2X2,
		MATCH_SQ_3X3,
		MATCH_SQ_ISLAND,
		MATCH_VERT_7,
		MATCH_HORZ_7,
		MATCH_GROUP_5,
		MATCH_GROUP_6,
		MATCH_GROUP_7,
		MATCH_GROUP_5_3CONSQ,
		MATCH_GROUP_5_5CONSQ, 
		MATCH_GROUP_6_3CONSQ, 
		MATCH_GROUP_6_5CONSQ, 
		
		/* game end */
		NUM_PLAY_1_CHECKER,
		NUM_PLAY_5_CHECKER,
		NUM_PLAY_10_CHECKER,
		NUM_PLAY_25_CHECKER,
		NUM_PLAY_50_CHECKER,
		NUM_PLAY_100_CHECKER,
		NUM_PLAY_250_CHECKER,
		NUM_PLAY_500_CHECKER,
		LOCAL_HIGH_SCORE,
		
		FIRST_PLAY_20M,
		
		SCORE_150_60S,
		SCORE_250_60S,
		SCORE_500_60S,
		SCORE_500_120S,
		SCORE_1000_120S,
		SCORE_150_20M,
		SCORE_300_20M,
		
		EARLY_BIRD,
		ALL_NIGHTER,
		LUNCH_TIME //,
	};
	
	/* test mode */
	public static final int IN_GAME = 0;
	public static final int GAME_END = 1;
	public static final int NO_LOCK = -1;

	
	int getId();
	void reset();
	int getPriority();
	int getTestMode();
	String getHeader();
	String getSubHeader();
	int getLock();
	String getDesc();
	String getPnmName();
	int getPointBonus();
	String getGoogleAchievementId();
	boolean isSingleIssue();
	boolean test(ArrayList<ArrayList<Piece>> groups);
	boolean test(int mode, int score, int numMove, int numplay);
}
