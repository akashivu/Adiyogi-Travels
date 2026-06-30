package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailTemplateService {

    // ══════════════════════════════════════════════════════════════
    // Constants
    // ══════════════════════════════════════════════════════════════

    private static final String SUPPORT_EMAIL = "adiyogicabz@gmail.com";
    private static final String SUPPORT_PHONE = "+91 76769 43788";
    private static final String WEBSITE        = "www.adiyogicabz.com";

    // ══════════════════════════════════════════════════════════════
    // Formatters
    // ══════════════════════════════════════════════════════════════

    private static final DateTimeFormatter DATE_IN  =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_OUT =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_IN  =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIME_OUT =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter BOOKING_TIME_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private String fmtDate(Object raw) {
        try {
            return LocalDate.parse(String.valueOf(raw), DATE_IN).format(DATE_OUT);
        } catch (Exception e) {
            return String.valueOf(raw);
        }
    }

    private String fmtTime(Object raw) {
        try {
            return LocalTime.parse(String.valueOf(raw), TIME_IN).format(TIME_OUT);
        } catch (Exception e) {
            return String.valueOf(raw);
        }
    }

    private String fmtRef(Object id) {
        return String.format("ADC-%06d", Long.parseLong(String.valueOf(id)));
    }

    private String fmtFare(Object fare) {
        try {
            return String.format("₹%,.2f", Double.parseDouble(String.valueOf(fare)));
        } catch (Exception e) {
            return "₹" + fare;
        }
    }

    private String bookingTimestamp() {
        return LocalDateTime.now().format(BOOKING_TIME_FMT);
    }

    // ══════════════════════════════════════════════════════════════
    // Status Badge
    // ══════════════════════════════════════════════════════════════

    private String statusBadge(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "<span class='status status-confirmed'>✦ CONFIRMED</span>";
            case "PENDING"   -> "<span class='status status-pending'>◎ PENDING</span>";
            case "CANCELLED" -> "<span class='status status-cancelled'>✕ CANCELLED</span>";
            default          -> "<span class='status'>" + status.toUpperCase() + "</span>";
        };
    }

    // ══════════════════════════════════════════════════════════════
    // Shared Styles
    // ══════════════════════════════════════════════════════════════

    private String styles() {
        return """
                <style>
                *{margin:0;padding:0;box-sizing:border-box;
                  font-family:'Segoe UI',Arial,Helvetica,sans-serif;}

                body{background:#f0f4f8;padding:30px;color:#1f2937;}

                .container{max-width:680px;margin:auto;background:#ffffff;
                    border-radius:16px;overflow:hidden;border:1px solid #e2e8f0;
                    box-shadow:0 12px 32px rgba(0,0,0,.09);}

                .header{background:linear-gradient(135deg,#15803d,#22c55e);
                    color:white;padding:36px 35px;text-align:center;}
                .logo-wrap{display:flex;align-items:center;justify-content:center;
                    gap:10px;margin-bottom:10px;}
                .logo-emoji{font-size:30px;line-height:1;}
                .logo-text{font-size:24px;font-weight:700;letter-spacing:.4px;}
                .header-title{font-size:26px;font-weight:700;margin-bottom:6px;}
                .header-sub{font-size:14px;opacity:.92;}

                .content{padding:36px 35px;}
                h2{color:#111827;font-size:20px;margin-bottom:8px;}
                p{line-height:1.75;font-size:15px;color:#374151;}

                table{width:100%;border-collapse:collapse;margin:22px 0;}
                th{background:#f0fdf4;color:#15803d;padding:13px 15px;text-align:left;
                    width:38%;border:1px solid #dcfce7;font-size:13px;font-weight:600;}
                td{padding:13px 15px;border:1px solid #e5e7eb;font-size:14px;color:#374151;}

                .status{display:inline-block;padding:5px 14px;border-radius:30px;
                    font-weight:700;font-size:12px;letter-spacing:.5px;}
                .status-confirmed{background:#dcfce7;color:#15803d;}
                .status-pending  {background:#fef9c3;color:#854d0e;}
                .status-cancelled{background:#fee2e2;color:#991b1b;}

                .card-success{background:#f0fdf4;border-left:5px solid #16a34a;
                    padding:20px 22px;margin:20px 0;border-radius:10px;}
                .card-success-title{color:#15803d;font-size:16px;font-weight:700;
                    margin-bottom:8px;}
                .card-success p{font-size:14px;color:#166534;line-height:1.7;}

                .card{background:#f8fafc;border-left:5px solid #16a34a;
                    padding:20px 22px;margin:20px 0;border-radius:10px;}
                .card-title{color:#111827;font-size:15px;font-weight:700;margin-bottom:12px;}
                .card p{font-size:14px;color:#374151;line-height:1.9;}
                .card a{color:#15803d;text-decoration:none;font-weight:600;}

                .divider{height:1px;background:#e5e7eb;margin:26px 0;}

                .footer{background:#f8fafc;padding:24px 35px;text-align:center;
                    color:#6b7280;font-size:13px;border-top:1px solid #e5e7eb;}
                .footer a{color:#16a34a;text-decoration:none;font-weight:600;}
                .footer-links{display:flex;justify-content:center;gap:20px;
                    margin:10px 0;flex-wrap:wrap;}

                @media(max-width:620px){
                    body{padding:10px;}
                    .content{padding:20px;}
                    table,tr,td,th{display:block;width:100%;}
                    th{border-bottom:none;}
                    td{border-top:none;margin-bottom:8px;}
                    .footer-links{flex-direction:column;gap:8px;}
                }
                </style>
                """;
    }

    // ══════════════════════════════════════════════════════════════
    // Table Row
    // ══════════════════════════════════════════════════════════════

    private String row(String emoji, String label, Object value) {
        return "<tr>"
                + "<th>" + emoji + "&nbsp; " + label + "</th>"
                + "<td>" + String.valueOf(value) + "</td>"
                + "</tr>\n";
    }

    // ══════════════════════════════════════════════════════════════
    // Header  &  Footer
    // ══════════════════════════════════════════════════════════════

    private String header(String title, String subtitle) {
        return "<div class='header'>"
                + "<div class='logo-wrap'>"
                + "<span class='logo-emoji'>🚖</span>"
                + "<span class='logo-text'>AdiyogiCabz</span>"
                + "</div>"
                + "<div class='header-title'>" + title + "</div>"
                + "<div class='header-sub'>"  + subtitle + "</div>"
                + "</div>";
    }

    private String footer() {
        return "<div class='footer'>"
                + "<p><b>AdiyogiCabz</b> — Reliable rides, every time.</p>"
                + "<div class='footer-links'>"
                + "<a href='mailto:" + SUPPORT_EMAIL + "'>" + SUPPORT_EMAIL + "</a>"
                + "<a href='tel:+917676943788'>"           + SUPPORT_PHONE  + "</a>"
                + "<a href='https://"  + WEBSITE + "'>"    + WEBSITE        + "</a>"
                + "</div>"
                + "<p style='margin-top:10px;'>© 2026 AdiyogiCabz. All Rights Reserved.</p>"
                + "</div>";
    }

    // ══════════════════════════════════════════════════════════════
    // Customer Booking Email
    // ══════════════════════════════════════════════════════════════

    public String buildCustomerBookingEmail(Booking booking) {

        String ref    = fmtRef(booking.getId());
        String fare   = fmtFare(booking.getFare());
        String date   = fmtDate(booking.getPickupDate());
        String time   = fmtTime(booking.getPickupTime());
        String status = statusBadge(booking.getStatus().name());

        String successCard =
                "<div class='card-success'>"
                        + "<div class='card-success-title'>✅ Booking Confirmed</div>"
                        + "<p>Your cab has been booked successfully.</p>"
                        + "<p>Driver details will be shared approximately "
                        + "<strong>30–60 minutes before pickup.</strong></p>"
                        + "</div>";

        String table = "<table>\n"
                + row("🔖", "Booking Reference", ref)
                + row("📌", "Status",            status)
                + row("🚗", "Vehicle",           booking.getVehicleName())
                + row("🛣️", "Trip Type",         booking.getTripType())
                + row("📍", "Pickup",            booking.getFromLocation())
                + row("🏁", "Destination",       booking.getToLocation())
                + row("📅", "Pickup Date",       date)
                + row("⏰", "Pickup Time",       time)
                + row("📱", "Mobile",            booking.getMobileNo())
                + row("📏", "Distance",          booking.getDistanceKm() + " km")
                + row("💰", "Fare",              fare)
                + "</table>";

        String helpCard =
                "<div class='card'>"
                        + "<div class='card-title'>🙋 Need Help?</div>"
                        + "<p>📧 <a href='mailto:" + SUPPORT_EMAIL + "'>" + SUPPORT_EMAIL + "</a></p>"
                        + "<p>📞 <a href='tel:+917676943788'>"            + SUPPORT_PHONE  + "</a></p>"
                        + "<p>🌐 <a href='https://" + WEBSITE + "'>"      + WEBSITE        + "</a></p>"
                        + "</div>";

        return "<!DOCTYPE html><html><head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>Booking Confirmed – AdiyogiCabz</title>"
                + styles()
                + "</head><body><div class='container'>"
                + header("Booking Confirmed! 🎉",
                "Your ride is all set — sit back and relax.")
                + "<div class='content'>"
                + "<h2>Hello " + booking.getCustomerName() + ",</h2>"
                + "<p>Thank you for choosing <strong>AdiyogiCabz</strong>. "
                + "Your booking has been confirmed successfully.</p>"
                + successCard
                + table
                + "<div class='divider'></div>"
                + helpCard
                + "</div>"
                + footer()
                + "</div></body></html>";
    }

    // ══════════════════════════════════════════════════════════════
    // Admin Booking Email
    // ══════════════════════════════════════════════════════════════

    public String buildAdminBookingEmail(Booking booking) {

        String ref       = fmtRef(booking.getId());
        String fare      = fmtFare(booking.getFare());
        String date      = fmtDate(booking.getPickupDate());
        String time      = fmtTime(booking.getPickupTime());
        String status    = statusBadge(booking.getStatus().name());
        String bookedAt  = bookingTimestamp();

        String alertCard =
                "<div class='card-success'>"
                        + "<div class='card-success-title'>🔔 New Booking Received</div>"
                        + "<p>A new booking has been placed and confirmed. "
                        + "Please assign a driver at the earliest.</p>"
                        + "</div>";

        String table = "<table>\n"
                + row("🔖", "Booking Reference",  ref)
                + row("📌", "Status",             status)
                + row("🕐", "Booking Time",       bookedAt)
                + row("👤", "Customer Name",      booking.getCustomerName())
                + row("📧", "Customer Email",     booking.getCustomerEmail())
                + row("📱", "Customer Mobile",    booking.getMobileNo())
                + row("🚗", "Vehicle",            booking.getVehicleName())
                + row("🛣️", "Trip Type",          booking.getTripType())
                + row("📍", "Pickup",             booking.getFromLocation())
                + row("🏁", "Destination",        booking.getToLocation())
                + row("📅", "Pickup Date",        date)
                + row("⏰", "Pickup Time",        time)
                + row("📏", "Distance",           booking.getDistanceKm() + " km")
                + row("💰", "Fare",               fare)
                + "</table>";

        String actionCard =
                "<div class='card'>"
                        + "<div class='card-title'>⚡ Action Required</div>"
                        + "<p>Log in to the admin panel and assign a driver for this booking.</p>"
                        + "<p style='margin-top:8px;'>📧 <a href='mailto:" + SUPPORT_EMAIL
                        + "'>" + SUPPORT_EMAIL + "</a></p>"
                        + "</div>";

        return "<!DOCTYPE html><html><head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>New Booking Alert – AdiyogiCabz Admin</title>"
                + styles()
                + "</head><body><div class='container'>"
                + header("New Booking Alert 🔔",
                "A new booking has just been confirmed.")
                + "<div class='content'>"
                + "<h2>Hello Admin,</h2>"
                + "<p>A new booking has been received from "
                + "<strong>" + booking.getCustomerName() + "</strong>. "
                + "Full details are below.</p>"
                + alertCard
                + table
                + "<div class='divider'></div>"
                + actionCard
                + "</div>"
                + footer()
                + "</div></body></html>";
    }

    // ══════════════════════════════════════════════════════════════
    // OTP Verification Email
    // ══════════════════════════════════════════════════════════════

    public String buildOtpEmail(String fullName, String otp) {

        String otpBox =
                "<div style='text-align:center;background:#f0fdf4;"
                        + "border:2px dashed #22c55e;border-radius:12px;"
                        + "padding:25px;margin:25px 0;'>"
                        + "<div style='font-size:42px;font-weight:bold;"
                        + "color:#16a34a;letter-spacing:8px;'>"
                        + otp
                        + "</div>"
                        + "</div>";

        String noteCard =
                "<div class='card'>"
                        + "⏳ This OTP is valid for <strong>10 minutes</strong>."
                        + "<br><br>"
                        + "For your security, never share this OTP with anyone."
                        + "</div>";

        return "<!DOCTYPE html><html><head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>Verify Your Email – AdiyogiCabz</title>"
                + styles()
                + "</head><body><div class='container'>"
                + header("🔐 Verify Your Email",
                "Complete your AdiyogiCabz registration")
                + "<div class='content'>"
                + "<h2>Hello " + fullName + " 👋</h2>"
                + "<p>Thank you for registering with <strong>AdiyogiCabz</strong>.</p>"
                + "<br>"
                + "<p>Please use the verification code below to activate your account.</p>"
                + "<br>"
                + otpBox
                + noteCard
                + "<div class='divider'></div>"
                + "<p>If you did not create an account, you can safely ignore this email.</p>"
                + "</div>"
                + footer()
                + "</div></body></html>";
    }
    public String buildForgotPasswordOtpEmail(String fullName, String otp) {

        return """
            <!DOCTYPE html>
            <html>
            <head>
            """
                + styles() +
                """
                </head>
    
                <body>
    
                <div class="container">
    
                """
                + header(
                "🔑 Reset Your Password",
                "Secure password reset verification"
        )
                + """

            <div class="content">

                <h2>Hello,
            """
                + fullName +
                """
                    👋</h2>
    
                    <p>
    
                        We received a request to reset your
                        <strong>AdiyogiCabz</strong> account password.
    
                    </p>
    
                    <br>
    
                    <p>
    
                        Use the verification code below to continue.
    
                    </p>
    
                    <br>
    
                    <div
                        style="text-align:center;
                               background:#f0fdf4;
                               border:2px dashed #16a34a;
                               border-radius:12px;
                               padding:25px;
                               margin:25px 0;">
    
                        <div
                            style="
                                font-size:42px;
                                font-weight:bold;
                                color:#16a34a;
                                letter-spacing:8px;">
    
                            """
                + otp +
                """
                        </div>
    
                    </div>
    
                    <div class="card">
    
                        ⏳ This OTP expires in
                        <strong>10 minutes</strong>.
    
                        <br><br>
    
                        If you did not request a password reset,
                        please ignore this email.
    
                    </div>
    
                </div>
    
                """
                + footer() +
                """
    
                </div>
    
                </body>
    
                </html>
    
                """;
    }
}