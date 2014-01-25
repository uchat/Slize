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

import com.tmeta.slize.Config;
import com.tmeta.slize.GameManager;
import com.tmeta.slize.board.Board;
import com.tmeta.slize.board.Piece;
import com.tmeta.slize.gameview.BoardView;
import com.tmeta.slize.gameview.PieceView;

public class AddEmptyBooster implements IBoosterExecutable {
	
	IBoosterClient _client;
	PieceView _pv;
	GameManager _gm;
	
	public AddEmptyBooster(GameManager gm) {
		_gm = gm;
	}
	
	@Override
	public void setClient(IBoosterClient client) {
		_client = client;
	}
	
	
	@Override
	public void start() {
		BoardView bv = _gm.getBoardView();
		Board gb = bv.getGameBoard();
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				Piece gp = gb.getGamePiece(x, y);
				if (gp == null)
					continue;
				gp.getPieceView().setFocus(true);
			}
		}
		
		_client.execResult(this, IBoosterExecutable.BoosterStatus.WAIT);
	}

	@Override
	public void onTouchBegan(PieceView pv) {
		; // ignore
	}

	@Override
	public void onTouchMoved(PieceView pv) {
		; // ignore
	}

	@Override
	public void onTouchEnded(PieceView pv) {
		if (pv.getGamePiece().isEmpty()) {
			_client.execResult(this, IBoosterExecutable.BoosterStatus.WAIT);
			return;
		}
		
		_pv = pv;
		_client.execResult(this, IBoosterExecutable.BoosterStatus.EXEC);
	}

	@Override
	public void execAction() {
		BoardView bv = _gm.getBoardView();
		Board gb = bv.getGameBoard();
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				Piece gp = gb.getGamePiece(x, y);
				if (gp == null)
					continue;
				gp.getPieceView().setFocus(false);
			}
		}
		_gm.replaceWithEmtpy(_pv);
		_client.execResult(this, IBoosterExecutable.BoosterStatus.FINISHED);	
	}

	@Override
	public BoosterTyp getTyp() {
		return IBoosterExecutable.BoosterTyp.EMPTY;
	}
}