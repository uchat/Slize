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

package com.tmeta.slize;

import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;

import com.tmeta.slize.award.AwardCenter;
import com.tmeta.slize.award.IAward;
import com.tmeta.slize.board.Board;
import com.tmeta.slize.board.Piece;
import com.tmeta.slize.booster.IBoosterExecutable;
import com.tmeta.slize.booster.IBoosterExecutable.BoosterStatus;
import com.tmeta.slize.gameview.BoardView;
import com.tmeta.slize.gameview.EmptyPieceView;
import com.tmeta.slize.gameview.GameKeeper;
import com.tmeta.slize.gameview.MoveLeftBar;
import com.tmeta.slize.gameview.PieceView;
import com.tmeta.slize.gameview.TimeBar;
import com.tmeta.slize.gameview.GameKeeper.TimeListener;

import android.content.Context;
import android.os.Vibrator;
import android.util.SparseArray;


public class GameManager implements IUpdateHandler {
	
	protected static final int GAME_WAIT = 0;
	protected static final int GAME_RUN = 1;
	protected static final int GAME_TIME_EXPIRED = 2;
	protected static final int GAME_BOOSTER_LUNCH = 3;
	protected static final int GAME_BOOSTER_RUNNING = 4;

	private static final int TICK_20M = 20;
	private static final int TICK_60 = 60;
	private static final int TICK_120 = 120;
	
	protected GameActivity _game;
	protected Board _gb;
	protected BoardView _bv;
	protected boolean _started, _selectAllowed, _staling, _didMove;
	protected boolean egem = false; 
	protected int _gameState, _savedState;
	protected int _score, _mode, _moveCount;
	
	protected SparseArray<ArrayList<Piece>> _fallingPieces;
	protected int[] _pieceInColumn;
	protected int[] _timeInColumn;
	
	protected GameKeeper _gameKeeper;
	protected float _dt;
	protected int _endScreenDelay, _clearExplosionDelay;
	
	protected GemFactory gemFactory;	
	protected Vibrator _vibrator;
	
	protected IGameEventListener _gs;
	protected Scene _scene;	
	protected int bucketKillLimit = 5;
	
	AwardCenter _awardCenter;
	IBoosterExecutable _booster;
	
	protected GameManager() {
		;
	}
	
	public GameManager(GameActivity game, Scene scene, 
			IGameEventListener gs, int mode, int boardWidth, int boardHeight) {
		_game = game;
		_started = false;
		_staling = false;
		_didMove = false;
		_score = 0;
		_gs = gs;
		_mode = mode;
		
		_vibrator = (Vibrator) _game.getSystemService(Context.VIBRATOR_SERVICE);
		
		_scene = scene;
		
		_awardCenter = _game.awardCenter;
		_awardCenter.initNewRound(_mode);
		
		TimeListener tl = new TimeListener() {
			@Override
			public void onTimeExpired() {
				_doTimeExpired();
			}

			@Override
			public void onTick(int tickLeft) {
				_gs.onTick(tickLeft);
			}

			@Override
			public void onTickAdded(float tickAdded) {
				_gs.onTickAdded(tickAdded);				
			}
		};
		
		if (mode == Config.MODE_20M) {
			_gameKeeper = new MoveLeftBar(_game, tl);
			_gameKeeper.setFulltick(TICK_20M, Config.WARNING_TIME);
		} else {
			_gameKeeper = new TimeBar(_game, tl);
			int t = 0;
			if (_mode == Config.MODE_60S)
				t = TICK_60;
			if (_mode == Config.MODE_120S)
				t = TICK_120;
			_gameKeeper.setFulltick(t, Config.WARNING_TIME);
		}
		
		_gameState = GAME_WAIT;
		_savedState = _gameState;
		_endScreenDelay = 0;
		_clearExplosionDelay = 0;
		
		_selectAllowed = false;
		
		gemFactory = new GemFactory();
		_bv = new BoardView(this, boardWidth, boardHeight);
	}
	
	private void _doTimeExpired() {
		_gameState = GAME_TIME_EXPIRED;
		clearSelectedPieces();
	}

	public BoardView getBoardView() {
		return _bv;
	}
	
