package com.neodisk.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UUIDUtils {

	public final static UUID UUID_EMPTY = new UUID(0, 0);

	public static UUID fromStringWithHyphens(String str) {
		return UUID.fromString(str);
	}

	public static UUID fromStringWhitoutHyphens(String str) {
		if (str.length() != 32) {
			throw new IllegalArgumentException("Invalid UUID string: " + str);
		}
		String s1 = "0x" + str.substring(0, 8);
		String s2 = "0x" + str.substring(9, 12);
		String s3 = "0x" + str.substring(13, 16);
		String s4 = "0x" + str.substring(17, 20);
		String s5 = "0x" + str.substring(21, 32);

		long mostSigBits = Long.decode(s1).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(s2).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(s3).longValue();

		long leastSigBits = Long.decode(s4).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(s5).longValue();

		return new UUID(mostSigBits, leastSigBits);
	}

	public static UUID fromString(String str) {
		if (str.indexOf((char) '-') >= 0) {
			return fromStringWithHyphens(str);
		} else {
			return fromStringWhitoutHyphens(str);
		}
	}

	public static String toString(UUID uuid) {
		return uuid.toString();
	}

	public static String toStringWhitoutHyphens(UUID uuid) {
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		return (digits(msb >> 32, 8) + digits(msb >> 16, 4) + digits(msb, 4)
				+ digits(lsb >> 48, 4) + digits(lsb, 12));
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	public static UUID fromByte(byte[] data) {
		if (data.length != 16) {
			throw new IllegalArgumentException("Invalid UUID byte[]");
		}

		long msb = 0;
		long lsb = 0;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (data[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);

		return new UUID(msb, lsb);
	}

	public static byte[] toByte(UUID uuid) {
		ByteArrayOutputStream ba = new ByteArrayOutputStream(16);
		DataOutputStream da = new DataOutputStream(ba);
		try {
			da.writeLong(uuid.getMostSignificantBits());
			da.writeLong(uuid.getLeastSignificantBits());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ba.toByteArray();
	}
	
	public static UUID randomUUID(){
		return UUID.randomUUID();
	}
	
	public static String randomUUIDString(){
		return toString(UUID.randomUUID());
	}
}
