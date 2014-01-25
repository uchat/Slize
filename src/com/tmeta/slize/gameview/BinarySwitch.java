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
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import com.tmeta.slize.GameActivity;

public class BinarySwitch extends Rectangle {
	
	Text _label, _on, _off;
	GameActivity _game;
	boolean _status;
	SwitchClickedListener _listener;
	
	public interface SwitchClickedListener {
		void onSwitchClicked(boolean state);
	}
	
	public BinarySwitch(GameActivity game, float x, float y, float width, float height, String str, boolean status, SwitchClickedListener listener ) {
		super(x, y, width, height, game.getVertexBufferObjectManager());
		
		setColor(1, 1, 1);
		_listener = listener;
		
		_game = game;
		_label = new Text(10, 0, game.res.font28, str,
				str.length(),
				game.getVertexBufferObjectManager());
		_label.setColor(0.2f, 0.2f, 0.2f);
		_label.setPosition(10, (height - _label.getHeight()) / 2);

		_off = new Text(0, 0, game.res.font28, "off",
				"off".length(),
				game.getVertexBufferObjectManager());
		float rx = width - _off.getWidth() - 10;
		_off.setPosition(rx, (height - _off.getHeight()) / 2);
		_off.setScaleCenterY(_off.getHeight() / 2);
		
		_on = new Text(0, 0, game.res.font28, "on",
				"on".length(),
				game.getVertexBufferObjectManager());
		rx = rx - _on.getWidth() - 10;
		_on.setPosition(rx, (height - _on.getHeight()) / 2);
		_on.setScaleCenterY(_on.getHeight() / 2);
		
		attachChild(_label);
		attachChild(_off);
		attachChild(_on);
		
		_status = status;
		_doSetStatus(_status);
	}
	
	private void _doSetStatus(boolean v) {
		if (v) {
			_on.setColor(165f/255f, 203f/255f, 99f/255f);
			_off.setColor(200f/255f, 200f/255f, 200f/255f);
			_on.setScale(1.2f);
			_off.setScale(1);
		} else {
			_off.setColor(165f/255f, 203f/255f, 99f/255f);
			_on.setColor(200f/255f, 200f/255f, 200f/255f);
			_off.setScale(1.2f);
			_on.setScale(1);

		}
	}
	
	public void setStatus(boolean v) {
		_status = v;
		this.registerEntityModifier(new ColorModifier(0.3f, _game.res.COLOR[0], new Color(1, 1, 1)));
		_doSetStatus(v);
		if (_listener != null) {
			_listener.onSwitchClicked(v);
		}
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		if (pSceneTouchEvent.isActionUp()) {
			setStatus(! _status);
		}
		return true;
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		_label.detachSelf();
		_on.detachSelf();
		_off.detachSelf();
		
		_label.dispose();
		_on.dispose();
		_off.dispose();
		
		super.dispose();
	}

}
