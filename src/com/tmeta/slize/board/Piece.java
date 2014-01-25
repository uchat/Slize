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

package com.tmeta.slize.board;

import com.tmeta.slize.gameview.PieceView;


public class Piece {
	public static final int S_OFFBOARD = 0;
	public static final int S_ONBOARD = 1;
	public static final int S_FALLING = 2;
	public static final int S_REMOVING = 3;
	
	public static final int TYP_EMPTY = -1;
	public static final int TYP_VERT_EXPL = -4;
	public static final int TYP_T_EXPL = -5;

	
	protected int /* _status, _delayFrame,*/ _typ, _x, _y;
	private boolean _selected, _movable, _empty, _star, _explosion, _pathable;
	
	public int delayFrame, status;
	
	private PieceView _pv;

	/* for a* path finding */
	protected Piece aParent;
	protected int aG, aH, aF;
	
	public Piece(int typ) {
		_typ = typ;
		status = S_OFFBOARD;
	}
	

	public int getTyp() {
		return _typ;
	}

	public int getX() {
		return _x;
	}

	public void setX(int _x) {
		this._x = _x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int _y) {
		this._y = _y;
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean _selected) {
		this._selected = _selected;
	}
	
	public boolean isExplosion() {
		return _explosion;
	}
	
	public boolean isPathable() {
		return _pathable;
	}
	
	public void setPathable(boolean p) {
		_pathable = p;
	}

	public void setExplosion(boolean ex) {
		_explosion = ex;
	}
	
	public boolean isMovable() {
		return _movable;
	}

	public void setMovable(boolean _movable) {
		this._movable = _movable;
	}

	public boolean isEmpty() {
		return _empty;
	}

	public void setEmpty(boolean _empty) {
		this._empty = _empty;
	}

	public boolean isStar() {
		return _star;
	}

	public void setStar(boolean _star) {
		this._star = _star;
	}



	public PieceView getPieceView() {
		return _pv;
	}

	public void setPieceView(PieceView _pv) {
		this._pv = _pv;
	}
	
	public void cleanUp() {
//		_pv = null;
	}
	
}
