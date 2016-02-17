package de.simocracy.postwriter.datumsrechner;

import javax.swing.JOptionPane;

import de.simocracy.postwriter.*;

public class DatumRechner {
	
	// RL->SY Ergebnis als einzelnen String
	public static String rLSyString(int rlTag, int rlMon, int rlJahr, int rlStu, int rlMin, Einst einst) {
		// Berechnung
		String[] bd = DatumRechner.rlSy(rlTag, rlMon, rlJahr, rlStu, rlMin, einst);
		
		// Ausgabe
		return bd[0] + "." + bd[1] + "." + bd[2] + " " + bd[3] + ":" + bd[4] + " Uhr";
	}
	
	// RL->SY
	public static String[] rlSy(String rlTag, String rlMon, String rlJahr, String rlStu, String rlMin, Einst einst){
		int iJahr = Integer.parseInt(rlJahr);
		int iMon = Integer.parseInt(rlMon);
		int iTag = Integer.parseInt(rlTag);
		int iStu = Integer.parseInt(rlStu);
		int iMin = Integer.parseInt(rlMin);
		
		return rlSy(iTag, iMon, iJahr, iStu, iMin, einst);
	}
	
	// RL->SY
	public static String[] rlSy(int rlTag, int rlMon, int rlJahr, int rlStu, int rlMin, Einst einst){
		// Pruefen, ob inputDatum zulaessig, wenn nein, Error
		if(rlJahr > 2008 || (rlJahr == 2008 && rlMon > 9)){
		// Allgemeines Zeug
			// RL-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar
			int rlFeb = schaltjahrFeb(rlJahr);
//			System.out.println("RL-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar " + rlFeb);
			
			// Tage eines RL-Quartals berechnen und in Array speichern
			int[] tageQuartalRl = {
				0,
				31 + rlFeb + 31,
				30 + 31 + 30,
				31 + 31 + 30,
				31 + 30 + 31
			};
			
			// Vergangene RL-Tage im RL-Quartal berechnen und in Array speichern
			int[] tageVergMonQuartRl = {
				0, // Januar
				31, // Februar
				31 + rlFeb, // März
				0, // April
				0 + 30, // Mai
				0 + 30 + 31, // Juni
				0, // Juli
				0 + 31, // August
				0 + 31 + 31, // September
				0, // Oktober
				0 + 31, // November
				0 + 31 + 30 // Dezember
			};
			
			// Ermittlung des RL-Quartals
			int rlQuartal = quartalErm(rlMon);
//			System.out.println("Ermittlung des RL-Quartals " + rlQuartal);
			
			// Ermittlung des Zeitanteils eines Tages
			double rlZeitAnteil = ((rlStu * 60D + rlMin) / (24D * 60D));
//			System.out.println("Ermittlung des Zeitanteils eines Tages " + rlZeitAnteil);
			
			// Ermittlung der RL-Tagesnummer im RL-Quartal
			double rlTagNrQartal = tageVergMonQuartRl[rlMon-1] + rlTag + rlZeitAnteil - 1;
//			System.out.println("Ermittlung der RL-Tagesnummer im RL-Quartal " + rlTagNrQartal);
			
		// Jahresermittlung
			// Berechnung SY-Jahr
			int syJahr = (rlJahr - 2009) * 4 + 2020 + rlQuartal;
//			System.out.println("Berechnung SY-Jahr " + syJahr);
			
			// SY-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar
			int syFeb = schaltjahrFeb(syJahr);
//			System.out.println("SY-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar " + syFeb);
			
		// Tages- und Monatsermittlung
			
			// Vergangene RL-Tage im RL-Quartal berechnen und in Array speichern
			int[] tageVergMonQuartSy = {
				0, // Januar
				31, // Februar
				31 + syFeb, // März
				0, // April
				0 + 30, // Mai
				0 + 30 + 31, // Juni
				0, // Juli
				0 + 31, // August
				0 + 31 + 31, // September
				0, // Oktober
				0 + 31, // November
				0 + 31 + 30 // Dezember
			};
			
			// Tage aller Quartale berechnen und in Array speichern
			int[] tageQuartalGesSy = {
				0,
				31 + syFeb + 31,
				31 + syFeb + 31 + 30 + 31 + 30,
				31 + syFeb + 31 + 30 + 31 + 30 + 31 + 31 + 30
			};
			
			// Berechnung Anzahl Tage im SY-Jahr
			int tageSyJahrGes = 365 - 28 + syFeb;
//			System.out.println("Berechnung Anzahl Tage im SY-Jahr " + tageSyJahrGes);
			
			// Berechnung SY-Tage pro RL-Tag
			double syTageRlTage = 1/((double) tageQuartalRl[rlQuartal] / (double) tageSyJahrGes);
//			System.out.println("Berechnung SY-Tage pro RL-Tag " + syTageRlTage);
			
			// Berechnung SY-Tag im SY-Jahr
			double syTagSyJahrD = (double) ((rlTagNrQartal * syTageRlTage) + 1);
//			System.out.println("Berechnung SY-Tag im SY-Jahr " + syTagSyJahrD);
			
			// Abrunden SY-Tag im SY-Jahr
			int syTagSyJahr = (int) syTagSyJahrD;
//			System.out.println("Abrunden SY-Tag im SY-Jahr " + syTagSyJahr);
			
			// Ermittlung SY-Monat und SY-Quartal
			int syMon = 0;
			int syQuartal = 0;
			if(syTagSyJahr > tageQuartalGesSy[3] + tageVergMonQuartSy[11]){
				syMon = 12;
				syQuartal = 4;
			}
			else if(syTagSyJahr > tageQuartalGesSy[3] + tageVergMonQuartSy[10]){
				syMon = 11;
				syQuartal = 4;
			}
			else if(syTagSyJahr > tageQuartalGesSy[3] + tageVergMonQuartSy[9]){
				syMon = 10;
				syQuartal = 4;
			}
			else if(syTagSyJahr > tageQuartalGesSy[2] + tageVergMonQuartSy[8]){
				syMon = 9;
				syQuartal = 3;
			}
			else if(syTagSyJahr > tageQuartalGesSy[2] + tageVergMonQuartSy[7]){
				syMon = 8;
				syQuartal = 3;
			}
			else if(syTagSyJahr > tageQuartalGesSy[2] + tageVergMonQuartSy[6]){
				syMon = 7;
				syQuartal = 3;
			}
			else if(syTagSyJahr > tageQuartalGesSy[1] + tageVergMonQuartSy[5]){
				syMon = 6;
				syQuartal = 2;
			}
			else if(syTagSyJahr > tageQuartalGesSy[1] + tageVergMonQuartSy[4]){
				syMon = 5;
				syQuartal = 2;
			}
			else if(syTagSyJahr > tageQuartalGesSy[1] + tageVergMonQuartSy[3]){
				syMon = 4;
				syQuartal = 2;
			}
			else if(syTagSyJahr > tageQuartalGesSy[0] + tageVergMonQuartSy[2]){
				syMon = 3;
				syQuartal = 1;
			}
			else if(syTagSyJahr > tageQuartalGesSy[0] + tageVergMonQuartSy[1]){
				syMon = 2;
				syQuartal = 1;
			}
			else if(syTagSyJahr > tageQuartalGesSy[0] + tageVergMonQuartSy[0]){
				syMon = 1;
				syQuartal = 1;
			}else{
				einst.outEx(68, "SY-Monat konnte nicht ermittelt werden.");
//				System.out.println("Error");
				return null;
			}
//			System.out.println("Ermittlung SY-Monat und SY-Quartal " + syMon + " " + syQuartal);
			
			// Berechnung SY-Tag
			int syTag = syTagSyJahr - (tageQuartalGesSy[syQuartal-1] + tageVergMonQuartSy[syMon-1]);
//			System.out.println("Berechnung SY-Tag " + syTag);
			
			// Berechnung SY-Stunde
			double syStundeD = (double) ((syTagSyJahrD - syTagSyJahr) * 24);
//			System.out.println("Berechnung SY-Stunde " + syStundeD);
			
			// Rundung SY-Stunde
			int syStunde = (int) syStundeD;
//			System.out.println("Rundung SY-Stunde " + syStunde);
			
			// Berechnung SY-Minute
			int syMinute = (int) ((syStundeD - syStunde)* 60);
//			System.out.println("Berechnung SY-Minute " + syMinute);
			
			// Führende 0er
			String syTagS = String.valueOf(syTag);
			if(syTag < 10) syTagS = "0" + syTagS;
			String syMonS = String.valueOf(syMon);
			if(syMon < 10) syMonS = "0" + syMonS;
			String syJahrS = String.valueOf(syJahr);
			String syStundeS = String.valueOf(syStunde);
			if(syStunde < 10) syStundeS = "0" + syStundeS;
			String syMinuteS = String.valueOf(syMinute);
			if(syMinute < 10) syMinuteS = "0" + syMinuteS;
			
		// Rueckgabe der Methode
//			return syTag + "." +  syMon + "." + syJahr;
			String[] erg = {syTagS, syMonS, syJahrS, syStundeS, syMinuteS};
			return erg;
		}
		
		// Error-Ausgabe wenn Datum nicht zulaessig
		else{
			JOptionPane.showMessageDialog(null, "Das eingegebene Datum ist nicht zulässig." + Einst.nl +
					"Frühstmöglichstes Datum ist der 1. Oktober 2008.", "Unzulässige Eingabe",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	// SY->RL
	public static String[] syRl(String syTag, String syMon, String syJahr, String syStu, String syMin, Einst einst){
		int iJahr = Integer.parseInt(syJahr);
		int iMon = Integer.parseInt(syMon);
		int iTag = Integer.parseInt(syTag);
		int iStu = Integer.parseInt(syStu);
		int iMin = Integer.parseInt(syMin);
		
		return syRl(iTag, iMon, iJahr, iStu, iMin, einst);
	}
	
	// SY->RL
	public static String[] syRl(int syTag, int syMon, int syJahr, int syStu, int syMin, Einst einst){
		// Initialisierung Variable Jahre nach 2020
		int syJahreN2020 = 0;
		
		// Initialisierung Variable rlJahr
		int rlJahr = 0;
		
		// Pruefung, welches Eingabejahr
		if(syJahr > 2020){
			// Wenn Eingabe nach 2020
			syJahreN2020 = syJahr - 2020;
			
			// Pruefung, ob bei SY-Jahre nach 2020 etwas agezogen werden muss
			int syJahreN2020N = syJahreN2020;
			if(syJahreN2020N % 4 == 0){
				syJahreN2020N--;
			}
			
			// RL-Jahr berechnen
			rlJahr = 2009 + (int) (syJahreN2020N / 4);
		}
		else if (syJahr == 2020){
			// Wenn Eingabejahr 2020
			rlJahr = 2008;
		}
		else{
			JOptionPane.showMessageDialog(null, "Das eingegebene Datum ist nicht zulässig." + Einst.nl +
					"Frühstmöglichstes Datum ist der 1. Januar 2020.", "Unzulässige Eingabe",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		// Ermittle RL-Quartal
		int rlQuartalZW = syJahreN2020 % 4;
		int rlQuartal = 0;
		switch (rlQuartalZW) {
		case 0:
			rlQuartal = 4;
			break;
		case 1:
			rlQuartal = 1;
			break;
		case 2:
			rlQuartal = 2;
			break;
		case 3:
			rlQuartal = 3;
			break;
		}

		// RL-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar
		int rlFeb = schaltjahrFeb(rlJahr);

		// SY-Schaltjahr-Pruefung mit Ausgabe der Tage im Februar
		int syFeb = schaltjahrFeb(syJahr);
		
		// Array mit Tage pro Quartal fuellen (RL)
		int[] vergangeneTageQuartalRl = {
				0, // Platzhalter
				0, // Januar
				31, // Februar
				31 + rlFeb, // März
				0, // April
				30, // Mai
				30 + 31, // Juni
				0, // Juli
				31, // August
				31 + 31, // September
				0, // Oktober
				31, // November
				31 + 30 // Dezember
		};
		
		// Array mit Tage pro Monat fuellen (SY)
		int[] vergangeneTageQuartalSy = {
				0, // Platzhalter
				0, // Januar
				31, // Februar
				31 + syFeb, // März
				0, // April
				30, // Mai
				30 + 31, // Juni
				0, // Juli
				31, // August
				31 + 31, // September
				0, // Oktober
				31, // November
				31 + 30 // Dezember
		};
		
		// Array mit Tage pro Quartal mit RL-Februar
		int[] tageProQuartalRl = {
				0,
				31 + rlFeb + 31,
				30 + 31 + 30,
				31 + 31 + 30,
				31 + 30 + 31
		};
		
		// Array vergangener Tage im Jahr mit RL-Februar
		int[] tageBisherJahrRl = {
				0,
				0,
				31 + rlFeb + 31,
				31 + rlFeb + 31 + 30 + 31 + 30,
				31 + rlFeb + 31 + 30 + 31 + 30 + 31 + 31 + 30
		};
		
		// Array vergangener Tage im Jahr mit SL-Februar
		int[] tageBisherJahrSy = {
				0,
				0,
				31 + syFeb + 31,
				31 + syFeb + 31 + 30 + 31 + 30,
				31 + syFeb + 31 + 30 + 31 + 30 + 31 + 31 + 30
		};
		
		// Berechnung SY-Quartal
		int syQuartal = quartalErm(syMon);
		
		// Berechnung SY-Zeitpunkt
		double rlZeitAnteil = ((syStu * 60D + syMin) / (24D * 60D));
		
		// Berechnung vergangener SY-Tage im ganzen SY-Jahr
		double vergSyTageSyJahrD = (double) (tageBisherJahrSy[syQuartal] + 
				vergangeneTageQuartalSy[syMon] + syTag + rlZeitAnteil - 1);
//		double rlTagNrQartal = tageVergMonQuartRl[rlMon-1] + rlTag + rlZeitAnteil - 1;
		
		// Ermittlung gesamtTage SY-Jahr
		int gesTageSyJahr = 365 - 28 + syFeb;
		
		// Ermittlung RL-Tag pro SY-Tag
		double rlTagProSyTag = (double) tageProQuartalRl[rlQuartal] / (double) gesTageSyJahr;
		
		// Vergangene RL-Tage im SY-Jahr
		double vergRlTageSyJahr = (double) vergSyTageSyJahrD * (double) rlTagProSyTag;
		
		// Berechnung vergangener RL-Tage im Jahr
		double vergRlTageRlJahr = (double) tageBisherJahrRl[rlQuartal] + (double) vergRlTageSyJahr;
		
		// Ermittlung des RL-Monats
		int rlMon = 0;
		if(vergRlTageRlJahr >= tageBisherJahrRl[4] + vergangeneTageQuartalRl[12]){
			rlMon = 12;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[4] + vergangeneTageQuartalRl[11]){
			rlMon = 11;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[4] + vergangeneTageQuartalRl[10]){
			rlMon = 10;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[3] + vergangeneTageQuartalRl[9]){
			rlMon = 9;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[3] + vergangeneTageQuartalRl[8]){
			rlMon = 8;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[3] + vergangeneTageQuartalRl[7]){
			rlMon = 7;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[2] + vergangeneTageQuartalRl[6]){
			rlMon = 6;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[2] + vergangeneTageQuartalRl[5]){
			rlMon = 5;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[2] + vergangeneTageQuartalRl[4]){
			rlMon = 4;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[1] + vergangeneTageQuartalRl[3]){
			rlMon = 3;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[1] + vergangeneTageQuartalRl[2]){
			rlMon = 2;
		}
		else if(vergRlTageRlJahr >= tageBisherJahrRl[1] + vergangeneTageQuartalRl[1]){
			rlMon = 1;
		}else{
			einst.outEx(69, "RL-Monat konnte nicht ermittelt werden.");
			return null;
		}
		
		// Berechnung RL-Tag
		double rlTagD = (double) (vergRlTageRlJahr - (tageBisherJahrRl[rlQuartal] + vergangeneTageQuartalRl[rlMon]));
		
		// Runden RL-Tag
		int rlTag = (int) Math.round(rlTagD + 0.5);
		
		// Runden Stunde
		double rlStundeD = (double) (((rlTagD - rlTag + 1) * 24));
		int rlStunde = (int) rlStundeD;
		
		// Runden Minute
		double rlMinuteD = (double) (((rlStundeD - rlStunde) *60 ));
		int rlMinute = (int) Math.round(rlMinuteD);
		
//		int rlTag = (int) Math.round(rlTagD + 0.4D);
		
//		return rlTag + "." + rlMon + "." + rlJahr + " " + rlStunde + ":" + rlMinute + " Uhr";
		
		// Führende 0er
		String rlTagS = String.valueOf(rlTag);
		if(rlTag < 10) rlTagS = "0" + rlTagS;
		String rlMonS = String.valueOf(rlMon);
		if(rlMon < 10) rlMonS = "0" + rlMonS;
		String rlJahrS = String.valueOf(rlJahr);
		String rlStundeS = String.valueOf(rlStunde);
		if(rlStunde == 24) rlStundeS = "0";
		if(rlStunde < 10) rlStundeS = "0" + rlStundeS;
		String rlMinuteS = String.valueOf(rlMinute);
		if(rlMinute == 60) rlMinuteS = "59";
		if(rlMinute < 10) rlMinuteS = "0" + rlMinuteS;
		
	// Rueckgabe der Methode
//		return syTag + "." +  syMon + "." + syJahr;
		String[] erg = {rlTagS, rlMonS, rlJahrS, rlStundeS, rlMinuteS};
		return erg;
	}
	
	// Pruefung, ob Jahr ein Schaltjahr
	protected static int schaltjahrFeb(int jahr){
		if((jahr % 4 == 0 && jahr % 100 != 0) || jahr % 400 == 0){
			// Wenn Schaltjahr, dann Februar 29 Tage
			return 29;
		}
		else{
			// Wenn kein Schaltjahr, dann Februar 28 Tage
			return 28;
		}
	}
	
	// Ermittlung des Quartals eines Jahres
	private static int quartalErm(int mon){
		int q = 0;
		switch (mon) {
		case 1:
		case 2:
		case 3:
			q = 1;
			break;
		case 4:
		case 5:
		case 6:
			q = 2;
			break;
		case 7:
		case 8:
		case 9:
			q = 3;
			break;
		case 10:
		case 11:
		case 12:
			q = 4;
			break;
		}
		return q;
	}
}
