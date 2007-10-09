package org.trails.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.callback.CallbackStack;

/**
 * Squeezes a {@link CallbackStack}
 */
public class CallbackStackAdaptor implements SqueezeAdaptor
{

	private static final Log LOG = LogFactory.getLog(CallbackStackAdaptor.class);

	public static final String PREFIX = "L";
	public static final String DELIMITER = "@C";

	public Class getDataClass()
	{
		return CallbackStack.class;
	}

	public String getPrefix()
	{
		return PREFIX;
	}

	public String squeeze(DataSqueezer squeezer, Object object)
	{
		CallbackStack stack = (CallbackStack) object;

		StringBuilder builder = new StringBuilder();

		for (ICallback callback : stack.getStack())
		{
			builder.append(squeezer.squeeze(callback)).append(DELIMITER);
		}

		return new StringBuilder(PREFIX).append(compress(builder.toString())).toString();
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
	{

		String uncompressed = uncompress(string.substring(PREFIX.length()));
		CallbackStack stack = new CallbackStack();

		if (uncompressed != null)
		{
			String[] values = uncompressed.split(DELIMITER);

			for (String s : values)
			{
				stack.getStack().add((ICallback) squeezer.unsqueeze(s));
			}
		}

		return stack;
	}

	public String compress(String string)
	{
		try
		{
			ByteArrayOutputStream bosCompressed = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(bosCompressed);
			BufferedOutputStream oos = new BufferedOutputStream(gos);
			oos.write(string.getBytes());
			oos.close();

			byte[] byteArray = bosCompressed.toByteArray();
			byte[] encoded = Base64.encodeBase64(byteArray);

			return new String(encoded);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public String uncompress(String string)
	{


		try
		{
			byte[] decoded = Base64.decodeBase64(string.getBytes());

			BufferedInputStream is = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(decoded)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String uncompressed = reader.readLine();
			is.close();
			return uncompressed;

		} catch (IOException e)
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}
}