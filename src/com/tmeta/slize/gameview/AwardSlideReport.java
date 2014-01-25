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

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.award.IAward;
import com.tmeta.slize.gameview.SlideOption.SlideOptionListener;
import com.tmeta.slize.panel.AwardInfoPanel;

public class AwardSlideReport extends Rectangle implements SlideOptionListener {
	
	GameActivity _game;
	Rectangle _rectTitle;
	SlideOption _soption;
	Text _rtitle;
	Scene _scene;
	ArrayList<Entity> _entities = new ArrayList<Entity>();
	ArrayList<Entity> _cards = new ArrayList<Entity>();
	
	String[] descs = new String[] {
			"page 1 of 7", 
			"page 2 of 7",
			"page 3 of 7",
			"page 4 of 7", 
			"page 5 of 7",
			"page 6 of 7",
			"page 7 of 7"
	};

	
	public AwardSlideReport(GameActivity game, Scene scene) {
		super(0, 0, 470, 545, game.getVertexBufferObjectManager());
		
		setColor(1f, 1f, 1f);
		_game = game;
		_scene = scene;
		
		_rectTitle = new Rectangle(0, 0, 470, 60, game.getVertexBufferObjectManager());
		_rectTitle.setColor(50f/255f, 58f/255f, 69f/255f);
		attachChild(_rectTitle);
		
		
		AwardCard c1 = new AwardCard(game, 65, 25, 330, 470);
		c1.addAward(game.awardCenter.getAward(IAward.LOCAL_HIGH_SCORE));
		c1.addAward(game.awardCenter.getAward(IAward.MATCH_SQ_2X2));
		c1.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_5));
		c1.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_6));
		c1.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_7));
		c1.finalize();
		_cards.add(c1);
		
		AwardCard c2 = new AwardCard(game, 65, 25, 330, 470);
		c2.addAward(game.awardCenter.getAward(IAward.MATCH_SQ_3X3));
		c2.addAward(game.awardCenter.getAward(IAward.MATCH_CROSS));
		c2.addAward(game.awardCenter.getAward(IAward.MATCH_SQ_ISLAND));
		c2.addAward(game.awardCenter.getAward(IAward.MATCH_VERT_7));
		c2.addAward(game.awardCenter.getAward(IAward.MATCH_HORZ_7));
		c2.finalize();
		_cards.add(c2);
		
		AwardCard c3 = new AwardCard(game, 65, 25, 330, 470);
		c3.addAward(game.awardCenter.getAward(IAward.FIRST_PLAY_20M));
		c3.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_5_3CONSQ));
		c3.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_5_5CONSQ));
		c3.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_6_3CONSQ));
		c3.addAward(game.awardCenter.getAward(IAward.MATCH_GROUP_6_5CONSQ));
		c3.finalize();
		_cards.add(c3);
		
		AwardCard c4 = new AwardCard(game, 65, 25, 330, 470);
		c4.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_1_CHECKER));
		c4.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_5_CHECKER));
		c4.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_10_CHECKER));
		c4.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_25_CHECKER));
		c4.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_50_CHECKER));
		c4.finalize();
		_cards.add(c4);
		
		AwardCard c5 = new AwardCard(game, 65, 25, 330, 470);
		c5.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_100_CHECKER));
		c5.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_250_CHECKER));
		c5.addAward(game.awardCenter.getAward(IAward.NUM_PLAY_500_CHECKER));
		c5.addAward(game.awardCenter.getAward(IAward.SCORE_150_20M));
		c5.addAward(game.awardCenter.getAward(IAward.SCORE_300_20M));
		c5.finalize();
		_cards.add(c5);
		
		AwardCard c6 = new AwardCard(game, 65, 25, 330, 470);
		c6.addAward(game.awardCenter.getAward(IAward.SCORE_150_60S));
		c6.addAward(game.awardCenter.getAward(IAward.SCORE_250_60S));
		c6.addAward(game.awardCenter.getAward(IAward.SCORE_500_60S));
		c6.addAward(game.awardCenter.getAward(IAward.SCORE_500_120S));
		c6.addAward(game.awardCenter.getAward(IAward.SCORE_1000_120S));
		c6.finalize();
		_cards.add(c6);
		
		AwardCard c7 = new AwardCard(game, 65, 25, 330, 470);
		c7.addAward(game.awardCenter.getAward(IAward.EARLY_BIRD));
		c7.addAward(game.awardCenter.getAward(IAward.ALL_NIGHTER));
		c7.addAward(game.awardCenter.getAward(IAward.LUNCH_TIME));
		c7.finalize();
		_cards.add(c7);
		
		_soption = new SlideOption(game, 460, 500, _cards, 0, false, this);
		_soption.setPosition(5, 50);
		attachChild(_soption);
		
		scene.registerTouchArea(_soption);
	}

	@Override
	public void onOptionClick(int index, float x, float y) {
		AwardCard ac = (AwardCard)_cards.get(index);
		final AwardCardEntry ae = ac.getClickedEntry(x, y);
		if (ae != null) {
			ae.flash();
			_scene.registerUpdateHandler(new TimerHandler(0.4f, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					_game.popupPanel(new AwardInfoPanel(ae.getAward()));					
				}}));
		}
	}

	
	@Override
	public void onOptionSwitch(final int index) {
		if (_rtitle == null) {
			_rtitle = new Text(0, 0, _game.res.font28, "",
					40, _game.getVertexBufferObjectManager());
			_rtitle.setColor(1, 1, 1);
			attachChild(_rtitle);
		}
		
		_rtitle.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 1, 0) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				_rtitle.setText(descs[index]);
				float w = _rtitle.getWidth();
				_rtitle.setPosition((480 - w) / 2, 18);
				_rtitle.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 0, 1));				
			}
		});
	}

}
