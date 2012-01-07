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


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BumerangEpisode {
	
	private static final String ILLEGAL_CHARACTERS = "(/|\r|\t|\0|\f|`|\\||\\\\|:|\\*)";
	private int year, month, day;
	private ArrayList<BumerangPiece> pieces = new ArrayList<BumerangPiece>();
	private String errorString = "OK";

	public BumerangEpisode(int year, int month, int day) {
		
		this.year = year;
		this.month = month;
		this.day = day;
		
		try {
			// Adás oldala a bumerang.hu-ról
			URL url = new URL(String.format("http://bumerang.hu/?ezaz=2_%04d%02d%02d", year, month, day));
			URLConnection conn = url.openConnection();
			
			Reader r;
			try {
				r = new InputStreamReader(conn.getInputStream(), "windows-1250"); // Komolyan???
			} catch (UnsupportedEncodingException e) {
				r = new InputStreamReader(conn.getInputStream());				
			}
			
			StringBuilder buf = new StringBuilder();
			while (true) {
			  int ch = r.read();
			  if (ch < 0) break;
			  buf.append((char) ch);
			}
			// HTML kibogarászása
			parse(buf.toString());
		
		} catch (MalformedURLException e) {
			/* Nem történhet meg (?) */
			errorString = "URL Error";
			return;
		} catch (IOException e) {
			// Hiba eltusolása. (Mint ha nem létezne az adás)
			errorString = "IOException";
			return;
		}
	}
	
	private void parse(String html) {
		
		// Regexp. Nem HTML Parser.
		String tableHeader = "<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">";
		Pattern titlePattern = Pattern.compile("<font.*?>([0-9:]+?) (.*?)</font>");
		//Pattern detailsPattern = Pattern.compile("<p align=\"justify\">.*?</p>");
		Pattern urlPattern = Pattern.compile("<!a href=\"(.+?\\.mp3)\">");
		
		// Takarítás (kommenteket nem :))
		html = html.replaceAll("\\s+", " ");

		String[] htmlPieces = html.split(tableHeader);

		// Az első darab nem kell
		for (int i=1; i<htmlPieces.length; i++) {

			BumerangPiece piece = new BumerangPiece();
			piece.id = i;
			
			Matcher titleMatcher = titlePattern.matcher(htmlPieces[i]);
			
			if (titleMatcher.find()) {
				piece.title = titleMatcher.group(2);
				piece.title.replaceAll("<br.*?>", "\n"); // HTML sortörések
				piece.title.replaceAll(ILLEGAL_CHARACTERS, "");
			}
			else {
				piece.title = "N/A";
			}
		
			Matcher urlMatcher = urlPattern.matcher(htmlPieces[i]);
			if (urlMatcher.find()) piece.url = urlMatcher.group(1);
			else piece.url = null;

			pieces.add(piece);
		}
	}

	public BumerangPiece[] getPieces() {
		BumerangPiece[] p = new BumerangPiece[pieces.size()];
		pieces.toArray(p);
		return p;
	}

	public int length() {
		return pieces.size();
	}
	
	public boolean isExists() {
		return !pieces.isEmpty();
	}

	public String getErrorString() {
		return errorString ;
	}
	
}
