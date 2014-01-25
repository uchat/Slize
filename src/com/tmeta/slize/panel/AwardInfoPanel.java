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
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.award.IAward;
import com.tmeta.slize.gameview.FlipCanvas;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class AwardInfoPanel extends Spanel implements LabelButtonListener {
	private final int START_TOP = 635;
	private final int BOX_WIDTH = 430;
	private final int BOX_TOP = 50;
	
	protected GameActivity _game;
	protected Scene _scene;
	private ArrayList<Entity> _entities = new ArrayList<Entity>();
	List<IAward> _rest;
	int _score, _mode;
	Rectangle _rect1;
	IAward _award;
	LabelButton _bContinue;
	FlipCanvas _canvas;
	LabelButton _bDone;
	
	public AwardInfoPanel(IAward award) {
		_award = award;
	}
	

	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;

		_rect1 = new Rectangle((480 - BOX_WIDTH) / 2, -1000, BOX_WIDTH, 570, game.getVertexBufferObjectManager());
		_entities.add(_rect1);
		this.attachChild(_rect1);	
		
		Rectangle _header = new Rectangle(0, 0, BOX_WIDTH, 85, game.getVertexBufferObjectManager());
		_header.setColor(_game.res.COLOR[3]);
		_entities.add(_header);
		_rect1.attachChild(_header);
		
		String str = _tryGetHeader(_award);
		Text title = new Text(0, 18, game.res.font56, str,
				str.length(),
				game.getVertexBufferObjectManager());
		game.centerText(BOX_WIDTH, title);
		title.setColor(0, 0, 0);
		_entities.add(title);
		_rect1.attachChild(title);
	
		str = _award.getSubHeader();
		Text subtitle = new Text(0, 100, game.res.font28, str,
				str.length(),
				game.getVertexBufferObjectManager());
		game.centerText(BOX_WIDTH, subtitle);
		subtitle.setColor(0,  0,  0);
		_entities.add(subtitle);
		_rect1.attachChild(subtitle);
		
		String finame = "";
		String fstr = "";
		if (_game.awardCenter.isWon(_award.getId())) {
			finame = _award.getPnmName();
			fstr = "You've won this award";
		} else if (_game.awardCenter.isWon(_award.getLock())) {
			finame = "available.pnm";
			fstr = "+" + _award.getPointBonus() + " bonus";
		} else {
			finame = "lock.pnm";
			IAward aw = _game.awardCenter.getAward(_award.getLock());
			fstr = "require " + _tryGetHeader(aw);
		}
		_canvas = new FlipCanvas(_game, 17, 14, finame);
		_canvas.setPosition(45,  190);
		_entities.add(_canvas);
		_rect1.attachChild(_canvas);
		
		Text bonus = new Text(0, 495, game.res.font28, fstr,
				fstr.length(),
				game.getVertexBufferObjectManager());
		game.centerText(BOX_WIDTH, bonus);
		bonus.setPosition(bonus.getX()-8, bonus.getY());
		bonus.setColor(_game.res.COLOR[0]);
		_entities.add(bonus);
		_rect1.attachChild(bonus);
		
		_bDone = new LabelButton(game, (480 - 200)/2, 1200, 200, 50, "done", null, game.res.font28, this);
		_bDone.setColor(223f/255f, 223f/255f, 223f/255f);
		attachChild(_bDone);
				
		_scene.registerTouchArea(_bDone);
	}
	
	private String _tryGetHeader(IAward a) {
		if (a.getId() == IAward.LOCAL_HIGH_SCORE)
			return "High Score";
		
		return a.getHeader();
	}

	@Override
	public void switchIn(final Object payload) {
		_rect1.registerEntityModifier(new MoveYModifier(0.5f, -1000, BOX_TOP, EaseBackOut.getInstance()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_IN, payload);
				_canvas.update();
			}
		});
		_bDone.registerEntityModifier(new MoveYModifier(0.5f, 1200, START_TOP));
	}
	
	@Override
	public void switchOut(final Object payload) {
		_rect1.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -1000) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_OUT, payload);
			}
		});
		_bDone.registerEntityModifier(new MoveYModifier(0.3f, START_TOP, 1200));
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
		if (lb == _bDone) {
			_game.dismissPopup();
			return;
		}
	}
	
	@Override
	public void onPause() {
		;
	}


}
