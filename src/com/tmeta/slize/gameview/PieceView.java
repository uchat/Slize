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

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;

import com.tmeta.slize.board.Piece;


public abstract class PieceView extends Entity {
	protected Piece mPiece;
	protected int _z;
	public float y;
	protected PieceView _replace;
	
	private MoveModifier _modifier;
	
	public interface PieceMoveListener {
		void onMoveFinished(PieceView pv);
	}
	
	public void updateSelect() {
		;
	}
	
	public PieceView getReplacePiece() {
		return _replace;
	}
	
	public void setReplacePiece(PieceView replace) {
		_replace = replace;
	}
	
	public Piece getGamePiece() {
		return mPiece;
	}
	
	public void move(float x, float y, final PieceMoveListener l) {
		if (_modifier != null) {
			this.unregisterEntityModifier(_modifier);
		}
		_modifier = new MoveModifier(0.15f, this.getX(), x, this.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				if (l != null)
					l.onMoveFinished(PieceView.this);
			}
		};
		this.registerEntityModifier(_modifier);
	}
	
	public void move(float x, float y) {
		if (_modifier != null) {
			this.unregisterEntityModifier(_modifier);
		}
		_modifier = new MoveModifier(0.05f, this.getX(), x, this.getY(), y);
		this.registerEntityModifier(_modifier);
	}
	
	public void destroy() {
		;
	}
	
	public abstract void onTouchBegan();
	public abstract void onTouchMoved();
	public abstract void onTouchEnded();
	public abstract void hide();
	public abstract void excite();
	public abstract void stale();
	public abstract void setFocus(boolean f);
}
