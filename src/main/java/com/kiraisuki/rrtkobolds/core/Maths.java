package com.kiraisuki.rrtkobolds.core;

public class Maths
{
	public static float toRadians(float degrees)
	{
		if(degrees < 0.0f)
			degrees = 360.0f + degrees;

		return (float)(degrees * (Math.PI / 180));
	}
}
