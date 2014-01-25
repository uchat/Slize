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
import java.util.Collections;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;

import com.tmeta.slize.GameActivity;
import com.tmeta.slize.Pair;

public class TwoColumnCard extends Rectangle {
	
	float _width, _height;
	ArrayList<Text> texts = new ArrayList<Text>();
	
	public TwoColumnCard(GameActivity game, float x, float y, float width, float height, List<Pair> pairs) {
		super(x, y, width, height, game.getVertexBufferObjectManager());
		_width = width;
		_height = height;
		
		int i = 0;
		Collections.sort(pairs, new Pair.CustomComparator());
		Collections.reverse(pairs);
		for (Pair p : pairs) {
			String s = p.key;
			Text t = new Text(30, i * 35 + 25, game.res.font28, s,
					s.length(),
					game.getVertexBufferObjectManager());
			t.setColor(0.1f, 0.1f, 0.1f);
			t.setScale(0.9f);
			texts.add(t);
			
			s = p.value + "";
			Text t2 = new Text(width - 70, i * 35 + 30, game.res.font28, s,
					s.length(),
					game.getVertexBufferObjectManager());
			t2.setColor(0.5f, 0.5f, 0.5f);
			t2.setScale(0.9f);
			texts.add(t2);

			attachChild(t);
			attachChild(t2);
			i++;
		}
		
		for (int j = i; j < 10; j++) {
			Text t = new Text(30, j * 35 + 25, game.res.font28, "--",
					"--".length(),
					game.getVertexBufferObjectManager());
			t.setColor(0.1f, 0.1f, 0.1f);
			t.setScale(0.9f);
			texts.add(t);
			
			Text t2 = new Text(width - 70, j * 35 + 30, game.res.font28, "--",
					"--".length(),
					game.getVertexBufferObjectManager());
			t2.setColor(0.5f, 0.5f, 0.5f);
			t2.setScale(0.9f);
			texts.add(t2);
			
			attachChild(t);
			attachChild(t2);
		}
	}

	@Override
	public void dispose() {
		if (super.isDisposed())
			return;
		
		for (Text t : texts) {
			t.detachSelf();
			t.dispose();
		}
		
		super.dispose();
	}
	
	
}
