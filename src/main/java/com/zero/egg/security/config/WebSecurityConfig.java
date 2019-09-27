package com.zero.egg.security.config;

import com.zero.egg.security.filter.CompanyAuthenticationProcessingFilter;
import com.zero.egg.security.filter.JWTAuthenticationFilter;
import com.zero.egg.security.filter.NormAuthenticationProcessingFilter;
import com.zero.egg.security.handler.CompanyAuthenticationSuccessHandler;
import com.zero.egg.security.handler.MyAccessDeniedHandler;
import com.zero.egg.security.handler.MyAuthenticationEntryPointHandler;
import com.zero.egg.security.handler.MyAuthenticationFailHandler;
import com.zero.egg.security.handler.MyLogoutSuccessHandler;
import com.zero.egg.security.handler.NormalAuthenticationSuccessHandler;
import com.zero.egg.security.provider.CompanyUserDaoAuthenticationProvider;
import com.zero.egg.security.provider.NormDaoAuthenticationProvider;
import com.zero.egg.security.service.CompanyUserDetailsServiceImpl;
import com.zero.egg.security.service.NomalUserDetailsServiceImpl;
import com.zero.egg.tool.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @ClassName WebSecurityConfig
 * @Description TODO
 * @Author lyming
 * @Date 2019/9/26 5:49 上午
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    private MyAuthenticationEntryPointHandler myAuthEntryPointHandler;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyAuthenticationFailHandler myLoginFailHandler;
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;
    @Autowired
    private NormDaoAuthenticationProvider normDaoAuthenticationProvider;
    @Autowired
    private NomalUserDetailsServiceImpl nomalUserDetailsService;
    @Autowired
    private NormalAuthenticationSuccessHandler normalAuthenticationSuccessHandler;
    @Autowired
    private CompanyUserDetailsServiceImpl companyUserDetailsService;
    @Autowired
    private CompanyUserDaoAuthenticationProvider companyUserDaoAuthenticationProvider;
    @Autowired
    private CompanyAuthenticationSuccessHandler companyAuthenticationSuccessHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 普通登录(PC端,企业用户端)
         */
        auth
            .authenticationProvider(normDaoAuthenticationProvider)
            .userDetailsService(nomalUserDetailsService);
        /**
         * PC企业账户
         */
        auth
            .authenticationProvider(companyUserDaoAuthenticationProvider)
            .userDetailsService(companyUserDetailsService);
        /**
         * 微信绑定登录(老板移动端,员工移动端,设备端)
         */
//        auth
//            .authenticationProvider(wxbindAuthenticationProvider())
//            .userDetailsService(wxbindUserDetailsService);
//        /**
//         * 纯微信端(订货平台)
//         */
//        auth
//            .authenticationProvider(wxAuthenticationProvider())
//            .userDetailsService(wxUserDetailsService);
//        /**
//         * Saas平台端(只有一个固定账号,后面可以改成内存账号)
//         */
//        auth
//            .authenticationProvider(saasAuthenticationProvider())
//            .userDetailsService(saasUserDetailsService);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "swagger-ui.html",
                "**/swagger-ui.html",
                "/favicon.ico",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.gif",
                "/swagger-resources/**",
                "/v2/**",
                "/**/*.ttf"
        );
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html"
        );
//        web.ignoring().antMatchers("/index" ,
//                "/security/noauth" ,
//                "/swagger/**,/webjars/**,/swagger-resources/**,/v2/api-docs,/**/*.html",
//                "/exception/**" ,
//                "/lombok",
//                "/api/**",
//                "/jpa/**",
//                "/upload/upload/**",
//                "/wxLogin",
//                "/wxOrderLogin",
//                "/system/login",
//                "/system/saaslogin"
//        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //添加JWT过滤器 没有过滤的请求都需经过此过滤器
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        // 关闭跨站请求防护
        http.csrf().disable()
                //基于token，所以不需要session;
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(TokenUtils.antMatchers.split(",")).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthEntryPointHandler)
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .logout()
                //退出系统url
                .logoutUrl("/system/logout")
                //退出系统后的url跳转
                .logoutSuccessUrl("/")
                //退出系统后的 业务处理
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .permitAll()
                .invalidateHttpSession(true);
    }

    /**
     * 普通登录过滤器(PC端)
     * @return
     */
    @Bean
    public NormAuthenticationProcessingFilter normAuthenticationProcessingFilter() throws Exception{
        NormAuthenticationProcessingFilter filter = new NormAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(normalAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myLoginFailHandler);
        return filter;
    }
    /**
     * 普通登录过滤器(企业账号)
     * @return
     */
    @Bean
    public CompanyAuthenticationProcessingFilter companyAuthenticationProcessingFilter() throws Exception{
        CompanyAuthenticationProcessingFilter filter = new CompanyAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(companyAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myLoginFailHandler);
        return filter;
    }

    /**
     * 微信绑定登录过滤器(老板移动端,员工移动端,设备端)
     * @return
     */
//    @Bean
//    public WxbindAuthenticationProcessingFilter wxbindAuthenticationProcessingFilter() throws Exception{
//        WxbindAuthenticationProcessingFilter filter = new WxbindAuthenticationProcessingFilter();
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setAuthenticationFailureHandler(myLoginFailHandler);
//        return filter;
//    }
    /**
     * 纯微信端过滤器(订货平台)
     * @return
     */
//    @Bean
//    public WxAuthenticationProcessingFilter wxAuthenticationProcessingFilter() throws Exception{
//        WxAuthenticationProcessingFilter filter = new WxAuthenticationProcessingFilter();
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setAuthenticationFailureHandler(wxAuthenticationFailureHandler);
//        return filter;
//    }
    /**
     * Saas平台端过滤器
     * @return
     */
//    @Bean
//    public SaasAuthenticationProcessingFilter saasAuthenticationProcessingFilter() throws Exception{
//        SaasAuthenticationProcessingFilter filter = new SaasAuthenticationProcessingFilter();
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setAuthenticationFailureHandler(wxAuthenticationFailureHandler);
//        return filter;
//    }
}
