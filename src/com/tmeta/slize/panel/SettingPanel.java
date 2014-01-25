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

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.gameview.BinarySwitch;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class SettingPanel extends Spanel implements LabelButtonListener {
	
	private final int HEADER_TOP = 30;
	private final int BOX_TOP = 100;
	private final int BOX_WIDTH = 430;
	private final int DONE_TOP = 630;
	
	protected GameActivity _game;
	protected Scene _scene;
	ArrayList<Entity> _entities = new ArrayList<Entity>();
	BinarySwitch _sound, _vibration;
	Rectangle _rect1;
	LabelButton _done, _signOut, _invite;
	Sprite _pservice;
	Text _header, _pinfo, _disableAds;
	boolean _signinAcceptClick = true;
	
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		
		_header = new Text(0, 0, _game.res.font56, "settings",
				"Settings".length(),
				_game.getVertexBufferObjectManager());
		_header.setPosition((480 - _header.getWidth()) / 2, -200);
		attachChild(_header);
		_entities.add(_header);
				
		_rect1 = new Rectangle((480 - BOX_WIDTH) / 2, -1000, BOX_WIDTH, 500, game.getVertexBufferObjectManager());
		_entities.add(_rect1);
		this.attachChild(_rect1);
		
		_sound = new BinarySwitch(game, 15, 50, 400, 40, "sound", _game.dbHandler.isSoundEnable(), 
				new BinarySwitch.SwitchClickedListener() {
					@Override
					public void onSwitchClicked(boolean state) {
						 _game.setSoundEnable(state);
					}
		});
		_entities.add(_sound);
		_rect1.attachChild(_sound);
		
		_vibration = new BinarySwitch(game, 15, 120, 400, 40, "vibration", _game.dbHandler.isVibrationEnable(), 
				new BinarySwitch.SwitchClickedListener() {
				@Override
				public void onSwitchClicked(boolean state) {
					_game.setVibrateEnable(state);
				}
		});
		_entities.add(_vibration);
		_rect1.attachChild(_vibration);
		
		_done = new LabelButton(game, (480-180)/2, 1200, 180, 60, "done", null, game.res.font28, this);
		_done.setBackgroundAlpha(0);
		_entities.add(_done);
		attachChild(_done);
		
		_scene.registerTouchArea(_sound);
		_scene.registerTouchArea(_vibration);
		_scene.registerTouchArea(_done);
	}
	
	@Override
	public void switchIn(final Object payload) {
		_done.registerEntityModifier(MoveYDelayModifier.create(0.1f, new MoveYModifier(0.4f, 1000, DONE_TOP)));
		_header.registerEntityModifier(new MoveYModifier(0.4f, -200, HEADER_TOP));
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, -1000, BOX_TOP, EaseBackOut.getInstance()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_IN, payload);
			}
		});
	}
	
	@Override
	public void switchOut(final Object payload) {
		_done.registerEntityModifier(new MoveYModifier(0.4f, DONE_TOP, 1000));
		_header.registerEntityModifier(new MoveYModifier(0.4f, HEADER_TOP, -200));
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -1000) {
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
		return false;
	}

	@Override
	public void onButtonClick(LabelButton lb) {
		if (lb == _done) {
			_game.dismissPopup();
			return;
		}
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		_scene.unregisterTouchArea(_pservice);
		super.dispose();
	}
	
	@Override
	public void onPause() {
		;
	}
}
