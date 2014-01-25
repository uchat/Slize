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

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.award.IAward;

public class AwardCardEntry extends Rectangle {
	Sprite s;
	Text name, bonus;
	IAward _award;
	GameActivity _game;
	
	public AwardCardEntry(GameActivity game, float x, float y, float width, IAward award) {
		super(x, y, width, 70, game.getVertexBufferObjectManager());
		setColor(1,1,1);
		_award = award;
		_game = game;
		int lock = award.getLock();
		if (! _game.awardCenter.isWon(lock)) {
			makeLock(lock);
		} else {		
			makeAvailable();
		}
	}
	
	private String _tryGetHeader(IAward a) {
		if (a.getId() == IAward.LOCAL_HIGH_SCORE)
			return "High Score";
		
		return a.getHeader();
	}
	
	public boolean isInbound(float x, float y) {
		return x > getX() + 5 && y > getY() && x < getX() + getWidth() - 5 && y < getY() + getHeight();
	}
	
	public void flash() {
		this.registerEntityModifier(new ColorModifier(0.3f, _game.res.COLOR[0], new Color(1,1,1)));
	}
	
	public IAward getAward() {
		return _award;
	}
	
	private void makeLock(int lock) {
		s = new Sprite(0, 0, _game.thumbMap.getLockTR(), _game.getVertexBufferObjectManager());	
		attachChild(s);
		addText(new Color(0.7f, 0.7f, 0.7f), new Color(0.7f, 0.7f, 0.7f));
	}
	
	private void makeAvailable() {
		if (_game.awardCenter.isWon(_award.getId()))
			s = new Sprite(0, 0, _game.thumbMap.getTR(_award.getId()), _game.getVertexBufferObjectManager());
		else
			s = new Sprite(0, 0, _game.thumbMap.getAvailableTR(), _game.getVertexBufferObjectManager());	
		
		attachChild(s);
		addText(new Color(0.4f, 0.4f, 0.4f), _game.res.COLOR[4]);
	}
	
	private void addText(Color c, Color b) {
		String str = _tryGetHeader(_award);
		if (str.length() > 19) {
			str = str.substring(0, 19) + "...";
		}
		name = new Text(80, 4, _game.res.font28, str, str.length(), _game.getVertexBufferObjectManager());
		name.setColor(c);
		name.setScale(0.9f);
		attachChild(name);
		
		str = "+" + _award.getPointBonus() + " points";
		bonus = new Text(80, 40, _game.res.font28, str, str.length(), _game.getVertexBufferObjectManager());
		bonus.setColor(b);
		bonus.setScale(0.9f);
		attachChild(bonus);
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		s.detachSelf();
		s.dispose();
		
		name.detachSelf();
		name.dispose();
		
		bonus.detachSelf();
		bonus.dispose();
		
		super.dispose();
	}
}