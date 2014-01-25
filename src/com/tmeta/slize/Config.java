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

public class Config {
	
	public static final int NUM_GEM = 6;

	public static final int MODE_20M = 3;
	public static final int MODE_60S = 1;
	public static final int MODE_120S = 2;
	
	public static final int FULLTIME = 60;
	public static final int WARNING_TIME = 5;
	
	public static final int WIN_LENGTH = 4;
	public static final int COMBO_LENGTH = 6;
	
	public static final int RESTRICT_DURATION = 3000;
		
	public static final int BOARD_WIDTH = 7;
	public static final int BOARD_HEIGHT = 7;
		
	public static float SCREEN_HEIGHT = 800;
	public static float SCREEN_WIDTH = 480;
	
	public static float PIECE_WIDTH = 65;
	public static float PIECE_HEIGHT = 65;

	public static float DROP_SPEED = 0.5f;
	
	private Config() {
		;
	}
	
}