	public void createNewBoard() {
		_bv.createNewBoard();
		
		_pieceInColumn = new int[Config.BOARD_WIDTH];
		_timeInColumn = new int[Config.BOARD_WIDTH];
		_fallingPieces = new SparseArray<ArrayList<Piece>>();
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
		    _pieceInColumn[x] = 0; 
		    _timeInColumn[x] = 0;
		    _fallingPieces.put(x, new ArrayList<Piece>());
		    
		    int c = 0;
		    for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
		    	PieceView gp = _bv.getPieceView(x, y);
		    	if (gp != null) {
		    		c++;
		    	}
		    }
		    _pieceInColumn[x] = c;
		}
		
		_scene.registerUpdateHandler(this);
		
		_started = true;
		_gameState = GAME_RUN;
		_savedState = GAME_RUN;
		_dt = 0;
		_score = 0;
	}
	
	public void addScore(int p) {
		_score += p;
		_gs.onScoreChanged(_score);
	}

	public GameKeeper getGameKeeper() {
		return _gameKeeper;
	}
	
	public void setBoardView(BoardView bv) {
		_bv = bv;
		_gb = bv.getGameBoard();
	}
	
	public GameActivity getGameActivity() {
		return _game;
	}
	
	public boolean isGamePausedOrEnded() {
		return _gameState == GAME_WAIT || _gameState == GAME_TIME_EXPIRED;
	}


	public boolean isMoveValid(Piece gp) {
		return _gb.isMoveValid(gp);
	}
	
	public boolean select(Piece gp) {
		return _gb.select(gp);
	}
	
	public void findSelectPath(Piece gp) {
		_gb.findSelectPath(gp);
	}
	
	
	public void moveSelectedPieces(PieceView ep) {
		List<Piece> sl = _gb.getSelectList();
		int sz = sl.size();
		if (sz > 0)
			_didMove = true;
		for (int i = sz - 1; i >= 0; i--) {
			PieceView pv = sl.get(i).getPieceView();
			_bv.move(pv,  ep);
		}
		_bv.drawBorder();
		_showExcite();
	}
	
	public void clearSelectedPieces() {
		_gb.clearSelectList();
		_bv.drawBorder();
	}
	
	public void updateSelectedPieces() {
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				PieceView pv = _bv.getPieceView(x, y);
				if (pv != null) {
					pv.updateSelect();
				}
			}
		}
		
		_bv.drawBorder();
	}

	protected void _doCheck(int x, int y, int typ, boolean[][] checked, ArrayList<Piece> group, boolean trim) {
		if (x < 0 || x >= Config.BOARD_WIDTH || y < 0 || y >= Config.BOARD_HEIGHT) 
			return;
		if (checked[x][y])
			return;
		Piece gp = _gb.getGamePiece(x, y);
		if (gp == null || gp.isEmpty() || gp.status != Piece.S_ONBOARD)
			return;
		if (gp.getTyp() != typ)
			return;
		checked[x][y] = true;
		group.add(gp);
		
		_doCheck(x, y - 1, typ, checked, group, trim);
		_doCheck(x + 1, y, typ, checked, group, trim);
		_doCheck(x, y + 1, typ, checked, group, trim);
		_doCheck(x - 1, y, typ, checked, group, trim);
		
		if (trim)
			_gb.trimSelect(gp);
	}

	
	public void check() {
		if (_gameState != GAME_RUN)
			return;
		
		clearSelectedPieces();
		
		boolean[][] checked = new boolean[Config.BOARD_WIDTH][Config.BOARD_HEIGHT];
		ArrayList<ArrayList<Piece>> groups = new ArrayList<ArrayList<Piece>>();
		for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
			for (int x = 0; x < Config.BOARD_WIDTH; x++) {
				Piece gp = _gb.getGamePiece(x, y);
				if (gp == null) 
					continue;
				gp.getPieceView().updateSelect();
				if (gp.status != Piece.S_ONBOARD) 
					continue;
				if (! gp.isEmpty()) {
					if (checked[x][y])
						continue;
					ArrayList<Piece> group = new ArrayList<Piece>();
					_doCheck(x, y, gp.getTyp(), checked, group, true);
					if (group.size() >= Config.WIN_LENGTH) {
						groups.add(group);
					}
				}
			}
		}
		
		if (groups.size() > 0)
			this._awardCenter.checkInGameAward(groups);
		
		Sound s = _game.sound.DROP;
		for (ArrayList<Piece> group : groups) {
			int size = group.size();
			if (size == 0)
				continue;
			int typ = group.get(0).getTyp();
			for (Piece gp : group) {
				_bv.markRemove(gp);  
				addScore(1);
				_runRemoveEffect(gp); 
			}
			if (size == 5) {
				addScore(2);
				_gs.onBonusTrigger(2);
			}
			if (size >= Config.COMBO_LENGTH) {
				removeAllTyp(typ);
				addScore(5);
				s = _game.sound.DROP6;
				_gs.onBonusTrigger(5);
			}
			_game.sound.play(s);
		}
	}
	
	
	private void _showExcite() {
		boolean[][] checked = new boolean[Config.BOARD_WIDTH][Config.BOARD_HEIGHT];
		int typ = -1;
		for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
			for (int x = 0; x < Config.BOARD_WIDTH; x++) {
				Piece gp = _gb.getGamePiece(x, y);
				if (gp == null) 
					continue;
				if (gp.status != Piece.S_ONBOARD) 
					continue;
				if (! gp.isEmpty()) {
					if (checked[x][y])
						continue;
					ArrayList<Piece> group = new ArrayList<Piece>();
					_doCheck(x, y, gp.getTyp(), checked, group, false);
					int size = group.size();
					if (size >= Config.WIN_LENGTH) {
						if (size >= Config.COMBO_LENGTH) {
							typ = group.get(0).getTyp();
							exciteAllTyp(typ);
						} else {
							for (Piece p : group) {
								p.getPieceView().excite();
							}
						}
					}
				}
			}
		}
	}
	
	public void exciteAllTyp(int typ) {
		_game.vibrate(60);
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				Piece gp = _gb.getGamePiece(x, y);
				if (gp == null || gp.isEmpty() || gp.status != Piece.S_ONBOARD)
					continue;
				if (gp.getTyp() == typ) {
					gp.getPieceView().excite();
				}
			}
		}
	}
	
	public void removeAllTyp(int typ) {
		_bv.hili(typ);
		_game.vibrate(150);
		
		ArrayList<Piece> ps = new ArrayList<Piece>();
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				Piece gp = _gb.getGamePiece(x, y);
				if (gp == null || gp.isEmpty() || gp.status != Piece.S_ONBOARD)
					continue;
				if (gp.getTyp() == typ) {
					ps.add(gp);
				}
			}
		}
		
		int total = ps.size();		
		addScore(total);
		gemFactory.setRestrict(typ, total * 3);
		
		for (Piece p : ps) {
			_bv.markRemove(p);
			_runRemoveEffect(p);
		}
	}

	
	private void _stalingPieces() {
		if (_staling)
			return;
		_staling = true;
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
				Piece gp = _gb.getGamePiece(x, y);
				if (gp == null)
					continue;
				gp.getPieceView().stale();
			}
		}
		_game.sound.play(_game.sound.LOST);
	}
	
	public void onTouchBegan(float x, float y) {
		if ((! _selectAllowed) || _gameState != GAME_RUN)
			return;

		PieceView pv = _getPieceViewTouched(x, y);
		if (pv == null)
			return;
		
		if (_gameState == GAME_BOOSTER_RUNNING) {
			_booster.onTouchBegan(pv);
			return;
		}
			
		pv.onTouchBegan();
	}
	
	public void onTouchMoved(float x, float y) {
		PieceView pv = _getPieceViewTouched(x, y);
		if (pv == null)
			return;
		
		if (_gameState == GAME_BOOSTER_RUNNING) {
			_booster.onTouchMoved(pv);
			return;
		}
		
		if (pv.getGamePiece().status == Piece.S_ONBOARD) {
			pv.onTouchMoved();
		}
	}
	
	public void onTouchEnded(float x, float y) {
		PieceView pv = _getPieceViewTouched(x, y);
		if (pv == null) {
			clearSelectedPieces();
			updateSelectedPieces();
			return;
		}
		
		if (_gameState == GAME_BOOSTER_RUNNING) {
			_booster.onTouchEnded(pv);
			return;
		}
		
		pv.onTouchEnded();
		if (_didMove) {
			_moveCount++;
			_gameKeeper.offbeatUpdate();
			_didMove = false;
		}
	}
	
	public int getScore() {
		return _score;
	}
	
	public int getMoveCount() {
		return _moveCount;
	}
	
	public void runGameEndEffect() {
		_bv.setRotationCenter(Config.SCREEN_WIDTH / 2, Config.PIECE_HEIGHT * Config.BOARD_HEIGHT / 2);
		_bv.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(
						new RotationModifier(0.2f, -2, 2),
						new RotationModifier(0.2f, 2, -2))));		
	}

	
	public void pauseGame() {
		_savedState = _gameState;
		_gameState = GAME_WAIT;
		_scene.unregisterUpdateHandler(this);
	}
	
	
	public void resumeGame() {
		_gameState = _savedState;
		_scene.registerUpdateHandler(this);
	}

	
	public final boolean isGameEnded() {
		return _gameState == GAME_TIME_EXPIRED;
	}


	public List<IAward> getAwardList() {
		return this._awardCenter.getAwardWon();
	}
	
	protected void _addPiece() {
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			if (_pieceInColumn[x] + _fallingPieces.get(x).size() < Config.BOARD_HEIGHT && _timeInColumn[x] >= 8) {
				PieceView pv = null;
				pv = gemFactory.create(this);
			    pv.setPosition(x * Config.PIECE_WIDTH, -1 * Config.PIECE_HEIGHT);
			    pv.y = -1;
			    _fallingPieces.get(x).add(pv.getGamePiece());
			    _bv.attachChild(pv);
			    _timeInColumn[x] = 0;
			}
			_timeInColumn[x]++;
		}
		_bv.sortChildren();
	}
	
	private int _findStopMark(int col) {
		int ret = Config.BOARD_HEIGHT;
		boolean nullFound = false;
		for (int y =  Config.BOARD_HEIGHT - 1; y >= 0; y--) {
			Piece gp = this._gb.getGamePiece(col, y);
			if (gp != null) {
				if (! nullFound) {
					ret = y;
				}
			} else {
				nullFound = true;
			}
		}
		return ret - 1;
	}
	
	protected boolean _moveColumnPieces() {
		int falling = 0;
		boolean tocheck = false;
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			ArrayList<Piece> column = _fallingPieces.get(x);
			falling += column.size();
			ArrayList<Piece> done = new ArrayList<Piece>();
			
			for (Piece gp : column) {
				PieceView pv = gp.getPieceView();
				pv.y += Config.DROP_SPEED;
				
				int stopMark = _findStopMark(x);
				if (stopMark >= 0 && pv.y >= stopMark) {
					done.add(gp);
					_gb.setGamePiece(gp, x, stopMark);
					_bv.settle(pv);
					falling--;
					_pieceInColumn[x]++;
					tocheck = true;
					if (_staling)
						gp.getPieceView().stale();
					_game.sound.play(_game.sound.TACK);
				} else {
					pv.setPosition(x * Config.PIECE_WIDTH, pv.y * Config.PIECE_HEIGHT);
				}
			}

			for (Piece gp : done) {
				column.remove(gp);
			}
		}
		if (tocheck) {
			check();
		}
		return falling != 0;
	}
	
	
	protected boolean _markFallingPieces() {
		boolean ret = false;
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
		    for (int y = 0; y < Config.BOARD_HEIGHT; y++) {
		    	Piece gp = _gb.getGamePiece(x, y);
		    	if (gp == null)
		    		continue;
		    	if (gp.delayFrame > 0) {
		    		ret = true;
		    		gp.delayFrame--;
		    		if (gp.delayFrame == 0) {
		    			_bv.remove(gp);
		    			_pieceInColumn[x]--;
		    			PieceView replace = gp.getPieceView().getReplacePiece();
		    			if (replace != null) {
		    				replace.setScaleCenter(Config.PIECE_WIDTH / 2, Config.PIECE_HEIGHT / 2);
		    				replace.registerEntityModifier(new ScaleModifier(0.2f, 0, 1));
		    				_gb.setGamePiece(replace.getGamePiece(), gp.getX(), gp.getY());
		    				_bv.attachPiece(replace);
		    				_pieceInColumn[x]++;
		    			}
		    			for (int ya = y - 1; ya >= 0; ya--) {
		    				Piece gpa = _gb.getGamePiece(x, ya);
		    				if (gpa == null || gpa.isEmpty() || ! gpa.isMovable() || gpa.isExplosion())
		    					continue;
		    				if (gpa.delayFrame > 0) {
		    					_bv.remove(gpa);
		    					this._pieceInColumn[x]--;
		    					continue;
		    				}
		    				this._pieceInColumn[x]--;
		    				_bv.markFalling(gpa);
		    				this._fallingPieces.get(x).add(gpa);
		    			}
		    		}
		    	}
		    }
		}
		return ret;
	}

	private boolean _allSettled() {
		for (int x = 0; x < Config.BOARD_WIDTH; x++) {
			if (_pieceInColumn[x] < Config.BOARD_HEIGHT) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onUpdate(float dt) {
		if (! _started)
			return;
		boolean marked = this._markFallingPieces();
		this._addPiece();
		boolean falling = this._moveColumnPieces();
		_selectAllowed = (! (falling || marked));
		if (_gameState == GAME_TIME_EXPIRED) {
			_gameKeeper.pause();
			if (! falling && ! marked) {
				_stalingPieces();
				_endScreenDelay++;
				if (_endScreenDelay == 20) {
					_scene.unregisterUpdateHandler(this);
					_game.scoreCenter.recordGamePlayed(_mode);
					_game.scoreCenter.recordScore(_mode,  _score);
					int numplay = _game.scoreCenter.getNumPlayAll();
					this._awardCenter.checkGameEndAward(_score, _moveCount, numplay);
					_gs.onTimeExpired();
				}
			} else {
				_endScreenDelay = 0;
			}
		}
		
		if (_selectAllowed && _gameState == GAME_BOOSTER_LUNCH) {
			_selectAllowed = false;
			if (_allSettled()) {
				_gameState = GAME_BOOSTER_RUNNING;
				_booster.start();
			}
		}
		
		_dt += dt;
		if (_dt >= 1) {
			_gameKeeper.freqUpdate(_dt);
			_dt = 0;
		}
	}

	public void runBooster(IBoosterExecutable booster) {
		_booster = booster;
		_booster.setClient(new IBoosterExecutable.IBoosterClient() {
	
			@Override
			public void execResult(IBoosterExecutable booster, BoosterStatus status) {
				if (status == IBoosterExecutable.BoosterStatus.WAIT)
					return;
				
				if (status == IBoosterExecutable.BoosterStatus.EXEC) {
					booster.execAction();
					_booster = null;
					_gs.onBoosterFinished(booster);
				}
				
				if (status == IBoosterExecutable.BoosterStatus.EXEC) {
					_gameState = GAME_RUN;
				}
			}
			
		});
		
		_gameState = GAME_BOOSTER_LUNCH;
		// booster will be run when no pieces are falling
	}

	protected void _runRemoveEffect(Piece gp) {
		if (gp.getTyp() < 0)
			return;
		
		final PieceView pv = gp.getPieceView();
		pv.hide();
	}
	
	
	private PieceView _getPieceViewTouched(float x, float y) {
		int bx = (int) Math.floor((x - _bv.getX()) / Config.PIECE_WIDTH);
		int by = (int) Math.floor((y - _bv.getY()) / Config.PIECE_HEIGHT);

		if (bx >= Config.BOARD_WIDTH || bx < 0 || by >= Config.BOARD_HEIGHT || by < 0)
		    return null;
		return _bv.getPieceView(bx, by);
	}

	public void replaceWithEmtpy(PieceView pv) {
		EmptyPieceView ep = new EmptyPieceView(this);
		_bv.markRemove(pv.getGamePiece(), ep);
		_runRemoveEffect(pv.getGamePiece()); 
	}
	
	public void swap(PieceView pv1, PieceView pv2) {
		_bv.swap(pv1, pv2);
	}
	
	public void addTime(float v) {
		_gameKeeper.addTick(v);
	}
	
	public void destroy() {
		_gameKeeper.dispose();
		_bv.dispose();
		
		if (_fallingPieces != null) {
			for (int x = 0; x < Config.BOARD_WIDTH; x++) {
				ArrayList<Piece> ps = _fallingPieces.get(x);
				for (Piece p : ps) {
					PieceView pv = p.getPieceView();
					pv.destroy();
				}
			}
		}
	}

	@Override
	public void reset() {
		;
	}

}
