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
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

abstract public class DownloadManager {

	private static final int MAX_BUFFER_SIZE = 64000; 
	
	// Állapotok
	public static final int WAITING     = 1;
	public static final int DOWNLOADING = 2;
	public static final int ERROR       = 3;
	public static final int SUCCESSFUL  = 4;
	
	// Hibaüzenet
	private String errorMessage;

	// Letöltés állapota
	private long size = -1;
	private long downloaded = 0;
	
	// Státusz
	private int status;
	
	// Getterek
	public int getStatus() {return status;}

	public long getSize() {return size;}
	public long getDownloaded() {return downloaded;}

	public String getErrorMessage() { return errorMessage; }	
	
	/** A metódus felel a folyamat állapotának kijelzéséért. */
	abstract void progress();
	
	/**
	 * Minimális hibakezelés
	 * @param message Hibaüzenet
	 */
	private void error(String message) {
		errorMessage = message;
		status = ERROR;
		progress();
	}
	
	public DownloadManager(String url, String fileName) {

		RandomAccessFile file = null;
		InputStream stream = null;
			
		try {

			// Fájl megnyitása, végére megyünk
			file = new RandomAccessFile(fileName, "rw");
			downloaded = file.length();
			file.seek(downloaded);
			
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();			
			connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
			connection.connect();
			
			// Csak 2xx http kódot fogad el.
			if (connection.getResponseCode() / 100 != 2) {
				// Már letöltve
				if (connection.getResponseCode() == 416) {
					size = downloaded;
					status = SUCCESSFUL;
					progress();
				} else {
					error("Hibás HTTP válasz: " + connection.getResponseCode());
				}
				return;
			}
				
			// Check for valid content length.
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error("Érvénytelen hossz. " + contentLength);
				return;
			}
				
			// Fájl mérete
			size = contentLength + downloaded;
			progress();
				
			stream = connection.getInputStream();
			while (true) {
				
				/* Size buffer according to how much of the file is left to download. */
				byte buffer[];
				
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[(int)(size - downloaded)];
				}
					
				// Beolvasás. Ha nincs mit, végeztünk
				int read = stream.read(buffer);
				if (read == -1) break;
					
				// Kiírás
				file.write(buffer, 0, read);
				downloaded += read;
				progress();
			}
			
			progress();

		} catch (IOException e) {
			error("IO Error");
		} finally {
		
			if (file != null)
				try {file.close();} catch (Exception e) {/* Ne ezen múljon */}
			if (stream != null)
				try {stream.close();} catch (Exception e) {/* Ne ezen múljon */}
		}
		status = SUCCESSFUL;
		progress();
	}
}
