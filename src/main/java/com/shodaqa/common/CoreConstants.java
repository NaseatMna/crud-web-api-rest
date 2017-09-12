package com.shodaqa.common;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public final class CoreConstants {
    /*
	* Default Constant
	*/
    public static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    /**
     * Rest response formatting keywords
     */
    public static final String STATUS_KEY = "status";
    public static final String RESPONSE_KEY = "response";
    public static final String ERROR_ARGS_KEY = "error_args";
    public static final String ERROR_STATUS = "error";
    public static final String SUCCESS_STATUS = "success";

    /**
     * HTTP status codes
     */
    public static final int HTTP_STATUS_CODE_OK = 200;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_ACCESS_DENIED = 403;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;

    public static final String PAGE_SIZE = "pagesize";

    public static final String POST = "post";
    public static final String PUT = "put";
    public static final String DELETE = "delete";
    public static final String GET = "get";
    public static final String PATCH = "patch";

    /**
     * Meta data
     */
    public static final String META = "meta";


    /**
     * Core Mail
     */
    public static final String EMAIL_TEXT_TEMPLATE_NAME = "text/email-text";
    public static final String EMAIL_SIMPLE_TEMPLATE_NAME = "html/email-simple";
    public static final String EMAIL_WITHATTACHMENT_TEMPLATE_NAME = "html/email-withattachment";
    public static final String EMAIL_INLINEIMAGE_TEMPLATE_NAME = "html/email-inlineimage";
    public static final String EMAIL_EDITABLE_TEMPLATE_CLASSPATH_RES = "classpath:mail/html5/email.html";

    public static final String BACKGROUND_IMAGE = "";
    public static final String LOGO_BACKGROUND_IMAGE = "mail/html5/images/logo-background.png";
    public static final String THYMELEAF_BANNER_IMAGE = "mail/html5/images/thymeleaf-banner.png";
    public static final String THYMELEAF_LOGO_IMAGE = "mail/html5/images/thymeleaf-logo.png";

    public static final String PNG_MIME = "image/png";

    public static final String VERIFY_CODE_EMAIL_RESETPASSWORD_CONTENT = "<tr>\n" +
            "                                    <td>\n" +
            "                                        <p>Hi there,</p>\n" +
            "                                        <p>\tVerification code to reset BVC password\n" +
            ", we just need to make sure this email address or username is yours.</p>\n" +
            "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\">\n" +
            "                                            <tbody>\n" +
            "                                            <tr>\n" +
            "                                                <td align=\"left\">\n" +
            "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                                                        <tbody>\n" +
            "                                                        <tr>\n" +
            "                                                            <td> <span>%d</span> </td>\n" +
            "                                                        </tr>\n" +
            "                                                        </tbody>\n" +
            "                                                    </table>\n" +
            "                                                </td>\n" +
            "                                            </tr>\n" +
            "                                            </tbody>\n" +
            "                                        </table>\n" +
            "                                        <p>To reset your password, enter this verification code when prompted\n" +
            "                                        <p>Good luck! Hope it works.</p>\n" +
            "                                    </td>\n" +
            "                                </tr>";

    public static final String INVOICE_GENERATE_TO_PDF = "<tr>\n" +
            "			<h3>Hi %1$s,</h3>\n" +
            "			<p>Your invoice printing has been generated successfully. <br> Please download it by this link: <br>\n" +
            "				<a href=\"%2$s\">%2$s</a> <br>\n" +
            "Goto directory folder: <br>\n" +
            "				<a href=\"%3$s\">%3$s</a> <br>\n" +
            "Thanks\n" +
            "			</p>\n" +
            "		</tr>";

    /**
     * Schedule
     */
    public static final String SEND_MAIL_SCHEDULE = "* * * * * *";

    public static final String GENERATE_INVOICE_SCHEDULE = "* * * * * *";
}
