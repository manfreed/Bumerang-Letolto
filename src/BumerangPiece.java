/*
 * Copyright 2012 - manfreed
 * This file is part of Bumeráng Letöltő.
 *
 * Bumeráng Letöltő is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bumeráng Letöltő is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Bumeráng Letöltő.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */


public class BumerangPiece {
	public String title, details, url;
	public int id, year, month, day;
	
	@Override
	public String toString() {
		return String.format(
			"%3d - %s (%s)",
			id, title, url
		);
	}
}
