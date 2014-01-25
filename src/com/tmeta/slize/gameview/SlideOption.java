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
import java.util.HashMap;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.tmeta.slize.GameActivity;

public class SlideOption extends Rectangle {

	Sprite _lt, _rt, _lt0, _rt0;
	ArrayList<Rectangle> _rectOptions;
	HashMap<Rectangle, Entity> _map;
	SlideOptionListener _listener;
	private boolean clickAccept = true, enabled = true;;
	int _current;
	float _height, _width;
	
	public interface SlideOptionListener {
		void onOptionClick(int index, float x, float y);
		void onOptionSwitch(int index);
	}
	
	public SlideOption(GameActivity game, float width, float height, List<Entity> options, int idx, boolean anim, SlideOptionListener listener) {
		super(0, 0, width, height, game.getVertexBufferObjectManager());
		this.setColor(1, 1, 1);
		this.setAlpha(0);
		_listener = listener;
		_height = height;
		_width = width;
		_lt = new Sprite(0, 0, game.res.ltTR, game.getVertexBufferObjectManager());
		_rt = new Sprite(0, 0, game.res.ltTR, game.getVertexBufferObjectManager());
		_rt.setRotation(180);

		_lt0 = new Sprite(0, 0, game.res.ltTR, game.getVertexBufferObjectManager());
		_rt0 = new Sprite(0, 0, game.res.ltTR, game.getVertexBufferObjectManager());
		_rt0.setRotation(180);

		float sy = (height - _lt.getHeight()) / 2;
		_lt0.setPosition(10, sy);
		_rt0.setPosition(width - _lt.getWidth() - 10, sy);
		_lt.setPosition(10, sy);
		_rt.setPosition(width - _lt.getWidth() - 10, sy);
		
		attachChild(_lt0);
		attachChild(_rt0);
		attachChild(_lt);
		attachChild(_rt);
		
		_rectOptions = new ArrayList<Rectangle>();
		_map = new HashMap<Rectangle, Entity>();
		
		float sx = _lt.getWidth() + 20;
		float wth = width - (sx * 2);
		for (Entity e : options) {
			Rectangle r = new Rectangle(sx, 0, wth, height, game.getVertexBufferObjectManager());
			r.setAlpha(0);
			e.setPosition(e.getX() - sx, e.getY());
			e.setAlpha(0);
			r.attachChild(e);
			r.setScaleY(0);
			attachChild(r);
			_rectOptions.add(r);
			_map.put(r,  e);
		}
		
		_current = idx;
		Rectangle r = _rectOptions.get(_current);
		if (anim)
			r.registerEntityModifier(new ScaleModifier(0.1f, 0, 1, 1, 1));
		else
			r.setScale(1);
		_map.get(r).setAlpha(1);
		if (_listener != null)
			_listener.onOptionSwitch(idx);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		if (enabled && clickAccept && pSceneTouchEvent.isActionUp()) {
			clickAccept = false;
			
			if (x >= 0 && x <= _lt.getWidth() + 20) {
				_leftAction();
				return true;
			}
			
			if (x <= _width && x >= _width - _lt.getWidth() - 20) {
				_rightAction();
				return true;
			}
			
			_optionAction(x - _lt.getWidth() - 20, y);
		}
		return false;
	}
	
	private void _optionAction(float x, float y) {
		if (_listener != null)
			_listener.onOptionClick(_current, x, y);
		clickAccept = true;
	}
	
	private void _show(final int idx) {
		clickAccept = false;
		final Rectangle c = _rectOptions.get(_current);
		c.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 1, 0) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				final Rectangle r = _rectOptions.get(idx);
				r.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, 0, 1) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						_current = idx;
						clickAccept = true;
						if (_listener != null)
							_listener.onOptionSwitch(idx);
					}
				});
				_map.get(r).setAlpha(1);
				_map.get(c).setAlpha(1);
			}
		});
	}
	
	private void _leftAction() {
		_lt0.registerEntityModifier(
				new ParallelEntityModifier(
					new MoveXModifier(0.3f, _lt.getX(), _lt.getX() - 20),
					new AlphaModifier(0.3f, 1, 0f),
					new ScaleModifier(0.3f, 1f, 2)
				));
		int toshow = (_current == 0) ? _rectOptions.size() - 1 : _current - 1;
		_show(toshow);	
	}
	
	private void _rightAction() {
		_rt0.registerEntityModifier(
				new ParallelEntityModifier(
						new MoveXModifier(0.3f, _rt.getX(), _rt.getX() + 20),
						new AlphaModifier(0.3f, 1, 0f),
						new ScaleModifier(0.3f, 1f, 2)
				));
		int toshow = (_current == _rectOptions.size() - 1) ? 0 : _current + 1;
		_show(toshow);	
	}
	

	
	public void setEnable(boolean f) {
		enabled = f;
	}
	
	public Entity getOption(int idx) {
		return _map.get(_rectOptions.get(idx));
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		super.dispose();
		
		_lt.detachSelf();
		_lt.dispose();
		
		_rt.detachSelf();
		_rt.dispose();
		
		_lt0.detachSelf();
		_lt0.dispose();
		
		_rt0.detachSelf();
		_rt0.dispose();
		
		for (Rectangle r : _rectOptions) {
			r.detachSelf();
			r.dispose();
		}

	}
	
}
