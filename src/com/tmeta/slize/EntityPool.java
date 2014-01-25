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

package com.tmeta.slize;

import java.util.Vector;

import org.andengine.entity.Entity;

public class EntityPool {
	
	public class WEntity  {
		private EntityPool _pool;
		public Entity entity;
		
		protected WEntity(Entity e, EntityPool pool) {
			entity = e;
			_pool = pool;
		}
				
		public void release() {
			_pool.release(this);	
		}
	}
	
	public interface EFactory {
		Entity create();
	}
	
	GameActivity _game;
	private Vector<WEntity> _pool;
	private EFactory _factory;

	public EntityPool(GameActivity game, EFactory factory, int numInit) {
		_game = game;
		_factory = factory;
		_pool = new Vector<WEntity>();
		
		for (int i = 0; i < numInit; i++) {
			_pool.add(new WEntity(_factory.create(), this));
		}
	}
	

	public WEntity acquire() {
		if (_pool.size() == 0) {
			_pool.add(new WEntity(_factory.create(), this));
		}
		return _pool.remove(0);
	}
	
	
	public void release(WEntity e) {
		_pool.add(e);
	}
	
}
