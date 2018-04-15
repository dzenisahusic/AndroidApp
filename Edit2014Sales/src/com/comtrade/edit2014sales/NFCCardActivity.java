package com.comtrade.edit2014sales;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comtrade.edit2014salesDzenisa.R;
import com.koushikdutta.ion.Ion;
public class NFCCardActivity extends Activity {
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";
	private TextView mTextView;
	private NfcAdapter mNfcAdapter;
	private CardDetails cd=null;
	
	
    TextView time;
    TextView tprezime;
    TextView tdatum;
    TextView tbrojKartice;
    TextView tbodovi;
    TextView tnoviBodovi;
    ImageView ionImage;
    Button button;
    Dialog dialog;
    Button dialogButton;
    CardDetails card;
    String stringUpis;
    SharedPreferences preferences;  
    boolean writeProtect = false;
    boolean upis = false;
    String bodovi="";
    Intent intent;
	
	final Context context = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfccard);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		bodovi = preferences.getString("bodovi", "0");
        time = (TextView) findViewById(R.id.ime);
        tprezime = (TextView) findViewById(R.id.prezime);
        tdatum = (TextView) findViewById(R.id.tdatum);
        tbrojKartice = (TextView) findViewById(R.id.brojKartice);
        tbodovi = (TextView) findViewById(R.id.poeni);
        tnoviBodovi=(TextView) findViewById(R.id.tnovi);
        button=(Button) findViewById(R.id.unesi);
        intent = new Intent(context, KasaFragment.class);
        ionImage= (ImageView) findViewById(R.id.imageView1);
        
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show();
		}
		handleIntent(getIntent());
		
		 
		// add button listener
		button.setOnClickListener(new OnClickListener() {
 
		  @Override
		  public void onClick(View arg0) {
			  
			  upis = true;
 
			// custom dialog
			dialog = new Dialog(context);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Prinesite karticu");
 
			Button dialogButton = (Button) dialog.findViewById(R.id.button1);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
 
			dialog.show();
		  }
		  });
	}
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}
	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);
		super.onPause();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		/**
		 * This method gets called, when a new Intent gets associated with the
		 * current activity instance. Instead of creating a new activity,
		 * onNewIntent will be called. For more information have a look at the
		 * documentation.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to
		 * the device.
		 */
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
	String action = intent.getAction();
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	    	Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	        String type = intent.getType();
	        if (MIME_TEXT_PLAIN.equals(type)) {
	            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	            if(!upis) new NdefReaderTask().execute(tag);
	            if(upis) {  
	                // check if tag is writable (to the extent that we can  
	                if(writableTag(tag)) {  
	                     //writeTag here  
	                     WriteResponse wr = writeTag(getTagAsNdef(), tag);  
	                    // String message = (wr.getStatus() == 1? "Success: " : "Failed: ") + wr.getMessage();  
	                     //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();  
	                     
	                } else {  
	                     Toast.makeText(context,"This tag is not writable",Toast.LENGTH_SHORT).show();  	                    
	                }             
	           } else {  
	               // Toast.makeText(context,"This tag type is not supported",Toast.LENGTH_SHORT).show();                  
	           }  
	             
	        } else {
	            Log.d(TAG, "Wrong mime type: " + type);
	        }
	    } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
	        // In case we would still use the Tech Discovered Intent
	    	Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	        String[] techList = tag.getTechList();
	        String searchedTech = Ndef.class.getName();
	         
	        for (String tech : techList) {
	            if (searchedTech.equals(tech)) {
	                new NdefReaderTask().execute(tag);
	                break;
	            }
	        }
	    }
	}
	
	
	private NdefMessage createRecord(String text) throws UnsupportedEncodingException {
	    //create the message in according with the standard
	    String lang = "en";
	    byte[] textBytes = text.getBytes();
	    byte[] langBytes = lang.getBytes("US-ASCII");
	    int langLength = langBytes.length;
	    int textLength = textBytes.length;
	    byte[] payload = new byte[1 + langLength + textLength];
	    payload[0] = (byte) langLength;
	    // copy langbytes and textbytes into payload
	    System.arraycopy(langBytes, 0, payload, 1, langLength);
	    System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
	    NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
	    NdefRecord[] records = { recordNFC };
	    NdefMessage message = new NdefMessage(records); 
	    return message;
	}
	
	 public WriteResponse writeTag(NdefMessage message, Tag tag) {  
	     int size = message.toByteArray().length;  
	     String mess = "";  
	     try {  
	       Ndef ndef = Ndef.get(tag);  
	       if (ndef != null) {  
	         ndef.connect();  
	         if (!ndef.isWritable()) {  
	           return new WriteResponse(0,"Tag is read-only");  
	         }  
	         if (ndef.getMaxSize() < size) {  
	           mess = "Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size  
	               + " bytes.";  
	           return new WriteResponse(0,mess);  
	         }  
	         
	         card=new CardDetails();
	         //String bodovi = preferences.getString("bodovi", "0");
	         String string=card.upis(cd, Integer.parseInt(bodovi));
	         Toast.makeText(context,"Uspje�no upisani loyalty poeni",Toast.LENGTH_SHORT).show();
	         //ndef.writeNdefMessage(message); 
	         
	         ndef.writeNdefMessage(createRecord(string));  
	         upis = false;
	         dialog.dismiss();
	         
	         startActivity(intent);}
	         
	         //if(writeProtect) ndef.makeReadOnly();      
	         return new WriteResponse(1,mess); 	        
	     } catch (Exception e) {  
	      // mess = "Failed to write tag";  
	       return new WriteResponse(0,mess);  
	     }  
	   }  
	   
	 private class WriteResponse {  
	        int status;  
	        String message;  
	        WriteResponse(int Status, String Message) {  
	             this.status = Status;  
	             this.message = Message;  
	        }  
	        public int getStatus() {  
	             return status;  
	        }  
	        public String getMessage() {  
	             return message;  
	        }  
	   }  
	    
	   private boolean writableTag(Tag tag) {  
	     try {  
	       Ndef ndef = Ndef.get(tag);  
	       if (ndef != null) {  
	         ndef.connect();  
	         if (!ndef.isWritable()) {  
	           Toast.makeText(context,"Tag is read-only.",Toast.LENGTH_SHORT).show();  
	           //Sounds.PlayFailed(context, silent);  
	           ndef.close();   
	           return false;  
	         }  
	         ndef.close();  
	         return true;  
	       }   
	     } catch (Exception e) {  
	       Toast.makeText(context,"Failed to read tag",Toast.LENGTH_SHORT).show();  
	       //Sounds.PlayFailed(context, silent);  
	     }  
	     return false;  
	   } 
	   private NdefMessage getTagAsNdef() {  
	        boolean addAAR = false;  
	        String uniqueId = "smartwhere.com/nfc.html";      
		     byte[] uriField = uniqueId.getBytes(Charset.forName("US-ASCII"));  
		     byte[] payload = new byte[uriField.length + 1];       //add 1 for the URI Prefix  
		     payload[0] = 0x01;                        //prefixes http://www. to the URI  
		     System.arraycopy(uriField, 0, payload, 1, uriField.length); //appends URI to payload  
		     NdefRecord rtdUriRecord = new NdefRecord(  
		       NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], payload);  
		     if(addAAR) {  
		          // note: returns AAR for different app (nfcreadtag)  
		          return new NdefMessage(new NdefRecord[] {  
		       rtdUriRecord, NdefRecord.createApplicationRecord("com.tapwise.nfcreadtag")  
		     });   
		     } else {  
		          return new NdefMessage(new NdefRecord[] {  
		               rtdUriRecord});  
		     }  
	   } 
	/**
	 * @param activity
	 *            The corresponding {@link Activity} requesting the foreground
	 *            dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);
		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};
		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}
		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}
	/**
	 * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	
	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}
		
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
		 	
	    @Override
	    protected String doInBackground(Tag... params) {
	        Tag tag = params[0];
	         
	        Ndef ndef = Ndef.get(tag);
	        if (ndef == null) {
	            // NDEF is not supported by this Tag. 
	            return null;
	        }
	 
	        NdefMessage ndefMessage = ndef.getCachedNdefMessage();	 
	        NdefRecord[] records = ndefMessage.getRecords();
	        for (NdefRecord ndefRecord : records) {
	            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
	                try {
	                    return readText(ndefRecord);
	                } catch (UnsupportedEncodingException e) {
	                    Log.e(TAG, "Unsupported Encoding", e);
	                }
	            }
	        }	 
	        return null;
	    }
	     
	    private String readText(NdefRecord record) throws UnsupportedEncodingException {
	 
	        byte[] payload = record.getPayload();
	 
	        // Get the Text Encoding
	        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
	 
	        // Get the Language Code
	        int languageCodeLength = payload[0] & 0063;
	         
	        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
	        // e.g. "en"
	         
	        // Get the Text
	        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
	    }
	     
	    @Override
	    protected void onPostExecute(String result) {
	        /*if (result != null) {
	            mTextView.setText("Read content: " + result);
	        }*/
	        cd = new CardDetails(result);
	        time.setText(cd.getImeKupca());
			tprezime.setText(cd.getPrezimeKupca());
			tdatum.setText(cd.getDatumZadnjePromjene());
			tbrojKartice.setText(cd.getBrojKartice());
			tbodovi.setText(cd.getBrojBodova());
			tnoviBodovi.setText(bodovi);
			
			Ion.with(ionImage)
			.placeholder(R.drawable.ic_launcher)
			.load(cd.getUrlSlikeKupca());
			
	    }
	    	    
	}
}