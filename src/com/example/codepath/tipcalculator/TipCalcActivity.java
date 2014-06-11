package com.example.codepath.tipcalculator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TipCalcActivity extends Activity {

	private static final String TIP_FILE = "TipCalculator.txt";
	
	private EditText etTransactionAmount;
	private NumberPicker tipPicker;
	private NumberPicker userPicker;
	private TextView tvTipAmount;
	private CheckBox cbSaveValues;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calc);
		
		// Get the transaction amount
		etTransactionAmount = (EditText) findViewById(R.id.etTransactionAmount);
		
		// The Save Values checkbox will be checked on app load if 
		// a data file is present.
		cbSaveValues = (CheckBox) findViewById(R.id.cbSaveValues);
		File tipFile = new File(getFilesDir(), TIP_FILE);
		cbSaveValues.setChecked(tipFile.exists());
		
		// Get the tip percentage
		tipPicker = (NumberPicker) findViewById(R.id.tipPicker);
		tipPicker.setWrapSelectorWheel(true);
		tipPicker.setMinValue(5);
		tipPicker.setMaxValue(25);
		
		// Get the number of users
		userPicker = (NumberPicker) findViewById(R.id.userPicker);
		userPicker.setWrapSelectorWheel(true);
		userPicker.setMinValue(1);
		userPicker.setMaxValue(25);
		
		// If preset values exist, load them in tipPicker and userPicker
		// Else set default values
		if (tipFile.exists()) {
			final List<Integer> values = readDataFromFile();
			if (values.size() == 2) {
				tipPicker.setValue(values.get(0).intValue());
				userPicker.setValue(values.get(1).intValue());
			}
		} else {
			tipPicker.setValue(15);
			userPicker.setValue(1);
		}
		
		// The file tip value will be displayed here.
		tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
		
		// Automatically calculate tip on transaction amount change
		takeActionOnTransactionAmountChange();
		
		// Automatically calculate tip on tip percentage change
		takeActionOnTipPercentageChange();
		
		// Automatically calculate tip on number of user change
		takeActionOnNumberOfUserChange();

		// Let the Save Value checkbox determine if the amounts should be saved to a file
		handleSaveValueCheckBox();
	}
	
	@Override
	protected void onPause() {
		handleSaveValueCheckBox();
	}
	
	@Override
	protected void onStop() {
		handleSaveValueCheckBox();
	}
	
	@Override
	protected void onDestroy() {
		handleSaveValueCheckBox();
	}
	
	// Automatically calculate the tip when the transaction amount is changed.
	private void takeActionOnTransactionAmountChange() {
		// XXX Yet to test this on a real phone
//		etTransactionAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (!hasFocus) {
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				}
//			}
//		});
		
		etTransactionAmount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// no-op
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// no-op
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				showCalculatedTip();
			}
		});
	}
	
	// Automatically calculate the tip when the tip percentage is changed.
	private void takeActionOnTipPercentageChange() {
		tipPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				tipPicker.setValue(newVal);
				showCalculatedTip();
			}
		});
	}
	
	// Automatically calculate the tip when the number of users is changed.
	private void takeActionOnNumberOfUserChange() {
		userPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				userPicker.setValue(newVal);
				showCalculatedTip();
			}
		});
	}
	
	// This method is called onCreate(), onPause(), onStop(), and onDestroy() 
	// If the save option is selected, save the tip percentage and
	// number of users to a file. If unchecked, delete it.
	private void handleSaveValueCheckBox() {
		
		cbSaveValues.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					writeDataToFile();
				} else {
					deleteDataFile();
				}
			}
		});
	}
	
	// Method that actually calculates the tip based on
	// - transaction amount
	// - tip percentage
	// - - number of users
	private void showCalculatedTip() {
		
		final double transactionAmount = getTransactionAmount();
		final int tipPercentage = tipPicker.getValue();
		final int numUsers = userPicker.getValue();
		
		final double tipAmount = transactionAmount * tipPercentage / (100.0 * numUsers);
		final String tipAmountToShow = String.format("$%s", new DecimalFormat("#0.00").format(tipAmount));
		tvTipAmount.setText(tipAmountToShow);
	}

	// Get the transaction amount from the EditText view.
	private double getTransactionAmount() {
		
		final String enteredAmount = etTransactionAmount.getText().toString();
		double transactionAmount = 0;
		try {
			transactionAmount = Double.parseDouble(enteredAmount);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
		}
		return transactionAmount;
	}
	
	// Write last selected tip percentage and number of users to a file
	// when the activity pauses, stops, or destroys
	private void writeDataToFile() {
		
		final File tipFile = new File(getFilesDir(), TIP_FILE);
		try {
			FileUtils.write(tipFile, Integer.toString(tipPicker.getValue())); // first line
			FileUtils.writeStringToFile(tipFile, "\n", true); // append a newline
			FileUtils.write(tipFile, Integer.toString(userPicker.getValue()), true); // second line
		} catch (IOException ex) {
			ex.printStackTrace();
			Toast.makeText(this, "Error: Could not save values.", Toast.LENGTH_SHORT).show();
		}
	}
	
	// Delete the above data file if the Save Value checkbox is unselected.
	private void deleteDataFile() {
		
		final File tipFile = new File(getFilesDir(), TIP_FILE);
		if (tipFile.exists()) {
			tipFile.delete();
		}
	}
	
	// Read the preset tip percentage and number of users from file.
	private List<Integer> readDataFromFile() {
		
		File tipFile = new File(getFilesDir(), TIP_FILE);
		List<Integer> intValues;
		
		try {
			List<String> values = FileUtils.readLines(tipFile);
			if (values.size() == 0) {
				intValues = new ArrayList<Integer>();
				intValues.add(Integer.parseInt(values.get(0))); // tip percentage
				intValues.add(Integer.parseInt(values.get(1))); // number of users
				Toast.makeText(this, intValues.get(0) + " " + intValues.get(1), Toast.LENGTH_SHORT).show();
				return intValues;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return new ArrayList<Integer>();
	}
}