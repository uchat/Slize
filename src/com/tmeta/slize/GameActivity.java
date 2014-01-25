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


import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.tmeta.slize.award.AwardCenter;
import com.tmeta.slize.award.AwardThumbMap;
import com.tmeta.slize.db.DbHandler;
import com.tmeta.slize.panel.FrontPanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;


public class GameActivity extends SimpleBaseGameActivity implements Spanel.SpListener {
	
	private Scene _scene;
	public GameResource res;
	public AwardThumbMap thumbMap;
	public AwardCenter awardCenter;
	private Vibrator _vibrator;
	Spanel currentPanel, popupPanel;
	boolean _ready, _vibrate;
	public ScoreCenter scoreCenter;
	public DbHandler dbHandler;
	public SoundResource sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentPanel = null;
        FrontPanel.LAST_ACTIVE_OPTION = 0;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	if (popupPanel != null) {
    		popupPanel.onPause();
    		return;
    	}
    	if (currentPanel != null)
    		currentPanel.onPause();
    }
	
    @Override
    public void onResumeGame() {
    	if (this.mEngine == null) {
    		Intent i = getBaseContext().getPackageManager()
    	             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(i);
    		return;
    	}
    	super.onResumeGame();
    }
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		
		final EngineOptions eo = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, 
			    new RatioResolutionPolicy(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT), camera);
		eo.getAudioOptions().setNeedsSound(true);
		return eo;
	}

	@Override
	protected void onCreateResources() {
		dbHandler = new DbHandler(this);
		res = new GameResource(this);
		sound = new SoundResource(this);
		thumbMap = new AwardThumbMap(this);
		scoreCenter = new ScoreCenter(this);
		awardCenter = new AwardCenter(this);
		_vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		_vibrate = dbHandler.isVibrationEnable();
		sound.setSoundEnabled(dbHandler.isSoundEnable());
	}

	@Override
	protected Scene onCreateScene() {
		_scene = new Scene();
		
		Rectangle bkg = new Rectangle(0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float x, float y) {
				if (currentPanel != null)
					return currentPanel.onBackgroundTouched(pSceneTouchEvent, x, y);
				return false;
			}
		};
		bkg.setColor(223f/255f, 223f/255f, 223f/255f);
		_scene.attachChild(bkg);
		_scene.registerTouchArea(bkg);
						
		switchPanel(new FrontPanel());
    	
		return _scene;
	}
	
	public void setSoundEnable(boolean state) {
		this.sound.setSoundEnabled(state);
		dbHandler.setSoundEnable(state ? 1 : 0);
	}
	
	public void setVibrateEnable(boolean state) {
		_vibrate = state;
		dbHandler.setVibrationEnable(state ? 1 : 0);
	}
	
	
	public void vibrate(int t) {
		if (_vibrate)
			_vibrator.vibrate(60);
	}
	
	public void popupPanel(final Spanel popup) {
		if (currentPanel == null)
			throw new RuntimeException("Must have base panel to show popup panel");
		if (popupPanel != null)
			return;
		sound.play(sound.SLIDE);
		popupPanel = popup;				
		_ready = false;
		final Spanel.SpListener oldListener = currentPanel.getListener();
		popup.create(this, _scene);
		popup.setPosition(0, 0);
		popup.setListener(new Spanel.SpListener() {
			@Override
			public void switchInFinished(Spanel sp, Object payload) {
				_ready = true;
			}

			@Override
			public void switchOutFinish(Spanel sp, Object payload) {
				currentPanel.switchIn(payload);
				popupPanel = null;
			}	
		});

		currentPanel.setListener(new Spanel.SpListener() {
			@Override
			public void switchInFinished(Spanel sp, Object payload) {
				_ready = true;
				currentPanel.setListener(oldListener);
				
				getEngine().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						popup.detachSelf();
						popup.dispose();
					}
				});
			}

			@Override
			public void switchOutFinish(Spanel sp, Object payload) {
				_scene.attachChild(popup);
				popup.switchIn(null);
			}
		});
		
		currentPanel.switchOut(popup);
	}
	
	public void dismissPopup() {
		if (popupPanel != null) {
			if (! _ready)
				return;
			sound.play(sound.SLIDE);
			_ready = false;
			popupPanel.switchOut(null);
			popupPanel = null;
		}
	}
	
	public void centerText(float width, Text t) {
		float w = t.getWidthScaled();
		t.setPosition((width - w) / 2, t.getY());
	}
	
	public void click(Entity e) {
		e.registerEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(0.05f, 1, 0.9f),
						new ScaleModifier(0.1f, 0.9f, 1)
						));
	}
	
	public void switchPanel(Spanel panel) {
		panel.create(this, _scene);
		panel.setPosition(0, 0);
		panel.setListener(this);
		_ready = false;
		sound.play(sound.SLIDE);

		if (popupPanel != null) {
			final Spanel s = currentPanel;
			currentPanel = popupPanel;
			popupPanel = null;
			currentPanel.setListener(this);
			getEngine().runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					s.dispose();
				}
			});
		}
		
		if (currentPanel != null) {
			currentPanel.switchOut(panel);
		} else {
			_scene.attachChild(panel);
			panel.switchIn(null);
		}
		currentPanel = panel;
	}

	@Override
	public void switchInFinished(Spanel sp, Object payload) {
		_ready = true;
	}

	@Override
	public void switchOutFinish(final Spanel sp, final Object payload) {
		if (payload != null) {
			Spanel panel = (Spanel)payload;
			_scene.attachChild(panel);
			((Spanel)payload).switchIn(null);
		}
		
		getEngine().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				sp.detachSelf();
				sp.dispose();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (! _ready)
			return;
		
		if (popupPanel != null) {
			dismissPopup();
			return;
		}
		
		if (currentPanel != null) {
			if (! currentPanel.onBackpressed()) {
				finish();
			}
		} else {
			finish();
		}
	}
}
