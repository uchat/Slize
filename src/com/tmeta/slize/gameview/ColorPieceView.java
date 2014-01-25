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

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import com.tmeta.slize.GameManager;
import com.tmeta.slize.Config;
import com.tmeta.slize.board.Piece;

public class ColorPieceView extends PieceView {
	
	private int _typ;
	private Rectangle _bkg;
	private GameManager _gm;
	private Sprite _sprite, _hili;
	boolean _destroyed = false;
	private IEntityModifier _hiliMod;
	private IEntityModifier _dotMod;
	
	public ColorPieceView(GameManager gm, int typ) {
		_typ = typ;
		_gm = gm;
		mPiece = new Piece(typ);
		mPiece.setPieceView(this);
		mPiece.setMovable(true);
		mPiece.setPathable(true);
		
		_bkg = new Rectangle(0, 0, 
				Config.PIECE_WIDTH,
				Config.PIECE_HEIGHT,
				_gm.getGameActivity().getVertexBufferObjectManager());
		_bkg.setColor(0.3f, 0.3f, 0.3f);
		_bkg.setAlpha(0);
		attachChild(_bkg);
		
		_hili = new Sprite(0, 0,
				_gm.getGameActivity().res.hiliTR, _gm.getGameActivity().getVertexBufferObjectManager());
		_hili.setColor(_gm.getGameActivity().res.COLOR[typ]);
		_hili.setAlpha(0);
		attachChild(_hili);
		
		_sprite = new Sprite(0, 0,
				_gm.getGameActivity().res.pieceTR, _gm.getGameActivity().getVertexBufferObjectManager());
		_sprite.setColor(_gm.getGameActivity().res.COLOR[typ]);
		attachChild(_sprite);
		
		this.setScaleCenter(Config.PIECE_WIDTH / 2, Config.PIECE_HEIGHT / 2);
	}
	
	public int getTyp() {
		return _typ;
	}
	
	@Override
	public void updateSelect() {
		;
	}

	@Override
	public void onTouchBegan() {
		if (_gm.isGamePausedOrEnded())
			return;
		
		if (mPiece.status != Piece.S_ONBOARD)
			return;
		if (! mPiece.isSelected()) {
			boolean r = _gm.select(mPiece);
			if (! r) {
				_gm.clearSelectedPieces();
				_gm.updateSelectedPieces();
				onTouchBegan();
			}
		}
	}

	@Override
	public void onTouchMoved() {
		if (_gm.isGamePausedOrEnded())
			return;

		if (mPiece.status != Piece.S_ONBOARD)
			return;

		if (! mPiece.isSelected()) {
			boolean r = _gm.select(mPiece);
			if (! r) {
				_gm.findSelectPath(mPiece);
			}
			_gm.updateSelectedPieces();
		} else {
			boolean r = _gm.select(mPiece);
			if (r) {
				_gm.updateSelectedPieces();
			}
		}
	}

	@Override
	public void destroy() {
		if (_destroyed)
			return;
		_destroyed = true;
		if (_hiliMod != null) {
			_hili.unregisterEntityModifier(_hiliMod);
		}
		_gm.getGameActivity().getEngine().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				_bkg.detachSelf();
				_bkg.dispose();
				
				_sprite.detachSelf();
				_sprite.dispose();
				
				_hili.detachSelf();
				_hili.dispose();
				
				dispose();		
			}
		});
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
		if (_hiliMod != null) {
			_hili.unregisterEntityModifier(_hiliMod);
			_hiliMod = null;
		}
		_hili.setAlpha(0);
		_sprite.registerEntityModifier(new ParallelEntityModifier(
				new AlphaModifier(0.3f, 1, 0),
				new ScaleModifier(0.3f, 1, 2)
				) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				destroy();
			}
		});
	}
	
	@Override
	public void excite() {
		if (_hiliMod != null)
			_hili.unregisterEntityModifier(_hiliMod);
		_hiliMod = new ParallelEntityModifier(
				new AlphaModifier(0.5f, 1, 0),
				new ScaleModifier(0.5f, 2f, 1f)
				);
		_hili.registerEntityModifier(_hiliMod);
	}

	@Override
	public void stale() {
		Color c = _sprite.getColor();
		_sprite.registerEntityModifier(new ColorModifier(0.4f, c, new Color(0.6f, 0.6f, 0.6f)));
	}
	
	@Override
	public void setFocus(boolean f) {
		if (f) {
			if (_dotMod != null)
				_sprite.unregisterEntityModifier(_dotMod);
			_dotMod = new ScaleModifier(0.1f, 1, 0.5f);
			_sprite.registerEntityModifier(_dotMod);
			
			if (_hiliMod != null)
				_hili.unregisterEntityModifier(_hiliMod);
			
			_hiliMod = new ParallelEntityModifier(
					new AlphaModifier(0.3f, 0, 1),
					new ScaleModifier(0.3f, 1f, 1.1f)
					);
			_hili.registerEntityModifier(_hiliMod);
		} else {
			_sprite.unregisterEntityModifier(_dotMod);
			_dotMod = new ScaleModifier(0.1f, 0.5f, 1);
			_sprite.registerEntityModifier(_dotMod);

			_hili.unregisterEntityModifier(_hiliMod);
			_hiliMod = new ParallelEntityModifier(
					new AlphaModifier(0.05f, 1, 0),
					new ScaleModifier(0.05f, 1.1f, 1f)
					);
			_hili.registerEntityModifier(_hiliMod);
		}
	}

}
