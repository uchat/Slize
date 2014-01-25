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

package com.tmeta.slize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.tmeta.slize.booster.IBoosterExecutable;

public class ScoreCenter {

	GameActivity _game;
	int numplays[];
	int _bestScore;
	int _totalPoint;
	HashMap<IBoosterExecutable.BoosterTyp, Integer> _boosters;
	
	public ScoreCenter(GameActivity game) {
		_game = game;
		numplays = _game.dbHandler.getNumPlayed();
		_totalPoint = _game.dbHandler.getTotalPoint();
		_boosters = new HashMap<IBoosterExecutable.BoosterTyp, Integer>();
		for (IBoosterExecutable.BoosterTyp typ : IBoosterExecutable.BoosterTyp.values()) {
			_boosters.put(typ, _game.dbHandler.getBoosterNum(typ));
		}
	}

	public int getBoosterCount(IBoosterExecutable.BoosterTyp typ) {
		return _boosters.get(typ);
	}
	
	public void recordBoosterUsed(IBoosterExecutable.BoosterTyp typ) {
		int v = _boosters.get(typ) - 1;
		if (v < 0)
			v = 0;
		_boosters.put(typ, v);
		_game.dbHandler.updateBooster(typ, v);
	}
	
	public void topupBooster(IBoosterExecutable.BoosterTyp typ, int bundle, int cost) {
		int v = _boosters.get(typ) + bundle;
		_boosters.put(typ, v);
		_game.dbHandler.updateBooster(typ, v);
		
		_totalPoint -= cost;
		_game.dbHandler.updateTotalPoint(_totalPoint);
	}

	public int getNumPlayAll() {
		return numplays[0];
	}
	
	public int getNumPlay20m() {
		return numplays[1];
	}

	public int getNumPlay60s() {
		return numplays[2];
	}
	
	public int getNumPlay120s() {
		return numplays[3];
	}
	
	public List<Pair> getUserTop10Weekly(int mode) {
		int[] scores = new int[10];
		int count = _game.dbHandler.getUserWeekTopScore(mode, scores);
		ArrayList<Pair> ret = new ArrayList<Pair>();
		for (int i = 0; i < count; i++) {
			ret.add(new Pair("You", scores[i]));
		}
		_bestScore = scores[0];
		return ret;
	}
	
	public List<Pair> getUserTop10AllTime(int mode) {
		int[] scores = new int[10];
		int count = _game.dbHandler.getUserTopScore(mode, scores);
		ArrayList<Pair> ret = new ArrayList<Pair>();
		for (int i = 0; i < count; i++) {
			ret.add(new Pair("You", scores[i]));
		}
		_bestScore = scores[0];
		return ret;
	}
	
	public int getUserTopScore() {
		return _bestScore;
	}
	
	public int getTotalPoint() {
		return _totalPoint;
	}
	
	public void recordScore(int mode, int score) {
		_game.dbHandler.recordScore(mode, score);
		_totalPoint += score;
		_game.dbHandler.updateTotalPoint(_totalPoint);
	}
	
	public void addBonusScore(int score) {
		_totalPoint += score;
		_game.dbHandler.updateTotalPoint(_totalPoint);
	}
	
	
	public void recordGamePlayed(int mode) {
		numplays[0]++;
		switch (mode) {
		case Config.MODE_20M:
			numplays[1]++;
			break;
		case Config.MODE_60S:
			numplays[2]++;
			break;
		case Config.MODE_120S:
			numplays[3]++;
			break;
		}
		_game.dbHandler.recordPlay(numplays[0], numplays[1], numplays[2], numplays[3]);
	}

}
