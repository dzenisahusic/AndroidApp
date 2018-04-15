package com.comtrade.edit2014sales;


import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.comtrade.edit2014salesDzenisa.R;

public class DodajArtikalFragment extends Fragment {
	static Button bDodaj;
	static EditText tBarkod;
	static EditText tNaziv;
	static EditText tCijena;
	static ArtikliDAO artikliDAO;
	
	//Ukoliko je fragmen sluzi za izmjenu artikla, pamti id zeljenog artikla
	static boolean izmijeni;
	static long idArtiklaIzmjena;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		artikliDAO = new ArtikliDAO(this.getActivity());
		artikliDAO.otvoriBazu();
	}
	
	@Override	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unosartikla, container, false);
        bDodaj = (Button) view.findViewById(R.id.bDodaj);
        tBarkod = (EditText) view.findViewById(R.id.tBarkod);
        tNaziv = (EditText) view.findViewById(R.id.tCijena);
        tCijena = (EditText) view.findViewById(R.id.tNaziv);
        
        bDodaj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(provjeriPodatke(tBarkod.getText().toString(), tNaziv.getText().toString(), tCijena.getText().toString())) {
					Artikal a;
					if(izmijeni) {
						Artikal noviArtikal = new Artikal();
						noviArtikal.setId(idArtiklaIzmjena);
						noviArtikal.setBarkod(Integer.parseInt(tBarkod.getText().toString()));
						noviArtikal.setNaziv(tNaziv.getText().toString());
						noviArtikal.setCijena(Float.parseFloat(tCijena.getText().toString()));
						artikliDAO.izmijeniArtikal(noviArtikal);
						izmijeni = false;
						idArtiklaIzmjena = 0;
						Toast poruka = Toast.makeText(getActivity(), "Uspješno ste izmijenili artikal!", Toast.LENGTH_LONG);
						poruka.show();
						MainActivity.promijeniFragmentIUredi(1, null);
					}
					else {
						a = artikliDAO.kreirajArtikal(0, Integer.parseInt(tBarkod.getText().toString()), tNaziv.getText().toString(), Float.parseFloat(tCijena.getText().toString()));
						Log.d("id", String.valueOf(a.getId()));
				        Log.d("barkod", String.valueOf(a.getBarkod()));
				        Log.d("naziv", a.getNaziv());
				        Log.d("cijena", String.valueOf(a.getCijena()));

				        Toast poruka = Toast.makeText(getActivity(), "Uspješno ste unijeli artikal!", Toast.LENGTH_SHORT);
				        poruka.show();
					}
					tBarkod.setText("");
			        tNaziv.setText("");
			        tCijena.setText("");
			        tBarkod.requestFocus();
				}
			}
		});
        return view;
    }
	
	public static void izmijeniArtikal(Artikal artikal) {
		bDodaj.setText("Uredi");
		tBarkod.setText(String.valueOf(artikal.getBarkod()));
		tNaziv.setText(artikal.getNaziv());
		tCijena.setText(String.valueOf(artikal.getCijena()));
		izmijeni = true;
		idArtiklaIzmjena = artikal.getId();
	}
	
	boolean provjeriPodatke(String barkod, String naziv, String cijena) {
		boolean barkodLos = false;
		boolean nazivLos = false;
		boolean cijenaLosa = false;
		boolean prikazanToast = false;
		Toast poruka = new Toast(getActivity());
		
		if(barkod.equals("")) {
			tBarkod.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
			poruka = Toast.makeText(getActivity(), "Morate unijeti sve podatke!", Toast.LENGTH_SHORT);
			poruka.show();
			prikazanToast = true;
			barkodLos = true;
		}
		else 
			tBarkod.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaispravno));
		
		if(naziv.equals("")) {
			tNaziv.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
			if(!prikazanToast) {
				poruka = Toast.makeText(getActivity(), "Morate unijeti sve podatke!", Toast.LENGTH_SHORT);
				poruka.show();
				prikazanToast = true;
			}
			nazivLos = true;
		}
		else 
			tNaziv.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaispravno));
		
		if(cijena.equals("")) {
			tCijena.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
			if(!prikazanToast) {
				poruka = Toast.makeText(getActivity(), "Morate unijeti sve podatke!", Toast.LENGTH_SHORT);
				poruka.show();
				prikazanToast = true;
			}
			cijenaLosa = true;
		}
		else 
			tCijena.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaispravno));
		
		try { 
	        Integer.parseInt(barkod); 
	        if( Integer.parseInt(barkod) < (long) 0) throw new IllegalArgumentException();
	        tBarkod.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaispravno));
	    } catch(NumberFormatException e) {
	    	tBarkod.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
	    	if(!prikazanToast) {
	    		poruka = Toast.makeText(getActivity(), "Barkod smije sadržavati samo brojeve!", Toast.LENGTH_SHORT);
	    		poruka.show();
	    		prikazanToast = true;
	    	}
	    	barkodLos = true;
	    } catch (IllegalArgumentException e) {
	    	tBarkod.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
	    	if(!prikazanToast) {
	    		poruka = Toast.makeText(getActivity(), "Barkod mora biti pozitivan broj!", Toast.LENGTH_SHORT);
	    		poruka.show();
	    		prikazanToast = true;
	    	}
	    	barkodLos = true;
		}
		
		try { 
	        Float.parseFloat(cijena); 
	        if(cijena.contains("f")) throw new NumberFormatException();
	        if( Float.parseFloat(cijena) < (float) 0) throw new IllegalArgumentException();
	        tCijena.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaispravno));
	    } catch(NumberFormatException e) {
	    	tCijena.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
	    	if(!prikazanToast) {
	    		poruka = Toast.makeText(getActivity(), "Cijena smije sadržavati samo brojeve!", Toast.LENGTH_SHORT);
	    		poruka.show();
	    	}
	    	cijenaLosa = true;
	    } catch (IllegalArgumentException e) {
	    	tCijena.setBackgroundDrawable(getResources().getDrawable(R.drawable.validacijaneispravno));
	    	if(!prikazanToast) {
	    		poruka = Toast.makeText(getActivity(), "Cijena ne smije biti negativna!", Toast.LENGTH_SHORT);
	    		poruka.show();
	    	}
	    	cijenaLosa = true;
		}
		if(barkodLos || cijenaLosa || nazivLos) 
			return false;
		else return true;
	}
	
	
}
