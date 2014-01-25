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

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.tmeta.slize.GameActivity;

public class LabelValueBox extends Entity {
	
	GameActivity _game;
	Text _label, _num;
	int _numLen = 0;
	
	private final String[] pads = {"0", "00", "000", "0000"};
	
	public LabelValueBox(GameActivity game, String text) {
		this(game, text, false);
	}

	
	public LabelValueBox(GameActivity game, String text, boolean ontop) {
		_game = game;
		_label = new Text(ontop ? 5 : 0, 
						  ontop ? 0 : 18, 
						  game.res.font28, text,
						  text.length() * 2,
						  game.getVertexBufferObjectManager());
		float tx = ontop ? 0 : _label.getWidthScaled() + 10;
		_num = new Text(tx, 
						ontop ? _label.getHeight() : 0,
						game.res.font56, "0123456789",
						"0123456789".length(),
						game.getVertexBufferObjectManager());
		_num.setText("0");
		_label.setColor(0, 0, 0);
		_num.setColor(0, 0, 0);
		attachChild(_label);
		attachChild(_num);
	}
	
	public void triggerLabelFocus(String str) {
		final String old = _label.getText().toString();
		_label.setText(str);
		_label.setColor(_game.res.COLOR[0]);
		_label.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(0.1f, 1, 1.5f),
						new ScaleModifier(0.1f, 1.5f, 1), 
						new DelayModifier(0.1f)), 5) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				_label.setText(old);
				_label.setColor(0, 0, 0);
			}
		});
	}
	
	public void setLabelColor(Color c) {
		_label.setColor(c);
	}
	
	public void setNumColor(Color c) {
		_num.setColor(c);
	}
	
	public void setLabelColor(float r, float g, float b) {
		_label.setColor(r, g, b);
	}
	
	public void setNumColor(float r, float g, float b) {
		_num.setColor(r, g, b);
	}
	
	public void setNumLen(int len) {
		_numLen = len;
	}
	
	public void setAsString(String v) {
		_num.setText(v);
	}

	public void setAsNum(int num) {
		if (_numLen <= 0) {
			_num.setText(String.valueOf(num));
		} else {
			String str = String.valueOf(num);
			int p = _numLen - str.length();
			if (p > 0)
				str = pads[p - 1] + str;
			_num.setText(str);
		}
	}
		
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		_label.detachSelf();
		_label.dispose();
		
		_num.detachSelf();
		_num.dispose();
		
		super.dispose();
	}
	
	
	
}
