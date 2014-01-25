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

import java.util.HashMap;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import com.tmeta.slize.EntityPool;
import com.tmeta.slize.EntityPool.WEntity;
import com.tmeta.slize.GameActivity;

public class BorderPool {
	
	GameActivity _game;
	EntityPool _barPool, _turnPool, _endPool;
	HashMap<Entity, WEntity> map = new HashMap<Entity, WEntity>();
	
	private class BarFac implements EntityPool.EFactory {
		@Override
		public Entity create() {
			return new Sprite(0, 0, _game.res.borderBarTR, _game.getVertexBufferObjectManager());
		}
	}
	
	private class TurnFac implements EntityPool.EFactory {
		@Override
		public Entity create() {
			return new Sprite(0, 0, _game.res.borderTurnTR, _game.getVertexBufferObjectManager());
		}
	}
	
	private class EndFac implements EntityPool.EFactory {
		@Override
		public Entity create() {
			return new Sprite(0, 0, _game.res.borderEndTR, _game.getVertexBufferObjectManager());
		}
	}
	
	
	public BorderPool(GameActivity game) {
		_game = game;
		_barPool = new EntityPool(_game, new BarFac(), 10);
		_turnPool = new EntityPool(_game, new TurnFac(), 10);
		_endPool = new EntityPool(_game, new EndFac(), 2);
	}
	
	public Sprite getBarSprite() {
		WEntity we = _barPool.acquire();
		map.put(we.entity, we);
		return (Sprite)we.entity;
	}
	
	public Sprite getTurnSprite() {
		WEntity we = _turnPool.acquire();
		map.put(we.entity, we);
		return (Sprite)we.entity;
	}
	
	public Sprite getEndSprite() {
		WEntity we = _endPool.acquire();
		map.put(we.entity, we);
		return (Sprite)we.entity;
	}
	
	public void release(Sprite s) {
		WEntity we = map.get(s);
		if (we != null) {
			s.reset();
			we.release();
		}
	}
}
