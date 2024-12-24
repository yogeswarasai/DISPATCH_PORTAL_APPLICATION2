
package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.iocl.Dispatch_Portal_Application.Repositaries.CaptchaRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.EmployeeRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstUserRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.RefSequenceRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.modal.CaptchaResponseData;
import com.iocl.Dispatch_Portal_Application.modal.EmployeeLoginModal;
import com.iocl.Dispatch_Portal_Application.modal.ProfileResponse;

import io.jsonwebtoken.io.IOException;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private MstUserRepository mstuserrepositary;
    

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EntityManager entityManager;

    
   private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


    public EmployeeService(EmployeeRepository employeeRepository, CaptchaRepository captchaRepository,
                           JwtUtils jwtUtils, EntityManager entityManager, MstUserRepository mstuserrepositary) {
        this.employeeRepository = employeeRepository;
        this.captchaRepository = captchaRepository;
        this.jwtUtils = jwtUtils;
        this.entityManager = entityManager;
        this.mstuserrepositary = mstuserrepositary;
    }

    public List<String> getAllEmployeeNames() {
        return employeeRepository.findAllEmpNames();
    }
    public Optional<MstEmployee> getEmployeeById(int empCode) {
        return employeeRepository.findById(empCode);
    }

    public List<MstEmployee> getEmployeesByLoc(String loc, String dept) {
        return employeeRepository.findEmployeeNamesByLocCodeAndPsaAndStatus(loc,dept);
    }

    public Optional<MstEmployee> getEmployee(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        int id = Integer.parseInt(jwtUtils.getUserNameFromJwtToken(jwt));
        return employeeRepository.findById(id);
    }
    
    public List<String> getAllEmployeeCodesWithZeros() {
        // Fetch all employee codes as integers
        List<Integer> employeeCodes = employeeRepository.findAllEmployeeCodes();

        // Convert each employee code to a string and append leading zeros
        return employeeCodes.stream()
                            .map(empCode -> appendZeros(empCode.toString())) // Convert Integer to String
                            .collect(Collectors.toList());
    }

    
    public List<String> getEmpCodesByLocCodes(String locName) {
        List<String> empCodes = employeeRepository.findEmpCodesByLocCode(locName);

        // Append leading zeros to each emp_code
        return empCodes.stream()
                       .map(this::appendZeros)
                       .collect(Collectors.toList());
    }
    
    public String getUserNameByUserId(String userId) {
        // Remove leading zeros from userId
        String empCodeWithoutZeros = stripLeadingZeros(userId);
        // Convert to Integer
        Integer empCode = Integer.parseInt(empCodeWithoutZeros);        
        // Fetch the username from emp_db based on the emp_code
        Optional<String> userName = employeeRepository.findEmpNameByEmpCode(empCode);
        // Return the username or "User not found" if the Optional is empty
        return userName.orElse("User not found");
    }

    private String stripLeadingZeros(String userId) {
        return userId.replaceFirst("^0+(?!$)", "");
    }

    
    public List<String> getEmployeeNamesByPsaCode(String locName , String psa) {
        return employeeRepository.findEmployeeNamesByLocNameAndPsaAndStatus(locName , psa);
    }

    public List<String> getEmployeeNamesByLoc(String locCode, String dept)
    {
    	List<MstEmployee> employees = getEmployeesByLoc(locCode,dept); 
    	return employees.stream() .map(MstEmployee::getEmpName) // Adjust this if the name field is different 
    			.collect(Collectors.toList()); 
    }  
    
      public String getRole(MstEmployee employee) {
      Optional<MstUser> userOptional =mstuserrepositary.findByUserId(appendZeros(String.valueOf(employee.getEmpCode())));
        
        if (userOptional.isPresent()) {
            MstUser user = userOptional.get();
            String roleId = user.getRoleId();
            
            if ("ADMIN".equalsIgnoreCase(roleId)) {
                return "ADMIN";
            } else if ("LOC_ADMIN".equalsIgnoreCase(roleId)) {
                return "LOC_ADMIN";
            } 
             else {
                return "EMPLOYEE"; // or any default role you want to set
            }
        }
        
        return "EMPLOYEE"; // Default role if user is not found
    }
    
    


      
      
      public ResponseEntity<Map<String, String>> checkCaptcha(String captchaValue) {
          Optional<CaptchaEntity> captchaEntity = captchaRepository.findByValue(captchaValue);
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

        Optional<CaptchaEntity> captchaEntity = captchaRepository.findByValue(loginRequest.getCaptcha_value());

        if (captchaEntity.isEmpty()) {
            logger.info("Captcha value not found in the database.");
            return ResponseEntity.status(400).body("Invalid or expired captcha");
        }

        CaptchaEntity captcha = captchaEntity.get();
        logger.info("Stored captcha value: " + captcha.getValue());
        logger.info("Captcha expiry time: " + captcha.getExpiryTime());

        if (!captcha.getValue().equals(loginRequest.getCaptcha_value())) {
            logger.info("Captcha value does not match.");
            return ResponseEntity.status(400).body("Invalid or expired captcha");
        }

        if (LocalDateTime.now().isAfter(captcha.getExpiryTime())) {
            logger.info("Captcha has expired.");
            return ResponseEntity.status(400).body("Invalid or expired captcha");
        }

        // LDAP Authentication Logic
        String username = appendZeros(String.valueOf(loginRequest.getId()));
        String password = loginRequest.getPassword();

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "LDAP://dcmkho03:389 LDAP://dcmkho1:389 LDAP://dcmkho2:389");
        env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            logger.info("Attempting to connect to LDAP server with username: " + username);
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            logger.info("LDAP authentication successful.");

            Optional<MstEmployee> employeeOptional = employeeRepository.findByEmpCode(loginRequest.getId());
            if (employeeOptional.isEmpty()) {
                logger.info("User not found with ID: " + username);
                return ResponseEntity.status(400).body("User not found");
            }
            MstEmployee employee = employeeOptional.get();

            String role = getRole(employeeOptional.get());
            System.out.println("Role before setting authorities: " + role);  // Add this line for debugging

