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

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.award.IAward;

public class AwardCard extends Rectangle {

	float _width, _height;
	ArrayList<IAward> _awards = new ArrayList<IAward>();
	GameActivity _game;
	IAward _award;
	ArrayList<AwardCardEntry> _aEntries = new ArrayList<AwardCardEntry>();
	
	
	
	public AwardCard(GameActivity game, float x, float y, float width, float height) {
		super(x, y, width, height, game.getVertexBufferObjectManager());
		setColor(1, 1, 1);
		_width = width;
		_height = height;
		_game = game;
	}
	
	public void addAward(IAward award) {
		_awards.add(award);
	}
	
	public void finalize() {
		float y = 10;
		for (IAward a : _awards) {
			AwardCardEntry ae = new AwardCardEntry(_game, 0, y, _width, a);
			attachChild(ae);
			_aEntries.add(ae);
			y += 90;
		}

	}
	
	public AwardCardEntry getClickedEntry(float x, float y) {
		for (AwardCardEntry ae : _aEntries) {
			if (ae.isInbound(x, y))
				return ae;
		}
		return null;
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		for (Entity e : _aEntries) {
			e.detachSelf();
			e.dispose();
		}
		
		super.dispose();
	}
}
