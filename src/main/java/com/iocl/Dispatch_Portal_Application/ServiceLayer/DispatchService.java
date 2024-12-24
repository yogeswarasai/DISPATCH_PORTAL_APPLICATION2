package com.iocl.Dispatch_Portal_Application.ServiceLayer;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.CaptchaEntity;
import com.iocl.Dispatch_Portal_Application.Entity.MstEmployee;
import com.iocl.Dispatch_Portal_Application.Entity.MstUser;
import com.iocl.Dispatch_Portal_Application.Entity.OtpEntity;
import com.iocl.Dispatch_Portal_Application.Entity.RefSequence;
import com.iocl.Dispatch_Portal_Application.Repositaries.CaptchaRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.EmployeeRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstUserRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.OtpRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.RefSequenceRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.modal.CaptchaResponseData;
import com.iocl.Dispatch_Portal_Application.modal.EmployeeLoginModal;
@Service
public class DispatchService {

    @Autowired
    private OtpRepository otpRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MstUserRepository mstUserRepository;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RefSequenceRepository refSequenceRepository;

    @Autowired
    private CaptchaRepository captchaRepositary;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private HttpClient httpClient;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private MstUserRepository mstuserrepositary;
    
    private static final Logger logger = LoggerFactory.getLogger(DispatchService.class);


