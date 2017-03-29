package com.wifin.kachingme.adaptors;

public class SpinnerValue {
	private String value;
	private int spinpos;

	public SpinnerValue(String itm, int pr) {
		this.value = itm;
		this.spinpos = pr;
	}

	public int hashCode() {
		int hashcode = 0;
		hashcode = spinpos * 20;
		hashcode += value.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof SpinnerValue) {
			SpinnerValue pp = (SpinnerValue) obj;
			return (pp.value.equals(this.value) && pp.spinpos == this.spinpos);
		} else {
			return false;
		}

	}

	public String getvalue() {
		return value;
	}

	public void setvalue(String value) {
		this.value = value;
	}

	public int getspinpos() {
		return spinpos;
	}

	public void setspinpos(int spinpos) {
		this.spinpos = spinpos;
	}

	public String toString() {
		return "value: " + value + "  spinpos: " + spinpos;
	}
}