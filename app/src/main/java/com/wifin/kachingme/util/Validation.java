package com.wifin.kachingme.util;

import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author prabhakaran
 * 
 *         This will validate the email address and phone number in the
 *         register_activity page
 */
public class Validation {
	// Regular Expression
	// you can change the expression based on your need
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
	private static final String PASSWORD_REGEX = "^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9_-]*$";

	// Error Messages
	private static final String REQUIRED_MSG = "required";
	private static final String EMAIL_MSG = "invalid email";
	private static final String PHONE_MSG = "Invalid";
	private static final String PASWORD_MSG = "Invalid";

	// call this method when you need to check email validation
	public static boolean isEmailAddress(EditText editText, boolean required) {
		return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
	}

	// call this method when you need to check phone number validation
	public static boolean isPhoneNumber(EditText editText, boolean required) {
		return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
	}

	public static boolean isPassword(EditText editText, boolean required) {
		return isValid(editText, PASSWORD_REGEX, PASWORD_MSG, required);
	}

	// return true if the input field is valid, based on the parameter passed
	public static boolean isValid(EditText editText, String regex,
			String errMsg, boolean required) {

		String text = editText.getText().toString().trim();
		// clearing the error, if it was previously set by some other values
		editText.setError(null);
		// text required and editText is blank, so return false
		if (required && !hasText(editText))
			return false;
		// pattern doesn't match so returning false
		if (required && !Pattern.matches(regex, text)) {
			editText.setError(errMsg);
			return false;
		}
		return true;
	}

	// check the input field has any text or not
	// return true if it contains text otherwise false
	public static boolean hasText(EditText editText) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0) {
			//editText.setError(REQUIRED_MSG);
			return false;
		}
		return true;
	}

	public static boolean hasText_1(EditText editText) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() != 6) {
			editText.setError(REQUIRED_MSG);
			return false;
		}
		return true;
	}

	// check the input field has any text or not
	// return true if it contains text otherwise false
	public static boolean hasText1(TextView editText) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0) {
			//editText.setError(REQUIRED_MSG);
			return false;
		}
		return true;
	}

	// check the input field has any text or not
	// return true if it contains text otherwise false
	public static boolean hasText2(EditText editText) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0) {
			editText.setError(REQUIRED_MSG);
			return false;
		}
		// if(text.matches("[a-zA-Z.? ]*")){
		// editText.setError("Only character");
		// return false;
		// }
		//
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		boolean b = m.find();
		if (b) {
			editText.setError("Only character");
			return false;
		}
		return true;
	}
}