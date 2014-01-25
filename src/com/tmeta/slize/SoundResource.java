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

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

public class SoundResource {
	public Sound MOVE;
	public Sound TIME;
	public Sound DROP, DROP6;
	public Sound TACK, SLIDE, LOST, WOOSH;
	
	long groupDelay = 0, tackDelay = 0;
	
	private boolean mSoundEnabled = true;
	GameActivity _game;
	
	public SoundResource(GameActivity game) {
		_game = game;
		SoundFactory.setAssetBasePath("mfx/");
		SoundManager sm = _game.getEngine().getSoundManager();
		try {
			MOVE = SoundFactory.createSoundFromAsset(sm, _game, "click_move.ogg");
			DROP = SoundFactory.createSoundFromAsset(sm, _game, "group.ogg");
			DROP6 = SoundFactory.createSoundFromAsset(sm, _game, "group6.ogg");
			TIME = SoundFactory.createSoundFromAsset(sm, _game, "time.ogg");
			TACK = SoundFactory.createSoundFromAsset(sm, _game, "tack.ogg");
			SLIDE = SoundFactory.createSoundFromAsset(sm, _game, "slide.ogg");
			LOST = SoundFactory.createSoundFromAsset(sm, _game, "lost.ogg");
			WOOSH = SoundFactory.createSoundFromAsset(sm, _game, "woosh.ogg");
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
		
	public  void play(Sound sound) {
		if (mSoundEnabled) {
			long t1 = System.currentTimeMillis();
			if (sound == DROP ) {
				if (t1 - groupDelay > 50) {
					sound.play();
					groupDelay = t1;
				}
				return;
			}			
			
			if (sound == TACK) {
				if (t1 - tackDelay > 50) {
					sound.play();
					tackDelay = t1;
				}
				return;
			}			
			sound.play();
		}
	}
		
	public synchronized void stop(Sound sound) {
		sound.stop();
	}
	
	public void setSoundEnabled(boolean v) {
		mSoundEnabled = v;
	}
		
	public boolean isSoundEnabled() {
		return mSoundEnabled;
	}


	
}



