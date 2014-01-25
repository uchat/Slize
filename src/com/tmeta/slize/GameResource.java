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

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.color.Color;

public class GameResource {
	
	public final Color COLOR[] = new Color[] {
		new Color(215f/255f, 97f/255f, 99f/255f),
		new Color(173f/255f, 105f/255f, 214f/255f),
		new Color(231f/255f, 223f/255f, 82f/255f),
		new Color(165f/255f, 203f/255f, 99f/255f),
		new Color(107f/255f, 162f/255f, 255f/255f),
		new Color(202f/255f, 207f/255f, 211f/255f),
	};
	
	private GameActivity _game;
	
	public BitmapTextureAtlas thumbAtlas;

	public TextureRegion pieceTR, hiliTR, borderBarTR, borderTurnTR, borderEndTR, ltTR;
	public TextureRegion swapTR, addEmptyTR, removeAllTR, add5TR, pServiceTR, fbTR, logoTR;

	public Font font28, font56;
	
	protected GameResource(GameActivity game) {
	    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");	    
		FontFactory.setAssetBasePath("fonts/");
		_game = game;
		_loadGraphics();
	}
	
	private void _loadGraphics() {
		
		thumbAtlas = new BitmapTextureAtlas(_game.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(thumbAtlas, _game, "map.png", 0, 0);
		thumbAtlas.load();
		
		add5TR = TextureRegionFactory.extractFromTexture(thumbAtlas, 467, 82, 40, 40);
		pieceTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 248, 170, 65, 65);
		hiliTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 400, 0, 65, 65);
		ltTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 467, 0, 40, 80);
		swapTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 467, 166, 40, 40);
		addEmptyTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 467, 124, 40, 40);
		removeAllTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 424, 201, 40, 40);
		borderBarTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 400, 67, 65, 65);
		borderTurnTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 400, 134, 65, 65);		
		borderEndTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 315, 170, 65, 65);
		pServiceTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 0, 170, 246, 54);
		fbTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 382, 201, 40, 40);
		logoTR = TextureRegionFactory.extractFromTexture(thumbAtlas, 0, 0, 398, 168);		
		
		Color cs = new Color(0.0f, 0.0f, 0.0f, 0.75f);
		final ITexture font28Txr = new BitmapTextureAtlas(_game.getTextureManager(), 
				1024, 512, TextureOptions.BILINEAR);
		font28 = FontFactory.createFromAsset(_game.getFontManager(), font28Txr, _game.getAssets(), "CALIBRIL.TTF", 
				28, true, Color.WHITE_ARGB_PACKED_INT);
		font28.load();
		
		final ITexture font56Txr = new BitmapTextureAtlas(_game.getTextureManager(), 
				1024, 512, TextureOptions.BILINEAR);
		font56 = FontFactory.createFromAsset(_game.getFontManager(), font56Txr, _game.getAssets(), "CALIBRIL.TTF", 
				56, true, cs.getARGBPackedInt());
		font56.load();
	}

}
