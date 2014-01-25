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

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameActivity;
import com.tmeta.slize.GameManager;
import com.tmeta.slize.board.Board;
import com.tmeta.slize.board.Piece;


public class BoardView extends Entity {

	protected GameManager _gm;
	protected GameActivity _game;
	protected Board _gb;
	protected BorderPool _borderPool;
	
	protected ArrayList<Entity> _entities;
	protected Rectangle _bkgRect;
	protected ArrayList<Sprite> _borders;
	protected int _boardHeight, _boardWidth;
		
	protected BoardView() {
		
	}
	
	public BoardView(GameManager gm, int boardWidth, int boardHeight) {
		_gm = gm;
		_game = gm.getGameActivity();
		_gb = new Board();
		_gm.setBoardView(this);
		_entities = new ArrayList<Entity>();
		_borders = new ArrayList<Sprite>();	
		_boardHeight = boardHeight;
		_boardWidth = boardWidth;
		
		_bkgRect = new Rectangle(0, 0, 
				Config.PIECE_WIDTH *  boardWidth,
				Config.PIECE_HEIGHT * boardHeight + 40,
				_game.getVertexBufferObjectManager());
		_bkgRect.setColor(1, 1, 1);
		
		attachChild(_bkgRect);
		_entities.add(_bkgRect);
		
		for (int x = 0; x < _boardWidth; x++) {
			float m = Config.PIECE_HEIGHT / 2;
			float ax = x * Config.PIECE_WIDTH + m;
			Line l = new Line(ax , m, ax, Config.PIECE_HEIGHT * boardHeight - m, 2, 
					_game.getVertexBufferObjectManager());
			l.setColor(0.8f, 0.8f, 0.8f);
			attachChild(l);
			_entities.add(l);
		}
		
		for (int y = 0; y < boardHeight; y++) {
			float m = Config.PIECE_WIDTH / 2;
			float ay = y * Config.PIECE_HEIGHT + m;
			Line l = new Line(m, ay, Config.PIECE_WIDTH * boardWidth - m, ay, 2, 
					_game.getVertexBufferObjectManager());
			l.setColor(0.8f, 0.8f, 0.8f);
			attachChild(l);
			_entities.add(l);
		}
		
		_borderPool = new BorderPool(_game);
	}
	
	public float getWidth() {
		return _bkgRect.getWidth();
	}
	
	public float getHeight() {
		return _bkgRect.getHeight();
	}
	
	
	public void hili(int typ) {
		_bkgRect.registerEntityModifier(new ColorModifier(0.5f,
				_game.res.COLOR[typ], Color.WHITE
				));
	}
	
	public void createNewBoard() {

		for (int x = 0; x < _boardWidth; x++) {
			for (int y = 0; y < _boardHeight; y++) {
				PieceView pv = null;
				if (x == 3 && y == 3) {
					pv = new EmptyPieceView(_gm);
				} 				
				if (pv != null) {
					_gb.setGamePiece(pv.mPiece, x, y);
					attachPiece(pv);
				}
			}
		}
	}

	
	public void createNewBoardPreload(int[][] dots) {

		for (int x = 0; x < _boardWidth; x++) {
			for (int y = 0; y < _boardHeight; y++) {
				PieceView pv = null;
				int typ = dots[y][x];
				if (typ == -1) {
					pv = new EmptyPieceView(_gm);
				}  else {
					pv = new ColorPieceView(_gm, typ);
				}
				if (pv != null) {
					_gb.setGamePiece(pv.mPiece, x, y);
					attachPiece(pv);
				}
			}
		}
	}

		
	public void attachPiece(PieceView pv) {
		float x = pv.getGamePiece().getX();
		float y = pv.getGamePiece().getY();
		pv.setPosition(x * Config.PIECE_WIDTH, y * Config.PIECE_HEIGHT);
		pv.getGamePiece().status = Piece.S_ONBOARD;
		this.attachChild(pv);
	}
	
	
	public void remove(Piece gp) {
		_gb.removeGamePiece(gp);
		gp.status = Piece.S_OFFBOARD;
		gp.delayFrame = 0;		
	}
	
	
	public void markRemove(Piece gp) {
		gp.delayFrame = 8;
		gp.status = Piece.S_REMOVING;
	}
	

