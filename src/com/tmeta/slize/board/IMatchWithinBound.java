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

package com.tmeta.slize.board;

import java.util.List;


public abstract class IMatchWithinBound implements IMatchPattern {

	private int[][] _pattern;
	
	public IMatchWithinBound(int[][] pattern) {
		_pattern = pattern;
	}

	@Override
	public boolean match(List<Piece> group) {
		if (group.size() != _pattern.length) // we need exact match
			return false;
		
		int[] offset = new int[2];
		int[][] result = toArray(group, offset);
		
		int lx = result.length;
		int ly = result[0].length;
		for (int x = 0; x < lx; x++) {
			for (int y = 0; y < ly; y++) {
				int c = 0;
				for (int[] p : _pattern) {
					int tx = x + p[0];
					int ty = y + p[1];
					if (tx < 0 || tx >= lx || ty < 0 || ty >= ly)
						break;
					if (result[tx][ty] == 0)
						break;
					c++;
				}
				if (c == _pattern.length) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private int[][] toArray(List<Piece> group, int[] offset) {
		int minx = 10, miny = 10, maxx = 0, maxy = 0;
		for (Piece gp : group) {
			int x = gp.getX();
			int y = gp.getY();
			if (x <= minx)
				minx = x;
			if (y <= miny)
				miny = y;
			if (x >= maxx)
				maxx = x;
			if (y >= maxy)
				maxy = y;
		}
		offset[0] = minx;
		offset[1] = miny;
		
		int zx = maxx - minx + 1;
		int zy = maxy - miny + 1;
		
		int[][] result = new int[zx][zy];
		for (int x = 0; x < zx; x++) {
			for (int y = 0; y < zy; y++) {
				result[x][y] = 0;
			}
		}
		
		for (Piece gp : group) {
			int x = gp.getX() - minx;
			int y = gp.getY() - miny;
			result[x][y] = 1;
		}
		
		return result;
		
	}


}
