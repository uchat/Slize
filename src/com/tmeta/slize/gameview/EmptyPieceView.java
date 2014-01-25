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
package com.tmeta.slize.gameview;

import org.andengine.entity.primitive.Rectangle;

import com.tmeta.slize.GameManager;
import com.tmeta.slize.Config;
import com.tmeta.slize.board.Piece;


public class EmptyPieceView extends PieceView {
	private GameManager _gm;
	private Rectangle _bkg;
	
	public EmptyPieceView(GameManager gm) {
		this._gm = gm;
		this.mPiece = new Piece(Piece.TYP_EMPTY);
		this.mPiece.setEmpty(true);
		this.mPiece.setPieceView(this);
		this._z = 1;
		
		_bkg = new Rectangle(0, 0, 
				Config.PIECE_WIDTH,
				Config.PIECE_HEIGHT,
				_gm.getGameActivity().getVertexBufferObjectManager());
		_bkg.setColor(0, 0, 0);
		_bkg.setAlpha(0);
		attachChild(_bkg);
		

	}
		
	@Override
	public void onTouchBegan() {
		;
	}

	@Override
	public void onTouchMoved() {
		if (_gm.isGamePausedOrEnded())
			return;
		if (mPiece.status != Piece.S_ONBOARD)
			return;
		if (_gm.isMoveValid(mPiece)) {
			_gm.moveSelectedPieces(this);
		}
	}

	@Override
	public void onTouchEnded() {
		if (_gm.isGamePausedOrEnded())
			return;
		if (mPiece.status != Piece.S_ONBOARD)
			return;
		_gm.clearSelectedPieces();
		_gm.updateSelectedPieces();
		_gm.check();
	}

	@Override
	public void hide() {
		;
	}

	@Override
	public void excite() {
		;
	}

	@Override
	public void stale() {
		;
	}

	@Override
	public void setFocus(boolean f) {
		;
	}

}
