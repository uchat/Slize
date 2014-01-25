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

package com.tmeta.slize.booster;

import com.tmeta.slize.gameview.PieceView;

public interface IBoosterExecutable {
	public enum BoosterTyp {
		ADD_5_SEC, EMPTY, REMOVE_ALL, SWAP
	}
	
	public enum BoosterStatus {
		WAIT, EXEC, FINISHED
	}
	
	public interface IBoosterClient {
		void execResult(IBoosterExecutable booster, BoosterStatus status);
	}
	
	BoosterTyp getTyp();
	void setClient(IBoosterClient client);
	void start();
	void onTouchBegan(PieceView pv);
	void onTouchMoved(PieceView pv);
	void onTouchEnded(PieceView pv);
	void execAction();
}
