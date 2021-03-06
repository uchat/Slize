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

import com.tmeta.slize.GameManager;
import com.tmeta.slize.gameview.PieceView;

public class Add5SecBooster implements IBoosterExecutable {

	GameManager _gm;
	IBoosterClient _client;
	
	public Add5SecBooster(GameManager gm) {
		
		_gm = gm;
	}
	
	@Override
	public void setClient(IBoosterClient client) {
		_client = client;
	}
	
	@Override
	public void start() {
		_client.execResult(this, IBoosterExecutable.BoosterStatus.EXEC);		
	}

	@Override
	public void onTouchBegan(PieceView pv) {
		;		
	}

	@Override
	public void onTouchMoved(PieceView pv) {
		;	
	}

	@Override
	public void onTouchEnded(PieceView pv) {
		;
	}

	@Override
	public void execAction() {
		_gm.addTime(5);
		_client.execResult(this, IBoosterExecutable.BoosterStatus.FINISHED);		
	}

	@Override
	public BoosterTyp getTyp() {
		return IBoosterExecutable.BoosterTyp.ADD_5_SEC;
	}


}
