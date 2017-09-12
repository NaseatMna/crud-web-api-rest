package com.shodaqa.utils;

import com.shodaqa.common.CoreConstants;
import com.shodaqa.exceptions.RestErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static com.shodaqa.utils.CollectionUtils.mapEntry;
import static com.shodaqa.utils.CollectionUtils.newHashMap;

public final class RestUtils {
	
	/**
	 *
	 */
	public static final int TIMEOUT_IN_SECOND = 5;
	private static final String VERBOSE = "verbose";
	
	private RestUtils() {
	
	}
	
	/**
	 * Creates a map to be converted to a Json map in the response's body.
	 *
	 * @return A map contains "status"="success".
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> successStatus() {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, (Object) CoreConstants.SUCCESS_STATUS));
	}
	
	/**
	 * @param response .
	 * @return A map contains "status"="success", "response"=response.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> successStatus(final Object response) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.SUCCESS_STATUS),
				mapEntry(CoreConstants.RESPONSE_KEY, response));
	}
	
	/**
	 * @param responseKey .
	 * @param response    .
	 * @return A map contains "status"="success", responseKey=response.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> successStatus(final String responseKey, final Object response) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.SUCCESS_STATUS),
				mapEntry(responseKey, response));
	}
	
	/**
	 * @param errorDesc .
	 * @return A map contains "status"="error", "error"=errorDesc.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> errorStatus(final String errorDesc) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.ERROR_STATUS),
				mapEntry(CoreConstants.ERROR_STATUS, errorDesc));
	}
	
	/**
	 * @param errorDesc .
	 * @param args      .
	 * @return A map contains "status"="error", "error"=errorDesc, "error_args"=args.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> errorStatus(final String errorDesc, final String... args) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.ERROR_STATUS),
				mapEntry(CoreConstants.ERROR_STATUS, errorDesc),
				mapEntry(CoreConstants.ERROR_ARGS_KEY, args));
	}
	
	/************
	 * Creates a Rest error map with verbose data.
	 *
	 * @param errorDesc
	 *            the error name.
	 * @param verboseData
	 *            the verbose data.
	 * @param args
	 *            the error description parameters.
	 * @return the rest error map.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> verboseErrorStatus(final String errorDesc, final String verboseData,
														 final String... args) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.ERROR_STATUS),
				mapEntry(CoreConstants.ERROR_STATUS, errorDesc),
				mapEntry(CoreConstants.ERROR_ARGS_KEY, args), mapEntry(VERBOSE, verboseData));
	}
	
	/************
	 * Creates a Rest error map.
	 *
	 * @param errorDesc
	 *            .
	 * @param args
	 *            .
	 * @return A map contains "status"="error", "error"=errorDesc, "error_args"=args.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> errorStatus(final String errorDesc, final Object... args) {
		return newHashMap(mapEntry(CoreConstants.STATUS_KEY, CoreConstants.ERROR_STATUS),
				mapEntry(CoreConstants.ERROR_STATUS, errorDesc),
				mapEntry(CoreConstants.ERROR_ARGS_KEY, args));
	}
	
	/**
	 * @param response   .
	 * @param httpMethod .
	 * @return response's body.
	 * @throws IOException        .
	 * @throws RestErrorException .
	 */
	public static String getResponseBody(final HttpResponse response, final HttpRequestBase httpMethod)
			throws IOException, RestErrorException {
		
		InputStream instream = null;
		try {
			final HttpEntity entity = response.getEntity();
			if (entity == null) {
				final RestErrorException e =
						new RestErrorException("comm_error",
								httpMethod.getURI().toString(), " response entity is null");
				throw e;
			}
			instream = entity.getContent();
			return getStringFromStream(instream);
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param is .
	 * @return .
	 * @throws IOException .
	 */
	public static String getStringFromStream(final InputStream is)
			throws IOException {
		final BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
}
