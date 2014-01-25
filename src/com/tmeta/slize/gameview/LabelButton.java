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

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseElasticOut;

import com.tmeta.slize.GameActivity;

public class LabelButton extends Rectangle {

	private GameActivity _game;
	LabelButtonListener _listener;
	Text _text;
	String _str;
	Sprite _img;
	float _delay = 0.2f;
	boolean _enabled = true, _clickAccept = true;
	boolean _selfDisable = false;
	
	public interface LabelButtonListener {
		void onButtonClick(LabelButton lb);
	}

	public LabelButton(GameActivity game, float x, float y, float width, 
			float height, String text, TextureRegion tr, Font font) {
		this(game, x, y, width, height, text, tr, font, null);
	}
	
	public LabelButton(GameActivity game, float x, float y, float width, 
			float height, String text, TextureRegion tr, Font font, LabelButtonListener listener) {
		super(x, y, width, height, game.getVertexBufferObjectManager());
		_game = game;
		_listener = listener;
		
		_text = new Text(0, 0, font, text,
				text.length() + 10,
				game.getVertexBufferObjectManager());
		_text.setColor(0, 0, 0);
		_str = text;
		float th = _text.getHeight();
		if (tr != null) {
			_img = new Sprite(0, 0, tr, game.getVertexBufferObjectManager());

			float iw = _img.getWidth();
			float ih = _img.getHeight();
			float oy = (height - ih) / 2;
			float fy = oy - oy / 2;
			_img.setPosition((width - iw) / 2, fy);		
			attachChild(_img);
			_text.setPosition((width - _text.getWidth()) / 2, height - th);
			attachChild(_text);
			
		} else {
			_text.setPosition((width - _text.getWidth()) / 2, (height - th) / 2);
			attachChild(_text);
		}
	}
	
	@Override
	public void setSize(float width, float height) {
		float th = _text.getHeight();
		if (_img != null) {
			float iw = _img.getWidth();
			float ih = _img.getHeight();
			float oy = (height - ih) / 2;
			float fy = oy - oy / 2;
			_img.setPosition((width - iw) / 2, fy);			
			_text.setPosition((width - _text.getWidth()) / 2, height - th);
		} else {
			_text.setPosition((width - _text.getWidth()) / 2, (height - th) / 2);			
		}
				
		super.setSize(width, height);
	}
	
	public void setTextColor(float r, float g, float b) {
		_text.setColor(r, g, b);
	}
	
	public void setTextColor(Color c) {
		_text.setColor(c);
	}

	
	public void setDelay(float v) {
		_delay = v;
	}
	
	public void setBackgroundAlpha(float v) {
		setAlpha(v);
	}
	
	public void setText(String str) {
		_text.setText(str);
		_text.setPosition((this.getWidth() - _text.getWidth()) / 2, _text.getY());

	}
	
	public void setEnabled(boolean f) {
		_enabled = f;
		float alpha = f ? 1 : 0.3f;
		_text.setAlpha(alpha);
		if (_img != null)
			_img.setAlpha(alpha);
	}
	
	public void setSelfDisable(boolean v) {
		_selfDisable = v;
	}
	
	public void excite() {
		_text.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.1f, 1, 1.2f),
						new ScaleModifier(0.3f, 1.2f, 1, EaseElasticOut
								.getInstance()), new DelayModifier(0.8f))));
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float x, float y) {
		if (_clickAccept && _enabled && pSceneTouchEvent.isActionUp()) {
			if (_selfDisable)
				_enabled = false;
			if (_listener != null) {
				_clickAccept = false;
				Color c = this.getColor();
				if (this.getAlpha() == 0) {
					setColor(_game.res.COLOR[0]);
					this.registerEntityModifier(new AlphaModifier(_delay, 1, 0) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							_listener.onButtonClick(LabelButton.this);
							_clickAccept = true;
						}
					});
				} else {
					this.registerEntityModifier(new ColorModifier(_delay, _game.res.COLOR[0], c) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							_listener.onButtonClick(LabelButton.this);
							_clickAccept = true;
						}
					});
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		_text.detachSelf();
		_text.dispose();
		
		if (_img != null) {
			_img.detachSelf();
			_img.dispose();
		}
		
		super.dispose();
	}
}