	public void markRemove(Piece gp, PieceView replace) {
		markRemove(gp);
		gp.getPieceView().setReplacePiece(replace);
	}
	
	public void markFalling(Piece gp) {
		_gb.removeGamePiece(gp);
		PieceView pv = gp.getPieceView();
		pv.y = gp.getY();
		gp.delayFrame = 0;
		gp.status = Piece.S_FALLING;
	}
	
	public void settle(PieceView pv) {
		float x = pv.getGamePiece().getX();
		float y = pv.getGamePiece().getY();
		pv.setPosition(x * Config.PIECE_WIDTH, y * Config.PIECE_HEIGHT);
		pv.getGamePiece().status = Piece.S_ONBOARD;
	}
	
	public PieceView getPieceView(int x, int y) {
		Piece gp = _gb.getGamePiece(x, y);
		return gp == null ? null : gp.getPieceView();
	}
	
	public void move(PieceView pv, PieceView ep) {
		if (pv.getGamePiece().status != Piece.S_ONBOARD)
			return;
		_gb.swap(pv.getGamePiece(), ep.getGamePiece());
		
		// move empty piece to avoid being touched
		ep.setPosition(ep.getGamePiece().getX() * Config.PIECE_WIDTH, ep.getGamePiece().getY() * Config.PIECE_HEIGHT);

		float nx = pv.getGamePiece().getX() * Config.PIECE_WIDTH;
		float ny = pv.getGamePiece().getY() * Config.PIECE_HEIGHT;
		pv.move(nx, ny);
	}
	
	public void swap(PieceView pv1, PieceView pv2) {
		if (pv1.getGamePiece().status != Piece.S_ONBOARD)
			return;
		if (pv2.getGamePiece().status != Piece.S_ONBOARD)
			return;
		
		_gb.swap(pv1.getGamePiece(), pv2.getGamePiece());

		float x1 = pv1.getGamePiece().getX() * Config.PIECE_WIDTH;
		float y1 = pv1.getGamePiece().getY() * Config.PIECE_HEIGHT;
		float x2 = pv2.getGamePiece().getX() * Config.PIECE_WIDTH;
		float y2 = pv2.getGamePiece().getY() * Config.PIECE_HEIGHT;
		pv1.move(x1, y1, null);
		pv2.move(x2, y2, new PieceView.PieceMoveListener() {
			@Override
			public void onMoveFinished(PieceView pv) {
				_gm.check();				
			}
		});
	}
	
	public void syncPieceView() {
	
	}
	
