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

import java.util.ArrayList;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameManager;
import com.tmeta.slize.board.Board;
import com.tmeta.slize.board.Piece;
import com.tmeta.slize.gameview.BoardView;
import com.tmeta.slize.gameview.PieceView;

public class SwapBooster implements IBoosterExecutable {
	private static final int WAIT_FIRST = 0;
	private static final int WAIT_SECOND = 1;
	
	IBoosterClient _client;
	int _state;
	PieceView pv1, pv2;
	GameManager _gm;
	ArrayList<PieceView> _pv2swap = new ArrayList<PieceView>();
	
	public SwapBooster(GameManager gm) {
		_state = WAIT_FIRST;
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
		
		if (_state == WAIT_FIRST) {
			pv1 = pv;
			pv1.excite();
			
			BoardView bv = _gm.getBoardView();	
			Board gb = bv.getGameBoard();
			ArrayList<PieceView> pvs = new ArrayList<PieceView>();
			
			for (int x = 0; x < Config.BOARD_WIDTH; x++) {
				for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
					Piece gp = gb.getGamePiece(x, y);
					if (gp == null)
						continue;
					pvs.add(gp.getPieceView());
				}
			}
			
			int x, y, px, py;
			px = pv.getGamePiece().getX();
			py = pv.getGamePiece().getY();
			
			// top
			x = px;
			y = py - 1;
			if (y >= 0) {
				PieceView pv2 = bv.getPieceView(x, y);
				if (pv2 != null) {
					pvs.remove(pv2);
					_pv2swap.add(pv2);
				}
			}

			// left
			x = px - 1;
			y = py;
			if (x >= 0) {
				PieceView pv2 = bv.getPieceView(x, y);
				if (pv2 != null) {
					pvs.remove(pv2);
					_pv2swap.add(pv2);
				}
			}
			
			
			// bottom
			x = px;
			y = py + 1;
			if (y <= Config.BOARD_HEIGHT - 1) {
				PieceView pv2 = bv.getPieceView(x, y);
				if (pv2 != null) {
					pvs.remove(pv2);
					_pv2swap.add(pv2);
				}
			}
			
			// right
			x = px + 1;
			y = py;
			if (x <= Config.BOARD_WIDTH - 1) {
				PieceView pv2 = bv.getPieceView(x, y);
				if (pv2 != null) {
					pvs.remove(pv2);
					_pv2swap.add(pv2);
				}
			}
			
			for (PieceView pv3 : pvs) {
				pv3.setFocus(false);
			}
			for (PieceView pv3 : _pv2swap) {
				pv3.setFocus(true);
			}
			
			_state = WAIT_SECOND;
			_client.execResult(this, IBoosterExecutable.BoosterStatus.WAIT);
			return;
		}
		
		if (_state == WAIT_SECOND) {
			if (_pv2swap.contains(pv)) {
				pv2 = pv;
				_client.execResult(this, IBoosterExecutable.BoosterStatus.EXEC);
				return;
			} else {
				for (PieceView pv3 : _pv2swap) {
					pv3.setFocus(true);
				}
				_client.execResult(this, IBoosterExecutable.BoosterStatus.WAIT);
			}
		}
		
	}

	@Override
	public void execAction() {
		for (PieceView pv3 : _pv2swap) {
			pv3.setFocus(false);
		}
		_gm.swap(pv1, pv2);
		_client.execResult(this, IBoosterExecutable.BoosterStatus.FINISHED);			
	}
	
	@Override
	public BoosterTyp getTyp() {
		return IBoosterExecutable.BoosterTyp.SWAP;
	}


}
