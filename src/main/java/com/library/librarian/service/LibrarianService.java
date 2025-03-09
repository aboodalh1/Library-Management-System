package com.library.librarian.service;
import com.library.config.JwtService;
import com.library.config.RateLimiterConfig;
import com.library.librarian.model.Librarian;
import com.library.librarian.repository.LibrarianRepository;
import com.library.librarian.request.AuthenticationRequest;
import com.library.librarian.request.ChangePasswordRequest;
import com.library.librarian.request.LibrarianRegisterRequest;
import com.library.librarian.response.LibrarianAuthResponse;
import com.library.utils.exceptions.TooManyRequestException;
import com.library.utils.exceptions.InvalidCredentialsException;
import com.library.utils.exceptions.RequestNotValidException;
import com.library.utils.mapper.ClassMapper;
import com.library.utils.validator.ObjectValidator;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class LibrarianService implements UserDetailsService {


    private final PasswordEncoder passwordEncoder;
    private final LibrarianRepository librarianRepository;
    private static final String LOGIN_RATE_LIMITER = "loginRateLimiter";
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RateLimiterConfig rateLimiterConfig;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Autowired
    public LibrarianService(PasswordEncoder passwordEncoder, LibrarianRepository librarianRepository, JwtService jwtService, AuthenticationManager authenticationManager, RateLimiterConfig rateLimiterConfig, RateLimiterRegistry rateLimiterRegistry, ObjectValidator<LibrarianRegisterRequest> registerRequestValidator, ObjectValidator<AuthenticationRequest> authenticationRequestValidator, ObjectValidator<AuthenticationRequest> authenticationRequestObjectValidator) {
        this.passwordEncoder = passwordEncoder;
        this.librarianRepository = librarianRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.rateLimiterConfig = rateLimiterConfig;
        this.rateLimiterRegistry = rateLimiterRegistry;
        this.registerRequestValidator = registerRequestValidator;
        this.authenticationRequestObjectValidator = authenticationRequestObjectValidator;
    }

    private final ObjectValidator<LibrarianRegisterRequest> registerRequestValidator;
    private final ObjectValidator<AuthenticationRequest> authenticationRequestObjectValidator;

    @Transactional
    public LibrarianAuthResponse librarianRegister(LibrarianRegisterRequest request) {
        Librarian existedEmail = librarianRepository.findByEmail(request.getEmail()).orElse(null);
        if (existedEmail != null) {
            throw new RequestNotValidException("Email already exists");
        }
        Librarian user = ClassMapper.INSTANCE.librarianDtoToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        librarianRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        LibrarianAuthResponse response = ClassMapper.INSTANCE.entityToDto(user);

        response.setToken(jwtToken);
        return response;
    }

    @Transactional
    public LibrarianAuthResponse librarianLogin(AuthenticationRequest request, HttpServletRequest httpServletRequest) {

        String userIp = httpServletRequest.getRemoteAddr();
        if (rateLimiterConfig.getBlockedIPs().contains(userIp)) {
            throw new TooManyRequestException("Rate limit reached for login attemps. try again later.");
        }

        String rateLimiterKey = LOGIN_RATE_LIMITER + "-" + userIp;
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterKey);

        if (rateLimiter.acquirePermission()) {
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        ));
            } catch (AuthenticationException exception) {
                throw new InvalidCredentialsException("invalid email or password");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            var user = librarianRepository.findByEmail(request.getEmail()).orElseThrow(
                    () -> new RequestNotValidException("email not found")
            );
            var jwtToken = jwtService.generateToken(user);
            LibrarianAuthResponse response = ClassMapper.INSTANCE.entityToDto(user);
            response.setToken(jwtToken);
            return response;
        }
        else
            rateLimiterConfig.blockIP(userIp);
        throw new TooManyRequestException("Rate limit reached for login attemps. try again later.");
    }


    @Transactional
    public void changePassword(
            ChangePasswordRequest request, Principal connectedUser){

        var user  = (Librarian) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new RequestNotValidException("Wrong password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RequestNotValidException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        librarianRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
