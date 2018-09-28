package top.daoyang.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.daoyang.demo.security.JwtAuthenticationFilter;
import top.daoyang.demo.security.UnAuthenticationEntryPoint;
import top.daoyang.demo.security.UserPrincipal;
import top.daoyang.demo.security.WXAuthenticationFilter;
import top.daoyang.demo.service.UserService;
import java.util.Optional;

@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserService userService;

    @Autowired
    private UnAuthenticationEntryPoint unAuthenticationEntryPoint;

    @Autowired
    private WXAuthenticationFilter wxAuthenticationFilter;

//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/auth/login", "/auth/sms",  "/auth/register", "/user/exist/**")
                    .permitAll()
                .antMatchers("/products/**", "/order/pay/**", "/order/alipay/notify", "/wx/login","/wx/grant", "/index")
                    .permitAll()
                .antMatchers("/category/**")
                    .permitAll()
                .antMatchers("/comment/**")
                    .permitAll()
                .antMatchers("/user/**").authenticated()
                .anyRequest()
                    .authenticated()
                        .and()
                .addFilterBefore(wxAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return s -> {

            Optional<top.daoyang.demo.entity.User> oUser = Optional.ofNullable(userService.findUserByUsername(s));
        oUser.orElseThrow(() -> new UsernameNotFoundException("The email and password you entered did not match our records."));

        return UserPrincipal.create(oUser.get());
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
