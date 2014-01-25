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

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class PausePanel extends Spanel implements LabelButtonListener {
	
	private static final float RESUME_TOP = 150;
	private static final float RESTART_TOP = 350;
	private static final float DISCARD_TOP = 450;
	
	protected GameActivity _game;
	protected Scene _scene;
	LabelButton _bContinue, _bRestart, _bDiscard;
	int _mode;
	boolean _clickAccept = true;
	IEntityModifier _mc, _mr, _md;
	
	public PausePanel(int mode) {
		_mode = mode;
	}
	
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		
		_bContinue = new LabelButton(game, (480-240)/2, 1200, 240, 80, "continue", null, game.res.font56, this);
		_bContinue.setColor(223f/255f, 223f/255f, 223f/255f);
		_bContinue.excite();
		_bContinue.setBackgroundAlpha(0);
		attachChild(_bContinue);
		
		_bRestart = new LabelButton(game, (480-220)/2, 1200, 220, 80, "restart", null, game.res.font56, this);
		_bRestart.setColor(223f/255f, 223f/255f, 223f/255f);
		_bRestart.setBackgroundAlpha(0);
		attachChild(_bRestart);
		
		_bDiscard = new LabelButton(game, (480-220)/2, 1200, 220, 80, "discard", null, game.res.font56, this);
		_bDiscard.setColor(223f/255f, 223f/255f, 223f/255f);
		_bDiscard.setBackgroundAlpha(0);
		attachChild(_bDiscard);
		
		_scene.registerTouchArea(_bContinue);
		_scene.registerTouchArea(_bRestart);
		_scene.registerTouchArea(_bDiscard);
	}

	@Override
	public void switchIn(final Object payload) {
		_mc = new MoveYModifier(0.4f, 810, RESUME_TOP, EaseBackOut.getInstance());
		_bContinue.registerEntityModifier(_mc);	
		
		_mr = MoveYDelayModifier.create(0.1f, new MoveYModifier(0.4f, 810, RESTART_TOP, EaseBackOut.getInstance()));
		_bRestart.registerEntityModifier(_mr);
		
		_md = MoveYDelayModifier.create(0.2f, new MoveYModifier(0.4f, 810, DISCARD_TOP, EaseBackOut.getInstance()));
		_bDiscard.registerEntityModifier(_md);	

		_scene.registerUpdateHandler(new TimerHandler(0.6f,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						notifyListener(Spanel.SWITCH_IN, payload);
					}
				}));

	}

	@Override
	public void switchOut(final Object payload) {
		_bDiscard.setEnabled(false);
		_bContinue.setEnabled(false);
		_bRestart.setEnabled(false);

		_bContinue.unregisterEntityModifier(_mc);
		_bRestart.unregisterEntityModifier(_mr);
		_bDiscard.unregisterEntityModifier(_md);
		
		_bContinue.registerEntityModifier(MoveYDelayModifier.create(0.2f, new MoveYModifier(0.4f, _bContinue.getY(), 1200)));	
		_bRestart.registerEntityModifier(MoveYDelayModifier.create(0.1f, new MoveYModifier(0.4f, _bRestart.getY(), 1200)));	
		_bDiscard.registerEntityModifier(new MoveYModifier(0.4f, _bDiscard.getY(), 1200));	
		
		_scene.registerUpdateHandler(new TimerHandler(0.5f,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						notifyListener(Spanel.SWITCH_OUT, payload);	
					}
				}));
		
	}

	@Override
	public boolean onBackgroundTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		return false;
	}

	@Override
	public boolean onBackpressed() {
		return false;
	}

	@Override
	public void onButtonClick(LabelButton lb) {
		if (! _clickAccept)
			return;
		_clickAccept = false;
		
		if (lb == _bContinue)
			_game.dismissPopup();

		if (lb == _bRestart)
			_game.switchPanel(new GamePanel(_mode));
		
		if (lb == _bDiscard)
			_game.switchPanel(new StatPanel(_mode, -1));
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		_scene.unregisterTouchArea(_bDiscard);
		_scene.unregisterTouchArea(_bRestart);
		_scene.unregisterTouchArea(_bContinue);
		
		_bContinue.detachSelf();
		_bContinue.dispose();
		
		_bRestart.detachSelf();
		_bRestart.dispose();
		
		_bDiscard.detachSelf();
		_bDiscard.dispose();
		
		super.dispose();
	}
	
	@Override
	public void onPause() {
		;
	}

}
