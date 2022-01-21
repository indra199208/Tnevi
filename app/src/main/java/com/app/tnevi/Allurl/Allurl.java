package com.app.tnevi.Allurl;

public class Allurl {


    public static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static String KEY_PASSWORD = null;
    public static String USER_NAME = "USER_NAME";
    public static String baseUrl;
    public static String RegistrationUrl;
    public static String loginUrl;
    public static String EmailVerification;
    public static String ResendOTP;
    public static String ForgotPassword;
    public static String ResetPassword;
    public static String GetAccount;
    public static String UpdateAccount;
    public static String UpdateLocation;
    public static String ChangePassword;
    public static String UpdateNotification;
    public static String CreateEvent;
    public static String GetCurrency;
    public static String GetPlans;
    public static String GetMyEvents;
    public static String GetEventDetails;
    public static String GetEvent;
    public static String AddRemoveFav;
    public static String SocailLogin;
    public static String AllCategory;
    public static String ContactOrganizer;
    public static String RefferalCode;
    public static String RemoveGallery;
    public static String EventReview;
    public static String ContactSupport;
    public static String FAQ;
    public static String EventTicket;
    public static String TicketCheckin;
    public static String CMS;
    public static String SeatAvibility;
    public static String BookTicket;
    public static String TicketSalesReport;
    public static String ViewRewards;
    public static String Cashout;
    public static String Review;
    public static String TicksalesList;
    public static String CommissionList;
    public static String WalletHistory;
    public static String Spam;
    public static String MarkSold;
    public static String TicketSalesReportDetails;
    public static String DeleteEvent;
    public static String commissionReport;
    public static String commissionReportDetails;
    public static String CouponList;
    public static String CouponApply;
    public static String DownloadPurchase;
    public static String DownloadTicket;
    public static String ApplyEpoints;
    public static String ViewTickets;
    public static String EmailTicketSend;
    public static String QRlist;
    public static String NotificationList;
    public static String ReportDownload;
    public static String PayoutDownload;
    public static String EventsMayLike;
    public static String EpointsList;
    public static String Buyepoints;
    public static String Applycash;
    public static String FeaturedNow;
    public static String StripePayment;
    public static String SaveCards;
    public static String AddNewCards;








    static {

        baseUrl = "http://dev8.ivantechnology.in/tnevi/api/v1";
        RegistrationUrl = baseUrl + "/signup";
        loginUrl = baseUrl + "/signin";
        EmailVerification = baseUrl + "/emailverification";
        ResendOTP = baseUrl + "/sendotp";
        ForgotPassword = baseUrl +"/verify-forgot-pass-otp";
        ResetPassword = baseUrl + "/reset-password";
        GetAccount = baseUrl + "/getaccount";
        UpdateAccount = baseUrl + "/update-account";
        UpdateLocation = baseUrl + "/update-location";
        ChangePassword = baseUrl + "/update-password";
        UpdateNotification = baseUrl + "/update-notification-settings";
        CreateEvent = baseUrl + "/create-event";
        GetCurrency = baseUrl + "/get-currencies";
        GetPlans = baseUrl + "/get-plans";
        GetMyEvents = baseUrl + "/get-my-event";
        GetEventDetails = baseUrl + "/event-details";
        GetEvent = baseUrl+"/get-event";
        AddRemoveFav = baseUrl + "/add-remove-fav";
        SocailLogin = baseUrl+"/signin";
        AllCategory = baseUrl+"/get-categories";
        ContactOrganizer = baseUrl+"/contact-organizer";
        RefferalCode = baseUrl + "/get-event-referral-code";
        RemoveGallery = baseUrl+"/remove-gallery-file";
        EventReview = baseUrl+"/event-review";
        ContactSupport = baseUrl+"/contact-support";
        FAQ = baseUrl+"/faq_question";
        EventTicket = baseUrl+"/event-ticket";
        TicketCheckin = baseUrl+"/event-ticket-checkin";
        CMS = baseUrl+"/cms-page";
        SeatAvibility = baseUrl+"/check-ticket-availability";
        BookTicket = baseUrl+"/ticket-booking";
        TicketSalesReport = baseUrl+"/ticket-sale-report";
        ViewRewards = baseUrl+"/view-rewards";
        Cashout = baseUrl+"/cash-out-request";
        Review = baseUrl+"/profile-review";
        TicksalesList = baseUrl+"/ticket-sales-list";
        CommissionList = baseUrl+"/commission-list";
        WalletHistory = baseUrl+"/wallet_history";
        Spam = baseUrl+"/add-to-spam";
        MarkSold = baseUrl+"/mark-as-sold";
        TicketSalesReportDetails = baseUrl+"/ticket-sale-report-details";
        DeleteEvent = baseUrl+"/delete-event";
        commissionReport = baseUrl+"/commission-report";
        commissionReportDetails = baseUrl+"/commission-report-details";
        CouponList = baseUrl+"/get-coupon";
        CouponApply = baseUrl+"/apply-coupon";
        DownloadPurchase = baseUrl+"/event-report-export";
        DownloadTicket = baseUrl+"/download-ticket";
        ApplyEpoints = baseUrl+"/apply-epoints";
        ViewTickets = baseUrl+"/view-ticket";
        EmailTicketSend = baseUrl + "/email-ticket";
        QRlist = baseUrl+"/qr-code-for-seats";
        NotificationList = baseUrl+"/user_notification";
        ReportDownload = baseUrl + "/event-attendance-report";
        PayoutDownload = baseUrl + "/event-payout-report";
        EventsMayLike = baseUrl+"/events-may-like";
        EpointsList = baseUrl+"/get-epoint-plans";
        Buyepoints = baseUrl+"/buy-epoints";
        Applycash = baseUrl+"/apply-cash";
        FeaturedNow = baseUrl+"/featured-now";
        StripePayment = baseUrl+"/charge_amount";
        SaveCards = baseUrl+"/find_customer";
        AddNewCards = baseUrl+"/customer_create";



        USER_NAME = "user_name";
        KEY_PASSWORD = "password";
    }
}
