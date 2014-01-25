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
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.Config;
import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.SlideOption;
import com.tmeta.slize.gameview.SlideOption.SlideOptionListener;

public class FrontPanel extends Spanel implements SlideOptionListener {

	public static int LAST_ACTIVE_OPTION = 0;
	
	protected GameActivity _game;
	protected Scene _scene;
	
	private Rectangle _rect1;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Text _text;
	private SlideOption _soption;
	private Sprite[] pvs;
	
	String[] descs = new String[] {"60 seconds of slip and slide", 
				"120 seconds of sliding awesomeness", 
				"20 moves allowed, no time limit",
				"look at all these awards!",
				"sound, ads, log in/out, etc"};
	
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		
		_rect1 = new Rectangle(10, -1000, 460, 220, game.getVertexBufferObjectManager());
		entities.add(_rect1);
		this.attachChild(_rect1);		
		
		Sprite logo = new Sprite(33, 28, _game.res.logoTR, _game.getVertexBufferObjectManager());
		_rect1.attachChild(logo);
		
		ArrayList<Entity> modes = new ArrayList<Entity>();
		String[] strs = new String[] {"60 sec.", "120 sec.", "20 moves", "awards", "settings"};
		for (String s : strs) {
			Text t = new Text(0, 60, game.res.font56, s,
					s.length(),
					game.getVertexBufferObjectManager());
			float len = t.getWidth();
			t.setPosition((400 - len) / 2, 75);
			modes.add(t);
		}
		
		_soption = new SlideOption(game, 400, 200, modes, LAST_ACTIVE_OPTION, false, this);
		_soption.setPosition(40, 1200);
		attachChild(_soption);
		entities.add(_soption);

		pvs = new Sprite[5];
		for (int i = 0; i < 5; i++) {
			pvs[i] = new Sprite(30 + i * 90, 300, _game.res.hiliTR, _game.getVertexBufferObjectManager());
			pvs[i].setColor(_game.res.COLOR[i]);
			pvs[i].setScale(0.8f);
			entities.add(pvs[i]);
			pvs[i].setAlpha(0);
			attachChild(pvs[i]);
		}
		
		_scene.registerTouchArea(_soption);
	}
	
	@Override
	public void switchIn(final Object payload) {
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, -1000, 50,  EaseBackOut.getInstance()));
		notifyListener(Spanel.SWITCH_IN, payload);	
		_soption.registerEntityModifier(new MoveYModifier(0.4f, 1200, 480));
		_text.registerEntityModifier(MoveYDelayModifier.create(0.15f, new MoveYModifier(0.4f, 1000, 660)));
		
		float d = 0.2f;
		for (int i = 0; i < 5; i++) {
			pvs[i].registerEntityModifier(new SequenceEntityModifier(
					new DelayModifier(0.5f),
					new AlphaModifier(d, 0, 1)
					));
			d += 0.05f;
		}
		
		this._soption.setEnable(true);
	}

	@Override
	public void switchOut(final Object payload) {
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, 50, -1000) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_OUT, payload);
			}
		});
		_text.registerEntityModifier(new MoveYModifier(0.4f, 660, 1000));
		_soption.registerEntityModifier(MoveYDelayModifier.create(0.15f, new MoveYModifier(0.4f, 480, 1200)));
		
		float d = 0.2f;
		for (int i = 0; i < 5; i++) {
			pvs[i].registerEntityModifier(new AlphaModifier(d, 1, 0));
			d += 0.05f;
		}
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
	public void onOptionClick(int index, float x, float y) {
		switch (index) {
		case 0:
			_game.switchPanel(new StatPanel(Config.MODE_60S));
			this._soption.setEnable(false);
			break;
		case 1:
			_game.switchPanel(new StatPanel(Config.MODE_120S));
			this._soption.setEnable(false);
			break;
		case 2:
			_game.switchPanel(new StatPanel(Config.MODE_20M));
			this._soption.setEnable(false);
			break;
		case 3:
			_game.switchPanel(new AwardListingPanel());
			break;
		case 4:
			_game.popupPanel(new SettingPanel());
			this._soption.setEnable(false);
			break;
		}
		LAST_ACTIVE_OPTION = index;
	}

	@Override
	public void onOptionSwitch(final int index) {
		if (_text == null) {
			_text = new Text(0, 1000, _game.res.font28, "",
					40, _game.getVertexBufferObjectManager());
			_text.setColor(0.4f, 0.4f, 0.4f);
			attachChild(_text);
		}
		
		_text.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 1, 0) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				_text.setText(descs[index]);
				float w = _text.getWidth();
				_text.setPosition((480 - w) / 2, _text.getY());
				_text.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 0, 1));				
			}
		});
	}

	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		_scene.unregisterTouchArea(_soption);
		for (Entity e : entities) {
			if (e.isDisposed())
				continue;
			if (e.hasParent())
				e.detachSelf();
			e.dispose();
		}
		super.dispose();
	}
	
	@Override
	public void onPause() {
		;
	}


}
