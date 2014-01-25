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

package com.tmeta.slize.award;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import com.tmeta.slize.GameActivity;

import android.util.SparseArray;

public class AwardThumbMap {
	public BitmapTextureAtlas thumbAtlas;
	private SparseArray<int[]> map;
	
	public AwardThumbMap(GameActivity game) {
		map = new SparseArray<int[]>();
		
		map.put(1, new int[] {213, 0});
		map.put(2, new int[] {284, 0});
		map.put(3, new int[] {355, 0});
		map.put(4, new int[] {426, 0});
		
		map.put(5, new int[] {0, 71});
		map.put(6, new int[] {71, 71});
		map.put(7, new int[] {142, 71});
		map.put(8, new int[] {213, 71});
		map.put(9, new int[] {284, 71});
		map.put(11, new int[] {355, 71});
		map.put(12, new int[] {426, 71});
		
		map.put(13, new int[] {0, 142});
		map.put(14, new int[] {71, 142});
		map.put(15, new int[] {142, 142});
		map.put(16, new int[] {213, 142});
		map.put(17, new int[] {284, 142});
		map.put(18, new int[] {355, 142});
		map.put(19, new int[] {426, 142});
		
		
		map.put(20, new int[] {0, 213});
		map.put(21, new int[] {71, 213});
		map.put(22, new int[] {142, 213});
		map.put(23, new int[] {213, 213});
		map.put(26, new int[] {284, 213});
		map.put(27, new int[] {355, 213});
		map.put(28, new int[] {426, 213});
		
		map.put(29, new int[] {0, 284});
		map.put(30, new int[] {71, 284});
		map.put(31, new int[] {142, 284});
		map.put(32, new int[] {213, 284});
		map.put(33, new int[] {284, 284});
		map.put(38, new int[] {355, 284});
		map.put(39, new int[] {426, 284});

		map.put(40, new int[] {0, 355});
		map.put(41, new int[] {71, 355});
		
		thumbAtlas = new BitmapTextureAtlas(game.getTextureManager(), 512, 512, TextureOptions.REPEATING_NEAREST);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(thumbAtlas, game, "thumbs.png", 0, 0);
		thumbAtlas.load();
	}
	
	public TextureRegion getGoogleLockTR() {
		return TextureRegionFactory.extractFromTexture(thumbAtlas, 142, 0, 70, 70);
	}
	
	public TextureRegion getLockTR() {
		return TextureRegionFactory.extractFromTexture(thumbAtlas, 71, 0, 70, 70);
	}

	public TextureRegion getAvailableTR() {
		return TextureRegionFactory.extractFromTexture(thumbAtlas, 0, 0, 70, 70);
	}

	
	public TextureRegion getTR(int awardId) {
		int[] xy = map.get(awardId);
		TextureRegion r = TextureRegionFactory.extractFromTexture(thumbAtlas, xy[0], xy[1], 70, 70);
		return r;		
	}
	

}
