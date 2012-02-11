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



import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommandLineParser {

	GregorianCalendar startDate = null;
	GregorianCalendar endDate = null;
	Integer startPart = null;
	Integer endPart = null;
	
	public CommandLineParser(String[] args) {

		boolean setStartDate = false;
		boolean setEndDate = false;
		
		// ####.##.##[-##]
		Pattern datePattern = Pattern.compile("^(\\d+).(\\d+).(\\d+)(-(\\d+)|)$");
		for (String arg: args) {
			
			// Súgó
			if (arg.equals("-h") || 
				arg.equals("-?") ||
				arg.equals("--help"))
			{	
				doHelp(); System.exit(0);
			}
			
			// Dátumok beállítása
			if (setStartDate || setEndDate) {
				
				Matcher m = datePattern.matcher(arg);
				
				if (m.matches()) {
					int year  = Integer.parseInt(m.group(1));
					int month = Integer.parseInt(m.group(2));
					int day   = Integer.parseInt(m.group(3));

					if (setStartDate) {
						startDate = new GregorianCalendar(year, month-1, day);
						if (m.group(5) != null) startPart = Integer.parseInt(m.group(5));
						setStartDate = false;
						continue;
					} else {
						endDate = new GregorianCalendar(year, month-1, day);
						if (m.group(5) != null) endPart = Integer.parseInt(m.group(5));
						setEndDate = false;
						continue;
					}
				} else {
					if (setStartDate) {
						System.err.println("A kezdő dátum formátuma érvénytelen.\n");
					} else {
						System.err.println("A záró dátum formátuma érvénytelen.\n");
					}
					doHelp();
					System.exit(1);
				}
			}
			
			// A következő körben a dátumok beállítása
			if (arg.equals("-s") || arg.equals("--start")) {setStartDate = true; continue;}
			if (arg.equals("-e") || arg.equals("--end")) {setEndDate = true; continue;}				
		}
		
		// Legalább egy dátumot meg kell adni
		if (startDate == null) {
			System.err.println("Legalább egy dátumot meg kell adni.\n");
			doHelp();
			System.exit(1);
		}
	}
	
	/** Súgó */
	static void doHelp() {
		System.out.println(
			"Bumeráng Letöltő - súgó\n" +
			"-----------------------\n" +
			"\n" +
			"Paraméterek:\n" +
			"  -h, --help:                       Ezen súgó megjelenítése\n" +
			"  -s <dátum>, vagy --start <dátum>  Kezdő dátum\n" +
			"  -e <dátum>, vagy --end <dátum>    Befejező dátum\n" +
			"\n" +

			"A program a kezdő és záró dátum közti adásokat tölti le. A záró" +
			"dátum elhagyható." +
			
			"A dátum formátuma: " +
			"  év.hónap.nap-darab. Pl 2011.12.12-12. A darabszám elhagyható.\n" +
			
			"A program automatikusan létrehoz egy mappát minden adásnak, és a\n" +
			"megszakadt letöltéseket megpróbálja folytatni.\n\n"
		);
	}
}
