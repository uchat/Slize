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

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.booster.Add5SecInfo;
import com.tmeta.slize.booster.AddEmptyInfo;
import com.tmeta.slize.booster.IBoosterExecutable;
import com.tmeta.slize.booster.RemoveAllInfo;
import com.tmeta.slize.booster.SwapInfo;
import com.tmeta.slize.gameview.ButtonBar;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.LabelValueBox;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.StatSlideReport;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class StatPanel extends Spanel implements LabelButtonListener {

	private final int SLIDE_REPORT_TOP = 100;
	private final int BTT_BAR_TOP = 545;
	private final int START_TOP = 648;
	private final int BOX_TOP = 10;
	private final int LINE_TOP = 15;
	
	protected GameActivity _game;
	protected Scene _scene;
	int _mode;
	LabelButton _bStart, _bAdd5sec, _bRemoveAll, _bSwap, _bAddEmpty;
	private LabelValueBox _totalBox, _scoreBox, _modeBox;
	StatSlideReport _slideReport;
	ButtonBar _bttBar;
	int _roundScore;
	Rectangle l1, l2;

	public StatPanel(int mode) {
		this(mode, -1);
	}
	
	public StatPanel(int mode, int score) {
		_mode = mode;
		_roundScore = score;
	}

	
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		
		String mode = "60 s.";
		if (_mode == Config.MODE_120S)
			mode = "120 s.";
		else if (_mode == Config.MODE_20M)
			mode = "20 #";
		_modeBox = new LabelValueBox(game, "mode", true);
		_modeBox.setPosition(10, -100);
		_modeBox.setLabelColor(game.res.COLOR[4]);
		_modeBox.setAsString(mode);
		attachChild(_modeBox);
		
		String tb = "start";
		if (_roundScore >= 0) {
			tb = "play again";
			_scoreBox = new LabelValueBox(game, "this round", true);
			_scoreBox.setPosition(170, -100);
			_scoreBox.setLabelColor(game.res.COLOR[0]);
			_scoreBox.setAsNum(_roundScore);
			attachChild(_scoreBox);
			
			l1 = new Rectangle(160, -300, 2, 80, game.getVertexBufferObjectManager());
			l1.setColor(0.7f, 0.7f, 0.7f);
			attachChild(l1);
		}
		
		l2 = new Rectangle(320, -300, 2, 80, game.getVertexBufferObjectManager());
		l2.setColor(0.7f, 0.7f, 0.7f);
		attachChild(l2);
				
		_totalBox = new LabelValueBox(game, "total", true);
		_totalBox.setPosition(330, -100);
		_totalBox.setLabelColor(game.res.COLOR[0]);
		_totalBox.setAsNum(_game.scoreCenter.getTotalPoint());
		attachChild(_totalBox);
		
		
		_slideReport = new StatSlideReport(game, _scene, _mode, _roundScore);
		_slideReport.setPosition(5, 900);
		attachChild(_slideReport);
		
		_bttBar = new ButtonBar(game, scene, 5, 1200, 470, 100, this);
		_bAdd5sec = _bttBar.addButton(_game.res.add5TR, "0");
		_bAddEmpty = _bttBar.addButton(_game.res.addEmptyTR, "1");
		_bSwap = _bttBar.addButton(_game.res.swapTR, "2");
		_bRemoveAll = _bttBar.addButton(_game.res.removeAllTR, "3");
		_bttBar.finalize();
		_bttBar.setPosition((480 - _bttBar.getWidth()) / 2, 1200);
		attachChild(_bttBar);
		
		_bStart = new LabelButton(game, (480-260)/2, 1200, 260, 70, tb, null, game.res.font56, this);
		_bStart.setColor(223f/255f, 223f/255f, 223f/255f);
		_bStart.excite();
		attachChild(_bStart);

		_scene.registerTouchArea(_bStart);
	}

	
	private void _updateBoosterChange() {
		_bAdd5sec.setText("" + _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.ADD_5_SEC));
		_bAddEmpty.setText("" + _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.EMPTY));
		_bSwap.setText("" + _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.SWAP));
		_bRemoveAll.setText("" + _game.scoreCenter.getBoosterCount(IBoosterExecutable.BoosterTyp.REMOVE_ALL));
		_totalBox.setAsNum(_game.scoreCenter.getTotalPoint());
	}
	
	@Override
	public void switchIn(final Object payload) {
		_updateBoosterChange();
		_slideReport.registerEntityModifier(
				new MoveYModifier(0.4f, -300, SLIDE_REPORT_TOP, EaseBackOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						notifyListener(Spanel.SWITCH_IN, payload);	
					}
				});
		_bttBar.registerEntityModifier(new MoveYModifier(0.4f, 1200, BTT_BAR_TOP));
		_bStart.registerEntityModifier(new MoveYModifier(0.5f, 1200, START_TOP));		
		_totalBox.registerEntityModifier(new MoveYModifier(0.4f, -200, BOX_TOP));
		_modeBox.registerEntityModifier(new MoveYModifier(0.4f, -200, BOX_TOP));
		if (l1 != null)
			l1.registerEntityModifier(new MoveYModifier(0.3f, -300, LINE_TOP));
		l2.registerEntityModifier(new MoveYModifier(0.3f, -300, LINE_TOP));
		if (_scoreBox != null)
			_scoreBox.registerEntityModifier(new MoveYModifier(0.4f, -200, BOX_TOP));		
	}
	
	@Override
	public void switchOut(final Object payload) {		
		_bttBar.registerEntityModifier(MoveYDelayModifier.create(0.15f, new MoveYModifier(0.4f, BTT_BAR_TOP, 1200)));
		_bStart.registerEntityModifier(new MoveYModifier(0.3f, START_TOP, 1200));
		_totalBox.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -200));
		_modeBox.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -200));
		if (l1 != null)
			l1.registerEntityModifier(new MoveYModifier(0.3f, LINE_TOP, -300));
		l2.registerEntityModifier(new MoveYModifier(0.3f, LINE_TOP, -300));
		if (_scoreBox != null)
			_scoreBox.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -200));
		
		_slideReport.registerEntityModifier(new MoveYModifier(0.4f, SLIDE_REPORT_TOP, -1000) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_OUT, payload);
			}
		});
	}
	
	@Override
	public boolean onBackgroundTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		return false;
	}

	@Override
	public boolean onBackpressed() {
		_game.switchPanel(new FrontPanel());
		return true;
	}
	
	@Override
	public void onButtonClick(LabelButton lb) {
		if (lb == _bAdd5sec) {
			_game.popupPanel(new BoosterPanel(new Add5SecInfo()));
			return;
		}
		
		if (lb == _bAddEmpty) {
			_game.popupPanel(new BoosterPanel(new AddEmptyInfo()));
			return;
		}
		
		if (lb == _bSwap) {
			_game.popupPanel(new BoosterPanel(new SwapInfo()));
			return;
		}
		
		
		if (lb == _bRemoveAll) {
			_game.popupPanel(new BoosterPanel(new RemoveAllInfo()));
			return;
		}
		

		if (lb == _bStart) {
			_game.switchPanel(new GamePanel(_mode));
			return;
		}
	}

	@Override
	public void dispose() {
		if (super.isDisposed())
			return;

		_scene.unregisterTouchArea(_bStart);
		
		_bStart.detachSelf();
		_bStart.dispose();
		
		_bttBar.detachSelf();
		_bttBar.dispose();
		
		if (_scoreBox != null) {
			_scoreBox.detachSelf();
			_scoreBox.dispose();			
		}
		
		if (l1 != null) {
			l1.detachSelf();
			l1.dispose();
		}
		
		l2.detachSelf();
		l2.dispose();
		
		_totalBox.detachSelf();
		_totalBox.dispose();
		
		_slideReport.detachSelf();
		_slideReport.dispose();
	}
	
	@Override
	public void onPause() {
		;
	}

}