	public void drawBorder() {
		_gm.getGameActivity().getEngine().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				_doDrawBorder();
			}
		});
	}
	
	public void addNewSpace(int x, int y) {
		PieceView pv = new EmptyPieceView(_gm);
		_gb.setGamePiece(pv.mPiece, x, y);
		attachPiece(pv);
	}
	
	public void _doDrawBorder() {
		for (final Sprite s : _borders) {
			s.detachSelf();
			_borderPool.release(s);
		}
		_borders.clear();
		
		List<Piece> sl = _gb.getSelectList();
		Piece prev = null;
		int len = sl.size();
		for (int i = 0; i < len; i++) {
			Piece gp = sl.get(i);
			int x = gp.getX();
			int y = gp.getY();
			float sx = x * Config.PIECE_WIDTH;
			float sy = y * Config.PIECE_HEIGHT;
			Sprite s = null;
			Piece next = null;
			if (i + 1 < len) {
				next = sl.get(i + 1);
			}
			
			if (prev == null && next != null) {
				int d0 = gp.getY() - next.getY();
				int d1 = gp.getX() - next.getX();
				s =  new Sprite(sx, sy, _game.res.borderEndTR, 
						_game.getVertexBufferObjectManager());
				if (d0 == -1 && d1 == 0) {
					s.setRotation(180);
				}
				if (d0 == 0 && d1 == 1) {
					s.setRotation(-90);
				}
				if (d0 == 0 && d1 == -1) {
					s.setRotation(90);
				}
				if (d0 == 1 && d1 == 0) {
					s.setRotation(0);
				}		
			}
			
			if (next == null && prev != null) {
				s =  new Sprite(sx, sy, _game.res.borderEndTR, 
						_game.getVertexBufferObjectManager());
				int d0 = prev.getY() - gp.getY();
				int d1 = prev.getX() - gp.getX();
				s =  new Sprite(sx, sy, _game.res.borderEndTR, 
						_game.getVertexBufferObjectManager());
				if (d0 == 0 && d1 == -1) {
					s.setRotation(-90);
				}
				if (d0 == 0 && d1 == 1) {
					s.setRotation(90);
				}
				if (d0 == 1 && d1 == 0) {
					s.setRotation(180);
				}
				
			}
			
			if (prev != null && next != null) {
				
				int d0 = prev.getY() - gp.getY();
				int d1 = gp.getY() - next.getY();
				int d2 = prev.getX() - gp.getX();
				int d3 = gp.getX() - next.getX();

				
				if ((d0 == 1 && d1 == 0 && d2 == 0 && d3 == -1) || (d0 == 0 && d1 == -1 && d2 == 1 && d3 == 0)) {
					s = new Sprite(sx, sy, _game.res.borderTurnTR, 
							_game.getVertexBufferObjectManager());
					s.setRotation(180);
				}
				if ((d0 == -1 && d1 == 0 && d2 == 0 && d3 == -1) || (d0 == 0 && d1 == 1 && d2 == 1 && d3 == 0)) {
					s = new Sprite(sx, sy, _game.res.borderTurnTR, 
							_game.getVertexBufferObjectManager());
					s.setRotation(90);
				}
				if ((d0 == 0 && d1 == -1 && d2 == -1 && d3 == 0) || (d0 == 1 && d1 == 0 && d2 == 0 && d3 == 1)) {
					s = new Sprite(sx, sy, _game.res.borderTurnTR, 
							_game.getVertexBufferObjectManager());
					s.setRotation(-90);
				}
				if ((d0 == 0 && d1 == 1 && d2 == -1 && d3 == 0) || (d0 == -1 && d1 == 0 && d2 == 0 && d3 == 1)) {
					s = new Sprite(sx, sy, _game.res.borderTurnTR, 
							_game.getVertexBufferObjectManager());
				}
				
				if ((d0 == 0 && d1 == 0 && d2 == -1 && d3 == -1) || (d0 == 0 && d1 == 0 && d2 == 1 && d3 == 1)) {
					s = new Sprite(sx, sy, _game.res.borderBarTR, 
							_game.getVertexBufferObjectManager());
					s.setRotation(90);
				}
				if ((d0 == -1 && d1 == -1 && d2 == 0 && d3 == 0) || (d0 == 1 && d1 == 1 && d2 == 0 && d3 == 0)) {
					s = new Sprite(sx, sy, _game.res.borderBarTR, 
							_game.getVertexBufferObjectManager());
				}
				
			}
			
			if (s != null) {
				s.setZIndex(10);
				_borders.add(s);
				this.attachChild(s);
			}
			prev = gp;
		}
		this.sortChildren();
	}
	
	public void destroy() {
		
		if (_entities != null) {
			for (Entity e : _entities) {
				e.detachSelf();
				e.dispose();
			}
			_entities.clear();
		}
	}

	public Board getGameBoard() {
		return _gb;
	}
	
}
