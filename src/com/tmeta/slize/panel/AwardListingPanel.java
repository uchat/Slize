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
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBackOut;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Spanel;
import com.tmeta.slize.gameview.AwardSlideReport;
import com.tmeta.slize.gameview.LabelButton;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class AwardListingPanel extends Spanel implements LabelButtonListener {
	private final int SLIDE_REPORT_TOP = 100;
	private final int START_TOP = 655;
	private final int BOX_TOP = 30;

	protected GameActivity _game;
	protected Scene _scene;
	AwardSlideReport _awardReport;
	LabelButton _bDone;
	Text header;
		
	@Override
	public void create(GameActivity game, Scene scene) {
		_game = game;
		_scene = scene;
		_game.awardCenter.refreshWonAward();
		
		header = new Text(0, 0, _game.res.font56, "awards", "Awards".length(), _game.getVertexBufferObjectManager());
		header.setPosition((480 - header.getWidth()) / 2, -200);
		attachChild(header);
		
		_awardReport = new AwardSlideReport(game, _scene);
		_awardReport.setPosition(5, 900);
		attachChild(_awardReport);
		
		_bDone = new LabelButton(game, (480 - 200)/2, 1200, 200, 50, "done", null, game.res.font28, this);
		_bDone.setColor(223f/255f, 223f/255f, 223f/255f);
		attachChild(_bDone);

		_scene.registerTouchArea(_bDone);
	}

	@Override
	public void switchIn(final Object payload) {
		_awardReport.registerEntityModifier(
				new MoveYModifier(0.4f, -300, SLIDE_REPORT_TOP, EaseBackOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						notifyListener(Spanel.SWITCH_IN, payload);	
					}
				});
		_bDone.registerEntityModifier(new MoveYModifier(0.5f, 1200, START_TOP));	
		header.registerEntityModifier(new MoveYModifier(0.4f, -200, BOX_TOP));
	}

	@Override
	public void switchOut(final Object payload) {
		_awardReport.registerEntityModifier(new MoveYModifier(0.4f, SLIDE_REPORT_TOP, -1000) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				notifyListener(Spanel.SWITCH_OUT, payload);
			}
		});
		_bDone.registerEntityModifier(new MoveYModifier(0.3f, START_TOP, 1200));
		header.registerEntityModifier(new MoveYModifier(0.4f, BOX_TOP, -200));
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
		onBackpressed();
	}
	
	@Override
	public void onPause() {
		;
	}


}
