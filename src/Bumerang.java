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

import java.awt.font.NumericShaper;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Bumerang {

	public static void main(String[] args) {
		
		// Paraméterek feldolgozása
		CommandLineParser opt = new CommandLineParser(args);
				
		// Ha a záró dátum nincs megadva
		if (opt.endDate == null) opt.endDate = opt.startDate;
		
		// Aktuális dátum
		GregorianCalendar date = (GregorianCalendar) opt.startDate.clone();
		
		while (date.compareTo(opt.endDate) <= 0) {

			String formattedDate = String.format("%04d.%02d.%02d",
					date.get(Calendar.YEAR),
					date.get(Calendar.MONTH) + 1,
					date.get(Calendar.DATE)
				);
			
			System.out.print(formattedDate + " - adás letöltése... ");
			
			BumerangEpisode episode = new BumerangEpisode(
					date.get(Calendar.YEAR),
					date.get(Calendar.MONTH) + 1,
					date.get(Calendar.DATE)
			);
			
			if (!episode.isExists()) {
				System.out.println("[ E:" + episode.getErrorString() + " ]");
			} else {
				System.out.println("[ OK ]");

				BumerangPiece[] pieces = episode.getPieces();

				// Létrehozzuk neki a mappát
				File folder = new File(formattedDate);
				
				if (!folder.isDirectory() && !folder.mkdir()) {
					System.err.println("Hiba a mappa létrehozása során:" + formattedDate);
					break;
				}			
				
				for (final BumerangPiece piece: pieces) {
					
					// Ha meg van határozva hányadik darabtól kezdjük, az első
					// nap kihagyjuk az az előttieket
					if (date.equals(opt.startDate) && opt.startPart != null) {
						System.out.println("Kihagy: " + piece.id);
						if (opt.startPart > piece.id) continue;
					}
					// Ha meg van határozva hányadik darabbal fejezzük be, az utolsó
					// nap kihagyjuk az az utániakat
					if (date.equals(opt.startDate) && opt.startPart != null) {
						System.out.println("Kihagy: " + piece.id);
						if (opt.endPart < piece.id) continue;
					}
					
					if (piece.url == null) {
						System.err.println(String.format("%2d %s", piece.id, "Hiba: Az url-t nem lehetett megfejteni."));
						break;
					}
					
					String fileName = String.format("%02d. %s.mp3", piece.id, piece.title);
					String filePath = new File(formattedDate, fileName).getAbsolutePath();
					DownloadManager dm = new DownloadManager(piece.url, filePath) {
						
						@Override
						void progress() {
							double percent;
							percent = (double)getDownloaded() / getSize() * 100;
							
							System.out.print(
								String.format(
									"\r%2d - %s (%,d/%,d) - %.2f%%",
									piece.id,
									piece.title,
									getDownloaded(),
									getSize(),
									percent
								)
							);
						}
					};
					if (dm.getStatus() != DownloadManager.SUCCESSFUL) System.out.println(dm.getErrorMessage());
					else System.out.println();
				}
				System.out.println();
			}
			date.add(Calendar.DATE, 1);
		}
		
	}
}
