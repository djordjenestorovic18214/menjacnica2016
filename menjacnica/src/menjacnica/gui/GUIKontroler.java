package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	private static MenjacnicaGUI glavniProzor;
	private static MenjacnicaInterface sistem;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sistem = new Menjacnica();
					glavniProzor = new MenjacnicaGUI();
					glavniProzor.setVisible(true);
					glavniProzor.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(glavniProzor.getContentPane());
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(glavniProzor.getContentPane());
			prozor.setVisible(true);
		}
	}
	
	public static void prikaziIzvrsiZamenuGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(glavniProzor.getContentPane());
			prozor.setVisible(true);
		}
	}
	
	public static void prikaziAboutProzor(){
		JOptionPane.showMessageDialog(glavniProzor.getContentPane(),
				"Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				MenjacnicaGUI.prikaziSveValute();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(glavniProzor.getContentPane(),
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void unesiKurs(String naziv, String skrNaziv, Object sifra, String pKurs, String kKurs, String sKurs) {
			
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skrNaziv);
			valuta.setSifra((Integer)(sifra));
			valuta.setProdajni(Double.parseDouble(pKurs));
			valuta.setKupovni(Double.parseDouble(kKurs));
			valuta.setSrednji(Double.parseDouble(sKurs));
				
			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			glavniProzor.prikaziSveValute();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try{
			sistem.obrisiValutu(valuta);
			
			glavniProzor.prikaziSveValute();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String izvrsiZamenu(Valuta valuta, boolean isSelected, String iznos){
		try{
			String konacniIznos =""+ 
					sistem.izvrsiTransakciju(valuta, isSelected, Double.parseDouble(iznos));
		
			return konacniIznos;
		} catch (Exception e1) {
		JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
				"Greska", JOptionPane.ERROR_MESSAGE);
		return "";
		}
	}

	public static List<Valuta> vratiKursnuListu() {
		return sistem.vratiKursnuListu();
	}
}
