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

import java.util.ArrayList;
import java.util.List;

import com.tmeta.slize.Config;

public class Board {
	private Piece[][] _board;
	private ArrayList<Piece> _selectList;
	private ArrayList<Piece> _emptyPieces;
	
	/* for A* pathfinder */
	private ArrayList<Piece> openList = new ArrayList<Piece>();
	private ArrayList<Piece> closedList = new ArrayList<Piece>();
	
	public Board() {
		_board = new Piece[Config.BOARD_WIDTH][Config.BOARD_HEIGHT];
		_selectList = new ArrayList<Piece>();
		_emptyPieces = new ArrayList<Piece>();
	}

	public void trimSelect(Piece gp) {
		if (! gp.isSelected() || _selectList.size() == 0)
			return;
		Piece p = _selectList.get(0);
		while (p != gp) {
			_selectList.remove(0);
			if (_selectList.size() == 0)
				break; // should not happen
			p = _selectList.get(0);
		}
	}
	
	public Piece getGamePiece(int x, int y) {
		return _board[x][y];
	}
	
	public void removeGamePiece(Piece gp) {
		_board[gp.getX()][gp.getY()] = null;
	}
	
	public void setGamePiece(Piece gp, int x, int y) {
		gp.setX(x);
		gp.setY(y);
		_board[x][y] = gp;
		if (gp.isEmpty()) {
			_emptyPieces.add(gp);
		}
	}
	
	public ArrayList<Piece> getEmptyPieces() {
		return this._emptyPieces;
	}
	
	public boolean select(Piece gp) {
		int s = _selectList.size();
		if (s == 0) {
			_selectList.add(gp);
			gp.setSelected(true);
			return true;
		}
		
		if (gp.isSelected()) {
			// cut selectLIst at gp (include gp in result)
			if (gp == _selectList.get(s - 1)) {
				return false;
			}
			ArrayList<Piece> s1 = new ArrayList<Piece>();
			boolean cut = false;
			for (Piece gp2 : _selectList) {
				if (cut) {
					gp2.setSelected(false);
					continue;
				}
				s1.add(gp2);
				if (gp2 == gp)
					cut = true;
			}
			_selectList = s1;
			return true;
		}
		
		Piece gpn = _selectList.get(s - 1);
		boolean test = (gpn.getX() == gp.getX() && Math.abs(gpn.getY() - gp.getY()) == 1)
				|| (gpn.getY() == gp.getY() && Math.abs(gpn.getX() - gp.getX()) == 1);
		if (test) {
			_selectList.add(gp);
			gp.setSelected(true);	
			return true;
		}
		return false;
	}
		
	public boolean isMoveValid(Piece ep) {
		int s = _selectList.size();
		if (s == 0) {
			return false;
		}
		Piece gpn = _selectList.get(s - 1);
		if (gpn.getX() == ep.getX() && Math.abs(gpn.getY() - ep.getY()) == 1) {
		    return true;
		}
		if (gpn.getY() == ep.getY() && Math.abs(gpn.getX() - ep.getX()) == 1) {
		    return  true;
		}
		return false;
	}
	
	public void swap(Piece gp1, Piece gp2) {
		int x1 = gp1.getX();
		int y1 = gp1.getY();
		gp1.setX(gp2.getX());
		gp1.setY(gp2.getY());
		gp2.setX(x1);
		gp2.setY(y1);
		_board[gp1.getX()][gp1.getY()] = gp1;
		_board[gp2.getX()][gp2.getY()] = gp2;
	}
	
	public List<Piece> getSelectList() {
		return _selectList;
	}
	
	public void clearSelectList() {
		for (Piece gp : _selectList) {
			gp.setSelected(false);
		}
		_selectList.clear();
	}
	
	public void findSelectPath(Piece to) {		
		Piece from = _selectList.get(_selectList.size() - 1);
		findPath(from, to);
		
		if (to.aParent == null) // no path here
			return;
		

		Piece gp = to;
		int s = _selectList.size();
		while (gp != null) {
			if (gp.aParent == null) {
				break;
			}
			_selectList.add(s, gp);
			gp.setSelected(true);
			gp.getPieceView().updateSelect();
			gp = gp.aParent;
		}
	}
	
	public void findPath(Piece from, Piece to) {
		// assume from, to are gem piece
		from.aParent = null;
		to.aParent = null;
		
		openList.clear();
		closedList.clear();
		
		openList.add(from);
		_findPath(from, to);

	}
	
	private void _findPath(Piece from, Piece to) {
		
		if (from == to || openList.size() == 0)
			return;
		
		openList.remove(from);
		closedList.add(from);
		
		// find adjacent node
		_addAdjacent(from, from._x, from._y - 1, to);
		_addAdjacent(from, from._x + 1, from._y, to);
		_addAdjacent(from, from._x, from._y + 1, to);
		_addAdjacent(from, from._x - 1, from._y, to);
		
		
		int lowest = Integer.MAX_VALUE;
		Piece selected = null;
		for (Piece gp : openList) {
			if (gp.aF <= lowest) {
				lowest = gp.aF;
				selected = gp;
			}
		}
		_findPath(selected, to);
	}
	
	private void _addAdjacent(Piece gp, int x, int y, Piece to) {
		if (x < 0 || x > Config.BOARD_WIDTH - 1)
			return;
		if (y < 0 || y > Config.BOARD_HEIGHT - 1)
			return;
		Piece adj = _board[x][y];
		if (adj == null)
			return;
		if (adj.status != Piece.S_ONBOARD) 
			return;
		if (closedList.contains(adj))
			return;
		if (! adj.isPathable())
			return;
		if (adj.isSelected())
			return;
		if (! openList.contains(adj)) {
			openList.add(adj);
			adj.aParent = gp;
			adj.aG = gp.aG + 10;
			adj.aH = findH(adj, to);
			adj.aF = adj.aG + adj.aH;
		} else {
			int newG = gp.aG + 10;
			if (newG < adj.aG) {
				adj.aParent = gp;
				adj.aG = newG;
				adj.aF = adj.aG + adj.aH;
			}
		}
	}
	
	private int findH(Piece node, Piece to) {
		int hz = Math.abs(node._x - to._x);
		int vt = Math.abs(node._y - to._y);
		return (hz + vt) * 10;
	}
	
}




