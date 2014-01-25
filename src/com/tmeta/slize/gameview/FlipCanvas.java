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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;

import com.tmeta.slize.GameActivity;

public class FlipCanvas extends Rectangle {
	private static final int SIZE = 20;
	
	Rectangle[][] _rect;
	int _w, _h;
	GameActivity _game;
	String _pnm;
	
	
	public interface Frame {
		int[] getImage();
	}
	
	
	public class PnmReader implements Frame {

		int[] img;
		
		public PnmReader(GameActivity game, String finame, int w, int h) {
			img = new int[w * h * 3];
			_game = game;
			InputStream in;
			try {
				in = game.getAssets().open("pnm/" + finame);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				reader.readLine();
				reader.readLine();
				reader.readLine();
				reader.readLine();
				int i = 0;
				String s;
				int v;
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						s = reader.readLine();
						v = Integer.parseInt(s);
						img[i++] = v;
						s = reader.readLine();
						v = Integer.parseInt(s);
						img[i++] = v;
						s = reader.readLine();
						v = Integer.parseInt(s);
						img[i++] = v;
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				
			}

		}
		
		@Override
		public int[] getImage() {
			return img;
		}
		
	}
	
	public FlipCanvas(GameActivity game, int w, int h, String pnm) {
		super(0, 0, w * SIZE, h * SIZE, game.getVertexBufferObjectManager());
		setColor(0.9f, 0.9f, 0.9f);
		_game = game;
		_pnm = pnm;
		_w = w;
		_h = h;
		_rect = new Rectangle[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				_rect[x][y] = new Rectangle(x * SIZE + 1, y * SIZE + 1, SIZE-2, SIZE-2, game.getVertexBufferObjectManager());
				_rect[x][y].setColor(0.95f, 0.95f, 0.95f);
				attachChild(_rect[x][y]);
			}
		}
	}
	
	public void update() {
		PnmReader pnm = new PnmReader(_game, _pnm, _w, _h);
		int[] map = pnm.getImage();
		int i = 0;
		for (int y = 0; y < _h; y++) {
			for (int x = 0; x < _w; x++) {
				Rectangle rect = _rect[x][y];
				float r = (float)map[i++]/255f;
				float g = (float)map[i++]/255f;
				float b = (float)map[i++]/255f;
				rect.registerEntityModifier(new SequenceEntityModifier(
						new DelayModifier(0.1f + (float)y/20f),
						new ColorModifier(0.02f + (float)y/30f, 0.1f, r, 0.1f, g, 0.1f, b)
						));
			}
		}
	}
	
	public void update(String pnm) {
		_pnm = pnm;
		update();
	}
	
	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		for (int y = 0; y < _h; y++) {
			for (int x = 0; x < _w; x++) {
				Rectangle r = _rect[x][y];
				r.detachSelf();
				r.dispose();
			}
		}
		super.dispose();
	}
	
}