    public ResponseEntity<?> sendOtp(Long mobileNumber) throws IOException, URISyntaxException, InterruptedException {
        Optional<MstUser> userOptional = mstUserRepository.findByMobileNumber(mobileNumber);

        if (userOptional.isPresent()) {
            MstUser user = userOptional.get();
            SendOtp(user);
            logger.info("OTP sent successfully to user with ID: {}", user.getUserId());
            return ResponseEntity.ok().body("OTP sent");
 
        } else {
            logger.warn("User not found for mobile number: {}", mobileNumber);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for mobile number");
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    public void SendOtp(MstUser user) throws IOException, URISyntaxException, InterruptedException {
    	
    	
    	 Optional<MstUser> userOptional = mstUserRepository.findByMobileNumber(user.getMobileNumber());
         if (userOptional.isPresent()) {
             MstUser usertest = userOptional.get();
             String userIdTrimmed = usertest.getUserId().trim();

             Optional<MstEmployee> employeeOpt = employeeService.getEmployeeById(Integer.parseInt(userIdTrimmed));
             if (employeeOpt.isPresent()) {
                 if (!"DISPATCH".equalsIgnoreCase(user.getRoleId())) {
                     logger.warn("User with role '{}' is not allowed to log in", user.getRoleId());
                 }
             }
    	 
    	 logger.info("Generating OTP for user ID: {}", user.getUserId());
    	// String mobileNumber = "9390617401";
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmpCode(user.getUserId());
        otpEntity.setOtp(ThreadLocalRandom.current().nextInt(100000, 1000000));
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpirationAt(otpEntity.getCreatedAt().plusMinutes(10));
        otpRepository.save(otpEntity);

        String message = "OTP for Dispatch Application login is " + otpEntity.getOtp() + ", which expires in 120 seconds -IndianOil.";
        String url = "https://sandesh.indianoil.co.in/sandesh/smsPrioOutMessageRequest/priooutgoingSMS";
        //https://spandan.indianoil.co.in/sandesh/smsOutMessageRequest/outgoingSMS
        //https://spandan.indianoil.co.in/sandesh/smsPrioOutMessageRequest/priooutgoingSMS
        URL urlObj = new URL(url.trim());

        JSONArray ja = new JSONArray();
        JSONObject obj = new JSONObject();
      //  String formattedMobileNumber = "+91" + user.getMobileNumber();

        obj.put("mobile_no", String.valueOf(user.getMobileNumber()));
        obj.put("sms_content", message.toString());
        obj.put("gst_flag", "1");
        obj.put("template_id", "1107167213257427106");
        obj.put("ref_in_msg_unique_id", "1107167213257427106");
        ja.put(obj);
       
       
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(ja.toString()))
                .uri(urlObj.toURI())
                .setHeader("authenticationToken", "1085779bb2f0935b728713bc33e7d0ab")
             //   .setHeader("authenticationToken", "20241213102019084094")
                .setHeader("Content-Type", "application/json")
                .setHeader("appId", "GatePass")
              //  .setHeader("appId", "DispatchApp")
                .setHeader("appCategory", "GatePass_OTP")
               // .setHeader("appCategory", "Dispatch_OTP")
                .setHeader("msgType", "ENG")
                .build();
        request.headers().map().forEach((k, v) -> logger.info("Header: {} = {}", k, v));

        logger.info("Request URI: {}", request.uri());
        logger.info("Request headers: {}", request.headers());
        logger.info("Request body: {}", ja.toString());
        logger.info("Sending SMS request: {}", request);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("SMS API response status code: {}", response.statusCode());

        logger.info("SMS API response: {}", response.body());

        if (response.statusCode() == 200) {
            logger.info("OTP successfully generated and sent for user ID: {}", user.getUserId());
        } else {
            logger.error("Failed to send OTP. SMS API responded with status code: {}", response.statusCode());
        }
             }

    }


    
    public ResponseEntity<?> verifyOtpAndGenerateJwt(Long mobileNumber, int otp, HttpServletResponse httpresponse) {

        logger.info("Verifying OTP for mobile number: {}", mobileNumber);

        Optional<MstUser> userOptional = mstUserRepository.findByMobileNumber(mobileNumber);
        if (userOptional.isPresent()) {
            MstUser user = userOptional.get();
            String userIdTrimmed = user.getUserId().trim();

            Optional<MstEmployee> employeeOpt = employeeService.getEmployeeById(Integer.parseInt(userIdTrimmed));
            if (employeeOpt.isPresent()) {
                if (!"DISPATCH".equalsIgnoreCase(user.getRoleId())) {
                    logger.warn("User with role '{}' is not allowed to log in", user.getRoleId());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized for OTP-based login");
                }

                // Find the latest OTP for the user
                OtpEntity otpEntity = otpRepository.findFirstByEmpCodeOrderByCreatedAtDesc(user.getUserId());
                if (otpEntity != null && otpEntity.getOtp() == otp) {
                    if (otpEntity.getExpirationAt().isAfter(LocalDateTime.now())) {
                        logger.info("OTP verified successfully for user ID: {}", user.getUserId());

                        Map<String, Object> claims = new HashMap<>();
                        claims.put("userId", user.getUserId());
                        claims.put("username", user.getUserName());
                        claims.put("Designation", user.getRoleId());
                        claims.put("locName", getLocName(user.getLocCode()));
                        claims.put("locCode", user.getLocCode());

                        String jwt = jwtUtils.generateTokenFromUsernameAndClaims(user.getUserId(), claims);
                        logger.info("JWT generated successfully for user ID: {}", user.getUserId());

                        // Check if the location is implemented
                        Optional<RefSequence> refSequenceOptional = refSequenceRepository.findById(user.getLocCode().trim());
                        if (refSequenceOptional.isEmpty()) {
                            logger.warn("Location code '{}' is not implemented in the ref_sequence table", user.getLocCode());
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application not implemented for the location");
                        }

                        ResponseCookie jwtCookie = jwtUtils.createJwtCookie(jwt);
                        httpresponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                        // Create the response map
                        Map<String, Object> responseMap = new LinkedHashMap<>();
                        responseMap.put("EmpName", user.getUserName());
                        responseMap.put("Designation", getDesignation(Integer.parseInt(userIdTrimmed)));
                        responseMap.put("Role", user.getRoleId());
                        responseMap.put("locationName", getLocName(user.getLocCode()));

                        return ResponseEntity.ok(responseMap);
                    } else {
                        logger.warn("OTP expired for user ID: {}", user.getUserId());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP expired");
                    }
                } else {
                    logger.warn("Invalid OTP '{}' provided for user ID: {}", otp, user.getUserId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Employee Code For User");
            }
        } else {
            logger.warn("User with mobile number {} not found", mobileNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
    
    
    
    public ResponseEntity<Map<String, String>> checkCaptcha(String captchaValue) {
        Optional<CaptchaEntity> captchaEntity = captchaRepositary.findByValue(captchaValue);
        Map<String, String> response = new HashMap<>();
        if (captchaEntity.isPresent()) {
            CaptchaEntity captcha = captchaEntity.get();
            System.out.println("Captcha Value: " + captcha.getValue());
            System.out.println("Captcha Expiry: " + captcha.getExpiryTime());
            if (captcha.getValue().equals(captchaValue) && LocalDateTime.now().isBefore(captcha.getExpiryTime())) {
                response.put("status", "valid");
                response.put("message", "Captcha is valid");
                return ResponseEntity.ok().body(response);
            }

        }
        response.put("status", "invalid");
        response.put("message", "Invalid or expired captcha");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
    
  

    public ResponseEntity<?> login(EmployeeLoginModal loginRequest, HttpServletResponse httpresponse) {
        Logger logger = LoggerFactory.getLogger(EmployeeService.class.getName());

        logger.info("Received captcha value: " + loginRequest.getCaptcha_value());

        // Validate captcha
        Optional<CaptchaEntity> captchaEntity = captchaRepositary.findByValue(loginRequest.getCaptcha_value());
        if (captchaEntity.isEmpty() || LocalDateTime.now().isAfter(captchaEntity.get().getExpiryTime())) {
            logger.info("Invalid or expired captcha.");
            return ResponseEntity.status(400).body("Invalid or expired captcha");
        }

        // Fetch user from the database and validate credentials
        String userId = appendZeros(String.valueOf(loginRequest.getId()));
        String password = loginRequest.getPassword();

        Optional<MstUser> userOptional = mstuserrepositary.findByUserId(userId);
        if (userOptional.isEmpty()) {
            logger.info("User not found with ID: " + userId);
            return ResponseEntity.status(400).body("User not found");
        }

        MstUser user = userOptional.get();

        // Check if the provided password matches the stored password
        if (!password.equals(user.getPassword())) {
            logger.info("Invalid password for user ID: " + userId);
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // User authentication successful
        String role = getRole(user);

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));

        UserDetails userDetails = User.builder()
            .username(userId)
            .password(password)
            .authorities(authorities)
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUserName());
        claims.put("Designation", user.getRoleId());
        claims.put("locName", getLocName(user.getLocCode()));
        claims.put("locCode", user.getLocCode());

        String jwt = jwtUtils.generateTokenFromUsernameAndClaims(userId, claims);

        ResponseCookie jwtCookie = jwtUtils.createJwtCookie(jwt);
        httpresponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        // Prepare response
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("EmpName", user.getUserName());
        responseMap.put("Designation", getDesignation(Integer.parseInt(userId)));
        responseMap.put("Role", user.getRoleId());
        responseMap.put("locationName", getLocName(user.getLocCode()));
        return ResponseEntity.ok(responseMap);
    }

    public String appendZeros(String id) {
        while (id.length() < 8) {
            id = "0" + id;
        }
        return id;
    }

    public String getRole(MstUser user) {
        String roleId = user.getRoleId();
        if ("ADMIN".equalsIgnoreCase(roleId)) {
            return "ADMIN";
        } else if ("LOC_ADMIN".equalsIgnoreCase(roleId)) {
            return "LOC_ADMIN";
        } 
        else if ("DISPATCH".equalsIgnoreCase(roleId)) {
            return "DISPATCH";
        }
        else {
            return "EMPLOYEE"; // Default role
        }
    }

    
  public ResponseEntity<?> logout() {
      ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("You've been signed out!");
  }

  public ResponseEntity<CaptchaResponseData> getCaptcha() throws IOException, java.io.IOException {
  	 // Create a BufferedImage object with a width of 200 pixels, height of 40 pixels, and a color model of TYPE_BYTE_INDEXED
      BufferedImage image = new BufferedImage(200, 40, BufferedImage.TYPE_BYTE_INDEXED);
      Graphics2D graphics = image.createGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, 350, 40);
      String fontNames[] = { "Dialog", "KaiTi", "Comic Sans MS", "AngsanaUPC", "MV Boli", "Cooper Black", "MingLiU",
              "Microsoft YaHei Light", "Consolas", "Simplified Arabic", "Vijaya", "Lucida Bright",
              "Microsoft Yi Baiti", "FangSong", "Gill Sans Ultra Bold Condensed", "Malgun Gothic", "KodchiangUPC",
              "Algerian", "Calibri", "MingLiU-ExtB", "Centaur", "Showcard Gothic", "Agency FB", "Ebrima", "Forte",
              "Gadugi", "Levenim MT", "Microsoft Tai Le", "Calibri Light", "Perpetua", "Kokila", "Dialog",
              "Segoe Script", "Gill Sans MT", "Rockwell Condensed", "Segoe UI", "Sitka Subheading", "Century Gothic",
              "Yu Gothic", "Tahoma", "Raavi", "Aldhabi", "David", "Cordia New", "Bodoni MT Poster Compressed",
              "KaiTi", "KodchiangUPC", "Utsaah", "Franklin Gothic Demi", "Informal Roman", "Serif", "Browallia New",
              "MoolBoran", "Microsoft YaHei", "Snap ITC", "Imprint MT Shadow", "Bell MT", "SansSerif", "Gulim" };
      int index = (int) (Math.random() * (fontNames.length - 1));
      Font font = new Font(fontNames[index], Font.ITALIC, 35);
      graphics.setFont(font);
      String captchaValue = RandomStringUtils.randomAlphabetic(6).toUpperCase();
      CaptchaEntity captchaEntity = new CaptchaEntity();
      captchaEntity.setValue(captchaValue);
      captchaEntity.setExpiryTime(LocalDateTime.now().plusSeconds(200));
      captchaEntity = captchaRepository.save(captchaEntity);

      graphics.drawString(captchaValue, 10, 30);
      graphics.dispose();

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", baos);
      byte[] bytes = baos.toByteArray();
      baos.close();

      CaptchaResponseData responseData = new CaptchaResponseData();
      responseData.setImage(bytes);
      responseData.setId(captchaEntity.getId());
      responseData.setCaptchaValue(captchaEntity.getValue()); // Add captcha value to response data


      return ResponseEntity.ok(responseData);
  }


    private String getLocName(String locCode) {
        TypedQuery<String> query = entityManager.createQuery(
                "SELECT l.locName FROM MstLocation l WHERE l.locCode = :locCode",
                String.class
        );
        query.setParameter("locCode", locCode);
        return query.getSingleResult();
    }
   

    private String getDesignation(int userId) {
        TypedQuery<String> query = entityManager.createQuery(
                "SELECT e.designation FROM MstEmployee e WHERE e.empCode = :userId",
                String.class
        );
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    }