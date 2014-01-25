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

package com.tmeta.slize.booster;

import com.tmeta.slize.booster.IBoosterExecutable.BoosterTyp;

public class AddEmptyInfo implements IBoosterInfo {

	@Override
	public BoosterTyp getTyp() {
		return IBoosterExecutable.BoosterTyp.EMPTY;
	}

	@Override
	public int getCostPerBundle() {
		return 500;
	}

	@Override
	public int getItemPerBundle() {
		return 5;
	}

	@Override
	public String getName() {
		return "nullifier";
	}

	@Override
	public String getFirstLine() {
		return "turn selected dot into\nempty space";
	}

	@Override
	public String getSecondLine() {
		return "";
	}
	
	@Override
	public String getPnm() {
		return "empty.pnm";
	}

}
