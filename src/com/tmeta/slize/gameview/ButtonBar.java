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
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TextureRegion;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.gameview.LabelButton.LabelButtonListener;

public class ButtonBar extends Rectangle {
	
	GameActivity _game;
	ArrayList<LabelButton> _btts;
	ArrayList<Entity> _entities = new ArrayList<Entity>();
	Scene _scene;
	LabelButtonListener _listener;
	
	
	public ButtonBar(GameActivity game, Scene scene, float x, float y, float width, float height, LabelButtonListener listener) {
		super(x, y, width, height, game.getVertexBufferObjectManager());
		setColor(240f/255f, 240f/255f, 240f/255f);
		_btts = new ArrayList<LabelButton>();
		_game = game;
		_scene = scene;
		_listener = listener;
	}
	
	public LabelButton addButton(TextureRegion tr, String s) {
		return addButton(tr, s, 0.2f);
	}
	
	public LabelButton addButton(TextureRegion tr, String s, float delay) {
		LabelButton btt = new LabelButton(_game, 0, 0, 0, 0, s, tr, _game.res.font28, _listener);
		btt.setBackgroundAlpha(0);
		btt.setDelay(delay);
		_btts.add(btt);
		return btt;
	}
	
	public void finalize() {
		int numBtt = _btts.size();
		float width = getWidth();
		float height = getHeight();
		float bwidth = width / (float)numBtt;
		for (float x = 0; x < 5; x++) {
			Rectangle l = new Rectangle(x * bwidth - (x == 0 ? 1 : 0), 0, 2, height, _game.getVertexBufferObjectManager());
			float c = (x == 0 || x == 4) ? 215f/255f : 230f/255f;
			l.setColor(c, c, c);
			attachChild(l);
			_entities.add(l);
		}
		
		float x = 0;
		for (LabelButton b : _btts) {
			b.setSize(bwidth - 2, height -2);
			b.setPosition(x + 1, 1);
			attachChild(b);
			_scene.registerTouchArea(b);
			x += bwidth;
		}

		Rectangle l = new Rectangle(0, 0, width, 2, _game.getVertexBufferObjectManager());
		l.setColor(230f/255f, 230f/255f, 230f/255f);
		attachChild(l);
		_entities.add(l);
		
		Rectangle l2 = new Rectangle(0, height, width, 2, _game.getVertexBufferObjectManager());
		l2.setColor(215f/255f, 215f/255f, 215f/255f);
		attachChild(l2);
		_entities.add(l2);
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		for (LabelButton b : _btts) {
			_scene.unregisterTouchArea(b);
			b.detachSelf();
			b.dispose();
		}
		
		for (Entity e : _entities) {
			e.detachSelf();
			e.dispose();
		}
		
		
		super.dispose();
	}
}
