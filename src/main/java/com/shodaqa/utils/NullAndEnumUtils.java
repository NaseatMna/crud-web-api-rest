package com.shodaqa.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class NullAndEnumUtils extends BeanUtilsBean {
	protected static NullAndEnumUtils instance;
	
	private NullAndEnumUtils(VAwareConvertUtilsBean enumAwareConvertUtilsBean) {
		super(enumAwareConvertUtilsBean);
	}
	
	public static synchronized NullAndEnumUtils getInstance() {
		if (instance == null) {
			/*
			 * Improvements for enums(if is available, convert string to enum):
			 */
			instance = new NullAndEnumUtils(new VAwareConvertUtilsBean());
			/*
			 * Below lines for some bug fixes
			 */
			// instance.getConvertUtils().register(new org.apache.commons.beanutils.converters.BigDecimalConverter(null), BigDecimal.class);
			instance.getConvertUtils().register(new DefaultDateConverter(), java.util.Date.class);
			instance.getConvertUtils().register(new DefaultDateConverter(), javax.xml.datatype.XMLGregorianCalendar.class);
		}
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	protected static boolean isEnumClass(Class kalas) {
		Method[] methods = kalas.getMethods();
		boolean fromValue = false;
		boolean fromString = false;
		for (Method method : methods) {
			if (method.getName().equals("fromValue")) {
				fromValue = true;
			} else if (method.getName().equals("fromString")) {
				fromString = true;
			}
		}
		return fromValue && fromString;
	}
	
	@Override
	public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		if (value == null)
			return;
		if (name.equals("hello")) {
			System.out.println();
		}
		super.copyProperty(dest, name, value);
	}
}

@SuppressWarnings("rawtypes")
class VAwareConvertUtilsBean extends ConvertUtilsBean2 {
	private static final EnumConverter ENUM_CONVERTER = new EnumConverter();
	private static final DefaultConverter DEFAULT_CONVERTER = new DefaultConverter();
	
	@Override
	public Converter lookup(Class pClazz) {
		final Converter converter = super.lookup(pClazz);
		
		if (converter == null && pClazz.isEnum() || NullAndEnumUtils.isEnumClass(pClazz)) {
			return ENUM_CONVERTER;
		} else if (converter == null) {
			return DEFAULT_CONVERTER;
		}
		return converter;
	}
}

@SuppressWarnings({"rawtypes"})
class DefaultConverter extends AbstractConverter {
	private static Object[] toObjectArray(Object[] array) {
		int length = Array.getLength(array);
		Object[] ret = new Object[length];
		for (int i = 0; i < length; i++)
			ret[i] = Array.get(array, i);
		return ret;
	}
	
	@Override
	public Object convert(Class arg0, Object arg1) {
		BeanUtilsBean beanUtils = NullAndEnumUtils.instance;
		
		if (arg0.isArray()) {
			Object[] from = toObjectArray((Object[]) arg1);
			Class componentType = arg0.getComponentType();
			
			Object[] array = (Object[]) Array.newInstance(componentType, from.length);
			
			int i = 0;
			for (Object obj : from) {
				try {
					array[i] = componentType.newInstance();
					beanUtils.copyProperties(array[i], obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
			return array;
		} else {
			try {
				Object o = arg0.newInstance();
				beanUtils.copyProperties(o, arg1);
				return o;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@Override
	protected Object convertToType(Class arg0, Object arg1) throws Throwable {
		if (arg0.isArray()) {
			Object[] from = toObjectArray((Object[]) arg1);
			Class componentType = arg0.getComponentType();
			
			Object[] array = (Object[]) Array.newInstance(componentType, from.length);
			
			BeanUtilsBean beanUtils = NullAndEnumUtils.instance;
			
			int i = 0;
			for (Object obj : from) {
				try {
					beanUtils.copyProperties(array[i], obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
			return array;
		}
		return null;
	}
	
	@Override
	protected Class getDefaultType() {
		return null;
	}
}

@SuppressWarnings({"rawtypes", "unchecked"})
class EnumConverter extends AbstractConverter {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnumConverter.class);
	
	private static String enumToString(Object value) {
		Method[] methods = value.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals("value")) {
				try {
					return (String) method.invoke(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return value.toString();
	}
	
	@Override
	protected String convertToString(final Object pValue) throws Throwable {
		return ((Enum) pValue).name();
	}
	
	@Override
	protected Object convertToType(final Class pType, final Object pValue) throws Throwable {
		if (NullAndEnumUtils.isEnumClass(pType)) {
			try {
				Method method = pType.getMethod("fromString", String.class);
				return method.invoke(null, enumToString(pValue));
			} catch (Throwable e) {
				return null;
			}
		} else {
			final Class<? extends Enum> type = pType;
			try {
				return Enum.valueOf(type, enumToString(pValue));
			} catch (final IllegalArgumentException e) {
				LOGGER.error("!!! No enum value \"" + pValue + "\" for " + type.getName());
			}
		}
		
		return null;
	}
	
	@Override
	protected Class getDefaultType() {
		return null;
	}
}

@SuppressWarnings({"rawtypes"})
class DefaultDateConverter extends AbstractConverter {
	
	@Override
	protected String convertToString(final Object pValue) throws Throwable {
		return pValue.toString();
	}
	
	@Override
	protected Object convertToType(final Class pType, final Object pValue) throws Throwable {
		Object out = null;
		
		try {
			DateConverter d = new DateConverter();
			out = d.convert(pType, pValue);
			
			return out;
		} catch (Exception e) {
		}
		
		if (pType.getName().equals("java.util.Date")) {
			if (pValue instanceof XMLGregorianCalendar) {
				out = ((XMLGregorianCalendar) pValue).toGregorianCalendar().getTime();
			} else if (pValue instanceof String) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				try {
					out = sdf.parse((String) pValue);
				} catch (Exception e) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						out = sdf.parse((String) pValue);
					} catch (Exception e2) {
						sdf = new SimpleDateFormat("dd/MM/yyyy");
						try {
							out = sdf.parse((String) pValue);
						} catch (Exception e3) {
							sdf = new SimpleDateFormat("dd.MM.yyyy");
							try {
								out = sdf.parse((String) pValue);
							} catch (Exception e4) {
								sdf = new SimpleDateFormat(); // ISO format
								try {
									out = sdf.parse((String) pValue);
								} catch (Exception e5) {
									out = pValue;
								}
							}
						}
					}
				}
			}
		} else if (pType.getName().equals("javax.xml.datatype.XMLGregorianCalendar") && pValue instanceof java.util.Date) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime((java.util.Date) pValue);
			out = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} else {
			out = pValue;
		}
		
		return out;
	}
	
	@Override
	protected Class getDefaultType() {
		return null;
		
	}
}