//            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));


            UserDetails userDetails = User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .build();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", employee.getEmpCode());
            claims.put("username", employee.getEmpName());
            claims.put("locCode", employee.getLocCode());
            claims.put("Designation", employee.getDesignation());
            claims.put("role", role);
            claims.put("locationName", employee.getLocName());

            String jwt = jwtUtils.generateTokenFromUsernameAndClaims(username, claims);
            
            System.out.println("Generated JWT: " + jwt);

            ResponseCookie jwtCookie = jwtUtils.createJwtCookie(jwt);
            httpresponse.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            
            Map<String, Object> response = new LinkedHashMap<>();
//            Map<String, String> response = new HashMap<>();
          //  response.put("jwt", jwt);
           
            response.put("EmpName", employee.getEmpName());
            response.put("Designation", employee.getDesignation());
            response.put("role", role);          
            response.put("locationName", employee.getLocName());
            response.put("locCode", employee.getLocCode());

            
           
           

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("LDAP Authentication failed for username: " + username + ". Possible reasons: Incorrect username or password.", e);
            return ResponseEntity.status(401).body("LDAP Authentication failed: Invalid credentials");
        } catch (CommunicationException e) {
            logger.error("CommunicationException: Unable to connect to LDAP server. Possible reasons: Network issues, incorrect server URL, or server downtime.", e);
            return ResponseEntity.status(500).body("Unable to connect to LDAP server");
        } catch (NamingException e) {
            logger.error("NamingException: LDAP authentication failed for username: " + username + ". Possible reasons: Configuration issues or invalid settings.", e);
            return ResponseEntity.status(401).body("LDAP Connection failed");
        }
    }


    public String appendZeros(String id) {
        while (id.length() < 8) {
            id = "0" + id;
        }
        return id;
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
    
    public boolean validateJwtToken(String jwt) {
        return jwtUtils.validateJwtToken(jwt);
    }


    
    public ResponseEntity<ProfileResponse> getEmployeeProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empCode = authentication.getName();  // Assuming empCode is the username

        Optional<MstEmployee> employeeOptional = employeeRepository.findByEmpCode(Integer.parseInt(empCode));

        if (employeeOptional.isPresent()) {
            MstEmployee employee = employeeOptional.get();
            ProfileResponse profile = new ProfileResponse();
            profile.setName(employee.getEmpName());
            profile.setDesignation(employee.getDesignation());
            profile.setLocName(employee.getLocName());

            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	public List<String> getEmployeesByLoc(String loc) {
		
		return employeeRepository.findEmployeeNamesByLocName(loc);
	}

	

}

