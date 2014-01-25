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

import java.util.Comparator;


public class Pair {
	
	public static class CustomComparator implements Comparator<Pair> {
	    @Override
	    public int compare(Pair o1, Pair o2) {
	        int d1 = o1.value;
	        int d2 = o2.value;
	        
	        if (d1 > d2)
	        	return 1;
	        if (d1 < d2)
	        	return -1;
	        return 0;
	    }
	}

	
	public String key;
	public int value;
	
	public Pair(String key, int value) {
		this.key = key;
		this.value = value;
	}
}
