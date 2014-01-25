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
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Pair;
import com.tmeta.slize.gameview.SlideOption.SlideOptionListener;

public class StatSlideReport extends Rectangle implements SlideOptionListener {
	
	GameActivity _game;
	Rectangle _rectTitle;
	TwoColumnCard _cardU7day, _cardUAlltime, _cardW7day, _cardWAlltime;
	SlideOption _soption;
	Text _rtitle;
	Scene _scene;
	int _mode;
	int _score;
	ArrayList<Entity> _entities = new ArrayList<Entity>();
	boolean _signinAcceptClick = true;
	
	String[] descs = new String[] {"last 7 days", "all time"};
	
	public StatSlideReport(GameActivity game, Scene scene, int mode, int score) {
		super(0, 0, 470, 445, game.getVertexBufferObjectManager());
		setColor(1f, 1f, 1f);
		_game = game;
		_scene = scene;
		_mode = mode;
		_score = score;
		
		_rectTitle = new Rectangle(0, 0, 470, 60, game.getVertexBufferObjectManager());
		_rectTitle.setColor(140f/255f, 186f/255f, 255f/255f);
		attachChild(_rectTitle);
		_entities.add(_rectTitle);
		
		
		List<Pair> uWeek = _game.scoreCenter.getUserTop10Weekly(_mode);
		ArrayList<Entity> cards = new ArrayList<Entity>();
		_cardU7day = new TwoColumnCard(game, 65, 10, 330, 370, uWeek);
		cards.add(_cardU7day);
		_entities.add(_cardU7day);
		
		List<Pair> uAllTime = _game.scoreCenter.getUserTop10AllTime(_mode);
		_cardUAlltime = new TwoColumnCard(game, 65, 10, 330, 370, uAllTime);
		cards.add(_cardUAlltime);
		_entities.add(_cardUAlltime);
		
		_soption = new SlideOption(game, 460, 420, cards, 0, false, this);
		_soption.setPosition(5, 50);
		attachChild(_soption);
		_entities.add(_soption);
		
		scene.registerTouchArea(_soption);
	}
		
	@Override
	public void onOptionSwitch(final int index) {
		if (_rtitle == null) {
			_rtitle = new Text(0, 0, _game.res.font28, "",
					40, _game.getVertexBufferObjectManager());
			_rtitle.setColor(0, 0, 0);
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

	@Override
	public void onOptionClick(int index, float x, float y) {
		;
	}
	
	public void reset() {
		_signinAcceptClick = true;
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		_scene.unregisterTouchArea(_soption);
		
		for (Entity e : _entities) {
			e.detachSelf();
			e.dispose();
		}
		
		super.dispose();
	}
}
