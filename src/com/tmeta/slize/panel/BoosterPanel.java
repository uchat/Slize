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

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.booster.IBoosterInfo;
import com.tmeta.slize.gameview.FlipCanvas;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.MoveYDelayModifier;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class BoosterPanel extends Spanel implements LabelButtonListener {
	
	private final int POINT_TOP = 30;	
	private final int BOX_TOP = 90;
	private final int BOX_WIDTH = 430;
	private final int DONE_TOP = 630;

	
	protected GameActivity _game;
	protected Scene _scene;
	private Rectangle _rect1;
	private ArrayList<Entity> _entities = new ArrayList<Entity>();
	IBoosterInfo _binfo;
	Text _tpoint;
	LabelButton _done, _buy;
	int _totalPoint;
	FlipCanvas _canvas;
	boolean _buyAccept = false;

	public BoosterPanel(IBoosterInfo binfo) {
		_binfo = binfo;
	}
	
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		
		_totalPoint = _game.scoreCenter.getTotalPoint();
		String str = "you have " + _totalPoint + " points";
		_tpoint = new Text(0, 0, _game.res.font28, str,
				str.length(), _game.getVertexBufferObjectManager());
		_tpoint.setColor(0.5f, 0.5f, 0.5f);
		_tpoint.setPosition((480 - _tpoint.getWidth()) / 2, -1000);
		_entities.add(_tpoint);
		attachChild(_tpoint);
		
		_rect1 = new Rectangle((480 - BOX_WIDTH) / 2, -1000, BOX_WIDTH, 500, game.getVertexBufferObjectManager());
		_entities.add(_rect1);
		this.attachChild(_rect1);	
		
		Rectangle header = new Rectangle(0, 0, BOX_WIDTH, 85, game.getVertexBufferObjectManager());
		header.setColor(140f/255f, 186f/255f, 255f/255f);
		_entities.add(header);
		_rect1.attachChild(header);
		
		str = _binfo.getName();
		Text name = new Text(0, 15, _game.res.font56, str,
				str.length(), _game.getVertexBufferObjectManager());
		_game.centerText(BOX_WIDTH, name);
		_entities.add(name);
		_rect1.attachChild(name);
	
		str = _binfo.getFirstLine();
		Text l1 = new Text(0, 343, _game.res.font28, str,
				str.length(), _game.getVertexBufferObjectManager());
		l1.setColor(0.3f, 0.3f, 0.3f);
		_game.centerText(BOX_WIDTH, l1);
		_entities.add(l1);
		_rect1.attachChild(l1);
		
		str = _binfo.getSecondLine();
		Text l2 = new Text(0, 380, _game.res.font28, str,
				str.length(), _game.getVertexBufferObjectManager());
		l2.setColor(0.5f, 0.5f, 0.5f);
		_game.centerText(BOX_WIDTH, l2);
		_entities.add(l2);
		_rect1.attachChild(l2);
		
		_canvas = new FlipCanvas(_game, 13, 11, _binfo.getPnm());
		_canvas.setPosition(85,  110);
		_entities.add(_canvas);
		_rect1.attachChild(_canvas);
		
		boolean enough = _totalPoint >= _binfo.getCostPerBundle();
		if (enough) {
			str = _binfo.getItemPerBundle() + " for " + _binfo.getCostPerBundle() + " points";
		} else {
			str = "need more points";
		}
		_buy = new LabelButton(game, (BOX_WIDTH-280)/2, 425, 280, 60, str, null, game.res.font28, this);
		_buy.setTextColor(0, 0, 0);
		_entities.add(_buy);
		_rect1.attachChild(_buy);
		if (enough) {
			_buy.setColor(_game.res.COLOR[3]);
		} else {
			_buy.setColor(_game.res.COLOR[5]);
			_buy.setEnabled(false);
		}
		
		_done = new LabelButton(game, (480-180)/2, 1200, 180, 60, "done", null, game.res.font28, this);
		_done.setBackgroundAlpha(0);
		_entities.add(_done);
		attachChild(_done);
		
		_scene.registerTouchArea(_done);
		_scene.registerTouchArea(_buy);
	}

	@Override
	public void switchIn(final Object payload) {		
		_tpoint.registerEntityModifier(new MoveYModifier(0.4f, -300, POINT_TOP));
		_done.registerEntityModifier(MoveYDelayModifier.create(0.1f, new MoveYModifier(0.4f, 1000, DONE_TOP)));
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, -1000, BOX_TOP, EaseBackOut.getInstance()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_IN, payload);
				_canvas.update();
				_buyAccept = true;
			}
		});
	}

	@Override
	public void switchOut(final Object payload) {
		_buyAccept = false;
		_tpoint.registerEntityModifier(new MoveYModifier(0.3f, POINT_TOP, -300));
		_done.registerEntityModifier(new MoveYModifier(0.4f, DONE_TOP, 1000));
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
		
		if (lb == _buy) {
			if (! _buyAccept)
				return;
			_buyAccept = false;
			int bundle = _binfo.getItemPerBundle();
			int cost = _binfo.getCostPerBundle();
			if (cost <= _totalPoint) {
				_game.scoreCenter.topupBooster(_binfo.getTyp(), bundle, cost);
				_totalPoint = _game.scoreCenter.getTotalPoint();
				String str = "you have " + _totalPoint + " points";
				_tpoint.setText(str);
				_canvas.update("plus5.pnm");
				boolean enough = _totalPoint >= _binfo.getCostPerBundle();
				if (! enough) {
					_buy.setColor(_game.res.COLOR[5]);
					_buy.setEnabled(false);
					_buy.setText("need more points");
				}
				_game.sound.play(_game.sound.WOOSH);
				
				registerUpdateHandler(new TimerHandler(1.6f,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						_canvas.update(_binfo.getPnm());
						_buyAccept = true;
					}
				}));
			}
		}
		
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		for (Entity e : _entities) {
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
