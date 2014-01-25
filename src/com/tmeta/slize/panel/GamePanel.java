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


package com.tmeta.slize.panel;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameManager;
import com.tmeta.slize.GameActivity;
import com.tmeta.slize.IGameEventListener;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.award.IAward;
import com.tmeta.slize.booster.Add5SecBooster;
import com.tmeta.slize.booster.Add5SecInfo;
import com.tmeta.slize.booster.AddEmptyBooster;
import com.tmeta.slize.booster.AddEmptyInfo;
import com.tmeta.slize.booster.IBoosterExecutable;
import com.tmeta.slize.booster.RemoveAllInfo;
import com.tmeta.slize.booster.SwapBooster;
import com.tmeta.slize.booster.RemoveAllBooster;
import com.tmeta.slize.booster.SwapInfo;
import com.tmeta.slize.gameview.BoardView;
import com.tmeta.slize.gameview.ButtonBar;
import com.tmeta.slize.gameview.GameKeeper;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.LabelValueBox;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class GamePanel extends Spanel implements IOnSceneTouchListener, IGameEventListener, LabelButtonListener {
	
	private final float BTT_BAR_TOP = 594;
	private final float BOARD_TOP = 100;
	private final float NUM_BOX = 40;
	
	protected GameActivity _game;
	protected Scene _scene;
	
	BoardView _bv;
	GameManager _gm;
	private boolean _isMouseDown, _isPaused;
	private float _tx, _ty;
	GameKeeper _timebar;
	private LabelValueBox _pointBox, _tickBox;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	int _mode;
	boolean _boosterClickAccept;
	
	ButtonBar _bttBar;
	LabelButton _bAdd5sec, _bRemoveAll, _bSwap, _bAddEmpty;
	
	Text _helpText;

	public GamePanel(int mode) {
		_mode = mode;
		_isPaused = false;
	}
	
	@Override
	public void create(GameActivity ga, Scene scene) {
		_game = ga;
		_scene = scene;
		_gm = new GameManager(ga, scene, this, _mode, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);
		_boosterClickAccept = true;
		
		_bv = _gm.getBoardView();
		_bv.setPosition((480 - _bv.getWidth()) / 2, -1000);

		
		String mode = _mode == Config.MODE_20M ? "move" : "time";
		_tickBox = new LabelValueBox(ga, mode);
		_tickBox.setNumLen(2);
		_tickBox.setPosition(15,  -100);
		_tickBox.setAsNum(0);
		attachChild(_tickBox);
		entities.add(_tickBox);
		
		_pointBox = new LabelValueBox(ga, "point");
		_pointBox.setNumLen(4);
		_pointBox.setPosition(280, -100);
		_pointBox.setAsNum(0);
		attachChild(_pointBox);
		entities.add(_pointBox);
		
		_timebar = _gm.getGameKeeper();
		_timebar.setPosition(0, 0);
		attachChild(_timebar);
		
		
		_bttBar = new ButtonBar(_game, scene, _bv.getX(), BTT_BAR_TOP, _bv.getWidth(), 100, this);
		_bAdd5sec = _bttBar.addButton(_game.res.add5TR, "0", 0.1f);
		_bAddEmpty = _bttBar.addButton(_game.res.addEmptyTR, "1", 0.1f);
		_bSwap = _bttBar.addButton(_game.res.swapTR, "2", 0.1f);
		_bRemoveAll = _bttBar.addButton(_game.res.removeAllTR, "3", 0.1f);
		_bttBar.finalize();
		_bttBar.setPosition((480 - _bttBar.getWidth()) / 2, 1200);
		attachChild(_bttBar);
		entities.add(_bttBar);
		
		this.attachChild(_bv); 

		_helpText = new Text(0, 560, ga.res.font28, "abcdefghijklmnopqrstuvwxyz",
				50, ga.getVertexBufferObjectManager());
		_helpText.setColor(0.5f, 0.5f, 0.5f);
		_helpText.setText("");
		_helpText.setScaleY(0);
		attachChild(_helpText);
		entities.add(_helpText);

		
		if (_mode == Config.MODE_20M)
			_bAdd5sec.setEnabled(false);
			
		
	}
	
	public void setHelpText(String str) {
		_helpText.setText(str);
		float w = _helpText.getWidth();
		_helpText.setPosition((480 - w) / 2, _helpText.getY());
		_helpText.registerEntityModifier(new ScaleModifier(0.2f, 1, 1, 0, 1));
	}
	
	public void clearHelpText() {
		_helpText.registerEntityModifier(new ScaleModifier(0.2f, 1, 1, 1, 0) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				_helpText.setText("");
			}
		});
	}

	public void start() {
		_boosterClickAccept = true;
		
		if (_isPaused) {
			_timebar.resume();
			_isPaused = false;
			return;
		}
		
		_gm.createNewBoard();
		_timebar.start();
		_timebar.offbeatUpdate();
	}
	
	private void _updateBoosterBtts() {
		int v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.ADD_5_SEC);
		_bAdd5sec.setText("" + v);
		
		v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.EMPTY);
		_bAddEmpty.setText("" + v);
		
		v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.SWAP);
		_bSwap.setText("" + v);
		
		v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.REMOVE_ALL);
		_bRemoveAll.setText("" + v);
	}
	
	@Override
	public void switchIn(final Object payload) {
		_updateBoosterBtts();
		_bv.registerEntityModifier(
				new MoveYModifier(0.5f, -1000, BOARD_TOP, EaseBackOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						start();
						notifyListener(Spanel.SWITCH_IN, payload);	
						_scene.setOnSceneTouchListener(GamePanel.this);
					}
				});
		_bttBar.registerEntityModifier(new MoveYModifier(0.5f, 1200, BTT_BAR_TOP));
		
		_pointBox.registerEntityModifier(
				new MoveYModifier(0.4f, -100, NUM_BOX,  EaseBackOut.getInstance()));

		_tickBox.registerEntityModifier(
				new MoveYModifier(0.4f, -100, NUM_BOX,  EaseBackOut.getInstance()));
		
		if (_helpText.getText().length() > 0) {
			_helpText.registerEntityModifier(
					new SequenceEntityModifier(
							new DelayModifier(0.5f),
							new ScaleModifier(0.2f, 1, 1, 0, 1)));
		}
	}
	

	@Override
	public void switchOut(final Object payload) {
		_bttBar.registerEntityModifier(MoveYDelayModifier.create(0.15f, new MoveYModifier(0.4f, BTT_BAR_TOP, 1200)));
		_bv.registerEntityModifier(
				new MoveYModifier(0.4f, BOARD_TOP, -1000) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						notifyListener(Spanel.SWITCH_OUT, payload);
						_scene.setOnSceneTouchListener(null);
					}
				});
		_pointBox.registerEntityModifier(
				new MoveYModifier(0.3f, NUM_BOX, -100));
		_tickBox.registerEntityModifier(
				new MoveYModifier(0.3f, NUM_BOX, -100));
		_helpText.registerEntityModifier(new ScaleModifier(0.2f, 1, 1, 1, 0));
	}



	@Override
	public boolean onBackgroundTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		if (_gm.isGamePausedOrEnded())
			return false;
		if (pSceneTouchEvent.isActionUp()) {
			_gm.clearSelectedPieces();
			_gm.updateSelectedPieces();
			_gm.check();
			return false;
		}
		return false;
	}


	
	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		if (arg1.getAction() == TouchEvent.ACTION_DOWN) {
			_isMouseDown = true;
			_tx = arg1.getX();
			_ty = arg1.getY();
			_gm.onTouchBegan(_tx, _ty);
		}
		
		if (arg1.getAction() == TouchEvent.ACTION_MOVE) {
			if (_isMouseDown) {
				float tx = arg1.getX();
				float ty = arg1.getY();
				if (Math.abs(_tx - tx) > 1 || Math.abs(_ty - ty) > 1) {
					_tx = tx;
					_ty = ty;
					_gm.onTouchMoved(_tx, _ty);
				}	
			}
		}
		
		if (arg1.getAction() == TouchEvent.ACTION_UP) {
			_isMouseDown = false;
			_tx = arg1.getX();
			_ty = arg1.getY();
			_gm.onTouchEnded(_tx, _ty);
		}
		
		return false;
	}

	@Override
	public void onTimeExpired() {
		List<IAward> as = _gm.getAwardList();
		if (as.size() > 0) {
			IAward first = as.remove(0);
			_gm.getGameActivity().switchPanel(new AwardingPanel(_mode, _gm.getScore(), first, as));
		} else {
			_gm.getGameActivity().switchPanel(new StatPanel(_mode, _gm.getScore()));
		}
	}

	@Override
	public void onScoreChanged(int s) {
		_pointBox.setAsNum(s);
	}

	@Override
	public void onBonusTrigger(int s) {

	}

	@Override
	public boolean onBackpressed() {
		if (_isPaused)
			return true;
		_isPaused = true;
		_timebar.pause();
		_gm.getGameActivity().popupPanel(new PausePanel(_mode));
		return true;
	}

	@Override
	public void onButtonClick(LabelButton lb) {
		if (_gm.isGameEnded()) // BUG 1001 - to test
			return;
		
		if (! _boosterClickAccept)
			return;		
		_boosterClickAccept = false;
		
		if (lb == _bAdd5sec) {
			int v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.ADD_5_SEC);
			if (v > 0) {
				_gm.runBooster(new Add5SecBooster(_gm));
				lb.setEnabled(false);
				_game.scoreCenter.recordBoosterUsed(IBoosterExecutable.BoosterTyp.ADD_5_SEC);
				lb.setText("" + (v - 1));
				_tickBox.triggerLabelFocus("+5 s.");
			} else {
				_isPaused = true;
				_timebar.pause();
				_game.popupPanel(new BoosterPanel(new Add5SecInfo()));
			}
			return;
		}
		
		if (lb == _bAddEmpty) {
			int v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.EMPTY);
			if (v > 0) {
				_gm.runBooster(new AddEmptyBooster(_gm));
				lb.setEnabled(false);
				_game.scoreCenter.recordBoosterUsed(IBoosterExecutable.BoosterTyp.EMPTY);
				lb.setText("" + (v - 1));
			} else {
				_isPaused = true;
				_timebar.pause();
				_game.popupPanel(new BoosterPanel(new AddEmptyInfo()));
			}
			return;
		}
	
		if (lb == _bSwap) {
			int v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.SWAP);
			if (v > 0) {
				_gm.runBooster(new SwapBooster(_gm));
				lb.setEnabled(false);
				_game.scoreCenter.recordBoosterUsed(IBoosterExecutable.BoosterTyp.SWAP);
				lb.setText("" + (v - 1));
			} else {
				_isPaused = true;
				_timebar.pause();
				_game.popupPanel(new BoosterPanel(new SwapInfo()));
			}
			return;
		}
		
		if (lb == _bRemoveAll) {
			int v = _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.REMOVE_ALL);
			if (v > 0) {
				_gm.runBooster(new RemoveAllBooster(_gm));
				lb.setEnabled(false);
				_game.scoreCenter.recordBoosterUsed(IBoosterExecutable.BoosterTyp.REMOVE_ALL);
				lb.setText("" + (v - 1));
			} else {
				_isPaused = true;
				_timebar.pause();
				_game.popupPanel(new BoosterPanel(new RemoveAllInfo()));
			}
			return;
		}
	}
	
	@Override
	public void onTick(int tickLeft) {
		_tickBox.setAsNum(tickLeft);
	}

	@Override
	public void onTickAdded(float tickAdded) {
		;
	}

	@Override
	public void onBoosterFinished(IBoosterExecutable booster) {
		_boosterClickAccept = true;
		clearHelpText();
		if (booster instanceof AddEmptyBooster) {
			_bAddEmpty.setEnabled(true);
		}
		
		if (booster instanceof SwapBooster) {
			_bSwap.setEnabled(true);
		}
		
		_gm.check();

	}
	
	@Override
	public void onPause() {
		onBackpressed();
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;

		_bv.detachSelf();
		_timebar.detachSelf();
		
		for (Entity e : entities) {
			if (e.isDisposed())
				continue;
			if (e.hasParent())
				e.detachSelf();
			e.dispose();
		}
		_gm.destroy();
		super.dispose();
	}


}
