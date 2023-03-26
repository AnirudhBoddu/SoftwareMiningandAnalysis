package com.concordia.soen;

public class Example {

	public static void main(String[] args) {

	}

	public void test() {
		try {
		} catch (Exception e) {
			try {
				int y = 30 / 0;
			} catch (Exception f) {
				System.out.println("Nested catch block");
			}
		}
	}

	public static void A() {
		try {
			throw new Exception();
		} catch (Exception ex) {
		}
	}

	public static void B() {
		try {
			throw new Exception();
		} catch (Exception ex) {
			try {
				throw new Exception();
			} catch (Exception e) {

			}
		}
	}

	public static void C() {
		try {
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
